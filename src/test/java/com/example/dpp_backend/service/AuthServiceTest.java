package com.example.dpp_backend.service;


import com.example.dpp_backend.model.LoginDTO;
import com.example.dpp_backend.model.RegisterDTO;
import com.example.dpp_backend.model.User;
import com.example.dpp_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

// unit tests with Mockito and Hamcrest
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository);
    }

    @DisplayName("Register user with existing email")
    @Test
    void testRegisterUserError() {

        RegisterDTO register = new RegisterDTO();
        register.setEmail("test1@test.com");
        register.setPassword("test");
        User test1 = new User();
        test1.fromRegister(register);

        when(userRepository.findByEmail("test1@test.com")).thenReturn(Optional.of(test1));

        assertThat(authService.registerUser(register), is(false));
    }


    @DisplayName("Register user with valid email")
    @Test
    void testRegisterUserValid() {

        RegisterDTO register = new RegisterDTO();
        register.setEmail("test2@test.com");
        register.setPassword("test");

        when(userRepository.findByEmail("test2@test.com")).thenReturn(Optional.empty());

        assertThat(authService.registerUser(register), is(true));
    }


    @DisplayName("Login user with valid email and password")
    @Test
    void testLoginUserValid() {

        LoginDTO login = new LoginDTO();
        login.setEmail("test@test.com");
        login.setPassword("test");
        User test = new User();
        test.setEmail(login.getEmail());
        test.setPassword(login.getPassword());

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(test));

        assertThat(authService.loginUser(login), not(nullValue()));
    }

    @DisplayName("Login user with invalid password")
    @Test
    void testLoginUserInvalid() {

        LoginDTO login = new LoginDTO();
        login.setEmail("test@test.com");
        login.setPassword("test");
        User test = new User();
        test.setEmail(login.getEmail());
        test.setPassword("wrong");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(test));

        assertThat(authService.loginUser(login), is(nullValue()));
    }

    @DisplayName("Login user with invalid email")
    @Test
    void testLoginUserInvalidEmail() {

        LoginDTO login = new LoginDTO();
        login.setEmail("invalid");
        login.setPassword("test");

        assertThat(authService.loginUser(login), is(nullValue()));
    }
}