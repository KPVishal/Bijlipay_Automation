package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.DTO.AxisDTO;
import com.bijlipay.Repository.AxisRepository;
import com.bijlipay.Service.AxisService;
import com.bijlipay.Service.TsdbService;
import com.bijlipay.Utils.CreateExcelView;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;
import com.jcraft.jsch.*;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.bijlipay.Utils.Constants.*;
import static com.bijlipay.Utils.Constants.BIJLIPAY_MIS_XLSX_AXIS;

@Service
public class AxisServiceImpl implements AxisService {
    private static final Logger log = LoggerFactory.getLogger(AxisServiceImpl.class);

    private JSch mJschSession = null;
    private Session mSSHSession = null;
    private ChannelSftp mChannelSftp = null;

    @Autowired
    private AxisService misService;

    @Autowired
    private TsdbService tsdbService;
    @Autowired
    private AxisRepository axisRepository;
    @Autowired
    private MailHandler mailHandler;

    @Value("${worldline.sftp.host}")
    private String worldlineSftpHost;
    @Value("${worldline.sftp.username}")
    private String worldlineSftpUsername;
    @Value("${worldline.sftp.password}")
    private String worldlineSftpPassword;

    private static boolean exists(ChannelSftp channelSftp, String path) {
        Vector res = null;
        try {
            res = channelSftp.ls(path);
        } catch (SftpException e) {
            log.error("Unexpected exception during ls files on sftp: [{}:{}]", e.id, e.getMessage());
        }
        return res != null && !res.isEmpty();
    }
//
//    @GetMapping(value = "/finance/mdr")
//    public ResponseEntity<?> pdfReportsWithMDR(HttpServletResponse response)
//            throws IOException, DocumentException, ParseException {
//        int ro = 1;
//        try {
////            Resource resource = fileStorageService.loadFileAsResource("MDRInvoice.xlsx");
////            Workbook workbook = new XSSFWorkbook(new FileInputStream(resource.getFile().getAbsolutePath()));
////            Sheet sheet = workbook.getSheetAt(0);
////            Iterator<Row> rowIterator = sheet.iterator();
//
//            int count=0;
//            int counter=0;
//            File f = new File("uploadDir/MDRInvoice.xlsx");
//            FileInputStream fis=new FileInputStream(f);
//            Workbook wb = StreamingReader.builder()
//                    .rowCacheSize(100)
//                    .bufferSize(4096).open(fis);
//            firstrow = true;
//            List<Merchant_details> list = new ArrayList<Merchant_details>();
//            for (Sheet sheet : wb) {
//                for(Row r : sheet) {
//                    if (firstrow) {
//                        firstrow = false;
//                        continue;
//                    }
//                    DataFormatter dataFormatter = new DataFormatter();
//
//                    Merchant_details md = new Merchant_details();
//                    System.out.println("0");
//                    md.setTid((r.getCell(0).getStringCellValue().isEmpty() || r.getCell(0).equals(null)) ? "NA"
//                            : String.valueOf(r.getCell(0).getStringCellValue()));

