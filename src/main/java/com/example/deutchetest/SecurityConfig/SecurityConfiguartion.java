package com.example.deutchetest.SecurityConfig;

import com.example.deutchetest.Config.CustomAuthenticationProvider;
import com.example.deutchetest.Config.CustomWebAuthenticationDetailSource;
import com.example.deutchetest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ComponentScan(basePackages = "com.example.deutchetest")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true)
public class SecurityConfiguartion extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomAuthenticationProvider daoAuthenticationProvider;
    @Autowired
    private CustomWebAuthenticationDetailSource authenticationDetailsSource;



    @Override
    public void configure(WebSecurity web)  {
        web
                .ignoring()
                .antMatchers("/resources/**","/static/**","/css/**","/js/**","/img/**","/vendor/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/getsignup","/postsignup","/login","/index", "/gra" , "/receiptform", "/searchprescription" , "/otp").permitAll()
                .antMatchers("/shivamdash").hasAuthority("user")
                .antMatchers("/Manufacturedash","/manfinvent","/addinvent").hasAuthority("manufacture")
                .antMatchers("/rinventory").hasAuthority("retailer")
                .antMatchers("/getshipments").hasAuthority("shipment")
                .antMatchers("/get/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .authenticationDetailsSource(authenticationDetailsSource)
                .loginPage("/login").successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        for (GrantedAuthority auth : authentication.getAuthorities()) {

                            if("admin".equals(auth.getAuthority())) {
                                response.sendRedirect("/home");
                            }

                            else if("superAdmin".equals(auth.getAuthority())) {
                                response.sendRedirect("/home");
                            }

                            else if ("user".equals(auth.getAuthority())) {
                                response.sendRedirect("/home");
                            }

                            else if ("shipment".equals(auth.getAuthority())) {
                                response.sendRedirect("/shipmentdash");
                            }
                            else if ("hospital".equals(auth.getAuthority())) {
                                response.sendRedirect("/receiptform");
                            }


                            else {
                                response.sendRedirect("/getsignup");
                            }
                        }
                    }
                }).failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        if(e.getMessage().equals("Wrong Security Answer")){
                            httpServletResponse.sendRedirect("/login?loginError=true");
                        }
                        else {
                                httpServletResponse.sendRedirect("/login?error=true");
                            }
                    }
                })
                .and().logout().permitAll();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() throws Exception {
        CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authProvider.afterPropertiesSet();
        authProvider.setUserDetailsService(userService);

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }


    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
