package com.bijlipay.Utils;

import com.bijlipay.Service.FileStorageService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;


@Service
public class MdrInsert {

    @Autowired
    private FileStorageService fileStorageService;


    public void insertRecords(String fileName) throws Exception {
        Resource resource = fileStorageService.loadFileAsResource("MDRInvoice.xlsx");
        Workbook wb = new XSSFWorkbook(new FileInputStream(resource.getFile().getAbsolutePath()));
        boolean FIRSTROWDAILY = true;
        for (Row row : wb.getSheetAt(0)) {
            if (FIRSTROWDAILY) {
                FIRSTROWDAILY = false;
                continue;
            }
        }
    }
}
