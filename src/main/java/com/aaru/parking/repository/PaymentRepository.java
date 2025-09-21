package com.aaru.parking.repository;

import com.aaru.parking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTicketId(Long ticketId);

    // Fetch all payments by ticket ID
    List<Payment> findByTicketIdIn(List<Long> ticketIds);

    // Delete all payments by ticket ID
    void deleteByTicketId(Long ticketId);
}
