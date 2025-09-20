package com.aaru.parking.service;

import com.aaru.parking.model.PricingRule;
import com.aaru.parking.model.VehicleType;

import java.math.BigDecimal;

public interface PricingService {

    // Calculate parking amount based on vehicle type and duration in minutes
    BigDecimal calculatePrice(VehicleType type, long minutes);

    // Add or update pricing rule (used by AdminController)
    PricingRule addOrUpdatePricingRule(PricingRule rule);
}
