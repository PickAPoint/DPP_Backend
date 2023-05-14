package com.example.dpp_backend.service;

import com.example.dpp_backend.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.dpp_backend.model.Package;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PackageRepository packageRepository;

    public List<Package> getAllPackages(int partnerId) {
        return packageRepository.findByPickUpId(partnerId);
    }

    public Package getPackageById(int packageId) {
        return packageRepository.findById(packageId).orElse(null);
    }
}
