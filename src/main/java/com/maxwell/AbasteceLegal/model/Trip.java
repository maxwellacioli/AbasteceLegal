package com.maxwell.AbasteceLegal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maxwell.AbasteceLegal.util.City;
import com.maxwell.AbasteceLegal.util.FuelType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "trips")
@Getter
@Setter
public class Trip {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Date date;

    @NotNull
    private float tripDistance;

    @NotNull
    private float fuelQuantity;

    @NotNull
    private float fuelConsumption;


    @Enumerated(EnumType.STRING)
    @NotNull
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private City city;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonIgnore
    private Vehicle vehicle;
}
