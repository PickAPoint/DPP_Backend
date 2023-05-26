package com.example.dpp_backend.service;

import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.utils.MessageSender;
import com.example.dpp_backend.utils.TokenGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.State;
import com.example.dpp_backend.model.UpdatePackageDTO;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PackageRepository packageRepository;
    private final MessageSender messageSender;
    private final TokenGenerator tokenGenerator;


    public List<Package> getAllPackages(int partnerId) {
        return packageRepository.findByPickUpId(partnerId);
    }


    public Package getPackageById(int packageId) {
        return packageRepository.findById(packageId).orElse(null);
    }


    public boolean updatePackage(UpdatePackageDTO updateDTO) {
        if (!updateDTO.isValid()){
            log.error("Invalid update package request");
            return false;
        }

        Package pkg = packageRepository.findById(updateDTO.getPackageId()).orElse(null);
        if (pkg == null) {
            log.error("Package not found");
            return false;
        }
        if (!pkg.canBeUpdated()) {
            log.error("Package cannot be updated");
            return false;
        }

        if (updateDTO.getNewState().equals("Delivered")) {

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

            if (!updateDTO.getToken().equals(pkg.getToken())) {
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
