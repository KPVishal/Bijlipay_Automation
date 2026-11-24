package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.FederalDTO;
import com.bijlipay.Repository.FederalRepository;
import com.bijlipay.Service.FederalService;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static com.bijlipay.Utils.Constants.*;

@Service
public class FederalServiceImplementation implements FederalService {
    private static final Logger log = LoggerFactory.getLogger(FederalServiceImplementation.class);

    private JSch mJschSession = null;
    private Session mSSHSession = null;
    private ChannelSftp mChannelSftp = null;


    @Autowired
    private TsdbService tsdbService;
    @Autowired
    private FederalService misService;

    @Autowired
    private FederalRepository federalRepository;

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
        List<FederalDTO> misDTOS = new ArrayList<>();
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
            CreateExcelView.createFederalReportForMerChant(misDTOS, date);
            boolean twoDayBefore = misService.findByCreatedAt(date);
            if (!twoDayBefore) {
                log.info("data inserted for the choosed date {}", date);
                federalRepository.saveAll(misDTOS);
                log.info("data insert started successfully");
//                tsdbService.insertFedral(misDTOS);
//                    log.error("Fedral count---------->",count);
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

    @Override
    public boolean findByCreatedAt(String oneDayBefore) {
        boolean result = federalRepository.countBydate(oneDayBefore) > 0;
        return result;
    }

    @Override
    public boolean connectToSFTPDownloadBeforeDates(long time) {
        boolean connectionStatus = false;
        boolean status = false;
        String basePath = "/users/MRL-RECON/Bijlipay/";
        final String REMOTEFILEPATH =
                basePath + DateUtil.longToDate(time, MMM) + " " + DateUtil.longToDate(time, yyyy) +
                        "/" + DateUtil.longToDate(time, MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_FEDERAL + DateUtil.longToDate(time,
                        ddMMyyyy) + ".xlsx";
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
                    File deleteIfExists = new File(LOCALFILEPATH_FEDERAL);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    final String givenDate = DateUtil.longToDate(time, ddMMyyyy);
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_FEDERAL +
                            BIJLIPAY_MIS_XLSX_FEDERAL + givenDate + ".xlsx");
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

    @Override
    public boolean connectToSFTPDownloadWithSpecificDate(Optional<Long> date) {
        boolean connectionStatus = false;
        boolean status = false;
        String REMOTEFILEPATH = null;
//        String basePath = "/users/MRL-RECON/Bijlipay/";
        String basePath = "/Bijlipay/";
        if (date.isPresent()) {
            REMOTEFILEPATH = basePath + DateUtil.longToDate(date.get(), MMM) + DateUtil.longToDate(date.get(), yyyy) +
                    "/" + DateUtil.longToDate(date.get(), MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_FEDERAL + DateUtil.longToDate(date.get(), ddMMyyyy) + ".xlsx";
        } else {
            REMOTEFILEPATH = basePath + DateUtil.currentDate(MMM) + DateUtil.currentDate(yyyy) +
                    "/" + DateUtil.currentDate(MMddyyyy) + "/" + BIJLIPAY_MIS_XLSX_FEDERAL + DateUtil.currentDate(ddMMyyyy) + ".xlsx";
        }
        log.info("Worldline Federal Scheduler File Path --{}", REMOTEFILEPATH);
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
                    File deleteIfExists = new File(LOCALFILEPATH_FEDERAL);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    final String givenDate = date.isPresent() ? DateUtil.longToDate(date.get(), ddMMyyyy) : DateUtil.currentDate(ddMMyyyy);
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_FEDERAL +
                            BIJLIPAY_MIS_XLSX_FEDERAL + givenDate + ".xlsx");
                    if (downloadStatus) {
                        status = true;
                    }

                } else {
                    mailHandler.sendSFTPFileNotFoundMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), "Federal");
                    log.info("SFTP File Not Found!");
                    return status;
                }
            } else {
                mailHandler.sendSFTPFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA), "Federal");
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

}
