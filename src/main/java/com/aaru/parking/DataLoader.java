package com.aaru.parking;

import com.aaru.parking.model.*;
import com.aaru.parking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ParkingLotRepository lotRepo;
    private final ParkingSlotRepository slotRepo;
    private final VehicleRepository vehicleRepo;
    private final TicketRepository ticketRepo;
    private final PaymentRepository paymentRepo;
    private final PricingRuleRepository pricingRepo;

    @Override
    @Transactional
    public void run(String... args) {

        // 1️⃣ Create a Parking Lot
        ParkingLot lot = lotRepo.save(
                ParkingLot.builder()
                        .name("Arun Plaza")
                        .build()
        );

        // 2️⃣ Add slots
        for (int floor = 1; floor <= 3; floor++) {
            // Cars
            for (int i = 0; i < 5; i++) {
                slotRepo.save(ParkingSlot.builder()
                        .parkingLot(lot)
                        .floor(floor)
                        .vehicleType(VehicleType.CAR)
                        .status(SlotStatus.AVAILABLE)
                        .build());
            }
            // Bikes
            for (int i = 0; i < 3; i++) {
                slotRepo.save(ParkingSlot.builder()
                        .parkingLot(lot)
                        .floor(floor)
                        .vehicleType(VehicleType.BIKE)
                        .status(SlotStatus.AVAILABLE)
                        .build());
            }
            // Trucks
            for (int i = 0; i < 2; i++) {
                slotRepo.save(ParkingSlot.builder()
                        .parkingLot(lot)
                        .floor(floor)
                        .vehicleType(VehicleType.TRUCK)
                        .status(SlotStatus.AVAILABLE)
                        .build());
            }
        }

        // 3️⃣ Create sample vehicles
        Vehicle car = vehicleRepo.save(Vehicle.builder()
                .plateNo("MH12AB1234")
                .type(VehicleType.CAR)
                .build());

        Vehicle bike = vehicleRepo.save(Vehicle.builder()
                .type(VehicleType.BIKE)
                .build());

        // 4️⃣ Assign one active ticket (car currently parked)
        ParkingSlot freeCarSlot = slotRepo.findAvailableSlots(
                SlotStatus.AVAILABLE,
                VehicleType.CAR,
                lot.getId()
        ).stream().findFirst()
         .orElseThrow(() -> new RuntimeException("No free car slot"));
        freeCarSlot.setStatus(SlotStatus.OCCUPIED);
        slotRepo.save(freeCarSlot);

        Ticket activeTicket = ticketRepo.save(Ticket.builder()
                .vehicle(car)
                .slot(freeCarSlot)
                .entryTime(LocalDateTime.now().minusMinutes(90)) // 1.5 hours ago
                .status(TicketStatus.ACTIVE)
                .build());

        // 5️⃣ Create one exited ticket + payment (bike example)
        ParkingSlot freeBikeSlot = slotRepo.findAvailableSlots(
                SlotStatus.AVAILABLE,
                VehicleType.BIKE,
                lot.getId()
        ).stream().findFirst()
         .orElseThrow(() -> new RuntimeException("No free bike slot"));

        Ticket exitedTicket = ticketRepo.save(Ticket.builder()
                .vehicle(bike)
                .slot(freeBikeSlot)
                .entryTime(LocalDateTime.now().minusHours(4))
                .exitTime(LocalDateTime.now().minusHours(1))
                .status(TicketStatus.EXITED)
                .build());

        paymentRepo.save(Payment.builder()
                .ticket(exitedTicket)
                .amount(BigDecimal.valueOf(30))
                .status(PaymentStatus.COMPLETED)
                .timestamp(LocalDateTime.now().minusHours(1))
                .build());

        // 6️⃣ Add Pricing Rules
        pricingRepo.save(PricingRule.builder()
                .vehicleType(VehicleType.CAR)
                .pricePerHour(BigDecimal.valueOf(10))
                .build());

        pricingRepo.save(PricingRule.builder()
                .vehicleType(VehicleType.BIKE)
                .pricePerHour(BigDecimal.valueOf(5))
                .build());

        pricingRepo.save(PricingRule.builder()
                .vehicleType(VehicleType.TRUCK)
                .pricePerHour(BigDecimal.valueOf(15))
                .build());

        System.out.println("✅ Demo data loaded successfully: ParkingLot, slots, vehicles, tickets, payments, pricing rules.");
    }
}
