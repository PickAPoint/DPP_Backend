package com.example.dpp_backend.controller;


import com.example.dpp_backend.model.RegisterDTO;
import com.example.dpp_backend.service.AuthService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

// unit tests with WebMvcTest, Mockito, RestAssured and hamcrest
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private RegisterDTO register;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        register = new RegisterDTO();
        register.setEmail("test@test.com");
        register.setPassword("test");
        register.setName("name");
        register.setAddress("address");
        register.setContact("contact");
    }

    @DisplayName("Register user with existing email")
    @Test
    void testRegisterUser() {
        when(authService.registerUser(any())).thenReturn(false);

        RestAssuredMockMvc.given()
                        .contentType("application/json")
                        .body(register)
                        .when()
                        .post("/auth/register")
                        .then()
                        .statusCode(200)
                        .body(equalTo("false"));
    }

    @DisplayName("Register user with new email")
    @Test
    void testRegisterUser2() {
        when(authService.registerUser(any())).thenReturn(true);

        RestAssuredMockMvc.given()
                        .contentType("application/json")
                        .body(register)
                        .when()
                        .post("/auth/register")
                        .then()
                        .statusCode(200)
                        .body(equalTo("true"));
    }

}