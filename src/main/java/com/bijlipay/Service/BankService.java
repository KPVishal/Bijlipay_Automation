package com.bijlipay.Service;

import java.io.File;
import java.util.Optional;

public interface BankService {
    boolean importPaymentData(File file);

    void updateBank();

    void updateMisTable();

    void deleteTemprecords();

    void uploadBijlipayMisForBankPortal();

    void updateDate(Optional<Long> fromDate, Optional<Long> toDate);
}
