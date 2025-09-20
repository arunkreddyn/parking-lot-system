package com.aaru.parking.service;

import com.aaru.parking.model.ParkingSlot;
import com.aaru.parking.model.VehicleType;

public interface SlotService {
    ParkingSlot allocateSlot(Long lotId, VehicleType vehicleType);
    void releaseSlot(ParkingSlot slot);
}
