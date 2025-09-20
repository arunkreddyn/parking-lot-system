package com.aaru.parking.service;

import com.aaru.parking.model.ParkingLot;
import com.aaru.parking.model.ParkingSlot;
import com.aaru.parking.model.VehicleType;

import java.util.List;

public interface AdminService {
    ParkingLot addParkingLot(ParkingLot lot);
    ParkingSlot addParkingSlot(Long lotId, VehicleType type, int floor);
    void removeParkingSlot(Long slotId);
    List<ParkingLot> getAllLots();
}
