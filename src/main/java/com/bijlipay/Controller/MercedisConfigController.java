package com.bijlipay.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MercedisConfigController {
    private final Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Value("${mercedes.datasource.url}")
    private String url;
    @Value("${mercedes.datasource.username}")
    private String username;
    @Value("${mercedes.datasource.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
