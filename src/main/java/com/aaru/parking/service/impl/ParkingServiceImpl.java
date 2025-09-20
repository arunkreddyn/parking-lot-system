package com.aaru.parking.service.impl;

import com.aaru.parking.model.*;
import com.aaru.parking.repository.*;
import com.aaru.parking.service.ParkingService;
import com.aaru.parking.service.PricingService;
import com.aaru.parking.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final VehicleRepository vehicleRepo;
    private final TicketRepository ticketRepo;
    private final PaymentRepository paymentRepo;
    private final PricingService pricingService;
    private final SlotService slotService; // Use SlotService for allocation & release

    @Override
    @Transactional
    public Ticket handleEntry(String plateNo, VehicleType type, String ownerEmail, Long lotId) {
        // Prevent duplicate active ticket
        if (ticketRepo.findByVehicle_PlateNoAndStatus(plateNo, TicketStatus.ACTIVE).isPresent()) {
            throw new IllegalStateException("Vehicle with plate " + plateNo + " is already parked.");
        }

        // Find or create vehicle
        Vehicle vehicle = vehicleRepo.findByPlateNo(plateNo)
                .orElseGet(() -> vehicleRepo.save(Vehicle.builder()
                        .plateNo(plateNo)
                        .type(type)
                        .ownerEmail(ownerEmail)
                        .build()));

        // Allocate slot via SlotService
        ParkingSlot slot = slotService.allocateSlot(lotId, type);

        // Create ticket
        Ticket ticket = Ticket.builder()
                .vehicle(vehicle)
                .slot(slot)
                .entryTime(LocalDateTime.now())
                .status(TicketStatus.ACTIVE)
                .build();

        return ticketRepo.save(ticket);
    }

    @Override
    @Transactional
    public Payment handleExit(Long ticketId) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket " + ticketId));

        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Ticket not active");
        }

        // Calculate parking duration and amount
        ticket.setExitTime(LocalDateTime.now());
        long minutes = Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toMinutes();
        BigDecimal amount = pricingService.calculatePrice(ticket.getVehicle().getType(), minutes);

        // Create payment
        Payment payment = Payment.builder()
                .ticket(ticket)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();

        paymentRepo.save(payment);

        payment.setStatus(PaymentStatus.COMPLETED);

        // Update ticket and release slot
        ticket.setStatus(TicketStatus.EXITED);
        slotService.releaseSlot(ticket.getSlot());

        ticketRepo.save(ticket);
        return paymentRepo.save(payment);
    }

    @Override
    public Ticket getActiveTicketByPlateNo(String plateNo) {
        return ticketRepo.findByVehicle_PlateNoAndStatus(plateNo, TicketStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active ticket for " + plateNo));
    }

    @Override
    public Payment getPaymentByTicket(Long ticketId) {
        return paymentRepo.findByTicketId(ticketId)
                .orElseThrow(() -> new IllegalStateException("No payment found for ticket " + ticketId));
    }
}
