package com.aaru.parking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryRequest {
    private String plateNo;
    private VehicleType type;
    private String ownerEmail;
    private Long lotId; // Added parking lot ID
}

