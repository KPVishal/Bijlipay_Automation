package com.bijlipay.SchedulerController;

import com.bijlipay.Service.KvbService;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Calendar;
import java.util.Optional;

import static com.bijlipay.Utils.Constants.*;
import static com.bijlipay.Utils.Constants.BIJLIPAY_MIS_XLSX_KVB;



@Component
@RestController
@EnableRetry
@RequestMapping("/file")
public class KvbSchedular {
    private static final Logger log = LoggerFactory.getLogger(com.bijlipay.SchedulerController.KvbSchedular.class);
    private final long milliseconds = 1000L;
    @Autowired
    private KvbService kvbService;
    @Autowired
    private MailHandler mailHandler;


//    @Scheduled(cron = "0 35/10 7-11 * * *") //TODO Trigger Every 10 Minutes from 7 35 AM to 11.35 AM
//    @Scheduled(cron = "0 30/10 7-11 * * *")
    //TODO Trigger Every 10 Minutes from 7 35 AM to 11.35 AM
    @Async
//    @Scheduled(cron = "0 45/12 7-12 * * *")
    @GetMapping("/kvbToMars-scheduler")
    @Retryable(value = Exception.class,maxAttempts = 7,backoff = @Backoff(delay = 100000))
    public void kvbProcesss() throws Exception {
        boolean download = false;
        boolean fileCreated = false;

//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -2);
//        String twoDayBeforeValue = DateUtil.calToDate(cal.getTime(), ddMMyyyy);
//        boolean twoDayBefore = kvbService.findByCreatedAt(twoDayBeforeValue);
//
//        cal.add(Calendar.DATE, 1);
//        String oneDayBeforeValue = DateUtil.calToDate(cal.getTime(), ddMMyyyy);
//        boolean onedayBefore = kvbService.findByCreatedAt(oneDayBeforeValue);
//
//        cal.add(Calendar.DATE, 1);
//        String currentDate = DateUtil.calToDate(cal.getTime(), ddMMyyyy);
//        boolean onDate = kvbService.findByCreatedAt(currentDate);
//
//        if (!twoDayBefore) {
//            log.info("twoDayBeforeValue {}", twoDayBeforeValue);
//            log.info("twoDayBefore {}", twoDayBefore);
//            for (int i = -2; i <= 0; i++) {
//                Calendar innerCal = Calendar.getInstance();
//                innerCal.add(Calendar.DATE, i);
//                DateUtil.calToDate(innerCal.getTime(), ddMMyyyy);
//                try {
//                    log.info("Start Download for the date_ " + DateUtil.currentDate(ddMMyyyy));
//                    download = kvbService.connectToSFTPDownloadBeforeDates(innerCal.getTimeInMillis() / milliseconds);
//                    String fileName = LOCALFILEPATH_KVB +
//                            BIJLIPAY_MIS_XLSX_KVB + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy) + ".xlsx";
//                    if (download && new File(fileName).exists()) {
//                        log.info("Downloaded SuccessFully...");
//                        fileCreated = kvbService.createUpdatedExcel(fileName,
//                                DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy));
//                        if (fileCreated) {
//                            log.info("File Upload SuccessFully");
//                            mailHandler.sendMailForKVBtoMars(DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy));
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
//        } else if (!onedayBefore) {
//            log.info("oneDayBeforeValue {}", oneDayBeforeValue);
//
//            log.info("onedayBefore {}", onedayBefore);
//            for (int i = -1; i <= 0; i++) {
//                Calendar innerCal = Calendar.getInstance();
//                innerCal.add(Calendar.DATE, i);
//                DateUtil.calToDate(innerCal.getTime(), ddMMyyyy);
//                try {
//                    log.info("Start Download for the date_ " + DateUtil.currentDate(ddMMyyyy));
//                    download = kvbService.connectToSFTPDownloadBeforeDates(innerCal.getTimeInMillis() / milliseconds);
//                    String fileName = LOCALFILEPATH_KVB +
//                            BIJLIPAY_MIS_XLSX_KVB + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy) + ".xlsx";
//                    if (download && new File(fileName).exists()) {
//                        log.info("Downloaded SuccessFully...");
//                        fileCreated = kvbService.createUpdatedExcel(fileName,
//                                DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy));
//                        if (fileCreated) {
//                            log.info("File Upload SuccessFully");
//                            mailHandler.sendMailForKVBtoMars(DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy));
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
//        } else {
//            if (onDate) {
//                log.info("currentDate {}", currentDate);
//                log.info("onDate {}", onDate);
//                log.info("File Uploaded Already_ " + DateUtil.currentDate(ddMMyyyy));
//            } else {
                try {
                    String fileName = LOCALFILEPATH_KVB +
                            BIJLIPAY_MIS_XLSX_KVB + DateUtil.currentDate(ddMMyyyy) + ".xlsx";
                    File file = new File(fileName);
                    if(!file.exists()) {
                        log.info("Start Download KVB Scheduler File for the date_ " + DateUtil.currentDate(ddMMyyyy));
                        download = kvbService.connectToSFTPDownloadWithSpecificDate(Optional.empty());
                        if (download && new File(fileName).exists()) {
                            log.info("KVB File Downloaded SuccessFully...");
                            fileCreated = kvbService.createUpdatedExcel(fileName,
                                    DateUtil.currentDate(ddMMyyyy));
                            if (fileCreated) {
                                log.info("KVB File Upload SuccessFully");
                                mailHandler.sendMailForKVBtoMars(DateUtil.currentDate(ddMMyyyy));
                                log.info("Today KVB Sent SuccessFully " + DateUtil.currentDate(ddMMyyyy));
                            } else {
                                log.info("Today KVB File Not Created " + DateUtil.currentDate(ddMMyyyy));
                                String fileNameKVB = "KVB";
                                mailHandler.sendSMBFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), fileNameKVB);
                            }
                        }
//                        else {
//                            String fileNameOriginal = "KVB";
//                            mailHandler.sendSFTPFileNotFoundMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), fileNameOriginal);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//    }
//}


