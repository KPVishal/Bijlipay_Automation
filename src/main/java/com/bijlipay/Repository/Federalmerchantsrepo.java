package com.bijlipay.Repository;

import com.bijlipay.Model.Federalmerchant_details;
import com.bijlipay.Model.Merchant_details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Federalmerchantsrepo extends JpaRepository<Federalmerchant_details, Long> {


    @Query(value = "SELECT Merchant_name, TID, Merchant_address1, Merchant_address2, merchant_Contact_No, GSTR_no, " +
            "Merchants_Pincode,state_code, email FROM [auto_Federalmerchant] WHERE tid =" +
            " ?1", nativeQuery = true)
    List<Merchant_details> findMerchantByTidfederal(String tid);

    @Query(value = "SELECT SUM(txnamount) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2  AND TXNamount<=2000 GROUP BY tid ", nativeQuery = true)
    Double findByTidAndMonthfederal(String tid, String longToStringDate);

    @Query(value = "SELECT SUM(mdr) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount<=2000 GROUP BY tid ", nativeQuery = true)
    Double findbyTidandMonthForMDRfederal(String string, String longToStringDate);

    //More Than

    @Query(value = "SELECT SUM(txnamount) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2  AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findByTidAndMonthForGreaterThan2kfederal(String string, String longToStringDate);

    @Query(value = "SELECT SUM(mdr) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findbyTidandMonthForMDRAndForGreaterThan2kfederal(String string, String longToStringDate);

    @Query(value = "SELECT SUM(gst) FROM [192.168.5.61].transaction_base.[dbo].[Bijlipay_Mis_YTD$] WHERE tid = ?1 AND month=?2 AND TXNamount>2000 GROUP BY tid ", nativeQuery = true)
    Double findbyTidandMonthForGstAndForGreaterThan2kfederal(String string, String longToStringDate);
}
