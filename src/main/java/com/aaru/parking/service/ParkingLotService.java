package com.aaru.parking.service;

import com.aaru.parking.model.ParkingLot;
import java.util.List;

public interface ParkingLotService {
    ParkingLot createParkingLot(ParkingLot lot);
    List<ParkingLot> getAllParkingLots();
    ParkingLot getParkingLotById(Long id);
}
