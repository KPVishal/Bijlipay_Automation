package com.bijlipay.Service;

import com.jcraft.jsch.JSchException;


import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;


public interface AxisService {

    boolean createUpdatedExcel(String bijlipayMisXlsx, String date) throws ParseException, IOException;

    //    @PreAuthorize("hasRole('ROLE_OP_HEAD')")
    boolean connectToSFTPDownloadWithSpecificDate(Optional<Long> date);

    boolean findByCreatedAt(String oneDayBefore);

    boolean connectToSFTPDownloadBeforeDates(long time) throws JSchException;

}
