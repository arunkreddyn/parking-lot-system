package com.aaru.parking.service.impl;

import com.aaru.parking.model.ParkingSlot;
import com.aaru.parking.model.Ticket;
import com.aaru.parking.model.Vehicle;
import com.aaru.parking.repository.TicketRepository;
import com.aaru.parking.service.SlotService;
import com.aaru.parking.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final SlotService slotService;

    @Override
    @Transactional
    public Ticket createTicket(Vehicle vehicle, ParkingSlot slot) {
        Ticket ticket = Ticket.builder()
                .vehicle(vehicle)
                .slot(slot)
                .entryTime(LocalDateTime.now())
                .build();
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket closeTicket(Ticket ticket) {
        ticket.setExitTime(LocalDateTime.now());
        slotService.releaseSlot(ticket.getSlot());
        return ticketRepository.save(ticket);
    }
}
