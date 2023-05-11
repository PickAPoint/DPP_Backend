package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
