package com.bijlipay.SchedulerController;

import com.bijlipay.Model.DTO.AxisDTO;
import com.bijlipay.Model.DTO.KVBDTo;
import com.bijlipay.Model.FederalDTO;
import com.bijlipay.Model.SibReport;
import com.bijlipay.Repository.AxisRepository;
import com.bijlipay.Repository.FederalRepository;
import com.bijlipay.Repository.KVBtoMarsRepository;
import com.bijlipay.Repository.SIBRepository;
import com.bijlipay.Service.AxisService;
import com.bijlipay.Service.FederalService;
import com.bijlipay.Service.TsdbService;
import com.bijlipay.ServiceImplementation.TsdbServiceImp;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;
import com.bijlipay.Utils.PGPDecryption;
import com.bijlipay.Utils.UnzipUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.bijlipay.Utils.Constants.*;

@Component
@RestController
@RequestMapping("/file")
public class AxisSchedular {
    private static final Logger log = LoggerFactory.getLogger(AxisSchedular.class);
    private final long milliseconds = 1000L;

    @Autowired
    private AxisRepository axisRepository;
    @Autowired
    private SIBRepository sibRepository;
    @Autowired
    TsdbService tsdbService;
    @Autowired
    private AxisService misService;
    @Autowired
    private FederalService federalService;
    @Autowired
    private MailHandler mailHandler;


//    @Scheduled(cron = "0 0/15 11-12 * * *")
//    @Scheduled(cron = "0 0/5 11-12 * * *")
    //TODO Trigger Every 15 Minutes from 4 AM to 5 AM

