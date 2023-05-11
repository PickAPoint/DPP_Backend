package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.service.PartnerService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

// unit tests with WebMvcTest, Mockito, RestAssured and hamcrest
@WebMvcTest(PartnerController.class)
@AutoConfigureMockMvc(addFilters = false)
class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    private Package pkg1;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        pkg1 = new Package();
        pkg1.setEStore("PrintPlate");
    }

    @DisplayName("Get all packages for partner")
    @Test
    void testGetAllPackages() {
        when(partnerService.getAllPackages(anyInt())).thenReturn(List.of(pkg1));

        RestAssuredMockMvc.given()
                .param("partnerId", 1)
                .when()
                .get("/partner/packages")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].estore", equalTo("PrintPlate"));
    }

    @DisplayName("Get package by id (valid id)")
    @Test
    void testGetPackageByValidId() {
        when(partnerService.getPackageById(anyInt())).thenReturn(pkg1);

        RestAssuredMockMvc.given()
                .when()
                .get("/partner/package/1")
                .then()
                .statusCode(200)
                .body("estore", equalTo("PrintPlate"));
    }

    @DisplayName("Get package by id (invalid id)")
    @Test
    void testGetPackageByInvalidId(){
        when(partnerService.getPackageById(anyInt())).thenReturn(null);

        RestAssuredMockMvc.given()
                .when()
                .get("/partner/package/1")
                .then()
                .statusCode(404);
    }

}