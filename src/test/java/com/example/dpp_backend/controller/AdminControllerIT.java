package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.User;
import com.example.dpp_backend.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.*;

// integration tests with RestAssured and hamcrest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private User user1;

    @BeforeEach
    void setUp() {

        RestAssured.port = port;

        user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@test.com");
        user1.setPassword("test");
        user1.setType("Pending");
    }


    @DisplayName("Get all users")
    @Test
    void testGetAllUsers() {

        userRepository.flush();
        userRepository.deleteAll();
        userRepository.save(user1);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/admin/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].email", equalTo("user1@test.com"));
    }

    @DisplayName("Validate user (valid)")
    @Test
    void testValidateUserValid() {

        User user2 = new User();
        user2.setId(2);
        user2.setType("Pending");
        userRepository.save(user2);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .post("/admin/validate/2")
                .then()
                .statusCode(200);
    }
}