    @Async
    @Scheduled(cron = "0 0/15 3-7 * * *")
    @GetMapping("/axis-schedular")
    public void axisProcesss() throws Exception {
        boolean download = false;
        boolean fileCreated = false;

//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -2);
//        String twoDayBeforeValue = DateUtil.calToDate(cal.getTime(), ddMMyyyy);
//        boolean twoDayBefore = misService.findByCreatedAt(twoDayBeforeValue);
//
//        cal.add(Calendar.DATE, 1);
//        String oneDayBeforeValue = DateUtil.calToDate(cal.getTime(), ddMMyyyy);
//        boolean onedayBefore = misService.findByCreatedAt(oneDayBeforeValue);
//
//        cal.add(Calendar.DATE, 1);
//        String currentDate = DateUtil.calToDate(cal.getTime(), ddMMyyyy);
//        boolean onDate = misService.findByCreatedAt(currentDate);

//        if (!twoDayBefore) {
//            log.info("twoDayBeforeValue {}", twoDayBeforeValue);
//            log.info("twoDayBefore {}", twoDayBefore);
//            for (int i = -2; i <= 0; i++) {
//                Calendar innerCal = Calendar.getInstance();
//                innerCal.add(Calendar.DATE, i);
//                DateUtil.calToDate(innerCal.getTime(), ddMMyyyy);
//                try {
//                    log.info("Start Download for the date_ -------------" + DateUtil.currentDate(ddMMyyyy));
//                    download = misService.connectToSFTPDownloadBeforeDates(innerCal.getTimeInMillis() / milliseconds);
//                    String fileName = LOCALFILEPATH_AXIS + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMM) + ".zip";
//                    log.info("SFTP Download : " + download + "\nfileName : " + fileName);
//                    if (download && new File(fileName).exists()) {
//                    log.info("Downloaded SuccessFully...");
//                        String excelFile = UnzipUtility.unzip(fileName, LOCALFILEPATH_AXIS);
//                    log.info("=========== Extracted Zipped File===============");
//                    fileCreated = misService.createUpdatedExcel(excelFile,
//                            DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy));
//                    if (fileCreated) {
//                        log.info("File Upload SuccessFully");
//                        mailHandler.sendMailForAxis(DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy));
//                        log.info("mail sent successfully after uploading the file ");
//                        log.info("File Sent SuccessFully " + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds,
//                                ddMMyyyy));
//                    } else {
//                        log.info("File Not Created " + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds,
//                                ddMMyyyy));
//                    }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
////         if (!onedayBefore) {
//        else if (!onedayBefore) {
//            log.info("oneDayBeforeValue {}", oneDayBeforeValue);
//
//            log.info("onedayBefore {}", onedayBefore);
//            for (int i = -1; i <= 0; i++) {
//                Calendar innerCal = Calendar.getInstance();
//                innerCal.add(Calendar.DATE, i);
//                DateUtil.calToDate(innerCal.getTime(), ddMMyyyy);
//                try {
//                    log.info("Start Download for the date_ " + DateUtil.currentDate(ddMMyyyy));
//                    download = misService.connectToSFTPDownloadBeforeDates(innerCal.getTimeInMillis() / milliseconds);
//
//                    String fileName = LOCALFILEPATH_AXIS +
//                            BIJLIPAY_MIS_XLSX_AXIS + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMM) + ".zip";
//                    if (download && new File(fileName).exists()) {
//                        log.info("Downloaded SuccessFully...");
//
//
////                        UnzipUtility.unzip(fileName, LOCALFILEPATH_AXIS);
//                        String excelFile = UnzipUtility.unzip(fileName, LOCALFILEPATH_AXIS);
//                        log.info("==================Extracted zipped file ===================");
//                        fileCreated = misService.createUpdatedExcel(excelFile,
//                                DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy));
//                        if (fileCreated) {
//                            log.info("File Upload SuccessFully");
//                            mailHandler.sendMailForAxis(DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy));
//                            log.info("mail sent successfully after uploading the file ");
//                            log.info("File Sent SuccessFully " + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds,
//                                    ddMMyyyy));
//                        } else {
//                            log.info("File Not Created " + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds,
//                                    ddMMyyyy));
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        else {
//            if (onDate) {
//                log.info("currentDate {}", currentDate);
//                log.info("onDate {}", onDate);
//                log.info("File Uploaded Already_ " + DateUtil.currentDate(ddMMyyyy));
//            }
//            else {
        try {
            String fileName = LOCALFILEPATH_AXIS +
                    BIJLIPAY_MIS_XLSX_AXIS + DateUtil.currentDate(ddMM) + ".zip";
            File file = new File(fileName);
            if (!file.exists()) {
                log.info("Start Download Axis Scheduler for the date_ " + DateUtil.currentDate(ddMMyyyy));
                download = misService.connectToSFTPDownloadWithSpecificDate(Optional.empty());
                if (download && new File(fileName).exists()) {
                    log.info("Axis File Downloaded SuccessFully...");
                    String excelFile = UnzipUtility.unzip(fileName, LOCALFILEPATH_AXIS);
                    if (PGPDecryption.getFileExtension(excelFile).equals(".pgp") && !PGPDecryption.getFileExtension(excelFile).equals("")) {
                        log.info("File extension is {}:", PGPDecryption.getFileExtension(excelFile) + " :: " + DateUtil.currentDate(ddMMyyyy));
                        excelFile = PGPDecryption.decryptFile(excelFile, LOCALFILEPATH_AXIS);
                    } else {
                        log.info("File extension is {}:", PGPDecryption.getFileExtension(excelFile) + " :: " + DateUtil.currentDate(ddMMyyyy));
                    }
                    log.info("==================Extracted Axis zipped file ===================");
                    fileCreated = misService.createUpdatedExcel(excelFile,
                            DateUtil.currentDate(ddMMyyyy));
                    if (fileCreated) {
                        log.info("Axis Scheduler File Upload SuccessFully");
                        mailHandler.sendMailForAxis(DateUtil.currentDate(ddMMyyyy));
                        log.info("Axis File mail sent successfully after uploading the file ");
                        log.info("Axis Today File Sent SuccessFully " + DateUtil.currentDate(ddMMyyyy));
                    } else {
                        log.info("Axis Today File Not Created " + DateUtil.currentDate(ddMMyyyy));
                        String fileNameAxis = "Axis";
                        mailHandler.sendSMBFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), fileNameAxis);
                    }
                }
//                else {
//                    String fileNameOriginal = "Axis";
//                    mailHandler.sendSFTPFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), fileNameOriginal);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
//    }
//}
