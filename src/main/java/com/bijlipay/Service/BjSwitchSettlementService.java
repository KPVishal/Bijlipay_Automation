package com.bijlipay.Service;

import com.bijlipay.Model.DTO.BjSwitchSettlementDto;

import java.sql.SQLException;
import java.util.List;

//@Service
public interface BjSwitchSettlementService {
    Long fetchAndSaveTxnData(List<Object[]> transactionDatas) throws SQLException;
}
