package com.example.dpp_backend.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
class UserTest {

    @DisplayName("Test password check")
    @Test
    void testPasswordCheck() {
        User user = new User();
        user.setPassword("password");
        assertThat(user.checkPassword("password"), is(true));
    }


}