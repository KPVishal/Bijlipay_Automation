package com.bijlipay.Controller;

import com.bijlipay.Service.BjSwitchSettlementService;
import com.bijlipay.Service.TsdbService;
import com.bijlipay.Utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Component
@RestController
@RequestMapping("/api")
public class BjSwitchSettlementController extends SwitchSettlementConfigController {
    private final Logger logger = LoggerFactory.getLogger(BaseController.class);
    int result;
    @Autowired
    private TsdbService tsdbService;
    @Autowired
    private BjSwitchSettlementService bjSwitchSettlementService;

    @Value("${vyappar.settlement.txn.url}")
    private String vyapparSettlementTxnSyncUrl;

    @Value("${vyappar.basic.auth.user}")
    private String basicAuthUsername;
    @Value("${vyappar.basic.auth.password}")
    private String basicAuthPassword;

    //Report for Ops Protal settlement upload Api
    //POS device
    @GetMapping("/bjswitchsettlement")
    public ResponseEntity<?> bjSwitchSettlement() throws SQLException {
        int rowsAffected = 0;
        Connection con = null;
        Statement stmt = null;
//        List<Object[]> totalDatas = new ArrayList<>();
        try {
            logger.info("To connection the  earth DataBase ----->");
            con = getConnection();
            stmt = con.createStatement();
            String query = "";
            String initTime = "";
            initTime = tsdbService.fetchInitTime();
            logger.info("To get Paymentdate ---->{}",initTime!=null?initTime:"NA");
            if (initTime != null) {
                query = "INSERT INTO TSDB.dbo.BijlipaySettlement(" +
                        "Txn_Date,MID,TID,Merchant_Name,City," +
                        "ifsc,Card_No,Card_Type,RRN,Association_Name,Txnamount,MDR,GST,Netamount," +
                        "Paymentdate,Typeidentifier,Upload_date)" +
                        "SELECT MPW_TXN_DATE,MPW_MERCHANT_ID,MPW_TERMINAL_ID,MPW_LEGAL_NAME,MPW_CITY,MPW_ME_IFSC," +
                        " MPW_CARD_NUMBER,MPW_CARD_TYPE,MPW_RRN,MPW_NETWORK,MPW_GROSS_AMOUNT," +
                        " MPW_MER_DISC_FEES,(MPW_SGST + MPW_CGST + MPW_IGST) AS gst,MPW_MER_PAYMENT,MPW_SETL_DATE,('ME-PAYOUT') as typeidentifier,MPW_UPLOAD_DATE FROM [192.168.5.61].ME_Payout.ME_PO.PAYMENT_DATA_WORK WHERE MPW_SETL_DATE " +
                        "  > '" + initTime + "' AND MPW_SETL_DATE <= convert(varchar, getdate(), 23) AND MPW_TXN_TYPE IN ('Sale','Pos') ORDER BY MPW_SETL_DATE DESC;";
            } else {
                query = "INSERT INTO TSDB.dbo.BijlipaySettlement(" +
                        "Txn_Date,MID,TID,Merchant_Name,City," +
                        "ifsc,Card_No,Card_Type,RRN,Association_Name,Txnamount,MDR,GST,Netamount," +
                        "Paymentdate,Typeidentifier,Upload_date)" +
                        "SELECT MPW_TXN_DATE,MPW_MERCHANT_ID,MPW_TERMINAL_ID,MPW_LEGAL_NAME,MPW_CITY,MPW_ME_IFSC," +
                        "MPW_CARD_NUMBER,MPW_CARD_TYPE,MPW_RRN,MPW_NETWORK,MPW_GROSS_AMOUNT," +
                        "MPW_MER_DISC_FEES,(MPW_SGST + MPW_CGST + MPW_IGST) AS gst,MPW_MER_PAYMENT,MPW_SETL_DATE,('ME-PAYOUT') as typeidentifier,MPW_UPLOAD_DATE FROM [192.168.5.61].ME_Payout.ME_PO.PAYMENT_DATA_WORK" +
                        "WHERE MPW_SETL_DATE = convert(varchar, getdate(), 23) AND MPW_TXN_TYPE IN ('Sale','Pos') ORDER BY MPW_SETL_DATE DESC;";
            }
            String updateIfsc = "UPDATE m SET m.ifsc = bankinfo_ifsc_code FROM TSDB.dbo.BijlipaySettlement m " +
                    "INNER JOIN (SELECT bankinfo_ifsc_code,TID  FROM TSDB.dbo.burgerData)f ON m.tid = f.tid WHERE m.Paymentdate > '" + initTime + "' AND m.Paymentdate <= convert(varchar, getdate(), 23);";
            String updateFields = "UPDATE s " +
                    "SET s.batch_number = t.batch_number, " +
                    "    s.invoice_number = t.invoice_number, " +
                    "    s.mti = t.mti, " +
                    "    s.txn_type = t.txn_type, " +
                    "    s.stan = t.stan " +
                    "FROM TSDB.dbo.BijlipaySettlement s " +
                    "INNER JOIN TSDB.dbo.bijlipayTransaction t ON t.rrn_number = s.RRN " +
                    "WHERE s.Txn_Date >= DATEADD(DAY, -3, CONVERT(DATE, GETDATE())) AND s.stan IS NULL;";

            String updateTxnStatus = "UPDATE t SET t.settlement_status = 'Settled' FROM bijlipayTransaction t INNER JOIN BijlipaySettlement s ON t.rrn_number = s.RRN WHERE s.RRN IS NOT NULL AND t.response_received_time >= DATEADD(day, -3, GETDATE());";
            logger.info("SWITCH-TXN : Current Query : {}", query);
            result = stmt.executeUpdate(query);
            stmt.close();
            con.close();

            con = getConnection();
            stmt = con.createStatement();
            logger.info("CURRENT QUERY FOR SETTLEMENT DATA UPDATE : {}", updateIfsc);
            rowsAffected = stmt.executeUpdate(updateIfsc);
            logger.info("NO OF ROWS UPDATED SETTLEMENT DATA UPDATE {}",rowsAffected);
            stmt.close();
            con.close();

            con = getConnection();
            stmt = con.createStatement();
            logger.info("CURRENT QUERY FOR MERCH PORTAL SETTLEMENT UPDATE : {}", updateFields);
            rowsAffected = stmt.executeUpdate(updateFields);
            logger.info("NO OF ROWS UPDATED MERCH PORTAL SETTLEMENT DATA UPDATE {}",rowsAffected);
            stmt.close();
            con.close();

            con = getConnection();
            stmt = con.createStatement();
            logger.info("CURRENT QUERY FOR MERCH PORTAL SETTLEMENT UPDATE : {}", updateTxnStatus);
            rowsAffected = stmt.executeUpdate(updateTxnStatus);
            logger.info("NO OF ROWS UPDATED MERCH PORTAL TXN DATA UPDATE {}",rowsAffected);
            stmt.close();
            con.close();

        } catch (SQLException e) {
            logger.info("The ANTERA ,PAYTIVO device for SQR syncing Error :---------->{}",e.getMessage());
            e.printStackTrace();
        }
        CompletableFuture.runAsync(this::updateSettlementTxnData);
        if (result > 0) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Data transferred successfully!", result), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Data already transfer!", result), HttpStatus.OK);
        }
    }

    private void updateSettlementTxnData(){
        logger.info(" To connecting the vyappar APi -------->");
        String url = vyapparSettlementTxnSyncUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(basicAuthUsername,basicAuthPassword);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>( headers);
        try {
            logger.info("The vyappar url -------->{}",url!=null?url:"NA");
            ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
            logger.info(" Vyappar Response Status -----> {}", response.getStatusCode());
            logger.info(" VyapparResponse Body -------> {}", response.getBody());
        }catch (HttpClientErrorException clientErrorException){
            logger.info("client Error Exception response code, response body-----------: {}{}",clientErrorException.getRawStatusCode(),clientErrorException.getResponseBodyAsString());
            clientErrorException.printStackTrace();
        }catch (HttpServerErrorException serverErrorException){
            logger.info("server Error Exception response code, response body -----------: {}{}",serverErrorException.getRawStatusCode(),serverErrorException.getResponseBodyAsString());
            serverErrorException.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
// SQR
    @Scheduled(cron = "0 57 11 * * *")
    @GetMapping("/bjSwitchQrSettlement")
    public ResponseEntity<?> bjSwitchQrSettlement() throws SQLException {
        Connection con = null;
        Statement stmt = null;
        try {
            logger.info("To connection the  earth DataBase ----->");
            con = getConnection();
            stmt = con.createStatement();
            String query = "";
            String initTime = "";
            initTime = tsdbService.fetchInitTimeForQR();
            logger.info("To get Paymentdate ---->{}",initTime!=null?initTime:"NA");
            if (initTime != null) {
                query = "INSERT INTO TSDB.dbo.BijlipayQrSettlement(" +
                        "Txn_Date,MID,TID,Merchant_Name,City," +
                        "ifsc,Card_No,Card_Type,RRN,Association_Name,Txnamount,MDR,GST,Netamount," +
                        "Paymentdate,Typeidentifier,Upload_date,MDRValue)" +
                        "SELECT MPW_TXN_DATE,MPW_MERCHANT_ID,MPW_TERMINAL_ID,MPW_LEGAL_NAME,MPW_CITY,MPW_ME_IFSC," +
                        " MPW_CARD_NUMBER,MPW_CARD_TYPE,MPW_RRN,MPW_NETWORK,MPW_GROSS_AMOUNT," +
                        " MPW_MERCHANT_MDR as MDR,(MPW_SGST + MPW_CGST + MPW_IGST) AS gst," +
                        "MPW_NET_PAYMENT,MPW_SETL_DATE,('ME-PAYOUT') as typeidentifier,MPW_UPLOAD_DATE,MPW_MER_DISC_FEES " +
                        "FROM [192.168.5.61].ME_Payout.ME_PO.PAYMENT_DATA_WORK WHERE MPW_SETL_DATE " +
                        "  > '" + initTime + "' AND MPW_SETL_DATE <= convert(varchar, getdate(), 23) AND " +
                        " MPW_NETWORK ='UPI' AND MPW_INSTANCE='UPI' ORDER BY MPW_SETL_DATE DESC;";
            } else {
                query = "INSERT INTO TSDB.dbo.BijlipayQrSettlement(" +
                        "Txn_Date,MID,TID,Merchant_Name,City," +
                        "ifsc,Card_No,Card_Type,RRN,Association_Name,Txnamount,MDR,GST,Netamount," +
                        "Paymentdate,Typeidentifier,Upload_date,MDRValue)" +
                        "SELECT MPW_TXN_DATE,MPW_MERCHANT_ID,MPW_TERMINAL_ID,MPW_LEGAL_NAME,MPW_CITY,MPW_ME_IFSC," +
                        "MPW_CARD_NUMBER,MPW_CARD_TYPE,MPW_RRN,MPW_NETWORK,MPW_GROSS_AMOUNT," +
                        "MPW_MERCHANT_MDR as MDR,(MPW_SGST + MPW_CGST + MPW_IGST) AS gst,MPW_NET_PAYMENT," +
                        "MPW_SETL_DATE,('ME-PAYOUT') as typeidentifier,MPW_UPLOAD_DATE,MPW_MER_DISC_FEES " +
                        "FROM [192.168.5.61].ME_Payout.ME_PO.PAYMENT_DATA_WORK " +
                        "WHERE MPW_NETWORK ='UPI' AND MPW_INSTANCE='UPI' ORDER BY MPW_SETL_DATE DESC;";
            }
            String updateVpa =null;
            if(initTime !=null){
                 updateVpa = "UPDATE m SET m.credit_vpa = f.VPA FROM TSDB.dbo.BijlipayQrSettlement m " +
                        "INNER JOIN (SELECT VPA,TID FROM TSDB.dbo.burgerData)f ON m.TID = f.TID WHERE m.Paymentdate > '" + initTime + "' AND m.Paymentdate <= convert(varchar, getdate(), 23);";
            }else{
                 updateVpa = "UPDATE m SET m.credit_vpa = f.VPA FROM TSDB.dbo.BijlipayQrSettlement m " +
                        "INNER JOIN (SELECT VPA,TID FROM TSDB.dbo.burgerData)f ON m.TID = f.TID;";
            }

            String updateSettlementStatus = "UPDATE t SET t.settlement_status = 'Settled' FROM customer_app.dbo.static_qr_txn t INNER JOIN " +
                    "TSDB.dbo.BijlipayQrSettlement s ON t.rrn = s.RRN WHERE s.RRN IS NOT NULL AND t.created_at >= DATEADD(MONTH, -3, GETDATE());";

            logger.info("SWITCH-QR-TXN : Current Query : {}", query);
            result = stmt.executeUpdate(query);
            stmt.close();
            con.close();

            con = getConnection();
            stmt = con.createStatement();
            logger.info("CURRENT QUERY FOR SETTLEMENT DATA VPA UPDATE : {}", updateVpa);
            stmt.executeUpdate(updateVpa);
            stmt.close();
            con.close();

            con = getConnection();
            stmt = con.createStatement();
            logger.info("CURRENT QUERY FOR SETTLEMENT STATUS  UPDATE : {}", updateSettlementStatus);
            stmt.executeUpdate(updateSettlementStatus);
            stmt.close();
            con.close();

        } catch (SQLException e) {
            logger.info("The ANTERA ,PAYTIVO  for POS device syncing Error :---------->{}",e.getMessage());
            e.printStackTrace();
        }
        if (result > 0) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Qr Data transferred successfully!", result), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Data already transferred!", result), HttpStatus.OK);
        }
    }


}
