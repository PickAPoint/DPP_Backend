package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.Client;
import com.example.dpp_backend.model.OrderDTO;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.State;
import com.example.dpp_backend.service.EStoreService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

// unit tests with WebMvcTest, Mockito, RestAssured and hamcrest
@WebMvcTest(EStoreController.class)
@AutoConfigureMockMvc(addFilters = false)
class EStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EStoreService eStoreService;

    private Client client1;
    private State state1;
    private Package pkg1;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

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

        orderDTO = new OrderDTO();
        orderDTO.setEStore("PrintPlate");
        orderDTO.setEmail("user@test.com");
    }

    @DisplayName("Add package from Order")
    @Test
    void testAddPackage() {
        when(eStoreService.addNewOrder(any())).thenReturn(pkg1.getId());

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(orderDTO)
                .when()
                .post("/eStore/order")
                .then()
                .statusCode(200)
                .body(equalTo("1"));
    }


}