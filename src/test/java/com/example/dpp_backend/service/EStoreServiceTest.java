package com.example.dpp_backend.service;

import com.example.dpp_backend.model.*;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.repository.ClientRepository;
import com.example.dpp_backend.repository.PackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

// unit tests with Mockito and Hamcrest
@ExtendWith(MockitoExtension.class)
class EStoreServiceTest {
    @Mock
    private PackageRepository packageRepository;

    @Mock
    private ClientRepository clientRepository;

    private EStoreService eStoreService;
    private Client client1;
    private State state1;
    private Package pkg1;

    @BeforeEach
    void setUp() {
        eStoreService = new EStoreService(clientRepository, packageRepository);

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
    }

    @DisplayName("Add package from Order")
    @Test
    void testAddPackage() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setEStore("PrintPlate");
        orderDTO.setEmail("user@test.com");

        when(clientRepository.save(any())).thenReturn(client1);
        when(packageRepository.save(any())).thenReturn(pkg1);

        int id = eStoreService.addNewOrder(orderDTO);
        assertThat(id, notNullValue());
    }

    @DisplayName("Update package state (success)")
    @Test
    void testUpdatePackageState() {
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        updateOrderDTO.setPackageId(1);
        updateOrderDTO.setNewState("Cancelled");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(pkg1));
        when(packageRepository.save(any())).thenReturn(pkg1);

        boolean success = eStoreService.updateOrder(updateOrderDTO);
        assertThat(success, is(true));
    }

    @DisplayName("Update package state (invalid id)")
    @Test
    void testUpdatePackageStateInvalidId() {
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        updateOrderDTO.setPackageId(2);
        updateOrderDTO.setNewState("Cancelled");

        when(packageRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(null));

        boolean success = eStoreService.updateOrder(updateOrderDTO);
        assertThat(success, is(false));
    }

    @DisplayName("Update package state (invalid new state)")
    @Test
    void testUpdatePackageStateInvalidNewState() {
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        updateOrderDTO.setPackageId(1);
        updateOrderDTO.setNewState("InvalidState");

        boolean success = eStoreService.updateOrder(updateOrderDTO);
        assertThat(success, is(false));
    }

    @DisplayName("Update package state (invalid state transition)")
    @Test
    void testUpdatePackageStateInvalidTransition() {
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        updateOrderDTO.setPackageId(1);
        updateOrderDTO.setNewState("Cancelled");

        pkg1.setOrderState("Cancelled");
        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(pkg1));

        boolean success = eStoreService.updateOrder(updateOrderDTO);
        assertThat(success, is(false));
    }


}