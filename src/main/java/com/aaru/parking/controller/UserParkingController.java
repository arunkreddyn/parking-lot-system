package com.aaru.parking.controller;

import com.aaru.parking.model.EntryRequest;
import com.aaru.parking.model.Payment;
import com.aaru.parking.model.Ticket;
import com.aaru.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserParkingController {

    private final ParkingService parkingService;

    /**
     * USER parks a vehicle → allocate slot & generate ticket
     */
    @PostMapping("/entry")
    public ResponseEntity<Ticket> parkVehicle(@RequestBody EntryRequest req) {
        Ticket ticket = parkingService.handleEntry(
                req.getPlateNo(),
                req.getType(),
                req.getOwnerEmail(),
                req.getLotId() // pass lotId to service
        );
        return ResponseEntity.ok(ticket);
    }

    /**
     * USER retrieves a vehicle → calculate fee, process payment, free slot
     */
    @PostMapping("/exit/{ticketId}")
    public ResponseEntity<Payment> retrieveVehicle(@PathVariable Long ticketId) {
        Payment payment = parkingService.handleExit(ticketId);
        return ResponseEntity.ok(payment);
    }

    /**
     * USER can view their active ticket
     */
    @GetMapping("/tickets/{plateNo}")
    public ResponseEntity<Ticket> getActiveTicket(@PathVariable String plateNo) {
        return ResponseEntity.ok(parkingService.getActiveTicketByPlateNo(plateNo));
    }

    /**
     * USER can view payment receipt for a ticket
     */
    @GetMapping("/payments/{ticketId}")
    public ResponseEntity<Payment> getPaymentReceipt(@PathVariable Long ticketId) {
        return ResponseEntity.ok(parkingService.getPaymentByTicket(ticketId));
    }
}
