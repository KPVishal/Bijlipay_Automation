package com.bijlipay.Utils;


import com.bijlipay.Model.DTO.*;
import com.bijlipay.Model.FederalDTO;
import com.bijlipay.Model.SibReport;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bijlipay.Utils.Constants.*;
import static org.apache.poi.ss.util.CellUtil.FONT;

@Service
public class CreateExcelView {

    private static final Logger log = LoggerFactory.getLogger(CreateExcelView.class);

    private static final String COMMA_DELIMITER = "|";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public static void createKVBReportForMerChant(List<KVBDTo> result, String date) throws IOException {
        DecimalFormat df = new DecimalFormat("####0.0000");

//        File file = new File(MERCHANT_FILE_DIRECTORY_KVB);
//        if (!file.exists()) {
//            if (file.mkdir()) {
//            } else {
//                log.info("Failed to create directory!");
//            }
//        }
//        // TODO below file was internal usage operation team needed
//        FileOutputStream fileOutForInternalUsage =
//                new FileOutputStream(MERCHANT_FILE_DIRECTORY_KVB +
//                        FILENAME_KVBtoMARS + date + ".xlsx");

//         TODO below file was upload in MARS fir
        NtlmPasswordAuthentication auth =
                new NtlmPasswordAuthentication(SMB_FEDERAL_DOMAIN, SMB_FEDERAL_USERNAME, SMB_FEDERAL_PASSWORD);
        SmbFile insert =
                new SmbFile(MARS_FILE_PATH_KVB + FILENAME_KVBtoMARS + date + ".xlsx", auth);
//        File insert =
//                new File(MARS_FILE_PATH + FILENAME_FEDERAL + date + ".xlsx");

        SmbFileOutputStream fileOut = (SmbFileOutputStream) insert.getOutputStream();
//        FileOutputStream fileOut = new FileOutputStream(insert);
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook());
        SXSSFSheet sheet = workbook.createSheet("00009_KVB - BIJLIPAY_MIS_REPORT");
//        SXSSFSheet sheet = workbook.createSheet("FED_TXN_Testing");
//        SXSSFSheet sheet = workbook.createSheet("FED_TXN");
        sheet.setDefaultColumnWidth(15);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(FONT);
        font.setBold(true);
        style.setFont(font);

        DataFormat fmt = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(fmt.getFormat("@"));

        Row header = sheet.createRow(0);

        for (int col = 0; col < HEADERS_KVB.length; col++) {
            Cell cell = header.createCell(col);
            cell.setCellValue(HEADERS_KVB[col]);
            cell.setCellStyle(style);
        }
        int rowCount = 1;

        for (KVBDTo output : result) {
            Row createRow = sheet.createRow(rowCount++);
            int col = 0;

            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(String.valueOf(DateUtil.formatForTimeOnly(output.getTxnDate(), DD_MM_YY)));
            createRow.createCell(col++).setCellValue(output.getMid());
            createRow.createCell(col++).setCellValue(output.getTid());
            createRow.createCell(col++).setCellValue(output.getMerchantName());
            createRow.createCell(col++).setCellValue(output.getTradename());
            createRow.createCell(col++).setCellValue(output.getCity());
            createRow.createCell(col++).setCellValue(output.getCardNo());
            createRow.createCell(col++).setCellValue(output.getCardType());
            createRow.createCell(col++).setCellValue(output.getrRN());
            createRow.createCell(col++).setCellValue(output.getAssociationName());
            createRow.createCell(col++).setCellValue(output.getAuthCode());
            createRow.createCell(col++).setCellValue(output.getBatchCode());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMdramount())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGstamount())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), DD_MM_YY));
            createRow.createCell(col++).setCellValue(output.getAggregatorName());
//            createRow.createCell(col++).setCellValue(output.getTradename());
//            createRow.createCell(col++).setCellValue(output.getTerminalSerialNo());
            createRow.createCell(col++).setCellValue(output.getCardType1());
            createRow.createCell(col++).setCellValue(output.getCardProduct());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getAssocationcost())));
            createRow.createCell(col++).setCellValue(output.getTransactiontype());
//                    .equalsIgnoreCase("SALE") ? "POS" :
//                    output.getTransactiontype());
//            createRow.createCell(col++).setCellValue(output.getCardProduct());
        }
        try {
            System.out.println("Start to copy File");
            workbook.write(fileOut);
            System.out.println("File Copied to SMB");
            fileOut.flush();
            fileOut.close();
            System.out.println("SMB File Closed");
//            System.out.println("Start to copy InternalUsage");
//            workbook.write(fileOutForInternalUsage);
//            System.out.println("File copied to ForInternalUsage");
//            fileOutForInternalUsage.flush();
//            fileOutForInternalUsage.close();
//            System.out.println("Internal File Closed");
            workbook.close();
            System.out.println("Closed");
        } catch (Exception e) {
            System.out.println("Error coping file " + e.getMessage());

        }
    }


    public static void createFederalReportForMerChant(List<FederalDTO> result, String date) throws IOException {
        DecimalFormat df = new DecimalFormat("####0.0000");

//        File file = new File(MERCHANT_FILE_DIRECTORY_FEDERAL);
//        if (!file.exists()) {
//            if (file.mkdir()) {
//            } else {
//                log.info("Failed to create directory!");
//            }
//        }
//        // TODO below file was internal usage operation team needed
//        FileOutputStream fileOutForInternalUsage =
//                new FileOutputStream(MERCHANT_FILE_DIRECTORY_FEDERAL +
//                        FILENAME_FEDERAL + date + ".xlsx");

//         TODO below file was upload in MARS fir
        NtlmPasswordAuthentication auth =
                new NtlmPasswordAuthentication(SMB_FEDERAL_DOMAIN, SMB_FEDERAL_USERNAME, SMB_FEDERAL_PASSWORD);
        SmbFile insert =
                new SmbFile(MARS_FILE_PATH + FILENAME_FEDERAL + date + ".xlsx", auth);
//        File insert =
//                new File(MARS_FILE_PATH + FILENAME_FEDERAL + date + ".xlsx");

        SmbFileOutputStream fileOut = (SmbFileOutputStream) insert.getOutputStream();
//        FileOutputStream fileOut = new FileOutputStream(insert);
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook());
        SXSSFSheet sheet = workbook.createSheet("FED_TXN");
