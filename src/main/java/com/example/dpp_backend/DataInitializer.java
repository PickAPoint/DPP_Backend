package com.example.dpp_backend;

import com.example.dpp_backend.model.User;
import com.example.dpp_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        log.info("Initializing data...");

        if (userRepository.findByEmail("admin@pap.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@pap.com");
            admin.setName("administrator");
            admin.setPassword("admin");
            admin.setType("Admin");
            userRepository.save(admin);
        }

        if (userRepository.findByEmail("functional@tests.com").isEmpty()) {
            User user = new User();
            user.setEmail("functional@tests.com");
            user.setName("Functional Tests");
            user.setPassword("tests");
            user.setType("Partner");
            userRepository.save(user);
            log.info("Data initialized");
        }
    }
}
