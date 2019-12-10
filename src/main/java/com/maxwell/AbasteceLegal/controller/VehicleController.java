package com.maxwell.AbasteceLegal.controller;

import com.maxwell.AbasteceLegal.model.Vehicle;
import com.maxwell.AbasteceLegal.repository.UserRepository;
import com.maxwell.AbasteceLegal.repository.VehicleRepository;
import com.maxwell.AbasteceLegal.security.services.UserPrinciple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class VehicleController {

    private VehicleRepository vehicleRepository;

    private UserRepository userRepository;

    public VehicleController(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/vehicles")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Vehicle> findAll(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    @GetMapping("/users/{userId}/vehicles")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<Vehicle> findByUserID(@PathVariable (value = "userId") Long userId,
                                      Pageable pageable) {
        return vehicleRepository.findByUserId(userId, pageable);
    }

    @PostMapping("/users/{userId}/vehicles")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity create(@PathVariable (value = "userId") Long userId,
                                 @RequestBody Vehicle vehicle) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple userPrinciple = (UserPrinciple)authentication.getPrincipal();

        if(!userPrinciple.getId().equals(userId)) {
            return ResponseEntity.badRequest()
                    .body("The current logged user has no permission to create a vehicle to another user");
        }

        if(vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate())) {
            return ResponseEntity.badRequest()
                    .body("License plate " + vehicle.getLicensePlate() + " already exists.");
        }

        return userRepository.findById(userId).map(user -> {
            vehicle.setUser(user);
            Vehicle vehicleSaved = vehicleRepository.save(vehicle);
            return ResponseEntity.ok().body(vehicleSaved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = {"/vehicles/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehicle> findById(@PathVariable("id") long id) {
        return vehicleRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = {"/vehicles/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return vehicleRepository.findById(id)
                .map(record -> {
                    vehicleRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}

