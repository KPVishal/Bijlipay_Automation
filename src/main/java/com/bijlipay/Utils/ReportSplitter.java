package com.bijlipay.Utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReportSplitter {

    private static final Logger logger = LoggerFactory.getLogger(ReportSplitter.class);


    public void excelSplit(String fileName,int count){
        try {
            FileInputStream fis = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet
            int batchSize = count;
            int rowCount = sheet.getPhysicalNumberOfRows() ;
            logger.info("Row Count--{}",rowCount);

            // Get header row

            // Splitting into batches
            for (int batchStart = 0; batchStart < rowCount; batchStart += batchSize) {
                int batchEnd = Math.min(batchStart + batchSize, rowCount);
                Row headerRow = sheet.getRow(0);

                // Create a new workbook for each batch
                Workbook newWorkbook = new XSSFWorkbook();
                Sheet newSheet = newWorkbook.createSheet();

                // Copy header row to new sheet
                Row newHeaderRow = newSheet.createRow(0);
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    Cell cell = headerRow.getCell(j);
                    if (cell != null) {
                        Cell newCell = newHeaderRow.createCell(j);
                        newCell.setCellValue(cell.getStringCellValue());
                    }
                }

                // Copy the rows for the batch
                for (int i = batchStart; i < batchEnd; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        Row newRow = newSheet.createRow(i - batchStart + (batchStart == 0 ? 0 : 1)); // Adjust row index for header
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                Cell newCell = newRow.createCell(j);
                                if (cell.getCellType() == CellType.STRING) {
                                    newCell.setCellValue(cell.getStringCellValue());
                                } else if (cell.getCellType() == CellType.NUMERIC) {
                                    newCell.setCellValue(cell.getNumericCellValue());
                                } // Handle other cell types as needed
                            }
                        }
                    }
                }
                // Write to a new Excel file
                String newFileName = fileName.substring(0, fileName.length() - 5);
                logger.info("New File Name ---{}",newFileName+"_"+((batchStart)/batchSize)+".xlsx");
                FileOutputStream fos = new FileOutputStream(new File(newFileName+"_"+((batchStart)/batchSize)+".xlsx"));
                newWorkbook.write(fos);
                fos.close();
                newWorkbook.close();
            }
            workbook.close();
            fis.close();
            System.out.println("Splitting completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
