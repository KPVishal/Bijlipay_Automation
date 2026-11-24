package com.bijlipay.SchedulerController;

import com.bijlipay.Service.MisService;
import com.bijlipay.Utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import static com.bijlipay.Utils.Constants.*;

@Component

@RestController
@RequestMapping("/api")
public class MisSwitchReport extends Exception {
    private static final Logger log = LoggerFactory.getLogger(MisSwitchReport.class);
    @Autowired
    private MisService misService;


    @Scheduled(cron = "0 0/14 1-3 * * *")  //TODO Trigger Every 30 Minutes from 4 30 PM to 7.30 PM
    @GetMapping("/MisCase11-scheduler")
    public void MisProcesss() throws Exception {

        if (new File(FILEDIRECTORY_MIS + "Succesful_mail_sended_MerchantId" + DateUtil.currentDate(yyyy_MM_dd) +
                ".xlsx").exists()){
            log.info("MIS Switch Report File Already exists for SwitchReport");
        }else {
        try {
            misService.sentMailForCase11();
            log.info("MIS Switch Report Mail sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Starting to Sent mail");
        }
    }
}}
