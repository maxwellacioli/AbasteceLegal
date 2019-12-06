package com.maxwell.AbasteceLegal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maxwell.AbasteceLegal.util.FuelType;
import com.maxwell.AbasteceLegal.util.VehicleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "vehicles")
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String model;

    private String licensePlate;

    private float currentTotalDistance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
