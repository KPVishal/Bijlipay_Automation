package com.bijlipay.Controller;

import com.bijlipay.Repository.PaymentTempRepository;
import com.bijlipay.Service.BankService;
import com.bijlipay.Service.FileStorageService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Optional;

import static com.bijlipay.Utils.Constants.PAYMENT_FILE;

@RestController
@RequestMapping("/api")
public class BankController {
    private static final Logger log = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private BankService bankService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PaymentTempRepository paymentTempRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/payment-file-upload/{nfileName}")
    public ResponseEntity<?> bankFileUpload(@RequestParam("file") MultipartFile file,
                                            @PathVariable(name="nfileName",required=true) String nfileName) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        String fileName = fileStorageService.storeBankFileName(file,nfileName);
        String fileUri = fileName; // constant file name internalFile.xlsx
        HashMap<String, String> result = new HashMap<>();
        result.put("fileUri", fileUri);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", result), HttpStatus.OK);
    }


    @GetMapping("/import-payment-file")
    public ResponseEntity<?> manipulatingData(){
        File file = new File(Constants.UPLOAD_DOCUMENT_DIR_MIS + PAYMENT_FILE);
        if (file.exists()) {
            log.info("File Name " + file.getAbsolutePath());
            bankService.deleteTemprecords();
            if(!bankService.importPaymentData(file)){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,"Please remove the null values in the excel and try again"),HttpStatus.BAD_REQUEST);
            }

            if(file.delete()){
                log.info("File was deleted ");
            }else{
                log.info("File was not deteled");
            }
        }else{
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,"File Not Available or Upload File First"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Success"),HttpStatus.OK);
    }
//
//    @GetMapping("file-upload-notification/{nfileName}/{status}")
//    public  ResponseEntity<?> FileUploadNoti1fication(@PathVariable(name="nfileName",required=true) String nfileName,@PathVariable(name = "status",required = true)String status) {
//        if (nfileName.isEmpty() && status.isEmpty()) {
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "File Name and status is Missing "), HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,nfileName,"Uploaded Successfully"+status), HttpStatus.OK);
//
//    }

    @GetMapping("file-upload-notification/{nfileName}/{status}")
    public  ResponseEntity<?> FileUploadNotification(@PathVariable(name="nfileName",required=true) String nfileName,@PathVariable(name = "status",required = true)String status) {

        File file = new File(nfileName,status);
//        File file = new File(Constants.UPLOAD_DOCUMENT_DIR_MIS + PAYMENT_FILE);
        if (file.exists() && status.equals("Success")) {
            log.info("File Name " + file.getAbsolutePath());
            bankService.deleteTemprecords();
            if(!bankService.importPaymentData(file)){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,"Please remove the null values in the excel and try again"),HttpStatus.BAD_REQUEST);
            }
            if(file.delete()){
                log.info("File was deleted ");
            }else{
                log.info("File was not deteled");
            }
        }else{
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,"File and Status are Not Available or Upload File First"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Success"),HttpStatus.OK);
    }

    @GetMapping(value = "updateOnboarding")
    public ResponseEntity<?> updateOnboardingData(){
        bankService.updateBank();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success"),HttpStatus.OK);
    }

    @GetMapping(value = "updateSettlementData")
    public ResponseEntity<?> updateBankMisData(){
        bankService.updateMisTable();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success"),HttpStatus.OK);
    }

    @GetMapping(value = "send-sms-to-banks")
    public ResponseEntity<?> sendSmstoBanks() throws URISyntaxException {
//        log.info("Update Bank Bijlipay_Mis table");
//        bankService.updateMisTable();
        log.info("DELETE SMS Temp table");
        paymentTempRepository.deleteBankTempTable();
        log.info("Insert Into SMS Temp Table");
        paymentTempRepository.insertBijlipayMisToBankTable();
        final String baseUrl = "http://localhost:6081/kbl";
        try {
            URI uri = new URI(baseUrl);
            ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        }catch(HttpClientErrorException ex) {
            log.info("Client Error {}",ex.getMessage());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Client Error"),HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (HttpServerErrorException e){
            log.info("Server Error {}",e.getMessage());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Server Error"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success"),HttpStatus.OK);
    }

    @GetMapping("update-payment-date")
    public ResponseEntity<?> updateCurrentDateFromChoosed(
            @RequestParam(value = "from",required = true)Optional<Long> fromDate,
            @RequestParam(value = "to",required = true)Optional<Long> toDate) throws URISyntaxException{
        bankService.updateDate(fromDate,toDate);
        return ResponseEntity.ok("Success");

    }

}
