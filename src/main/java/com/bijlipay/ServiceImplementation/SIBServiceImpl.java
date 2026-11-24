package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.SibReport;
import com.bijlipay.Repository.SIBRepository;
import com.bijlipay.Service.SIBService;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.bijlipay.Utils.Constants.*;
import static com.bijlipay.Utils.Constants.BIJLIPAY_MIS_XLSX_FEDERAL;

@Service
public class SIBServiceImpl implements SIBService {

    private static final Logger log = LoggerFactory.getLogger(SIBServiceImpl.class);
    @Autowired
    TsdbService tsdbService;
    private JSch mJschSession = null;
    private Session mSSHSession = null;
    private ChannelSftp mChannelSftp = null;

    @Autowired
    private SIBService sibService;

    @Autowired
    private SIBRepository sibRepository;
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

    @Override
    public boolean createUpdatedExcel(String bijlipayMisXlsx, String date) {
        boolean FIRSTROW = true;
        boolean updated = false;
        Row.MissingCellPolicy ROW_BLANK_AS_NULL = Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
        int count = 0;
        FileInputStream file = null;
        Workbook wb = null;
        List<SibReport> misDTOS = new ArrayList<>();
        try {
            file = new FileInputStream(bijlipayMisXlsx);
            wb = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096).open(file);
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
                misTable.setAssocationFee((float) row.getCell(cellValue++).getNumericCellValue());
                misTable.setNarration(row.getCell(cellValue, ROW_BLANK_AS_NULL) == null ? "NA" : row.getCell(cellValue).getStringCellValue().equalsIgnoreCase("SALE") ? "POS" : row.getCell(cellValue).getStringCellValue());
                misTable.setMonth(DateUtil.currentDate(MMM_yy));
                misTable.setDate(date);
                misDTOS.add(misTable);
                count++;
            }
            wb.close();
            file.close();
        } catch (IOException e) {
            System.out.println("IOException reading Mars Excel File " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error reading Mars Excel File " + e.getMessage());
        }
        try {  // TODO Create federal file directly using smb
            CreateExcelView.createSIBReportForMerChant(misDTOS, date);
            boolean twoDayBefore = sibService.findByCreatedAt(date);
            if (!twoDayBefore) {
                log.info("data inserted for the choosed date {}", date);
                sibRepository.saveAll(misDTOS);
                log.info("data insert started successfully");
//                tsdbService.insertSib(misDTOS);
//                log.info("sib count -------->",count);
                log.info("data insert Finished successfully");
            } else {
                log.info("already data inserted for the choosed date {}", date);
            }
            updated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updated;
    }

    @Override
    public boolean findByCreatedAt(String oneDayBefore) {
        boolean result = sibRepository.countBydate(oneDayBefore) > 0;
        return result;
    }

    @Override
    public boolean connectToSFTPDownloadBeforeDates(long time) {
        Date mDate = new Date(time * 1000L);
        boolean connectionStatus = false;
        boolean status = false;
        String destPath = BIJLIPAY_MIS_XLSX_SIB_ZIP + DateUtil.longToDate(time, ddMM) + ".zip";
//        String destPath =BIJLIPAY_MIS_XLSX_SIB_FILENAME+ DateUtil.longToDate(time,ddMMyyyy) + ".xlsx";

        final String REMOTEFILEPATH =
                SFTP_SIB_PATH + MONTHS[mDate.getMonth()] + "-" + DateUtil.longToDate(time, yy) + "/" + DateUtil.longToDate(time, ddMMyy) +
                        "/" + destPath;
        log.info("REMOTEFILEPATH {}", REMOTEFILEPATH);
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
                    File deleteIfExists = new File(LOCALFILEPATH_SIB);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_SIB +
                            destPath);
                    if (downloadStatus) {
                        boolean unZipStatus = unzipfile(LOCALFILEPATH_SIB + destPath, LOCALFILEPATH_SIB);
                        if (unZipStatus) {
                            status = true;
                        }
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

    @Override
    public boolean connectToSFTPDownloadWithSpecificDate(Optional<Long> date) {
        String destPath = null;
        boolean connectionStatus = false;
        boolean status = false;
        String REMOTEFILEPATH = null;
        if (date.isPresent()) {
            Date mDate = new Date(date.get() * 1000L);
            destPath = BIJLIPAY_MIS_XLSX_SIB_ZIP + DateUtil.longToDate(date.get(), ddMM) + ".zip";
//            destPath =BIJLIPAY_MIS_XLSX_SIB_FILENAME+ DateUtil.longToDate(date.get() ,ddMMyyyy) + ".xlsx";
            REMOTEFILEPATH = SFTP_SIB_PATH + MONTHS[mDate.getMonth()] + "-" + DateUtil.longToDate(date.get(), yy) + "/" + DateUtil.longToDate(date.get(), ddMMyy) +
                    "/" + destPath;
        } else {
            Date mDate = new Date(new Date().getTime());
            destPath = BIJLIPAY_MIS_XLSX_SIB_ZIP + DateUtil.currentDate(ddMM) + ".zip";
//            destPath =BIJLIPAY_MIS_XLSX_SIB_FILENAME+ DateUtil.currentDate(ddMMyyyy) + ".xlsx";
            REMOTEFILEPATH = SFTP_SIB_PATH + MONTHS[mDate.getMonth()] + "-" + DateUtil.currentDate(yy) + "/" + DateUtil.currentDate(ddMMyy) +
                    "/" + destPath;
        }
        log.info("Worldline SIB Scheduler File Path --{}",REMOTEFILEPATH);
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
                    File deleteIfExists = new File(LOCALFILEPATH_SIB);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_SIB +
                            destPath);
                    if (downloadStatus) {
                        boolean unZipStatus = unzipfile(LOCALFILEPATH_SIB + destPath, LOCALFILEPATH_SIB);
                        if (unZipStatus) {
                            status = true;
                        }
                    }
                } else {
                    mailHandler.sendSFTPFileNotFoundMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), "SIB");
                    log.info("SFTP File Not Found!");
                    return status;
                }
            } else {
                mailHandler.sendSFTPFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), "SIB");
                log.info("SFTP Connection Failed!");
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
//            finally {
//                try {
//                    tryReConnect();
//                } catch (Exception e) {
//                    System.out.print(e);
//                }
//            }
        }
        return status;
    }

    public boolean tryReConnect() {
        String destPath = null;
        boolean connectionStatus = false;
        boolean status = false;
        String REMOTEFILEPATH = null;
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
                    File deleteIfExists = new File(LOCALFILEPATH_SIB);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_SIB +
                            destPath);
                    if (downloadStatus) {
                        boolean unZipStatus = unzipfile(LOCALFILEPATH_SIB + destPath, LOCALFILEPATH_SIB);
                        if (unZipStatus) {
                            status = true;
                        }
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
        return connectionStatus;
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
}
