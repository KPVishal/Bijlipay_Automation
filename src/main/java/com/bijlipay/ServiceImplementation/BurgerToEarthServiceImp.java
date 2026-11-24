package com.bijlipay.ServiceImplementation;

import com.bijlipay.Service.BurgerToEarthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.bijlipay.Utils.Constants.*;

@Service
public class BurgerToEarthServiceImp implements BurgerToEarthService {
    private final Logger log = LoggerFactory.getLogger(BurgerToEarthServiceImp.class);

    public Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URLEARTH, DBUSERNAMEEARTH, DBPASSWORDEARTH);
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Connection getMiraConnection() {
        try {
            Connection con = DriverManager.getConnection(URLMIRA, DBUSERNAMEMIRA, DBPASSWORDMIRA);
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void burgerDataTransfer() {
        try {
            String query = "";
            log.info("Data transfer process started--------------->");
            Connection conn = null;
            Statement stmtt = null;

            conn = getConnection();
            stmtt = conn.createStatement();

            String dropTableIfExists = "IF OBJECT_ID('TSDB.dbo.burgerData', 'U') IS NOT NULL DROP TABLE TSDB.dbo.burgerData;";
            log.info("DROP TABLE IF EXISTS FOR burgerData UPDATE: {}", dropTableIfExists);
            stmtt.executeUpdate(dropTableIfExists);

            stmtt.close();
            conn.close();

            conn = getConnection();
            stmtt = conn.createStatement();
            log.info("burgerDAta started successfully");
            query = "SELECT * INTO TSDB.dbo.burgerData FROM [MIRA].Transaction_Base.dbo.UVW_Burger_KBL;";
            stmtt.execute(query);

            query = "INSERT INTO TSDB.dbo.burgerData SELECT * FROM [Venus].[Transaction_base].[dbo].[UVW_Burger_Axis];";
            stmtt.execute(query);
            log.info("burgerDAta finished successfully");
            log.info("update started successfully");
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_INDIAN OVERSEAS BANK' WHERE MAP_LEAD_FROM = 'LS_INDIAN OVERSEAS BANK_WIRECARD';";
            stmtt.execute(query);
            log.info("update finished successfully iob");

            log.info("update started for aubank successfully");
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_AU SMALL FINANCE BANK LIMITED' WHERE MAP_LEAD_FROM = 'LS_AU SMALL FINANCE BANK LIMITED_WIRECARD';";
            stmtt.execute(query);
            log.info("update finished successfully aubank");

            log.info("update started for Dcbbank successfully");
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_DCB BANK LIMITED' WHERE MAP_LEAD_FROM = 'LS_DCB BANK LIMITED_WIRECARD';";
            stmtt.execute(query);
            log.info("update finished successfully Dcbbank");

            log.info("update started for dhanalakshmibank successfully");
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_DHANALAKSHMI BANK' WHERE MAP_LEAD_FROM = 'LS_DHANALAKSHMI BANK_WIRECARD';";
            stmtt.execute(query);
            log.info("update finished successfully dhanalakshmi bank");

            log.info("update started for equatasbank successfully");
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_EQUITAS SMALL FINANCE BANK LIMITED' WHERE MAP_LEAD_FROM = 'LS_EQUITAS SMALL FINANCE BANK LIMITED_TMCC';";
            stmtt.execute(query);
            log.info("update finished successfully equatas bank");

            log.info("update started for karnataka bank successfully");
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_KARNATAKA BANK LIMITED' WHERE MAP_LEAD_FROM = 'LS_KARNATAKA BANK LIMITED_WIRECARD';";
            stmtt.execute(query);
            log.info("update finished successfully karnataka bank");

            log.info("update started for karurvaysya bank successfully");
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_KARUR VYSYA BANK' WHERE MAP_LEAD_FROM = 'LS_KARUR VYSYA BANK_WIRECARD';";
            stmtt.execute(query);
            log.info("update finished successfully karurvaysya bank");

            log.info("update started for union bank successfully");
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_UNION BANK OF INDIA' WHERE MAP_LEAD_FROM = 'LS_UNION BANK OF INDIA_WIRECARD';";
            stmtt.execute(query);
            log.info("update finished successfully union bank");

            stmtt.close();
            conn.close();
            log.info("----------------------Process Finished------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tsdbBackup() throws SQLException {
        Connection con = null;
        Statement stmt = null;
        con = getConnection();
        stmt = con.createStatement();
        String query = "";
        log.info("SETTLEMENT DATA BACKUP START");
        query = "INSERT INTO TSDB.dbo.BijlipaySettlement_2023 (Association_Name,Card_No,Card_Type,City,GST,ifsc,Typeidentifier,MDR,Merchant_Name,MID,Netamount,Paymentdate,RRN,TID,Txnamount,Txn_Date,Upload_date) " +
                "SELECT Association_Name,Card_No,Card_Type,City,GST,ifsc,Typeidentifier,MDR,Merchant_Name,MID,Netamount,Paymentdate,RRN,TID,Txnamount,Txn_Date,Upload_date FROM TSDB.dbo.BijlipaySettlement bs WHERE bs.Paymentdate <= DATEADD(day,-60, GETDATE())";
        stmt.executeUpdate(query);
        log.info("SETTLEMENT DELETE START");
        query = "DELETE FROM TSDB.dbo.BijlipaySettlement WHERE Paymentdate <= DATEADD(day,-60, GETDATE());";
        stmt.executeUpdate(query);
        log.info("TRANSACTION BACKUP START");
        query = "INSERT INTO TSDB.dbo.bijlipayTransaction_2023 (txn_additional_amount,batch_number,invoice_number,merchant_id,mti,rrn_number,response_received_time,stan,terminal_id,txn_amount,txn_date,txn_response_code,txn_time,txn_type,urn) " +
                "SELECT txn_additional_amount,batch_number,invoice_number,merchant_id,mti,rrn_number,response_received_time,stan,terminal_id,txn_amount,txn_date,txn_response_code,txn_time,txn_type,urn From TSDB.dbo.bijlipayTransaction WHERE response_received_time <= DATEADD(day,-60, GETDATE())";
        stmt.executeUpdate(query);
        log.info("DELETE TRANSACTION START");
        query = "DELETE FROM TSDB.dbo.bijlipayTransaction WHERE response_received_time <= DATEADD(day,-60, GETDATE())";
        stmt.executeUpdate(query);
        log.info("ALL PROCESS FINISHED");
        stmt.close();
        con.close();
    }

    public void burgerDataUpdate(){
        try {
            String query = "";
            log.info("update process started--------------->");
            Connection conn = null;
            Statement stmtt = null;
            conn = getConnection();
            stmtt = conn.createStatement();
            query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM  = 'LS_INDIAN OVERSEAS BANK' WHERE MAP_LEAD_FROM = 'LS_INDIAN OVERSEAS BANK_WIRECARD'; ";
            stmtt.execute(query);
            stmtt.close();
            conn.close();
            log.info("---------------------- update process Finished------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void riskHoldTransfer() {

        try {
            Connection conn = null;
            Statement stmtt = null;
            conn = getConnection();
            stmtt = conn.createStatement();
            log.info("Transfer started successfully");
            String query = "SELECT * INTO TSDB.dbo.riskholdData FROM [192.168.5.61].Transaction_Base.dbo.VIEW_HOLD_TRANSACTIONS;";
            stmtt.execute(query);
            log.info("data transferred successfully");
            stmtt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
