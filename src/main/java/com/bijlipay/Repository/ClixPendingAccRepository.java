package com.bijlipay.Repository;

import com.bijlipay.Model.ClixDetailsAcc;
import com.bijlipay.Model.ClixPendingAcc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClixPendingAccRepository extends JpaRepository<ClixPendingAcc, Integer> {
}
