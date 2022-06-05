package com.example.deutchetest.Config;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String securityAnswer;
    private String email;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        securityAnswer = request.getParameter("securityAnswer");
        email = request.getParameter("email");

    }

    public String getEmail() {
        return email;
    }

    public String getVerificationAnswer() {
        return securityAnswer;
    }
}
