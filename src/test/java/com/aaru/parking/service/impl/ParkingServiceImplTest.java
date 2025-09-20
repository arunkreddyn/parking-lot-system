package com.aaru.parking.service.impl;

import com.aaru.parking.model.*;
import com.aaru.parking.repository.*;
import com.aaru.parking.service.PricingService;
import com.aaru.parking.service.SlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingServiceImplTest {
    @Mock
    VehicleRepository vehicleRepo;
    @Mock
    TicketRepository ticketRepo;
    @Mock
    ParkingSlotRepository slotRepo;
    @Mock
    PaymentRepository paymentRepo;
    @Mock
    PricingService pricingService;

    @Mock
    SlotService slotService;

    @InjectMocks
    ParkingServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleEntry_shouldThrowIfDuplicateVehicle() {
        when(ticketRepo.findByVehicle_PlateNoAndStatus("ABC123", TicketStatus.ACTIVE)).thenReturn(Optional.of(new Ticket()));
        assertThrows(IllegalStateException.class, () -> service.handleEntry("ABC123", VehicleType.CAR, "user@email.com", 1L));
    }

    @Test
    void handleEntry_shouldAllocateSlotAndCreateTicket() {
        when(ticketRepo.findByVehicle_PlateNoAndStatus(anyString(), any())).thenReturn(Optional.empty());
        Vehicle vehicle = Vehicle.builder().plateNo("ABC123").type(VehicleType.CAR).ownerEmail("user@email.com").build();
        when(vehicleRepo.findByPlateNo(anyString())).thenReturn(Optional.of(vehicle));
        ParkingSlot slot = ParkingSlot.builder().id(1L).vehicleType(VehicleType.CAR).status(SlotStatus.AVAILABLE).build();
        when(slotRepo.findAvailableSlots(
                any(SlotStatus.class),
                any(VehicleType.class),
                anyLong()
        )).thenReturn(List.of(slot));
        when(slotRepo.save(any())).thenReturn(slot);
        Ticket ticket = Ticket.builder().vehicle(vehicle).slot(slot).entryTime(LocalDateTime.now()).status(TicketStatus.ACTIVE).build();
        when(ticketRepo.save(any())).thenReturn(ticket);
        Ticket result = service.handleEntry("ABC123", VehicleType.CAR, "user@email.com", 1L);
        assertEquals(ticket, result);
    }

    @Test
    void handleExit_shouldFreeSlotAndCompletePayment() {
        Ticket ticket = Ticket.builder().id(1L).status(TicketStatus.ACTIVE).entryTime(LocalDateTime.now().minusHours(2)).slot(ParkingSlot.builder().id(1L).status(SlotStatus.OCCUPIED).build()).vehicle(Vehicle.builder().type(VehicleType.CAR).build()).build();
        when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticket));
        when(pricingService.calculatePrice(any(), anyLong())).thenReturn(BigDecimal.TEN);
        when(paymentRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(slotRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Payment payment = service.handleExit(1L);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(SlotStatus.OCCUPIED, ticket.getSlot().getStatus());
        assertEquals(TicketStatus.EXITED, ticket.getStatus());
    }
}

