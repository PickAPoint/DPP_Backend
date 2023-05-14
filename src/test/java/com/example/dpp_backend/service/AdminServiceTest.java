package com.example.dpp_backend.service;

import com.example.dpp_backend.model.User;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

// unit tests with Mockito and Hamcrest
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PackageRepository packageRepository;

    private AdminService adminService;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(userRepository, packageRepository);

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

}