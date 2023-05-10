package com.example.dpp_backend.repository;

import com.example.dpp_backend.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Integer> {
}
