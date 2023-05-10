package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.LoginDTO;
import com.example.dpp_backend.model.RegisterDTO;
import com.example.dpp_backend.model.UserDetailsDTO;
import com.example.dpp_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*") // NOSONAR
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerUser(
            @RequestBody RegisterDTO register
    ) {
        log.info("Registering user {}", register.getEmail());
        boolean success = authService.registerUser(register);
        if (!success) {
            log.error("Error registering user {}", register.getEmail());
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDetailsDTO> loginUser(
            @RequestBody LoginDTO login
    ) {
        log.info("Logging in user {}", login.getEmail());
        UserDetailsDTO userDetails = authService.loginUser(login);
        if (userDetails == null) {
            log.error("Error logging in user {}", login.getEmail());
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(userDetails);
    }
}
