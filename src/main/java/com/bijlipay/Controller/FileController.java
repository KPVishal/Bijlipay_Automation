package com.bijlipay.Controller;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.bijlipay.Repository.LoanReceiptResponseRespo;
import com.bijlipay.Repository.MisRespository;
import com.bijlipay.SchedulerController.MisScheduler;
import com.bijlipay.Service.ClixService;
import com.bijlipay.Service.FileStorageService;
import com.bijlipay.Service.FintreeService;
import com.bijlipay.Service.MisService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.ReportSplitter;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/file")
public class FileController extends MisScheduler {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private MisService misService;
    @Autowired
    private FintreeService finservice;
    @Autowired
    private ClixService clixservice;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private LoanReceiptResponseRespo loanReceiptResponseRespo;
    @Autowired
    private MisRespository misRespository;

    @Value("${amazon.endpoint.url}")
    private String endpointUrl;
    @Value("${amazon.bucket.name}")
    private String bucketName;
    @Value("${amazon.access.key}")
    private String accessKey;
    @Value("${amazon.secret.key}")
    private String secretKey;

    @Value("${amazon.s3.file.path}")
    private String filePath;


    @PostMapping("/pendingFile")
    public ResponseEntity<?> pendingFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        String fileName = fileStorageService.storeFileReturnFileName(file);
        String fileUri = fileName;
        HashMap<String, String> result = new HashMap<>();
        result.put("fileUri", fileUri);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result), HttpStatus.OK);
    }


    @PostMapping("/fileupload")
    public ResponseEntity<?> fileupload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
//        String fileName = fileStorageService.storeFileReturnFileName(file);
        String fileName = fileStorageService.fileUpload(file);
        String fileUri = fileName;
        HashMap<String, String> result = new HashMap<>();
        result.put("fileUri", fileUri);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result), HttpStatus.OK);
    }
    @PostMapping("/uploadFile")
    public ResponseEntity<?> misuploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
