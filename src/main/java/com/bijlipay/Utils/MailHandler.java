package com.bijlipay.Utils;


import com.bijlipay.Model.Merchant_details;
import com.bijlipay.Security.JwtAuthenticationFilter;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bijlipay.Utils.Constants.*;
import static com.bijlipay.Utils.Constants.SENTO_KvbtoMars;

@Component
public class MailHandler {

    private static final Logger logger = LoggerFactory.getLogger(MailHandler.class);

    @Autowired
    private JavaMailSender sender;


    public void sendMailForKVBtoMars(String date) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmailId);
            helper.setTo(SENTO_KvbtoMars);
//            helper.setCc(CC_FEDERAL);
            helper.setSubject("Today KVB Report " + date);
            helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Federal " +
                    "Report for the day uploaded successfully " + date + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" +
                    "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or" +
                    " write to us @ service@bijlipay.co.in."));
//            try {
//                FileSystemResource file =
//                        new FileSystemResource(MERCHANT_FILE_DIRECTORY_FEDERAL +
//                                FILENAME_FEDERAL + date + ".xlsx");
//                helper.addAttachment(file.getFilename(), file);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            try {
                sender.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    public void sendMailForFederal(String date) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmailId);
            helper.setTo(SENTO_FEDERAL);
//            helper.setCc(CC_FEDERAL);
            helper.setSubject("Today Federal Report " + date);
            helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Federal " +
                    "Report for the day uploaded successfully " + date + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" +
                    "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or" +
                    " write to us @ service@bijlipay.co.in."));
