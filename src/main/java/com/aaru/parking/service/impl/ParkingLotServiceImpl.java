package com.aaru.parking.service.impl;

import com.aaru.parking.model.ParkingLot;
import com.aaru.parking.repository.ParkingLotRepository;
import com.aaru.parking.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository lotRepository;

    @Override
    public ParkingLot createParkingLot(ParkingLot lot) {
        return lotRepository.save(lot);
    }

    @Override
    public List<ParkingLot> getAllParkingLots() {
        return lotRepository.findAll();
    }

    @Override
    public ParkingLot getParkingLotById(Long id) {
        return lotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking lot not found"));
    }
}
