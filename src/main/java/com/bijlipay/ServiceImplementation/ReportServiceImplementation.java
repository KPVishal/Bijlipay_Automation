package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.DTO.CustomExcel;
import com.bijlipay.Model.DTO.InvoiceDTO;
import com.bijlipay.Model.DTO.MerchantDataDto;
import com.bijlipay.Repository.MisRespository;
import com.bijlipay.Service.ReportService;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;
import com.bijlipay.Utils.ReportsUtils;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.bijlipay.Utils.Constants.*;

@Service
public class ReportServiceImplementation implements ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportServiceImplementation.class);

    @Autowired
    private MisRespository misRespository;

    @Autowired
    private MailHandler mailHandler;

    @Override
    public void invoiceMailToCustomer() {
        boolean firstrow = true;
        FileInputStream file = null;
        Workbook wb = null;
        List<InvoiceDTO> merchantDataDtosMap = null;
        try {
            file = new FileInputStream(new File(CUSTOMER_MAIL_DIR));
            wb = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096).open(file);
            merchantDataDtosMap = new ArrayList<InvoiceDTO>();

            for (Row row : wb.getSheetAt(0)) {
                if (firstrow) {
                    firstrow = false;
                    continue;
                }
                try {
                    InvoiceDTO merchantDataDto = new InvoiceDTO();
                    int cellValue = 0;
                    merchantDataDto.setGst(row.getCell(0).getStringCellValue());
                    merchantDataDto.setDocumentType(row.getCell(13).getStringCellValue());
                    merchantDataDto.setDocumentName(row.getCell(14).getStringCellValue());
                    merchantDataDto.setFromDate(row.getCell(73).getStringCellValue());
                    merchantDataDto.setToDate(row.getCell(74).getStringCellValue());
                    merchantDataDto.setMailIds(row.getCell(137).getStringCellValue());
                    merchantDataDtosMap.add(merchantDataDto);
                }catch (Exception e){
                    log.info("Error in excel pdf file"+e);
                }
            }
            wb.close();
            file.close();
            sendMailToCustomer(merchantDataDtosMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error While Reading pdf Excel File " + e);
        } finally {
            try {
                wb.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Workbook downloadReport(Optional<String> reportName) {
        List<Object[]> misReports = null;
        String rName = reportName.get();
        String sheetName = "";
        String[] columnName = null;
        List<CustomExcel> objects = new ArrayList<>();
        CustomExcel customExcel = new CustomExcel();
        // TODO MDR INVOICE Monthly Report
        if(rName.equals("InvoiceMdr")){
            LocalDateTime currentTime = LocalDateTime.now();
            currentTime = currentTime.minusMonths(1);
            String year = String.valueOf(currentTime.getYear());
            String customizedMonth = currentTime.getMonth().toString().substring(0,3).toLowerCase()+"_"+year.substring(0,2);
            System.out.print("Previous Month "+customizedMonth);
//            misReports = misRespository.mdrInvoicePreviousMonthReport(customizedMonth);
//            sheetName= customizedMonth;
//            columnName = INVOICE_MDR_HEADER;

            customExcel.setSheetName(customizedMonth);
            customExcel.setColumnName(INVOICE_MDR_HEADER);
            customExcel.setData(misRespository.mdrInvoicePreviousMonthReport(customizedMonth));
            objects.add(customExcel);
        }
        // TODO AB_GPRS
        if(rName.equals("AB_GPRS")) {
//            misReports = misRespository.getABGprsOutput();
//            sheetName= rName;
//            columnName = GPRS;

            customExcel.setSheetName(rName);
            customExcel.setColumnName(GPRS);
            customExcel.setData(misRespository.getABGprsOutput());
            objects.add(customExcel);
            }
        // TODO 2
        else if(rName.equals("MUKESH SUPER MART")){
//            misReports = misRespository.getReportForMukeshSuperMart();
//            sheetName= rName;
//            columnName = FORMATTWO_MIS;

            customExcel.setSheetName(rName);
            customExcel.setColumnName(FORMATTWO_MIS);
            customExcel.setData(misRespository.getReportForMukeshSuperMart());
            objects.add(customExcel);
        }
        // TODO 6
        else if(rName.equals("WHITE MAGIC")){
//            misReports = misRespository.getReportForWhiteMagic();
//            sheetName= rName;
//            columnName = FORMATFOUR_MIS;

            customExcel.setSheetName(rName);
            customExcel.setColumnName(FORMATFOUR_MIS);
            customExcel.setData(misRespository.getReportForWhiteMagic());
            objects.add(customExcel);
        }
        // TODO 10
        else if (rName.equals("Fuel")){
//            misReports = misRespository.getReportForFuel();
//            sheetName= rName;
//            columnName = FORMATFIVE_MIS;

            customExcel.setSheetName(rName);
            customExcel.setColumnName(FORMATFIVE_MIS);
            customExcel.setData(misRespository.getReportForFuel());
            objects.add(customExcel);
        }
        // TODO EMI
        else if(rName.equals("EMI")){
//            misReports = misRespository.getEmiReport();
//            sheetName= rName;
//            columnName = EMI;
            customExcel.setSheetName(rName);
            customExcel.setColumnName(EMI);
            customExcel.setData(misRespository.getEmiReport());
            objects.add(customExcel);
        }
        // TODO FLASH_REPORT
        else if(rName.equals("FLASH_REPORT")){
//            misReports = misRespository.getFlashReport();
//            sheetName= rName;
//            columnName = FLASH_REPORT;

            customExcel.setSheetName(rName);
            customExcel.setColumnName(FLASH_REPORT);
            customExcel.setData(misRespository.getFlashReport());
            objects.add(customExcel);
        }
        // TODO RESELLER
        else if(rName.equals("RESELLER")){
//            misReports = misRespository.getRellerReport();
//            sheetName= rName;
//            columnName = DCB_SHEET1;
            customExcel.setSheetName(rName);
            customExcel.setColumnName(DCB_SHEET1);
            customExcel.setData(misRespository.getRellerReport());
            objects.add(customExcel);
        }
        // TODO 1,3,4,8
        else if(rName.equals("M SQUARE AUTOMOBILE")||
                rName.equals("NYASA ENTERPRISES") ||
                rName.equals("RAVINDRA BHARATHI EDUCATIONAL")||
                rName.equals("THE PINKY WINES")){
//            misReports = misRespository.getReportByFormatOne(rName);
//            sheetName = rName;
//            columnName = FORMATONE_MIS;

            customExcel.setSheetName(rName);
            customExcel.setColumnName(FORMATONE_MIS);
            customExcel.setData(misRespository.getReportByFormatOne(rName));
            objects.add(customExcel);
        }
        else if(rName.equals("Consolidated Commission Details")){
            LocalDate currentTime = LocalDate.now();
            currentTime = currentTime.minusMonths(1);
            String firstDayOfMonth = currentTime.withDayOfMonth(1).toString();
            String lastDayOfMonth = currentTime.withDayOfMonth(currentTime.lengthOfMonth()).toString();
            System.out.println("First day: " + firstDayOfMonth);
            System.out.println("Last day: " + lastDayOfMonth);
            customExcel.setSheetName(rName);
            customExcel.setColumnName(ConsolidateCommisionDeatils);
            customExcel.setData(misRespository.getConsolidatedCommissionDetails(firstDayOfMonth,lastDayOfMonth));
            objects.add(customExcel);
        }
        return ReportsUtils.createExcel(objects);
//        return ReportsUtils.createFormatOneExcel(misReports,sheetName,columnName);
    }

    @Override
    public Workbook downloadReportTypeTwo(Optional<String> reportName) {
//        List<Object[]> output1 = null;
//        List<Object[]> output2 = null;
//        List<Object[]> output3 = null;
        String rName = reportName.get();
//        String sheetName1 = "";
//        String[] columnName1 = null;
//        String sheetName2 = "";
//        String[] columnName2 = null;
//        String sheetName3 = "";
//        String[] columnName3 = null;
        List<CustomExcel> objects = new ArrayList<>();
        CustomExcel customExcel = new CustomExcel();
        CustomExcel customExcel2 = new CustomExcel();
        CustomExcel customExcel3 = new CustomExcel();

        // TODO 5
        if(rName.equals("VENDIMAN")){
//            output1 = misRespository.getReportByFormatOne(rName);
//            sheetName1= "Summary";
//            columnName1 = FORMATONE_MIS;
//            output2 = misRespository.getReportByFormatThree();
//            sheetName2= "Data";
//            columnName2 = FORMATTHREE_MIS;

            customExcel.setSheetName("Summary");
            customExcel.setColumnName(FORMATONE_MIS);
            customExcel.setData(misRespository.getReportByFormatOne(rName));
            objects.add(customExcel);
            customExcel2.setSheetName("Data");
            customExcel2.setColumnName(FORMATTHREE_MIS);
            customExcel2.setData(misRespository.getReportByFormatThree());
            objects.add(customExcel2);
        }
        // TODO 7 7(2)
        else if(rName.equals("DODEJA GROUP")){
//            output1 = misRespository.getReportForDODEJAGROUP();
//            sheetName1= "DodejaGroup";
//            columnName1 = FORMATFOUR_MIS;
//            output2 = misRespository.getReportByUnitedGroup();
//            sheetName2= "UnitedGroup";
//            columnName2 = FORMATFOUR_MIS;

            customExcel.setSheetName("DodejaGroup");
            customExcel.setColumnName(FORMATFOUR_MIS);
            customExcel.setData(misRespository.getReportForDODEJAGROUP());
            objects.add(customExcel);
            customExcel2.setSheetName("UnitedGroup");
            customExcel2.setColumnName(FORMATFOUR_MIS);
            customExcel2.setData(misRespository.getReportByUnitedGroup());
            objects.add(customExcel2);
        }
        // TODO 9 9(2)
        else if(rName.equals("KVO")){
//            output1 = misRespository.getReportForKVO();
//            sheetName1= "SummaryKvo";
//            columnName1 = FORMATONE_MIS;
//            output2 = misRespository.getReportByPinky();
//            sheetName2= "SummaryPinky";
//            columnName2 = FORMATONE_MIS;

            customExcel.setSheetName("SummaryKvo");
            customExcel.setColumnName(FORMATONE_MIS);
            customExcel.setData(misRespository.getReportForKVO());
            objects.add(customExcel);
            customExcel2.setSheetName("SummaryPinky");
            customExcel2.setColumnName(FORMATONE_MIS);
            customExcel2.setData(misRespository.getReportByPinky());
            objects.add(customExcel2);
        }
        // TODO DCB
        else if(rName.equals("DCB_GPRS")){
//            output1 = misRespository.getReportForDcbSheet1();
//            sheetName1= "Summary";
//            columnName1 = DCB_SHEET1;
//            output2 = misRespository.getReportByDCBSheet2();
//            sheetName2= "Data";
//            columnName2 = GPRS;

            customExcel.setSheetName("Summary");
            customExcel.setColumnName(DCB_SHEET1);
            customExcel.setData(misRespository.getReportForDcbSheet1());
            objects.add(customExcel);
            customExcel2.setSheetName("Data");
            customExcel2.setColumnName(GPRS);
            customExcel2.setData(misRespository.getReportByDCBSheet2());
            objects.add(customExcel2);
        }
        // TODO Finance
        else if(rName.equals("FINANCE")){
//            output1 = misRespository.getReportForFinanceSheet1();
//            sheetName1= "Summary1";
//            columnName1 = FINANCE_SHEET;
//            output2 = misRespository.getReportForFinanceSheet2();
//            sheetName2= "Summary2";
//            columnName2 = FINANCE_SHEET;

            customExcel.setSheetName("Summary1");
            customExcel.setColumnName(FINANCE_SHEET);
            customExcel.setData(misRespository.getReportForFinanceSheet1());
            objects.add(customExcel);
            customExcel2.setSheetName("Summary2");
            customExcel2.setColumnName(FINANCE_SHEET);
            customExcel2.setData(misRespository.getReportForFinanceSheet2());
            objects.add(customExcel2);
        }
        // TODO SHELL AMEX
        else if(rName.equals("SHELL AMEX")){
//            output1 = misRespository.getReportForAmexAssociation();
//            sheetName1= "Summary";
//            columnName1 = AMEXASS;
//            output2 = misRespository.getReportForAmexData();
//            sheetName2= "Data";
//            columnName2 = AMEXDATA;

            customExcel.setSheetName("Summary");
            customExcel.setColumnName(AMEXASS);
            customExcel.setData(misRespository.getReportForAmexAssociation());
            objects.add(customExcel);
            customExcel2.setSheetName("Data");
            customExcel2.setColumnName(AMEXDATA);
            customExcel2.setData(misRespository.getReportForAmexData());
            objects.add(customExcel2);
        }
        // TODO MID MONTHLY SUMMARY
        else if(rName.equals("MID MONTHLY SUMMARY")){
//            output1 = misRespository.getReportForMonthlyYtd();
//            sheetName1= "Summary";
//            columnName1 = AMEXASS;
//            output2 = misRespository.getReportForMonthlyMtd();
//            sheetName2= "Data";
//            columnName2 = AMEXDATA;
//
            customExcel.setSheetName("Summary");
            customExcel.setColumnName(AMEXASS);
            customExcel.setData(misRespository.getReportForMonthlyYtd());
            objects.add(customExcel);
            customExcel2.setSheetName("Data");
            customExcel2.setColumnName(AMEXDATA);
            customExcel2.setData(misRespository.getReportForMonthlyMtd());
            objects.add(customExcel2);
        }else if(rName.equals("PhysicalPOS")){
            customExcel.setSheetName("Summary_DC<2K");
            customExcel.setColumnName(DC);
            customExcel.setData(misRespository.getDebitCardLessThan2K());
            objects.add(customExcel);
            customExcel2.setSheetName("Summary_DC>2K");
            customExcel2.setColumnName(DC);
            customExcel2.setData(misRespository.getDebitCardGreaterThan2K());
            objects.add(customExcel2);

        }else if(rName.equals("RBI")){
            customExcel.setSheetName("Summary");
            customExcel.setColumnName(RBI_SUMMARY);
            customExcel.setData(misRespository.getRbiSummary());
            objects.add(customExcel);
            customExcel2.setSheetName("Data");
            customExcel2.setColumnName(RBI_DATA);
            customExcel2.setData(misRespository.getRbiData());
            objects.add(customExcel2);
        }else if(rName.equals("SETTLEMENT_MIS_IOB")){
            customExcel.setSheetName("Summary");
            customExcel.setColumnName(Settlement_SUMMARY);
            customExcel.setData(misRespository.getSettlementIOBSummary());
            objects.add(customExcel);
            customExcel2.setSheetName("Data");
            customExcel2.setColumnName(Settlement_DATA);
            customExcel2.setData( misRespository.getSettlementIOBData());
            objects.add(customExcel2);
        }
        else if(rName.equals("SETTLEMENT_MIS_FDR")){
            final String[] SUMMARY = {"MERCHANT_NAME","TERMINAL",DateUtil.currentDate("MMM_yy")};
            customExcel.setSheetName("Summary");
            customExcel.setColumnName(Settlement_SUMMARY);
            customExcel.setData(misRespository.getSettlementFDRSummary());
            objects.add(customExcel);
            customExcel2.setSheetName("Summary_Merchant_Level");
            customExcel2.setColumnName(SUMMARY);
            customExcel2.setData(misRespository.getSettlementFDRSummaryTwo());
            objects.add(customExcel2);
            customExcel3.setSheetName("Data");
            customExcel3.setColumnName(Settlement_DATA);
            customExcel3.setData(misRespository.getSettlementFDRData());
            objects.add(customExcel3);

        }
        else{
            return null;
        }
        return ReportsUtils.createExcel(objects);
//        return ReportsUtils.createExcel(output1,output2,sheetName1,sheetName2,columnName1,columnName2);
    }



    private void sendMailToCustomer(List<InvoiceDTO> merchantDataDtosMap) {
        if(merchantDataDtosMap.size()>0){
            merchantDataDtosMap.stream().forEach(l-> {
                try {
                    sendMail(l.getGst(),l.getFromDate(),l.getToDate(),l.getDocumentType(),l.getMailIds(),l.getDocumentName());
                } catch (Exception e) {
                    log.info("Error While sending mail pdf file"+e.getMessage());
                }
            });
        }
    }


    void sendMail(String gst,String fromDate,String toDate,String documentType,String tomailIds,String documentName) {
//        final String smtpServerHost ="email-smtp.us-west-2.amazonaws.com" ;
        final String smtpServerHost ="email-smtp.ap-south-1.amazonaws.com" ;
        final String smtpServerPort = "587";
//        final String smtpUserName ="AKIA2PZ6MUMQ6JJAWXA5" ;
        final String smtpUserName ="AKIA2PZ6MUMQ35ZNDFC5" ;
//        final String smtpUserPassword = "BBEDvco4bxm8QWC+l2huasW6bhlZiio4WRNAPvbkbnR8";
        final String smtpUserPassword = "BCKbuflsP/pWTJ9LLswIeyQ1cDupgiSN4zO6WzYWNi/c";
//        New Update for Invoice GST mail to change as bijlipay.invoice@bijlipay.co.in
//        final String fromUserEmail = "misreports@bijlipay.co.in";
        final String fromUserEmail ="bijlipay.invoice@bijlipay.co.in";
        String subject ="Invoice for Bijlipay GST "+gst+" – Mdr/Rental payment from "+fromDate+" To "+toDate+" - Invoice No."+documentName ;
        String nfileName = String.format("%s_%s_%s.pdf",documentType,gst,documentName);
        System.out.println(nfileName);
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", smtpServerPort);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);

            MimeMessage msg = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            // TODO Sent TO Trim
            String[] sentToArr = tomailIds.split(",");
            List<String> sentToItemList = new ArrayList<>();
            for (String s : sentToArr) {
                sentToItemList.add(s);
            }
            String[] itemsArraySentTo = new String[sentToItemList.size()];
            itemsArraySentTo = sentToItemList.toArray(itemsArraySentTo);
            String[] sentToTrimed = Arrays.stream(itemsArraySentTo).map(String::trim).toArray(String[]::new);


            helper.setTo(sentToTrimed);
            helper.setFrom(fromUserEmail);
            helper.setSubject(subject);
            helper.setText(String.format("Dear Customer\n" +
                    "\n" +
                    "Thank you for using Bijlipay Card Swiping Machine. \n" +
                    "\n" +
                    "Please find your GST invoice attached in this email for your reference. This invoice is towards the device mdr/rental paid/collected against your card swiping device\n" +
                    "\n" +
                    "In case you need any assistance you can write back to us at service@bijlipay.co.in (or) call us at 18004200235 between 9am to 9m\n" +
                    "\n" +
                    "We once again thank you for giving us this opportunity to serve you.\n" +
                    "\n" +
                    "Warm regards\n" +
                    "Team Bijlipay\n" +
                    "\n" +
                    "P.S - Note : please click here https://www.bijlipay.co.in/best-practices-POS-terminal-cleaning.pdf to follow the best practise to clean your POS device regularly.\n" +
                    "\n" +
                    "---------------------------------------------------------------------------------------------------------------\n" +
                    "This is an auto generated email. Please do not respond to this email id.\n" +
                    "For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in.\n" +
                    "---------------------------------------------------------------------------------------------------------------\n"));
            FileSystemResource file =
                    new FileSystemResource(INVOICE_FILES_DIR + "/" + nfileName);
            helper.addAttachment(file.getFilename(), file);
            Transport transport = session.getTransport();
            transport.connect(smtpServerHost, smtpUserName, smtpUserPassword);
//            System.out.print("Mail Sending...for the Mid "+mid);
            transport.sendMessage(msg, msg.getAllRecipients());
//            System.out.print("Mail sent...for the Mid "+mid);
        } catch (Exception ex) {
            log.info("Exception {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

}
