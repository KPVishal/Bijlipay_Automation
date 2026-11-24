package com.bijlipay.Repository;

import com.bijlipay.Model.PaymentTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface PaymentTempRepository extends JpaRepository<PaymentTemp,Integer> {
    @Modifying
    @Transactional
    @Query(value = "Insert into bank.dbo.Bijlipay_Mis([AggregatorName],[Approval_Code],[ASSOCIATION_fee],[AssociationName],[bank],[Batch_Code],[CardNo],[CardProduct],[cardType],[CardType1]\n" +
            ",[Card_type_A],[cc_fuel_new_ic_fee],[city],[email],[gst],[ifsc],[incentive],[location],[mcc],[MCC_Desc],[mdr_bj_calculation],[mdr_perc_table]\n" +
            ",[mdr_percentage],[merchantName],[mid],[mdr],[Narration],[ne_MDR],[Netamount],[New_IC_Fee],[new_non_ic_fee],[Paymentdate],[rRN],[state]\n" +
            ",[surcharge],[surcharge_new_omc],[TerminalSerialNo],[tid],[Tradename],[txnDate],[txnamount])\n" +
            "\n" +
            "SELECT Aggregator_Name,Approval_Code,ASSOCIATION_fee,Association_Name,Bank,Batch_Code,Card_No,Card_Product,Card_Type,Card_Type1,Card_type_A\n" +
            ",cc_fuel_new_ic_fee,City,email,GST,ifsc,Incentive,Location,MCC,MCC_Desc,mdr_bj_calculation,mdr_perc_table,mdr_percentage,Merchant_Name\n" +
            ",MID,mdr,Narration,Ne_MDR,Netamount,New_IC_Fee,new_non_ic_fee,Paymentdate,RRN,State,surcharge,surcharge_new_omc,Terminal_Serial_No\n" +
            ",t.TID,Tradename,Txn_Date,Txnamount  FROM Authentication.dbo.[payment_temp] t " +
            " inner join (Select flag,Tid from Bijlipay.dbo.TID_Mars$) m on t.tid = m.TID where m.flag='Y';",nativeQuery = true)
    void insertPaymentDataToBankDatabase();


    @Modifying
    @Transactional
    @Query(value = "Truncate table Authentication.dbo.[payment_temp]",nativeQuery = true)
    void deleteBijlipayMisTempTable();
//
//    @Modifying
//    @Transactional
//    @Query(value ="DELETE FROM bank.dbo.Bijlipay_Mis where Paymentdate < DATEADD(MONTH, -3, GETDATE());" +
//            "UPDATE bank.dbo.Bijlipay_Mis SET status = 'N' WHERE paymentdate= convert(varchar, getdate(), 23) AND status is null;" +
//            "UPDATE  m SET m.merchantName  = f.Merchants_Establishment_Name ,m.MCC_Desc  = f.Terminals_mcc, m.ifsc = f.Terminals_Ifsc_Code FROM " +
//            "bank.dbo.Bijlipay_Mis m INNER JOIN ( SELECT Terminals_TID,Merchants_Establishment_Name,Terminals_mcc,Terminals_Ifsc_Code FROM Bijlipay.dbo.GPRS_MID_MASTER$ )f ON m.TID = f.Terminals_TID " +
//            "and m.paymentdate= convert(varchar, getdate(), 23) AND m.status = 'N' ;" +
//            "UPDATE m  SET m.bank = B.bank_name,m.status = 'Y' from bank.dbo.Bijlipay_Mis m INNER JOIN Auth.dbo.bank B ON  SUBSTRING(m.ifsc,0,5) = SUBSTRING(B.bank_code,0,5) " +
//            "and m.paymentdate= convert(varchar, getdate(), 23) AND m.status = 'N';" +
//            "UPDATE m SET m.shell = 'shell' from bank.dbo.Bijlipay_Mis m INNER JOIN \n" +
//            "(SELECT distinct(Terminals_MID) from bank.dbo.shell$)t\n" +
//            " on t.Terminals_MID = m.mid and m.shell is null AND m.paymentdate= convert(varchar, getdate(), 23);" +
//            "DELETE FROM bank.dbo.Bijlipay_Mis where bank IN ('South Indian Bank','Federal Bank') and  tid not LIKE '%A';",nativeQuery = true)
//    void updateMisTable();


    @Modifying
    @Transactional
    @Query(value ="DELETE FROM bank.dbo.Bijlipay_Mis where Paymentdate < DATEADD(MONTH, -3, GETDATE());\n" +
            "UPDATE m SET m.shell = 'shell' from bank.dbo.Bijlipay_Mis m INNER JOIN(\n" +
            "SELECT distinct(Terminals_MID) from bank.dbo.shell$)t\n" +
            "on t.Terminals_MID = m.mid and m.shell is null AND m.paymentdate= convert(varchar, getdate(), 23);\n" +
            "DELETE FROM bank.dbo.Bijlipay_Mis where paymentdate= convert(varchar, getdate(), 23) AND bank IN ('South Indian Bank','Federal Bank') and  tid not LIKE '%A';",nativeQuery = true)
    void updateMisTable();

    @Transactional
    @Modifying
    @Query(value = "update bank.dbo.on_boarding set status = 'N' Where status is null;" +
            " update bank.dbo.on_boarding set corp_cc = ROUND(corp_cc,2),debit_greater = ROUND(debit_greater,2),debit_lesser=ROUND(debit_lesser,2),\n" +
            " intl_cc = ROUND(intl_cc,2),premium_cc=ROUND(premium_cc,2),std_cc=ROUND(std_cc,2),super_premium_cc=ROUND(super_premium_cc,2) WHERE status = 'N'; " +
            " UPDATE m SET m.Terminals_Bank = B.bank_name,m.status = 'Y' from bank.dbo.on_boarding m" +
            " INNER JOIN Auth.dbo.bank B ON  SUBSTRING(m.Terminals_Ifsc_Code,0,5) = SUBSTRING(B.bank_code,0,5)" +
            " WHERE m.status = 'N'",nativeQuery = true)
    void updateBank();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM KBL.dbo.auto_bijlipay_mis_temp$",nativeQuery = true)
    void deleteBankTempTable();

    @Modifying
    @Transactional
    @Query(value = "Insert into KBL.dbo.auto_bijlipay_mis_temp$([association_name],[card_type],[card_type1],[merchant_name],[mid]\n" +
            ",[rrn],[tid],[txn_date],[txnamount],card_no,approval_code,bank,paymentdate)\n" +
            "SELECT AssociationName,cardType,CardType1,merchantName,MID,rrn,TID,txnDate,Txnamount,CardNo,Approval_Code,\n" +
            "bank,Paymentdate FROM bank.[dbo].Bijlipay_Mis\n" +
            "where Paymentdate=convert(varchar, getdate(), 23) and CardNo Not IN ('Other ******Fees','EMI******EMI') and bank IN (SELECT bank_original_name from KBL.dbo.kbl_mail);",nativeQuery = true)
    void insertBijlipayMisToBankTable();

    @Modifying
    @Transactional
    @Query(value = "Insert into bank.dbo.Bijlipay_Mis([AggregatorName],[Approval_Code],[ASSOCIATION_fee],[AssociationName],[bank],[Batch_Code],[CardNo],[CardProduct],[cardType],[CardType1]\n" +
            ",[Card_type_A],[cc_fuel_new_ic_fee],[city],[email],[gst],[ifsc],[incentive],[location],[mcc],[MCC_Desc],[mdr_bj_calculation],[mdr_perc_table]\n" +
            ",[mdr_percentage],[merchantName],[mid],[mdr],[Narration],[ne_MDR],[Netamount],[New_IC_Fee],[new_non_ic_fee],[Paymentdate],[rRN],[state]\n" +
            ",[surcharge],[surcharge_new_omc],[TerminalSerialNo],[tid],[Tradename],[txnDate],[txnamount])\n" +
            "\n" +
            "SELECT Aggregator_Name,Approval_Code,ASSOCIATION_fee,Association_Name,Bank,Batch_Code,Card_No,Card_Product,Card_Type,Card_Type1,Card_type_A\n" +
            ",cc_fuel_new_ic_fee,City,email,GST,ifsc,Incentive,Location,MCC,MCC_Desc,mdr_bj_calculation,mdr_perc_table,mdr_percentage,Merchant_Name\n" +
            ",MID,mdr,Narration,Ne_MDR,Netamount,New_IC_Fee,new_non_ic_fee,Paymentdate,RRN,State,surcharge,surcharge_new_omc,Terminal_Serial_No\n" +
            ",TID,Tradename,Txn_Date,Txnamount  FROM [Authentication].[dbo].[auto_bijlipay_mis_temp$] t " +
            " inner join Bijlipay.dbo.GPRS_MID_MASTER$ m on t.tid = m.Terminals_TID where m.flag='Y';",nativeQuery = true)
    void uploadcurrentDateMisData();

    @Modifying
    @Transactional
    @Query(value = "DELETE from  Authentication.dbo.payment_temp where association_name ='FEE';\n" +
            "update Authentication.dbo.payment_temp SET paymentdate=CONVERT(varchar,getdate(),23);" +
            "update t set t.ifsc=m.IFSC from Authentication.dbo.payment_temp t inner join(\n" +
            "select tid,IFSC from Bijlipay.dbo.TID_Mars$) m on t.tid=m.TID;\n" +
            "UPDATE m  SET m.bank = B.bank_name from Authentication.dbo.payment_temp m INNER JOIN Auth.dbo.bank B ON \n" +
            "SUBSTRING(m.ifsc,0,5) = SUBSTRING(B.bank_code,0,5); \n" +
            "DELETE FROM Authentication.dbo.payment_temp where bank IN ('South Indian Bank','Federal Bank') and  tid not LIKE '%A'; " +
            "Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'TMCL%'; " +
            "Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'DLXB%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'KARB%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'IOBA%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'KVBL%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'SRCB%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'SVCB%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'UJVN%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'CLBL%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'ESFB%';\n" +
            "  Update Bijlipay.dbo.TID_Mars$ set Flag = 'Y' from Bijlipay.dbo.TID_Mars$ where IFSC like 'CLBL%';",nativeQuery =true )
    void updateTempMisTable();

    @Modifying
    @Transactional
    @Query(value = "UPDATE m  SET m.bank = B.bank_name from Authentication.dbo.auto_bijlipay_mis_temp$ m \n" +
            "INNER JOIN Auth.dbo.bank B ON  SUBSTRING(m.ifsc,0,5) = SUBSTRING(B.bank_code,0,5);" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'AUBL00%' and Merchants_Created_Time >= '2017-10-06 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'MAHB000C%' and Merchants_Created_Time >= '2018-03-01 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'ESMF00%' and Merchants_Created_Time >= '2019-09-27 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'IOBA00%' and Merchants_Created_Time >= '2017-06-16 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'KARB00%' and Merchants_Created_Time >= '2019-07-04 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'KVBL00%' and Merchants_Created_Time >= '2019-09-04 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'SRCB00%' and Merchants_Created_Time >= '2017-08-14 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'SVCB00%' and Merchants_Created_Time >= '2017-07-15 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'UJVN00%' and Merchants_Created_Time >= '2017-07-21 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'CLBL00%' and Merchants_Created_Time >= '2020-07-04 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'ESFB00%' and Merchants_Created_Time >= '2017-02-22 00:00:00.000';\n" +
            "Update Bijlipay.dbo.GPRS_MID_MASTER$ set Flag = 'Y'  where Terminals_Ifsc_Code like 'DLXB00%' and Merchants_Created_Time >= '2017-08-14 00:00:00.000';\n",nativeQuery = true)
    void updateBankName();

    @Modifying
    @Transactional
    @Query(value = "update bank.dbo.Bijlipay_Mis SET Paymentdate=CONVERT(varchar,getdate(),23) " +
            "Where paymentdate >= ?1 and paymentdate<=?2",nativeQuery = true)
    void updateCurrentDate(Date vfrom, Date vto);

    @Modifying
    @Transactional
    @Query(value = "insert into Bijlipay.dbo.ace_merchant_details(amount,card_scheme,masked_card_number,mdr,merchant_id,rrn,settlement_amount,terminal_id,txn_date,txn_time,txn_type,status)\n" +
            "select  txnamount,Association_Name,Card_No,mdr,mid,rrn,Netamount,tid,convert(varchar,Txn_Date,23),convert(varchar,Txn_Date,8),Narration,Approval_Code from Authentication.dbo.payment_temp \n" +
            " where  mid in (SELECT mid FROM Authentication.dbo.ace_mids group by mid) and Txnamount > 0;" +
            "" +
            "update Bijlipay.dbo.ace_merchant_details SET app_code='-',status='00', billing_number='-',urn='-'  \n" +
            "where merchant_id IN (SELECT mid FROM Authentication.dbo.ace_mids group by mid) and app_code is null; " +
            "update m  SET m.city=g.CTY_NAME from " +
            "Bijlipay.dbo.ace_merchant_details m inner join Bijlipay.dbo.TID_Mars$ g on m.merchant_id=g.MMID; ",nativeQuery = true)
    void insertIntoAceTable();

}
