package com.bijlipay.Repository;

import com.bijlipay.Model.LoanReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanReceiptResponseRespo extends JpaRepository<LoanReceipt,Integer> {
}
