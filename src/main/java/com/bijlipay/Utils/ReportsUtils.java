package com.bijlipay.Utils;

import com.bijlipay.Model.DTO.CustomExcel;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReportsUtils {
    private static Logger logger = LoggerFactory.getLogger(ReportsUtils.class);
    public static Workbook createFormatOneExcel(List<Object[]> rows, String sheetName, String[] headers) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Create header
        Row headerRow = sheet.createRow(0);

        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i, CellType.STRING);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Insert rows
        for (int i = 0; i < rows.size(); i++) {

            // We have already used first row for header
            Row row = sheet.createRow(i + 1);

            Object[] rowItem = rows.get(i);

            // Create cell and put values
            for (int j = 0; j < rowItem.length; j++) {
                Cell cell = row.createCell(j);

                Object valueObject = rowItem[j];

                if (valueObject == null) {

                    cell.setCellValue("NA");

                } else if (valueObject instanceof Double) {

                    cell.setCellValue((Double) valueObject);

                } else if (valueObject instanceof BigDecimal) {

                    cell.setCellValue(((BigDecimal) valueObject).doubleValue());

                } else if (valueObject instanceof Integer) {

                    cell.setCellValue((Integer) valueObject);

                } else if (valueObject instanceof BigInteger) {

                    cell.setCellValue(((BigInteger) valueObject).intValue());

                } else if (valueObject instanceof Boolean) {

                    cell.setCellValue((Boolean) valueObject);

                } else if (valueObject instanceof Date) {

                    CellStyle cellStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((Date) valueObject);

                } else if (valueObject instanceof Long) {

                    cell.setCellValue((Long) valueObject);

                } else {

                    cell.setCellValue(valueObject.toString());
                }

            }

        }

        // Auto format cell width
        autoSizeColumns(sheet);

        return workbook;
    }

    public static Workbook createExcel(List<Object[]> firstObject, List<Object[]> secondObject, String firstSheetName, String secondSheetName, String[] firstSheetHeaders, String[] secondSheetHeaders) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(firstSheetName);

        // Create header
        Row headerRow = sheet.createRow(0);

        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        for (int i = 0; i < firstSheetHeaders.length; i++) {
            Cell cell = headerRow.createCell(i, CellType.STRING);
            cell.setCellValue(firstSheetHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        // Insert rows
        for (int i = 0; i < firstObject.size(); i++) {

            // We have already used first row for header
            Row row = sheet.createRow(i + 1);

            Object[] rowItem = firstObject.get(i);

            // Create cell and put values
            for (int j = 0; j < rowItem.length; j++) {
                Cell cell = row.createCell(j);

                Object valueObject = rowItem[j];

                if (valueObject == null) {

                    cell.setCellValue("NA");

                } else if (valueObject instanceof Double) {

                    cell.setCellValue((Double) valueObject);

                } else if (valueObject instanceof BigDecimal) {

                    cell.setCellValue(((BigDecimal) valueObject).doubleValue());

                } else if (valueObject instanceof Integer) {

                    cell.setCellValue((Integer) valueObject);

                } else if (valueObject instanceof BigInteger) {

                    cell.setCellValue(((BigInteger) valueObject).intValue());

                } else if (valueObject instanceof Boolean) {

                    cell.setCellValue((Boolean) valueObject);

                } else if (valueObject instanceof Date) {

                    CellStyle cellStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((Date) valueObject);

                } else if (valueObject instanceof Long) {

                    cell.setCellValue((Long) valueObject);

                } else {

                    cell.setCellValue(valueObject.toString());
                }

            }

        }

        Sheet sheetTwo = workbook.createSheet(secondSheetName);

        // Create header
        Row secondSheetheaderRow = sheetTwo.createRow(0);

        for (int i = 0; i < secondSheetHeaders.length; i++) {
            Cell cell = secondSheetheaderRow.createCell(i, CellType.STRING);
            cell.setCellValue(secondSheetHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        // Insert rows
        for (int i = 0; i < secondObject.size(); i++) {

            // We have already used first row for header
            Row row = sheetTwo.createRow(i + 1);

            Object[] rowItem = secondObject.get(i);

            // Create cell and put values
            for (int j = 0; j < rowItem.length; j++) {
                Cell cell = row.createCell(j);

                Object valueObject = rowItem[j];

                if (valueObject == null) {

                    cell.setCellValue("NA");

                } else if (valueObject instanceof Double) {

                    cell.setCellValue((Double) valueObject);

                } else if (valueObject instanceof BigDecimal) {

                    cell.setCellValue(((BigDecimal) valueObject).doubleValue());

                } else if (valueObject instanceof Integer) {

                    cell.setCellValue((Integer) valueObject);

                } else if (valueObject instanceof BigInteger) {

                    cell.setCellValue(((BigInteger) valueObject).intValue());

                } else if (valueObject instanceof Boolean) {

                    cell.setCellValue((Boolean) valueObject);

                } else if (valueObject instanceof Date) {

                    CellStyle cellStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((Date) valueObject);

                } else if (valueObject instanceof Long) {

                    cell.setCellValue((Long) valueObject);

                } else {

                    cell.setCellValue(valueObject.toString());
                }

            }

        }

        // Auto format cell width
//        autoSizeColumns(sheet);

        return workbook;
    }

    private static void autoSizeColumns(Sheet sheet) {
        if (sheet.getPhysicalNumberOfRows() > 0) {
            Row row = sheet.getRow(sheet.getFirstRowNum());
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                sheet.autoSizeColumn(columnIndex);
            }
        }
    }

    public static Workbook createExcel(List<CustomExcel> datas) {
        logger.info("==================START TO CREATE EXCEL===============");
        Workbook workbook = new SXSSFWorkbook();
        ZipSecureFile.setMinInflateRatio(0);
        datas.stream().forEach(data->{
        logger.info("SHEET NAME {}",data.getSheetName());
        logger.info("ROW COUNT {}",data.getData().size());

        CustomExcel customExcel = data;
        Sheet sheet = workbook.createSheet(customExcel.getSheetName());

        // Create header
        Row headerRow = sheet.createRow(0);

        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        for (int i = 0; i < customExcel.getColumnName().length; i++) {
            Cell cell = headerRow.createCell(i, CellType.STRING);
            cell.setCellValue(customExcel.getColumnName()[i]);
            cell.setCellStyle(headerStyle);
        }

        // Insert rows
        for (int i = 0; i < customExcel.getData().size(); i++) {

            // We have already used first row for header
            Row row = sheet.createRow(i + 1);

            Object[] rowItem = customExcel.getData().get(i);

            // Create cell and put values
            for (int j = 0; j < rowItem.length; j++) {
                Cell cell = row.createCell(j);

                Object valueObject = rowItem[j];

                if (valueObject == null) {

                    cell.setCellValue("NA");

                } else if (valueObject instanceof Double) {

                    cell.setCellValue((Double) valueObject);

                } else if (valueObject instanceof BigDecimal) {

                    cell.setCellValue(((BigDecimal) valueObject).doubleValue());

                } else if (valueObject instanceof Integer) {

                    cell.setCellValue((Integer) valueObject);

                } else if (valueObject instanceof BigInteger) {

                    cell.setCellValue(((BigInteger) valueObject).intValue());

                } else if (valueObject instanceof Boolean) {

                    cell.setCellValue((Boolean) valueObject);

                } else if (valueObject instanceof Date) {
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((Date) valueObject);

                } else if (valueObject instanceof Long) {

                    cell.setCellValue((Long) valueObject);

                } else {

                    cell.setCellValue(valueObject.toString());
                }

            }

        }
        logger.info("==================CREATED EXCEL===============");
        // Auto format cell width
//        autoSizeColumns(sheet);
        });
        return workbook;
    }


}
