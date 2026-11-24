package com.bijlipay.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.bijlipay.Model.FintreeMaster;
import com.bijlipay.Model.DTO.FintreeMasterDTO;

public interface FintreeRepo extends JpaRepository<FintreeMaster, Integer>{
	
	 	@Modifying
	    @Transactional
	    @Query(value = "UPDATE [dbo].[fintree_master$] SET total_transaction_value =0 , rental_collected=0, net_transaction_value=0, " +
	            "merchant_share=0,Lender_capital_share=0,settled_with_Lender_capital=0,mdr =0,gst=0,total_eligible_value=0,value_allocated_perct=0,value_allocated=0 WHERE caption=?1 ", nativeQuery = true)
	    int setzero(String getname);
	 
	 
	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE r SET r.renttobecollect=0 FROM loan_rental$ r ", nativeQuery = true)
	    int makezero();

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE [dbo].[fintree_master$] SET count = datediff(day,value_date, getdate() ) WHERE caption=?1", nativeQuery = true)
	    int setdatediff(String getname);

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE m SET m.total_transaction_value  = f.txnamount FROM [dbo].[fintree_master$]  m INNER JOIN (SELECT tid,SUM(txnamount)txnamount FROM auto_daily_sett GROUP BY tid)f ON m.tid = f.tid WHERE caption=?1", nativeQuery = true)
	    int settxnvalue(String getname);

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE   m SET m.net_transaction_value  = f.Netamount FROM [dbo].[fintree_master$]  m INNER JOIN (SELECT tid,SUM(Netamount)Netamount FROM auto_daily_sett GROUP BY  tid)f ON m.tid = f.tid WHERE caption=?1", nativeQuery = true)
	    int setnettxnvalue(String getname);

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE   m SET m.txncount  = f.txncount FROM [dbo].[fintree_master$]  m INNER JOIN (SELECT tid,SUM(txncount)txncount FROM auto_daily_sett GROUP BY  tid)f ON m.tid = f.tid WHERE caption=NEO", nativeQuery = true)
	    int settxncount();

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE  [dbo].[fintree_master$]  SET  net_transaction_value  = 0  WHERE net_transaction_value < 0 and caption=?1", nativeQuery = true)
	    int setnettxnvaluezero(String getname);

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE   m SET m.mdr  = f.mrlmdr FROM [dbo].[fintree_master$] m INNER JOIN ( SELECT tid,SUM(mrlmdr)mrlmdr FROM auto_daily_sett GROUP BY  tid )f ON m.tid = f.tid WHERE caption=?1 ;"
	            + "UPDATE   m SET m.gst  = f.gst FROM [dbo].[fintree_master$] m INNER JOIN(SELECT tid,SUM(gst)gst FROM auto_daily_sett GROUP BY  tid ) f ON m.tid = f.tid WHERE caption=?1", nativeQuery = true)
	    int setmdr(String getname);

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE m SET m.Lender_capital_share = (m.Lender_capital_share1 * m.count) + f.pending_balance FROM [dbo].[fintree_master$] m INNER JOIN(SELECT tid, pending_balance FROM [dbo].[Fintree_pending$] )f ON m.tid = f.tid WHERE caption=?1", nativeQuery = true)
	    int setclixcapital(String getname);

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE [dbo].[fintree_master$] SET value_date = GETDATE() WHERE caption=?1", nativeQuery = true)
	    int setvaluedate(String getname);

	    @Query(value = "SELECT new com.bijlipay.Model.DTO.FintreeMasterDTO(m.mid,m.tid, m.clixCaptialShare, m.clixCaptialShare1, m.rental, m.rentcollect, m.netTransactionValue, m.merchantShare, m.count, x.pendingBalance,"
	            + "x.excessamt) FROM FintreeMaster m Left Join FintreePending x ON m.tid=x.tid WHERE m.caption=?1 order by m.sNo")
	    List<FintreeMasterDTO> getallmaster(String getname);

	    @Query(value = "SELECT new com.bijlipay.Model.DTO.FintreeMasterDTO(m.meName,m.mid,m.tid,m.totalTransactionValue,m.netTransactionValue,m.merchantShare,m.clixCaptialShare,m.mail_id,m.loanAccountNumber) FROM FintreeMaster m WHERE caption=?1 order by m.sNo")
	    List<FintreeMasterDTO> generatemail(String getname);
	    
	    @Query(value = "SELECT new com.bijlipay.Model.DTO.FintreeMasterDTO(m.mid,m.tid,m.total_eligible_value ) FROM FintreeMaster m  order by m.tid")
	    List<FintreeMasterDTO> nettxn();