//        SXSSFSheet sheet = workbook.createSheet("FED_TXN_Testing");
//        SXSSFSheet sheet = workbook.createSheet("FED_TXN");
        sheet.setDefaultColumnWidth(15);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(FONT);
        font.setBold(true);
        style.setFont(font);

        DataFormat fmt = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(fmt.getFormat("@"));

        Row header = sheet.createRow(0);

        for (int col = 0; col < HEADERS_FEDERAL.length; col++) {
            Cell cell = header.createCell(col);
            cell.setCellValue(HEADERS_FEDERAL[col]);
            cell.setCellStyle(style);
        }
        int rowCount = 1;

        for (FederalDTO output : result) {
            Row createRow = sheet.createRow(rowCount++);
            int col = 0;

            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(String.valueOf(DateUtil.formatForTimeOnly(output.getTxnDate(), DD_MM_YYYY_HH_MM_SS_AA)));
            createRow.createCell(col++).setCellValue(output.getMid());
            createRow.createCell(col++).setCellValue(output.getTid());
            createRow.createCell(col++).setCellValue(output.getMerchantName());
            createRow.createCell(col++).setCellValue(output.getCity());
            createRow.createCell(col++).setCellValue(output.getCardNo());
            createRow.createCell(col++).setCellValue(output.getCardType());
            createRow.createCell(col++).setCellValue(output.getrRN());
            createRow.createCell(col++).setCellValue(output.getAssociationName());
            createRow.createCell(col++).setCellValue(output.getApprovalCode());
            createRow.createCell(col++).setCellValue(output.getBatchCode());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), DD_MM_YYYY_HH_MM_SS_AA));
            createRow.createCell(col++).setCellValue(output.getAggregatorName());
            createRow.createCell(col++).setCellValue(output.getTradename());
            createRow.createCell(col++).setCellValue(output.getTerminalSerialNo());
            createRow.createCell(col++).setCellValue(output.getCardType1());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getAssocationFee())));
            createRow.createCell(col++).setCellValue(output.getNarration().equalsIgnoreCase("SALE") ? "POS" :
                    output.getNarration());
            createRow.createCell(col++).setCellValue(output.getCardProduct());
        }
        try {
            System.out.println("Start to copy File");
            workbook.write(fileOut);
            System.out.println("File Copied to SMB");
            fileOut.flush();
            fileOut.close();
            System.out.println("SMB File Closed");
//            System.out.println("Start to copy InternalUsage");
//            workbook.write(fileOutForInternalUsage);
//            System.out.println("File copied to ForInternalUsage");
//            fileOutForInternalUsage.flush();
//            fileOutForInternalUsage.close();
//            System.out.println("Internal File Closed");
            workbook.close();
            System.out.println("Closed");
        } catch (Exception e) {
            System.out.println("Error coping file " + e.getMessage());

        }
    }

    public static void createAxisReportForMerChant(List<AxisDTO> result, String date) throws IOException {
        DecimalFormat df = new DecimalFormat("####0.0000");

//        File file = new File(MERCHANT_FILE_DIRECTORY_FEDERAL);
//        if (!file.exists()) {
//            if (file.mkdir()) {
//            } else {
//                log.info("Failed to create directory!");
//            }
//        }
//        // TODO below file was internal usage operation team needed
//        FileOutputStream fileOutForInternalUsage =
//                new FileOutputStream(MERCHANT_FILE_DIRECTORY_FEDERAL +
//                        FILENAME_FEDERAL + date + ".xlsx");

//         TODO below file was upload in MARS fir

        // ========= The Ip was changed then it get Error Connection rested  so we go for local path to masr path ==========

//        NtlmPasswordAuthentication auth =
//                new NtlmPasswordAuthentication(SMB_AXIS_DOMAIN, SMB_AXIS_USERNAME, SMB_AXIS_PASSWORD);
//        SmbFile insert =
//                new SmbFile(MARS_FILE_PATH_AXIS + FILENAME_AXIS + date + ".xlsx", auth);
//        File insert =
//                new File(MARS_FILE_PATH + FILENAME_FEDERAL + date + ".xlsx");


             // ========= Error Connection rested =========
//        log.info("inserting it to smb path for Axis--");
//        SmbFileOutputStream fileOut = (SmbFileOutputStream) insert.getOutputStream();
//        log.info("file inserted into smb path For Axis --");
//        FileOutputStream fileOut = new FileOutputStream(insert);

     // paths
        String localFileName = FILENAME_AXIS + date + ".xlsx";
        String localFilePath = LOCALFILEPATH_AXIS + localFileName;

        // Ensure local directory exists before writing
        File localDir = new File(LOCALFILEPATH_AXIS);
        if (!localDir.exists()) {
            boolean created = localDir.mkdirs();
            log.info("Local directory created: --->{} , ----> {}", LOCALFILEPATH_AXIS, created);
        }

        // write the excels report

        log.info(" ----- To write the excel report ----");
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook());
        SXSSFSheet sheet = workbook.createSheet("Bijlipay_Aggr_Report_8_");
        sheet.setDefaultColumnWidth(15);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(FONT);
        font.setBold(true);
        style.setFont(font);

        DataFormat fmt = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(fmt.getFormat("@"));

        Row header = sheet.createRow(0);

        for (int col = 0; col < HEADERS_AXIS.length; col++) {
            Cell cell = header.createCell(col);
            cell.setCellValue(HEADERS_AXIS[col]);
            cell.setCellStyle(style);
        }
        int rowCount = 1;

        for (AxisDTO output : result) {
            Row createRow = sheet.createRow(rowCount++);
            int col = 0;
            createRow.createCell(col++).setCellValue(output.getMid());
//            log.info("mid is :{}",output.getMid());
            createRow.createCell(col++).setCellValue(output.getTid());
//            log.info("tid is {}",output.getTid());
            createRow.createCell(col++).setCellValue(output.getMerchantName());
//            log.info("merchant name is {}",output.getMerchantName());
            createRow.createCell(col++).setCellValue(output.getCardNo());
//            log.info("card no is {}",output.getCardNo());
            createRow.createCell(col++).setCellValue(output.getTransactionType());
//            log.info("transaction type {}",output.getTransactionType());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
//            log.info("txn amnt {}",output.getTxnamount());
            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(String.valueOf(DateUtil.formatForTimeOnly(output.getTxnDate(), ddMMyyyy)));
//            log.info("txn date {}",output.getTxnDate());
//            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.createCell(col++).setCellValue(output.getTxnTime());
//            log.info("txn time {}",output.getTxnTime());
            createRow.createCell(col++).setCellValue(output.getrRN());
//            log.info("rrn {}",output.getrRN());
//            createRow.createCell(col++).setCellValue(output.getAssociationName());
            createRow.createCell(col++).setCellValue(output.getApprovalCode());
//            log.info("approval code{}",output.getApprovalCode());
            createRow.createCell(col++).setCellValue(output.getInterchangeType());
//            log.info("interchange type {}",output.getInterchangeType());
            createRow.createCell(col++).setCellValue(output.getPosEntryMode());
//            log.info("pos entry mode",output.getPosEntryMode());
            createRow.createCell(col++).setCellValue(output.getCreditDebitFlag());
//            log.info("credit debit flag {}",output.getCreditDebitFlag());
            createRow.createCell(col++).setCellValue(output.getStan());
//            log.info("stan {}",output.getStan());
            createRow.createCell(col++).setCellValue(output.getTransactionCurrencyCode());
//            log.info("transaction currency code",output.getTransactionCurrencyCode());
            createRow.createCell(col++).setCellValue(output.getCardType());
//            log.info("card type ---{}",output.getCardType());
            createRow.createCell(col++).setCellValue(output.getCardTypeDesc());
//            log.info("card type desc -----{}",output.getCardTypeDesc());
        }


        // This for SMB file wirte and close ....
