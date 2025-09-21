package com.aaru.parking.service.impl;

import com.aaru.parking.model.ParkingLot;
import com.aaru.parking.model.ParkingSlot;
import com.aaru.parking.model.VehicleType;
import com.aaru.parking.repository.ParkingLotRepository;
import com.aaru.parking.repository.ParkingSlotRepository;
import com.aaru.parking.repository.TicketRepository;
import com.aaru.parking.repository.PaymentRepository;
import com.aaru.parking.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ParkingLotRepository lotRepo;
    private final ParkingSlotRepository slotRepo;
    private final TicketRepository ticketRepo;
    private final PaymentRepository paymentRepo;

    @Override
    public ParkingLot addParkingLot(ParkingLot lot) {
        return lotRepo.save(lot);
    }

    @Override
    @Transactional
    public ParkingSlot addParkingSlot(Long lotId, VehicleType vehicleType, int floor) {
        ParkingLot lot = lotRepo.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot not found: " + lotId));

        ParkingSlot slot = ParkingSlot.builder()
                .parkingLot(lot)
                .floor(floor)
                .vehicleType(vehicleType)
                .status(com.aaru.parking.model.SlotStatus.AVAILABLE)
                .build();

        return slotRepo.save(slot);
    }

    @Override
    @Transactional
    public void removeParkingSlot(Long slotId) {
        // Fetch tickets associated with the slot
        var tickets = ticketRepo.findBySlotId(slotId);

        // Delete payments linked to those tickets first
        tickets.forEach(ticket -> paymentRepo.deleteByTicketId(ticket.getId()));

        // Delete tickets
        ticketRepo.deleteBySlotId(slotId);

        // Finally, delete the parking slot
        slotRepo.deleteById(slotId);
    }

    @Override
    public List<ParkingLot> getAllLots() {
        return lotRepo.findAll();
    }
}
