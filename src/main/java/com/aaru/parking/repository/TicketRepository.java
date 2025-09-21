package com.aaru.parking.repository;

import com.aaru.parking.model.Ticket;
import com.aaru.parking.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByVehicle_PlateNoAndStatus(String plateNo, TicketStatus status);

    // Fetch all tickets by slot ID
    List<Ticket> findBySlotId(Long slotId);

    // Delete all tickets by slot ID
    void deleteBySlotId(Long slotId);
}
