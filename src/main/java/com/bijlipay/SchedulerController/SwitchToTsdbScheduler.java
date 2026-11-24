package com.bijlipay.SchedulerController;
import com.bijlipay.Controller.SwitchBaseController;
import com.bijlipay.Repository.AxisRepository;
import com.bijlipay.Service.SwitchToTsdbService;
import com.bijlipay.Utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component

@RestController
@RequestMapping("/api")
public class SwitchToTsdbScheduler extends SwitchBaseController {
    private static final Logger log = LoggerFactory.getLogger(SwitchToTsdbScheduler.class);
    @Autowired
    private AxisRepository axisRepository;
    @Autowired
    private SwitchToTsdbService switchToTsdbService;

    @GetMapping("/switchtotsdb")
//    @Scheduled(cron = "0 0/2 * * * *")
    @Transactional
    public void switchToTsdb() throws Exception {
        Connection con = null;
        Statement stmt = null;
        try {
            log.info("SWITCH-TXN : Getting the connection to BPSwitchOn DB to fetch user from lead information table : ");
            con = getConnection();
            stmt = con.createStatement();
            String initTime = switchToTsdbService.fetchTransactionInitTime();
            String query = "";
            if (initTime != null) {
                query = "SELECT [batch_number],[invoice_number],[merchant_id],[mti],[rrn_number],[response_received_time]," +
                        "[stan],[terminal_id],[txn_amount],[txn_date],[txn_response_code],[txn_time],[txn_type]," +
                        "[txn_additional_amount],[urn] From bank.dbo.switch_transactions Where response_received_time > '" + initTime +"'  AND response_received_time <= GETDATE()";

            } else {
                query = "SELECT [batch_number],[invoice_number],[merchant_id],[mti],[rrn_number],[response_received_time]," +
                        "[stan],[terminal_id],[txn_amount],[txn_date],[txn_response_code],[txn_time],[txn_type]," +
                        "[txn_additional_amount],[urn] From bank.dbo.switch_transactions Where response_received_time > DATEADD(minute, -5,  GETDATE()) ;";
            }
//
            log.info("SWITCH-TXN : Current Query : {}", query);
            ResultSet resultSet = stmt.executeQuery(query);
            List<Object[]> transactionDatas = new ArrayList<>();
            int count = 0;
            while (resultSet.next()) {
                int cols = resultSet.getMetaData().getColumnCount();
                Object[] arr = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    arr[i] = resultSet.getObject(i + 1);
                    count++;
                }
                transactionDatas.add(arr);

            }
//            log.info("SWITCH-TXN : transactions objects : {}", transactionDatas);

            resultSet.close();

            stmt.close();
            con.close();
            switchToTsdbService.fetchAndSaveTxnData(transactionDatas);

            log.info("inserted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

