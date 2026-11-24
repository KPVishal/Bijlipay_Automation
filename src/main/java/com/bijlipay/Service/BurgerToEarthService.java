package com.bijlipay.Service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public interface BurgerToEarthService {
    Connection getConnection();

    Connection getMiraConnection();

    void burgerDataTransfer();

    void tsdbBackup() throws SQLException;
    void burgerDataUpdate();

    void riskHoldTransfer();
}
