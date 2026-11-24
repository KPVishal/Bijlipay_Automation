package com.bijlipay.SchedulerController;

import com.bijlipay.Service.BankService;
import com.bijlipay.Service.MisService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;
import com.jcraft.jsch.JSchException;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static com.bijlipay.Utils.Constants.*;

@Component
@RestController
@RequestMapping("/api")
public class MisScheduler {
    private static final Logger log = LoggerFactory.getLogger(MisScheduler.class);
    private boolean firstrow = true;
    private Connection con = null;
    @Autowired
    private MisService misService;
    @Autowired
    private BankService bankService;


    @Async
//    @Scheduled(cron="* 30/13 11-12 * * *")
//    @Scheduled(cron = "0 10,15,20 18 * * *")
    @GetMapping("/mis-scheduler")
    public void misProcess() throws Exception {
        if (new File(FILEDIRECTORY_MIS + DateUtil.currentDate(ddMMyy)+".zip").exists()
        && new File(FILEDIRECTORY_MIS + BIJLIPAY_MIS_XLSX__MIS).exists()) {
            log.info("MIS File Already Exits");
        } else {
            log.info("MIS Process Started By : " + System.getProperty("user.name"));
            boolean result = misService.connectToSFTPFileDownload();
            if (result) {
                FileInputStream file = null;
                Workbook wb = null;
                try {
                    file = new FileInputStream(new File(SCHEDULER));
                    wb = StreamingReader.builder()
                            .rowCacheSize(100)
                            .bufferSize(4096).open(file);
                    log.info("MIS auto_bijlipay_mis_temp$ records start to delete");
                    misService.deletemistemp();
                    for (Sheet sheet : wb) {
                        firstrow = true;
                        for (Row row : sheet) {
                            if (firstrow) {
                                firstrow = false;
                                continue;
                            }
                            if (DateUtil.currentDate(MMddyy).equals(DateUtil.formatForSch(row.getCell(1).getStringCellValue()))) {
                                if (YES.equals(row.getCell(2).getStringCellValue())) {
                                    log.info("MIS Bijlipay_Mis$ start to delete");
                                    deleteMisTable();
                                    try {misService.deleteRentTable();}catch (Exception e){e.printStackTrace();}
                                }
                                if (YES.equals(row.getCell(3).getStringCellValue())) {
                                    misService.saveMisDataToTable(FILEDIRECTORY_MIS + BIJLIPAY_MIS_XLSX__MIS);
                                    mistempUpdate();
                                    misInsert();
                                    try{log.info("Current Date Bank Mis Records Insert Start");
                                        bankService.uploadBijlipayMisForBankPortal();
                                        log.info("Current Date Bank Mis Records Insert Over");
                                        bankService.updateMisTable();
                                        log.info("Current Date Bank Mis Update Over");
                                    }
                                    catch (Exception e)
                                    {log.info("Error Current Date Mis Upload {}",e.getMessage());}
                                    try{misService.insertDailyRecords();
                                    }catch (Exception e){e.printStackTrace();}
                                }
                                if (YES.equals(row.getCell(4).getStringCellValue())) {
                                    try{
                                      /*  misService.sentMail();*/
                                        log.info("MIS File Mail Sent Successfully");
                                    }catch (Exception e){
                                        System.out.println("Error Starting to Sent mail");
                                    }
                                }
                                if (YES.equals(row.getCell(5).getStringCellValue())) {
                                    log.info("deleted auto_daily_sett records");
                                    misService.deleteystrecord();
//                                    log.info("deleted daily_sett records");
//                                    misdeleteDailySett();
                                }
                                if (YES.equals(row.getCell(6).getStringCellValue())) {
                                    misService.saveDailySettlementDataToTable(BIJLIPAY_SETTLEMENT_XLSX__MIS);
//                                    DailySettInsert();
                                }
                            }
                        }
                    }
                    wb.close();
                    file.close();
                    log.info("Process Over");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.gc();
                }
            } else {
                log.info("MIS File Process Not Done! ");
            }
        }
    }