//	    @Query(value = "SELECT mid,tid,merchant_name,rrn,txnamount,netamount,mdr,gst FROM  Bijlipay.dbo.[Bijlipay_Mis$] WHERE MID=?1 ", nativeQuery = true)
//	    List<Object[]> generatemail1(String mid);

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE m SET m.rental=m.rental+r.renttobecollect FROM fintree_master$ m JOIN loan_rental$ r ON m.tid=r.tid ", nativeQuery = true)
	    int setrent();

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE [dbo].[fintree_master$] SET net_transaction_value=?1 ,  rental=?2, rental_collected=?3 WHERE tid=?4", nativeQuery = true)
	    int update(double fNet_transaction_Value, double rental, double rent_collected, String tid);

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE [dbo].[fintree_master$] SET total_transaction_Value =0 ,rental_collected=0, " +
	            "     net_transaction_value=0, Merchant_Share=0,lender_Capital_Share=0,settled_with_lender_Capital=0,MDR =0,gst=0 WHERE caption=?1 ;"
	            + "UPDATE m SET m.Total_transaction_value  = f.txnamount FROM [dbo].[fintree_master$]  m INNER JOIN (SELECT tid,SUM(txnamount)txnamount FROM auto_daily_sett GROUP BY tid)f ON m.tid = f.tid WHERE caption=?1 ;"
	            + "UPDATE m SET m.Net_transaction_Value  = f.Netamount FROM [dbo].[fintree_master$]  m INNER JOIN (SELECT tid,SUM(Netamount)Netamount FROM auto_daily_sett GROUP BY  tid)f ON m.tid = f.tid WHERE caption=?1 ;"
	            + "UPDATE [dbo].[fintree_master$] SET count = datediff(day,VALUE_DATE, getdate() ) WHERE caption=?1 ;"
	            + "UPDATE m SET m.MDR  = f.mrlMDR FROM [dbo].[fintree_master$] m INNER JOIN ( SELECT tid,SUM(mrlMDR)mrlMDR FROM auto_daily_sett GROUP BY  tid )f ON m.tid = f.tid WHERE caption=?1 ; "
	            + "UPDATE   m SET m.GST  = f.GST FROM [dbo].[fintree_master$] m INNER JOIN(SELECT tid,SUM(GST)GST FROM auto_daily_sett GROUP BY  tid ) f ON m.tid = f.tid WHERE caption=?1 ;"
	            + "UPDATE m SET m.lender_Capital_Share = (m.lender_capital_Share1 * m.count) + f.Pending_Balance FROM [dbo].[fintree_master$]  m INNER JOIN(SELECT tid, Pending_Balance  FROM [dbo].[Fintree_pending$] )f ON m.tid = f.tid WHERE caption=?1 ;"
	            + "update [dbo].[fintree_master$] SET VALUE_DATE = GETDATE() WHERE caption=?1 ;", nativeQuery = true)
	    void updateallquery(String getname);
	    
	   
	    @Modifying
	    @Transactional
	    @Query(value = "update [dbo].[fintree_master$] set total_eligible_value=(select CASE WHEN sum(net_transaction_value)>sum(lender_capital_share1) THEN sum(lender_capital_share1) WHEN  sum(net_transaction_value)<sum(lender_capital_share1) THEN sum(net_transaction_value) END as total_eligible_value from [dbo].[fintree_master$] where mid=?1) where mid=?1", nativeQuery = true)
	    int gettev(String strMID);
	    
	    @Modifying
	    @Transactional
	    @Query(value = "update [dbo].[fintree_master$] set value_allocated_perct= (select ((lender_capital_share1 /(select sum(lender_capital_share1)  from [dbo].[fintree_master$] where mid=?2 )) * 100) value_allocated_perct from [dbo].[fintree_master$] where tid=?1) where tid=?1", nativeQuery = true)
	    int value_allocated_perc(String strTID, String strMID);
	    
	    @Modifying
	    @Transactional
	    @Query(value = "update [dbo].[fintree_master$] set value_allocated=(select ((total_eligible_value * value_allocated_perct)/100) value_allocated from [dbo].[fintree_master$] where tid=?1) where tid=?1", nativeQuery = true)
	    int value_allocated(String getname);
	    
	    @Modifying
	    @Transactional
	    @Query(value = "update [dbo].[fintree_master$] set merchant_share=0 where merchant_share !=0 ", nativeQuery = true)
		int merchant_share();


		

	    
}
