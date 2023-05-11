package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.Client;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.State;
import com.example.dpp_backend.repository.ClientRepository;
import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.repository.StateRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Date;

import static org.hamcrest.Matchers.*;

// integration tests with RestAssured and hamcrest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PartnerControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StateRepository stateRepository;

    private Client client1;
    private Package pkg1;
    private State state1;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        packageRepository.deleteAll();
        clientRepository.deleteAll();
        stateRepository.deleteAll();

        client1 = new Client();
        client1.setEmail("client1@test.com");
        client1.setId(1);

        state1 = new State();
        state1.setOrderState("OrderPlaced");
        state1.setOrderDate(new Date());

        pkg1 = new Package();
        pkg1.setEStore("PrintPlate");
        pkg1.setClient(client1);
        pkg1.setOrderState(state1.getOrderState());
        pkg1.getStates().add(state1);

    }

    @DisplayName("Get all packages for a partner")
    @Test
    void testGetAllPackages(){
        clientRepository.save(client1);
//        stateRepository.save(state1);
        packageRepository.save(pkg1);

        RestAssured.given()
                .contentType("application/json")
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
}
