package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.DTO.KVBDTo;
import com.bijlipay.Repository.KVBtoMarsRepository;
import com.bijlipay.SchedulerController.AxisSchedular;
import com.bijlipay.Service.KvbService;
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

public class KvbServiceImpl implements KvbService {
    private JSch mJschSession = null;
    private Session mSSHSession = null;
    private ChannelSftp mChannelSftp = null;

    @Autowired
    private KVBtoMarsRepository kvBtoMarsRepository;

    @Autowired
    private TsdbService tsdbService;
    @Autowired
    private KvbService kvbService;
    @Autowired
    private MailHandler mailHandler;


    @Value("${worldline.sftp.host}")
    private String worldlineSftpHost;
    @Value("${worldline.sftp.username}")
    private String worldlineSftpUsername;
    @Value("${worldline.sftp.password}")
    private String worldlineSftpPassword;
    private static final Logger log = LoggerFactory.getLogger(KvbServiceImpl.class);

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
    public boolean connectToSFTPDownloadBeforeDates(long time) {
        boolean connectionStatus = false;
        boolean status = false;
        String basePath = "/users/MRL-RECON/Bijlipay/KVB/";
        final String REMOTEFILEPATH =
                basePath + DateUtil.longToDate(time, MMM) + "-" + DateUtil.longToDate(time, yy) +
                        "/" + DateUtil.longToDate(time, ddMMyy) + "/" + BIJLIPAY_MIS_XLSX_KVB + DateUtil.longToDate(time,
                        ddMMyyyy) + ".xlsx";
        log.info("file remote path is {}",REMOTEFILEPATH);
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
                    File deleteIfExists = new File(LOCALFILEPATH_KVB);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }
                    final String givenDate = DateUtil.longToDate(time, ddMMyyyy);
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_KVB +
                            BIJLIPAY_MIS_XLSX_KVB + givenDate + ".xlsx");
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
//        String basePath = "/users/MRL-RECON/Bijlipay/KVB/";
        String basePath = "/Bijlipay/KVB/";
        if (date.isPresent()) {
            REMOTEFILEPATH = basePath + DateUtil.longToDate(date.get(), MMM).toUpperCase() + "-" + DateUtil.longToDate(date.get(), yy) +
                    "/" + DateUtil.longToDate(date.get(), ddMMyy) + "/" + BIJLIPAY_MIS_XLSX_KVB + DateUtil.longToDate(date.get(), ddMMyyyy) + ".xlsx";

            log.info("file Remote path is {}",REMOTEFILEPATH);
        } else {
            REMOTEFILEPATH = basePath + DateUtil.currentDate(MMM).toUpperCase() + "-" + DateUtil.currentDate(yy) +
                    "/" + DateUtil.currentDate(ddMMyy) + "/" + BIJLIPAY_MIS_XLSX_KVB + DateUtil.currentDate(ddMMyyyy) + ".xlsx";

            log.info("file Remote path is {}",REMOTEFILEPATH);
        }
        log.info("Worldline KVB Scheduler File Path --{}",REMOTEFILEPATH);
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
                    File deleteIfExists = new File(LOCALFILEPATH_KVB);
                    if (deleteIfExists.exists()) {
                        FileUtils.deleteDirectory(deleteIfExists);
                    }
                    if (!deleteIfExists.exists()) {
                        deleteIfExists.mkdir();
                    }


                    final String givenDate = date.isPresent() ? DateUtil.longToDate(date.get(), ddMMyyyy) : DateUtil.currentDate(ddMMyyyy);
                    boolean downloadStatus = downloadFile(REMOTEFILEPATH, LOCALFILEPATH_KVB +
                            BIJLIPAY_MIS_XLSX_KVB + givenDate + ".xlsx");
                    if (downloadStatus) {
                        status = true;
                    }
                } else {
                    mailHandler.sendSFTPFileNotFoundMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA),"KVB");
                    log.info("SFTP File Not Found!");
                    return status;
                }
            } else {
                mailHandler.sendSFTPFailedMail(DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA),"KVB");
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

    @Override
    public boolean createUpdatedExcel(String bijlipayMisXlsx, String date) {
        boolean FIRSTROW = true;
        boolean updated = false;
        FileInputStream file = null;
        Workbook wb = null;
        List<KVBDTo> kvbDTOS = new ArrayList<>();
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
                KVBDTo misTable = new KVBDTo();
                int cellValue = 0;

                misTable.setTxnDate(DateUtil.convertStringToDate(row.getCell(cellValue++).getStringCellValue(), "dd-MM-yy"));
                misTable.setMid((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setTid((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setMerchantName((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setTradename((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setCity((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardNo((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardType((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setrRN((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setAssociationName((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setAuthCode(((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue()));
                misTable.setBatchCode((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                try {
                    misTable.setTxnamount((float) row.getCell(cellValue++).getNumericCellValue());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    misTable.setMdramount(((float) row.getCell(cellValue++).getNumericCellValue()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    misTable.setGstamount((float) row.getCell(cellValue++).getNumericCellValue());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    misTable.setNetamount((float) row.getCell(cellValue++).getNumericCellValue());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                misTable.setPaymentdate(DateUtil.convertStringToDate(row.getCell(cellValue++).getStringCellValue(), "dd-MM-yy"));
                misTable.setAggregatorName((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardType1((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                misTable.setCardProduct((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
                try {
                    misTable.setAssocationcost((float) row.getCell(cellValue++).getNumericCellValue());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                misTable.setTransactiontype((row.getCell(cellValue).getStringCellValue().isEmpty() || row.getCell(cellValue).getStringCellValue().equals(null)) ? " " : row.getCell(cellValue++).getStringCellValue());
//                        .equalsIgnoreCase("SALE") ? "POS" : row.getCell(cellValue++).getStringCellValue());
//                misTable.setMonth(DateUtil.currentDate(MMM_yy));
                misTable.setDate(date);
                kvbDTOS.add(misTable);
            }wb.close();
            file.close();
        }catch (IOException e){
            System.out.println("IOException reading KVB Excel File "+e.getMessage());
        }catch (Exception e){
            System.out.println("Error reading KVB Excel File "+e.getMessage());
            System.gc();
        }
        try {  // TODO Create KVB file directly using smb
            CreateExcelView.createKVBReportForMerChant(kvbDTOS, date);
            boolean twoDayBefore = kvbService.findByCreatedAt(date);
            if (!twoDayBefore) {
                log.info("data inserted for the choosed date {}", date);
                kvBtoMarsRepository.saveAll(kvbDTOS);
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
        boolean result = kvBtoMarsRepository.countBydate(oneDayBefore) > 0;
        return result;
    }




}

