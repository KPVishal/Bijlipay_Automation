//package com.bijlipay.SchedulerController;
//
//import com.bijlipay.Service.BankService;
//import com.bijlipay.Service.MisService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import static com.bijlipay.Utils.Constants.*;
//
//public class MisMarsDataSchedular {
//    private static final Logger log = LoggerFactory.getLogger(MisMarsDataSchedular.class);
//    private Connection con = null;
//    @Autowired
//    private MisService misService;
//    @Autowired
//    private BankService bankService;
//
//
//    //    @Scheduled(cron = "0 11 1-7,14-20 * 1-6")
//    //    @Scheduled(cron = " 0 0 11 * * *")
//    @Scheduled(cron = "0 10 1-7,15-20,29-31 * 1-6")
//    @GetMapping("/mis-mars-data-schedular")
//    public void misProcess() throws Exception {
//
//
//        log.info("Bijlipay_Mis$ start to delete");
//        deleteMisTable();
//        log.info("Inserting into Bijlipay_Mis$==============");
//        saveMisDataToTable();//to save payment_data_work table records into auto_bijlipay_mis_temp$ table
//        log.info("inserted successfully into auto_bijlipay_mis_temp$ table");
//        mistempUpdate();
//        misInsert();
//        try{log.info("Current Date Bank Mis Records Insert Start");
//            bankService.uploadBijlipayMisForBankPortal();
//            log.info("Current Date Bank Mis Records Insert Over");
//            bankService.updateMisTable();
//            log.info("Current Date Bank Mis Update Over");
//        }
//        catch (Exception e)
//        {log.info("Error Current Date Mis Upload {}",e.getMessage());}
//        try{misService.insertDailyRecords();}catch (Exception e){e.printStackTrace();}
//        log.info("deleted auto_daily_sett records");
//        misService.deleteystrecord();
//
//        misService.saveDailySettlementDataToTable(BIJLIPAY_SETTLEMENT_XLSX__MIS);
//
//    }
//
//    public void saveMisDataToTable() {
//        getConnection();
//        try {
//            Statement statement = con.createStatement();
//            for (String query : AUTOTEMPMISINSERT) {
//                int executeUpdate = statement.executeUpdate(query);
//                log.info("auto_temp_table updated rows {}", executeUpdate);
//            }
//            con.close();
//        } catch (SQLException e) {
//            log.info("Could not update auto_bijlipay_mis_temp$" + e.getMessage());
//        }
//    }
//
//    public void mistempUpdate() {
//        getConnection();
//        try {
//            Statement statement = con.createStatement();
//            for (String query : MISUPDATE) {
//                int executeUpdate = statement.executeUpdate(query);
//                log.info("updated rows {}", executeUpdate);
//            }
//            con.close();
//        } catch (SQLException e) {
//            log.info("Could not update auto_bijlipay_mis_temp$" + e.getMessage());
//        }
//    }
//    public void getConnection() {
//        try {
//            con = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void deleteMisTable() throws SQLException {
//        getConnection();
//        con.prepareStatement(DELETEMISTABLE).executeUpdate();
//        con.close();
//    }
//    public void misInsert() throws SQLException {
//        getConnection();
//        log.info("Bijlipay_Mis$ records insert");
//        con.prepareStatement(INSERTINTOMIS).execute();
//        con.close();
//    }
//}
