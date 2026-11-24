package com.bijlipay.SchedulerController;


import com.bijlipay.Service.FederalService;
import com.bijlipay.Service.TsdbService;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
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


@Component
@RestController
@RequestMapping("/file")
public class FederalScheduler {
    private static final Logger log = LoggerFactory.getLogger(FederalScheduler.class);
    private final long milliseconds = 1000L;
    @Autowired
    private FederalService misService;

    @Autowired
    private TsdbService tsdbService;
    @Autowired
    private MailHandler mailHandler;

//    @Scheduled(cron = "0 0/10 12-14 * * *") //TODO Trigger Every 15 Minutes from 8 AM to 11.45 AM
//    @Scheduled(cron = "0 15/10 7-11 * * *") //TODO Trigger Every 15 Minutes from 8 AM to 11.45 AM

    @Async
    @Scheduled(cron = "0 15/11 7-11 * * *")
    @GetMapping("/federal-schedular")
    @Retryable(value = Exception.class,maxAttempts = 7,backoff = @Backoff(delay = 10000))
    public void federalProcesss() throws Exception {
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
//
//                    String fileName =LOCALFILEPATH_FEDERAL +
//                            BIJLIPAY_MIS_XLSX_FEDERAL + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy) + ".xlsx";
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
//                    String fileName =LOCALFILEPATH_FEDERAL +
//                            BIJLIPAY_MIS_XLSX_FEDERAL + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy) + ".xlsx";
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
                    String fileName =LOCALFILEPATH_FEDERAL +
                            BIJLIPAY_MIS_XLSX_FEDERAL + DateUtil.currentDate(ddMMyyyy) + ".xlsx";
                    File file = new File(fileName);
                    if(!file.exists()) {
                        log.info("Start Download Federal Scheduler File the date_ " + DateUtil.currentDate(yyyy_MM_dd));
                        download = misService.connectToSFTPDownloadWithSpecificDate(Optional.empty());
                        if (download && new File(fileName).exists()) {
                            log.info("Federal File Downloaded SuccessFully...");
                            fileCreated = misService.createUpdatedExcel(fileName,
                                    DateUtil.currentDate(yyyy_MM_dd));
                            if (fileCreated) {
                                log.info("Federal File Upload SuccessFully");
                                mailHandler.sendMailForFederal(DateUtil.currentDate(yyyy_MM_dd));
                                log.info("Federal Today File Sent SuccessFully " + DateUtil.currentDate(yyyy_MM_dd));
                            } else {
                                log.info("Federal Today File Not Created " + DateUtil.currentDate(yyyy_MM_dd));
                                String fileNameFederal = "Federal";
                                mailHandler.sendSMBFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), fileNameFederal);
                            }
                        }
//                        else {
//                            String fileNameOriginal = "Federal";
//                            mailHandler.sendSFTPFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), fileNameOriginal);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//    }


//}
