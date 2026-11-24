package com.bijlipay.Repository;

import com.bijlipay.Model.ClixMaster;
import com.bijlipay.Model.DTO.ClixMasterDTO;
import com.bijlipay.Model.DTO.MasterClixDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ClixMasterRepository extends JpaRepository<ClixMaster, Integer> {


    @Query(value = "SELECT Caption FROM [dbo].[loan_master$] GROUP BY Caption", nativeQuery = true)
    List<String> getMename();

    @Query(value = "SELECT Caption FROM [dbo].[loan_master$] WHERE Caption like ?1% GROUP BY Caption", nativeQuery = true)
    List<String> getMename(Optional<String> searchTerm);

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[loan_master$] SET total_transaction_value =0 , rental_collected=0, net_transaction_value=0, " +
            "merchant_share=0,Lender_capital_share=0,settled_with_Lender_capital=0,mdr =0,gst=0 WHERE caption=?1 ", nativeQuery = true)
    int setzero(String getname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE r SET r.renttobecollect=0 FROM loan_rental$ r ", nativeQuery = true)
    int makezero();

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[loan_master$] SET count = datediff(day,value_date, getdate() ) WHERE caption=?1", nativeQuery = true)
    int setdatediff(String getname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE m SET m.total_transaction_value  = f.txnamount FROM [dbo].[loan_master$]  m INNER JOIN (SELECT mid,SUM(txnamount)txnamount FROM auto_daily_sett GROUP BY mid)f ON m.mid = f.mid WHERE caption=?1", nativeQuery = true)
    int settxnvalue(String getname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE   m SET m.net_transaction_value  = f.Netamount FROM [dbo].[loan_master$]  m INNER JOIN (SELECT mid,SUM(Netamount)Netamount FROM auto_daily_sett GROUP BY  mid)f ON m.mid = f.mid WHERE caption=?1", nativeQuery = true)
    int setnettxnvalue(String getname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE   m SET m.txncount  = f.txncount FROM [dbo].[loan_master$]  m INNER JOIN (SELECT mid,SUM(txncount)txncount FROM auto_daily_sett GROUP BY  MID)f ON m.mid = f.mid WHERE caption=NEO", nativeQuery = true)
    int settxncount();

    @Modifying
    @Transactional
    @Query(value = "UPDATE  [dbo].[loan_master$]  SET  net_transaction_value  = 0  WHERE net_transaction_value < 0 and caption=?1", nativeQuery = true)
    int setnettxnvaluezero(String getname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE   m SET m.mdr  = f.mrlmdr FROM [dbo].[loan_master$] m INNER JOIN ( SELECT mid,SUM(mrlmdr)mrlmdr FROM auto_daily_sett GROUP BY  mid )f ON m.mid = f.mid WHERE caption=?1 ;"
            + "UPDATE   m SET m.gst  = f.gst FROM [dbo].[loan_master$] m INNER JOIN(SELECT mid,SUM(gst)gst FROM auto_daily_sett GROUP BY  mid ) f ON m.mid = f.mid WHERE caption=?1", nativeQuery = true)
    int setmdr(String getname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE m SET m.Lender_capital_share = (m.Lender_capital_share1 * m.count) + f.pending_balance FROM [dbo].[loan_master$] m INNER JOIN(SELECT mid, pending_balance FROM [dbo].[loan_pending$] )f ON m.mid = f.mid WHERE caption=?1", nativeQuery = true)
    int setclixcapital(String getname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[loan_master$] SET value_date = GETDATE() WHERE caption=?1", nativeQuery = true)
    int setvaluedate(String getname);

    @Query(value = "SELECT new com.bijlipay.Model.DTO.ClixMasterDTO(m.mid, m.clixCaptialShare, m.clixCaptialShare1, m.rental, m.rentcollect, m.netTransactionValue, m.merchantShare, m.count, x.pendingBalance,"
            + "x.excessamt) FROM ClixMaster m Left Join ClixPending x ON m.mid=x.mid WHERE m.caption=?1 order by m.sNo")
    List<ClixMasterDTO> getallmaster(String getname);

    @Query(value = "SELECT new com.bijlipay.Model.DTO.ClixMasterDTO(m.meName,m.mid,m.totalTransactionValue,m.netTransactionValue,m.merchantShare,m.clixCaptialShare,m.mail_id,m.loanAccountNumber) FROM ClixMaster m WHERE caption=?1 order by m.sNo")
    List<ClixMasterDTO> generatemail(String getname);

    @Query(value = "SELECT mid,tid,merchant_name,rrn,txnamount,netamount,mdr,gst FROM  Bijlipay.dbo.[Bijlipay_Mis$] WHERE MID=?1 ", nativeQuery = true)
    List<Object[]> generatemail1(String mid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE m SET m.rental=m.rental+r.renttobecollect FROM loan_master$ m JOIN loan_rental$ r ON m.mid=r.mid ", nativeQuery = true)
    int setrent();

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[loan_master$] SET net_transaction_value=?1 ,  rental=?2, rental_collected=?3 WHERE mid=?4", nativeQuery = true)
    int update(double fNet_transaction_Value, double rental, double rent_collected, String mid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[loan_master$] SET total_transaction_Value =0 ,rental_collected=0, " +
            "     net_transaction_value=0, Merchant_Share=0,lender_Capital_Share=0,settled_with_lender_Capital=0,MDR =0,gst=0 WHERE caption=?1 ;"
            + "UPDATE m SET m.Total_transaction_value  = f.txnamount FROM [dbo].[loan_master$]  m INNER JOIN (SELECT MID,SUM(txnamount)txnamount FROM auto_daily_sett GROUP BY MID)f ON m.mid = f.mid WHERE caption=?1 ;"
            + "UPDATE m SET m.Net_transaction_Value  = f.Netamount FROM [dbo].[loan_master$]  m INNER JOIN (SELECT mid,SUM(Netamount)Netamount FROM auto_daily_sett GROUP BY  MID)f ON m.mid = f.mid WHERE caption=?1 ;"
            + "UPDATE [dbo].[loan_master$] SET count = datediff(day,VALUE_DATE, getdate() ) WHERE caption=?1 ;"
            + "UPDATE m SET m.MDR  = f.mrlMDR FROM [dbo].[loan_master$] m INNER JOIN ( SELECT mid,SUM(mrlMDR)mrlMDR FROM auto_daily_sett GROUP BY  MID )f ON m.mid = f.mid WHERE caption=?1 ; "
            + "UPDATE   m SET m.GST  = f.GST FROM [dbo].[loan_master$] m INNER JOIN(SELECT mid,SUM(GST)GST FROM auto_daily_sett GROUP BY  MID ) f ON m.mid = f.mid WHERE caption=?1 ;"
            + "UPDATE m SET m.lender_Capital_Share = (m.lender_capital_Share1 * m.count) + f.Pending_Balance FROM [dbo].[loan_master$]  m INNER JOIN(SELECT MID, Pending_Balance  FROM [dbo].[loan_pending$] )f ON m.mid = f.mid WHERE caption=?1 ;"
            + "update [dbo].[loan_master$] SET VALUE_DATE = GETDATE() WHERE caption=?1 ;", nativeQuery = true)
    void updateallquery(String getname);

    @Query(value = "SELECT new com.bijlipay.Model.DTO.ClixMasterDTO(m.mid, m.clixCaptialShare, m.clixCaptialShare1, m.rental, m.rentcollect, m.netTransactionValue, m.merchantShare, m.count, x.pendingBalance,"
            + "x.excessamt) FROM ClixMaster m Left Join ClixPending x ON m.mid=x.mid WHERE m.caption=?1 order by m.sNo")
    List<ClixMasterDTO> getallvistamaster(String getname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE  [dbo].[loan_master$] SET Total_transaction_Value =0 ,rental_collected=0, " +
            "       net_transaction_value=0, Merchant_Share=0,lender_Capital_Share=0,settled_with_lender_Capital=0,MDR =0,gst=0 WHERE caption=?1 ;"
            + "UPDATE [dbo].[loan_master$] SET count = datediff(day,VALUE_DATE, getdate() )   WHERE caption=?1 ;"
            + "UPDATE m SET m.Total_transaction_value  = f.txnamount FROM [dbo].[loan_master$]  m INNER JOIN (SELECT MID,SUM(txnamount)txnamount FROM auto_daily_sett GROUP BY mid)f ON m.mid = f.mid WHERE caption=?1 ;"
            + "UPDATE m SET m.Net_transaction_Value  = f.Netamount FROM [dbo].[loan_master$]  m INNER JOIN (SELECT mid,SUM(Netamount)Netamount FROM auto_daily_sett GROUP BY  mid)f ON m.mid = f.mid WHERE caption=?1 ;"
            + "UPDATE [dbo].[loan_master$] SET Net_transaction_Value  = 0 WHERE Net_transaction_Value < 0 and caption=?1 ;"
            + "UPDATE m SET m.MDR  = f.mrlMDR FROM [dbo].[loan_master$] m INNER JOIN ( SELECT mid,SUM(mrlMDR)mrlMDR FROM auto_daily_sett GROUP BY  MID )f ON m.mid = f.mid WHERE caption=?1 ;"
            + "UPDATE m SET m.GST  = f.GST FROM [dbo].[loan_master$] m INNER JOIN(SELECT mid,SUM(GST)GST FROM auto_daily_sett GROUP BY  MID ) f ON m.mid = f.mid WHERE caption=?1 ;"
            + "UPDATE m SET m.lender_Capital_Share = (m.lender_capital_Share1 * m.count) + f.Pending_Balance FROM [dbo].[loan_master$]  m INNER JOIN(SELECT MID, Pending_Balance  FROM [dbo].[loan_pending$] )f ON m.mid = f.mid and m.lender_capital_Share1 > 0 WHERE caption=?1 ;"
            + "UPDATE [dbo].[loan_master$] SET lender_Capital_Share = (Net_transaction_Value * held_amt /100)   WHERE lender_capital_Share1 = 0 and caption=?1 ;"
            + "UPDATE [dbo].[loan_master$] SET VALUE_DATE = GETDATE() WHERE caption=?1", nativeQuery = true)
    void updateallcapitalquery(String getname);

    @Query(value = "SELECT new com.bijlipay.Model.DTO.ClixMasterDTO(m.mid,m.clixCaptialShare,m.clixCaptialShare1,m.heldAmt,m.netTransactionValue,m.merchantShare,m.count,x.pendingBalance,x.excessamt,m.rental,m.rentcollect) FROM ClixMaster m Left Join ClixPending x ON m.mid=x.mid WHERE m.caption=?1 order by m.sNo")
    List<ClixMasterDTO> getallcapitalmaster(String getname);

    @Query(value = "SELECT mid FROM [dbo].[loan_master$] WHERE mid IN (?1) GROUP BY mid", nativeQuery = true)
    List<String> getMid(Optional<String> searchTerm);

    @Query(value = "SELECT mid FROM [dbo].[loan_master$]", nativeQuery = true)
    List<String> getMid();

    @Query(value = "SELECT * FROM [dbo].[loan_master$] WHERE mid IN (?1) GROUP BY mid", nativeQuery = true)
    int getMid(String searchTerm);

    @Transactional
    @Modifying
    @Query(value = " DELETE FROM [dbo].[loan_master$] WHERE mid = ?1 ", nativeQuery = true)
    void deletebyMid(String mid);

    @Query(value = "SELECT * FROM [dbo].[loan_pending$] WHERE mid IN (?1) GROUP BY mid", nativeQuery = true)
    int getpendingMid(String getmid);

    @Transactional
    @Modifying
    @Query(value = " DELETE FROM [dbo].[loan_pending$] WHERE mid = ?1 ", nativeQuery = true)
    void deletebyPendingMid(String mid);

    @Query(value = "SELECT * FROM [dbo].[loan_detail$] WHERE mid IN (?1) GROUP BY mid", nativeQuery = true)
    int getdetailsMid(String getmid);

    @Transactional
    @Modifying
    @Query(value = " DELETE FROM [dbo].[loan_detail$] WHERE mid = ?1 ", nativeQuery = true)
    void deletebyDetailsMid(String midArrayresult);

    @Query(value = "SELECT * FROM [dbo].[Loan_rental$] WHERE mid IN (?1) GROUP BY mid", nativeQuery = true)
    boolean getRentalMid(String getmid);

    @Transactional
    @Modifying
    @Query(value = " DELETE FROM [dbo].[Loan_rental$] WHERE mid = ?1 ", nativeQuery = true)
    void deletebyRentalMid(String midArrayresult);


    boolean existsByMid(String midArrayresult);

    @Query("SELECT format from ClixMaster where caption = ?1 group by format")
    int getformat(String getname);
}



