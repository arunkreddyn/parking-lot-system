package com.aaru.parking.service;

import com.aaru.parking.model.ParkingSlot;
import com.aaru.parking.model.Ticket;
import com.aaru.parking.model.Vehicle;

public interface TicketService {
    Ticket createTicket(Vehicle vehicle, ParkingSlot slot);
    Ticket closeTicket(Ticket ticket);
}
