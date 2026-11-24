package com.bijlipay.Service;

import java.sql.SQLException;

public interface ClixAccService {
    void executingLogic() throws SQLException;

    void insertClixFileAcc();

    void updateLenderCapticalShareAndMerchantShare();
}
