package com.example.dpp_backend.service;

import com.example.dpp_backend.model.User;
import com.example.dpp_backend.model.Client;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.UpdatePackageDTO;
import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.repository.UserRepository;
import com.example.dpp_backend.utils.MessageSender;
import com.example.dpp_backend.utils.TokenGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

// unit tests with Mockito and Hamcrest
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PackageRepository packageRepository;
    
    @Mock
    private TokenGenerator tokenGenerator;
    
    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private AdminService adminService;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(userRepository, packageRepository, messageSender, tokenGenerator);

        user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setPassword("test");
        user1.setType("Pending");

        user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setPassword("test");
        user2.setType("Admin");

        user3 = new User();
        user3.setEmail("user3@test.com");
        user3.setPassword("test");
        user3.setType("Partner");
    }

    @DisplayName("Get all users")
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(java.util.Arrays.asList(user1, user2, user3));

        assertThat(adminService.getAllUsers(), hasSize(2));
    }

    @DisplayName("Validate user (valid)")
    @Test
    void testValidateUserValid() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user1));

        assertThat(adminService.validateUser(1), is(true));
    }

    @DisplayName("Validate user (invalid id)")
    @Test
    void testValidateUserInvalidId() {
        assertThat(adminService.validateUser(1), is(false));
    }

    @DisplayName("Validate user (invalid type)")
    @Test
    void testValidateUserInvalidType() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user2));

        assertThat(adminService.validateUser(1), is(false));
    }

    @DisplayName("Delete user (valid)")
    @Test
    void testDeleteUserValid() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user1));

        assertThat(adminService.deleteUser(1), is(true));
    }

    @DisplayName("Delete user (invalid id)")
    @Test
    void testDeleteUserInvalidId() {
        assertThat(adminService.deleteUser(1), is(false));
    }

    @DisplayName("Delete user (invalid type)")
    @Test
    void testDeleteUserInvalidType() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user2));

        assertThat(adminService.deleteUser(1), is(false));
    }

    @DisplayName("Get all packages")
    @Test
    void testGetAllPackages() {

        Package test1 = new Package();
        test1.setEStore("PrintPlate");
        Package test2 = new Package();
        test2.setEStore("PrintPlate");

        when(packageRepository.findAll()).thenReturn(List.of(test1, test2));

        assertThat(adminService.getAllPackages(), hasSize(2));
    }

    @DisplayName("Get package by id (valid id)")
    @Test
    void testGetPackageByValidId() {

        Package test1 = new Package();
        test1.setEStore("PrintPlate");
        test1.setId(1);

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(test1));

        assertThat(adminService.getPackageById(1), is(test1));
    }

    @DisplayName("Get package by id (invalid id)")
    @Test
    void testGetPackageByInvalidId() {
        when(packageRepository.findById(1)).thenReturn(java.util.Optional.empty());
        assertThat(adminService.getPackageById(1), is(nullValue()));
    }

    @DisplayName("Update package (valid id)")
    @Test
    void testUpdatePackageValidId() {
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

        boolean success = adminService.updatePackage(updatePackageDTO);
        assertThat(success, is(true));
        assertEquals("Delivered", pkg.getOrderState());
        assertEquals("123456", pkg.getToken());
        verify(messageSender, times(1)).send(anyString(), anyString());
        verify(tokenGenerator, times(1)).generate();
    }

    @DisplayName("Update package (invalid id)")
    @Test
    void testUpdatePackageInvalidId() {
        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Delivered");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.empty());

        assertThat(adminService.updatePackage(updatePackageDTO), is(false));
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

        boolean success = adminService.updatePackage(updatePackageDTO);
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

        boolean success = adminService.updatePackage(updatePackageDTO);
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

        boolean success = adminService.updatePackage(updatePackageDTO);
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

        boolean success = adminService.updatePackage(updatePackageDTO);
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

        boolean success = adminService.updatePackage(updatePackageDTO);
        assertThat(success, is(false));
        assertEquals("InTransit", pkg.getOrderState());
    }

    @DisplayName("Update package state with invalid id")
    @Test
    void testUpdatePackageStateWithInvalidId() {
        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(1);
        updatePackageDTO.setNewState("Delivered");

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.empty());

        boolean success = adminService.updatePackage(updatePackageDTO);
        assertThat(success, is(false));
    }



}