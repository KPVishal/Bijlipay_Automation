package com.bijlipay.Controller;

import com.bijlipay.Service.ReportService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;


    @GetMapping(value = "invoice-mail-sent")
    public ResponseEntity<?> mailToCustomer(){
        reportService.invoiceMailToCustomer();
           return new ResponseEntity(new ApiResponse(HttpStatus.OK, "Success"),
                HttpStatus.OK);
    }

    @GetMapping(value = "download-report")
    public void reports(@RequestParam(value = "type",required = true) long type,
                        @RequestParam(value = "rname",required = true) Optional<String> reportName,
                        HttpServletResponse response) throws IOException {
        String fileName = String.format("Monthly Report - %s.xlsx", reportName+"_"+DateUtil.currentDate("yyyy-MM-dd"));
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        Workbook workBook = null;
        if(reportName.isPresent() && type == 1){
            workBook =  reportService.downloadReport(reportName);
        }else if(reportName.isPresent() && (type == 2 || type == 3)){
            workBook =  reportService.downloadReportTypeTwo(reportName);
        }
        workBook.write(response.getOutputStream());
        workBook.close();

    }
 }
