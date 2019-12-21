package com.maxwell.AbasteceLegal.controller;

import com.maxwell.AbasteceLegal.model.User;
import com.maxwell.AbasteceLegal.model.Vehicle;
import com.maxwell.AbasteceLegal.repository.UserRepository;
import com.maxwell.AbasteceLegal.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @PutMapping("/users/vehicles/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> selectedVehicle(@AuthenticationPrincipal User user,
                                             @PathVariable (value = "id") Long vehicleId) {

        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndUserId(vehicleId, user.getId());

        if(vehicle.isPresent()) {
            user.setPrincipalId(vehicleId);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users/vehicles/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getVehicleById(@AuthenticationPrincipal User user,
                                             @PathVariable (value = "id") Long vehicleId) {

        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndUserId(vehicleId, user.getId());

        if(vehicle.isPresent()) {
            return ResponseEntity.ok().body(vehicle.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/vehicles/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteVehicleById(@AuthenticationPrincipal User user,
                                            @PathVariable (value = "id") Long vehicleId) {

        Long userId = user.getId();
        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndUserId(vehicleId, userId);

        if(vehicle.isPresent()) {
            vehicleRepository.delete(vehicle.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users/{userId}/vehicles")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity create(@PathVariable (value = "userId") Long userId,
                                 @RequestBody Vehicle vehicle) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();

        if(!user.getId().equals(userId)) {
            return ResponseEntity.badRequest()
                    .body("The current logged user has no permission to create a vehicle to another user");
        }

        if(vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate())) {
            return ResponseEntity.badRequest()
                    .body("License plate " + vehicle.getLicensePlate() + " already exists.");
        }

        return userRepository.findById(userId).map(u -> {
            vehicle.setUser(u);
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

