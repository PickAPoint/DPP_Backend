package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.UserDetailsDTO;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*") // NOSONAR
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public List<UserDetailsDTO> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PostMapping("/validate/{id}")
    public boolean validateUser(@PathVariable int id) {
        return adminService.validateUser(id);
    }

    @DeleteMapping("/users/{id}")
    public boolean deleteUser(@PathVariable int id) {
        return adminService.deleteUser(id);
    }

    @GetMapping("/packages")
    public List<Package> getAllPackages() {
        return adminService.getAllPackages();
    }
}
