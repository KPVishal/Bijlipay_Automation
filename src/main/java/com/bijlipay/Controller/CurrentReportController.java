package com.bijlipay.Controller;

import com.bijlipay.Model.CurrentReport;
import com.bijlipay.Model.ReportLog;
import com.bijlipay.Model.User;
import com.bijlipay.Repository.CurrentReportRepository;
import com.bijlipay.Repository.ReportLogRepository;
import com.bijlipay.Security.CurrentUser;
import com.bijlipay.Security.UserPrincipal;
import com.bijlipay.Service.UserService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.bijlipay.Utils.Constants.yyyy_MM_dd;

@RestController
@RequestMapping(value = "/api")
public class CurrentReportController {

    private static final Logger logger = LoggerFactory.getLogger(CurrentReportController.class);
    public int i = 1;
    boolean test = false;
    String str = "select all";
    @Autowired
    private CurrentReportRepository currentReportRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportLogRepository reportLogRepository;
    @Value("${invoice.path}")
    private String filePath;

    @GetMapping(value = {"/merchantname", "/merchantname/{merchantname}"})
    public ResponseEntity<?> getmerchant(@PathVariable("merchantname") Optional<String> merchantname) {

        List<String> queryResult = new ArrayList<String>();
        List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();

        if (merchantname.isPresent()) {
            queryResult = currentReportRepository.getMerchantByNameWithSearch(merchantname.get());
            if (queryResult.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NO_CONTENT, "Name Not Available"), HttpStatus.NO_CONTENT);
            } else {
                queryResult.stream().forEach(l -> {
                    Map<String, String> tempMap = new HashMap<>();
                    tempMap.put("id", String.valueOf(i));
                    tempMap.put("name", l);
                    i++;
                    arrayList.add(tempMap);
                });
            }

        } else {
            queryResult = currentReportRepository.getMerchantByName();
            queryResult.stream().forEach(l -> {
                Map<String, String> tempMap = new HashMap<>();
                tempMap.put("id", String.valueOf(i));
                tempMap.put("name", l);
                i++;
                arrayList.add(tempMap);
            });
        }
        return new ResponseEntity<>(arrayList, HttpStatus.OK);
    }
    @GetMapping(value = { "/month-wise-report" })
    public ResponseEntity<?> email() throws Exception {

        List<Object[]> result = new ArrayList<>();
        List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
        try {

            Workbook wb = new XSSFWorkbook(new File(filePath));
            Sheet sheetAt = wb.getSheetAt(0);


            Font font = wb.createFont();
            font.setBold(true);

            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(font);


            boolean flag = true;
            for (Row row : sheetAt) {
                if (flag) {
                    flag = false;
                    continue;
                }
                String mid = row.getCell(0).getStringCellValue();
                String emailid = row.getCell(1).getStringCellValue();

                result = currentReportRepository.getMidCurrentBasedMonthReport(mid);
                List<List<Object>> lists = result.stream().map((t) -> {
                    Object txn_date = t[0];
                    Object mid1 = t[1];
                    Object tid = t[2];
                    Object merchant_name = t[3];
                    Object city = t[4];
                    Object card_no = t[5];
                    Object card_type = t[6];

                    Object rrn = t[7];
                    Object association_name = t[8];
                    Object approval_code = t[9];
                    Object paymentdate = t[10];

                    Object card_type1 = t[11];
                    Object narration = t[12];
                    Object card_product = t[13];


                    return Arrays.asList(txn_date, mid1, tid, merchant_name, city, card_no, card_type, rrn,
                            association_name, approval_code, paymentdate, narration, card_product);
                }).collect(Collectors.toList());
                String[] headers = {"TxnDate", "Mid", "Tid", "MerchantName", "City", "cardNo", "cardType", "Rrn", "AssociationName", "ApprovalCode", "PaymentDate", "narration", "card_product"};
                Workbook txnList = createReport("TxnList", headers, lists);
//			FileOutputStream fileOutputStream=new FileOutputStream(new Date().getTime()+".xlsx");
//			txnList.write(fileOutputStream);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                txnList.write(bos); // write excel data to a byte array

                sendMail(bos, emailid);
                txnList.close();
                try {
                    Files.deleteIfExists(Paths.get(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            return ResponseEntity.ok("Some error occured");
        }
        return ResponseEntity.ok("Success Send to Mail");
    }
    private void sendMail(ByteArrayOutputStream bos, String emailid) throws MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("misreports@bijlipay.co.in", "Avengers!123");
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("misreports@bijlipay.co.in", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailid));

        msg.setSubject("Settlement Report");

        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Dear All," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Please find the  enclosed attachment " + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in.", "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        MimeBodyPart attachPart = new MimeBodyPart();
        DataSource dataSource = new ByteArrayDataSource(bos.toByteArray(), "application/vnd.ms-excel");
        attachPart.setDataHandler(new DataHandler(dataSource));
        attachPart.setFileName(new Date().getTime()+".xlsx");
//		attachPart.attachFile("C:/Users/vinothkumar/Desktop/space issue.xlsx");
        multipart.addBodyPart(attachPart);
        msg.setContent(multipart);

        Transport.send(msg);

    }
    public static Workbook createReport(String sheetName, String[] headers, List<List<Object>> rows) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Create header
        Row headerRow = sheet.createRow(0);

        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i, CellType.STRING);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Insert rows
        for (int i = 0; i < rows.size(); i++) {

            // We have already used first row for header
            Row row = sheet.createRow(i + 1);

            List<Object> rowItem = rows.get(i);

            // Create cell and put values
            for (int j = 0; j < rowItem.size(); j++) {
                Cell cell = row.createCell(j);

                Object valueObject = rowItem.get(j);

                if (valueObject == null) {

                    cell.setCellValue("NA");

                } else if (valueObject instanceof Double) {

                    cell.setCellValue((Double) valueObject);

                } else if (valueObject instanceof BigDecimal) {

                    cell.setCellValue(((BigDecimal) valueObject).doubleValue());

                } else if (valueObject instanceof Integer) {

                    cell.setCellValue((Integer) valueObject);

                } else if (valueObject instanceof BigInteger) {

                    cell.setCellValue(((BigInteger) valueObject).intValue());

                } else if (valueObject instanceof Boolean) {

                    cell.setCellValue((Boolean) valueObject);

                } else if (valueObject instanceof Date) {

                    CellStyle cellStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((Date) valueObject);

                } else if (valueObject instanceof Long) {

                    cell.setCellValue((Long) valueObject);

                } else {

                    cell.setCellValue(valueObject.toString());
                }

            }

        }

        return workbook;
    }


    // Mid
    @GetMapping(value = {"/{merchantname}/mid"})
    public ResponseEntity<?> getmerchantmid1(@CurrentUser UserPrincipal currentUser,
                                             @PathVariable("merchantname") Optional<String> merchantname) {

        User user = userService.findById(currentUser.getId());

        ReportLog log = ReportLog.to(user.getUsername(), merchantname.get());
        reportLogRepository.save(log);

        List<String> queryResult = new ArrayList<String>();

        List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();

        if (test == false) {
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("id", String.valueOf(i));
            tempMap.put("mid", "select all");
            i++;
            arrayList.add(tempMap);
        }
        queryResult = currentReportRepository.getMidByName(merchantname);
        queryResult.stream().forEach(l -> {
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("id", String.valueOf(i));
            tempMap.put("mid", l);
            i++;
            arrayList.add(tempMap);
        });

        return new ResponseEntity<>(arrayList, HttpStatus.OK);
    }

    @GetMapping(value = {"/{merchantname}/{mid}/tid"})
    public ResponseEntity<?> getmidtid(@CurrentUser UserPrincipal currentUser,
                                       @PathVariable("merchantname") String merchantname, @PathVariable("mid") String mid) {

        User user = userService.findById(currentUser.getId());

        List<String> queryResult = new ArrayList<String>();
        List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();

        String[] midArray = mid.split(",");
        List<String> testTid = new ArrayList<>();
        for (String result : midArray) {
            testTid.add(result);
        }

        if (test == false) {
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("id", String.valueOf(i));
            tempMap.put("tid", "select all");
            i++;
            arrayList.add(tempMap);
        }
        if (testTid.contains(str)) {

            ReportLog reportLog = ReportLog.to(user.getUsername(), merchantname + "-" + mid);
            reportLogRepository.save(reportLog);

            queryResult = currentReportRepository.getTidbyme(merchantname);
            queryResult.stream().forEach(l -> {
                Map<String, String> tempMap = new HashMap<>();
                tempMap.put("id", String.valueOf(i));
                tempMap.put("tid", l);
                i++;

                arrayList.add(tempMap);
            });
        } else {
            ReportLog reportLog = ReportLog.to(user.getUsername(), merchantname + "-" + mid);
            reportLogRepository.save(reportLog);

            queryResult = currentReportRepository.getTid(testTid);
            queryResult.stream().forEach(l -> {
                Map<String, String> tempMap = new HashMap<>();
                tempMap.put("id", String.valueOf(i));
                tempMap.put("tid", l);
                i++;

                arrayList.add(tempMap);
            });
        }
        return new ResponseEntity<>(arrayList, HttpStatus.OK);
    }

    @GetMapping(value = {"/{merchantname}/{mid}/{tid}/{from}/{to}"})
    public ResponseEntity<?> reportGeneration(@CurrentUser UserPrincipal currentUser,
                                              @PathVariable("merchantname") String merchant_name, @PathVariable("mid") String mid,
                                              @PathVariable("tid") String tid, @PathVariable("from") long from, @PathVariable("to") long to,
                                              HttpServletResponse response) throws IOException {

        User user = userService.findById(currentUser.getId());

        List<CurrentReport> result = new ArrayList<>();

        String[] midArray = mid.split(",");
        List<String> midResult = new ArrayList<>();
        for (String midArrayresult : midArray) {
            midResult.add(midArrayresult);
        }

        String[] tidArray = tid.split(",");
        List<String> tidArrayResult = new ArrayList<>();
        for (String Arrayresult : tidArray) {
            tidArrayResult.add(Arrayresult);
        }

        // current
        if (DateUtil.givenMonth(from) == DateUtil.currentMonth()) {

            ReportLog reportLog = ReportLog.to(user.getUsername(), "merchantName-" + merchant_name + "-Mid-" + mid + "-Tid-"
                    + tid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);

            if (midResult.contains(str) && tidArrayResult.contains(str)) {
                result = currentReportRepository.getCurrentMonthReport(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd));
            } else if (tidArrayResult.contains(str)) {
                result = currentReportRepository.getCurrentMonthReport(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd), midResult);
            } else {
                result = currentReportRepository.getCurrentMonthReport(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd), midResult, tidArrayResult);
            }
        }
        // previous
        else if (DateUtil.givenMonth(from) == DateUtil.previousMonth()) {
            ReportLog reportLog = ReportLog.to(user.getUsername(), "merchantName-" + merchant_name + "-Mid-" + mid + "-Tid-"
                    + tid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);

            if (midResult.contains(str) && tidArrayResult.contains(str)) {
                result = currentReportRepository.getdownloadPrevious(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd));
            } else if (tidArrayResult.contains(str)) {
                result = currentReportRepository.getdownloadPrevious(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd), midResult);
            } else {
                result = currentReportRepository.getdownloadPrevious(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd), midResult, tidArrayResult);
            }
        }

        // previous last
