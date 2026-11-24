package com.bijlipay.Controller;

import com.bijlipay.Service.BurgerToEarthService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.PGPDecryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.bijlipay.Utils.Constants.LOCALFILEPATH_AXIS;

@Component
@RestController
@RequestMapping("api")
public class BurgerToEarthController extends SwitchSettlementConfigController {
    private final Logger logger = LoggerFactory.getLogger(BurgerToEarthController.class);

    //    change the db properties to uat in burgerToearth service before testing this controller in uat
    @Autowired
    BurgerToEarthService burgerToEarthService;

    @Scheduled(cron = "0 5 4,23 * * * ")
    @GetMapping("/burgertoearth")
    //Transfer data from burgertable to earth tsdb;
    public ResponseEntity<?> transferDate() throws SQLException {
        final Logger log = LoggerFactory.getLogger(MiraToMercedesController.class);
        final int MAX_RETRIES = 10;

        int result;
        int attempts = 0;
        boolean success = false;
        while (attempts < MAX_RETRIES && !success) {
            attempts++;
            Connection con = null;
            Statement stmt = null;

            try {
                con = getConnection();
                stmt = con.createStatement();
               logger.info("----- Calling Axis New Data Base 10.10.0.72 to insert into Master DR ----");
                String dropTableIfExists = "IF OBJECT_ID('TSDB.dbo.burgerData', 'U') IS NOT NULL DROP TABLE TSDB.dbo.burgerData;";
                logger.info("DROP TABLE IF EXISTS FOR MERCEDES BURGER UPDATE: {}", dropTableIfExists);
                stmt.executeUpdate(dropTableIfExists);

                stmt.close();
                con.close();

                con = getConnection();
                stmt = con.createStatement();

                String burgerTransfer = "SELECT * INTO TSDB.dbo.burgerData FROM [192.168.5.61].Transaction_Base.dbo.UVW_Burger_KBL;";
                logger.info("CURRENT QUERY FOR MERCEDES BURGER UPDATE: {}", burgerTransfer);
                result = stmt.executeUpdate(burgerTransfer);

                String additionalTransfer = "INSERT INTO TSDB.dbo.burgerData SELECT * FROM [10.10.0.72].[Transaction_base].[dbo].[UVW_Burger_Axis];";
                logger.info("VENUS DATAS MERGER: {}", additionalTransfer);
                stmt.executeUpdate(additionalTransfer);

                String query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_INDIAN OVERSEAS BANK' WHERE MAP_LEAD_FROM = 'LS_INDIAN OVERSEAS BANK_WIRECARD';";
                stmt.execute(query);
                log.info("update finished successfully iob");

                log.info("update started for aubank successfully");
                query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_AU SMALL FINANCE BANK LIMITED' WHERE MAP_LEAD_FROM = 'LS_AU SMALL FINANCE BANK LIMITED_WIRECARD';";
                stmt.execute(query);
                log.info("update finished successfully aubank");

                log.info("update started for Dcbbank successfully");
                query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_DCB BANK LIMITED' WHERE MAP_LEAD_FROM = 'LS_DCB BANK LIMITED_WIRECARD';";
                stmt.execute(query);
                log.info("update finished successfully Dcbbank");

                log.info("update started for dhanalakshmibank successfully");
                query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_DHANALAKSHMI BANK' WHERE MAP_LEAD_FROM = 'LS_DHANALAKSHMI BANK_WIRECARD';";
                stmt.execute(query);
                log.info("update finished successfully dhanalakshmi bank");

                log.info("update started for equatasbank successfully");
                query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_EQUITAS SMALL FINANCE BANK LIMITED' WHERE MAP_LEAD_FROM = 'LS_EQUITAS SMALL FINANCE BANK LIMITED_TMCC';";
                stmt.execute(query);
                log.info("update finished successfully equatas bank");

                log.info("update started for karnataka bank successfully");
                query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_KARNATAKA BANK LIMITED' WHERE MAP_LEAD_FROM = 'LS_KARNATAKA BANK LIMITED_WIRECARD';";
                stmt.execute(query);
                log.info("update finished successfully karnataka bank");

                log.info("update started for karurvaysya bank successfully");
                query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_KARUR VYSYA BANK' WHERE MAP_LEAD_FROM = 'LS_KARUR VYSYA BANK_WIRECARD';";
                stmt.execute(query);
                log.info("update finished successfully karurvaysya bank");

                log.info("update started for union bank successfully");
                query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_UNION BANK OF INDIA' WHERE MAP_LEAD_FROM = 'LS_UNION BANK OF INDIA_WIRECARD';";
                stmt.execute(query);
                log.info("update finished successfully union bank");

                log.info("update started for Federal Bank successfully");
                query = "UPDATE TSDB.dbo.burgerData SET MAP_LEAD_FROM = 'LS_FEDERAL BANK' WHERE MAP_LEAD_FROM = 'LS_Federal_Direct';";
                stmt.execute(query);
                log.info("update finished successfully Federal Bank");

                log.info("update started for TERMINAL_STATUS Active");
                query = "UPDATE TSDB.dbo.burgerData SET TERMINAL_STATUS = 'Active' WHERE  TERMINAL_STATUS='Activate';";
                stmt.execute(query);
                log.info("update finished successfully TERMINAL_STATUS Active");

                log.info("update started for TERMINAL_STATUS Deactive");
                query = "UPDATE TSDB.dbo.burgerData SET TERMINAL_STATUS = 'Deactive' WHERE  TERMINAL_STATUS='Deactivate';";
                stmt.execute(query);
                log.info("update finished successfully TERMINAL_STATUS Deactive");

                stmt.close();
                con.close();

                success = true;

            } catch (SQLException e) {
                logger.error("SQLException encountered during attempt {} of {}. Retrying...", attempts, MAX_RETRIES, e);
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        logger.error("Error closing statement", ex);
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Error closing connection", ex);
                    }
                }

                if (attempts == MAX_RETRIES) {
                    logger.error("Max retries reached. Could not complete the operation.");
                }
            }
        }
        return ResponseEntity.ok("success");
    }

   @Scheduled(cron = "0 0 0 * * * ")
    @GetMapping("/tsdbdatabackup")
    public ResponseEntity<?> fetchBackupTsdb() throws SQLException {
        try {
            burgerToEarthService.tsdbBackup();
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "PROCESS FINISHED"), HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "EXCEPTION OCCURRED", e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    //Transfer data from riskhold to earth tsdb;
    //@Scheduled(cron = "0 0 4 * * * ")
    @GetMapping("/riskhold")
    public ResponseEntity<?> riskHoldTransfer() throws SQLException {

        Connection con = null;
        Statement stmt = null;
        try {
            con = burgerToEarthService.getConnection();
            stmt = con.createStatement();
            String query = "DROP TABLE TSDB.dbo.riskholdData;";
            boolean result = stmt.execute(query);
            if (!result) {
                result = true;
            }
            logger.info("Table dropped!!! ----------->{}", result);
            stmt.close();
            con.close();
            burgerToEarthService.riskHoldTransfer();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(con !=null){
                con.close();
            }
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success"), HttpStatus.OK);
    }
}
