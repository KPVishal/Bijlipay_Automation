package com.bijlipay.Controller;

import com.bijlipay.Service.BankService;
import com.bijlipay.Service.MisService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.DateUtil;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static com.bijlipay.Utils.Constants.*;

@RestController
@RequestMapping("/api")
public class MisController {
    private static final Logger log = LoggerFactory.getLogger(MisController.class);
    private boolean firstrow = true;
    private Connection con = null;
    @Autowired
    private MisService misService;
    @Autowired
    private BankService bankService;

    @GetMapping("/mis/operation-team-manual-process")
    private ResponseEntity misManualProcess() throws Exception {
        if (new File(FILEDIRECTORY_MIS + DateUtil.currentDate(ddMMyy) + ".zip").exists()
                && new File(FILEDIRECTORY_MIS + BIJLIPAY_MIS_XLSX__MIS).exists()) {
            log.info("File Already Exits");
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "File Already Exits"), HttpStatus.BAD_REQUEST);
        } else {
            log.info("Process Started Manually By  : " + System.getProperty("user.name"));
            boolean result = misService.connectToSFTPFileDownload();
            if (result) {
                FileInputStream file = null;
                Workbook wb = null;
                try {
                    file = new FileInputStream(new File(SCHEDULER));
                    wb = StreamingReader.builder()
                            .rowCacheSize(100)
                            .bufferSize(4096).open(file);
                    log.info("auto_bijlipay_mis_temp$ records start to delete");
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
                                    log.info("Bijlipay_Mis$ start to delete");
                                    misManualdeleteMisTable();
                                    try {
                                        misService.deleteRentTable();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (YES.equals(row.getCell(3).getStringCellValue())) {
                                    misService.saveMisDataToTable(FILEDIRECTORY_MIS + BIJLIPAY_MIS_XLSX__MIS);
                                    misManualtempUpdate();
                                    misManualInsert();
                                    try {
                                        log.info("Current Date Bank Mis Records Insert Start");
                                        bankService.uploadBijlipayMisForBankPortal();
                                        log.info("Current Date Bank Mis Records Insert Over");
                                        bankService.updateMisTable();
                                        log.info("Current Date Bank Mis Update Over");
                                    } catch (Exception e) {
                                        log.info("Error Current Date Mis Upload {}", e.getMessage());
                                    }
                                    try {
                                        misService.insertDailyRecords();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (YES.equals(row.getCell(4).getStringCellValue())) {
                                    log.info("Mail Sent");
                                    misService.sentMail();
                                }
                                if (YES.equals(row.getCell(5).getStringCellValue())) {
                                    log.info("deleted auto_daily_sett records");
                                    misService.deleteystrecord();
//                                    log.info("deleted daily_sett records");
//                                    misManualdeleteDailySett();
                                }
                                if (YES.equals(row.getCell(6).getStringCellValue())) {
                                    misService.saveDailySettlementDataToTable(BIJLIPAY_SETTLEMENT_XLSX__MIS);
//                                    misDailySettInsert();
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
                return new ResponseEntity(new ApiResponse(HttpStatus.OK, "Process over mail sent to merchants"),
                        HttpStatus.OK);
            } else {
                log.info("File Process Not Done! ");
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "File Process Not Over"), HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void misManualdeleteDailySett() throws SQLException {
        try {
            getConnection();
            con.prepareStatement(DELETEDAILYSETT).executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    private void misDailySettInsert() throws SQLException {
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

    public void misManualdeleteMisTable() throws SQLException {
        try {
            getConnection();
            con.prepareStatement(DELETEMISTABLE).executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public void misManualInsert() throws SQLException {
        try {
            getConnection();
            log.info("Bijlipay_Mis$ records insert");
            con.prepareStatement(INSERTINTOMIS).execute();
            log.info("tsdb records insert");
            con.prepareStatement(INSERTINTOSETTLEMENT).execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public void misManualtempUpdate() throws SQLException {
        try {
            getConnection();
            Statement statement = con.createStatement();
            for (String query : MISUPDATE) {
                int executeUpdate = statement.executeUpdate(query);
                log.info("updated rows {}", executeUpdate);
            }
            con.close();
        } catch (SQLException e) {
            log.info("Could not update auto_bijlipay_mis_temp$" + e.getMessage());
        } finally {
            con.close();
        }
    }

    public void getConnection() {
        try {
            con = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/mis/deleteAndInsert-mistable")
    public ResponseEntity overAllprocesssFile() throws SQLException {
        File file = new File(SFTPWORKINGDIR_MIS + "/" + BIJLIPAY_MIS_XLSX__MIS);
        if (file.exists()) {
            log.info("File Name " + file.getAbsolutePath());
            misService.deletemistemp();
            try {
                misManualdeleteMisTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            misService.saveMisDataToTable(SFTPWORKINGDIR_MIS + "/" + BIJLIPAY_MIS_XLSX__MIS);
            log.info("MisTemp Insertion Process Over");
            misManualtempUpdate();
            log.info("Mis Update Process Over");
            try {
                misManualInsert();
                log.info("Mis Insertion Process Over");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(new ApiResponse(HttpStatus.OK, "Table Updated Successfully"), HttpStatus.OK);
        } else {
            log.info("File Not Available ");
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "File Not Available"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mis/insert-mistable-without-delete")
    public ResponseEntity processsFilewithParticularCondition() throws SQLException {
        File file = new File(SFTPWORKINGDIR_MIS + "/" + BIJLIPAY_MIS_XLSX__MIS);
        if (file.exists()) {
            log.info("File Name " + file.getAbsolutePath());
            misService.deletemistemp();
            misService.saveMisDataToTable(SFTPWORKINGDIR_MIS + "/" + BIJLIPAY_MIS_XLSX__MIS);
            log.info("MisTemp Insertion Process Over");
            misManualtempUpdate();
            log.info("Mis Update Process Over");
            try {
                misManualInsert();
                log.info("Mis Insertion Process Over");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(new ApiResponse(HttpStatus.OK, "Table Upadted Successfully"), HttpStatus.OK);
        } else {
            log.info("File Not Available ");
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "File Not Available"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mis/scheduler-bijlipay-to-merchant-file")
    public ResponseEntity sftpFileUpload() {
        File schedule = new File(UPLOAD_DOCUMENT_DIR_SCHEDULE_MIS);
        File merchants = new File(UPLOAD_DOCUMENT_DIR_Bijlipay_to_Merchants_MIS);
        boolean result = false;
        final String[] filePath;
        if (schedule.exists() && merchants.exists()) {
            filePath = new String[]{UPLOAD_DOCUMENT_DIR_SCHEDULE_MIS, UPLOAD_DOCUMENT_DIR_Bijlipay_to_Merchants_MIS};
            result = misService.connectToSFTPFileUpload(filePath);
        } else if (schedule.exists()) {
            filePath = new String[]{UPLOAD_DOCUMENT_DIR_SCHEDULE_MIS};
            result = misService.connectToSFTPFileUpload(filePath);
        } else if (merchants.exists()) {
            filePath = new String[]{UPLOAD_DOCUMENT_DIR_Bijlipay_to_Merchants_MIS};
            result = misService.connectToSFTPFileUpload(filePath);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "Please check a uploaded file or file " +
                    "not uploaded"),
                    HttpStatus.BAD_REQUEST);
        }
        if (result) {
            if (schedule.exists()) {
                schedule.delete();
            }
            if (merchants.exists()) {
                merchants.delete();
            }
            return new ResponseEntity(new ApiResponse(HttpStatus.OK, "File uploaded "),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "File not uploaded "),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/mis/federalSettlementReport-manual-process", "/mis/federalSettlementReport-manual-process/{date}"})
    public ResponseEntity operationTeamManualProcess(@PathVariable("date") Optional<Long> date) throws URISyntaxException {
        final String givenDate = date.isPresent() ? DateUtil.longToDate(date.get(), ddMMyyyy) : DateUtil.currentDate(ddMMyyyy);
        if (new File(FILEDIRECTORY_MIS +
                BIJLIPAY_SETTLEMENT_REPORT + givenDate + ".xlsx").exists()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST,
                    "File Upload Already for the date_" + givenDate), HttpStatus.BAD_REQUEST);
        } else {
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
                            boolean download = false;
                            try {
                                download = date.isPresent() ? misService.connectToSFTPDownloadWithSpecificDate(date) :
                                        misService.connectToSFTPDownloadWithSpecificDate(Optional.empty());
                                if (download) {
                                    misService.federalSettlementReport(FILEDIRECTORY_MIS +
                                            BIJLIPAY_SETTLEMENT_REPORT + givenDate + ".xlsx");
                                } else {
                                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "SFTP file not available now, please try after sometime "),
                                            HttpStatus.BAD_REQUEST);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK, " Success "),
                HttpStatus.OK);
    }

    @GetMapping("/mis/settlementReport")
    private void federalSettlementReport() throws SQLException {
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
                                    } else {
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
}
