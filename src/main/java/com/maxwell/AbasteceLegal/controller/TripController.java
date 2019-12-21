package com.maxwell.AbasteceLegal.controller;

import com.maxwell.AbasteceLegal.model.Trip;
import com.maxwell.AbasteceLegal.model.User;
import com.maxwell.AbasteceLegal.model.Vehicle;
import com.maxwell.AbasteceLegal.repository.TripRepository;
import com.maxwell.AbasteceLegal.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TripController {

    private TripRepository tripRepository;

    private VehicleRepository vehicleRepository;

    public TripController(TripRepository tripRepository, VehicleRepository vehicleRepository) {
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("/trips")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Trip> findAll(Pageable pageable) {
        return tripRepository.findAll(pageable);
    }


    @GetMapping("/vehicles/{vehicleId}/trips")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<Trip> findAllTripsByVehicleId(@PathVariable(value = "vehicleId") Long vehicleId,
                                              Pageable pageable) {
        return tripRepository.findByVehicleId(vehicleId, pageable);
    }

    @GetMapping("/vehicles/{vehicleId}/trips/{tripId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Trip> findByIdAndVehicleId(@AuthenticationPrincipal User user,
                                                     @PathVariable(value = "vehicleId") Long vehicleId,
                                                     @PathVariable(value = "tripId") Long tripId) {

        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndUserId(vehicleId, user.getId());

        if (vehicle.isPresent()) {
            Optional<Trip> trip = tripRepository.findByIdAndVehicleId(tripId, vehicleId);
            if (trip.isPresent()) {
                return ResponseEntity.ok().body(trip.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/vehicles/{vehicleId}/trips/{tripId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Trip> deleteByIdAndVehicleId(@AuthenticationPrincipal User user,
                                                       @PathVariable(value = "vehicleId") Long vehicleId,
                                                       @PathVariable(value = "tripId") Long tripId) {

        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndUserId(vehicleId, user.getId());

        if (vehicle.isPresent()) {
            Optional<Trip> trip = tripRepository.findByIdAndVehicleId(tripId, vehicleId);
            if (trip.isPresent()) {
                tripRepository.delete(trip.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/vehicles/{vehicleId}/trips")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Trip> createTrip(@PathVariable(value = "vehicleId") Long vehicleId,
                                           @RequestBody Trip trip) {
        return vehicleRepository.findById(vehicleId).map(vehicle -> {
            trip.setFuelConsumption(fuelConsumptionCalculator(trip.getTripDistance(), trip.getFuelQuantity()));
            trip.setVehicle(vehicle);
            Trip tripSaved = tripRepository.save(trip);

            //Updating current total distance
            vehicle.setTotalDistance(vehicle.getTotalDistance() + trip.getTripDistance());
            vehicleRepository.save(vehicle);

            return ResponseEntity.ok().body(tripSaved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = {"/trips/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return tripRepository.findById(id)
                .map(record -> {
                    tripRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    private float fuelConsumptionCalculator(float tripDistance, float fuelQuantity) {
        return tripDistance / fuelQuantity;
    }

}

