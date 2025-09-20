package com.aaru.parking.service;

import com.aaru.parking.model.Payment;
import com.aaru.parking.model.Ticket;

import java.math.BigDecimal;

public interface PaymentService {
    Payment createPayment(Ticket ticket, BigDecimal amount);
    Payment completePayment(Payment payment);
    Payment failPayment(Payment payment);
}
