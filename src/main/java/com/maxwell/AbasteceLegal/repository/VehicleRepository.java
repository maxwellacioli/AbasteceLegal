package com.maxwell.AbasteceLegal.repository;

import com.maxwell.AbasteceLegal.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Page<Vehicle> findByUserId(Long userId, Pageable pageable);
    Boolean existsByLicensePlate(String licensePlate);
}
