package com.example.dpp_backend.service;

import com.example.dpp_backend.model.User;
import com.example.dpp_backend.model.UserDetailsDTO;
import com.example.dpp_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public List<UserDetailsDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDetailsDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("admin")) {
                continue;
            }
            usersDTO.add(new UserDetailsDTO(user));
        }
        return usersDTO;
    }
}
