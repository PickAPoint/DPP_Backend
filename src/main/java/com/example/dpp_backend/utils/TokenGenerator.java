package com.example.dpp_backend.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 6;
    private final SecureRandom rand = new SecureRandom();

    public String generate() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = rand.nextInt(CHARACTERS.length());
            char randChar = CHARACTERS.charAt(index);
            token.append(randChar);
        }

        return token.toString();
    }
}
