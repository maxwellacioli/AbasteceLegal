package com.maxwell.AbasteceLegal.controller;

import com.maxwell.AbasteceLegal.model.Vehicle;
import com.maxwell.AbasteceLegal.repository.UserRepository;
import com.maxwell.AbasteceLegal.repository.VehicleRepository;
import com.maxwell.AbasteceLegal.util.LoggedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class VehicleController {

    private VehicleRepository repository;

    private UserRepository userRepository;

    public VehicleController(VehicleRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @GetMapping("/vehicles")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Vehicle> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/users/{userId}/vehicles")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<Vehicle> findByUserID(@PathVariable (value = "userId") Long userId,
                                      Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    @PostMapping("/users/{userId}/vehicles")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity create(@PathVariable (value = "userId") Long userId,
                                 @RequestBody Vehicle vehicle) {

        if(!LoggedUser.getInstance().getUser().getId().equals(userId)) {
            return ResponseEntity.badRequest()
                    .body("The current logged user has no permission to create a vehicle to another user");
        }

        return userRepository.findById(userId).map(user -> {
            vehicle.setUser(user);
            Vehicle vehicleSaved = repository.save(vehicle);
            return ResponseEntity.ok().body(vehicleSaved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = {"/vehicles/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehicle> findById(@PathVariable("id") long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = {"/vehicles/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}