    @Override
    public boolean createUpdatedExcel(String bijlipayMisXlsx, String date) throws IOException, InvalidObjectException {
        boolean FIRSTROW = true;
        boolean updated = false;
        int count1 = 0;
        FileInputStream file = null;
        Workbook wb = null;
        List<AxisDTO> misDTOS = new ArrayList<>();
        try {
            file = new FileInputStream(bijlipayMisXlsx);
            log.info("Input stream file : ------------------ : {}", file.available());

            try {
                wb = StreamingReader.builder()
                        .rowCacheSize(100)
                        .bufferSize(4096).open(file);
//                wb = WorkbookFactory.create(file);
            } catch (Exception e) {
                e.printStackTrace();
                System.gc();
            }
            log.info("After opening file :------------- :{}", file);
            for (Row row : wb.getSheetAt(0)) {
                if (FIRSTROW) {
                    FIRSTROW = false;
                    continue;
                }
               // AxisDTO misTable = new AxisDTO();
                int cellValue = 0;
                int count = 0;
              /*  int cellValue = 0;
//                log.info("mid val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setMid((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("tid val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setTid((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("merchant name val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setMerchantName((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("mid card no {}",row.getCell(cellValue).getStringCellValue());
                misTable.setCardNo((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("mid transaction type {}",row.getCell(cellValue).getStringCellValue());
                misTable.setTransactionType((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? "" : row.getCell(cellValue++).getStringCellValue());
//                log.info("mid txn amnt {}",row.getCell(cellValue).getStringCellValue());
                misTable.setTxnamount(Float.parseFloat((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? "" : row.getCell(cellValue++).getStringCellValue()));
//                try {
//                    misTable.setTxnamount((float) row.getCell(cellValue++).getNumericCellValue());
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//                log.info("txn date val {}",row.getCell(cellValue).getStringCellValue());


                misTable.setTxnDate(DateUtil.convertStringToDate(row.getCell(cellValue++).getStringCellValue(), "ddMMyyyy"));
//                log.info("txn time val {}",row.getCell(cellValue).getStringCellValue());
//                misTable.setTxnTime(DateUtil.convertStringToTime(row.getCell(cellValue++).getStringCellValue()));
                misTable.setTxnTime((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());

//                misTable.setTxnTime(DateUtil.convertStringToTime(row.getCell(cellValue++).getStringCellValue(),"HHMMSS"));
//                log.info("rrn val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setrRN((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("approval code val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setApprovalCode((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("interchange type val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setInterchangeType((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("pos entry mode val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setPosEntryMode((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("credit debit flag val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setCreditDebitFlag((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("stan val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setStan((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("transaction currency code val {}",row.getCell(cellValue).getNumericCellValue());
//
                try {
                    misTable.setTransactionCurrencyCode((float) row.getCell(cellValue++).getNumericCellValue());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
//                log.info("card type val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setCardType((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                log.info("card type desc val {}",row.getCell(cellValue).getStringCellValue());
                misTable.setCardTypeDesc((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setDate(date);
//                log.info("adding to axis report table ---------",misTable);
                misDTOS.add(misTable);
//                log.info("added to axis report table ---------",misTable);
                count++;

               */
                AxisDTO misTable = new AxisDTO();
                misTable.setMid(getCellType(row.getCell(0)));
                misTable.setTid(getCellType(row.getCell(1)));
                misTable.setMerchantName(getCellType(row.getCell(2)));
                misTable.setCardNo(getCellType(row.getCell(3)));
                misTable.setTransactionType(getCellType(row.getCell(4)));
                misTable.setTxnamount(Float.parseFloat(getCellType(row.getCell(5))));
                String stdate=getCellType(row.getCell(6));
                misTable.setTxnDate( DateUtil.convertStringToDate(stdate,"ddMMyyyy"));
                misTable.setTxnTime(getCellType(row.getCell(7)));
                misTable.setrRN(getCellType(row.getCell(8)));
                misTable.setApprovalCode(getCellType(row.getCell(9)));
                misTable.setInterchangeType(getCellType(row.getCell(10)));
                misTable.setPosEntryMode(getCellType(row.getCell(11)));
                misTable.setCreditDebitFlag(getCellType(row.getCell(12)));
                misTable.setStan(getCellType(row.getCell(13)));
                misTable.setTransactionCurrencyCode(Float.parseFloat(getCellType(row.getCell(14))));
                misTable.setCardType(getCellType(row.getCell(15)));
                misTable.setCardTypeDesc(getCellType(row.getCell(16)));
                misTable.setDate(date);
                misDTOS.add(misTable);

            }
            wb.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("IOException reading Mars Excel File " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error reading Mars Excel File " + e.getMessage());
            System.gc();
        }
        try {  // TODO Create federal file directly using smb
            CreateExcelView.createAxisReportForMerChant(misDTOS, date);
        } catch (Exception e) {
            log.error("Error creating Axis report file: {}", e.getMessage());
            e.printStackTrace();
        }

        // Database operations separated from file I/O to avoid holding connections during slow file copy
        try {
            boolean twoDayBefore = misService.findByCreatedAt(date);
            if (!twoDayBefore) {
                log.info("data inserted for the choosed date {}", date);
                axisRepository.saveAll(misDTOS);
//                log.info("data insert started successfully");
//                tsdbService.insertAxis(misDTOS);
              //  log.info("Axis count :------------- :{}", count1);
                log.info("data insert finished successfully");
            } else {
                log.info("already data inserted for the choosed date {}", date);
            }
            updated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updated;
    }

    private String getCellType(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                return String.valueOf(sdf.format(cell.getDateCellValue()));
            } else {
                return String.valueOf(cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        }
        return "";
    }

    @Override
    @Transactional(readOnly = true)
    public boolean findByCreatedAt(String oneDayBefore) {
        boolean result = axisRepository.countBydate(oneDayBefore) > 0;
        return result;
    }

    private ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
//        jsch.setKnownHosts("C:\\Users\\vinothkumar\\.ssh\\known_hosts");
        Session jschSession = jsch.getSession(worldlineSftpUsername, worldlineSftpHost);
        jschSession.setPassword(worldlineSftpPassword);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    @Override
    public boolean connectToSFTPDownloadBeforeDates(long time) {
        boolean connectionStatus = false;
        boolean status = false;
        String basePath = "/users/MRL-RECON/Bijlipay/";
        final String REMOTEFILEPATH =
                basePath + DateUtil.longToDate(time, MMM) + " " + DateUtil.longToDate(time, yyyy) +
                        "/" + DateUtil.longToDate(time, MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.longToDate(time,
                        ddMM) + ".zip";
//        final String REMOTEFILEPATH =
//                basePath + DateUtil.longToDate(time, MMM) + " " + DateUtil.longToDate(time, yyyy) +
//                        "/" + DateUtil.longToDate(time, MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.longToDate(time,
//                        ddMM) + ".xlsx";
        try {
            this.mJschSession = new JSch();

            Properties config = new Properties();

            config.put("StrictHostKeyChecking", "no");

            JSch.setConfig(config);

            this.mSSHSession = mJschSession.getSession(worldlineSftpUsername, worldlineSftpHost, SFTP_PORT);

            this.mSSHSession.setPassword(worldlineSftpPassword);

            this.mSSHSession.setConfig("PreferredAuthentications",
                    "publickey,keyboard-interactive,password");

            this.mSSHSession.connect();

            this.mChannelSftp = (ChannelSftp) this.mSSHSession.openChannel("sftp");

            this.mChannelSftp.connect();

            if (this.mChannelSftp != null) {
                connectionStatus = true;
            }
            if (connectionStatus) {
                if (exists(mChannelSftp, REMOTEFILEPATH)) {
                    File deleteIfExists = new File(LOCALFILEPATH_AXIS);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    final String givenDate = DateUtil.longToDate(time, ddMM);

                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_AXIS +
                            BIJLIPAY_MIS_XLSX_AXIS + givenDate + ".zip");

//                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_AXIS +
//                            BIJLIPAY_MIS_XLSX_AXIS + givenDate + ".xlsx");
                    if (downloadStatus) {
                        status = true;
                    }
                } else {
                    return status;
                }
            } else {
                log.info("SFTP Connection Faild!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.mChannelSftp.disconnect();
            } catch (Exception e) {
                System.out.print(e);
            }
            try {
                this.mSSHSession.disconnect();
                status = true;
            } catch (Exception e) {
                System.out.print(e);
            }
        }
        return status;
    }

    //
//    @Override
//    public boolean connectToSFTPDownloadBeforeDates(long time) throws JSchException {
//        boolean connectionStatus = false;
//        boolean status = false;
//        String basePath = "/users/MRL-RECON/Bijlipay/";
//        final String REMOTEFILEPATH =
//                basePath + DateUtil.longToDate(time, MMM) + " " + DateUtil.longToDate(time, yyyy) +
//                        "/" + DateUtil.longToDate(time, MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.longToDate(time,
//                        ddMM) + ".zip";
//        ChannelSftp channelSftp = setupJsch();
//
//        try {
//            channelSftp.connect();
//
//            if (channelSftp != null) {
//                connectionStatus = true;
//            }
//            if (connectionStatus) {
//                if (exists(channelSftp, REMOTEFILEPATH)) {
//                    File deleteIfExists = new File(LOCALFILEPATH_AXIS);
//                    if (deleteIfExists.exists()) {
//                        FileUtils.deleteDirectory(deleteIfExists);
//                    }
//                    if (!deleteIfExists.exists()) {
//                        deleteIfExists.mkdir();
//                    }
//                    final String givenDate = DateUtil.longToDate(time, ddMMyyyy);
//                    boolean downloadStatus = downloadFile(channelSftp, REMOTEFILEPATH, LOCALFILEPATH_AXIS +
//                            BIJLIPAY_MIS_XLSX_AXIS + givenDate + ".zip");
//                    if (downloadStatus) {
//                        status = true;
//                    }
//                } else {
//                    return status;
//                }
//            } else {
//                log.info("SFTP Connection Faild!");
//            }
//            channelSftp.exit();
//        } catch (Exception e) {
//            channelSftp.exit();
//            e.printStackTrace();
//        }
//        return status;
//    }
//
    @Override
    public boolean connectToSFTPDownloadWithSpecificDate(Optional<Long> date) {
        boolean connectionStatus = false;
        boolean status = false;
        String REMOTEFILEPATH = null;
//        String basePath = "/users/MRL-RECON/Bijlipay/";
        String basePath = "/Bijlipay/";
        if (date.isPresent()) {
            REMOTEFILEPATH = basePath + DateUtil.longToDate(date.get(), MMM) + DateUtil.longToDate(date.get(), yyyy) +
                    "/" + DateUtil.longToDate(date.get(), MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.longToDate(date.get(), ddMM) + ".zip";
//        if (date.isPresent()) {
//            REMOTEFILEPATH = basePath + DateUtil.longToDate(date.get(), MMM) + " " + DateUtil.longToDate(date.get(), yyyy) +
//                    "/" + DateUtil.longToDate(date.get(), MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.longToDate(date.get(), ddMM) + ".xlsx";
        } else {
            REMOTEFILEPATH = basePath + DateUtil.currentDate(MMM)+ DateUtil.currentDate(yyyy) +
                    "/" + DateUtil.currentDate(MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.currentDate(ddMM) + ".zip";
//            REMOTEFILEPATH = basePath + DateUtil.currentDate(MMM) + " " + DateUtil.currentDate(yyyy) +
//                    "/" + DateUtil.currentDate(MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_AXIS + DateUtil.currentDate(ddMM) + ".xlsx";
        }
        log.info("Worldline Axis Scheduler File Path --{}",REMOTEFILEPATH);
        try {
            this.mJschSession = new JSch();

            Properties config = new Properties();

            config.put("StrictHostKeyChecking", "no");

            JSch.setConfig(config);

            this.mSSHSession = mJschSession.getSession(worldlineSftpUsername, worldlineSftpHost, SFTP_PORT);

            this.mSSHSession.setPassword(worldlineSftpPassword);

            this.mSSHSession.setConfig("PreferredAuthentications",
                    "publickey,keyboard-interactive,password");

            this.mSSHSession.connect();

            this.mChannelSftp = (ChannelSftp) this.mSSHSession.openChannel("sftp");

            this.mChannelSftp.connect();

            if (this.mChannelSftp != null) {
                connectionStatus = true;
            }
            if (connectionStatus) {
                if (exists(mChannelSftp, REMOTEFILEPATH)) {
                    File deleteIfExists = new File(LOCALFILEPATH_AXIS);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    final String givenDate = date.isPresent() ? DateUtil.longToDate(date.get(), ddMM) : DateUtil.currentDate(ddMM);
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_AXIS +
                            BIJLIPAY_MIS_XLSX_AXIS + givenDate + ".zip");
//                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_AXIS +
//                            BIJLIPAY_MIS_XLSX_AXIS + givenDate + ".xlsx");
                    if (downloadStatus) {
                        status = true;
                    }
                } else {
                    mailHandler.sendSFTPFileNotFoundMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), "Axis");
                    log.info("SFTP File Not Found!");
                    return status;
                }
            } else {
                mailHandler.sendSFTPFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), "Axis");
                log.info("SFTP Connection Faild!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.gc();
        } finally {
            try {
                this.mChannelSftp.disconnect();
            } catch (Exception e) {
                System.out.print(e);
            }
            try {
                this.mSSHSession.disconnect();
                status = true;
            } catch (Exception e) {
                System.out.print(e);
            }
        }
//        System.gc();
        return status;
    }

    public boolean downloadFile(String strSftpFile, String strLocalFile) {
        log.info("Remote File Path --{} and Local File Path --{}",strSftpFile,strSftpFile);
        boolean downloadStatus = false;
        try {
            this.mChannelSftp.get(strSftpFile, strLocalFile);
            //   log.info("Downloading....");
            downloadStatus = true;
            //   log.info("Success....");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadStatus;
    }

    public boolean downloadFile(ChannelSftp channelSftp, String strSftpFile, String strLocalFile) {
        boolean downloadStatus = false;
        try {
            channelSftp.get(strSftpFile, strLocalFile);
            //   log.info("Downloading....");
            downloadStatus = true;
            //   log.info("Success....");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadStatus;
    }

}