//		else if (DateUtil.givenMonth(from) < DateUtil.previousMonth()
//				&& (DateUtil.givenMonth(to) < DateUtil.previousMonth())) {
//			ReportLog log = ReportLog.to(user.getUsername(), "merchantName-" + merchant_name + "-Mid-" + mid + "-Tid-"
//					+ tid + "-from-" + DateUtil.longToDate(from,yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to,yyyy_MM_dd));
//			reportLogRepository.save(log);
//
//			if (midResult.contains(str) && tidArrayResult.contains(str)) {
//				result = currentReportRepository.getdownloadLastPrevious(merchant_name, DateUtil.longToDate(from,yyyy_MM_dd),
//						DateUtil.longToDate(to,yyyy_MM_dd));
//			} else if (tidArrayResult.contains(str)) {
//				result = currentReportRepository.getdownloadLastPrevious(merchant_name, DateUtil.longToDate(from,yyyy_MM_dd),
//						DateUtil.longToDate(to,yyyy_MM_dd), midResult);
//			} else {
//				result = currentReportRepository.getdownloadLastPrevious(merchant_name, DateUtil.longToDate(from,yyyy_MM_dd),
//						DateUtil.longToDate(to,yyyy_MM_dd), midResult, tidArrayResult);
//			}
//		}

        // previous and previous last
        else {
            ReportLog reportLog = ReportLog.to(user.getUsername(), "merchantName-" + merchant_name + "-Mid-" + mid + "-Tid-"
                    + tid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);

            if (midResult.contains(str) && tidArrayResult.contains(str)) {
                result = currentReportRepository.getdownloadLastPreviousLast(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd));
            } else if (tidArrayResult.contains(str)) {
                result = currentReportRepository.getdownloadLastPreviousLast(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd), midResult);
            } else {

                result = currentReportRepository.getdownloadLastPreviousLast(merchant_name, DateUtil.longToDate(from, yyyy_MM_dd),
                        DateUtil.longToDate(to, yyyy_MM_dd), midResult, tidArrayResult);
            }
        }

        if (!(result.size() > 65000)) {
            if (result.size() > 0) {
                List<String> header = Arrays.asList("Txn_Date", "MID", "TID", "Merchant_Name", "City", "Card_No",
                        "Card_Type", "RRN", "Association_Name", "Approval_Code", "Batch_Code", "Txnamount", "MDR",
                        "GST", "Netamount", "Paymentdate", "Card_Type1", "Narration", "Card_Product");
                response.addHeader("content-disposition", "attachment; filename=Opsreport.xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(header, result,
                        "txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,"
                                + "netamount,paymentdate,card_type1,narration,card_product",
                        response.getOutputStream());
                response.flushBuffer();
                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(new ApiResponse(HttpStatus.NO_CONTENT, "Data Not Available"),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Exceeds Row Limit"),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = {"/{merchantname}/{from}/{to}"})
    public ResponseEntity<?> mereportGeneration(@CurrentUser UserPrincipal currentUser,
                                                @PathVariable("merchantname") String merchant_name, @PathVariable("from") long from,
                                                @PathVariable("to") long to, HttpServletResponse response) throws IOException {
        User user = userService.findById(currentUser.getId());
        List<CurrentReport> result = new ArrayList<>();

        String[] meArray = merchant_name.split(",");
        List<String> meResult = new ArrayList<>();
        for (String midArrayresult : meArray) {
            meResult.add(midArrayresult);
        }

        // current
        if (DateUtil.givenMonth(from) == DateUtil.currentMonth()) {
            ReportLog reportLog = ReportLog.to(user.getUsername(), "merchantName-" + merchant_name + "-from-"
                    + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getMeCurrentMonthReport(meResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }

        // previous
        else if (DateUtil.givenMonth(from) == DateUtil.previousMonth()) {
            ReportLog reportLog = ReportLog.to(user.getUsername(), "merchantName-" + merchant_name + "-from-"
                    + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getMedownloadPrevious(meResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }

        // previous last
//		else if (DateUtil.givenMonth(from) < DateUtil.previousMonth()
//				&& (DateUtil.givenMonth(to) < DateUtil.previousMonth())) {
//			ReportLog reportLog = ReportLog.to(user.getUsername(), "merchantName-" + merchant_name + "-from-"
//					+ DateUtil.longToDate(from,yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to,yyyy_MM_dd));
//			reportLogRepository.save(reportLog);
//
//			result = currentReportRepository.getMedownloadLastPrevious(meResult, DateUtil.longToDate(from,yyyy_MM_dd),
//					DateUtil.longToDate(to,yyyy_MM_dd));
//		}

        // previous and previous last

        else {
            System.out.println("Merchant previous last");
            ReportLog reportLog = ReportLog.to(user.getUsername(), "merchantName-" + merchant_name + "-from-"
                    + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getMedownloadLastPreviousLast(meResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }

        logger.info("Size {}", result.size());
        if (!(result.size() > 65000)) {
            if (result.size() > 0) {
                List<String> header = Arrays.asList("Txn_Date", "MID", "TID", "Merchant_Name", "City", "Card_No",
                        "Card_Type", "RRN", "Association_Name", "Approval_Code", "Batch_Code", "Txnamount", "MDR",
                        "GST", "Netamount", "Paymentdate", "Card_Type1", "Narration", "Card_Product");
                response.addHeader("content-disposition", "attachment; filename=Opsreport.xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(header, result, "txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,netamount,paymentdate,card_type1,narration,card_product ", response.getOutputStream());
                response.flushBuffer();
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NO_CONTENT, "Data Not Available"),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Exceeds Row Limit"),
                HttpStatus.BAD_REQUEST);

    }

    @GetMapping(value = {"/mid/{mid}/{from}/{to}"})
    public ResponseEntity<?> midreportGeneration(@CurrentUser UserPrincipal currentUser,
                                                 @PathVariable("mid") String mid, @PathVariable("from") long from, @PathVariable("to") long to,
                                                 HttpServletResponse response) throws IOException {

        User user = userService.findById(currentUser.getId());
        List<CurrentReport> result = new ArrayList<>();
        String[] midArray = mid.split(",");
        List<String> midResult = new ArrayList<>();
        for (String midArrayresult : midArray) {
            midResult.add(midArrayresult);
        }
        // current
        if (DateUtil.givenMonth(from) == DateUtil.currentMonth()) {
            ReportLog reportLog = ReportLog.to(user.getUsername(),
                    "Mid-" + mid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getMidCurrentMonthReport(midResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }

        // previous
        else if (DateUtil.givenMonth(from) == DateUtil.previousMonth()) {
            ReportLog reportLog = ReportLog.to(user.getUsername(),
                    "Mid-" + mid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getMiddownloadPrevious(midResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }

        // previous last
//		else if (DateUtil.givenMonth(from) < DateUtil.previousMonth()
//				&& (DateUtil.givenMonth(to) < DateUtil.previousMonth())) {
//			ReportLog reportLog = ReportLog.to(user.getUsername(),
//					"Mid-" + mid + "-from-" + DateUtil.longToDate(from,yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to,yyyy_MM_dd));
//			reportLogRepository.save(reportLog);
//
//			result = currentReportRepository.getMiddownloadLastPrevious(midResult, DateUtil.longToDate(from,yyyy_MM_dd),
//					DateUtil.longToDate(to,yyyy_MM_dd));
//		}

        // previous and previous last
        else {
            ReportLog reportLog = ReportLog.to(user.getUsername(),
                    "Mid-" + mid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getMiddownloadLastPreviousLast(midResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }
        if (!(result.size() > 60000)) {
            if (result.size() > 0) {
                List<String> header = Arrays.asList("Txn_Date", "MID", "TID", "Merchant_Name", "City", "Card_No",
                        "Card_Type", "RRN", "Association_Name", "Approval_Code", "Batch_Code", "Txnamount", "MDR",
                        "GST", "Netamount", "Paymentdate", "Card_Type1", "Narration", "Card_Product");
                response.addHeader("content-disposition", "attachment; filename=Opsreport.xls");
                response.setContentType("application/vnd.ms-excel");

                new SimpleExporter().gridExport(header, result,
                        "txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,"
                                + "netamount,paymentdate,card_type1,narration,card_product",
                        response.getOutputStream());
                response.flushBuffer();
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NO_CONTENT, "Data Not Available"),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Exceeds Row Limit"),
                HttpStatus.BAD_REQUEST);

    }

    @GetMapping(value = {"/tid/{tid}/{from}/{to}"})
    public ResponseEntity<?> tidreportGeneration(@CurrentUser UserPrincipal currentUser,
                                                 @PathVariable("tid") String tid, @PathVariable("from") long from, @PathVariable("to") long to,
                                                 HttpServletResponse response) throws IOException {


        User user = userService.findById(currentUser.getId());
        List<CurrentReport> result = new ArrayList<>();
        String[] tidArray = tid.split(",");
        List<String> tidResult = new ArrayList<>();
        for (String midArrayresult : tidArray) {
            tidResult.add(midArrayresult);
        }
        // current
        if (DateUtil.givenMonth(from) == DateUtil.currentMonth()) {
            ReportLog reportLog = ReportLog.to(user.getUsername(),
                    "Tid-" + tid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getTidCurrentMonthReport(tidResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }
        // previous
        else if (DateUtil.givenMonth(from) == DateUtil.previousMonth()) {
            ReportLog reportLog = ReportLog.to(user.getUsername(),
                    "Tid-" + tid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getTiddownloadPrevious(tidResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }
        // previous last
//		else if (DateUtil.givenMonth(from) < DateUtil.previousMonth()
//				&& (DateUtil.givenMonth(to) < DateUtil.previousMonth())) {
//			ReportLog log = ReportLog.to(user.getUsername(),
//					"Tid-" + tid + "-from-" + DateUtil.longToDate(from,yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to,yyyy_MM_dd));
//			reportLogRepository.save(log);
//
//			result = currentReportRepository.getTiddownloadLastPrevious(tidResult, DateUtil.longToDate(from,yyyy_MM_dd),
//					DateUtil.longToDate(to,yyyy_MM_dd));
//		}

        // previous and previous last
        else {
            ReportLog reportLog = ReportLog.to(user.getUsername(),
                    "Tid-" + tid + "-from-" + DateUtil.longToDate(from, yyyy_MM_dd) + "-To-" + DateUtil.longToDate(to, yyyy_MM_dd));
            reportLogRepository.save(reportLog);
            result = currentReportRepository.getTiddownloadLastPreviousLast(tidResult, DateUtil.longToDate(from, yyyy_MM_dd),
                    DateUtil.longToDate(to, yyyy_MM_dd));
        }
        if (!(result.size() > 65000)) {
            if (result.size() > 0) {
                List<String> header = Arrays.asList("Txn_Date", "MID", "TID", "Merchant_Name", "City", "Card_No",
                        "Card_Type", "RRN", "Association_Name", "Approval_Code", "Batch_Code", "Txnamount", "MDR",
                        "GST", "Netamount", "Paymentdate", "Card_Type1", "Narration", "Card_Product");
                response.addHeader("content-disposition", "attachment; filename=Opsreport.xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(header, result,
                        "txn_date,mid,tid,merchant_name,city,card_no,card_type,rrn,association_name,approval_code,batch_code,txnamount,mdr,gst,"
                                + "netamount,paymentdate,card_type1,narration,card_product",
                        response.getOutputStream());
                response.flushBuffer();
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NO_CONTENT, "Data Not Available"),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Exceeds Row Limit"),
                HttpStatus.BAD_REQUEST);
    }

}
