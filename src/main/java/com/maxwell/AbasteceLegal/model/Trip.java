package com.maxwell.AbasteceLegal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maxwell.AbasteceLegal.util.City;
import com.maxwell.AbasteceLegal.util.FuelType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "trips")
@Getter
@Setter
public class Trip {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Date date;

    private float tripDistance;

    private float fuelQuantity;

    private float fuelConsumption;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)

    private City city;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonIgnore
    private Vehicle vehicle;
}
