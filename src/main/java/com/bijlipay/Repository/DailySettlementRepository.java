package com.bijlipay.Repository;

import com.bijlipay.Model.DailySettlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySettlementRepository extends JpaRepository<DailySettlement,Integer> {
}
