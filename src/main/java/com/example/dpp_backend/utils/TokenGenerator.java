package com.example.dpp_backend.utils;

import java.util.Random;

public class TokenGenerator {

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int TOKEN_LENGTH = 6;
    private final Random rand = new Random();

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
