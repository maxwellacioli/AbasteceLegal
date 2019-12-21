package com.maxwell.AbasteceLegal.repository;

import com.maxwell.AbasteceLegal.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Page<Trip> findByVehicleId(Long vehicleId, Pageable pageable);
    Optional<Trip> findByIdAndVehicleId(Long id, Long vehicleId);
}