//        try {
//            log.info("Start to copy File");
//            workbook.write(fileOut);
//            log.info("File Copied to SMB");
//            fileOut.flush();
//            fileOut.close();
//            log.info("SMB File Closed");
//            workbook.close();
//            log.info("Closed");
//        } catch (Exception e) {
//            log.info("Error coping axis aggr report file " + e.getMessage());
//        }

           //Write workbook to local file

        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            workbook.write(fos);
            workbook.close();
            log.info("Workbook written to local path: ------>{}", localFilePath);
        } catch (Exception e) {
            log.info("Error writing workbook to local file: {}", e.getMessage());
            e.printStackTrace();
            return;
        }


        // Copy local file to MARS path
        try {
            Path source = Paths.get(localFilePath);
            if (!Files.exists(source)) {
                log.info("Source file does not exist at local path: ----->{}", localFilePath);
                return;
            }

            Path marsDir = Paths.get(MARS_FILE_PATH_AXIS);
            if (!Files.exists(marsDir)) {
                log.info("MARS directory does not exist, attempting to create: ---->{}", MARS_FILE_PATH_AXIS);
                Files.createDirectories(marsDir);
            }

            if (!Files.isWritable(marsDir)) {
                log.info("MARS path is not writable: --->{}. Please check if the mount is active and permissions are correct.", MARS_FILE_PATH_AXIS);
                return;
            }

            Path dest = Paths.get(MARS_FILE_PATH_AXIS, localFileName);
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            log.info("File copied to MARS path: ---->{}", dest);
        } catch (Exception e) {
            log.info("Error copying file to MARS path:--->{} | Exception:---->{}", MARS_FILE_PATH_AXIS + localFileName, e.getMessage(), e);
            e.printStackTrace();
        }


    }
    public Workbook createManualFederalReportForMerChant(List<FederalDTO> result) throws IOException {
        DecimalFormat df = new DecimalFormat("####0.0000");
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook());
        SXSSFSheet sheet = workbook.createSheet("FED_TXN");
        sheet.setDefaultColumnWidth(15);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(FONT);
        font.setBold(true);
        style.setFont(font);
        DataFormat fmt = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(fmt.getFormat("@"));
        Row header = sheet.createRow(0);

        for (int col = 0; col < HEADERS_FEDERAL.length; col++) {
            Cell cell = header.createCell(col);
            cell.setCellValue(HEADERS_FEDERAL[col]);
            cell.setCellStyle(style);
        }
        int rowCount = 1;

        for (FederalDTO output : result) {
            Row createRow = sheet.createRow(rowCount++);
            int col = 0;

            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(String.valueOf(DateUtil.formatForTimeOnly(output.getTxnDate(), DD_MM_YYYY_HH_MM_SS_AA)));
            createRow.createCell(col++).setCellValue(output.getMid());
            createRow.createCell(col++).setCellValue(output.getTid());
            createRow.createCell(col++).setCellValue(output.getMerchantName());
            createRow.createCell(col++).setCellValue(output.getCity());
            createRow.createCell(col++).setCellValue(output.getCardNo());
            createRow.createCell(col++).setCellValue(output.getCardType());
            createRow.createCell(col++).setCellValue(output.getrRN());
            createRow.createCell(col++).setCellValue(output.getAssociationName());
            createRow.createCell(col++).setCellValue(output.getApprovalCode());
            createRow.createCell(col++).setCellValue(output.getBatchCode());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), DD_MM_YYYY_HH_MM_SS_AA));
            createRow.createCell(col++).setCellValue(output.getAggregatorName());
            createRow.createCell(col++).setCellValue(output.getTradename());
            createRow.createCell(col++).setCellValue(output.getTerminalSerialNo());
            createRow.createCell(col++).setCellValue(output.getCardType1());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getAssocationFee())));
            createRow.createCell(col++).setCellValue(output.getNarration().equalsIgnoreCase("SALE") ? "POS" :
                    output.getNarration());
            createRow.createCell(col++).setCellValue(output.getCardProduct());

        }
        return workbook;
    }
    public Workbook createManualSIBReportForMerChant(List<SibReport> result) throws IOException {
        DecimalFormat df = new DecimalFormat("####0.0000");
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook());
        SXSSFSheet sheet = workbook.createSheet("SIB_TXN");
        sheet.setDefaultColumnWidth(15);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(FONT);
        font.setBold(true);
        style.setFont(font);

        DataFormat fmt = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(fmt.getFormat("@"));

        Row row = sheet.createRow(0);

        for (int col = 0; col < HEADERS_FEDERAL.length; col++) {
            Cell cell = row.createCell(col);
            cell.setCellValue(HEADERS_FEDERAL[col]);
            cell.setCellStyle(style);
        }
        int rowCount = 1;

        for (SibReport output : result) {
            Row createRow = sheet.createRow(rowCount++);
            int col = 0;

            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(String.valueOf(DateUtil.formatForTimeOnly(output.getTxnDate(), DD_MM_YYYY_HH_MM_SS_AA)));
            createRow.createCell(col++).setCellValue(output.getMid());
            createRow.createCell(col++).setCellValue(output.getTid());
            createRow.createCell(col++).setCellValue(output.getMerchantName());
            createRow.createCell(col++).setCellValue(output.getCity());
            createRow.createCell(col++).setCellValue(output.getCardNo());
            createRow.createCell(col++).setCellValue(output.getCardType());
            createRow.createCell(col++).setCellValue(output.getrRN());
            createRow.createCell(col++).setCellValue(output.getAssociationName());
            createRow.createCell(col++).setCellValue(output.getApprovalCode());
            createRow.createCell(col++).setCellValue(output.getBatchCode());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), DD_MM_YYYY_HH_MM_SS_AA));
            createRow.createCell(col++).setCellValue(output.getAggregatorName());
            createRow.createCell(col++).setCellValue(output.getTradename());
            createRow.createCell(col++).setCellValue(output.getTerminalSerialNo());
            createRow.createCell(col++).setCellValue(output.getCardType1());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getAssocationFee())));
            createRow.createCell(col++).setCellValue(output.getNarration().equalsIgnoreCase("SALE") ? "POS" :
                    output.getNarration());
            createRow.createCell(col++).setCellValue(output.getCardProduct());
        }
      return workbook;
    }

    public static void createSIBReportForMerChant(List<SibReport> result, String date) throws IOException {
        DecimalFormat df = new DecimalFormat("####0.0000");

//         TODO below file was upload in MARS fir
        NtlmPasswordAuthentication auth =
                new NtlmPasswordAuthentication(SMB_FEDERAL_DOMAIN, SMB_FEDERAL_USERNAME, SMB_FEDERAL_PASSWORD);
        SmbFile insert =
                new SmbFile(MARS_SIB_FILE_PATH + MARS_SIB_FILENAME + date + ".xlsx", auth);
//        File insert = new File(MARS_SIB_FILE_PATH + MARS_SIB_FILENAME + date + ".xlsx");
        SmbFileOutputStream fileOut = (SmbFileOutputStream) insert.getOutputStream();
//        FileOutputStream fileOut = new FileOutputStream(insert);

        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook());
        SXSSFSheet sheet = workbook.createSheet("FED_TXN");
        sheet.setDefaultColumnWidth(15);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(FONT);
        font.setBold(true);
        style.setFont(font);

        DataFormat fmt = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(fmt.getFormat("@"));

        Row header = sheet.createRow(0);

        for (int col = 0; col < HEADERS_FEDERAL.length; col++) {
            Cell cell = header.createCell(col);
            cell.setCellValue(HEADERS_FEDERAL[col]);
            cell.setCellStyle(style);
        }
        int rowCount = 1;

        for (SibReport output : result) {
            Row createRow = sheet.createRow(rowCount++);
            int col = 0;

            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(String.valueOf(DateUtil.formatForTimeOnly(output.getTxnDate(), DD_MM_YYYY_HH_MM_SS_AA)));
            createRow.createCell(col++).setCellValue(output.getMid());
            createRow.createCell(col++).setCellValue(output.getTid());
            createRow.createCell(col++).setCellValue(output.getMerchantName());
            createRow.createCell(col++).setCellValue(output.getCity());
            createRow.createCell(col++).setCellValue(output.getCardNo());
            createRow.createCell(col++).setCellValue(output.getCardType());
            createRow.createCell(col++).setCellValue(output.getrRN());
            createRow.createCell(col++).setCellValue(output.getAssociationName());
            createRow.createCell(col++).setCellValue(output.getApprovalCode());
            createRow.createCell(col++).setCellValue(output.getBatchCode());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
            createRow.createCell(col).setCellStyle(cellStyle);
            createRow.getCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), DD_MM_YYYY_HH_MM_SS_AA));
            createRow.createCell(col++).setCellValue(output.getAggregatorName());
            createRow.createCell(col++).setCellValue(output.getTradename());
            createRow.createCell(col++).setCellValue(output.getTerminalSerialNo());
            createRow.createCell(col++).setCellValue(output.getCardType1());
            createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getAssocationFee())));
            createRow.createCell(col++).setCellValue(output.getNarration().equalsIgnoreCase("SALE") ? "POS" :
                    output.getNarration());
            createRow.createCell(col++).setCellValue(output.getCardProduct());
        }
        try {
            System.out.println("Start SIB to copy File");
            workbook.write(fileOut);
            System.out.println("SIB File Copied to SMB");
            fileOut.flush();
            fileOut.close();
            System.out.println("SMB File Closed");
            workbook.close();
            System.out.println("Closed");
        } catch (Exception e) {
            System.out.println("Error coping file SIB " + e.getMessage());

        }
    }

    public static void createMisReportForMerChants(List<SwitchTransactionObject> result, String merchantName, int format) throws IOException {
        Workbook workbook = null;
        FileOutputStream fileOut = null;
        DecimalFormat df = new DecimalFormat("####0.00");
        File file = new File(MERCHANT_FILE_DIRECTORY_MIS);
        if (!file.exists()) {
            if (file.mkdir()) {
                log.info("Directory is created!");
            } else {
                log.info("Failed to create directory!");
            }
        }
        try {
            fileOut =
                    new FileOutputStream(MERCHANT_FILE_DIRECTORY_MIS + merchantName + "_" + DateUtil.currentDate(
                            yyyy_MM_dd) +
                            ".xlsx");

            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(merchantName);
            sheet.setDefaultColumnWidth(15);

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName(FONT);
            font.setBold(true);
            style.setFont(font);

            CellStyle txnDateFormat = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            txnDateFormat.setDataFormat(
                    createHelper.createDataFormat().getFormat("dd/MM/yy hh:mm:ss "));

            CellStyle paymentDateFormat = workbook.createCellStyle();
            CreationHelper helper = workbook.getCreationHelper();
            paymentDateFormat.setDataFormat(
                    helper.createDataFormat().getFormat("dd-MMM-yyyy"));

            CellStyle format5Merchant = workbook.createCellStyle();
            CreationHelper format5Merchanthelper = workbook.getCreationHelper();
            format5Merchant.setDataFormat(
                    format5Merchanthelper.createDataFormat().getFormat("dd-MMM-yyyy"));

//            CellStyle txnDateANDTimeFormat = workbook.createCellStyle();
//            CreationHelper creationHelper = workbook.getCreationHelper();
//            txnDateANDTimeFormat.setDataFormat(
//                    creationHelper.createDataFormat().getFormat("hh:mm:ss.SSS"));
            if (format == FORMAT_11) {

                Row header = sheet.createRow(0);
                for (int col = 0; col < ELEVEN.length; col++) {
                    Cell cell = header.createCell(col);
                    cell.setCellValue(ELEVEN[col]);
                    cell.setCellStyle(style);
                }
                int rowCount = 1;

                for (SwitchTransactionObject output : result) {
                    Row createRow = sheet.createRow(rowCount++);
                    int col = 0;

                    createRow.createCell(col++).setCellValue(output.getMti());
                    createRow.createCell(col++).setCellValue(output.getTxnType());
                    createRow.createCell(col++).setCellValue(output.getTerminalId());
                    createRow.createCell(col++).setCellValue(output.getMerchantId());
                    createRow.createCell(col++).setCellValue(output.getTxnDate());
                    createRow.createCell(col++).setCellValue(output.getTxnTime());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnAmount())));
                    createRow.createCell(col++).setCellValue(output.getTxnResponseCode());
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getResponseReceivedTime(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getRrnNumber());
                    createRow.createCell(col++).setCellValue(output.getStan());
                    createRow.createCell(col++).setCellValue(output.getInvoiceNumber());
                    createRow.createCell(col++).setCellValue(output.getBatchNumber());
                    createRow.createCell(col++).setCellValue(output.getUrn());

                }
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }

            workbook.close();
        } catch (Exception e) {
            log.info("Error on Excel Creation " + merchantName + " " + e.getMessage());
            e.printStackTrace();

        }
    }