    private void misdeleteDailySett() throws SQLException {
        try {
            getConnection();
            log.info("daily_sett start to delete");
            con.prepareStatement(DELETEDAILYSETT).executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    private void DailySettInsert() throws SQLException {
        try {
            getConnection();
            log.info("daily_sett start to insert");
            con.prepareStatement(INSERTINTODAILYSETT).execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    private void savetomtd() throws SQLException {
        getConnection();
        boolean stmt = con.prepareStatement(INSERTINTOMTD).execute();
        log.info(" Insert INTO Done");
        con.close();
    }

    public void deleteMisTable() throws SQLException {
        getConnection();
        con.prepareStatement(DELETEMISTABLE).executeUpdate();
        con.close();
    }

    public void misInsert() throws SQLException {
        getConnection();
/*        log.info("Bijlipay_Mis$ records insert");
        con.prepareStatement(INSERTINTOMIS).execute();*/
        log.info("TSDB settlement records insert");
        log.info("start inserting into BijlipaySettlement");
        con.prepareStatement(CORPDATA).execute();
        log.info("inserted done on BijlipaySettlement ");
        con.close();
    }

    public void mistempUpdate() {
        getConnection();
        try {
            Statement statement = con.createStatement();
            for (String query : MISUPDATE) {
                int executeUpdate = statement.executeUpdate(query);
                log.info("updated rows {}", executeUpdate);
            }
            con.close();
        } catch (SQLException e) {
            log.info("Could not update auto_bijlipay_mis_temp$" + e.getMessage());
        }
    }

    @Scheduled(cron = " 0 0 6 * * *")
    protected void misTempDelete() throws SQLException {
        FileInputStream file = null;
        Workbook wb = null;
        try {
            file = new FileInputStream(new File(SCHEDULER));
            wb = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096).open(file);
            firstrow = true;
            for (Row row : wb.getSheetAt(0)) {
                if (firstrow) {
                    firstrow = false;
                    continue;
                }
                if (DateUtil.currentDate(MMddyy).equals(DateUtil.formatForSch(row.getCell(1).getStringCellValue()))) {
                    if (YES.equals(row.getCell(2).getStringCellValue())) {
                        try {
                            deleteMisTable();
                            log.info("6 AM Mis Delete Process Over");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            wb.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("6 AM Mis Delete Process Not Done");
        }

    }

    public void getConnection() {
        try {
            con = DriverManager.getConnection(URL_Auth, DBUSERNAME, DBPASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Scheduled(cron = " 0 0 8-11 * * *")
    protected void federalSettlementReport() throws SQLException {
        FileInputStream file = null;
        Workbook wb = null;
        try {
            file = new FileInputStream(new File(SCHEDULER));
            wb = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096).open(file);
            firstrow = true;
            for (Row row : wb.getSheetAt(0)) {
                if (firstrow) {
                    firstrow = false;
                    continue;
                }
                if (DateUtil.currentDate(MMddyy).equals(DateUtil.formatForSch(row.getCell(1).getStringCellValue()))) {
                    if (YES.equals(row.getCell(6).getStringCellValue())) {
                        try {
                            final String givenDate = DateUtil.currentDate(ddMMyyyy);
                            if (new File(FILEDIRECTORY_MIS +
                                    BIJLIPAY_SETTLEMENT_REPORT + givenDate + ".xlsx").exists()) {
                                log.info("federalSettlementReport insert process over already done");
                            } else {
                                boolean download = false;
                                try {
                                    download = misService.connectToSFTPDownloadWithSpecificDate(Optional.empty());
                                    if (download) {
                                        misService.federalSettlementReport(FILEDIRECTORY_MIS +
                                                BIJLIPAY_SETTLEMENT_REPORT + givenDate + ".xlsx");
                                    }else{
                                        log.info("federalSettlementReport file not available in the sftp");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            log.info("federalSettlementReport insert process over");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            wb.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("federalSettlementReport Process Not Done");
        }

    }




    @Async
/*    @Scheduled(cron="0 35,45,50 11-12 * * *")*/
//    @Scheduled(cron = "0 20,25,30 15 * * *")
    @GetMapping("/mis-scheduler-corp")
    public void misProcessCorp() throws Exception {
        if (new File(FILEDIRECTORY_MIS + DateUtil.currentDate(ddMMyy)+".zip").exists()
                && new File(FILEDIRECTORY_MIS + BIJLIPAY_MIS_XLSX__MIS).exists()) {
            log.info("MIS File Already Exits");
        } else {
            log.info("MIS Process Started By : " + System.getProperty("user.name"));
            log.info("This is CORP data");
            boolean result = misService.connectToSFTPFileDownload();
            if (result) {
                FileInputStream file = null;
                Workbook wb = null;
                try {
                    file = new FileInputStream(new File(SCHEDULER));
                    wb = StreamingReader.builder()
                            .rowCacheSize(100)
                            .bufferSize(4096).open(file);
                    log.info("MIS auto_bijlipay_mis_temp$ records start to delete");
                    misService.deletemistemp();
                    for (Sheet sheet : wb) {
                        firstrow = true;
                        for (Row row : sheet) {
                            if (firstrow) {
                                firstrow = false;
                                continue;
                            }
                            if (DateUtil.currentDate(MMddyy).equals(DateUtil.formatForSch(row.getCell(1).getStringCellValue()))) {

                                if (YES.equals(row.getCell(3).getStringCellValue())) {
                                    misService.saveMisDataToTable(FILEDIRECTORY_MIS + BIJLIPAY_MIS_XLSX__MIS);
                                    misInsert();
                                }
                            }
                        }
                    }
                    wb.close();
                    file.close();
                    log.info("Process Over");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("MIS File Process Not Done! ");
            }
        }
    }

   /* @Scheduled(cron = "0 0 6 * * *")*/
   @Scheduled(cron="* 30/15 11-12 * * *")
    @GetMapping("/MisCase11-scheduler/sync-corp-data")
    public void corpDataSync() throws JSchException {
        if (new File(FILEDIRECTORY_MIS + DateUtil.currentDate(ddMMyy)+".zip").exists()
                && new File(FILEDIRECTORY_MIS + BIJLIPAY_MIS_XLSX__MIS).exists()) {
            log.info("MIS File Already Exits");
        } else {
            log.info("MIS Process Started By : " + System.getProperty("user.name"));
            log.info("This is CORP data");
            boolean result = misService.connectToSFTPFileCorpDownload();
            if (result) {
                FileInputStream file = null;
                Workbook wb = null;
                try {
                    file = new FileInputStream(new File(SCHEDULER));
                    wb = StreamingReader.builder()
                            .rowCacheSize(100)
                            .bufferSize(4096).open(file);
                    log.info("MIS auto_bijlipay_mis_temp$ records start to delete");
                    misService.deletemistemp();
                    for (Sheet sheet : wb) {
                        firstrow = true;
                        for (Row row : sheet) {
                            if (firstrow) {
                                firstrow = false;
                                continue;
                            }
                            if (DateUtil.currentDate(MMddyy).equals(DateUtil.formatForSch(row.getCell(1).getStringCellValue()))) {

                                if (YES.equals(row.getCell(3).getStringCellValue())) {
                                    misService.saveMisDataToTable(FILEDIRECTORY_MIS + BIJLIPAY_MIS_XLSX__MIS);
                                    misInsert();
                                }
                            }
                        }
                    }
                    wb.close();
                    file.close();
                    log.info("Process Over");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("MIS File Process Not Done! ");
            }
        }
    }


}
