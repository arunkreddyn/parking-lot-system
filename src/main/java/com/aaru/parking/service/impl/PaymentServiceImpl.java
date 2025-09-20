package com.aaru.parking.service.impl;

import com.aaru.parking.model.Payment;
import com.aaru.parking.model.PaymentStatus;
import com.aaru.parking.model.Ticket;
import com.aaru.parking.repository.PaymentRepository;
import com.aaru.parking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment createPayment(Ticket ticket, BigDecimal amount) {
        Payment payment = Payment.builder()
                .ticket(ticket)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment completePayment(Payment payment) {
        payment.setStatus(PaymentStatus.COMPLETED);
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment failPayment(Payment payment) {
        payment.setStatus(PaymentStatus.FAILED);
        return paymentRepository.save(payment);
    }
}
