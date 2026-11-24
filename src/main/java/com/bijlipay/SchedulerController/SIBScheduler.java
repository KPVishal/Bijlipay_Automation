package com.bijlipay.SchedulerController;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import com.bijlipay.Service.FederalService;
import com.bijlipay.Service.SIBService;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;
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
import java.util.Date;
import java.util.Optional;

import static com.bijlipay.Utils.Constants.*;
import static com.bijlipay.Utils.Constants.yyyy_MM_dd;

@Component
@RestController
@EnableRetry
@RequestMapping("/file")
public class SIBScheduler {
    private static final Logger log = LoggerFactory.getLogger(SIBScheduler.class);
    private final long milliseconds = 1000L;
    @Autowired
    private SIBService misService;
    @Autowired
    private MailHandler mailHandler;

    @Async
    @GetMapping("/sib-trigger")
    @Retryable(value = Exception.class,maxAttempts = 7,backoff = @Backoff(delay = 100000))
//    @Scheduled(cron = "0 0/10 8-12 * * *")
//    @Scheduled(cron = "0 0/10 14-15 * * *")

    @Scheduled(cron = "0 0/10 8-12 * * *")
// TODO Trigger Every 15 Minutes from 8 AM to 11.45 AM
    public void sbiScheduler() throws Exception {
        boolean download = false;
        boolean fileCreated = false;

//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -2);
//        String twoDayBeforeValue = DateUtil.calToDate(cal.getTime(), yyyy_MM_dd);
//        boolean twoDayBefore = misService.findByCreatedAt(twoDayBeforeValue);
//
//        cal.add(Calendar.DATE, 1);
//        String oneDayBeforeValue = DateUtil.calToDate(cal.getTime(), yyyy_MM_dd);
//        boolean onedayBefore = misService.findByCreatedAt(oneDayBeforeValue);
//
//        cal.add(Calendar.DATE, 1);
//        String currentDate = DateUtil.calToDate(cal.getTime(), yyyy_MM_dd);
//        boolean onDate = misService.findByCreatedAt(currentDate);
//
//        if (!twoDayBefore) {
//            log.info("twoDayBeforeValue {}", twoDayBeforeValue);
//            log.info("twoDayBefore {}", twoDayBefore);
//            for (int i = -2; i <= 0; i++) {
//                Calendar innerCal = Calendar.getInstance();
//                innerCal.add(Calendar.DATE, i);
//                DateUtil.calToDate(innerCal.getTime(), yyyy_MM_dd);
//                try {
//                    log.info("Start Download for the date_ " + DateUtil.currentDate(yyyy_MM_dd));
//                    download = misService.connectToSFTPDownloadBeforeDates(innerCal.getTimeInMillis() / milliseconds);
//                    String fileName = LOCALFILEPATH_SIB +
//                            BIJLIPAY_MIS_XLSX_SIB_FILENAME + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy) + ".xlsx";
//                    if (download && new File(fileName).exists()) {
//                        log.info("Downloaded SuccessFully...");
//                        fileCreated = misService.createUpdatedExcel(fileName,
//                                DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, yyyy_MM_dd));
//                        if (fileCreated) {
//                            log.info("File Upload SuccessFully");
//                            mailHandler.sendMailForFederal(DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, yyyy_MM_dd));
//                            log.info("File Sent SuccessFully " + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds,
//                                    yyyy_MM_dd));
//                        } else {
//                            log.info("File Not Created " + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds,
//                                    yyyy_MM_dd));
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } else if (!onedayBefore) {
//            log.info("oneDayBeforeValue {}", oneDayBeforeValue);
//
//            log.info("onedayBefore {}", onedayBefore);
//            for (int i = -1; i <= 0; i++) {
//                Calendar innerCal = Calendar.getInstance();
//                innerCal.add(Calendar.DATE, i);
//                DateUtil.calToDate(innerCal.getTime(), yyyy_MM_dd);
//                try {
//                    log.info("Start Download for the date_ " + DateUtil.currentDate(yyyy_MM_dd));
//                    download = misService.connectToSFTPDownloadBeforeDates(innerCal.getTimeInMillis() / milliseconds);
//                    String fileName =LOCALFILEPATH_SIB +
//                            BIJLIPAY_MIS_XLSX_SIB_FILENAME + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy) + ".xlsx";
//                    if (download && new File(fileName).exists()) {
//                        log.info("Downloaded SuccessFully...");
//                        fileCreated = misService.createUpdatedExcel(fileName,
//                                DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, yyyy_MM_dd));
//                        if (fileCreated) {
//                            log.info("File Upload SuccessFully");
//                            mailHandler.sendMailForFederal(DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, yyyy_MM_dd));
//                            log.info("File Sent SuccessFully " + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds,
//                                    yyyy_MM_dd));
//                        } else {
//                            log.info("File Not Created " + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds,
//                                    yyyy_MM_dd));
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            if (onDate) {
//                log.info("currentDate {}", currentDate);
//                log.info("onDate {}", onDate);
//                log.info("File Uploaded Already_ " + DateUtil.currentDate(yyyy_MM_dd));
//            } else {
                try {
                    String inputPath =LOCALFILEPATH_SIB +
                            BIJLIPAY_MIS_XLSX_SIB_FILENAME + DateUtil.currentDate(ddMMyyyy) + ".xlsx";
                    File file = new File(inputPath);
                    if(!file.exists()) {
                        log.info("Start Download SIB Scheduler for the date_ " + DateUtil.currentDate(yyyy_MM_dd));
                        download = misService.connectToSFTPDownloadWithSpecificDate(Optional.empty());
                        if (download) {
                            log.info("SIB File Downloaded SuccessFully...");
                            System.out.println("Input Path " + inputPath);
                            if (new File(inputPath).exists()) {
                                System.out.println("SIB File Available ");
                                fileCreated = misService.createUpdatedExcel(inputPath,
                                        DateUtil.currentDate(yyyy_MM_dd));
                                if (fileCreated) {
                                    log.info("SIB File Upload SuccessFully");
                                    mailHandler.sendMailForSIB(DateUtil.currentDate(yyyy_MM_dd));
                                    log.info("Today SIB Sent SuccessFully " + DateUtil.currentDate(yyyy_MM_dd));
                                } else {
                                    log.info("Today SIB File Not Created " + DateUtil.currentDate(yyyy_MM_dd));
                                    String fileName = "SIB";
                                    mailHandler.sendSMBFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), fileName);
                                }
                            }
                            System.out.println("SIB File Not Available ");
                        }
//                        else {
//                            String fileNameOriginal = "SIB";
//                            mailHandler.sendSFTPFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), fileNameOriginal);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//        }
//    }

    @GetMapping("/test")
    public void test() throws Exception {
        sbiScheduler();
        Date mDate = new Date(new Date().getTime());
        String[] m= {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        final String REMOTEFILEPATH =
                SFTP_SIB_PATH + MONTHS[mDate.getMonth()]+"_"+DateUtil.longToDate(new Date().getTime()/milliseconds,yy) + "/" + DateUtil.longToDate(new Date().getTime()/milliseconds, ddMMyy) +
                        "/" + BIJLIPAY_MIS_XLSX_SIB_ZIP + DateUtil.longToDate(new Date().getTime()/milliseconds,ddMM) + ".zip";

        System.out.print(REMOTEFILEPATH);
    }



}

