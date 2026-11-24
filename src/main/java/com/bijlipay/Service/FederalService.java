package com.bijlipay.Service;

import com.jcraft.jsch.JSchException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

public interface FederalService {

    boolean createUpdatedExcel(String bijlipayMisXlsx, String date) throws ParseException;

//    @PreAuthorize("hasRole('ROLE_OP_HEAD')")
    boolean connectToSFTPDownloadWithSpecificDate(Optional<Long> date);

    boolean findByCreatedAt(String oneDayBefore);

    boolean connectToSFTPDownloadBeforeDates(long time);
}
