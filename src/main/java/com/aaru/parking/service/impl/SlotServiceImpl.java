package com.aaru.parking.service.impl;

import com.aaru.parking.model.ParkingSlot;
import com.aaru.parking.model.SlotStatus;
import com.aaru.parking.model.VehicleType;
import com.aaru.parking.repository.ParkingSlotRepository;
import com.aaru.parking.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final ParkingSlotRepository slotRepository;

    @Override
    @Transactional
    public ParkingSlot allocateSlot(Long lotId, VehicleType vehicleType) {
        ParkingSlot slot = slotRepository.findAvailableSlots(SlotStatus.AVAILABLE, vehicleType, lotId)
                .stream()
                .findFirst() // pick the first available slot
                .orElseThrow(() -> new RuntimeException("No available slot for vehicle type " + vehicleType));

        slot.setStatus(SlotStatus.OCCUPIED);
        return slotRepository.save(slot);
    }

    @Override
    @Transactional
    public void releaseSlot(ParkingSlot slot) {
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);
    }
}
