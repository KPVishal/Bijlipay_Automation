package com.bijlipay.Controller;


import com.bijlipay.Service.BurgerToEarthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@RestController
@RequestMapping("api")
public class MiraToMercedesController extends MercedisConfigController {

    private final Logger logger = LoggerFactory.getLogger(BurgerToEarthController.class);
    @Autowired
    BurgerToEarthService burgerToEarthService;

    @Scheduled(cron = "0 0 19,1 * * * ")
    @GetMapping("/burgertomercedes")
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

                String dropTableIfExists = "IF OBJECT_ID('Transaction_Base.dbo.BurgerData', 'U') IS NOT NULL DROP TABLE Transaction_Base.dbo.BurgerData;";
                logger.info("DROP TABLE IF EXISTS FOR MERCEDES BURGER UPDATE: {}", dropTableIfExists);
                stmt.executeUpdate(dropTableIfExists);

                stmt.close();
                con.close();

                con = getConnection();
                stmt = con.createStatement();

                String burgerTransfer = "SELECT * INTO Transaction_Base.dbo.BurgerData FROM [MIRA].Transaction_Base.dbo.UVW_Burger_KBL;";
                logger.info("CURRENT QUERY FOR MERCEDES BURGER UPDATE: {}", burgerTransfer);
                result = stmt.executeUpdate(burgerTransfer);

                String additionalTransfer = "INSERT INTO Transaction_Base.dbo.BurgerData SELECT * FROM [Venus].[Transaction_base].[dbo].[UVW_Burger_Axis];";
                logger.info("VENUS DATAS MERGER: {}", additionalTransfer);
                stmt.executeUpdate(additionalTransfer);

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
}
