package com.aaru.parking.repository;

import com.aaru.parking.model.ParkingSlot;
import com.aaru.parking.model.SlotStatus;
import com.aaru.parking.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    /**
     * Finds all available parking slots for a given vehicle type in a specific lot.
     * Pessimistic lock ensures concurrency safety.
     * Ordered by floor and slot ID for predictable assignment.
     *
     * @param status      Slot status (AVAILABLE)
     * @param vehicleType Vehicle type (CAR, BIKE, etc.)
     * @param lotId       Parking lot ID
     * @return List of available slots
     */
    @Lock(PESSIMISTIC_WRITE)
    @Query("""
           SELECT s 
           FROM ParkingSlot s 
           WHERE s.status = :status 
             AND s.vehicleType = :vehicleType
             AND s.parkingLot.id = :lotId
           ORDER BY s.floor ASC, s.id ASC
           """)
    List<ParkingSlot> findAvailableSlots(
            @Param("status") SlotStatus status,
            @Param("vehicleType") VehicleType vehicleType,
            @Param("lotId") Long lotId
    );
}
