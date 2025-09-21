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
public class UserParkingController {

    private final ParkingService parkingService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/entry")
    public ResponseEntity<Ticket> parkVehicle(@RequestBody EntryRequest req) {
        Ticket ticket = parkingService.handleEntry(
                req.getPlateNo(),
                req.getType(),
                req.getOwnerEmail(),
                req.getLotId()
        );
        return ResponseEntity.ok(ticket);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/exit/{ticketId}")
    public ResponseEntity<Payment> retrieveVehicle(@PathVariable Long ticketId) {
        Payment payment = parkingService.handleExit(ticketId);
        return ResponseEntity.ok(payment);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/tickets/{plateNo}")
    public ResponseEntity<Ticket> getActiveTicket(@PathVariable String plateNo) {
        return ResponseEntity.ok(parkingService.getActiveTicketByPlateNo(plateNo));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/payments/{ticketId}")
    public ResponseEntity<Payment> getPaymentReceipt(@PathVariable Long ticketId) {
        return ResponseEntity.ok(parkingService.getPaymentByTicket(ticketId));
    }
}
