package com.bijlipay.Service;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Optional;

public interface ReportService {
    void invoiceMailToCustomer();

    Workbook downloadReport(Optional<String> reportName);

    Workbook downloadReportTypeTwo(Optional<String> reportName);

}
