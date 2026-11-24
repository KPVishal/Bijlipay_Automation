package com.bijlipay.Repository;

import com.bijlipay.Model.Loan_rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Loan_rental,String> {

    boolean existsByMid(String midArrayresult);
}
