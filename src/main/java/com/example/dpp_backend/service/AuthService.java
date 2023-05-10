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
        User user = new User();
        user.fromRegister(register);
        userRepository.save(user);
        return true;
    }

    public UserDetailsDTO loginUser(LoginDTO login) {
        User user = userRepository.findByEmail(login.getEmail()).orElse(null);
        if (user == null) {
            log.error("User not found");
            return null;
        }
        if (!user.checkPassword(login.getPassword())) {
            log.error("Wrong password");
            return null;
        }
        return new UserDetailsDTO(user);
    }
}
