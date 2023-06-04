package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.Client;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.State;
import com.example.dpp_backend.model.UpdatePackageDTO;
import com.example.dpp_backend.repository.ClientRepository;
import com.example.dpp_backend.repository.PackageRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.hamcrest.Matchers.*;

// integration tests with RestAssured and hamcrest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PartnerControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private ClientRepository clientRepository;


    private Client client1;
    private Package pkg1;
    private State state1;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        clientRepository.flush();
        packageRepository.flush();

        client1 = new Client();
        client1.setEmail("client1@test.com");
        client1.setId(1);

        state1 = new State();
        state1.setOrderState("OrderPlaced");
        state1.setOrderDate(new Date());

        pkg1 = new Package();
        pkg1.setId(1);
        pkg1.setEStore("PrintPlate");
        pkg1.setClient(client1);
        pkg1.setOrderState(state1.getOrderState());
        pkg1.getStates().add(state1);
        pkg1.setPickUpId(1);

        clientRepository.save(client1);
        packageRepository.save(pkg1);

    }


    @DisplayName("Get all packages for a partner")
    @Test
    void testGetAllPackages(){

        RestAssured.given()
                .when()
                .get("/partner/packages?partnerId=1")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].estore", equalTo(pkg1.getEStore()))
                .body("[0].client.email", equalTo(pkg1.getClient().getEmail()))
                .body("[0].orderState", equalTo(pkg1.getOrderState()))
                .body("[0].states.size()", is(1))
                .body("[0].states[0].orderState", equalTo(pkg1.getStates().get(0).getOrderState()));
    }

    @DisplayName("Get package by id (valid id)")
    @Test
    void testGetPackageByValidId() {

        packageRepository.flush();
        packageRepository.deleteAll();
        Package p = packageRepository.save(pkg1);

        RestAssured.given()
                .when()
                .get("/partner/package/" + p.getId())
                .then()
                .statusCode(200)
                .body("estore", equalTo(pkg1.getEStore()))
                .body("client.email", equalTo(pkg1.getClient().getEmail()))
                .body("orderState", equalTo(pkg1.getOrderState()))
                .body("states.size()", is(1))
                .body("states[0].orderState", equalTo(pkg1.getStates().get(0).getOrderState()));
    }

    @DisplayName("Get package by id (invalid id)")
    @Test
    void testGetPackageByInvalidId(){

        RestAssured.given()
                .when()
                .get("/partner/packages/2")
                .then()
                .statusCode(404);
    }

    @DisplayName("Update package state (success)")
    @Test
    void testUpdatePackageStateSuccess() {
        packageRepository.flush();
        packageRepository.deleteAll();
        Package p = packageRepository.save(pkg1);

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(p.getId());
        updatePackageDTO.setNewState("Delivered");

        RestAssured.given()
                .contentType("application/json")
                .body(updatePackageDTO)
                .when()
                .put("/partner/package")
                .then()
                .statusCode(200)
                .body(equalTo("true"));
    }

    @DisplayName("Update package state with invalid state")
    @Test
    void testUpdatePackageStateInvalidState() {
        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Cancelled");

        RestAssured.given()
                .contentType("application/json")
                .body(updatePackageDTO)
                .when()
                .put("/partner/package")
                .then()
                .statusCode(200)
                .body(equalTo("false"));
    }

    @DisplayName("Update package state with invalid id")
    @Test
    void testUpdatePackageStateInvalidId() {
        packageRepository.flush();
        packageRepository.deleteAll();
        Package p = packageRepository.save(pkg1);
        
        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(p.getId() + 1);
        updatePackageDTO.setNewState("Delivered");

        RestAssured.given()
                .contentType("application/json")
                .body(updatePackageDTO)
                .when()
                .put("/partner/package")
                .then()
                .statusCode(200)
                .body(equalTo("false"));
    }

    @DisplayName("Update package state when cancelled")
    @Test
    void testUpdatePackageStateWhenCancelled() {
        packageRepository.flush();
        packageRepository.deleteAll();
        pkg1.setOrderState("Cancelled");
        Package p = packageRepository.save(pkg1);

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(p.getId());
        updatePackageDTO.setNewState("Delivered");

        RestAssured.given()
                .contentType("application/json")
                .body(updatePackageDTO)
                .when()
                .put("/partner/package")
                .then()
                .statusCode(200)
                .body(equalTo("false"));
    }

    @DisplayName("Update package state to Collected using invalid token")
    @Test
    void testUpdatePackageStateInvalidToken() {
        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(2);
        updatePackageDTO.setNewState("Collected");
        updatePackageDTO.setToken("------");

        RestAssured.given()
                .contentType("application/json")
                .body(updatePackageDTO)
                .when()
                .put("/partner/package")
                .then()
                .statusCode(200)
                .body(equalTo("false"));
    }

}
