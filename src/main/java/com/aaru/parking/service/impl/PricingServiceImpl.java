package com.aaru.parking.service.impl;

import com.aaru.parking.model.PricingRule;
import com.aaru.parking.model.VehicleType;
import com.aaru.parking.repository.PricingRuleRepository;
import com.aaru.parking.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final PricingRuleRepository pricingRuleRepository;

    @Override
    public BigDecimal calculatePrice(VehicleType type, long minutes) {
        PricingRule rule = pricingRuleRepository.findByVehicleType(type)
                .orElseThrow(() -> new IllegalStateException("No pricing rule found for " + type));

        BigDecimal hours = BigDecimal.valueOf(Math.ceil(minutes / 60.0));
        return rule.getPricePerHour().multiply(hours);
    }

    @Override
    @Transactional
    public PricingRule addOrUpdatePricingRule(PricingRule rule) {
        return pricingRuleRepository.save(rule);
    }
}
