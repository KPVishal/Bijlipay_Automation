package com.bijlipay.Controller;

import com.bijlipay.Model.FederalDTO;
import com.bijlipay.Model.SibReport;
import com.bijlipay.Repository.FederalRepository;
import com.bijlipay.Repository.SIBRepository;
import com.bijlipay.Service.FileStorageService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.CreateExcelView;
import com.bijlipay.Utils.DateUtil;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.bijlipay.Utils.Constants.*;
@RequestMapping("/api")
@RestController
public class ManualFederalAndSibProcess {
    private static final Logger log = LoggerFactory.getLogger(ManualFederalAndSibProcess.class);
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FederalRepository federalRepository;
    @Autowired
    private SIBRepository sibRepository;

    @Autowired
    private CreateExcelView excelView;
    //    consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE
    @PostMapping(value = "/federal/manual",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> manualFederalProcess(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        String fileName = fileStorageService.storeFileReturnFileName(file);
        String fileUrl = fileName;
        log.info("file path" + fileUrl);
        HashMap<String, String> result = new HashMap<>();
        result.put("fileUri", fileUrl);
        log.info("file " + result);
        Resource resource = fileStorageService.loadFileAsResource(fileUrl);
        log.info("resource  " + resource.getFile());
        Workbook wb = null;
        boolean FIRSTROW = true;
        final long milliseconds = 1000L;
        Calendar innerCal = Calendar.getInstance();
        DateUtil.calToDate(innerCal.getTime(), ddMMyyyy);
        String federaldate = DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy);
        Row.MissingCellPolicy ROW_BLANK_AS_NULL = Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
//        Row.MissingCellPolicy ROW_NULL_AND_BLANK = Row.MissingCellPolicy.RETURN_NULL_AND_BLANK;
        List<FederalDTO> federalDTOS = new ArrayList<>();
        try {
            FileInputStream excelFile = new FileInputStream(resource.getFile());
            wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(excelFile);
            for (Row row : wb.getSheetAt(0)) {
                if (FIRSTROW) {
                    FIRSTROW = false;
                    continue;
                }
                FederalDTO misTable = new FederalDTO();
                int cellValue = 0;
                misTable.setTxnDate(DateUtil.convertStringToDate(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "" : row.getCell(cellValue++).getStringCellValue(), "dd-MM-yy"));
                misTable.setMid(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setTid(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setMerchantName(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setTradename(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCity(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardNo(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardType(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                try {
                    misTable.setrRN(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                misTable.setAssociationName(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                if (row.getCell(cellValue) == null) {
                    misTable.setApprovalCode("100000");
                    cellValue++;
                } else {
                    misTable.setApprovalCode(row.getCell(cellValue++).getStringCellValue());
                }
                misTable.setBatchCode(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setTxnamount(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? 0 : (float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setMrlmdr(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? 0 : (float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setGst(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? 0 : (float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setNetamount(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? 0 : (float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setPaymentdate(DateUtil.convertStringToDate(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "" : row.getCell(cellValue++).getStringCellValue(), "dd-MM-yy"));
                misTable.setAggregatorName(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardType1(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardProduct(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setAssocationFee(row.getCell(cellValue,ROW_BLANK_AS_NULL) == null ? 0 :(float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setNarration(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue).getStringCellValue().equalsIgnoreCase("SALE") ? "POS" : row.getCell(cellValue).getStringCellValue());
                misTable.setMonth(DateUtil.currentDate(MMM_yy));
                misTable.setDate(federaldate);
                federalDTOS.add(misTable);
            }
            wb.close();
            excelFile.close();

        } catch (IOException e) {
            System.out.println("IOException reading Mars Excel File " + e.getMessage());
//        } catch (Exception e) {
//            System.out.println("Error reading Mars Excel File " + e.getMessage());
        }

        try {  // TODO Create federal file client side path

            Workbook workbook = null;
            String filename = String.format("FED_TXN - %s.xlsx", federaldate);
            response.setContentType("application/vnd.ms-excel");
//            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
            workbook = excelView.createManualFederalReportForMerChant(federalDTOS);
            log.info("create workbook...{}",workbook);

            boolean already = findByCreatedAtFederal(federaldate);
            if (!already) {
                log.info("data inserted for the choosed date {}", federaldate);
                log.info("saved data  {}", federalDTOS);
                federalRepository.saveAll(federalDTOS);

            } else {
                log.info("already data inserted for the choosed date {}", federaldate);
            }

            workbook.write(response.getOutputStream());
            workbook.close();
            log.info("Excel  file Download Successfully...{}");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Success"),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"exception occured while generating excel"+e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public String handleHttpMediaTypeNotAcceptableException() {
        return "acceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE;
    }

    @PostMapping(value = "/sib/manual",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sibManualProcess(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        String fileName = fileStorageService.storeFileReturnFileName(file);
        String fileUrl = fileName;
        log.info("file path" + fileUrl);
        HashMap<String, String> result = new HashMap<>();
        result.put("fileUri", fileUrl);
        log.info("file " + result);
        Resource resource = fileStorageService.loadFileAsResource(fileUrl);
        log.info("resource  " + resource.getFile());
        boolean FIRSTROW = true;
        final long milliseconds = 1000L;
        Row.MissingCellPolicy ROW_BLANK_AS_NULL = Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
        Workbook wb = null;
        Calendar innerCal = Calendar.getInstance();
        DateUtil.calToDate(innerCal.getTime(), ddMMyyyy);
        String sibdate = DateUtil.longToDate(innerCal.getTimeInMillis() / milliseconds, ddMMyyyy);
        List<SibReport> misDTOS = new ArrayList<>();
        try {
            FileInputStream excelFile = new FileInputStream(resource.getFile());
            wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(excelFile);
            for (Row row : wb.getSheetAt(0)) {
                if (FIRSTROW) {
                    FIRSTROW = false;
                    continue;
                }
                SibReport misTable = new SibReport();
                int cellValue = 0;
                misTable.setTxnDate(DateUtil.convertStringToDate(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "" : row.getCell(cellValue++).getStringCellValue(), "dd-MM-yy"));
                misTable.setMid(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setTid(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setMerchantName(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setTradename(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCity(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardNo(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardType(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setrRN(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setAssociationName(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                if(row.getCell(cellValue)==null){
                    misTable.setApprovalCode("100000");
                    cellValue++;
                }else{
                    misTable.setApprovalCode(row.getCell(cellValue++).getStringCellValue());
                }
                misTable.setBatchCode(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setTxnamount(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? 0 : (float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setMrlmdr(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? 0 : (float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setGst(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? 0 : (float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setNetamount(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? 0 : (float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setPaymentdate(DateUtil.convertStringToDate(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "" : row.getCell(cellValue++).getStringCellValue(), "dd-MM-yy"));
                misTable.setAggregatorName(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardType1(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardProduct(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue++).getStringCellValue());
                misTable.setAssocationFee(row.getCell(cellValue,ROW_BLANK_AS_NULL) == null ? 0 :(float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setNarration(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue).getStringCellValue().equalsIgnoreCase("SALE") ? "POS" : row.getCell(cellValue).getStringCellValue());
                misTable.setMonth(DateUtil.currentDate(MMM_yy));
                misTable.setDate(sibdate);
                misDTOS.add(misTable);
            }
            wb.close();
            excelFile.close();
        } catch (IOException e) {
            System.out.println("IOException reading Mars Excel File " + e.getMessage());
//        } catch (Exception e) {
//            System.out.println("Error reading Mars Excel File " + e.getMessage());
        }

        try {  // TODO Create federal file client side path
            Workbook workbook = null;
            String filename = String.format("SIB_TXN - %s.xlsx", sibdate);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
            workbook = excelView.createManualSIBReportForMerChant(misDTOS);
            log.info("create workbook {}",workbook);

            boolean already = findByCreatedAtSib(sibdate);
            if (!already) {
                log.info("data inserted for the choosed date {}", sibdate);
                log.info("saved data  {}", misDTOS);
                sibRepository.saveAll(misDTOS);
            } else {
                log.info("already data inserted for the choosed date {}", sibdate);
            }
            workbook.write(response.getOutputStream());
            workbook.close();
            log.info("Excel File Download Successfully....{}");
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Success"),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"exception ccured while generating excel"+e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public boolean findByCreatedAtFederal(String oneDayBefore) {
        boolean result = sibRepository.countBydate(oneDayBefore) > 0;
        return result;
    }

    public boolean findByCreatedAtSib(String oneDayBefore) {
        boolean result = federalRepository.countBydate(oneDayBefore) > 0;
        return result;
    }

}


