package com.bijlipay.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SwitchSettlementConfigController {

    private final Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Value("${earth.datasource.url}")
    private String url;
    @Value("${earth.datasource.username}")
    private String username;
    @Value("${earth.datasource.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
