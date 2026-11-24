package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.DTO.BijlipaySettlementDto;
import com.bijlipay.Model.DTO.MerchantDataDto;
import com.bijlipay.Model.DTO.MisObject;
import com.bijlipay.Model.DTO.SwitchTransactionObject;
import com.bijlipay.Model.DailySettlement;
import com.bijlipay.Model.MisTable;
import com.bijlipay.Repository.AxisRepository;
import com.bijlipay.Repository.DailySettlementRepository;
import com.bijlipay.Repository.MisRespository;
import com.bijlipay.Repository.UserRepository;
import com.bijlipay.Service.MisService;
import com.bijlipay.Service.TsdbService;
import com.bijlipay.Utils.CreateExcelView;
import com.bijlipay.Utils.DateUtil;
import com.bijlipay.Utils.MailHandler;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.bijlipay.Utils.Constants.*;

@Service
public class MisServiceImplementation implements MisService {

    private static final Logger log = LoggerFactory.getLogger(MisServiceImplementation.class);
    @Autowired
    public MisRespository misRespository;
    @Autowired
    public DailySettlementRepository dailySettlementRepository;
    @Autowired
    TsdbService tsdbService;
    private boolean firstrow = true;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MailHandler mailHandler;
    @Autowired
    private UserRepository userRepository;
    private JSch mJschSession = null;
    private Session mSSHSession = null;
    private ChannelSftp mChannelSftp = null;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private AxisRepository axisRepository;

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
            log.error("files on available in sftp: [{}:{}]", e.id, e.getMessage());
        }
        return res != null && !res.isEmpty();
    }

    @Override
    public void saveMisDataToTable(String bijlipayMisXlsx) {
        Workbook wb = null;
        FileInputStream file = null;
        int count =0;
        try {
            file = new FileInputStream(new File(bijlipayMisXlsx));
            wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file);
            for (Sheet sheet : wb) {
                log.info("Row Count {}", sheet.getLastRowNum());
                if (sheet.getLastRowNum() == 0) {
                    continue;
                }
                firstrow = true;
                for (Row row : sheet) {
                    if (firstrow) {
                        firstrow = false;
                        continue;
                    }

                    MisTable misTable = new MisTable();
                    misTable.setTxnDate(DateUtil.convertStringToDate(row.getCell(0).getStringCellValue(), MM_DD_YYYY_HH_MM_SS_AA));
                    misTable.setMid(row.getCell(1) == null ? "NA" : row.getCell(1).getStringCellValue());
                    misTable.setTid(row.getCell(2) == null ? "NA" : row.getCell(2).getStringCellValue());
                    misTable.setMerchantName(row.getCell(3) == null ? "NA" : row.getCell(3).getStringCellValue());
                    misTable.setCity(row.getCell(4) == null ? "NA" : row.getCell(4).getStringCellValue());
                    misTable.setCardNo(row.getCell(5) == null ? "NA" : row.getCell(5).getStringCellValue());
                    misTable.setCardType(row.getCell(6) == null ? "NA" : row.getCell(6).getStringCellValue());
                    misTable.setrRN(row.getCell(7) == null ? "NA" : row.getCell(7).getStringCellValue());
                    misTable.setAssociationName(row.getCell(8) == null ? "NA" : row.getCell(8).getStringCellValue());
                    misTable.setTxnamount(Float.parseFloat(row.getCell(11) == null ? "NA" : row.getCell(11).getStringCellValue()));
                    misTable.setMrlmdr((float) Double.parseDouble(row.getCell(12) == null ? "NA" : row.getCell(12).getStringCellValue()));
                    misTable.setGst(Float.parseFloat(row.getCell(13) == null ? "NA" : row.getCell(13).getStringCellValue()));
                    misTable.setNetamount(Float.parseFloat(row.getCell(14) == null ? "NA" : row.getCell(14).getStringCellValue()));
                    misTable.setPaymentdate(DateUtil.convertStringToDate(row.getCell(15) == null ? "NA" : row.getCell(15).getStringCellValue(), MM_DD_YYYY_HH_MM_SS_AA));
                    misTable.setUploadDate(DateUtil.yesterday());
                    String ifsc = misRespository.getIfsc(row.getCell(2) == null ? "NA" : row.getCell(2).getStringCellValue());
                    misTable.setIfsc(ifsc);
                    misRespository.save(misTable);
                    log.info("auto_bijlipay_mis_temp$ Start to save");


                }




                    /*MisTable misTable = misTable = new MisTable();
                    int cellValue = 0;
                    misTable.setTxnDate(DateUtil.convertStringToDate(row.getCell(cellValue).getStringCellValue(), MM_DD_YYYY_HH_MM_SS_AA));
                    cellValue++;
                    misTable.setMid(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setTid(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setMerchantName(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setCity(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setCardNo(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setCardType(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setrRN(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setAssociationName(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setApprovalCode(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setBatchCode(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setTxnamount(row.getCell(cellValue) == null ? (float) 0.00 : (float) row.getCell(cellValue).getNumericCellValue());
                    cellValue++;
                    misTable.setMrlmdr(row.getCell(cellValue) == null ? (float) 0.00 : (float) row.getCell(cellValue).getNumericCellValue());
                    cellValue++;
                    misTable.setGst(row.getCell(cellValue) == null ? (float) 0.00 : (float) row.getCell(cellValue).getNumericCellValue());
                    cellValue++;
                    misTable.setNetamount(row.getCell(cellValue) == null ? (float) 0.00 : (float) row.getCell(cellValue).getNumericCellValue());
                    cellValue++;
                    misTable.setPaymentdate(DateUtil.convertStringToDate(row.getCell(cellValue).getStringCellValue(), MM_DD_YYYY_HH_MM_SS_AA));
                    cellValue++;
                    misTable.setAggregatorName(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setTradename(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setTerminalSerialNo(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setCardType1(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setAssocationFee(row.getCell(cellValue) == null ? (float) 0.00 : (float) row.getCell(cellValue).getNumericCellValue());
                    cellValue++;
                    misTable.setNarration(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    cellValue++;
                    misTable.setCardProduct(row.getCell(cellValue) == null ? "NA" : row.getCell(cellValue).getStringCellValue());
                    map.add(misTable);
//                    log.info("Mis table row data : ------------ : ID : {}, RRN : {}, TID : {}, MID : {}, txn_date : {}, TxnAmount : {}", misTable.getId(),
//                            misTable.getrRN(), misTable.getTid(), misTable.getMid(), misTable.getTxnDate(), misTable.getTxnamount());
                }
                log.info("auto_bijlipay_mis_temp$ Start to save");
                misRespository.saveAll(map);
                count++;*/
            }
            wb.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error " + e);
        } finally {
            try {
                wb.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
    }

    private void updateAce_merchant() {
        int count = userRepository.updateace();
        log.info("Ace_merchant update count-->" + count);

    }

    private float parseFloat(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Float.parseFloat(strNumber);
            } catch (Exception e) {
                return -1;
            }
        } else return 0;
    }

    @Override
    public void saveDailySettlementDataToTable(String bijlipaySettlementXlsx) {
        Workbook wb = null;
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(FILEDIRECTORY_MIS + bijlipaySettlementXlsx));
            wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file);
            List<DailySettlement> map = new ArrayList<>();
            for (Sheet sheet : wb) {
                log.info("Row Count {}", sheet.getLastRowNum());
                if (sheet.getLastRowNum() == 0) {
                    continue;
                }
                firstrow = true;
                for (Row row : sheet) {
                    if (firstrow) {
                        firstrow = false;
                        continue;
                    }
                    DailySettlement misTable = new DailySettlement();
                    int cellValue = 0;
                    misTable.setDate(DateUtil.convertStringToDate(row.getCell(cellValue++).getStringCellValue(), MM_DD_YYYY_HH_MM_SS_AA));
                    misTable.setMid(row.getCell(cellValue++).getStringCellValue());
                    misTable.setTid(row.getCell(cellValue++).getStringCellValue());
                    misTable.setMerchantName(row.getCell(cellValue++).getStringCellValue());
                    misTable.setCity(row.getCell(cellValue++).getStringCellValue());
                    misTable.setAccountNumber(row.getCell(cellValue++).getStringCellValue());
                    misTable.setBranchCode(row.getCell(cellValue++).getStringCellValue());
                    misTable.setTxncount((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setTxnamount((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setMrlmdr((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setGst((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setNetamount((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setCardproduct(row.getCell(cellValue++).getStringCellValue());
                    misTable.setCard_Type(row.getCell(cellValue++).getStringCellValue());
                    misTable.setAssociationfee((float) row.getCell(cellValue++).getNumericCellValue());
                    map.add(misTable);
                }
                log.info("auto_daily_sett start to insert");
                dailySettlementRepository.saveAll(map);
            }
            wb.close();
            file.close();
            map.clear();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error " + e);
        } finally {
            try {
                wb.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }

    }

    @Override
    public Boolean connectToSFTPFileDownload() {
        boolean connectionStatus = false;
        boolean status = false;

        try {
            log.info("Starting SFTP connection...");
            this.mJschSession = new JSch();
            log.info("JSch object created.");

            java.util.Properties config = new java.util.Properties();

            config.put("StrictHostKeyChecking", "no");

            JSch.setConfig(config);

            log.info("JSch config set (host key checking disabled).");

            log.info("Getting session for host: {}, port: {}, user: {}", worldlineSftpHost, SFTP_PORT, worldlineSftpUsername);

            this.mSSHSession = mJschSession.getSession(worldlineSftpUsername, worldlineSftpHost, SFTP_PORT);

            this.mSSHSession.setPassword(worldlineSftpPassword);

            this.mSSHSession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            log.info("Connecting SSH session...");
            if (!this.mSSHSession.isConnected()) {
                this.mSSHSession.connect();
                log.info("New SFTP connection establish");
            }

            log.info("SSH session connected.");

            log.info("Opening SFTP channel...");


            this.mChannelSftp = (ChannelSftp) this.mSSHSession.openChannel("sftp");
            log.info("Connecting SFTP channel...");

            if (!this.mChannelSftp.isConnected()) {
                this.mChannelSftp.connect();
                log.info("New Channel connected with WorldLine");
            }

            log.info("SFTP channel connected.");
            if (this.mChannelSftp != null) {
                log.info("SFTP Channel is valid.");
                connectionStatus = true;
            }
            if (connectionStatus) {
                File deleteIfExists = new File(FILEDIRECTORY_MIS);
                log.info("Checking if local directory exists: {}", FILEDIRECTORY_MIS);
                if (deleteIfExists.exists()) {
                    log.info("Directory exists. Deleting...");
                    FileUtils.deleteDirectory(new File(FILEDIRECTORY_MIS));
                    log.info("Directory deleted.");

                }
                if (!deleteIfExists.exists()) {
                    log.info("Creating directory: {}", FILEDIRECTORY_MIS);
                    deleteIfExists.mkdir();

                }
                String REMOTEFILEPATH = BASEURL + DateUtil.currentDate(MMM) + DateUtil.currentDate(yyyy) + "/" + DateUtil.currentDate(MMddyyyy) + "/" + DateUtil.currentDate(ddMMyy) + ".zip";
                log.info("Worldline MIS Scheduler File Path --{}", REMOTEFILEPATH);
                final String dest = FILEDIRECTORY_MIS + DateUtil.currentDate(ddMMyy) + ".zip";
                boolean downloadStatus = downloadFile(REMOTEFILEPATH, dest);
                close();
                if (downloadStatus) {
                    boolean unZipStatus = unzipfile(dest, FILEDIRECTORY_MIS);
                    if (unZipStatus) {
                        status = true;
                    }
                }
            } else {
                log.info("SFTP Connection Faild!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error " + e);
        }
        return status;
    }

    public boolean connectToSFTPFileCorpDownload() {
        boolean connectionStatus = false;
        boolean status = false;
        log.info("COnnection initialized");

        try {
            this.mJschSession = new JSch();
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch.setConfig(config);
            log.info("Getting session for host: {}, port: {}, user: {}", worldlineSftpHost, SFTP_PORT, worldlineSftpUsername);
            this.mSSHSession = mJschSession.getSession(worldlineSftpUsername, worldlineSftpHost, SFTP_PORT);
            this.mSSHSession.setPassword(worldlineSftpPassword);
            this.mSSHSession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            log.info("Connecting SSH session...");
            this.mSSHSession.connect();
            this.mChannelSftp = (ChannelSftp) this.mSSHSession.openChannel("sftp");
            log.info("Connecting SFTP channel...");
            this.mChannelSftp.connect();
            log.info("SFTP channel connected.");
            if (this.mChannelSftp != null) {
                log.info("SFTP Channel is valid.");
                connectionStatus = true;
            }
            if (connectionStatus) {
                File deleteIfExists = new File(FILEDIRECTORY_MIS);
                log.info("Checking if local directory exists: {}", FILEDIRECTORY_MIS);
                if (deleteIfExists.exists()) {
                    log.info("Directory exists. Deleting...");
                    FileUtils.deleteDirectory(new File(FILEDIRECTORY_MIS));
                    log.info("Directory deleted.");
                }
                if (!deleteIfExists.exists()) {
                    log.info("Creating directory: {}", FILEDIRECTORY_MIS);
                    deleteIfExists.mkdir();
                }
                String REMOTEFILEPATH = BASEURL + DateUtil.currentDate(MMM) + DateUtil.currentDate(yyyy) + "/" + DateUtil.currentDate(MMddyyyy) + "/" + DateUtil.currentDate(ddMMyy) + ".zip";
                log.info("Worldline MIS Scheduler File Path --{}", REMOTEFILEPATH);
                final String dest = FILEDIRECTORY_MIS + DateUtil.currentDate(ddMMyy) + ".zip";
                boolean downloadStatus = downloadCorpFile(REMOTEFILEPATH, dest);
                close();
                if (downloadStatus) {
                    boolean unZipStatus = unzipfile(dest, FILEDIRECTORY_MIS);
                    if (unZipStatus) {
                        status = true;
                    }
                }
            } else {
                log.info("SFTP Corp file download. Connection lost!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error " + e);
        }
        return status;
    }

    public boolean downloadFile(String strSftpFile, String strLocalFile) {
        boolean downloadStatus = false;
        try {
            this.mChannelSftp.get(strSftpFile, strLocalFile);
            downloadStatus = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadStatus;
    }

    public boolean downloadCorpFile(String strSftpFile, String strLocalFile) {
        log.info("Remote File Path --{} and Local File Path --{}", strSftpFile, strSftpFile);
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

    public void close() {
        try {
            this.mChannelSftp.disconnect();
            log.info("Channel Disconnected....");
        } catch (Exception e) {
            log.info("Error Channel Disconnected... " + e);
        }
        try {
            this.mSSHSession.disconnect();
            log.info("Session Disconnected....");
        } catch (Exception e) {
            log.info("Error Session Disconnected... " + e);
        }
    }

    public Boolean unzipfile(String zipFilePath, String destDir) {
        boolean unZipStatus = false;
        byte[] buffer = new byte[BUFFER_SIZE];
        ZipFile file = null;
        try {
            file = new ZipFile(zipFilePath);
            FileSystem fileSystem = FileSystems.getDefault();
            Enumeration<? extends ZipEntry> entries = file.entries();
            String uncompressedDirectory = destDir;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    Files.createDirectories(fileSystem.getPath(uncompressedDirectory + entry.getName()));
                } else {
                    InputStream is = file.getInputStream(entry);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    String uncompressedFileName = uncompressedDirectory + entry.getName();
                    Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
                    if (new File(uncompressedFileName).exists()) {
                        new File(uncompressedFileName).delete();
                    }
                    Files.createFile(uncompressedFilePath);
                    FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
                    int len;
                    while ((len = bis.read(buffer)) > BINARY_FILE_TYPE) {
                        fileOutput.write(buffer, BINARY_FILE_TYPE, len);
                    }
                    fileOutput.close();
                }
            }
            file.close();
            unZipStatus = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
        return unZipStatus;
    }

    @Override
    public void sentMailForCase11() throws Exception {
        firstrow = true;
        FileInputStream file = null;
        Workbook wb = null;
        List<MerchantDataDto> merchantDataDtosMap = null;
        try {
            file = new FileInputStream(new File(MERCHANT_FILE_DEATILS_SwitchReport));
            wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file);
            merchantDataDtosMap = new ArrayList<MerchantDataDto>();

            for (Row row : wb.getSheetAt(0)) {
                if (firstrow) {
                    firstrow = false;
                    continue;
                }
                try {
                    MerchantDataDto merchantDataDto = new MerchantDataDto();
                    int cellValue = 0;
                    merchantDataDto.setMerchantName(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDto.setFormat((int) row.getCell(cellValue++).getNumericCellValue());
                    merchantDataDto.setMailId(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDto.setMailIdBcc(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDto.setMid(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDto.setVerifiedMaildId(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDtosMap.add(merchantDataDto);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in merchant bijlipay SwitchReport file" + e.getMessage());
                }
            }
            wb.close();
            file.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.info("Error While Reading Merchant Excel File " + e);
        } finally {
            wb.close();
            file.close();
        }
        // TODO MerchantNameList excel created for who received mail.
        Workbook workbook = null;
        FileOutputStream fileOut = null;
        fileOut = new FileOutputStream(FILEDIRECTORY_MIS + "Succesful_mail_sended_MerchantId" + DateUtil.currentDate(yyyy_MM_dd) + ".xlsx");

        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("MerchantNameList");
        sheet.setDefaultColumnWidth(15);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Merchant Id");
        int rowCount = 1;
        List<String> formatTenFileNameList = new ArrayList<>();
        List<MisObject> queryOutput = new ArrayList<>();
        List<SwitchTransactionObject> queryOutput1 = new ArrayList<>();
        Row createRow;
        for (MerchantDataDto dto : merchantDataDtosMap) {

            switch (dto.getFormat()) {

                case 11:
                    queryOutput1 = findBySerachedMerChantFormatEleven(dto.getMerchantName());

                    if (queryOutput1.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChants(queryOutput1, dto.getMerchantName() + "_" + dto.getVerifiedMaildId(), dto.format);
//                            CreateExcelView.createMisReportForMerChants(queryOutput1, dto.getMerchantName() + "_" , dto.format);
                            log.info("CASE 11 EXCEL FILE GENERATION............" + dto.getMerchantName());
//                            formatTenFileNameList.add(dto.getMerchantName() + "_" + dto.getVerifiedMaildId());
                            mailHandler.misSendMail(dto.getMerchantName() + "_" + dto.getVerifiedMaildId(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_3);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName() + "_" + dto.getVerifiedMaildId(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                default: // code to be executed if n doesn't match any cases
                    try {
                        log.info("before mail handler");
                        mailHandler.misSendMail(dto.getMerchantName(), INTERNALMAILDTO, "", dto.getFormat());
                        log.info("mail handler executed");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        //TODO All forat 10 created file sent mail to internal.
        try {
            sentAllFormat10File(formatTenFileNameList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        workbook.close();
    }

    @Override
    public void sentMail() throws Exception {
        firstrow = true;
        FileInputStream file = null;
        Workbook wb = null;
        List<MerchantDataDto> merchantDataDtosMap = null;
        try {
            file = new FileInputStream(new File(MERCHANT_FILE_DEATILS));
            wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file);
            merchantDataDtosMap = new ArrayList<MerchantDataDto>();

            for (Row row : wb.getSheetAt(0)) {
                if (firstrow) {
                    firstrow = false;
                    continue;
                }
                try {
                    MerchantDataDto merchantDataDto = new MerchantDataDto();
                    int cellValue = 0;
                    merchantDataDto.setMerchantName(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDto.setFormat((int) row.getCell(cellValue++).getNumericCellValue());
                    merchantDataDto.setMailId(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDto.setMailIdBcc(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDto.setMid(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDto.setVerifiedMaildId(row.getCell(cellValue++).getStringCellValue());
                    merchantDataDtosMap.add(merchantDataDto);
                } catch (Exception e) {
                    System.out.println("Error in merchant bijlipay file" + e.getMessage());
                }
            }
            wb.close();
            file.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.info("Error While Reading Merchant Excel File " + e);
        } finally {
            wb.close();
            file.close();
        }
        // TODO MerchantNameList excel created for who received mail.
        Workbook workbook = null;
        FileOutputStream fileOut = null;
        fileOut = new FileOutputStream(FILEDIRECTORY_MIS + "MerchantName_" + DateUtil.currentDate(yyyy_MM_dd) + ".xlsx");

        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("MerchantNameList");
        sheet.setDefaultColumnWidth(15);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Merchant Name");
        int rowCount = 1;
        List<String> formatTenFileNameList = new ArrayList<>();
        List<MisObject> queryOutput = new ArrayList<>();
        List<SwitchTransactionObject> queryOutput1 = new ArrayList<>();
        Row createRow;
        for (MerchantDataDto dto : merchantDataDtosMap) {

            switch (dto.getFormat()) {
                case 1:
                    queryOutput = findBySerachedMerChantFormatOne(dto.getMerchantName());
//                    log.info("after reaching query based on merchant name format case 1");
                    if (queryOutput.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChant(queryOutput, dto.getMerchantName(), dto.format);
//                            log.info("getting in to mail handler to send mail : case 1");
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_2);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    queryOutput = findBySerachedMerChantFormatTwo(dto.getMerchantName());
//                    log.info("after reaching query based on merchant name format 2");
                    if (queryOutput.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChant(queryOutput, dto.getMerchantName(), dto.format);
//                            log.info("getting in to mail handler to send mail : case 2");
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_2);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    queryOutput = findBySerachedMerChantFormatThree(dto.getMerchantName());
//                    log.info("after reaching query based on merchant name format case 3");
                    if (queryOutput.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChant(queryOutput, dto.getMerchantName(), dto.format);
//                            log.info("getting in to mail handler to send mail : case 3");
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_2);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 4:
                    queryOutput = findBySerachedMerChantFormatFour(dto.getMerchantName());
//                    log.info("after reaching query based on merchant name format case 4");
                    if (queryOutput.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChant(queryOutput, dto.getMerchantName(), dto.format);
//                            log.info("getting in to mail handler to send mail : case 4");
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_2);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 5:
                    queryOutput = findBySerachedMerChantFormatFive(dto.getMerchantName());
                    log.info("after reaching query based on merchant name format case 5");
                    if (queryOutput.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChant(queryOutput, dto.getMerchantName(), dto.format);
//                            log.info("getting in to mail handler to send mail : case 5");
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_2);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 6:
                    queryOutput = findBySerachedMerChantFormatSix(dto.getMerchantName());
//                    log.info("after reaching query based on merchant name format case 6");
                    if (queryOutput.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChant(queryOutput, dto.getMerchantName(), dto.format);
//                            log.info("getting in to mail handler to send mail : case 6");
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_2);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 7:

                    if (dto.getVerifiedMaildId().equalsIgnoreCase("NA")) {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName(), INTERNALMAILDTO, "", FORMAT_0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        queryOutput = findBySerachedMerChantFormatSeven(dto.getMerchantName(), dto.getVerifiedMaildId());
//                        log.info("after reaching query based on merchant name format case 7");
                        if (queryOutput.size() > 0) {
                            try {
                                CreateExcelView.createMisReportForMerChant(queryOutput, dto.getMerchantName(), dto.format);
//                                log.info("getting in to mail handler to send mail : case 7");
                                mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_2);
                                createRow = sheet.createRow(rowCount++);
                                createRow.createCell(0).setCellValue(dto.getMerchantName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                mailHandler.misSendMail(dto.getMerchantName(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                                createRow = sheet.createRow(rowCount++);
                                createRow.createCell(0).setCellValue(dto.getMerchantName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case 10: // TODO it was different from above format Merchant name should be mid_verifiedmailid
                    queryOutput = findBySerachedMerChantFormatTen(dto.getMerchantName());
//                    log.info("after reaching query based on merchant name format case 10");
                    if (queryOutput.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChant(queryOutput, dto.getMid() + "_" + dto.getVerifiedMaildId(), dto.format);
                            formatTenFileNameList.add(dto.getMid() + "_" + dto.getVerifiedMaildId());
//                            log.info("getting in to mail handler to send mail : case 10");
                            mailHandler.misSendMail(dto.getMid() + "_" + dto.getVerifiedMaildId(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_3);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMid() + "_" + dto.getVerifiedMaildId(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 11:
                    queryOutput1 = findBySerachedMerChantFormatEleven(dto.getMerchantName());

                    if (queryOutput1.size() > 0) {
                        try {
                            CreateExcelView.createMisReportForMerChants(queryOutput1, dto.getMerchantName() + "_" + dto.getVerifiedMaildId(), dto.format);
                            log.info("CASE 11 EXCEL FILE GENERATION............" + dto.getMerchantName());
//                            formatTenFileNameList.add(dto.getMerchantName() + "_" + dto.getVerifiedMaildId());
                            mailHandler.misSendMail(dto.getMerchantName() + "_" + dto.getVerifiedMaildId(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_3);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            mailHandler.misSendMail(dto.getMerchantName() + "_" + dto.getVerifiedMaildId(), dto.getMailId(), dto.getMailIdBcc(), FORMAT_1);
                            createRow = sheet.createRow(rowCount++);
                            createRow.createCell(0).setCellValue(dto.getMerchantName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                default: // code to be executed if n doesn't match any cases
                    try {
                        mailHandler.misSendMail(dto.getMerchantName(), INTERNALMAILDTO, "", dto.getFormat());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        //TODO All forat 10 created file sent mail to internal.
        try {
            sentAllFormat10File(formatTenFileNameList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        workbook.close();
    }

    private List<SwitchTransactionObject> findBySerachedMerChantFormatEleven(String MerchantId) {
        String query = "SELECT m.mti,m.txn_type,m.terminal_id,m.merchant_id,m.txn_date,m.txn_time,m.txn_amount, m.txn_response_code,m.response_received_time ,m.rrn_number, " + "m.stan,m.invoice_number, m.batch_number,m.urn " + "FROM bank.dbo.switch_transactions m where m.merchant_id = :merchant_id AND m.txn_date = '" + DateUtil.yesterday() + "'";


        Query q = entityManager.createNativeQuery(query);
        q.setParameter("merchant_id", MerchantId);
        log.info("Query Executing" + query);
        List<Object[]> result = (List<Object[]>) q.getResultList();
        List<SwitchTransactionObject> switchTransactionObjects = new ArrayList<>();
        result.forEach(r -> {

            SwitchTransactionObject trans = new SwitchTransactionObject();
            trans.setMti(r[0] == null ? "null" : r[0].toString());
            trans.setTxnType(r[1] == null ? "null" : r[1].toString());
            trans.setTerminalId(r[2] == null ? "null" : r[2].toString());
            trans.setMerchantId(r[3] == null ? "null" : r[3].toString());
            trans.setTxnDate(r[4] == null ? "null" : r[4].toString());
            trans.setTxnTime(r[5] == null ? "null" : r[5].toString());
            trans.setTxnAmount(r[6] == null || r[6].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[6]).floatValue());
            trans.setTxnResponseCode(r[7] == null ? "null" : r[7].toString());
            trans.setResponseReceivedTime((Date) r[8]);
            trans.setRrnNumber(r[9] == null ? "null" : r[9].toString());
            trans.setStan(r[10] == null ? "null" : r[10].toString());
            trans.setInvoiceNumber(r[11] == null ? "null" : r[11].toString());
            trans.setBatchNumber(r[12] == null ? "null" : r[12].toString());
            trans.setUrn(r[13] == null ? "null" : r[13].toString());

            switchTransactionObjects.add(trans);
        });
        return switchTransactionObjects;
    }

    private void sentAllFormat10File(List<String> fileStringList) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setText(String.format("Dear Team," + "\n\n" + "Greetings from Bijlipay!!" + "\n\n" + "Please find the " + "enclosed settlements report of shell merchant generated today " + " " + "\n\n" + "Regards," + "\n" + "Team Bijlipay\n\n Note :" + "This is an auto generated email. Please do not respond to this email id.\n For any queries You can call us @ 1800 4200 235 or write to us @ service@bijlipay.co.in."));
        helper.setFrom(fromEmailId);
        helper.setTo(SHELLMAILIDS);
        log.info("file size {}", fileStringList.size());
        for (String fileName : fileStringList) {
            FileSystemResource fr = new FileSystemResource(MERCHANT_FILE_DIRECTORY_MIS + fileName + "_" + DateUtil.currentDate("yyyy-MM-dd") + ".xlsx");
            helper.addAttachment(fr.getFilename(), fr);
        }
        try {
            sender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<MisObject> findBySerachedMerChantFormatTen(String mid) {
        String query = "SELECT m.Txn_Date,m.Txn_Date as txn_time,m.mid,m.tid,m.Merchant_Name,m.city,m.Card_NO, m.Card_Type,m.RRN ,m.Association_Name, " + "m.Txnamount,m.MDR, m.gst, m.netamount,m.card_Type1,m.tradename,m" + ".paymentdate, m" + ".narration FROM bijlipay.dbo.Bijlipay_Mis$ m where m.MID = :mid";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("mid", mid);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<MisObject> misObjects = new ArrayList<>();
        results.forEach(r -> {
            MisObject object = new MisObject();
            object.setTxnDate((Date) r[0]);
            object.setTxnTime((Date) r[1]);
            object.setMid(r[2] == null ? "null" : r[2].toString());
            object.setTid(r[3] == null ? "null" : r[3].toString());
            object.setMerchantName(r[4] == null ? "null" : r[4].toString());
            object.setCity(r[5] == null ? "null" : r[5].toString());
            object.setCardNo(r[6] == null ? "null" : r[6].toString());
            object.setCardType(r[7] == null ? "null" : r[7].toString());
            object.setrRN(r[8] == null ? "null" : r[8].toString());
            object.setAssociationName(r[9] == null ? "null" : r[9].toString());
            object.setTxnamount(r[10] == null || r[10].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[10]).floatValue());
            object.setMrlmdr(r[11] == null || r[11].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[11]).floatValue());
            object.setGst(r[12] == null || r[12].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[12]).floatValue());
            object.setNetamount(r[13] == null || r[13].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[13]).floatValue());
            object.setCardType1(r[14] == null ? "null" : r[14].toString());
            object.setTradename(r[15] == null ? "null" : r[15].toString());
            object.setPaymentdate((Date) r[16]);
            object.setNarration(r[17] == null ? null : r[17].toString());
            misObjects.add(object);
        });
        return misObjects;
    }

    private List<MisObject> findBySerachedMerChantFormatSeven(String merchantName, String mailId) {
        String query = "SELECT m.Txn_Date,m.mid,m.tid,m.Merchant_Name,m.city,m.Card_NO, m.Card_Type,m.RRN ,m.Association_Name,m.Txnamount,m.MDR, m.gst, m.netamount,m.card_Type1,m.tradename,m" + ".paymentdate, m.narration FROM bijlipay.dbo.Bijlipay_Mis$ m where m.merchant_name = :merchant_name AND m.email = " + ":mailId";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("merchant_name", merchantName);
        q.setParameter("mailId", mailId);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<MisObject> misObjects = new ArrayList<>();
        results.forEach(r -> {
            MisObject object = new MisObject();
            object.setTxnDate((Date) r[0]);
            object.setMid(r[1] == null ? "null" : r[1].toString());
            object.setTid(r[2] == null ? "null" : r[2].toString());
            object.setMerchantName(r[3] == null ? "null" : r[3].toString());
            object.setCity(r[4] == null ? "null" : r[4].toString());
            object.setCardNo(r[5] == null ? "null" : r[5].toString());
            object.setCardType(r[6] == null ? "null" : r[6].toString());
            object.setrRN(r[7] == null ? "null" : r[7].toString());
            object.setAssociationName(r[8] == null ? "null" : r[8].toString());
            object.setTxnamount(r[9] == null || r[9].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[9]).floatValue());
            object.setMrlmdr(r[10] == null || r[10].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[10]).floatValue());
            object.setGst(r[11] == null || r[11].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[11]).floatValue());
            object.setNetamount(r[12] == null || r[12].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[12]).floatValue());
            object.setCardType1(r[13] == null ? "null" : r[13].toString());
            object.setTradename(r[14] == null ? "null" : r[14].toString());
            object.setPaymentdate((Date) r[15]);
            object.setNarration(r[16] == null ? null : r[16].toString());
            misObjects.add(object);
        });
        return misObjects;
    }

    private List<MisObject> findBySerachedMerChantFormatSix(String merchantName) {
        String query = "SELECT m.Txn_Date,m.mid,m.tid,m.Merchant_Name,m.city,m.Card_NO, m.Card_Type,m.RRN ,m.Association_Name, m.Txnamount,m.MDR, m.gst," + " m.netamount,m.card_Type1,m.tradename,m.paymentdate, m.narration,m.incentive FROM bijlipay.dbo.Bijlipay_Mis$ m " + "where m.merchant_name = :merchant_name";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("merchant_name", merchantName);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<MisObject> misObjects = new ArrayList<>();
        results.forEach(r -> {
            MisObject obj = new MisObject();
            obj.setTxnDate((Date) r[0]);
            obj.setMid(r[1] == null ? "null" : (String) r[1]);
            obj.setTid(r[2] == null ? "null" : (String) r[2]);
            obj.setMerchantName(r[3] == null ? "null" : (String) r[3]);
            obj.setCity(r[4] == null ? "null" : (String) r[4]);
            obj.setCardNo(r[5] == null ? "null" : (String) r[5]);
            obj.setCardType(r[6] == null ? "null" : (String) r[6]);
            obj.setrRN(r[7] == null ? "null" : (String) r[7]);
            obj.setAssociationName(r[8] == null ? "null" : (String) r[8]);
            obj.setTxnamount(r[9] == null || r[9].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[9]).floatValue());
            obj.setMrlmdr(r[10] == null || r[10].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[10]).floatValue());
            obj.setGst(r[11] == null || r[11].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[11]).floatValue());
            obj.setNetamount(r[12] == null || r[12].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[12]).floatValue());
            obj.setCardType1(r[13] == null ? "null" : (String) r[13]);
            obj.setTradename(r[14].toString() == null ? "null" : (String) r[14]);
            obj.setPaymentdate((Date) r[15]);
            obj.setNarration(r[16].toString() == null ? "null" : (String) r[16]);
            obj.setIncentive(r[17] == null || r[17].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[17]).floatValue());
            misObjects.add(obj);
        });
        return misObjects;
    }

    private List<MisObject> findBySerachedMerChantFormatFive(String merchantName) {
        String query = "SELECT m.Txn_Date,m.Txn_Date as txn_time,m.mid,m.tid,m.Merchant_Name,m.city,m.Card_NO, m" + ".Card_Type,m.RRN ,m.Association_Name, " + "m.Txnamount,m.MDR, m.gst, m.netamount,m.card_Type1,m.tradename,m.paymentdate, m.narration FROM " + "bijlipay.dbo.Bijlipay_Mis$ m where m.merchant_name = :merchant_name";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("merchant_name", merchantName);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<MisObject> misObjects = new ArrayList<>();
        results.forEach(r -> {
            MisObject object = new MisObject();
            object.setTxnDate((Date) r[0]);
            object.setTxnTime((Date) r[1]);
            object.setMid(r[2] == null ? "null" : r[2].toString());
            object.setTid(r[3] == null ? "null" : r[3].toString());
            object.setMerchantName(r[4] == null ? "null" : r[4].toString());
            object.setCity(r[5] == null ? "null" : r[5].toString());
            object.setCardNo(r[6] == null ? "null" : r[6].toString());
            object.setCardType(r[7] == null ? "null" : r[7].toString());
            object.setrRN(r[8] == null ? "null" : r[8].toString());
            object.setAssociationName(r[9] == null ? "null" : r[9].toString());
            object.setTxnamount(r[10] == null || r[10].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[10]).floatValue());
            object.setMrlmdr(r[11] == null || r[11].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[11]).floatValue());
            object.setGst(r[12] == null || r[12].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[12]).floatValue());
            object.setNetamount(r[13] == null || r[13].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[13]).floatValue());
            object.setCardType1(r[14] == null ? "null" : r[14].toString());
            object.setTradename(r[15] == null ? "null" : r[15].toString());
            object.setPaymentdate((Date) r[16]);
            object.setNarration(r[17] == null ? null : r[17].toString());
            misObjects.add(object);
        });
        return misObjects;
    }

    private List<MisObject> findBySerachedMerChantFormatFour(String merchantName) {
        String query = "SELECT m.Txn_Date,m.mid,m.tid,m.Merchant_Name,m.city,m.state,m.Card_NO, m.Card_Type,m.RRN ,m.Association_Name,m.Txnamount,m.MDR," + " m.gst, m.netamount,m.card_Type1,m.tradename,m.paymentdate,m.narration FROM bijlipay.dbo.Bijlipay_Mis$ m where " + "m.merchant_name = :merchant_name";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("merchant_name", merchantName);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<MisObject> misObjects = new ArrayList<>();
        results.forEach(r -> {
            MisObject object = new MisObject();
            object.setTxnDate((Date) r[0]);
            object.setMid(r[1] == null ? "null" : r[1].toString());
            object.setTid(r[2] == null ? "null" : r[2].toString());
            object.setMerchantName(r[3] == null ? "null" : r[3].toString());
            object.setCity(r[4] == null ? "null" : r[4].toString());
            object.setState(r[5] == null ? "null" : r[5].toString());
            object.setCardNo(r[6] == null ? "null" : r[6].toString());
            object.setCardType(r[7] == null ? "null" : r[7].toString());
            object.setrRN(r[8] == null ? "null" : r[8].toString());
            object.setAssociationName(r[9] == null ? "null" : r[9].toString());
            object.setTxnamount(r[10] == null || r[10].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[10]).floatValue());
            object.setMrlmdr(r[11] == null || r[11].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[11]).floatValue());
            object.setGst(r[12] == null || r[12].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[12]).floatValue());
            object.setNetamount(r[13] == null || r[13].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[13]).floatValue());
            object.setCardType1(r[14] == null ? "null" : r[14].toString());
            object.setTradename(r[15] == null ? "null" : r[15].toString());
            object.setPaymentdate((Date) r[16]);
            object.setNarration(r[17] == null ? null : r[17].toString());
            misObjects.add(object);
        });
        return misObjects;
    }

    private List<MisObject> findBySerachedMerChantFormatThree(String merchantName) {
        String query = "SELECT m.Txn_Date,m.mid,m.tid,m.Merchant_Name,m.city,m.Card_NO, m.Card_Type,m.RRN,m.Association_Name,m.Txnamount,m.MDR," + "m.gst, m.netamount,m.card_Type1,m.tradename,m.mdr_percentage,m.paymentdate,m.narration FROM " + "bijlipay.dbo.Bijlipay_Mis$ m where m.merchant_name = :merchant_name";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("merchant_name", merchantName);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<MisObject> misObjects = new ArrayList<>();
        results.forEach(r -> {
            MisObject object = new MisObject();
            object.setTxnDate((Date) r[0]);
            object.setMid(r[1] == null ? "null" : r[1].toString());
            object.setTid(r[2] == null ? "null" : r[2].toString());
            object.setMerchantName(r[3] == null ? "null" : r[3].toString());
            object.setCity(r[4] == null ? "null" : r[4].toString());
            object.setCardNo(r[5] == null ? "null" : r[5].toString());
            object.setCardType(r[6] == null ? "null" : r[6].toString());
            object.setrRN(r[7] == null ? "null" : r[7].toString());
            object.setAssociationName(r[8] == null ? "null" : r[8].toString());
            object.setTxnamount(r[9] == null || r[9].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[9]).floatValue());
            object.setMrlmdr(r[10] == null || r[10].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[10]).floatValue());
            object.setGst(r[11] == null || r[11].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[11]).floatValue());
            object.setNetamount(r[12] == null || r[12].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[12]).floatValue());
            object.setCardType1(r[13] == null ? "null" : r[13].toString());
            object.setTradename(r[14] == null ? "null" : r[14].toString());
            object.setMdr_percentage(r[15] == null || r[15].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[15]).floatValue());
            object.setPaymentdate((Date) r[16]);
            object.setNarration(r[17] == null ? null : r[17].toString());
            misObjects.add(object);
        });
        return misObjects;
    }

    private List<MisObject> findBySerachedMerChantFormatTwo(String merchantName) {
        String query = "SELECT m.Txn_Date,m.mid,m.tid,m.Merchant_Name,m.city,m.Card_NO, m.Card_Type,m.RRN,m.Association_Name,m.Txnamount,m.MDR," + "m.gst, m.netamount,m.card_Type1,m.tradename,m.location,m.paymentdate,m.narration FROM bijlipay.dbo.Bijlipay_Mis$ " + "m where m.merchant_name = :merchant_name";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("merchant_name", merchantName);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<MisObject> misObjects = new ArrayList<>();
        results.forEach(r -> {
            MisObject object = new MisObject();
            object.setTxnDate((Date) r[0]);
            object.setMid(r[1] == null ? "null" : r[1].toString());
            object.setTid(r[2] == null ? "null" : r[2].toString());
            object.setMerchantName(r[3] == null ? "null" : r[3].toString());
            object.setCity(r[4] == null ? "null" : r[4].toString());
            object.setCardNo(r[5] == null ? "null" : r[5].toString());
            object.setCardType(r[6] == null ? "null" : r[6].toString());
            object.setrRN(r[7] == null ? "null" : r[7].toString());
            object.setAssociationName(r[8] == null ? "null" : r[8].toString());
            object.setTxnamount(r[9] == null || r[9].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[9]).floatValue());
            object.setMrlmdr(r[10] == null || r[10].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[10]).floatValue());
            object.setGst(r[11] == null || r[11].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[11]).floatValue());
            object.setNetamount(r[12] == null || r[12].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[12]).floatValue());
            object.setCardType1(r[13] == null ? "null" : r[13].toString());
            object.setTradename(r[14] == null ? "null" : r[14].toString());
            object.setLocation(r[15] == null ? "null" : r[15].toString());
            object.setPaymentdate((Date) r[16]);
            object.setNarration(r[17] == null ? null : r[17].toString());
            misObjects.add(object);
        });
        return misObjects;
    }

    private List<MisObject> findBySerachedMerChantFormatOne(String merchantName) {
        String query = "SELECT m.Txn_Date,m.mid,m.tid,m.Merchant_Name,m.city,m.Card_NO, m.Card_Type,m.RRN ,m.Association_Name, " + "m.Txnamount,m.MDR, m.gst, m.netamount,m.card_Type1,m.tradename,m" + ".paymentdate, m" + ".narration FROM bijlipay.dbo.Bijlipay_Mis$ m where m.merchant_name = :merchant_name";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("merchant_name", merchantName);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<MisObject> misObjects = new ArrayList<>();
        results.forEach(r -> {
            MisObject object = new MisObject();
            object.setTxnDate((Date) r[0]);
            object.setMid(r[1] == null ? "null" : r[1].toString());
            object.setTid(r[2] == null ? "null" : r[2].toString());
            object.setMerchantName(r[3] == null ? "null" : r[3].toString());
            object.setCity(r[4] == null ? "null" : r[4].toString());
            object.setCardNo(r[5] == null ? "null" : r[5].toString());
            object.setCardType(r[6] == null ? "null" : r[6].toString());
            object.setrRN(r[7] == null ? "null" : r[7].toString());
            object.setAssociationName(r[8] == null ? "null" : r[8].toString());
            object.setTxnamount(r[9] == null || r[9].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[9]).floatValue());
            object.setMrlmdr(r[10] == null || r[10].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[10]).floatValue());
            object.setGst(r[11] == null || r[11].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[11]).floatValue());
            object.setNetamount(r[12] == null || r[12].toString().trim().isEmpty() ? ((Double) 0.0).floatValue() : ((Double) r[12]).floatValue());
            object.setCardType1(r[13] == null ? "null" : r[13].toString());
            object.setTradename(r[14] == null ? "null" : r[14].toString());
            object.setPaymentdate((Date) r[15]);
            object.setNarration(r[16] == null ? null : r[16].toString());
            misObjects.add(object);
        });
        return misObjects;
    }

    @Modifying
    @Override
    public void deleteystrecord() {
        dailySettlementRepository.deleteAllInBatch();
    }

    @Modifying
    @Override
    public void deletemistemp() {
        misRespository.deleteAllInBatch();
    }

    @Override
    public boolean connectToSFTPFileUpload(String[] filePath) {
        boolean connectionStatus = false;
        boolean status = false;
        try {
            this.mJschSession = new JSch();

            java.util.Properties config = new java.util.Properties();

            config.put("StrictHostKeyChecking", "no");

            JSch.setConfig(config);

            this.mSSHSession = mJschSession.getSession(TOMCAT_SFTP_USERNAME_MIS, TOMCAT_SFTP_HOST_MIS, TOMCAT_SFTP_PORT_MIS);

            this.mSSHSession.setPassword(TOMCAT_SFTP_PASSWORD_MIS);

            this.mSSHSession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");

            this.mSSHSession.connect();

            this.mChannelSftp = (ChannelSftp) this.mSSHSession.openChannel("sftp");

            this.mChannelSftp.connect();

            if (this.mChannelSftp != null) {
                connectionStatus = true;
            }
            if (connectionStatus) {
                boolean upload = false;
                for (String localFilePath : filePath) {
                    upload = uploadFile(localFilePath);
                }
                if (upload) {
                    status = true;
                }
                close();
            } else {
                log.info("SFTP Connection Faild!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public void federalSettlementReport(String bijlipaySettlementReport) {
        Workbook wb = null;
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(bijlipaySettlementReport));
            wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file);
            List<DailySettlement> map = new ArrayList<>();
            for (Sheet sheet : wb) {
                firstrow = true;
                for (Row row : sheet) {
                    if (firstrow) {
                        firstrow = false;
                        continue;
                    }
                    DailySettlement misTable = new DailySettlement();
                    int cellValue = 0;
                    misTable.setDate(DateUtil.convertStringToDate(row.getCell(cellValue++).getStringCellValue(), DD_MM_YY));
                    misTable.setMid(row.getCell(cellValue++).getStringCellValue());
                    misTable.setTid(row.getCell(cellValue++).getStringCellValue());
                    misTable.setMerchantName(row.getCell(cellValue++).getStringCellValue());
                    misTable.setCity(row.getCell(cellValue++).getStringCellValue());
                    misTable.setAccountNumber(row.getCell(cellValue++).getStringCellValue());
                    misTable.setBranchCode(row.getCell(cellValue++).getStringCellValue());
                    misTable.setTxncount((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setTxnamount((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setMrlmdr((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setGst((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setNetamount((float) row.getCell(cellValue++).getNumericCellValue());
                    misTable.setTxn_type(row.getCell(cellValue++).getStringCellValue());
                    misTable.setCard_Type(row.getCell(cellValue++).getStringCellValue());
                    misTable.setCardproduct(row.getCell(cellValue++).getStringCellValue());
                    misTable.setAssociationfee((float) row.getCell(cellValue++).getNumericCellValue());
                    map.add(misTable);
                }
                log.info("federalSettlement start to insert");
                dailySettlementRepository.saveAll(map);
            }
            wb.close();
            file.close();
            map.clear();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error " + e);
        } finally {
            try {
                wb.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
    }

    @Override
    public boolean connectToSFTPDownloadWithSpecificDate(Optional<Long> date) {
        boolean connectionStatus = false;
        boolean status = false;
        String REMOTEFILEPATH = null;
        String baseUrl = "/users/MRL-RECON/Bijlipay/";
        if (date.isPresent()) {
            REMOTEFILEPATH = baseUrl + DateUtil.longToDate(date.get(), MMM) + " " + DateUtil.longToDate(date.get(), yyyy) + "/" + DateUtil.longToDate(date.get(), MMddyyyy) + "/" + BIJLIPAY_SETTLEMENT_REPORT + DateUtil.longToDate(date.get(), ddMMyyyy) + ".xlsx";
        } else {
            REMOTEFILEPATH = baseUrl + DateUtil.currentDate(MMM) + " " + DateUtil.currentDate(yyyy) + "/" + DateUtil.currentDate(MMddyyyy) + "/" + BIJLIPAY_SETTLEMENT_REPORT + DateUtil.currentDate(ddMMyyyy) + ".xlsx";
        }
        try {
            this.mJschSession = new JSch();

            Properties config = new Properties();

            config.put("StrictHostKeyChecking", "no");

            JSch.setConfig(config);

            this.mSSHSession = mJschSession.getSession(worldlineSftpUsername, worldlineSftpHost, SFTP_PORT);

            this.mSSHSession.setPassword(worldlineSftpPassword);

            this.mSSHSession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");

            this.mSSHSession.connect();

            this.mChannelSftp = (ChannelSftp) this.mSSHSession.openChannel("sftp");

            this.mChannelSftp.connect();

            if (this.mChannelSftp != null) {
                connectionStatus = true;
            }
            if (connectionStatus) {
                if (exists(mChannelSftp, REMOTEFILEPATH)) {
                    File deleteIfExists = new File(FILEDIRECTORY_MIS);
//                    if (deleteIfExists.exists()) {
//                        FileUtils.deleteDirectory(deleteIfExists);
//                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    final String givenDate = date.isPresent() ? DateUtil.longToDate(date.get(), ddMMyyyy) : DateUtil.currentDate(ddMMyyyy);
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, FILEDIRECTORY_MIS + BIJLIPAY_SETTLEMENT_REPORT + givenDate + ".xlsx");
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

    @Transactional
    @Modifying
    @Override
    public void deleteRentTable() {
        int deletedRecords = entityManager.createNativeQuery("DELETE FROM rent.dbo.daily").executeUpdate();
        System.out.println("Daily Records Deleted " + deletedRecords);
    }

    @Transactional
    @Modifying
    @Override
    public void insertDailyRecords() {
        int insertRecords = entityManager.createNativeQuery("Insert INTO rent.dbo.daily (tid,txnamount) " + "SELECT tid,sum(netamount) from Bijlipay.dbo.Bijlipay_Mis$  group by tid ").executeUpdate();
        System.out.println("Daily Records Deleted " + insertRecords);
        int updateTxnAmountIsNull = entityManager.createNativeQuery("UPDATE rent.dbo.daily SET txnamount = 0 where txnamount is null").executeUpdate();
        System.out.println("Updated null record from daily " + updateTxnAmountIsNull);
        misRespository.insertIntoAceTableFromAuthTable();
    }

    public boolean uploadFile(String strLocalFile) {
        boolean status = false;
        try {
            this.mChannelSftp.cd(SFTPWORKINGDIR_MIS);
            File f = new File(strLocalFile);
            this.mChannelSftp.put(new FileInputStream(f), f.getName());
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
