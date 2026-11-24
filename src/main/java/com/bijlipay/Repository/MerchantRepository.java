package com.bijlipay.Repository;

import com.bijlipay.Model.Merchant_details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant_details, Long> {

    @Query(value = "SELECT Merchant_name, TID, Merchant_address1, Merchant_address2, merchant_Contact_No, GSTR_no, Merchants_Pincode,state_code, email FROM [Authentication].[dbo].[auto_Merchants] WHERE tid = ?1", nativeQuery = true)
    List<Merchant_details> findMerchantByTid(String tid);

    @Query(value = "SELECT SUM(txnamount) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2  AND TXNamount<=2000 GROUP BY tid ", nativeQuery = true)
    Double findByTidAndMonth(String tid, String longToStringDate);

    @Query(value = "SELECT SUM(mdr) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount<=2000 GROUP BY tid ", nativeQuery = true)
    Double findbyTidandMonthForMDR(String string, String longToStringDate);

    //More Than

    @Query(value = "SELECT SUM(txnamount) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2  AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findByTidAndMonthForGreaterThan2k(String string, String longToStringDate);

    @Query(value = "SELECT SUM(mdr) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findbyTidandMonthForMDRAndForGreaterThan2k(String string, String longToStringDate);

    @Query(value = "SELECT SUM(gst) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findbyTidandMonthForGstAndForGreaterThan2k(String string, String longToStringDate);


    
    
    @Query(value = "SELECT SUM(txnamount) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2  AND TXNamount<=2000 GROUP BY tid ", nativeQuery = true)
    Double findByfederalTidAndMonth(String tid, String longToStringDate);

    @Query(value = "SELECT SUM(mdr) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount<=2000 GROUP BY tid ", nativeQuery = true)
    Double findbyfederalTidandMonthForMDR(String string, String longToStringDate);

    //More Than

    @Query(value = "SELECT SUM(txnamount) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2  AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findByfederalTidAndMonthForGreaterThan2k(String string, String longToStringDate);

    @Query(value = "SELECT SUM(mdr) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findbyfederalTidandMonthForMDRAndForGreaterThan2k(String string, String longToStringDate);

    @Query(value = "SELECT SUM(gst) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findbyfederalTidandMonthForGstAndForGreaterThan2k(String string, String longToStringDate);

    @Query(value = "SELECT tid FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 group by tid", nativeQuery = true)
	List<String> findtid(String tid);

    @Query(value = "SELECT tid FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 group by tid", nativeQuery = true)
	List<String> findfdrtid(String tid);
}