package com.aaru.parking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType; // ensure matches repository & service

    private int floor; // floor number for ordering

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    @ToString.Exclude
    private ParkingLot parkingLot;

    @OneToOne(mappedBy = "slot",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Ticket ticket;
}