//            try {
//                FileSystemResource file =
//                        new FileSystemResource(MERCHANT_FILE_DIRECTORY_FEDERAL +
//                                FILENAME_FEDERAL + date + ".xlsx");
//                helper.addAttachment(file.getFilename(), file);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            try {
                sender.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendMailForSIB(String date) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmailId);
            helper.setTo(SENTO_SIB);
            helper.setSubject("Today SIB Report " + date);
            helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "SIB " +
                    "Report for the day uploaded successfully " + date + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" +
                    "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or" +
                    " write to us @ service@bijlipay.co.in."));
            try {
                sender.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


//    =========================Axis Report===================


    public void sendMailForAxis(String date) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmailId);
            helper.setTo(SENTO_AXIS);
            helper.setSubject("Today AXIS Report " + date);
            helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Axis " +
                    "Report for the day uploaded successfully " + date + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" +
                    "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or" +
                    " write to us @ service@bijlipay.co.in."));
            try {
                sender.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

//====================================================Mis  Report==============================================================================================

    public void misSendMail(String merchantName, String sentTo, String sentToBcc, int format) throws Exception {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // TODO Sent TO Trim
            String[] sentToArr = sentTo.split(",");
            List<String> sentToItemList = new ArrayList<>();
            for (String s : sentToArr) {
                sentToItemList.add(s);
            }
            String[] itemsArraySentTo = new String[sentToItemList.size()];
            itemsArraySentTo = sentToItemList.toArray(itemsArraySentTo);
            String[] sentToTrimed = Arrays.stream(itemsArraySentTo).map(String::trim).toArray(String[]::new);

            // TODO Sent TO BCC Trim
            String[] sentToBccArr = sentToBcc.split(",");
            List<String> sentToBccItemList = new ArrayList<>();
            for (String s : sentToBccArr) {
                sentToBccItemList.add(s);
            }
            String[] itemsArraySentToBcc = new String[sentToBccItemList.size()];
            itemsArraySentToBcc = sentToBccItemList.toArray(itemsArraySentToBcc);
            String[] senToBccTrimed = Arrays.stream(itemsArraySentToBcc).map(String::trim).toArray(String[]::new);
            helper.setFrom(fromEmailId);
            helper.setTo(sentToTrimed);
            helper.setBcc(senToBccTrimed);
            helper.setSubject(merchantName + "_" + DateUtil.currentDate(yyyy_MM_dd));
            FileSystemResource file;
            try {
                switch (format) {
                    case 0:
                        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" +
                                "Please update a verified mail id field in excel For " + merchantName +
                                " " + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
                        try {
                            sender.send(message);
                        }catch (Exception e){
                            System.out.println("merchantName "+merchantName+"SentTo "+sentTo+"sentToBcc"+sentToBcc+"format "+format);
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "There are NIL transactions for the day " + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
                        try {
                            sender.send(message);
                        }catch (Exception e){
                            System.out.println("merchantName "+merchantName+"SentTo "+sentTo+"sentToBcc"+sentToBcc+"format "+format);
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Please find the enclosed settlements report for " + "'" + DateUtil.currentDate(yyyy_MM_dd) + "_" + merchantName + "'" + "\n\n" + "Attachment has protected with the password. Please use the usual password to access it." + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
                        zipFile(merchantName);
                        file = new FileSystemResource(MERCHANT_FILE_DIRECTORY_MIS +merchantName + "_" + DateUtil.currentDate(yyyy_MM_dd) +
                                ".zip");
                        helper.addAttachment(file.getFilename(), file);
                        try {
                            sender.send(message);
                        }catch (Exception e){
                            System.out.println("merchantName "+merchantName+"SentTo "+sentTo+"sentToBcc"+sentToBcc+"format "+format);
                            e.printStackTrace();
                        }
                        break;
                    // TODO it used previously but it was never used now
                    case 3:
                        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Please find the  enclosed settlements report for " + "'" + DateUtil.currentDate(yyyy_MM_dd) + "_" + merchantName + "'" + "\n\n" + "Attachment has protected with the password. Please use the usual password to access it." + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
//                        zipFile(merchantName);
                        file = new FileSystemResource(MERCHANT_FILE_DIRECTORY_MIS +merchantName + "_" + DateUtil.currentDate(yyyy_MM_dd) +
                                ".xlsx");
                        logger.info("format3  EXCEL CHANGES"+file.getFilename());
                        helper.addAttachment(file.getFilename(), file);
                        try {
                            sender.send(message);
                        }catch (Exception e){
                            System.out.println("merchantName "+merchantName+"SentTo "+sentTo+"sentToBcc"+sentToBcc+"format "+format);
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "There are NIL transactions for the day " + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
                        try {
                            sender.send(message);
                        }catch (Exception e){
                            System.out.println("merchantName "+merchantName+"SentTo "+sentTo+"sentToBcc"+sentToBcc+"format "+format);
                            e.printStackTrace();
                        }
                        break;
                    default:
                        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "We " +
                                "do not have a format " + format + " for this merchant " + merchantName + " now ,kindly check a format before update a excel " +
                                "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated " +
                                "email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
                        try {
                            sender.send(message);
                        }catch (Exception e){
                            System.out.println("merchantName "+merchantName+"SentTo "+sentTo+"sentToBcc"+sentToBcc+"format "+format);
                            e.printStackTrace();
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void zipFile(String merchantName) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(MERCHANT_FILE_DIRECTORY_MIS +  merchantName + "_" + DateUtil.currentDate(yyyy_MM_dd) +
                    ".zip");
            ArrayList<File> filesToAdd = new ArrayList<File>();
            filesToAdd.add(new File(MERCHANT_FILE_DIRECTORY_MIS +  merchantName + "_" + DateUtil.currentDate(yyyy_MM_dd) +
                    ".xlsx"));
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(ZIPFILEPASSWORD);
            try {
                zipFile.addFiles(filesToAdd, parameters);
            } catch (ZipException e) {
                System.out.println(" Creating Zip Error "+e.getMessage());
            }
        } catch (ZipException e) {
            e.printStackTrace();
            System.gc();
        }
    }

    /*
     * mail sent to operation team, attachment of merchant name, who recevied mail ie. merchant
     * */
    public void sentMailToInternal() throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String[] sentTo = {"opsportal@bijlipay.co.in"};
        helper.setFrom(fromEmailId);
        helper.setTo(sentTo);
        helper.setSubject("MailList_" + DateUtil.currentDate(yyyy_MM_dd));
        FileSystemResource file;
        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Please find the " +
                "attached report for mis automated." +
                " " +
                "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n " +
                "For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
        file = new FileSystemResource(FILEDIRECTORY_MIS + "sentMailIDs_" + DateUtil.currentDate(yyyy_MM_dd) +
                ".xlsx");
        helper.addAttachment(file.getFilename(), file);
        sender.send(message);
    }
//    =================================================================================================================

    public void PdfsendEmail(String newfolder, Merchant_details response, String string) throws Exception {
        MimeMessage message = sender.createMimeMessage();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yyyy");
//		String date = simpleDateFormat.format(new Date());
//		System.out.println(date);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//      New update To change mail id for gst as "invoice@bijlipay.co.in"
        helper.setFrom(fromEmailId1);
        helper.setTo(response.getEmail());
        helper.setSubject("MDR GST Invoice for " + string + " – Bijlipay GPRS POS");
        helper.setText(String.format("Dear Madam/Sir," +
                "\n" +
                "\n" +
                "Greetings from Bijlipay" +
                "\n" +
                "\n" +
                "We are happy to share the MDR GST Invoice for the month of " + string + "." +
                "\n" +
                "\n" +
                "Please feel free to write to us on service@bijlipay.co.in in case you require any clarifications." +
                "\n" +
                "\n" +
                "Assuring you of our best services." +
                "\n" +
                "\n" +
                "\n" +
                "Regards," +
                "\n" +
                "Bijlipay Team")
        );
        FileSystemResource file =
                new FileSystemResource(newfolder + "/" + response.getName() + "_" + response.getTid() + ".pdf");
        helper.addAttachment(file.getFilename(), file);
        sender.send(message);
    }
//=====================================================================================================================

    public void sendMail(String merchantName, String sentTo, int format) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //  helper.setTo();
        helper.setSubject(DateUtil.currentDate(yyyy_MM_dd) + "_" + merchantName);
        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Please find the  enclosed settlements report for " + "'" + DateUtil.currentDate(yyyy_MM_dd) + "_" + merchantName + "'" + "\n\n" + "Attachment has protected with the password. Please use the usual password to access it." + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
        // FileSystemResource   file = new FileSystemResource(MERCHANT_FILE_DIRECTORY + "/" + DateUtil.currentDate() +
        //         "_" + merchantName + ".zip");
        //  helper.addAttachment(file.getFilename(), file);
        sender.send(message);
    }

    public void sendMail() throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //  helper.setTo();
        helper.setSubject(DateUtil.currentDate(yyyy_MM_dd));
        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Nill transaction for today." + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
        // FileSystemResource   file = new FileSystemResource(MERCHANT_FILE_DIRECTORY + "/" + DateUtil.currentDate() +
        //         "_" + merchantName + ".zip");
        //  helper.addAttachment(file.getFilename(), file);
        sender.send(message);
    }

    public void sendMail(String meName, String mail_id) {

        System.out.println("nill transaction_>>>>>>>>" + meName);

    }

    public void sendMails(String meName, String mail_id, String getname, String mid) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmailId);
        helper.setTo(mail_id);
        helper.setSubject(getname + "_report_" + DateUtil.currentDate(yyyy_MM_dd));
        helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Please find the  enclosed " + getname + "report for " + meName + "_" + DateUtil.currentDate(dd_MMM_yyyy) + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
        FileSystemResource file =
                new FileSystemResource(LEAD_DIRECTORY + "/" + getname + "/" + meName + "_" + mid + "_" + DateUtil.currentDate(dd_MMM_yyyy) + ".xlsx");
        helper.addAttachment(file.getFilename(), file);
        sender.send(message);
    }

    public void sendSFTPFailedMail(String date,String fileName) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmailId);
            helper.setTo(SFTP_AXIS);
            helper.setSubject("SFTP Connection Failed (WorldLine) - " + date);
            helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + ""+fileName+" Scheduler File is Not Processed Due to SFTP Connection Failed. " +
                    "\n\n" +"SFTP Server Details :\n\n IP : 180.179.249.63\n UserName : mrlbijilipay\n\n"+ "Regards," + "\n" + "Team Bijlipay\n\n Note :" +
                    "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or" +
                    " write to us @ service@bijlipay.co.in."));
            try {
                sender.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendSFTPFileNotFoundMail(String date,String fileName) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmailId);
            helper.setTo(SFTP_AXIS);
            helper.setSubject("SFTP File Not Found (WorldLine) - " + date);
            helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + ""+fileName+" Scheduler File is Not Processed Due to File Not FOund In Respective directory. " +
                    "\n\n" +"SFTP Server Details :\n\n IP : 180.179.249.63\n UserName : mrlbijilipay\n\n"+ "Regards," + "\n" + "Team Bijlipay\n\n Note :" +
                    "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or" +
                    " write to us @ service@bijlipay.co.in."));
            try {
                sender.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendSMBFailedMail(String date, String fileName) {
        String ip= null;
        if(fileName.equals("Axis")){
            ip="10.10.0.72";
//            ip="192.168.2.66";  Older IP
        }else{
            ip ="192.168.2.61";
        }
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmailId);
            helper.setTo(SFTP_AXIS);
            helper.setSubject("SMB Connection Failed (MARS) - " + date);
            helper.setText(String.format("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + ""+fileName+" Scheduler File is Not Processed Due to SMB Connection Failed. " +
                    "\n\n" +"SMB Server Details :\n\n IP :"+ip+" \n UserName : marsinput\n Domain : workgroup\n\n"+ "Regards," + "\n" + "Team Bijlipay\n\n Note :" +
                    "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or" +
                    " write to us @ service@bijlipay.co.in."));
            try {
                sender.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
