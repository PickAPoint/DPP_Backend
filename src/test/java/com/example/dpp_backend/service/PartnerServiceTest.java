package com.example.dpp_backend.service;

import com.example.dpp_backend.model.Client;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.UpdatePackageDTO;
import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.utils.MessageSender;
import com.example.dpp_backend.utils.TokenGenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

// unit tests with Mockito and Hamcrest
@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {

    @InjectMocks
    private PartnerService partnerService;

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private MessageSender messageSender;


    @DisplayName("Get all packages for partner")
    @Test
    void testGetAllPackages() {

        Package test1 = new Package();
        test1.setEStore("PrintPlate");
        Package test2 = new Package();
        test2.setEStore("PrintPlate");

        when(packageRepository.findByPickUpId(1)).thenReturn(List.of(test1, test2));

        assertThat(partnerService.getAllPackages(1), hasSize(2));
    }


    @DisplayName("Get package by id (valid id)")
    @Test
    void testGetPackageByValidId() {

        Package test1 = new Package();
        test1.setEStore("PrintPlate");
        test1.setId(1);

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(test1));

        assertThat(partnerService.getPackageById(1), is(test1));
    }


    @DisplayName("Get package by id (invalid id)")
    @Test
    void testGetPackageByInvalidId(){
        when(packageRepository.findById(1)).thenReturn(java.util.Optional.empty());
        assertThat(partnerService.getPackageById(1), is(nullValue()));
    }


    @DisplayName("Update package state to delivered")
    @Test
    void testUpdatePackageStateToDelivered() {
        Client client = new Client();
        client.setContact("123456789");

        Package pkg = new Package();
        pkg.setEStore("PrintPlate");
        pkg.setId(1);
        pkg.setOrderState("InTransit");
        pkg.setClient(client);

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Delivered");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(pkg));
        when(packageRepository.save(any())).thenReturn(pkg);
        when(tokenGenerator.generate()).thenReturn("123456");
        when(messageSender.send(anyString(), anyString())).thenReturn(true);

        boolean success = partnerService.updatePackage(updatePackageDTO);
        assertThat(success, is(true));
        assertEquals("Delivered", pkg.getOrderState());
        assertEquals("123456", pkg.getToken());
        verify(messageSender, times(1)).send(anyString(), anyString());
        verify(tokenGenerator, times(1)).generate();
    }


    @DisplayName("Update package state to delivered when client contact is null")
    @Test
    void testUpdatePackageStateToDeliveredWhenClientContactIsNull() {
        Client client = new Client();

        Package pkg = new Package();
        pkg.setEStore("PrintPlate");
        pkg.setId(1);
        pkg.setOrderState("InTransit");
        pkg.setClient(client);

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Delivered");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(pkg));
        when(packageRepository.save(any())).thenReturn(pkg);
        when(tokenGenerator.generate()).thenReturn("123456");

        boolean success = partnerService.updatePackage(updatePackageDTO);
        assertThat(success, is(true));
        assertEquals("Delivered", pkg.getOrderState());
        assertEquals("123456", pkg.getToken());
        verify(messageSender, times(0)).send(anyString(), anyString());
        verify(tokenGenerator, times(1)).generate();
    }


    @DisplayName("Update package state to delivered when client contact is empty")
    @Test
    void testUpdatePackageStateToDeliveredWhenClientContactIsEmpty() {
        Client client = new Client();
        client.setContact("");

        Package pkg = new Package();
        pkg.setEStore("PrintPlate");
        pkg.setId(1);
        pkg.setOrderState("InTransit");
        pkg.setClient(client);

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Delivered");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(pkg));
        when(packageRepository.save(any())).thenReturn(pkg);
        when(tokenGenerator.generate()).thenReturn("123456");

        boolean success = partnerService.updatePackage(updatePackageDTO);
        assertThat(success, is(true));
        assertEquals("Delivered", pkg.getOrderState());
        assertEquals("123456", pkg.getToken());
        verify(messageSender, times(0)).send(anyString(), anyString());
        verify(tokenGenerator, times(1)).generate();
    }


    @DisplayName("Update package state to collected using a valid token")
    @Test
    void testUpdatePackageStateToCollectedWithValidToken() {
        Package pkg = new Package();
        pkg.setEStore("PrintPlate");
        pkg.setId(1);
        pkg.setOrderState("Delivered");
        pkg.setToken("123456");

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Collected");
        updatePackageDTO.setToken("123456");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(pkg));
        when(packageRepository.save(any())).thenReturn(pkg);

        boolean success = partnerService.updatePackage(updatePackageDTO);
        assertThat(success, is(true));
        assertEquals("Collected", pkg.getOrderState());
    }


    @DisplayName("Update package state to collected using an invalid token")
    @Test
    void testUpdatePackageStateToCollectedWithInvalidToken() {
        Package pkg = new Package();
        pkg.setEStore("PrintPlate");
        pkg.setId(1);
        pkg.setOrderState("Delivered");
        pkg.setToken("123456");

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Collected");
        updatePackageDTO.setToken("1234567");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(pkg));

        boolean success = partnerService.updatePackage(updatePackageDTO);
        assertThat(success, is(false));
        assertEquals("Delivered", pkg.getOrderState());
    }


    @DisplayName("Update package state to an invalid state")
    @Test
    void testUpdatePackageStateToInvalidState() {
        Package pkg = new Package();
        pkg.setEStore("PrintPlate");
        pkg.setId(1);
        pkg.setOrderState("InTransit");

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Cancelled");

        boolean success = partnerService.updatePackage(updatePackageDTO);
        assertThat(success, is(false));
        assertEquals("InTransit", pkg.getOrderState());
    }


    @DisplayName("Update package state from a restricted state")
    @Test
    void testUpdatePackageStateFromRestrictedState() {
        Package pkg = new Package();
        pkg.setEStore("PrintPlate");
        pkg.setId(1);
        pkg.setOrderState("Cancelled");

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Delivered");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(pkg));

        boolean success = partnerService.updatePackage(updatePackageDTO);
        assertThat(success, is(false));
        assertEquals("Cancelled", pkg.getOrderState());
    }


    @DisplayName("Update package state with invalid id")
    @Test
    void testUpdatePackageStateWithInvalidId() {
        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Delivered");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.empty());

        boolean success = partnerService.updatePackage(updatePackageDTO);
        assertThat(success, is(false));
    }

}