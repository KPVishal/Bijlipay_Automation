package com.bijlipay.Controller;

import com.bijlipay.Model.*;
import com.bijlipay.Repository.*;
import com.bijlipay.Service.FileStorageService;
import com.bijlipay.Utils.*;
import com.itextpdf.text.DocumentException;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.util.*;

import static com.bijlipay.Utils.Constants.*;

@RestController
@RequestMapping("/api")
public class PdfController {

    private static final Logger log = LoggerFactory.getLogger(PdfController.class);
    long count = 0;
    Connection conn;
    Statement stmt;
    ResultSet resultset = null;
    DatabaseMetaData md;
    private boolean firstrow = true;
    @Autowired
    private Invoice_NumberRepos invoice_NumberRepos;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private NewInvoiceRepos newInvoiceRepos;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Invoicelogrepo invoicerepo;
    @Autowired
    private MdrInsert mdrinsert;
    @Autowired
    private MailHandler mailHandler;
    @Autowired
    private Federalmerchantsrepo fdrlmrchtrepo;

    @GetMapping(value = "/finance/date/{date}")
    public ResponseEntity<?> pdfReportWithDate(@PathVariable("date") long date) throws IOException, DocumentException {


        Invoice_Number invoice_Number = invoice_NumberRepos.findbyInvoiceId();
        count = invoice_Number.getInvoice_Number();
        List<Merchant_details> detailsList = merchantRepository.findAll();

        log.info("Merchant Details list {}", detailsList);
        String longToStringDate = DateUtil.longToDate(date, MMM_YY);
//                NtlmPasswordAuthentication auth =
//                        new NtlmPasswordAuthentication(SMB_PDF_DOMAIN, SMB_PDF_USERNAME, SMB_PDF_PASSWORD);
        try {
//                    SmbFile monthFolder =
//                            new SmbFile(PDF_MDR_FILE_PATH + DateUtil.longToDate(date, MMM_yy), auth);
            File monthFolder = new File(PDF_MDR_FILE_PATH_NEW + DateUtil.longToDate(date, MMM_yy));
            if (!monthFolder.exists()) {
                log.info("file path exits so delete{}", monthFolder);
                monthFolder.mkdir();
            }
            detailsList.forEach(d -> {
                Merchant_details response = new Merchant_details();
                PdfReport report = new PdfReport();
                response.setName(d.getName());
                response.setFirstAddress(d.getFirstAddress());
                response.setSecondAddress(d.getSecondAddress());
                response.setContactNumber(d.getContactNumber());
                response.setMerchant_GstrNumber(d.getMerchant_GstrNumber());
                response.setPincode(d.getPincode());
                response.setTid(d.getTid());
                response.setCategory(d.getCategory());
                response.setStateCode(d.getStateCode());
                response.setEmail(d.getEmail());
                response.setDate(DateUtil.invoiceDate(date));
                response.setDateForInvoice(DateUtil.longToDate(date, MMM));
                response.setDateForMail(DateUtil.longToDate(date, MMM_YY));
                response.setDateinvoice(DateUtil.longToDate(date, MMM__YY));

                List<String> result = merchantRepository.findtid(d.getTid());
                List<String> result1 = merchantRepository.findfdrtid(d.getTid());
                if (result != null && result.size() > 0) {
                    Double lessthan2kamount = merchantRepository.findByTidAndMonth(response.getTid(), longToStringDate);
                    Double lessThan2kForSumofMdr = merchantRepository.findbyTidandMonthForMDR(response.getTid(),
                            longToStringDate);
                    Double greaterThantotalAmount = merchantRepository
                            .findByTidAndMonthForGreaterThan2k(response.getTid(), longToStringDate);
                    Double greaterThan2kForSumofMdr = merchantRepository
                            .findbyTidandMonthForMDRAndForGreaterThan2k(response.getTid(), longToStringDate);
                    Double greaterThan2kForSumofgst = merchantRepository
                            .findbyTidandMonthForGstAndForGreaterThan2k(response.getTid(), longToStringDate);
                    if (lessthan2kamount == null) {
                        response.setTransaction_value(0.00);
                    } else {
                        response.setTransaction_value(lessthan2kamount);
                    }
                    if (lessThan2kForSumofMdr == null) {
                        response.setMdr(0.00);
                    } else {
                        response.setMdr(lessThan2kForSumofMdr);
                    }
                    if (greaterThantotalAmount == null) {
                        response.setGreatertransaction_value(0.00);
                    } else {
                        response.setGreatertransaction_value(greaterThantotalAmount);
                    }
                    if (greaterThan2kForSumofMdr == null) {
                        response.setGreatermdr(0.00);
                    } else {
                        response.setGreatermdr(greaterThan2kForSumofMdr);
                    }
                    if (greaterThan2kForSumofgst == null) {
                        response.setGreatergst(0.00);
                    } else {
                        response.setGreatergst(greaterThan2kForSumofgst);
                    }
                    System.out.println(d.getTid());
                    System.out.println(response.getTransaction_value());
//                    System.out.println(response.getGst().floatValue());
                    System.out.println(response.getMdr());
                    System.out.println("=============================================================================");
                    System.out.println(d.getTid());
                    System.out.println(response.getGreatertransaction_value());
                    System.out.println(response.getGreatergst().floatValue());
                    System.out.println(response.getGreatermdr());
                    Double value = response.getGreatermdr() + response.getMdr();
                    if (value > 0) {
                        System.out.println("inside pdf create");
                        String mdrInvoice = monthFolder.getAbsolutePath();
                        report.createPDFMDR(date, response, ++count);
                        Invoice_Log log = Invoice_Log.to(d.getTid(), DateUtil.longToDate(date, MMM_YY), count);
                        invoicerepo.save(log);

                        if (!(response.getEmail().equalsIgnoreCase("NA") || response.getEmail() == "NA")) {
                            try {
                                //  mailHandler.PdfsendEmail(mdrInvoice, response, DateUtil.longToDate(dateValueThread,MMM_YY));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Email not found_" + d.getTid());
                        }
                        count = count++;
                        System.out.println(count);
                    }
                } else if (result1 != null && result1.size() > 0) {
                    Double lessthan2kamount = merchantRepository.findByfederalTidAndMonth(response.getTid(), longToStringDate);
                    Double lessThan2kForSumofMdr = merchantRepository.findbyfederalTidandMonthForMDR(response.getTid(),
                            longToStringDate);
                    Double greaterThantotalAmount = merchantRepository
                            .findByfederalTidAndMonthForGreaterThan2k(response.getTid(), longToStringDate);
                    Double greaterThan2kForSumofMdr = merchantRepository
                            .findbyfederalTidandMonthForMDRAndForGreaterThan2k(response.getTid(), longToStringDate);
                    Double greaterThan2kForSumofgst = merchantRepository
                            .findbyfederalTidandMonthForGstAndForGreaterThan2k(response.getTid(), longToStringDate);
                    if (lessthan2kamount == null) {
                        response.setTransaction_value(0.00);
                    } else {
                        response.setTransaction_value(lessthan2kamount);
                    }
                    if (lessThan2kForSumofMdr == null) {
                        response.setMdr(0.00);
                    } else {
                        response.setMdr(lessThan2kForSumofMdr);
                    }
                    if (greaterThantotalAmount == null) {
                        response.setGreatertransaction_value(0.00);
                    } else {
                        response.setGreatertransaction_value(greaterThantotalAmount);
                    }
                    if (greaterThan2kForSumofMdr == null) {
                        response.setGreatermdr(0.00);
                    } else {
                        response.setGreatermdr(greaterThan2kForSumofMdr);
                    }
                    if (greaterThan2kForSumofgst == null) {
                        response.setGreatergst(0.00);
                    } else {
                        response.setGreatergst(greaterThan2kForSumofgst);
                    }
                    System.out.println(d.getTid());
                    System.out.println(response.getTransaction_value());
//                        System.out.println(response.getGst().floatValue());
                    System.out.println(response.getMdr());
                    System.out.println("=============================================================================");
                    System.out.println(d.getTid());
                    System.out.println(response.getGreatertransaction_value());
                    System.out.println(response.getGreatergst().floatValue());
                    System.out.println(response.getGreatermdr());
                    Double value = response.getGreatermdr() + response.getMdr();
                    if (value > 0) {
                        System.out.println("inside pdf create");
                        String mdrInvoice = monthFolder.getAbsolutePath();
                        report.createPDFMDR(date, response, ++count);
                        Invoice_Log log = Invoice_Log.to(d.getTid(), DateUtil.longToDate(date, MMM_YY), count);
                        invoicerepo.save(log);

                        if (!(response.getEmail().equalsIgnoreCase("NA") || response.getEmail() == "NA")) {
                            try {
                                //  mailHandler.PdfsendEmail(mdrInvoice, response, DateUtil.longToDate(dateValueThread,MMM_YY));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Email not found_" + d.getTid());
                        }
                        count = count++;
                        System.out.println(count);
                    }
                }
            });
            invoice_NumberRepos.updateInvoiceNumber(count);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Successfully created"), HttpStatus.OK);
    }


    @GetMapping(value = "/finance/tid/{tid}/date/{date}")
    public ResponseEntity<?> pdfReportWithTID(@PathVariable("tid") String tid, @PathVariable("date") long date)
            throws IOException, DocumentException {
        String[] tidArray = tid.split(",");
//        NtlmPasswordAuthentication auth =
//                new NtlmPasswordAuthentication(SMB_PDF_DOMAIN, SMB_PDF_USERNAME, SMB_PDF_PASSWORD);
//        SmbFile monthFolder =
//                new SmbFile(PDF_MDR_FILE_PATH + DateUtil.longToDate(date, MMM_yy), auth);
        File monthFolder = new File(PDF_MDR_FILE_PATH_NEW + DateUtil.longToDate(date, MMM_yy));
        if (!monthFolder.exists()) {
            log.info("file path exits so delete{}", monthFolder);
            monthFolder.mkdir();
        }
        for (String Arrayresult : tidArray) {
            System.out.println(Arrayresult + ">>>>" + DateUtil.longToDate(date, MMM_YY));
            Invoice_Log invoiceno = invoicerepo.tidswithdate(Arrayresult, DateUtil.longToDate(date, MMM_YY));
            if (!(invoiceno == null) || invoiceno.getLogId() != 0) {
                System.out.println(invoiceno.getLogId() + ">>>" + invoiceno.getInvoiceno());
                System.out.println("test");
                long invoice_Number = invoiceno.getInvoiceno();
                count = invoice_Number;
                System.out.println("test");
                List<Merchant_details> detailsList = merchantRepository.findMerchantByTid(Arrayresult);
                System.out.println(detailsList);
                String longToStringDate = DateUtil.longToDate(date, MMM_YY);
                System.out.println("Test2");
                detailsList.forEach(d -> {
                    System.out.println("Test3");
                    Merchant_details response = new Merchant_details();
                    PdfReport report = new PdfReport();
                    response.setName(d.getName());
                    response.setFirstAddress(d.getFirstAddress());
                    response.setSecondAddress(d.getSecondAddress());
                    response.setContactNumber(d.getContactNumber());
                    response.setMerchant_GstrNumber(d.getMerchant_GstrNumber());
                    response.setPincode(d.getPincode());
                    response.setTid(d.getTid());
                    response.setCategory(d.getCategory());
                    response.setStateCode(d.getStateCode());
                    response.setEmail(d.getEmail());
                    response.setDate(DateUtil.invoiceDate(date));
                    response.setDateForInvoice(DateUtil.longToDate(date, MMM));
                    response.setDateForMail(DateUtil.longToDate(date, MMM_YY));
                    response.setDateinvoice(DateUtil.longToDate(date, MMM__YY));

                    Double lessthan2kamount = merchantRepository.findByTidAndMonth(response.getTid(), longToStringDate);

                    Double lessThan2kForSumofMdr = merchantRepository.findbyTidandMonthForMDR(response.getTid(),
                            longToStringDate);

                    Double greaterThantotalAmount = merchantRepository.findByTidAndMonthForGreaterThan2k(response.getTid(),
                            longToStringDate);

                    Double greaterThan2kForSumofMdr = merchantRepository
                            .findbyTidandMonthForMDRAndForGreaterThan2k(response.getTid(), longToStringDate);

                    Double greaterThan2kForSumofgst = merchantRepository
                            .findbyTidandMonthForGstAndForGreaterThan2k(response.getTid(), longToStringDate);

                    if (lessthan2kamount == null) {
                        response.setTransaction_value(0.00);
                    } else {
                        response.setTransaction_value(lessthan2kamount);
                    }

                    if (lessThan2kForSumofMdr == null) {
                        response.setMdr(0.00);
                    } else {
                        response.setMdr(lessThan2kForSumofMdr);
                    }

                    if (greaterThantotalAmount == null) {
                        response.setGreatertransaction_value(0.00);
                    } else {
                        response.setGreatertransaction_value(greaterThantotalAmount);
                    }

                    if (greaterThan2kForSumofMdr == null) {
                        response.setGreatermdr(0.00);
                    } else {
                        response.setGreatermdr(greaterThan2kForSumofMdr);
                    }

                    if (greaterThan2kForSumofgst == null) {
                        response.setGreatergst(0.00);
                    } else {
                        response.setGreatergst(greaterThan2kForSumofgst);
                    }

                    System.out.println(d.getTid());
                    System.out.println(response.getTransaction_value()); //

                    System.out.println(response.getMdr());

                    System.out.println("   =============================================================================");

                    System.out.println(d.getTid());
                    System.out.println(response.getGreatertransaction_value());
                    System.out.println(response.getGreatergst().floatValue());
                    System.out.println(response.getGreatermdr());

                    Double value = response.getGreatermdr() + response.getMdr();
                    if (value > 0) {
                        System.out.println("inside pdf create");
                        String rental = monthFolder.getAbsolutePath();
                        report.createPDF(date, response, count);

                    }
                });
            } else {
                System.out.println("test");
                Invoice_Number invoice_Number = invoice_NumberRepos.findbyInvoiceId();
                count = invoice_Number.getInvoice_Number();
                System.out.println("test");
                List<Merchant_details> detailsList = merchantRepository.findMerchantByTid(Arrayresult);
                System.out.println(detailsList);
                String longToStringDate = DateUtil.longToDate(date, MMM_YY);
                System.out.println("Test2");
                detailsList.forEach(d -> {
                    System.out.println("Test3");
                    Merchant_details response = new Merchant_details();
                    PdfReport report = new PdfReport();
                    response.setName(d.getName());
                    response.setFirstAddress(d.getFirstAddress());
                    response.setSecondAddress(d.getSecondAddress());
                    response.setContactNumber(d.getContactNumber());
                    response.setMerchant_GstrNumber(d.getMerchant_GstrNumber());
                    response.setPincode(d.getPincode());
                    response.setTid(d.getTid());
                    response.setCategory(d.getCategory());
                    response.setStateCode(d.getStateCode());
                    response.setEmail(d.getEmail());
                    response.setDate(DateUtil.invoiceDate(date));
                    response.setDateForInvoice(DateUtil.longToDate(date, MMM));
                    response.setDateForMail(DateUtil.longToDate(date, MMM_YY));
                    response.setDateinvoice(DateUtil.longToDate(date, MMM__YY));

                    Double lessthan2kamount = merchantRepository.findByTidAndMonth(response.getTid(), longToStringDate);

                    Double lessThan2kForSumofMdr = merchantRepository.findbyTidandMonthForMDR(response.getTid(),
                            longToStringDate);

                    Double greaterThantotalAmount = merchantRepository.findByTidAndMonthForGreaterThan2k(response.getTid(),
                            longToStringDate);

                    Double greaterThan2kForSumofMdr = merchantRepository
                            .findbyTidandMonthForMDRAndForGreaterThan2k(response.getTid(), longToStringDate);

                    Double greaterThan2kForSumofgst = merchantRepository
                            .findbyTidandMonthForGstAndForGreaterThan2k(response.getTid(), longToStringDate);

                    if (lessthan2kamount == null) {
                        response.setTransaction_value(0.00);
                    } else {
                        response.setTransaction_value(lessthan2kamount);
                    }

                    if (lessThan2kForSumofMdr == null) {
                        response.setMdr(0.00);
                    } else {
                        response.setMdr(lessThan2kForSumofMdr);
                    }

                    if (greaterThantotalAmount == null) {
                        response.setGreatertransaction_value(0.00);
                    } else {
                        response.setGreatertransaction_value(greaterThantotalAmount);
                    }

                    if (greaterThan2kForSumofMdr == null) {
                        response.setGreatermdr(0.00);
                    } else {
                        response.setGreatermdr(greaterThan2kForSumofMdr);
                    }

                    if (greaterThan2kForSumofgst == null) {
                        response.setGreatergst(0.00);
                    } else {
                        response.setGreatergst(greaterThan2kForSumofgst);
                    }

                    System.out.println(d.getTid());
                    System.out.println(response.getTransaction_value()); //

                    System.out.println(response.getMdr());

                    System.out.println("   =============================================================================");

                    System.out.println(d.getTid());
                    System.out.println(response.getGreatertransaction_value());
                    System.out.println(response.getGreatergst().floatValue());
                    System.out.println(response.getGreatermdr());

                    Double value = response.getGreatermdr() + response.getMdr();
                    if (value > 0) {
                        System.out.println("inside pdf create");
                        String rental = monthFolder.getAbsolutePath();
                        report.createPDF(date, response, ++count);
                        Invoice_Log log = Invoice_Log.to(d.getTid(), DateUtil.longToDate(date, MMM_YY), count);
                        invoicerepo.save(log);
                        count = count++;
                    }
                });
                invoice_NumberRepos.updateInvoiceNumber(count);
            }
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Successfully created"), HttpStatus.OK);
    }

    @GetMapping(value = "/finance/tids")
    public void pdfReportsWithTID(HttpServletResponse response)
            throws IOException, DocumentException, ParseException {
        int ro = 1;
        try {
            Resource resource = fileStorageService.loadFileAsResource("NewInvoice.xlsx");
            Workbook workbook = new XSSFWorkbook(new FileInputStream(resource.getFile().getAbsolutePath()));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
//             NtlmPasswordAuthentication auth =
//                     new NtlmPasswordAuthentication(SMB_PDF_DOMAIN, SMB_PDF_USERNAME, SMB_PDF_PASSWORD);

            try {
                log.info("File path ========== " + PDF_RENTAL_FILE_PATH_NEW + DateUtil.currentDate(MMM_yy));
//                 SmbFile monthFolder = new SmbFile(PDF_RENTAL_FILE_PATH_NEW + DateUtil.currentDate(MMM_yy));
                String dirPath = PDF_RENTAL_FILE_PATH_NEW + DateUtil.currentDate(MMM_yy);
                File monthFolder = new File(dirPath);
                if (!monthFolder.exists()) {
                    log.info("file path exits or Not ? !: {}", monthFolder.exists());
                    log.info("Creating file : ------------- : {}", monthFolder.mkdir());
                    Path check = Files.createDirectories(Paths.get(monthFolder.getAbsolutePath()));
                    log.info("FIle created : --------- : {}", check);
                }
                List<NewInvoice> map = new ArrayList<NewInvoice>();
                for (Row row : sheet) {
                    if ( row.getRowNum() == 0 ) {
                        continue;
                    }
                    NewInvoice nv = new NewInvoice();
                    DataFormatter dataFormatter = new DataFormatter();
                    log.info("1 : name : {}", String.valueOf(row.getCell(0)));
                    nv.setName(String.valueOf(row.getCell(0)));
                    log.info("2 : Tid : {}", String.valueOf(row.getCell(1)));
                    nv.setTid(String.valueOf(row.getCell(1).getStringCellValue()));
                    System.out.println("3");
                    nv.setFirstAddress(String.valueOf(row.getCell(2).getStringCellValue()));
                    System.out.println("4");
                    nv.setContactNumber(dataFormatter.formatCellValue(row.getCell(3)));
                    System.out.println("5");
                    nv.setMerchant_GstrNumber(String.valueOf(row.getCell(4).getStringCellValue()));
                    System.out.println("6");
                    nv.setStateCode(dataFormatter.formatCellValue(row.getCell(5)));
                    System.out.println("7");
                    nv.setValue(String.valueOf(row.getCell(6).getStringCellValue()));
                    System.out.println("8");
                    nv.setiGst(row.getCell(7).getStringCellValue());
                    System.out.println("9");
                    log.info("9 : SGST : {}", row.getCell(8).getStringCellValue());
                    nv.setStateGst(row.getCell(8).getStringCellValue());
                    System.out.println("10");
                    nv.setCentralGst(row.getCell(9).getStringCellValue());
                    System.out.println("11");
                    nv.setCreatedAt(DateUtil.currentDate(new Date()));
                    System.out.println("12");
                    nv.setDate(row.getCell(10).getStringCellValue());
                    System.out.println("13");
                    nv.setInvoice_Number(row.getCell(11).getStringCellValue());
                    System.out.println("14");
                    nv.setDescription(row.getCell(12).getStringCellValue());
                    System.out.println("15");
                    InvoiceReport ir = new InvoiceReport();
                    ir.createPDF(nv, monthFolder.getAbsolutePath());
                    Double totalGst = Double.valueOf(nv.getCentralGst()) + Double.valueOf(nv.getStateGst())
                            + Double.valueOf(nv.getiGst());
                    nv.setGstTotal(String.valueOf(totalGst));
                    newInvoiceRepos.save(nv);
                }
//                while (rowIterator.hasNext()) {
//                    NewInvoice nv = new NewInvoice();
//                    DataFormatter dataFormatter = new DataFormatter();
//                    System.out.println("1");
//                    nv.setName(String.valueOf(sheet.getRow(ro).getCell(0)));
//                    System.out.println("2");
//                    nv.setTid(String.valueOf(sheet.getRow(ro).getCell(1).getStringCellValue()));
//                    System.out.println("3");
//                    nv.setFirstAddress(String.valueOf(sheet.getRow(ro).getCell(2).getStringCellValue()));
//                    System.out.println("4");
//                    nv.setContactNumber(dataFormatter.formatCellValue(sheet.getRow(ro).getCell(3)));
//                    System.out.println("5");
//                    nv.setMerchant_GstrNumber(String.valueOf(sheet.getRow(ro).getCell(4).getStringCellValue()));
//                    System.out.println("6");
//                    nv.setStateCode(dataFormatter.formatCellValue(sheet.getRow(ro).getCell(5)));
//                    System.out.println("7");
//                    nv.setValue(String.valueOf(sheet.getRow(ro).getCell(6).getStringCellValue()));
//                    System.out.println("8");
//                    nv.setiGst(sheet.getRow(ro).getCell(7).getStringCellValue());
//                    System.out.println("9");
//                    nv.setStateGst(sheet.getRow(ro).getCell(8).getStringCellValue());
//                    System.out.println("10");
//                    nv.setCentralGst(sheet.getRow(ro).getCell(9).getStringCellValue());
//                    System.out.println("11");
//                    nv.setCreatedAt(DateUtil.currentDate(new Date()));
//                    System.out.println("12");
//                    nv.setDate(sheet.getRow(ro).getCell(10).getStringCellValue());
//                    System.out.println("13");
//                    nv.setInvoice_Number(sheet.getRow(ro).getCell(11).getStringCellValue());
//                    System.out.println("14");
//                    nv.setDescription(sheet.getRow(ro).getCell(12).getStringCellValue());
//                    System.out.println("15");
//                    InvoiceReport ir = new InvoiceReport();
//                    ir.createPDF(nv, monthFolder.getAbsolutePath());
//                    Double totalGst = Double.valueOf(nv.getCentralGst()) + Double.valueOf(nv.getStateGst())
//                            + Double.valueOf(nv.getiGst());
//                    nv.setGstTotal(String.valueOf(totalGst));
//                    newInvoiceRepos.save(nv);
//                    ro++;
//                }
//                     log.info("PDF file start to insert");
                workbook.close();
//                     wb.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<NewInvoice> result = newInvoiceRepos.getdata(DateUtil.currentDate(new Date()));
            if (result.size() > 0) {
                List<String> header = Arrays.asList("invoice_Number", "Merchant_name", "Tid", "Merchant_address1",
                        "merchant_Contact_No", "GSTR_no", "state_Code", "value", "IGST", "SGST", "CGST", "date", "gstTotal",
                        "category");
                response.addHeader("content-disposition", "attachment; filename=NewInvoiceWithInvNo.xlsx");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(header, result, "invoice_Number, name,tid, firstAddress,"
                                + " contactNumber,merchant_GstrNumber, stateCode, value, iGst, stateGst, centralGst, date, gstTotal,category",
                        response.getOutputStream());
                response.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping(value = "/finance/mdr")
    public ResponseEntity<?> pdfReportsWithMDR(HttpServletResponse response)
            throws IOException, DocumentException, ParseException {
        int ro = 1;
        try {
//            Resource resource = fileStorageService.loadFileAsResource("MDRInvoice.xlsx");
//            Workbook workbook = new XSSFWorkbook(new FileInputStream(resource.getFile().getAbsolutePath()));
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();

            int count = 0;
            int counter = 0;
            File f = new File("uploadDir/MDRInvoice.xlsx");
            FileInputStream fis = new FileInputStream(f);
            Workbook wb = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096).open(fis);
            firstrow = true;
            List<Merchant_details> list = new ArrayList<Merchant_details>();
            for (Sheet sheet : wb) {
                for (Row r : sheet) {
                    if (firstrow) {
                        firstrow = false;
                        continue;
                    }
                    DataFormatter dataFormatter = new DataFormatter();

                    Merchant_details md = new Merchant_details();
                    System.out.println("0");
                    md.setTid((r.getCell(0).getStringCellValue().isEmpty() || r.getCell(0).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(0).getStringCellValue()));
                    System.out.println("00");
                    md.setMid((r.getCell(1).getStringCellValue().isEmpty() || r.getCell(1).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(1).getStringCellValue()));
                    System.out.println("1");
                    md.setCreateDate((r.getCell(2).equals(null) || r.getCell(2).getStringCellValue().isEmpty()) ?
                            new Date() : r.getCell(2).getDateCellValue());
                    System.out.println("2");
                    md.setName((r.getCell(3).getStringCellValue().isEmpty() || r.getCell(3).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(3).getStringCellValue()));
                    System.out.println("3");
                    md.setContactNumber((r.getCell(4).getStringCellValue().isEmpty() || r.getCell(4).equals(null)) ? "NA"
                            : r.getCell(4).getStringCellValue());
                    System.out.println("4");
                    md.setFirstAddress(dataFormatter.formatCellValue(r.getCell(5)));
                    System.out.println("5");
                    md.setSecondAddress(dataFormatter.formatCellValue(r.getCell(6)));
                    System.out.println("6");
                    md.setCity((r.getCell(7).getStringCellValue().isEmpty() || r.getCell(7).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(7).getStringCellValue()));
                    System.out.println("7");
                    md.setMerchantTaluk((r.getCell(8).getStringCellValue().isEmpty() || r.getCell(8).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(8).getStringCellValue()));
                    System.out.println("8");
                    md.setMerchantRegion((r.getCell(9).getStringCellValue().isEmpty() || r.getCell(9).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(9).getStringCellValue()));
                    System.out.println("9");
                    md.setMerchantDist((r.getCell(10).getStringCellValue().isEmpty() || r.getCell(10).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(10).getStringCellValue()));
                    System.out.println("10");
                    md.setMerchantState((r.getCell(11).getStringCellValue().isEmpty() || r.getCell(11).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(11).getStringCellValue()));
                    System.out.println("11");
                    md.setPincode((r.getCell(12).getStringCellValue().isEmpty() || r.getCell(12).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(12).getStringCellValue()));
                    System.out.println("12");
                    md.setMerchant_GstrNumber((r.getCell(13).getStringCellValue().isEmpty() || r.getCell(13).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(13).getStringCellValue()));
                    System.out.println("13");
                    md.setStateCode((int) r.getCell(14).getNumericCellValue());
                    System.out.println("14");
                    md.setEmail((r.getCell(15).getStringCellValue().isEmpty() || r.getCell(15).equals(null)) ? "NA"
                            : String.valueOf(r.getCell(15).getStringCellValue()));
                    System.out.println("15");
                    list.add(md);
//              if ((counter + 1) % 500 == 0 || (counter + 1) == sheet.getLastRowNum()) {
//              	 merchantRepository.saveAll(list);
//      		        list.clear();
//      		      }
//      		      counter++;    
                }
                merchantRepository.saveAll(list);
            }
            wb.close();
            System.out.println(count);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Successfully inserted"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/finance/deletemerchant")
    public ResponseEntity<?> deleteMerchantType() {
        try {
            merchantRepository.deleteAllInBatch();
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", ""), HttpStatus.OK);

    }

    @GetMapping(value = "/finance/mdrdownload/{date}")
    public void ExcelDownloadMDR(@PathVariable("date") long date, HttpServletResponse response)
            throws IOException, DocumentException, ParseException, SQLException {
        String dateValue = DateUtil.longToDate(date, MMM_YY);
        Workbook workBook = null;
        try {
            conn = DriverManager.getConnection(URL_Auth, "custrepuser", "Custom@123$");
            stmt = conn.createStatement();
            String fileName = String.format("%s.xlsx", "mdr_report");
            response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/vnd.ms-excel");
            String sql = "select m.TID,m.Terminals_MID,m.Merchant_name,right(m.Merchant_address1,10),right(m.Merchant_address2,10)" +
                    ",m.Merchants_State,m.Gstr_no,d.[Txn <= 2k],d.[Txn > 2k],d.[Txn <= 2k -GST ],d.[Txn > 2k -GST]," +
                    "d.[Txn <= 2k -MDR ],d.[Txn > 2k -MDR],l.invoice_no from auto_merchants m inner Join" +
                    "(Select tid," +
                    "sum ( CASE  when txnamount <= 2000  THEN txnamount  else 0 end )  as Txn <= 2k ," +
                    "sum (  CASE  when txnamount > 2000 THEN txnamount  else 0 end )  as Txn > 2k ," +
                    "sum (  CASE  when txnamount <= 2000 THEN gst  else 0 end ) as Txn <= 2k -GST  ," +
                    "sum (  CASE  when txnamount > 2000 THEN gst  else 0 end ) as Txn > 2k -GST ," +
                    "sum (  CASE  when txnamount <= 2000 THEN mdr  else 0 end )  as Txn <= 2k -MDR  ," +
                    "sum (  CASE  when txnamount > 2000 THEN mdr  else 0 end ) as Txn > 2k -MDR  " +
                    "from [192.168.5.61].[Transaction_Base].[dbo].[Bijlipay_Mis_YTD$]  where month='" + dateValue + "' and MDR>0 group by tid) d on d.tid=m.TID " +
                    "inner join (Select tid,invoice_no from auto_invoice_logs where " +
                    "month='" + dateValue + "')l on l.tid=m.TID";
            resultset = stmt.executeQuery(sql);
            workBook = CreateExcelView.createExcelForSelectQuery(resultset);
            workBook.write(response.getOutputStream());
            workBook.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        } finally {
            if (resultset != null) {
                try {
                    resultset.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException sqlEx) {
                }
                resultset = null;
            }
        }
    }

    @Scheduled(cron = "0 0 0 1 6 ?")
    public void financialYearScheduler() {
        invoice_NumberRepos.updateInvoiceNumber(0);
    }

    @GetMapping(value = "/finance/federal/date/{date}")
    public ResponseEntity<?> pdfReportWithDatefederal(@PathVariable("date") long date) throws IOException,
            DocumentException {
        long dateValue = date;
        new Thread(new Runnable() {
            private final long dateValueThread;

            {
                this.dateValueThread = dateValue;
            }

            @Override
            public void run() {
                Invoice_Number invoice_Number = invoice_NumberRepos.findbyInvoiceId();
                count = invoice_Number.getInvoice_Number();
                List<Federalmerchant_details> detailsList = fdrlmrchtrepo.findAll();

                log.info("Merchant Details list {}", detailsList);
                String longToStringDate = DateUtil.longToDate(dateValueThread, MMM_YY);
//                NtlmPasswordAuthentication auth =
//                        new NtlmPasswordAuthentication(SMB_PDF_DOMAIN, SMB_PDF_USERNAME, SMB_PDF_PASSWORD);
                try {
                    File monthFolder = new File(PDF_RENTAL_FILE_PATH_NEW + DateUtil.longToDate(date, MMM_yy));
//                    SmbFile monthFolder = new SmbFile(PDF_RENTAL_FILE_PATH_NEW + DateUtil.longToDate(date, MMM_yy));
                    if (!monthFolder.exists()) {
                        log.info("file path exits so delete{}", monthFolder);
                        monthFolder.mkdir();
                    }
                    detailsList.forEach(d -> {
                        Merchant_details response = new Merchant_details();
                        PdfReport report = new PdfReport();
                        response.setName(d.getName());
                        response.setFirstAddress(d.getFirstAddress());
                        response.setSecondAddress(d.getSecondAddress());
                        response.setContactNumber(d.getContactNumber());
                        response.setMerchant_GstrNumber(d.getMerchant_GstrNumber());
                        response.setPincode(d.getPincode());
                        response.setTid(d.getTid());
                        response.setCategory(d.getCategory());
                        response.setStateCode(d.getStateCode());
                        response.setEmail(d.getEmail());
                        response.setDate(DateUtil.invoiceDate(dateValueThread));
                        response.setDateForInvoice(DateUtil.longToDate(dateValueThread, MMM));
                        response.setDateForMail(DateUtil.longToDate(dateValueThread, MMM_YY));
                        response.setDateinvoice(DateUtil.longToDate(dateValueThread, MMM__YY));
                        Double lessthan2kamount = fdrlmrchtrepo.findByTidAndMonthfederal(response.getTid(), longToStringDate);
                        Double lessThan2kForSumofMdr = fdrlmrchtrepo.findbyTidandMonthForMDRfederal(response.getTid(),
                                longToStringDate);
                        Double greaterThantotalAmount = fdrlmrchtrepo
                                .findByTidAndMonthForGreaterThan2kfederal(response.getTid(), longToStringDate);
                        Double greaterThan2kForSumofMdr = fdrlmrchtrepo
                                .findbyTidandMonthForMDRAndForGreaterThan2kfederal(response.getTid(), longToStringDate);
                        Double greaterThan2kForSumofgst = fdrlmrchtrepo
                                .findbyTidandMonthForGstAndForGreaterThan2kfederal(response.getTid(), longToStringDate);
                        if (lessthan2kamount == null) {
                            response.setTransaction_value(0.00);
                        } else {
                            response.setTransaction_value(lessthan2kamount);
                        }
                        if (lessThan2kForSumofMdr == null) {
                            response.setMdr(0.00);
                        } else {
                            response.setMdr(lessThan2kForSumofMdr);
                        }
                        if (greaterThantotalAmount == null) {
                            response.setGreatertransaction_value(0.00);
                        } else {
                            response.setGreatertransaction_value(greaterThantotalAmount);
                        }
                        if (greaterThan2kForSumofMdr == null) {
                            response.setGreatermdr(0.00);
                        } else {
                            response.setGreatermdr(greaterThan2kForSumofMdr);
                        }
                        if (greaterThan2kForSumofgst == null) {
                            response.setGreatergst(0.00);
                        } else {
                            response.setGreatergst(greaterThan2kForSumofgst);
                        }
                        System.out.println(d.getTid());
                        System.out.println(response.getTransaction_value());
//                    System.out.println(response.getGst().floatValue());
                        System.out.println(response.getMdr());
                        System.out.println("=============================================================================");
                        System.out.println(d.getTid());
                        System.out.println(response.getGreatertransaction_value());
                        System.out.println(response.getGreatergst().floatValue());
                        System.out.println(response.getGreatermdr());
                        Double value = response.getGreatermdr() + response.getMdr();
                        if (value > 0) {
                            System.out.println("inside pdf create");
                            String mdrInvoice = monthFolder.getAbsolutePath();
                            report.createPDF(date, response, ++count);
                            Invoice_Log log = Invoice_Log.to(d.getTid(), DateUtil.longToDate(dateValueThread, MMM_YY), count);
                            invoicerepo.save(log);
                            if (!(response.getEmail().equalsIgnoreCase("NA") || response.getEmail() == "NA")) {
                                try {
                                    //  mailHandler.PdfsendEmail(mdrInvoice, response, DateUtil.longToDate(dateValueThread,MMM_YY));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("Email not found_" + d.getTid());
                            }
                            count = count++;
                            System.out.println(count);
                        }
                    });
                    invoice_NumberRepos.updateInvoiceNumber(count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Successfully created"), HttpStatus.OK);
    }

}