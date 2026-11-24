package com.bijlipay.Service;

import java.text.ParseException;
import java.util.Optional;

public interface SIBService {

    boolean createUpdatedExcel(String bijlipayMisXlsx, String date) throws ParseException;

    //    @PreAuthorize("hasRole('ROLE_OP_HEAD')")
    boolean connectToSFTPDownloadWithSpecificDate(Optional<Long> date);

    boolean findByCreatedAt(String oneDayBefore);

    boolean connectToSFTPDownloadBeforeDates(long time);
}
