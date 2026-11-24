package com.bijlipay.Repository;

import com.bijlipay.Model.MisTable;
import com.bijlipay.Utils.DateUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface MisRespository extends JpaRepository<MisTable, Integer> {

    @Query(value = "SELECT net_transaction_value,merchant_accountnumber,me_name,lender_capital_share,settled_with_lender_capital,value_date FROM Authentication.dbo.test  " +
            "where caption = 'apac'\n",nativeQuery = true)
    List<Object[]> getApac();

//    //========================================Format=1,4,9,12,14========================================================
//
//    @Query(value = "SELECT new com.bijlipay.Model.DTO.MisObject(m.txnDate,m.mid,m.tid,m.merchantName,m.city,m.cardNo, m.cardType,m.rRN ,m.associationName, " +
//            "m.txnamount,m.mrlmdr, m.gst, m.netamount,m.cardType1,m.tradename,m.paymentdate, m.narration) FROM " +
//            "bijlipay_mis_temp$ m where m.merchantName = ?1 ")
//    List<MisObject> findBySerachedMerChantFormatOneAndFourAndNineAndTwevelAndFourteen(String merchantName);
//
//    //===============================================Format=2==location=================================================
//
//    @Query(value = "SELECT new com.bijlipay.Model.DTO.MisObject(m.txnDate,m.mid,m.tid,m.merchantName,m.city,m.cardNo,m.cardType,m.rRN,m.associationName,  " +
//            "  m.txnamount,m.mrlmdr, m.gst, m.netamount,m.cardType1,m.tradename,m.location,m.paymentdate,m.narration)" +
//            " FROM bijlipay_mis_temp$ m where m.merchantName like ?1% ")
//    List<MisObject> findBySerachedMerChantFormatTwo(String merchantName);
//
//    //=================================================Format=3==mdr_percentage=========================================
//
//    @Query(value = "SELECT new com.bijlipay.Model.DTO.MisObject(m.txnDate,m.mid,m.tid,m.merchantName,m.city,m.cardNo, m.cardType,m.rRN ,m.associationName," +
//            "m.txnamount,m.mrlmdr, m.gst, m.netamount,m.cardType1,m.tradename,m.mdr_percentage,m.paymentdate,m" +
//            ".narration) FROM bijlipay_mis_temp$ m  where m.merchantName = ?1 ")
//    List<MisObject> findBySerachedMerChantFormatThree(String merchantName);
//
//    //================================================Format=5,6==aggregatorName========================================
//
//    @Query(value = "SELECT new com.bijlipay.Model.DTO.MisObject(m.txnDate,m.mid,m.tid,m.merchantName,m.city,m.cardNo, m.cardType,m.rRN ,m.associationName, " +
//            "m.txnamount,m.mrlmdr,m.gst,m.netamount,m.cardType1,m.tradename,m.paymentdate,m.aggregatorName,m" +
//            ".narration) FROM bijlipay_mis_temp$ m where m.merchantName = ?1")
//    List<MisObject> findBySerachedMerChantFormatFiveAndSix(String merchantName);
//
//    //=================================================Format=8======state==============================================
//
//    @Query(value = "SELECT new com.bijlipay.Model.DTO.MisObject(m.txnDate,m.mid,m.tid,m.merchantName,m.city,m.state,m.cardNo,m.cardType,m.rRN ,m.associationName, " +
//            "m.txnamount,m.mrlmdr, m.gst, m.netamount,m.cardType1,m.tradename,m.paymentdate, m.narration) FROM earth" +
//            ".Bijlipay.dbo.MisTable m where m.merchantName = ?1 ")
//    List<MisObject> findBySerachedMerChantFormatEight(String merchantName);
//
//    //===============================================Format=10,11==txnDate===============================================
//    // ADD txn Time
//    @Query(value = "SELECT new com.bijlipay.Model.DTO.MisObject(m.txnDate,m.txnDate,m.mid,m.tid,m.merchantName,m.city,m.cardNo, m.cardType,m.rRN ,m.associationName, " +
//            "m.txnamount,m.mrlmdr, m.gst, m.netamount,m.cardType1,m.tradename,m.paymentdate,m.narration) FROM earth" +
//            ".Bijlipay.dbo.MisTable m where m.mid = ?1 ")
//    List<MisObject> findBySerachedMerChantFormatTenAndEleven(String mid);
//
//    //===================================================Format=13====cardProduct=======================================
//
//    @Query(value = "SELECT new com.bijlipay.Model.DTO.MisObject(m.txnDate,m.mid,m.tid,m.merchantName,m.city,m.cardNo, m.cardType,m.rRN ,m.associationName, " +
//            "m.txnamount,m.mrlmdr,m.cardProduct, m.gst, m.netamount,m.cardType1,m.tradename, m.paymentdate, m.narration) FROM bijlipay_mis_temp$ m where m.merchantName = ?1 ")
//    List<MisObject> findBySerachedMerChantFormatThirteen(String merchantName);
//
//    //=================================================Format=15=====incentive==========================================
//
//    @Query(value = "SELECT new com.bijlipay.Model.DTO.MisObject(m.txnDate,m.mid,m.tid,m.merchantName,m.city,m.cardNo, m.cardType,m.rRN ,m.associationName, " +
//            "m.txnamount,m.mrlmdr, m.gst, m.netamount,m.cardType1,m.tradename, m.paymentdate, m.narration,m.incentive) FROM bijlipay_mis_temp$ m where m.cardNo  not like '%amt'  and  m.merchantName like ?1% ")
//    List<MisObject> findBySerachedMerChantFormatFifteen(String merchantName);

    @Query(value = "Select A.TID, sum(A.Netamount) Net_Value,B.Merchants_Establishment_Name,A.Date from " +
            "Bijlipay.dbo.clix_sett$ A left join Bijlipay.dbo.GPRS_MID_MASTER$ B on A.TID =B.Terminals_TID where" +
            " B.Merchants_Establishment_Name LIKE '%?1%' Group by A.TID,B.Merchants_Establishment_Name,A.Date",nativeQuery = true)
    List<Object[]> getReportByFormatOne(String name);

    @Query(value = "Select A.TID, sum(A.Netamount) Net_Value,A.Date from Bijlipay.dbo.clix_sett$ A  left join Bijlipay.dbo.GPRS_MID_MASTER$ B on A.TID =B.Terminals_TID where B.Merchants_Establishment_Name like '%MUKESH SUPER MART%' Group by A.TID,A.Date",nativeQuery = true)
    List<Object[]> getReportForMukeshSuperMart();

    @Query(value = "select Terminal_ID TID,Sum(Gross_Amount) Txn_amount,Legal_Name Merchant_Name, Settle Paymentdate from Bijlipay.dbo.IOB_HITACHI$ where Legal_Name LIKE 'WHITE MAGIC%' group by Terminal_ID,Legal_Name,Settle",nativeQuery = true)
    List<Object[]> getReportForWhiteMagic();

    @Query(value = "Select A.Txn_Date,\n" +
            "A.MID,\n" +
            "A.TID,\n" +
            "A.Merchant_Name,\n" +
            "A.City,\n" +
            "A.Card_No,\n" +
            "A.Card_Type,\n" +
            "A.RRN,\n" +
            "A.Association_Name,\n" +
            "A.Approval_Code,\n" +
            "A.Batch_Code,\n" +
            "A.Txnamount,\n" +
            "A.MDR,\n" +
            "A.GST,\n" +
            "A.Netamount,\n" +
            "A.Paymentdate,B.Terminals_Mcc from Bijlipay.dbo.Bijlipay_Mis$ A Left join Bijlipay.dbo.GPRS_MID_MASTER$ B on A.TID = B.Terminals_TID where A.Card_No like '%DING%' and A.Netamount !='-5' and B.mcc_code='5541' order by Paymentdate",nativeQuery = true)
    List<Object[]> getReportForFuel();

    @Query(value = "select Txn_Date,MID,TID,Merchant_Name,City,Card_No,Card_Type,RRN,Association_Name,Txnamount,MDR,GST,Netamount,Card_Type1,Tradename,Paymentdate,narration from Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name like '%VENDIMAN%'",nativeQuery = true)
    List<Object[]> getReportByFormatThree();

    @Query(value = "select TID,Sum(Txnamount) Txn_amount,Merchant_Name,Paymentdate from Bijlipay.dbo.Bijlipay_Mis$ where  Merchant_Name like 'Dimple Wines%' OR Merchant_Name like 'DIPTI WINES%' OR Merchant_Name like 'Esskay Wines' OR Merchant_Name like 'Globe Wines' group by TID,Merchant_Name,Paymentdate\n" +
            "Union All select Terminal_ID TID,Sum(Gross_Amount) Txn_amount,Legal_Name Merchant_Name, Settle Paymentdate from Bijlipay.dbo.IOB_HITACHI$ where Legal_Name like 'SANTOSH WINE' OR Legal_Name like 'NEW MAHARASHTRA WINES' OR Legal_Name like 'LORDS WINES' OR Legal_Name like 'SARVADE WINES' OR Legal_Name like 'SUYASH WINES' OR Legal_Name like 'KWALITY WINE SHOP' OR Legal_Name like 'SAROVAR WINES' group by Terminal_ID,Legal_Name,Settle",nativeQuery = true)
    List<Object[]> getReportByFormatFourSubFormat(String name);

    @Query(value = "Select A.TID, sum(A.Netamount) Net_Value,B.Merchants_Establishment_Name,A.Date from  Bijlipay.dbo.clix_sett$ A left join GPRS_MID_MASTER$ B on A.TID =B.Terminals_TID where B.Merchants_Establishment_Name in ('SHREE KVO JAIN MANAV SEVA KENDRA CHEMIST',\n" +
            "'SHRI KVO JAIN MANAV SEVA KENDRA CHEMIST DOMBIVALI',\n" +
            "'SHRI K V O JAIN MANAV SEVA KENDRA NALLASOPARA',\n" +
            "'SHRI KVO JAIN MANAV SEVA KENDRA MIRA ROAD',\n" +
            "'KVO JAIN MANAV SEVA KENDRA') Group by A.TID,B.Merchants_Establishment_Name,A.Date",nativeQuery = true)
    List<Object[]> getReportByFormatOneSubQuery();

    @Query(value = "select distinct A.TID Terminals_TID,A.MMID Terminals_MID,A.MAP_CREATED_DATE 'Created_Date',A.LEGAL_NAME Legal_Name,'GPRS' Terminals_Communication_Mode,B.Mcc_Des Terminals_Mcc,'ANDHRA BANK'Terminals_Bank,A.IFSC, left(A.BANKINFO_ACCOUNT_NO,8)+'*******' BANKINFO_ACCOUNT_NO from  Bijlipay.dbo.TID_Mars$ A left Join Bijlipay.dbo.MCCZ$ B on A.MERCINFO_MCC = B.Mcc_code where A.IFSC like 'ANDB00%' and A.TID not like '%M' and A.MAP_CREATED_DATE > '2019-12-09'\n" +
            "Union All\n" +
            "select Terminals_TID,Terminals_MID,Merchants_Created_Time 'Created_Date',Merchants_Establishment_Name 'Legal_Name','GPRS' Terminals_Communication_Mode,Terminals_Mcc ,'ANDHRA BANK'Terminals_Bank,Terminals_Ifsc_Code IFSC,left(Terminals_Account_Number,8)+'*******' BANKINFO_ACCOUNT_NO from  Bijlipay.dbo.GPRS_MID_MASTER$ where Terminals_Ifsc_Code like 'ANDB00%' ",nativeQuery = true)
    List<Object[]> getABGprsOutput();

    @Query(value = "Select A.Txn_Date,A.MID,A.TID,A.Merchant_Name,A.City,A.Card_No," +
            "A.Card_Type,A.RRN,A.Association_Name,A.Approval_Code,A.Batch_Code," +
            "A.Txnamount,A.MDR,A.GST,A.Netamount,A.Paymentdate,A.Aggregator_Name," +
            "A.Tradename,A.Terminal_Serial_No,A.Card_Type1,A.ASSOCIATION_fee,A.Narration," +
            "A.Card_Product,B.BPRegion from  Bijlipay.dbo.Bijlipay_Mis$ A left join Bijlipay.dbo.GPRS_MID_MASTER$ B on A.TID = B.Terminals_TID where A.RRN in (select RRN from  Bijlipay.dbo.Bijlipay_Mis$ where Card_No like '%EMI%') order by RRN,Txn_Date;",nativeQuery = true)
    List<Object[]> getEmiReport();

    @Query(value = "select A.TID,A.Card_Type,A.Narration, count(A.Txnamount) Nos ,Sum(A.Txnamount) Val from  Bijlipay.dbo.Bijlipay_Mis_MTD$ A where A.Paymentdate between DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1) and DATEADD(ms, -3, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 1)) and A.Txnamount>0  Group by A.TID,A.Card_Type,A.Narration",nativeQuery = true)
    List<Object[]> getFlashReport();

    @Query(value = "select TID,Sum(Txnamount) Txn_amount,Merchant_Name,Paymentdate from  Bijlipay.dbo.Bijlipay_Mis$ where  Merchant_Name like 'Dimple Wines%' OR Merchant_Name like 'DIPTI WINES%' OR Merchant_Name like 'Esskay Wines' OR Merchant_Name like 'Globe Wines' group by TID,Merchant_Name,Paymentdate\n" +
            "Union All\n" +
            "select Terminal_ID TID,Sum(Gross_Amount) Txn_amount,Legal_Name Merchant_Name, Settle Paymentdate from  Bijlipay.dbo.IOB_HITACHI$ where Legal_Name like 'SANTOSH WINE' OR Legal_Name like 'NEW MAHARASHTRA WINES' OR Legal_Name like 'LORDS WINES' OR Legal_Name like 'SARVADE WINES' OR Legal_Name like 'SUYASH WINES' OR Legal_Name like 'KWALITY WINE SHOP' OR Legal_Name like 'SAROVAR WINES' group by Terminal_ID,Legal_Name,Settle;",nativeQuery = true)
    List<Object[]> getReportForDODEJAGROUP();

    @Query(value = "select TID,Sum(Txnamount) Txn_amount,Merchant_Name,Paymentdate from  Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name like 'NARAIN WINE MART' group by TID,Merchant_Name,Paymentdate",nativeQuery = true)
    List<Object[]> getReportByUnitedGroup();

    @Query(value = "Select A.TID, sum(A.Netamount) Net_Value,B.Merchants_Establishment_Name,A.Date from  Bijlipay.dbo.clix_sett$ A left join Bijlipay.dbo.GPRS_MID_MASTER$ B on A.TID =B.Terminals_TID where B.Merchants_Establishment_Name in ('SHREE KVO JAIN MANAV SEVA KENDRA CHEMIST',\n" +
            "'SHRI KVO JAIN MANAV SEVA KENDRA CHEMIST DOMBIVALI',\n" +
            "'SHRI K V O JAIN MANAV SEVA KENDRA NALLASOPARA',\n" +
            "'SHRI KVO JAIN MANAV SEVA KENDRA MIRA ROAD',\n" +
            "'KVO JAIN MANAV SEVA KENDRA') Group by A.TID,B.Merchants_Establishment_Name,A.Date",nativeQuery = true)
    List<Object[]> getReportForKVO();

    @Query(value = "Select A.TID, sum(A.Netamount) Net_Value,B.Merchants_Establishment_Name,A.Date from  Bijlipay.dbo.clix_sett$ A left join Bijlipay.dbo.GPRS_MID_MASTER$ B on A.TID =B.Terminals_TID where B.Merchants_Establishment_Name like 'PINKY WINES%' Group by A.TID,B.Merchants_Establishment_Name,A.Date",nativeQuery = true)
    List<Object[]> getReportByPinky();

    @Query(value = "select TID, count(Txnamount) Txn_Count,sum(Txnamount) Txn_Amount from  Bijlipay.dbo.Bijlipay_Mis_MTD$ " +
            "where Paymentdate between DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1) and DATEADD(ms, -3, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 1)) and Txnamount > 0 and Narration in ('pos','Qr_txn','Sale') Group by TID",nativeQuery = true)
    List<Object[]> getReportForDcbSheet1();

    @Query(value = "select distinct A.TID Terminals_TID,A.MMID Terminals_MID,A.MAP_CREATED_DATE 'Created_Date',A.LEGAL_NAME Legal_Name,'GPRS' Terminals_Communication_Mode,B.Mcc_Des Terminals_Mcc,'DCB BANK'Terminals_Bank,A.IFSC, left(A.BANKINFO_ACCOUNT_NO,8)+'*******' BANKINFO_ACCOUNT_NO from  Bijlipay.dbo.TID_Mars$ A left Join Bijlipay.dbo.MCCZ$ B on A.MERCINFO_MCC = B.Mcc_code where A.IFSC like 'DCBL00%' and A.TID not like '%M' and A.MAP_CREATED_DATE > '2019-12-09'\n" +
            "Union All\n" +
            "select Terminals_TID,Terminals_MID,Merchants_Created_Time 'Created_Date',Merchants_Establishment_Name 'Legal_Name','GPRS' Terminals_Communication_Mode,Terminals_Mcc ,'DCB BANK'Terminals_Bank,Terminals_Ifsc_Code IFSC,left(Terminals_Account_Number,8)+'*******' BANKINFO_ACCOUNT_NO from  Bijlipay.dbo.GPRS_MID_MASTER$ where Terminals_Ifsc_Code like 'DCBL00%' ",nativeQuery = true)
    List<Object[]> getReportByDCBSheet2();

    @Query(value = "select TID, count(Txnamount) Txn_Count,sum(Txnamount) Txn_Amount,sum(MDR) MDR_Amount,sum(GST) GST_Amount,sum(Netamount) Net_Amount " +
            " from  Bijlipay.dbo.Bijlipay_Mis_MTD$ where Paymentdate between  DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1) and DATEADD(ms, -3, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 1)) and Txnamount > 0 and Narration in ('pos','Qr_txn','Sale','MICROATM') Group by TID",nativeQuery = true)
    List<Object[]> getReportForFinanceSheet1();

    @Query(value = "select TID, count(Txnamount) Txn_Count,sum(Txnamount) Txn_Amount,sum(MDR) MDR_Amount,sum(GST) GST_Amount,sum(Netamount) Net_Amount from  Bijlipay.dbo.Bijlipay_Mis_MTD$ where " +
            "Paymentdate between  DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1)  and DATEADD(ms, -3, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 1)) and Txnamount > 0 and Narration = 'Cash@pos' Group by TID",nativeQuery = true)
    List<Object[]> getReportForFinanceSheet2();

    @Query(value = "select TID, count(Txnamount) Txn_Count,sum(Txnamount) Txn_Amount from  [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Txnamount >= 1000 and Narration = 'Cash@pos' " +
            "and Paymentdate between DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1) and  DATEADD(ms, -3, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 1)) Group by TID",nativeQuery = true)
    List<Object[]> getRellerReport();

    @Query(value = "Select Distinct association_Name from Bijlipay.dbo.Bijlipay_Mis_MTD$",nativeQuery = true)
    List<Object[]> getReportForAmexAssociation();

    @Query(value = "select TID,paymentdate,Count(Txnamount) Txn_Count,sum(Txnamount) Txn_Amount from Bijlipay.dbo.Bijlipay_Mis$ where\n" +
            " Txnamount > 0 and association_Name ='amex' and Merchant_Name like 'shell%' Group by TID,paymentdate",nativeQuery = true)
    List<Object[]> getReportForAmexData();

    @Query(value = "select TID,count(Txnamount)TXN_Count,sum(Txnamount)TXN_Value from [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Txnamount >0 and " +
            "Paymentdate between  DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1)  and DATEADD(ms, -3, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 1))   Group by TID",nativeQuery = true)
    List<Object[]> getReportForMonthlyYtd();

    @Query(value = "select TID,count(Txnamount)TXN_Count,sum(Txnamount)TXN_Value from Bijlipay.dbo.Bijlipay_Mis_MTD$ where Txnamount >0 and " +
            "Paymentdate between  DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1) and DATEADD(ms, -3, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 1))   Group by TID",nativeQuery = true)
    List<Object[]> getReportForMonthlyMtd();

    @Query(value = "select m.TID,m.Terminals_MID,m.Merchant_name,right(m.Merchant_address1,10),right(m.Merchant_address2,10)\n" +
            ",m.Merchants_State,m.Gstr_no,d.[Txn <= 2k],d.[Txn > 2k],d.[Txn <= 2k -GST ],d.[Txn > 2k -GST],\n" +
            "d.[Txn <= 2k -MDR ],d.[Txn > 2k -MDR],l.invoice_no from earth.[Authentication].[dbo].[auto_merchants] m inner Join\n" +
            "(Select tid,\n" +
            "sum ( CASE  when txnamount <= 2000  THEN txnamount  else 0 end )  \"Txn <= 2k\" ,\n" +
            "sum (  CASE  when txnamount > 2000 THEN txnamount  else 0 end )  \"Txn > 2k\" ,\n" +
            "sum (  CASE  when txnamount <= 2000 THEN gst  else 0 end )  \"Txn <= 2k -GST \" ,\n" +
            "sum (  CASE  when txnamount > 2000 THEN gst  else 0 end ) \"Txn > 2k -GST\" ,\n" +
            "sum (  CASE  when txnamount <= 2000 THEN mdr  else 0 end )  \"Txn <= 2k -MDR \" ,\n" +
            "sum (  CASE  when txnamount > 2000 THEN mdr  else 0 end ) \"Txn > 2k -MDR\"  \n" +
            "from [192.168.5.61].[Transaction_Base].[dbo].[Bijlipay_Mis_YTD$]  where month=?1 and MDR>0 group by tid) d on d.tid=m.TID \n" +
            "inner join (Select tid,invoice_no from earth.[Authentication].[dbo].[auto_invoice_logs] where month=?1)l on l.tid=m.TID;",nativeQuery = true)
    List<Object[]> mdrInvoicePreviousMonthReport(String customizedMonth);

    @Query(value ="select Terminal_ID,Network,Settle, count(Gross_Amount) Txn_Count,sum(Gross_Amount) Txn_Amount,sum(Merchant_Discount_Fees) MDR_Amount,sum(GST) GST_Amount,sum(Merchant_Net_Payment) Net_Amount from bijlipay.dbo.IOB_HITACHI_MTD where Settle between ?1 and ?2 and Terminal_ID like '%A' and Identifier ='FDR' and Gross_Amount > 0 Group by Terminal_ID, Network,Settle",nativeQuery = true)
    List<Object[]> getConsolidatedCommissionDetails(String firstDay,String lastDay);

    @Query(value = " select Txn_Mode, Count(Gross_Amount) Txn_Count,Sum(Gross_Amount) Txn_Rs , 'DC <2K' 'Type'   from bijlipay.dbo.IOB_HITACHI_MTD where Gross_Amount between 0 and 2000 and Settle between DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1)  and DATEADD(ms, -1, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 0)) and Card_Type = 'Debit' and Terminal_ID like '%A' and Identifier ='FDR' Group by Txn_Mode;",nativeQuery = true)
    List<Object[]> getDebitCardLessThan2K();

    @Query(value = "select Txn_Mode, Count(Gross_Amount) Txn_Count,Sum(Gross_Amount) Txn_Rs, 'DC >2K' 'Type'  from bijlipay.dbo.IOB_HITACHI_MTD where Gross_Amount > 2000 and Settle between DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1) and DATEADD(ms, -1, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 0)) and Card_Type = 'Debit'  and Terminal_ID like '%A' and Identifier ='FDR' Group by Txn_Mode;",nativeQuery = true)
    List<Object[]> getDebitCardGreaterThan2K();

    @Query(value = "select count(Gross_Amount) Txn_Count,sum(Gross_Amount) Txn_Amount from bijlipay.dbo.IOB_HITACHI_MTD where Settle between DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1) and DATEADD(ms, -1, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 0)) and Left(Card_Number,6) in ('468756','450478','504481','404834','484762','484763','555942','502260','532711',\n" +
            "'461134','457474','607165','607322','607363','608107','652181','515630','555915') and Terminal_ID like '%A' and Identifier ='FDR'",nativeQuery = true)
    List<Object[]> getRbiSummary();

    @Query(value = "select * from bijlipay.dbo.IOB_HITACHI_MTD where Settle between DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 1) and DATEADD(ms, -1, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) + 1, 0)) and Left(Card_Number,6) in ('468756','450478','504481','404834','484762','484763','555942','502260','532711',\n" +
            "'461134','457474','607165','607322','607363','608107','652181','515630','555915') and Terminal_ID like '%A' and Identifier ='FDR'",nativeQuery = true)
    List<Object[]> getRbiData();

    @Query(value = "Select Paymentdate 'SETTLEMENTDATE',Count(Txnamount)'TXNCOUNT',Sum(Txnamount)'TXNAMOUNT' from bijlipay.dbo.IOBA Group by Paymentdate",nativeQuery = true)
    List<Object[]> getSettlementIOBSummary();

    @Query(value = "Select * from bijlipay.dbo.IOBA;",nativeQuery = true)
    List<Object[]> getSettlementIOBData();

    @Query(value = "Select Paymentdate 'SETTLEMENTDATE',Count(Txnamount)'TXNCOUNT',Sum(Txnamount)'TXNAMOUNT' from bijlipay.dbo.FDR_MTD Group by Paymentdate",nativeQuery = true)
    List<Object[]> getSettlementFDRSummary();

    @Query(value = "Select Merchant_Name 'MERCHANT_NAME','GPRS'As 'TERMINAL',Sum(Txnamount) as amount from bijlipay.dbo.FDR_MTD Group by Merchant_Name \n",nativeQuery = true)
    List<Object[]> getSettlementFDRSummaryTwo();

    @Query(value = "Select * from bijlipay.dbo.FDR_MTD",nativeQuery = true)
    List<Object[]> getSettlementFDRData();

    @Modifying
    @Transactional
    @Query(value = "insert into Bijlipay.dbo.ace_merchant_details(city,amount,card_scheme,masked_card_number,mdr,merchant_id,rrn,settlement_amount,terminal_id,txn_date,txn_time,txn_type,status)\n" +
            "select  city,txnamount,Association_Name,Card_No,mdr,mid,rrn,Netamount,tid,convert(varchar,Txn_Date,23),convert(varchar,Txn_Date,8),Narration,Approval_Code from Authentication.dbo.auto_bijlipay_mis_temp$ \n" +
            " where  mid in (SELECT mid FROM Bijlipay.dbo.ace_mids group by mid) and Txnamount > 0;" +
            "" +
            "update Bijlipay.dbo.ace_merchant_details SET app_code='-',status='00', billing_number='-',urn='-'  \n" +
            "where merchant_id IN (SELECT mid FROM Bijlipay.dbo.ace_mids group by mid) and app_code is null",nativeQuery = true)
    void insertIntoAceTableFromAuthTable();


    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.BijlipaySettlement(Association_Name,Card_No,Card_Type,City,GST,MDR,Merchant_Name,MID,Netamount, Paymentdate,RRN,TID,Txnamount,Txn_Date,Upload_date)\n" +
            "SELECT Txn_Date,MID,TID,Merchant_Name,City,ifsc,Card_No,card_type,RRN,Association_Name,Txnamount,mdr,GST,Netamount,Paymentdate,'CORP',CONVERT (date, GETDATE())\n" +
            "FROM [Authentication].[dbo].[auto_bijlipay_mis_temp$] where Txn_Date=?1",nativeQuery = true)
            void insertCorp(String date);


    @Query(value = "select BANKINFO_IFSC_CODE from TSDB.dbo.burgerdata where TID =?1",nativeQuery = true)
    String getIfsc(String tid);




}