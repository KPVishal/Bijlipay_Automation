package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.MisTable;
import com.bijlipay.Model.PaymentTemp;
import com.bijlipay.Repository.MisRespository;
import com.bijlipay.Repository.PaymentTempRepository;
import com.bijlipay.Service.BankService;
import com.bijlipay.Utils.DateUtil;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.bijlipay.Utils.Constants.MM_DD_YYYY_HH_MM_SS_AA_HITACHI;
import static com.bijlipay.Utils.Constants.YYYYMMDD;

@Service
public class BankSerivceImplementation implements BankService {
    private static final Logger log = LoggerFactory.getLogger(BankSerivceImplementation.class);
    @Autowired
    public PaymentTempRepository tempRepository;
    @Override
    public boolean importPaymentData(File absoluteFilePath) {
        boolean flag = false;
        Workbook wb = null;
        FileInputStream file = null;
        boolean firstrow = true;
        boolean secondrow = true;
        try {
            file = new FileInputStream(absoluteFilePath);
            wb = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096).open(file);
            List<PaymentTemp> map = new ArrayList<PaymentTemp>();
            for (Sheet sheet : wb) {
                if(sheet.getSheetName().equals("Payment_Details_"+ DateUtil.currentDate(YYYYMMDD))) {
                    for (Row row : sheet) {
                        if (firstrow) {
                            firstrow = false;
                            continue;
                        }
                        if (secondrow) {
                            secondrow = false;
                            continue;
                        }
                        PaymentTemp hitachi = new PaymentTemp();
                        int cellValue = 0;
                        hitachi.setMid(checkEmptyorNull(row.getCell(cellValue++)));
                        hitachi.setTid(checkEmptyorNull(row.getCell(cellValue++)));
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        hitachi.setMerchantName(checkEmptyorNull(row.getCell(cellValue++)));
                        hitachi.setCity(checkEmptyorNull(row.getCell(cellValue++)));
                        hitachi.setCardNo(checkEmptyorNull(row.getCell(cellValue++)));
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        try {
                            hitachi.setTxnamount((float) row.getCell(cellValue++).getNumericCellValue());
                        }catch (Exception e){
                            e.getMessage();
                        }
                        try {
                        hitachi.setNe_MDR(checkEmptyorNull(row.getCell(cellValue++)));
                        }catch (Exception e){
                        e.getMessage();
                        }
                        try {
                        hitachi.setMrlmdr((float) row.getCell(cellValue++).getNumericCellValue());
                        }catch (Exception e){
                            e.getMessage();
                        }
                        try{
                        hitachi.setGst((float) (row.getCell(cellValue++).getNumericCellValue() +
                                row.getCell(cellValue++).getNumericCellValue() + row.getCell(cellValue++).getNumericCellValue()));
                        }catch (Exception e){
                        e.getMessage();
                        }
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        try{
                        hitachi.setNetamount((float) row.getCell(cellValue++).getNumericCellValue());
                        }catch (Exception e){
                            e.getMessage();
                        }
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        cellValue++;
                        hitachi.setrRN(checkEmptyorNull(row.getCell(cellValue++)));
                        cellValue++;
                        cellValue++;
                        hitachi.setTxnDate(DateUtil.convertStringToDate(row.getCell(cellValue++).getStringCellValue(), MM_DD_YYYY_HH_MM_SS_AA_HITACHI));
                        cellValue++;
                        hitachi.setPaymentdate(DateUtil.convertStringToDate(row.getCell(cellValue++).getStringCellValue(), MM_DD_YYYY_HH_MM_SS_AA_HITACHI));
                        cellValue++;
                        hitachi.setCardType(checkEmptyorNull(row.getCell(cellValue++)));
                        cellValue++;
                        hitachi.setAssociationName(checkEmptyorNull(row.getCell(cellValue++)));
                        map.add(hitachi);
                    }
                    log.info("Temp bank data Start to save");
                    tempRepository.saveAll(map);
                    try{
                        //Trsnsaction Ace Portal Data
                        log.info("Trsnsaction Ace Portal Data");
                        tempRepository.insertIntoAceTable();
                    }catch (Exception e){
                        log.info("Error while insert or update ace table");
                    }
                    log.info("bank data temp table start to update");
                    tempRepository.updateTempMisTable();
                    log.info("production table start to insert record");
                    tempRepository.insertPaymentDataToBankDatabase();
                    log.info("production table inserted successfully");
                    flag = true;
                }
            }
            wb.close();
            file.close();
            map.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
        return flag;
    }

    @Override
    public void updateBank() {
        tempRepository.updateBank();
    }

    @Override
    public void updateMisTable() {
        tempRepository.updateMisTable();
    }

    @Override
    public void deleteTemprecords() {
        tempRepository.deleteBijlipayMisTempTable();
    }

    @Override
    public void uploadBijlipayMisForBankPortal() {
        tempRepository.updateBankName();
        tempRepository.uploadcurrentDateMisData();
    }

    @Override
    public void updateDate(Optional<Long> fromDate, Optional<Long> toDate) {
        Date vfrom = DateUtil.longtoDate(fromDate.get());
        Date vto=DateUtil.longtoDate(toDate.get());
        System.out.print("vfrom "+vfrom);
        System.out.print("vfrom "+vto);
        log.info("choosed From Date {}<-To Date->{}",vfrom,vto);
        tempRepository.updateCurrentDate(vfrom,vto);
    }

    public String checkEmptyorNull(Cell cell){
        if (cell == null) { // use row.getCell(x, Row.CREATE_NULL_AS_BLANK) to avoid null cells
            return "NA";
        }
        return cell.getStringCellValue();
    }

}
