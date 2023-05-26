package com.example.dpp_backend.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TokenGeneratorTest {

    private TokenGenerator tokenGenerator = new TokenGenerator();

    @DisplayName("Test token generator")
    @Test
    void testTokenGenerator() {

        TokenGenerator tokenGenerator = new TokenGenerator();

        String token = tokenGenerator.generate();

        assertThat(token, notNullValue());
        assertThat(token.length(), is(6));
    }    
}
