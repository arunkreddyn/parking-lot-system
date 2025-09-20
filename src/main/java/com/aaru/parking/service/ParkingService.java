package com.aaru.parking.service;

import com.aaru.parking.model.Payment;
import com.aaru.parking.model.Ticket;
import com.aaru.parking.model.VehicleType;

public interface ParkingService {

    /**
     * Handles vehicle entry: assigns slot in specified lot and creates ticket.
     *
     * @param plateNo   Vehicle plate number
     * @param type      Vehicle type
     * @param ownerEmail Owner email
     * @param lotId     Parking lot ID
     * @return Ticket for the parked vehicle
     */
    Ticket handleEntry(String plateNo, VehicleType type, String ownerEmail, Long lotId);

    /**
     * Handles vehicle exit: calculates payment, frees slot, and updates ticket.
     *
     * @param ticketId Ticket ID
     * @return Payment object
     */
    Payment handleExit(Long ticketId);

    /**
     * Retrieves the active ticket for a given vehicle plate number.
     *
     * @param plateNo Vehicle plate number
     * @return Active Ticket
     */
    Ticket getActiveTicketByPlateNo(String plateNo);

    /**
     * Retrieves the payment for a given ticket ID.
     *
     * @param ticketId Ticket ID
     * @return Payment object
     */
    Payment getPaymentByTicket(Long ticketId);
}
