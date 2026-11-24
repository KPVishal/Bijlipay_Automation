//package com.bijlipay.SchedulerController;
//
//import com.bijlipay.Service.AxisService;
//import com.bijlipay.Service.FederalService;
//import com.bijlipay.Utils.DateUtil;
//import com.bijlipay.Utils.MailHandler;
//import com.bijlipay.Utils.UnzipUtility;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.util.Calendar;
//import java.util.Optional;
//
//import static com.bijlipay.Utils.Constants.*;
//import static com.bijlipay.Utils.Constants.ddMMyyyy;
//
//
//
//
//import com.bijlipay.Service.AxisService;
//import com.bijlipay.Service.FederalService;
//import com.bijlipay.Utils.DateUtil;
//import com.bijlipay.Utils.MailHandler;
//import com.bijlipay.Utils.UnzipUtility;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.util.Calendar;
//import java.util.Optional;
//
//import static com.bijlipay.Utils.Constants.*;
//
//    @Component
//
//    @RestController
//    @RequestMapping("/api")
//    public class UnZipClass {
//        private static final Logger log = LoggerFactory.getLogger(com.bijlipay.SchedulerController.AxisSchedular.class);
//        private final long milliseconds = 1000L;
//        @Autowired
//        private AxisService misService;
//        @Autowired
//        private FederalService federalService;
//        @Autowired
//        private MailHandler mailHandler;
//
//
//        //TODO Trigger Every 15 Minutes from 4 AM to 5 AM
//        @GetMapping("/axis-schedular")
//        public void axisProcesss() throws Exception {
//            boolean download = false;
//            boolean fileCreated = false;
//
//            {
//
//                {
//
//                    try {
//                        log.info("Start Download for the date_ -------------" + DateUtil.currentDate(ddMMyyyy));
//
////                        download = misService.connectToSFTPDownloadBeforeDates(innerCal.getTimeInMillis() / milliseconds);
//                        String fileName = LOCALFILEPATH_AXIS + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMM) + ".zip";
//                        log.info("SFTP Download : " + download + "\nfileName : " + fileName);
//                        if (download && new File(fileName).exists()) {
//                            log.info("Downloaded SuccessFully...");
//                            String excelFile = UnzipUtility.unzip(fileName, LOCALFILEPATH_AXIS);
//
//                            log.info("=========== Extracted Zipped File===============");
//
//                            if (fileCreated) {
//                                log.info("File Upload SuccessFully");
//                                log.info("mail sent successfully after uploading the file ");
//
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//
//
//
//
