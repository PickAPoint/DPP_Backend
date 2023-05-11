package com.example.dpp_backend.service;

import com.example.dpp_backend.model.User;
import com.example.dpp_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

// unit tests with Mockito and Hamcrest
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    private AdminService adminService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(userRepository);

        user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setPassword("test");
        user1.setType("Pending");

        user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setPassword("test");
        user2.setType("Pending");
    }

    @DisplayName("Get all users")
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(java.util.Arrays.asList(user1, user2));

        assertThat(adminService.getAllUsers(), hasSize(2));
    }

}