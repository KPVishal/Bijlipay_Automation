package com.bijlipay.Repository;

import com.bijlipay.Model.DTO.AxisDTO;
import com.bijlipay.Model.DTO.BjSwitchSettlementDto;
import com.bijlipay.Model.DTO.BjTransactionDto;
import com.bijlipay.Model.DTO.KVBDTo;
import com.bijlipay.Model.DailySettlement;
import com.bijlipay.Model.FederalDTO;
import com.bijlipay.Model.MisTable;
import com.bijlipay.Model.SibReport;
import com.bijlipay.Utils.DateUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface AxisRepository extends JpaRepository<AxisDTO, Long> {
    int countBydate(String oneDayBefore);

    @Query(value = "SELECT TOP(10)* FROM Authentication.dbo.axis_report ORDER BY createAt desc", nativeQuery = true)
    List<AxisDTO> getLat10records();

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bijlipayTransaction([txn_additional_amount],[batch_number],[invoice_number],[merchant_id]," +
            "[mti],[rrn_number],[response_received_time],[stan],[terminal_id],[txn_amount],[txn_date],[txn_response_code],[txn_time]," +
            "[txn_type],[urn]) " +
            "VALUES (:#{#data.TxnAdditionalAmount},:#{#data.batchNumber},:#{#data.invoiceNumber},:#{#data.merchantId}," +
            ":#{#data.mti},:#{#data.rRNumber},CONVERT(datetime2,:#{#data.responseReceivedTime}),:#{#data.stan},:#{#data.terminalId}," +
            ":#{#data.txnAmount},:#{#data.txnDate},:#{#data.txnResponseCode},:#{#data.txnTime},:#{#data.txnType},:#{#data.urn})", nativeQuery = true)
    void insertAxisDataToTsdbDatabase(@Param("data") BjTransactionDto data);

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bijlipayTransaction([txn_additional_amount],[batch_number],[invoice_number],[merchant_id]," +
            "[mti],[rrn_number],[response_received_time],[stan],[terminal_id],[txn_amount],[txn_date],[txn_response_code],[txn_time]," +
            "[txn_type],[urn]) " +
            "VALUES (:#{#data.TxnAdditionalAmount},:#{#data.batchNumber},:#{#data.invoiceNumber},:#{#data.merchantId}," +
            ":#{#data.mti},:#{#data.rRNumber},:#{#data.responseReceivedTime},:#{#data.stan},:#{#data.terminalId}," +
            ":#{#data.txnAmount},:#{#data.txnDate},:#{#data.txnResponseCode},:#{#data.txnTime},:#{#data.txnType},:#{#data.urn})"
            , nativeQuery = true)
    void insertFedralDataToTsdbDatabase(@Param("data") BjTransactionDto data);

    @Query(value = "SELECT top 1convert(varchar,Paymentdate, 23)FROM TSDB.dbo.BijlipaySettlement WHERE Typeidentifier = 'ME-PAYOUT' order by Paymentdate desc", nativeQuery = true)
    String fetchInitTime();

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bj_transaction([AggregatorName],[ASSOCIATION_fee]," +
            "[AssociationName],[authCode],[Batch_Code],[CardNo],[CardProduct],[cardType],[CardType1],[city],\n" +
            "[gst],[mdr],[merchantName],[mid],[Netamount],[Paymentdate],[rRN],[tid],[Tradename]," +
            "[transactionType],[txnDate],[txnamount])" +
            "values(:#{#data. aggregatorName},:#{#data. assocationcost},:#{#data. associationName}," +
            ":#{#data.authCode},:#{#data.batchCode},:#{#data.cardNo},:#{#data.cardProduct}," +
            ":#{#data.cardType},:#{#data.cardType1},:#{#data.city},:#{#data.gstamount}," +
            ":#{#data.mdramount},:#{#data.merchantName},:#{#data.mid},:#{#data.netamount}," +
            ":#{#data.paymentdate},:#{#data.rRN},:#{#data.tid},:#{#data.tradename}," +
            ":#{#data.transactiontype},:#{#data.txnDate},:#{#data.txnamount})", nativeQuery = true)
    void insertKvbDataToTsdbDatabase(@Param("data") KVBDTo data);

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bijlipayTransaction([txn_additional_amount],[batch_number],[invoice_number],[merchant_id]," +
            "[mti],[rrn_number],[response_received_time],[stan],[terminal_id],[txn_amount],[txn_date],[txn_response_code],[txn_time]," +
            "[txn_type],[urn]) " +
            "VALUES (:#{#data.TxnAdditionalAmount},:#{#data.batchNumber},:#{#data.invoiceNumber},:#{#data.merchantId}," +
            ":#{#data.mti},:#{#data.rRNumber},:#{#data.responseReceivedTime},:#{#data.stan},:#{#data.terminalId}," +
            ":#{#data.txnAmount},:#{#data.txnDate},:#{#data.txnResponseCode},:#{#data.txnTime},:#{#data.txnType},:#{#data.urn})"
            , nativeQuery = true)
    void insertSibDataToTsdbDatabase(@Param("data") BjTransactionDto data);

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bj_settlement([ins_code],[prj_serial_number],[mid],[tid],[paymentMode]," +
            "[payment_to],[paymentId],[merchantName],[city],[CardNo],[mcc],[MCC_Desc],[transactionType],[card_sub_category]," +
            "[card_category],[agr_net_payment],[agrGrossAmount],[merchantMdr],[mdrDiscFee],[sgst],[cgst]," +
            "[igst],[incentive],[tds],[min_util_fees],[mer_payment],[agrRslrMdr],[agrRslrDiscFees],[agrRslrSgst]," +
            "[agrRslrCgst],[agrRslrIgst],[agrRslrInstv],[agrRslrTds],[agrRslrMinFees],[net_payment],[txn_sharing_amount]," +
            "[mdr_sharing_amount],[msg_type],[trace_no],[rRN],[resp_code],[auth_id_rsp],[txnDate],[posEntryMode],[setl_date]," +
            "[upload_date],[onnus_offus],[cardType],[txn_mode],[network],[remarks],[CardProduct],[ifsc],[account],[instance],[switch_type])" +
            "values(:#{#data.insCode},:#{#data.prjSerialNumber},:#{#data.mid},:#{#data.tid},:#{#data.paymentMode},:#{#data.paymentTo}," +
            ":#{#data.paymentId},:#{#data.merchantName},:#{#data.city},:#{#data.cardNo},:#{#data.mcc},:#{#data.mcc_Desc},:#{#data.transactionType}," +
            " :#{#data.cardSubCategory},:#{#data.cardCategory},:#{#data.agrNetPayment},:#{#data.agrGrossAmount},:#{#data.merchantMdr},:#{#data.mdrDiscFee}," +
            " :#{#data.sgst},:#{#data.cgst},:#{#data.igst},:#{#data.incentive},:#{#data.tds},:#{#data.minUtilFees},:#{#data.merPayment},:#{#data.agrRslrMdr}," +
            " :#{#data.agrRslrDiscFees},:#{#data.agrRslrSgst},:#{#data.agrRslrCgst},:#{#data.agrRslrIgst},:#{#data.agrRslrInstv},:#{#data.agrRslrTds}," +
            " :#{#data.agrRslrMinFees},:#{#data.netPayment},:#{#data.txnSharingAmount},:#{#data.mdrSharingAmount},:#{#data.msgType},:#{#data.traceNo}," +
            " :#{#data.rRN},:#{#data.respCode},:#{#data.authIdRsp},:#{#data.txnDate},:#{#data.posEntryMode},:#{#data.setlDate},:#{#data.uploadDate}," +
            " :#{#data.onnusOffus},:#{#data.cardType},:#{#data.txnMode},:#{#data.network},:#{#data.remarks},:#{#data.cardProduct},:#{#data.ifsc}," +
            " :#{#data.account},:#{#data.instance},:#{#data.switchType})"
            , nativeQuery = true)
    void insertDataIntoBjSwitchSettlement(@Param("data") BjSwitchSettlementDto data);

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bj_settlement([txnDate],[mid],[tid],[merchantName]" +
            " ,[city],[CardNo],[cardType],[rRN],[AssociationName],[Approval_Code],[Batch_Code],[txnamount]" +
            ",[mdr],[gst],[Netamount],[Paymentdate],[AggregatorName],[Tradename],[TerminalSerialNo],[CardType1]" +
            ",[ASSOCIATION_fee],[Narration],[CardProduct],[instance])" +
            "values(:#{#data.txnDate},:#{#data.mid},:#{#data.tid},:#{#data.merchantName}," +
            ":#{#data.city},:#{#data.cardNo},:#{#data.cardType},:#{#data.rRN},:#{#data.associationName},:#{#data.approvalCode}," +
            ":#{#data.batchCode},:#{#data.txnamount},:#{#data.mrlmdr},:#{#data.gst},:#{#data.netamount},:#{#data.paymentdate}," +
            ":#{#data.aggregatorName},:#{#data.tradename},:#{#data.terminalSerialNo},:#{#data.cardType1},:#{#data.assocationFee}," +
            " :#{#data.narration},:#{#data.cardProduct},'CORPORATION')"
            , nativeQuery = true)
    void insertCorpDataToTsdbDatabase(@Param("data") MisTable data);

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bj_settlement([txnDate],[mid],[tid],[merchantName],[city]," +
            "[account],[ifsc],[txn_count],[txnamount],[mdr],[gst],[Netamount],[CardProduct]," +
            "[cardType],[ASSOCIATION_fee],[switch_type])values(:#{#data.date},:#{#data.date},:#{#data.mid}," +
            ":#{#data.tid},:#{#data.merchantName},:#{#data.city},:#{#data.accountNumber},:#{#data.branchCode}," +
            ":#{#data.Txncount},:#{#data.txnamount},:#{#data.mrlmdr},:#{#data.gst},:#{#data.netamount},:#{#data.cardproduct}," +
            ":#{#data.card_Type},:#{#data.associationfee})"
            , nativeQuery = true)
    void insertCorpSettlementToDatabase(@Param("data") DailySettlement data);

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bj_transaction([Batch_Code],[invoice_number],[mid],[mti],[rRN],[response_received_time]," +
            "[stan],[tid],[txnamount],[txnDate],[txn_response_code],[txnTime],[txn_type],[txn_additional_amount]," +
            "[urn])" +
            "SELECT [batch_number],[invoice_number],[merchant_id],[mti],[rrn_number],[response_received_time]," +
            "[stan],[terminal_id],[txn_amount],[txn_date],[txn_response_code],[txn_time],[txn_type]," +
            "[txn_additional_amount],[urn] From bank.dbo.switch_transactions Where response_received_time > :data AND response_received_time <= GETDATE()"
            , nativeQuery = true)
    Integer insertSwitchTransactionToDatabase(@Param("data") String fetchTime);

    @Modifying
    @Transactional
    @Query(value = "Insert into TSDB.dbo.bj_transaction([Batch_Code],[invoice_number],[mid],[mti],[rRN],[response_received_time]," +
            "[stan],[tid],[txnamount],[txnDate],[txn_response_code],[txnTime],[txn_type],[txn_additional_amount]," +
            "[urn])" +
            "SELECT [batch_number],[invoice_number],[merchant_id],[mti],[rrn_number],[response_received_time]," +
            "[stan],[terminal_id],[txn_amount],[txn_date],[txn_response_code],[txn_time],[txn_type]," +
            "[txn_additional_amount],[urn] From bank.dbo.switch_transactions Where response_received_time > DATEADD(minute, -5,  GETDATE()) ;"
            , nativeQuery = true)
    Integer insertSwitchTransactionToDatabaseFull();
    @Query(value = "SELECT top 1(response_received_time) FROM TSDB.dbo.bj_transaction order by response_received_time desc", nativeQuery = true)
    String fetchTransactionInitTime();

    @Query(value = "SELECT top 1convert(varchar,Paymentdate, 23) FROM TSDB.dbo.BijlipayQrSettlement WHERE Typeidentifier = 'ME-PAYOUT' order by Paymentdate desc", nativeQuery = true)
    String fetchInitTimeForQR();
}
