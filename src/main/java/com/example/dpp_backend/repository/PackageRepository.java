package com.example.dpp_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.dpp_backend.model.Package;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Integer> {
    Optional<Package> findById(int id);
    List<Package> findByOrderState(String orderState);
    List<Package> findByClient_Id(int clientId);
}
