package com.aaru.parking.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // Corrected: Added the missing timestamp field
    private LocalDateTime timestamp;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
