package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.RegisterDTO;
import com.example.dpp_backend.model.User;
import com.example.dpp_backend.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.*;

// integration tests with RestAssured and hamcrest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIT {

    @LocalServerPort
    private int port;
    private RegisterDTO register;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        RestAssured.port = port;

        register = new RegisterDTO();
        register.setEmail("test@test.com");
        register.setPassword("test");
        register.setName("name");
        register.setAddress("address");
        register.setContact("contact");
    }

    @DisplayName("Register user with existing email")
    @Test
    void testRegisterUserInvalid() {

        User user = new User(register);
        userRepository.save(user);

        RestAssured.given()
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
    void testRegisterUserSuccess() {

        RestAssured.given()
                .contentType("application/json")
                .body(register)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body(equalTo("true"));
    }

}
