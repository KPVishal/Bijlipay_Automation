package com.bijlipay.Repository;

import com.bijlipay.Model.CurrentReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CurrentReportRepository extends CrudRepository<CurrentReport, String> {


    //Search With Merchant Name
    @Query(value = "SELECT Distinct(Merchants_Establishment_Name) AS Merchant_Name FROM bijlipay.dbo.GPRS_MID_MASTER$ where Merchants_Establishment_Name like ?1% ", nativeQuery = true)
    List<String> getMerchantByNameWithSearch(String merchantname);

    //Search with merchant name AND mid
    @Query(value = "SELECT Distinct(Merchants_Establishment_Name,mid) FROM bijlipay.dbo.GPRS_MID_MASTER$ where Merchants_Establishment_Name = ?1 AND mid like ?2%", nativeQuery = true)
    List<String> getmidByNameWithSearch(Optional<String> merchantname, String mid);

    //Search with merchant name AND mid AND tid
    @Query(value = "SELECT Distinct(Merchants_Establishment_Name,mid,tid) FROM [192.168.5.61].transaction_base.dbo.GPRS_MID_MASTER$ where Merchants_Establishment_Name = ?1 AND mid=?2 AND tid like ?3%", nativeQuery = true)
    List<String> gettidByNameWithSearch(Optional<String> merchantname, Optional<String> mid, String tid);

    // get merchant name
    @Query(value = "SELECT Distinct(Merchants_Establishment_Name) AS Merchant_Name FROM bijlipay.dbo.GPRS_MID_MASTER$ ", nativeQuery = true)
    List<String> getMerchantByName();

//>>>>>>>>>>test query

    // current month (merchant_name,mid,tid)
    @Query(value = "SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getCurrentMonthReport(String merchant_name, String longToDate, String longToDate2, List<String> midResult, List<String> tidArrayResult);

    // previous month (merchant_name,mid,tid)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getdownloadPrevious(String merchant_name, String longToDate, String longToDate2, List<String> midResult, List<String> tidArrayResult);

    // previous last (merchant_name,mid,tid)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product " +
            "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate >=?2 AND Paymentdate <=?3 ORDER BY paymentdate asc", nativeQuery = true)
    List<String> getdownloadLastPrevious(String merchant_name, String longToDate, String longToDate2, List<String> midResult, List<String> tidArrayResult);

    // previous AND previous last (merchant_name,mid,tid)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND mid in (?4) AND tid in (?5) AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getdownloadLastPreviousLast(String merchant_name, String longToDate, String longToDate2, List<String> midResult, List<String> tidArrayResult);

    //tid
    @Query(value = " SELECT Distinct(Terminals_TID) AS tid FROM bijlipay.dbo.GPRS_MID_MASTER$ where Merchants_Establishment_Name=?1 ", nativeQuery = true)
    List<String> getTidbyme(String merchantname);

    @Query(value = " SELECT Distinct(Terminals_TID) AS tid FROM bijlipay.dbo.GPRS_MID_MASTER$ where Terminals_MID in (?1) ", nativeQuery = true)
    List<String> getTid(List<String> testTid);

    //mid
    @Query(value = "SELECT Distinct(Terminals_MID) AS mid FROM bijlipay.dbo.GPRS_MID_MASTER$ where Merchants_Establishment_Name=?1", nativeQuery = true)
    List<String> getMidByName(Optional<String> merchantname);

    // current month (merchant_name)
    @Query(value = "SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getCurrentMonthReport(String merchant_name, String longToDate, String longToDate2);

    // current month (merchant_name only)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product "
            + " FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3 ORDER BY paymentdate asc", nativeQuery = true)
    List<CurrentReport> getMeCurrentMonthReport(List<String> meResult, String longToDate, String longToDate2);

    // current month (mid only)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product "
            + " FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where mid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3  ORDER BY paymentdate asc", nativeQuery = true)
    List<CurrentReport> getMidCurrentMonthReport(List<String> midResult, String longToDate, String longToDate2);

    // current month (tid only)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product "
            + " FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where tid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3 ORDER BY paymentdate asc ", nativeQuery = true)
    List<CurrentReport> getTidCurrentMonthReport(List<String> tidResult, String longToDate, String longToDate2);

    // current month (merchant_name,mid)
    @Query(value = "SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getCurrentMonthReport(String merchant_name, String longToDate, String longToDate2, List<String> midResult);

    // previous month (merchant_name)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1 AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getdownloadPrevious(String merchant_name, String longToDate, String longToDate2);

    // previous month (merchant_name only)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106) , ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106) , ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getMedownloadPrevious(List<String> meResult, String longToDate, String longToDate2);

    // previous month (mid only)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where mid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where mid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getMiddownloadPrevious(List<String> midResult, String longToDate, String longToDate2);

    // previous month (tid only)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where tid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where tid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getTiddownloadPrevious(List<String> tidResult, String longToDate, String longToDate2);

    // previous month (merchant_name,mid)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getdownloadPrevious(String merchant_name, String longToDate, String longToDate2, List<String> midResult);

    // previous last (merchant_name)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product " +
            "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1  AND Paymentdate >=?2 AND Paymentdate <=?3 ORDER BY Paymentdate asc", nativeQuery = true)
    List<String> getdownloadLastPrevious(String merchant_name, String longToDate, String longToDate2);

    // previous last month (merchant_name only)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product " +
            "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3 ORDER BY Paymentdate asc", nativeQuery = true)
    List<String> getMedownloadLastPrevious(List<String> meResult, String longToDate, String longToDate2);

    // previous last month (mid only)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product " +
            "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where mid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3 ORDER BY Paymentdate asc", nativeQuery = true)
    List<String> getMiddownloadLastPrevious(List<String> midResult, String longToDate, String longToDate2);

    // previous last month (tid only)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product " +
            "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where tid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3 ORDER BY Paymentdate asc", nativeQuery = true)
    List<String> getTiddownloadLastPrevious(List<String> tidResult, String longToDate, String longToDate2);

    // previous last (merchant_name,mid)
    @Query(value = "SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS payment_date,card_type1,narration,card_product " +
            "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate >=?2 AND Paymentdate <=?3 ORDER BY Paymentdate asc", nativeQuery = true)
    List<String> getdownloadLastPrevious(String merchant_name, String longToDate, String longToDate2, List<String> midResult);

    // previous AND previous last (merchant_name)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1 AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getdownloadLastPreviousLast(String merchant_name, String longToDate, String longToDate2);

    // previous AND previous last (Merchant_Name only)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product  "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getMedownloadLastPreviousLast(List<String> meResult, String longToDate, String longToDate2);

    // previous AND previous last (mid only)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where mid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where mid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getMiddownloadLastPreviousLast(List<String> midResult, String longToDate, String longToDate2);

    // previous AND previous last (tid only)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where tid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where tid in (?1) AND Paymentdate >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getTiddownloadLastPreviousLast(List<String> tidResult, String longToDate, String longToDate2);

    // previous AND previous last (merchant_name,mid)
    @Query(value = " SELECT txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product "
            + "FROM((SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date "
            + "FROM [192.168.5.61].Transaction_Base.dbo.Bijlipay_Mis_YTD$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "Union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            "FROM Bijlipay.dbo.Bijlipay_Mis_MTD$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate >=?2 AND Paymentdate <=?3) "
            + "union all (SELECT REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code," +
            "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product,paymentdate as payment_date " +
            " FROM Bijlipay.dbo.Bijlipay_Mis$ where Merchant_Name=?1 AND mid in (?4) AND Paymentdate  >=?2 AND Paymentdate <=?3))dum ORDER BY payment_date asc ", nativeQuery = true)
    List<CurrentReport> getdownloadLastPreviousLast(String merchant_name, String longToDate, String longToDate2, List<String> midResult);

    @Query(value="Select REPLACE(CONVERT(VARCHAR(11), txn_date, 106), ' ', '-') AS txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,"
            + "batch_code,txnamount,mdr,gst,netamount,REPLACE(CONVERT(VARCHAR(11), paymentdate, 106), ' ', '-') AS paymentdate,card_type1,narration,card_product "
            + " from  Transaction_Base.dbo.Bijlipay_Mis_MTD$ where mid =?1 AND Paymentdate >=DATEADD(ms, -3, DATEADD(mm, DATEDIFF(m, 0, GETDATE()) - 1, 2)) " +
            " and Paymentdate <= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0) order by paymentdate asc",nativeQuery=true)
    List<Object[]> getMidCurrentBasedMonthReport(String mid);
}
