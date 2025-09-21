package com.aaru.parking.controller;

import com.aaru.parking.model.ParkingLot;
import com.aaru.parking.model.ParkingSlot;
import com.aaru.parking.model.PricingRule;
import com.aaru.parking.model.VehicleType;
import com.aaru.parking.service.AdminService;
import com.aaru.parking.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final PricingService pricingService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/lots")
    public ResponseEntity<ParkingLot> addLot(@RequestBody ParkingLot lot) {
        return ResponseEntity.ok(adminService.addParkingLot(lot));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/lots/{lotId}/slots")
    public ResponseEntity<ParkingSlot> addSlot(@PathVariable Long lotId,
                                               @RequestParam VehicleType type,
                                               @RequestParam int floor) {
        return ResponseEntity.ok(adminService.addParkingSlot(lotId, type, floor));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/slots/{slotId}")
    public ResponseEntity<Void> removeSlot(@PathVariable Long slotId) {
        adminService.removeParkingSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/pricing")
    public ResponseEntity<PricingRule> addPricing(@RequestBody PricingRule rule) {
        return ResponseEntity.ok(pricingService.addOrUpdatePricingRule(rule));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/lots")
    public ResponseEntity<List<ParkingLot>> getAllLots() {
        return ResponseEntity.ok(adminService.getAllLots());
    }
}
