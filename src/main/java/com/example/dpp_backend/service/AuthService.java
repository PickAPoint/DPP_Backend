package com.example.dpp_backend.service;

import com.example.dpp_backend.model.LoginDTO;
import com.example.dpp_backend.model.RegisterDTO;
import com.example.dpp_backend.model.User;
import com.example.dpp_backend.model.UserDetailsDTO;
import com.example.dpp_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public boolean registerUser(RegisterDTO register) {
        if (userRepository.findByEmail(register.getEmail()).isPresent()) {
            log.error("User already exists");
            return false;
        }
        User user = new User(register);
        userRepository.save(user);
        return true;
    }
}
