package com.bijlipay.Controller;

import com.bijlipay.Service.FederalService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.bijlipay.Utils.Constants.*;


@RestController
@RequestMapping(value = "/api")
public class FederalManualController {
    private static final Logger log = LoggerFactory.getLogger(FederalManualController.class);

    @Autowired
    private FederalService federalService;
    @Autowired
    private MailHandler mailHandler;

    @GetMapping(value = {"/federal/operation-team-manual-process", "/federal/operation-team-manual-process/{date}"})
    public ResponseEntity operationTeamManualProcess(@PathVariable("date") Optional<Long> date) throws URISyntaxException {
        final String givenDate = date.isPresent() ? DateUtil.longToDate(date.get(), ddMMyyyy) : DateUtil.currentDate(ddMMyyyy);
        if (new File(LOCALFILEPATH_FEDERAL +
                BIJLIPAY_MIS_XLSX_FEDERAL + givenDate + ".xlsx").exists()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST,
                    "File Upload Already for the date_" + givenDate), HttpStatus.BAD_REQUEST);
        } else {
            boolean download = false;
            boolean fileCreated = false;
            try {
                download = date.isPresent() ? federalService.connectToSFTPDownloadWithSpecificDate(date) :
                        federalService.connectToSFTPDownloadWithSpecificDate(Optional.empty());
                if (download) {
                    fileCreated = federalService.createUpdatedExcel(LOCALFILEPATH_FEDERAL +
                            BIJLIPAY_MIS_XLSX_FEDERAL + givenDate + ".xlsx", date.isPresent() ?
                            DateUtil.longToDate(date.get(), yyyy_MM_dd) : DateUtil.currentDate(yyyy_MM_dd));
                    if (fileCreated) {
                        mailHandler.sendMailForFederal(date.isPresent() ? DateUtil.longToDate(date.get(), yyyy_MM_dd) : DateUtil.currentDate(yyyy_MM_dd));
                        return new ResponseEntity(new ApiResponse(HttpStatus.OK, "File Uploaded SuccessFully for the " +
                                "date " + givenDate), HttpStatus.OK);
                    } else {
                        return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST,
                                "File Not Created for the date" + givenDate),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "SFTP file not available now, please try after sometime "),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
