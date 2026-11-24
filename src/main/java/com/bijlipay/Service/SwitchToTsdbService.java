package com.bijlipay.Service;

import java.util.Date;
import java.util.List;

public interface SwitchToTsdbService {

    Integer insertToTransaction(String fetchTime);

    Integer insertToTransactionCurrent();

    String fetchTransactionInitTime();

    void fetchAndSaveTxnData(List<Object[]> transactionDatas);
}
