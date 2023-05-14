package com.example.dpp_backend.service;

import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.repository.ClientRepository;
import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

// unit tests with Mockito and Hamcrest
@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private StateRepository stateRepository;

    private PartnerService partnerService;

    @BeforeEach
    void setUp() {
        partnerService = new PartnerService(packageRepository, clientRepository);
    }

    @DisplayName("Get all packages for partner")
    @Test
    void testGetAllPackages() {

        Package test1 = new Package();
        test1.setEStore("PrintPlate");
        Package test2 = new Package();
        test2.setEStore("PrintPlate");

        when(packageRepository.findByPickUpId(1)).thenReturn(List.of(test1, test2));

        assertThat(partnerService.getAllPackages(1), hasSize(2));
    }

    @DisplayName("Get package by id (valid id)")
    @Test
    void testGetPackageByValidId() {

        Package test1 = new Package();
        test1.setEStore("PrintPlate");
        test1.setId(1);

        when(packageRepository.findById(1)).thenReturn(java.util.Optional.of(test1));

        assertThat(partnerService.getPackageById(1), is(test1));
    }

    @DisplayName("Get package by id (invalid id)")
    @Test
    void testGetPackageByInvalidId(){
        when(packageRepository.findById(1)).thenReturn(java.util.Optional.empty());
        assertThat(partnerService.getPackageById(1), is(nullValue()));
    }


}