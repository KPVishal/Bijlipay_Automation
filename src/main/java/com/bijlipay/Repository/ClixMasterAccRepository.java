package com.bijlipay.Repository;

import com.bijlipay.Model.ClixDetailsAcc;
import com.bijlipay.Model.ClixMasterAcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ClixMasterAccRepository  extends JpaRepository<ClixMasterAcc, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[clix_master_acc] SET total_transaction_value =0 , rental_collected=0, net_transaction_value=0, " +
            "merchant_share=0,Lender_capital_share=0,settled_with_Lender_capital=0,mdr =0,gst=0  ", nativeQuery = true)
    int setzero();

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[clix_master_acc] SET count = datediff(day,value_date, getdate() ) ", nativeQuery = true)
    int setdatediff();

    @Modifying
    @Transactional
    @Query(value = "UPDATE m SET m.total_transaction_value  = f.txnamount FROM [dbo].[clix_master_acc]  m INNER JOIN (SELECT mid,SUM(txnamount)txnamount FROM auto_daily_sett GROUP BY mid)f ON m.mid = f.mid ", nativeQuery = true)
    int settxnvalue();

    @Modifying
    @Transactional
    @Query(value = "UPDATE   m SET m.net_transaction_value  = f.Netamount FROM [dbo].[clix_master_acc]  m INNER JOIN (SELECT mid,SUM(Netamount)Netamount FROM auto_daily_sett GROUP BY  mid)f ON m.mid = f.mid ", nativeQuery = true)
    int setnettxnvalue();

    @Modifying
    @Transactional
    @Query(value = "UPDATE  [dbo].[clix_master_acc]  SET  net_transaction_value  = 0  WHERE net_transaction_value < 0 ", nativeQuery = true)
    int setnettxnvaluezero();

    @Modifying
    @Transactional
    @Query(value = "UPDATE   m SET m.mdr  = f.mrlmdr FROM [dbo].[clix_master_acc] m INNER JOIN ( SELECT mid,SUM(mrlmdr)mrlmdr FROM auto_daily_sett GROUP BY  mid )f ON m.mid = f.mid ;"
            + "UPDATE   m SET m.gst  = f.gst FROM [dbo].[clix_master_acc] m INNER JOIN(SELECT mid,SUM(gst)gst FROM auto_daily_sett GROUP BY  mid ) f ON m.mid = f.mid ", nativeQuery = true)
    int setmdr();


    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[clix_master_acc] SET value_date = GETDATE() ", nativeQuery = true)
    int setvaluedate();

    @Modifying
    @Transactional
    @Query(value = "update TOP (1) clix_master_acc SET  lender_capital_share = l.lender_capital_share, merchant_share = l.merchant_share from clix_master_acc c\n" +
            "Inner join loan_master_based_on_account_number$ l on c.loan_account_number = l.loan_account_number",nativeQuery = true)
    void updateShares();
}
