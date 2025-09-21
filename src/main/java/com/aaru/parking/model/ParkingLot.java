package com.aaru.parking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Lot name

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ParkingSlot> slots; // Slots in this lot
}
