package com.maxwell.AbasteceLegal.repository;

import com.maxwell.AbasteceLegal.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Page<Vehicle> findByUserId(Long userId, Pageable pageable);
    Boolean existsByLicensePlate(String licensePlate);
    Optional<Vehicle> findByIdAndUserId(Long id, Long userId);
}