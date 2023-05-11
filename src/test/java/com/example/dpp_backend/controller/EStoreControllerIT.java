package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.*;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.repository.ClientRepository;
import com.example.dpp_backend.repository.PackageRepository;
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
class EStoreControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client client1;
    private Package pkg1;
    private State state1;
    private OrderDTO orderDTO;

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

        clientRepository.save(client1);
        packageRepository.save(pkg1);

        orderDTO = new OrderDTO();
        orderDTO.setEStore("PrintPlate");
        orderDTO.setEmail("user@test.com");

    }

    @DisplayName("Add package from Order")
    @Test
    void testAddPackage() {
        RestAssured.given()
                .contentType("application/json")
                .body(orderDTO)
                .when()
                .post("/eStore/order")
                .then()
                .statusCode(200)
                .body(notNullValue());

        assert(packageRepository.findById(2).isPresent());
    }

    @DisplayName("Update package state (success)")
    @Test
    void testUpdatePackageStateSuccess() {
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        updateOrderDTO.setPackageId(1);
        updateOrderDTO.setNewState("Cancelled");

        RestAssured.given()
                .contentType("application/json")
                .body(updateOrderDTO)
                .when()
                .put("/eStore/order")
                .then()
                .statusCode(200)
                .body(equalTo("true"));
    }
}
