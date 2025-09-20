package com.aaru.parking.repository;

import com.aaru.parking.model.PricingRule;
import com.aaru.parking.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {

    Optional<PricingRule> findByVehicleType(VehicleType type);
}
