package com.example.deutchetest.Config;

import com.example.deutchetest.entity.User;
import com.example.deutchetest.repository.UserRepo;
import com.example.deutchetest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String verificationCode
                = ((CustomWebAuthenticationDetails) auth.getDetails())
                .getVerificationAnswer();
        String email = ((CustomWebAuthenticationDetails) auth.getDetails()).getEmail();
        User user = null;
        Optional<User> user1 = userRepository.findByEmail(email);
        if(user1.isPresent()){
            user = user1.get();
        }
        if ((user == null) || !auth.getCredentials().equals(user.getPassword()) ) {
            throw new BadCredentialsException("Invalid username or password ");
        }
        if(!user.getSecurityAnswer().equals(verificationCode)){
            throw new BadCredentialsException("Wrong Security Answer");
        }

        return new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), Arrays.stream(user.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    public CustomAuthenticationProvider() {
        super();
    }

@Override
protected void doAfterPropertiesSet()  {
    new UserService();
}
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userService);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}