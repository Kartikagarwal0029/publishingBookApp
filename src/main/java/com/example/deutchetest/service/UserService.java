package com.example.deutchetest.service;

import com.example.deutchetest.entity.User;
import com.example.deutchetest.repository.UserRepo;
import com.example.deutchetest.userDetail.MyUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return new MyUserDetail(user);
    }

    public User CreateUser(User user) {
        User newUser = null;
        Optional<User> userExist = userRepo.findByEmail(user.getEmail());
        if (!userExist.isPresent()) {
            newUser = userRepo.save(user);
        }
        return newUser;
    }

    public MyUserDetail getUser(String email) throws UsernameNotFoundException {
        User user=userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return new MyUserDetail(user);
    }
}
