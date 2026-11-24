package com.bijlipay.Repository;

import com.bijlipay.Model.ClixPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClixPendingRepository extends JpaRepository<ClixPending,Integer> {

    @Query(value="SELECT merchant_name,mid,pending_balance,REPLACE(CONVERT(VARCHAR(30), date, 101), ' ', '-') AS " +
            "date,excess_amt FROM [dbo].[loan_pending$] WHERE caption=?1",nativeQuery=true)
    List<ClixPending> exceldnld2(String getname);

    boolean existsByMid(String midArrayresult);
}