//    ==============================================================================================================================

    public static void createMisReportForMerChant(List<MisObject> result, String merchantName, int format) throws IOException {
        Workbook workbook = null;
        FileOutputStream fileOut = null;
        DecimalFormat df = new DecimalFormat("####0.00");
        File file = new File(MERCHANT_FILE_DIRECTORY_MIS);
        if (!file.exists()) {
            if (file.mkdir()) {
                log.info("Directory is created!");
            } else {
                log.info("Failed to create directory!");
            }
        }
        try {
            fileOut =
                    new FileOutputStream(MERCHANT_FILE_DIRECTORY_MIS + merchantName + "_" + DateUtil.currentDate(
                            yyyy_MM_dd) +
                            ".xlsx");

            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(merchantName);
            sheet.setDefaultColumnWidth(15);

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName(FONT);
            font.setBold(true);
            style.setFont(font);

            CellStyle txnDateFormat = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            txnDateFormat.setDataFormat(
                    createHelper.createDataFormat().getFormat("dd/MM/yy hh:mm:ss "));

            CellStyle paymentDateFormat = workbook.createCellStyle();
            CreationHelper helper = workbook.getCreationHelper();
            paymentDateFormat.setDataFormat(
                    helper.createDataFormat().getFormat("dd-MMM-yyyy"));

            CellStyle format5Merchant = workbook.createCellStyle();
            CreationHelper format5Merchanthelper = workbook.getCreationHelper();
            format5Merchant.setDataFormat(
                    format5Merchanthelper.createDataFormat().getFormat("dd-MMM-yyyy"));

//            CellStyle txnDateANDTimeFormat = workbook.createCellStyle();
//            CreationHelper creationHelper = workbook.getCreationHelper();
//            txnDateANDTimeFormat.setDataFormat(
//                    creationHelper.createDataFormat().getFormat("hh:mm:ss.SSS"));

            if (format == FORMAT_1 || format == FORMAT_7) {
                Row header = sheet.createRow(0);
                for (int col = 0; col < ONE_AND_SEVEN.length; col++) {
                    Cell cell = header.createCell(col);
                    cell.setCellValue(ONE_AND_SEVEN[col]);
                    cell.setCellStyle(style);
                }
                int rowCount = 1;

                for (MisObject output : result) {
                    Row createRow = sheet.createRow(rowCount++);
                    int col = 0;
//                    createRow.createCell(col).setCellStyle(txnDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnDate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getMid());
                    createRow.createCell(col++).setCellValue(output.getTid());
                    createRow.createCell(col++).setCellValue(output.getMerchantName());
                    createRow.createCell(col++).setCellValue(output.getCity());
                    createRow.createCell(col++).setCellValue(output.getCardNo());
                    createRow.createCell(col++).setCellValue(output.getCardType());
                    createRow.createCell(col++).setCellValue(output.getrRN());
                    createRow.createCell(col++).setCellValue(output.getAssociationName());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
                    createRow.createCell(col++).setCellValue(output.getCardType1());
                    createRow.createCell(col++).setCellValue(output.getTradename());
//                    createRow.createCell(col).setCellStyle(paymentDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getNarration());
                }
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } else if (format == FORMAT_2) {

                Row header = sheet.createRow(0);

                for (int col = 0; col < TWO.length; col++) {
                    Cell cell = header.createCell(col);
                    cell.setCellValue(TWO[col]);
                    cell.setCellStyle(style);
                }
                int rowCount = 1;
                for (MisObject output : result) {
                    Row createRow = sheet.createRow(rowCount++);
                    int col = 0;
                    //  createRow.createCell(col).setCellStyle(txnDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnDate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getMid());
                    createRow.createCell(col++).setCellValue(output.getTid());
                    createRow.createCell(col++).setCellValue(output.getMerchantName());
                    createRow.createCell(col++).setCellValue(output.getCity());
                    createRow.createCell(col++).setCellValue(output.getCardNo());
                    createRow.createCell(col++).setCellValue(output.getCardType());
                    createRow.createCell(col++).setCellValue(output.getrRN());
                    createRow.createCell(col++).setCellValue(output.getAssociationName());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
                    createRow.createCell(col++).setCellValue(output.getCardType1());
                    createRow.createCell(col++).setCellValue(output.getTradename());
                    createRow.createCell(col++).setCellValue(output.getLocation());
                    //  createRow.createCell(col).setCellStyle(paymentDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getNarration());
                }
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } else if (format == FORMAT_3) {

                Row header = sheet.createRow(0);
                for (int col = 0; col < THREE.length; col++) {
                    Cell cell = header.createCell(col);
                    cell.setCellValue(THREE[col]);
                    cell.setCellStyle(style);
                }
                int rowCount = 1;
                for (MisObject output : result) {
                    Row createRow = sheet.createRow(rowCount++);
                    int col = 0;
                    //  createRow.createCell(col).setCellStyle(txnDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnDate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getMid());
                    createRow.createCell(col++).setCellValue(output.getTid());
                    createRow.createCell(col++).setCellValue(output.getMerchantName());
                    createRow.createCell(col++).setCellValue(output.getCity());
                    createRow.createCell(col++).setCellValue(output.getCardNo());
                    createRow.createCell(col++).setCellValue(output.getCardType());
                    createRow.createCell(col++).setCellValue(output.getrRN());
                    createRow.createCell(col++).setCellValue(output.getAssociationName());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
                    createRow.createCell(col++).setCellValue(output.getCardType1());
                    createRow.createCell(col++).setCellValue(output.getTradename());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMdr_percentage())));
                    //  createRow.createCell(col).setCellStyle(paymentDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getNarration());
                }
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } else if (format == FORMAT_4) {

                Row header = sheet.createRow(0);

                for (int col = 0; col < FOUR.length; col++) {
                    Cell cell = header.createCell(col);
                    cell.setCellValue(FOUR[col]);
                    cell.setCellStyle(style);
                }
                int rowCount = 1;
                for (MisObject output : result) {
                    Row createRow = sheet.createRow(rowCount++);
                    int col = 0;
                    // createRow.createCell(col).setCellStyle(txnDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnDate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getMid());
                    createRow.createCell(col++).setCellValue(output.getTid());
                    createRow.createCell(col++).setCellValue(output.getMerchantName());
                    createRow.createCell(col++).setCellValue(output.getCity());
                    createRow.createCell(col++).setCellValue(output.getState());
                    createRow.createCell(col++).setCellValue(output.getCardNo());
                    createRow.createCell(col++).setCellValue(output.getCardType());
                    createRow.createCell(col++).setCellValue(output.getrRN());
                    createRow.createCell(col++).setCellValue(output.getAssociationName());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
                    createRow.createCell(col++).setCellValue(output.getCardType1());
                    createRow.createCell(col++).setCellValue(output.getTradename());
                    //  createRow.createCell(col).setCellStyle(paymentDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getNarration());
                }
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } else if (format == FORMAT_5) {

                Row header = sheet.createRow(0);

                for (int col = 0; col < FIVE_AND_TEN.length; col++) {
                    Cell cell = header.createCell(col);
                    cell.setCellValue(FIVE_AND_TEN[col]);
                    cell.setCellStyle(style);
                }
                int rowCount = 1;
                for (MisObject output : result) {
                    Row createRow = sheet.createRow(rowCount++);
                    int col = 0;
//                    createRow.createCell(col).setCellStyle(format5Merchant);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnDate(), dd_MMM_yyyy));
//                    createRow.createCell(col).setCellStyle(txnDateANDTimeFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnTime(), HH_mm_ss_SSS));
                    createRow.createCell(col++).setCellValue(output.getMid());
                    createRow.createCell(col++).setCellValue(output.getTid());
                    createRow.createCell(col++).setCellValue(output.getMerchantName());
                    createRow.createCell(col++).setCellValue(output.getCity());
                    createRow.createCell(col++).setCellValue(output.getCardNo());
                    createRow.createCell(col++).setCellValue(output.getCardType());
                    createRow.createCell(col++).setCellValue(output.getrRN());
                    createRow.createCell(col++).setCellValue(output.getAssociationName());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
                    createRow.createCell(col++).setCellValue(output.getCardType1());
                    createRow.createCell(col++).setCellValue(output.getTradename());
//                    createRow.createCell(col).setCellStyle(paymentDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getNarration());
                }
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } else if (format == FORMAT_6) {

                Row header = sheet.createRow(0);
                for (int col = 0; col < SIX.length; col++) {
                    Cell cell = header.createCell(col);
                    cell.setCellValue(SIX[col]);
                    cell.setCellStyle(style);
                }
                int rowCount = 1;
                for (MisObject output : result) {
                    Row createRow = sheet.createRow(rowCount++);
                    int col = 0;
                    //   createRow.createCell(col).setCellStyle(txnDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnDate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getMid());
                    createRow.createCell(col++).setCellValue(output.getTid());
                    createRow.createCell(col++).setCellValue(output.getMerchantName());
                    createRow.createCell(col++).setCellValue(output.getCity());
                    createRow.createCell(col++).setCellValue(output.getCardNo());
                    createRow.createCell(col++).setCellValue(output.getCardType());
                    createRow.createCell(col++).setCellValue(output.getrRN());
                    createRow.createCell(col++).setCellValue(output.getAssociationName());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
                    createRow.createCell(col++).setCellValue(output.getCardType1());
                    createRow.createCell(col++).setCellValue(output.getTradename());
//                    createRow.createCell(col).setCellStyle(paymentDateFormat);
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getNarration());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getIncentive())));
                }
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } else if (format == FORMAT_10) {
                Row header = sheet.createRow(0);
                for (int col = 0; col < FIVE_AND_TEN.length; col++) {
                    Cell cell = header.createCell(col);
                    cell.setCellValue(FIVE_AND_TEN[col]);
                    cell.setCellStyle(style);
                }
                int rowCount = 1;
                for (MisObject output : result) {
                    Row createRow = sheet.createRow(rowCount++);
                    int col = 0;
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnDate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getTxnTime(), HH_mm_ss));
                    createRow.createCell(col++).setCellValue(output.getMid());
                    createRow.createCell(col++).setCellValue(output.getTid());
                    createRow.createCell(col++).setCellValue(output.getMerchantName());
                    createRow.createCell(col++).setCellValue(output.getCity());
                    createRow.createCell(col++).setCellValue(output.getCardNo());
                    createRow.createCell(col++).setCellValue(output.getCardType());
                    createRow.createCell(col++).setCellValue(output.getrRN());
                    createRow.createCell(col++).setCellValue(output.getAssociationName());
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getTxnamount())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getMrlmdr())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getGst())));
                    createRow.createCell(col++).setCellValue(Double.valueOf(df.format(output.getNetamount())));
                    createRow.createCell(col++).setCellValue(output.getCardType1());
                    createRow.createCell(col++).setCellValue(output.getTradename());
                    createRow.createCell(col++).setCellValue(DateUtil.formatForTimeOnly(output.getPaymentdate(), dd_MMM_yyyy));
                    createRow.createCell(col++).setCellValue(output.getNarration());
                }
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }
            workbook.close();
        } catch (Exception e) {
            log.info("Error on Excel Creation " + merchantName + " " + e.getMessage());
            e.printStackTrace();

        }
    }

