package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.DTO.AxisDTO;
import com.bijlipay.Model.DTO.BjTransactionDto;
import com.bijlipay.Model.DTO.KVBDTo;
import com.bijlipay.Model.DailySettlement;
import com.bijlipay.Model.FederalDTO;
import com.bijlipay.Model.MisTable;
import com.bijlipay.Model.SibReport;
import com.bijlipay.Repository.AxisRepository;
import com.bijlipay.Service.TsdbService;
import com.bijlipay.Utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static java.sql.DriverManager.getConnection;

@Service
public class TsdbServiceImp implements TsdbService {

    private static final Logger log = LoggerFactory.getLogger(TsdbServiceImp.class);
    private static final String TAG = "TSDB-BJ-TXN : ";
    @Autowired
    private AxisRepository axisRepository;

    @Override
    public void insertAxis(List<AxisDTO> misDTOS) {
        log.info(TAG + "Axis txn sync started : Inserting Data Count : ---- : {}", misDTOS.size());
        misDTOS.forEach(s -> {
            BjTransactionDto axis = new BjTransactionDto();
            axis.setBatchNumber("000001");
            axis.setInvoiceNumber("000001");
            axis.setMerchantId(s.getMid());
            axis.setMti(checkTxnMti(s.getTransactionType().toUpperCase()));
            axis.setrRNumber(s.getrRN());
            axis.setResponseReceivedTime(s.getTxnDate());
            axis.setStan(s.getStan());
            axis.setTerminalId(s.getTid());
            axis.setTxnAmount((double) s.getTxnamount());
            String txnDate = DateUtil.convertDateToString(s.getTxnDate(), "mmdd");
            String txnTime = DateUtil.convertDateToString(s.getTxnDate(), "hhmmss");
            axis.setTxnDate(txnDate);
            axis.setTxnResponseCode("00");
            axis.setTxnTime(txnTime);
            axis.setTxnType(checkTxnType(s.getTransactionType().toUpperCase()));
            axis.setTxnAdditionalAmount(0.0);
            axis.setUrn(s.getrRN());
            axisRepository.insertAxisDataToTsdbDatabase(axis);
        });
        log.info(TAG + "Axis txn sync completed : Inserted Data Count : ---- : {}", misDTOS.size());
    }

    @Override
    public void insertFedral(List<FederalDTO> misDTOS) {
        log.info(TAG + "Fedral txn sync started : Inserting Data Count : ---- : {}", misDTOS.size());
        misDTOS.forEach(s -> {
            BjTransactionDto axis = new BjTransactionDto();
            axis.setBatchNumber(s.getBatchCode());
            axis.setInvoiceNumber("000001");
            axis.setMerchantId(s.getMid());
            axis.setMti(checkTxnMti(s.getNarration().toUpperCase()));
            axis.setrRNumber(s.getrRN());
            axis.setResponseReceivedTime(s.getTxnDate());
            axis.setStan(s.getrRN().substring(0, 6));
            axis.setTerminalId(s.getTid());
            axis.setTxnAmount((double) s.getTxnamount());
            String txnDate = DateUtil.convertDateToString(s.getTxnDate(), "mmdd");
            String txnTime = DateUtil.convertDateToString(s.getTxnDate(), "hhmmss");
            axis.setTxnDate(txnDate);
            axis.setTxnResponseCode("00");
            axis.setTxnTime(txnTime);
            axis.setTxnType(checkTxnType(s.getNarration()).toUpperCase());
            axis.setTxnAdditionalAmount(0.0);
            axis.setUrn(s.getrRN());
            axisRepository.insertFedralDataToTsdbDatabase(axis);
        });
        log.info(TAG + "Fedral txn sync started : Inserted Data Count : ---- : {}", misDTOS.size());
    }


    @Override
    public void insertKvb(List<KVBDTo> misDTOS) {

        misDTOS.forEach(s -> {
            axisRepository.insertKvbDataToTsdbDatabase(s);

        });
    }

    @Override
    public void insertSib(List<SibReport> misDTOS) {
        log.info(TAG + "SIB txn sync started : Inserting Data Count : ---- : {}", misDTOS.size());
        misDTOS.forEach(s -> {
            BjTransactionDto axis = new BjTransactionDto();
            axis.setBatchNumber(s.getBatchCode());
            axis.setInvoiceNumber("000001");
            axis.setMerchantId(s.getMid());
            axis.setMti(checkTxnMti(s.getNarration().toUpperCase()));
            axis.setrRNumber(s.getrRN());
            axis.setResponseReceivedTime(s.getTxnDate());
            axis.setStan(s.getrRN().substring(0, 6));
            axis.setTerminalId(s.getTid());
            axis.setTxnAmount((double) s.getTxnamount());
            String txnDate = DateUtil.convertDateToString(s.getTxnDate(), "mmdd");
            String txnTime = DateUtil.convertDateToString(s.getTxnDate(), "hhmmss");
            axis.setTxnDate(txnDate);
            axis.setTxnResponseCode("00");
            axis.setTxnTime(txnTime);
            axis.setTxnType(checkTxnType(s.getNarration().toUpperCase()));
            axis.setTxnAdditionalAmount(0.0);
            axis.setUrn(s.getrRN());
            axisRepository.insertSibDataToTsdbDatabase(axis);
        });
        log.info(TAG + "SIB txn sync started : Inserting Data Count : ---- : {}", misDTOS.size());
    }

    @Override
    public void insertCorp(List<MisTable> misDTOS) {
        misDTOS.forEach(s -> {
            axisRepository.insertCorpDataToTsdbDatabase(s);

        });
    }

    @Override
    public void insertCorpSettlement(List<DailySettlement> misDTOS) {
        misDTOS.forEach(s -> {
            axisRepository.insertCorpSettlementToDatabase(s);

        });
    }






    @Override
    public String fetchInitTime() {
        return axisRepository.fetchInitTime();
    }

    @Override
    public String fetchInitTimeForQR() {
        return axisRepository.fetchInitTimeForQR();
    }

    private String checkTxnType(String txnType) {
        switch (txnType) {
            case "SALE":
                return "00";
            case "UPI":
                return "37";
            default:
                return "00";
        }
    }

    private String checkTxnMti(String mti) {
        switch (mti) {
            case "SALE":
                return "0200";
            case "UPI":
                return "0100";
            default:
                return "0200";
        }
    }

}

