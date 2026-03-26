package com.example.Task_Manager;

import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    public String generateToken(String username) {
        return username + "_token";
    }

    public String validateToken(String token) {

        if (token != null && token.endsWith("_token")) {
            return token.replace("_token", "");
        }

        return null;
    }
}