//    =================================================================================================================

    public static StringBuffer createCsvFileReport(List<FrmDataDTO> misObjects) {
        StringBuffer fileWriter = null;
        try {
            fileWriter = new StringBuffer();
            //Write a new IOB object list to the CSV file
            for (FrmDataDTO dataDTO : misObjects) {
                fileWriter.append(dataDTO.getEventType());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getEventSubType());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getEventName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getMsgBody());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getHost_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getSys_time());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getEvent_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getChannel());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getUser_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getUser_type());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getCust_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getDevice_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getIp_address());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getIp_country());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getIp_city());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getCust_name());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getSucc_fail_flg());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getError_code());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getError_desc());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getDr_account_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getTxn_amt());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getAvl_bal());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getCr_account_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getCr_ifsc_code());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getPayee_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getPayee_name());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getTran_type());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getEntry_mode());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getBranch_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getAcct_ownership());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getAcct_open_date());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getPart_tran_type());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getCountry_code());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getCust_card_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getBin());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getMcc_code());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getTerminal_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getMerchant_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getCust_mob_no());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getTxn_secured_flag());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getProduct_code());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getDev_owner_id());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getAtm_dom_limit());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getAtm_intr_limit());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getPos_ecom_dom_limit());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getPos_ecom_intr_limit());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getTran_date());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getMask_card_no());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getState_code());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getCard_type());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getTran_time());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getPos_entry_mode());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getInterbankcard());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(dataDTO.getDecline_type());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            log.info("CSV file was created successfully !!!");
        } catch (Exception e) {
            log.info("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }
        return fileWriter;
    }

//    ===================================================================================================================

    public static void createExcel(List<ClixMasterDTO> ex, ClixMasterDTO l2, String mename, String getname) throws IOException {
        File file = new File(LEAD_DIRECTORY + getname);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String date = simpleDateFormat.format(new Date());
        System.out.println(date);
        if (!file.exists()) {
            if (file.mkdir()) {
                log.info("Directory is created!");
            } else {
                log.info("Failed to create directory!");
            }
        }
        log.info("Directory Already Exists");
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            FileOutputStream fileOut = new FileOutputStream(file + "/" + mename + "_" + l2.getMid() + "_" + date +
                    ".xlsx");
            Sheet sheet = workbook.createSheet(mename);
            sheet.setDefaultColumnWidth(15);
            CellStyle style = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            style.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            Font font = workbook.createFont();
            font.setFontName(FONT);
            font.setBold(true);
//            font.setColor(HSSFColor.BLACK.index);
            style.setFont(font);
            style.setFont(font);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("SR N0");
            header.getCell(0).setCellStyle(style);
            header.createCell(1).setCellValue("MID");
            header.getCell(1).setCellStyle(style);
            header.createCell(2).setCellValue("TID");
            header.getCell(2).setCellStyle(style);
            header.createCell(3).setCellValue("ME NAME");
            header.getCell(3).setCellStyle(style);
            header.createCell(4).setCellValue("RRN");
            header.getCell(4).setCellStyle(style);
            header.createCell(5).setCellValue("TXN AMOUNT");
            header.getCell(5).setCellStyle(style);
            header.createCell(6).setCellValue("NET AMOUNT");
            header.getCell(6).setCellStyle(style);
            header.createCell(7).setCellValue("MDR");
            header.getCell(7).setCellStyle(style);
            header.createCell(8).setCellValue("GST");
            header.getCell(8).setCellStyle(style);
            int rowCount = 1;
            for (ClixMasterDTO l : ex) {
                Row createRow = sheet.createRow(rowCount);
                createRow.createCell(0).setCellValue(rowCount);
                System.out.println(rowCount++);
                createRow.createCell(1).setCellValue(l.getMid());
                System.out.println(l.getMid());
                createRow.createCell(2).setCellValue(l.getTid());
                System.out.println(l.getTid());
                createRow.createCell(3).setCellValue(l.getMeName());
                System.out.println(l.getMeName());
                createRow.createCell(4).setCellValue(String.valueOf(l.getRrn()));
                System.out.println(l.getRrn());
                createRow.createCell(5).setCellValue(l.getTxnamount());
                System.out.println(l.getTxnamount());
                createRow.createCell(6).setCellValue(l.getNetamount());
                System.out.println(l.getNetamount());
                createRow.createCell(7).setCellValue(l.getMdr());
                System.out.println(l.getMdr());
                createRow.createCell(8).setCellValue(l.getGst());
                System.out.println(l.getGst());
            }
            ++rowCount;

            Row createRow = sheet.createRow(rowCount);
            System.out.println(rowCount++);
            System.out.println(l2.getLoanAccountNumber());
            createRow.createCell(0).setCellValue("LOAN ACCOUNT NUMBER");
            createRow.getCell(0).setCellStyle(style);
            createRow.createCell(1).setCellValue(l2.getLoanAccountNumber());

            Row createRow1 = sheet.createRow(rowCount++);
            createRow1.createCell(0).setCellValue("TOTAL TRANSACTION VALUE");
            createRow1.getCell(0).setCellStyle(style);
            createRow1.createCell(1).setCellValue(l2.getTotalTransactionValue());
            System.out.println(l2.getTotalTransactionValue());

            Row createRow2 = sheet.createRow(rowCount++);
            createRow2.createCell(0).setCellValue("NET TRANSACTION VALUE");
            createRow2.getCell(0).setCellStyle(style);
            createRow2.createCell(1).setCellValue(l2.getNetTransactionValue());
            System.out.println(l2.getNetTransactionValue());

            Row createRow3 = sheet.createRow(rowCount++);
            createRow3.createCell(0).setCellValue("MERCHANT SHARE");
            createRow3.getCell(0).setCellStyle(style);
            createRow3.createCell(1).setCellValue(l2.getMerchantShare());
            System.out.println(l2.getMerchantShare());

            Row createRow4 = sheet.createRow(rowCount++);
            createRow4.createCell(0).setCellValue("CAPITAL SHARE");
            createRow4.getCell(0).setCellStyle(style);
            createRow4.createCell(1).setCellValue(l2.getClixCaptialShare());
            System.out.println(l2.getClixCaptialShare());

            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
            workbook.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Workbook createExcelForSelectQuery(ResultSet resultset) {

        Workbook workbook = null;
        try {

            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Test");
            sheet.setDefaultColumnWidth(15);

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName(FONT);
            font.setBold(true);
            style.setFont(font);


            final List<Object[]> data = new ArrayList<>();
            String[] columnNames = null;
            int columnCount = 0;

            try {
                final ResultSetMetaData metaData = resultset.getMetaData();
                columnCount = metaData.getColumnCount();
                columnNames = new String[columnCount];
                for (int column = 0; column < columnCount; column++) {
                    columnNames[column] = metaData.getColumnName(column + 1).toUpperCase();
                }

                while (resultset.next()) {
                    final Object[] row = new Object[columnCount];
                    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                        row[columnIndex] = resultset.getObject(columnIndex + 1);
                    }
                    data.add(row);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            Row header = sheet.createRow(0);
            for (int col = 0; col < columnNames.length; col++) {
                Cell cell = header.createCell(col);
                cell.setCellValue(columnNames[col].toUpperCase());
                cell.setCellStyle(style);
            }
            int rowCount = 1;

            for (Object[] obj : data) {
                Row body = sheet.createRow(rowCount++);
                for (int index = 0; index < obj.length; index++) {
                    body.createCell(index).setCellValue(obj[index].toString());
                }
            }
        } catch (Exception e) {
            log.info("Error on Excel Creation " + e.getMessage());
            e.printStackTrace();

        }
        return workbook;
    }


}
