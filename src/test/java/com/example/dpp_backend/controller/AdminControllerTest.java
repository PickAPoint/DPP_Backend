package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.UserDetailsDTO;
import com.example.dpp_backend.service.AdminService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

// unit tests with WebMvcTest, Mockito, RestAssured and hamcrest
@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    private UserDetailsDTO userDetails;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        userDetails = new UserDetailsDTO();
        userDetails.setEmail("test@test.com");
        userDetails.setName("name");
        userDetails.setAddress("address");
        userDetails.setType("Pending");
    }

    @DisplayName("Get all users")
    @Test
    void testGetAllUsers() {
        when(adminService.getAllUsers()).thenReturn(List.of(userDetails));

        RestAssuredMockMvc.given()
                .when()
                .get("/admin/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        verify(adminService, times(1)).getAllUsers();
    }

    @DisplayName("Validate user (valid)")
    @Test
    void testValidateUserValid() {
        when(adminService.validateUser(anyInt())).thenReturn(true);

        RestAssuredMockMvc.given()
                .when()
                .post("/admin/validate/1")
                .then()
                .statusCode(200)
                .body(is("true"));

        verify(adminService, times(1)).validateUser(anyInt());
    }

    @DisplayName("Delete user (valid)")
    @Test
    void testDeleteUserValid() {
        when(adminService.deleteUser(anyInt())).thenReturn(true);

        RestAssuredMockMvc.given()
                .when()
                .delete("/admin/users/1")
                .then()
                .statusCode(200)
                .body(is("true"));

        verify(adminService, times(1)).deleteUser(anyInt());
    }

    @DisplayName("Get all packages")
    @Test
    void testGetAllPackages() {

        Package test1 = new Package();
        test1.setEStore("PrintPlate");
        Package test2 = new Package();
        test2.setEStore("PrintPlate");

        when(adminService.getAllPackages()).thenReturn(List.of(test1, test2));

        RestAssuredMockMvc.given()
                .when()
                .get("/admin/packages")
                .then()
                .statusCode(200)
                .body("$", hasSize(2));
    }

}