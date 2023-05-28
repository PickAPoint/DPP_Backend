package com.example.dpp_backend.service;

import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.State;
import com.example.dpp_backend.model.UpdatePackageDTO;
import com.example.dpp_backend.model.User;
import com.example.dpp_backend.model.UserDetailsDTO;
import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.repository.UserRepository;
import com.example.dpp_backend.utils.MessageSender;
import com.example.dpp_backend.utils.TokenGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final MessageSender messageSender;
    private final TokenGenerator tokenGenerator;

    public List<UserDetailsDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDetailsDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("Admin")) {
                continue;
            }
            usersDTO.add(new UserDetailsDTO(user));
        }
        return usersDTO;
    }

    public boolean validateUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.info("User not found");
            return false;
        }
        if (!user.getType().equals("Pending")) {
            log.info("User is not pending");
            return false;
        }
        user.setType("Partner");
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.info("User not found");
            return false;
        }
        if (user.getType().equals("Admin")) {
            log.info("Cannot delete admin");
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    public Package getPackageById(int packageId) {
        return packageRepository.findById(packageId).orElse(null);
    }

    public boolean updatePackage(UpdatePackageDTO updateDTO) {
        Package pkg = packageRepository.findById(updateDTO.getPackageId()).orElse(null);
        if (pkg == null) {
            log.error("Package not found");
            return false;
        }

        if (updateDTO.getNewState().equals("Delivered") && pkg.getToken() == null) {

            //generate token
            String token = tokenGenerator.generate();
            log.info("Token generated: {}", token);
            pkg.setToken(token);

            //send message
            if (pkg.getClient().getContact() != null && !pkg.getClient().getContact().equals("")) {
                String message = String.format("Your package with id %d has been delivered. Use this code to collect it: %s", pkg.getId(), token);
                messageSender.send(message, pkg.getClient().getContact());
            }
        }
        else {  

            if (updateDTO.getToken() != null && !updateDTO.getToken().equals(pkg.getToken())) {
                log.error("Pick up code is not correct");
                return false;
            }
        }
        
        State state = new State();
        state.setOrderState(updateDTO.getNewState());
        state.setOrderDate(new Date());
        pkg.getStates().add(state);
        pkg.setOrderState(updateDTO.getNewState());
        packageRepository.save(pkg);

        return true;
    }
}
