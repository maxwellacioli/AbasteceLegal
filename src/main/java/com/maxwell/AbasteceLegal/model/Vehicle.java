package com.maxwell.AbasteceLegal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maxwell.AbasteceLegal.util.FuelType;
import com.maxwell.AbasteceLegal.util.VehicleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "vehicles")
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @NotNull
    private String model;

    @NotNull
    private String licensePlate;

    @NotNull
    private float totalDistance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