//        String fileName = fileStorageService.storeFileReturnFileName(file);
        String fileName = fileStorageService.storeFile(file);
        String fileUri = fileName;
        HashMap<String, String> result = new HashMap<>();
        result.put("fileUri", fileUri);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result), HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        String mimeType = null;
        try {
            System.out.println(resource.getFile().getAbsolutePath());
            //String mimeType= URLConnection.guessContentTypeFromName(resource.getFile().getAbsolutePath());
            InputStream is = new BufferedInputStream(new FileInputStream(resource.getFile()));
            mimeType = URLConnection.guessContentTypeFromStream(is);
            System.out.println(mimeType);
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            System.out.println(contentType);
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not bezz determined
        contentType = (contentType != null ? contentType : (mimeType != null ? mimeType : "application/pdf")); //application/octet-stream

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/mis/uploadMultipleFiles")
    public ResponseEntity<?> misuploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        HashMap<String, List<String>> result = new HashMap<>();
        List<String> strings = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
            }
            String fileName = fileStorageService.storeFileReturnFileName(file);
            strings.add(fileName);
        }
        result.put("FileName ", strings);
        log.info(" FileName List {} " + strings);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", result),
                HttpStatus.OK);
    }

    @PostMapping("/clix/getname/uploadFile")
    public ResponseEntity<?> uploadFileForClix(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }

        String fileName = fileStorageService.clixStoreFileReturnFileName(file);

        if (fileName.equals("Clixfile.xlsx")) {
            String fileUri = fileName;
            HashMap<String, String> result = new HashMap<>();
            result.put("fileUri", fileUri);

            try {
                Thread.sleep(10000);
                System.out.println("inside insert");
                clixservice.insertRecords();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "please review the uploaded format fields"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "successfully uploaded"), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Please Upload correct file format"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/finance/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        String fileName = fileStorageService.storeFileReturnFileNameForPDF(file);
        if (fileName.equals("NewInvoice.xlsx")) {
            String fileUri = fileName;
            HashMap<String, String> result = new HashMap<>();
            result.put("fileUri", fileUri);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "successfully uploaded", result), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Please Upload correct file format"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/finance/uploadmdrFile")
    public ResponseEntity<?> uploadmdrFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        String fileName = fileStorageService.storemdrFileReturnFileName(file);
        if (fileName.equals("MDRInvoice.xlsx")) {
            String fileUri = fileName;
            HashMap<String, String> result = new HashMap<>();
            result.put("fileUri", fileUri);
//            mdrinsert.insertRecords(fileName);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "successfully uploaded", result), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Please Upload correct file format"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/finance/uploadmdrfederalFile")
    public ResponseEntity<?> uploadmdrFilefederal(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        String fileName = fileStorageService.storemdrFileReturnFileNamefederal(file);
        if (fileName.equals("MDRfederalInvoice.xlsx")) {
            String fileUri = fileName;
            HashMap<String, String> result = new HashMap<>();
            result.put("fileUri", fileUri);
//            mdrinsert.insertRecords(fileName);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "successfully uploaded", result), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Please Upload correct file format"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/clix/Fintree/uploadFile")
    public ResponseEntity<?> uploadFileForFintree(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }

        String fileName = fileStorageService.fintreeStoreFileReturnFileName(file);

        if (fileName.equals("Fintreefile.xlsx")) {
            String fileUri = fileName;
            HashMap<String, String> result = new HashMap<>();
            result.put("fileUri", fileUri);

            try {
                Thread.sleep(10000);
                System.out.println("inside insert");
                finservice.insertRecords();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "please review the uploaded format fields"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "successfully uploaded"), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Please Upload correct file format"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/clixAcc/uploadFile")
    public ResponseEntity<?> uploadFileForClixAcc(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }

        String fileName = fileStorageService.clixAccStoreFileReturnFileName(file);

        if (fileName.equals("ClixfileAcc.xlsx")) {
            String fileUri = fileName;
            HashMap<String, String> result = new HashMap<>();
            result.put("fileUri", fileUri);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "successfully uploaded"), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Please Upload correct file format"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload-s3-file")
    public ResponseEntity<?> uploadS3File(@RequestParam("file") MultipartFile file) throws Exception {
        /* Create S3 Client Object */
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey,secretKey)))
                .build();

        log.info("AWS S3 File Access Key --{}---AND Secret Key --{}",accessKey,secretKey);
        /* Set Bucket Name */
        FileInputStream inputStream = null;

        try {
            String uploadDir = filePath;
            File fh = new File(filePath);
            if (!fh.exists()) {
                fh.mkdir();
            }
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            final String filepathNew = uploadDir + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + file.getOriginalFilename());
            Files.write(path, bytes);
            ReportSplitter splitter = new ReportSplitter();
            splitter.excelSplit(filepathNew, 10000);
            log.info("File Split successfully----");
            File existFile = new File(filepathNew);
            existFile.delete();
            log.info("File Deleted successfully----");
            log.info("Original File Path ---{}", filePath);
            /* Create File Object */
            File paths = new File(filePath);
            String[] files = paths.list();
            for (String fileName : files) {
                File file1 = new File(filePath+fileName);
                log.info("Inside loop file name --{}",file1.getName());
                /* Set Object Key */
                String objectKey = file1.getName();
                /* Send Put Object Request */
                PutObjectResult result = s3.putObject(bucketName, objectKey, file1);
                /* Create InputStream Object */
                inputStream = new FileInputStream(file1);

                /* Create and Set Object Metadata */
                ObjectMetadata metadata = new ObjectMetadata();
                Map<String, String> map = new HashMap<>();
                map.put("url", endpointUrl);

                metadata.setUserMetadata(map);

                /* Set Content Length */
                metadata.setContentLength(file1.length());

                /* Create PutObjectRequest Object */
                PutObjectRequest request = new PutObjectRequest(bucketName, objectKey, inputStream, metadata);

                /* Set StorageClass as Standard Infrequent Access */
                request.setStorageClass(StorageClass.StandardInfrequentAccess);

                /* Set Canned ACL as BucketOwnerFullControl */
                request.setCannedAcl(CannedAccessControlList.BucketOwnerFullControl);

                /* Send Put Object Request */
                result = s3.putObject(request);
                log.info("Upload Status --{}", result);
                log.info("File Uploaded successfully----");
            }
            FileUtils.cleanDirectory(paths);
        } catch (AmazonServiceException e) {

            System.out.println(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (s3 != null) {
                    s3.shutdown();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
    }

//    @GetMapping("test")
//    public void test() {
//        List<Object[]> mids = misRespository.getApac();
//        mids.stream().forEach( m-> {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//            headers.add("ChannelID","BIJLIPAY");
//            headers.add("RequestID",String.valueOf(System.currentTimeMillis()));
//            headers.add("RequestDate", DateUtil.currentDate(YYYYMMDDHHMMSS));
//            headers.add("Content-Type","application/json");
//            Request request = new Request();
//            request.setTxnCount("10");
//            request.setTxnTotalValue(m[0].toString());
//            Transaction transaction = new Transaction(m);
//            request.setTransactions(Arrays.asList(transaction));
//            HttpEntity entity = new HttpEntity(request, headers);
//            ResponseEntity<Response> exchange = null;
//            log.info("request sending {}",entity);
//            try {
//                exchange = new RestTemplate().exchange("http://ec2-52-14-179-54.us-east-2.compute.amazonaws.com:8280/rbl/1.1/collection",
//                        HttpMethod.POST, entity, Response.class);
//                LoanReceiptResponse[] loanReceipts = exchange.getBody().getLoanReceipt();
//                Arrays.stream(loanReceipts).forEach(l -> {
//                    log.info("response recevied{}",l);
//                    LoanReceipt loanReceipt = new LoanReceipt(l);
//                    loanReceiptResponseRespo.save(loanReceipt);
//                        });
//
//            } catch (final HttpClientErrorException httpClientErrorException) {
//                System.out.println("Client " + httpClientErrorException.getMessage());
//                ObjectMapper objectMapper = new ObjectMapper();
//                objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
//                try {
//                    String res = httpClientErrorException.getResponseBodyAsString();//{"status":"true","msg":"success"}
//                    System.out.println(res);
//                    com.bijlipay.Model.DTO.Error loginValidator = objectMapper.readValue(res, com.bijlipay.Model.DTO.Error.class);//replaced result.getResponseAsString() with res
//                    System.out.println(loginValidator);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } catch (HttpServerErrorException httpServerErrorException) {
//                System.out.println("Server " + httpServerErrorException.getMessage());
//                ObjectMapper objectMapper = new ObjectMapper();
//                objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
//
//                try {
//                    String res = httpServerErrorException.getResponseBodyAsString();//{"status":"true","msg":"success"}
//                    com.bijlipay.Model.DTO.Error loginValidator = objectMapper.readValue(res, com.bijlipay.Model.DTO.Error.class);//replaced result.getResponseAsString() with res
//                    System.out.println(loginValidator);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            });
//    }
}
