package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.UpdatePackageDTO;
import com.example.dpp_backend.service.PartnerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*") // NOSONAR
@RestController
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerController {
    
    private final PartnerService partnerService;

    @GetMapping("/packages")
    public List<Package> getAllPackages(
            @RequestParam int partnerId
    ) {
        return partnerService.getAllPackages(partnerId);
    }

    @GetMapping("/package/{id}")
    public ResponseEntity<Package> getPackageById(
            @PathVariable int id
    ) {
        Package packageObj = partnerService.getPackageById(id);
        if (packageObj == null) {
            log.error("Error getting package with id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(packageObj);
    }

    @PutMapping("/package")
    public ResponseEntity<Boolean> updatePackage(
            @RequestBody UpdatePackageDTO updateDTO
            ){
        log.info("Updating package: {}", updateDTO);
        return ResponseEntity.ok(partnerService.updatePackage(updateDTO));
    }
}
