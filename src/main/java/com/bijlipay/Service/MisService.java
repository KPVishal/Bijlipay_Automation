package com.bijlipay.Service;

import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

public interface MisService {

    void saveMisDataToTable(String bijlipayMisXlsx) ;

    Boolean connectToSFTPFileDownload() throws JSchException;
    boolean connectToSFTPFileCorpDownload() throws JSchException;

    void saveDailySettlementDataToTable(String bijlipaySettlementXlsx) throws ParseException;

    void sentMail() throws Exception;
    void sentMailForCase11() throws Exception;

    void deleteystrecord();

    void deletemistemp();

    boolean connectToSFTPFileUpload(String[] filePath);

    void federalSettlementReport(String bijlipaySettlementReport);

    boolean connectToSFTPDownloadWithSpecificDate(Optional<Long> date);

    void deleteRentTable();

    void insertDailyRecords();
}