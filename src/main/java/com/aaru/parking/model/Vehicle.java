package com.aaru.parking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plateNo;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    private String ownerEmail;

    @OneToOne(mappedBy = "vehicle")
    @ToString.Exclude
    private Ticket ticket;
}
