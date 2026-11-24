package com.bijlipay.Utils;

import com.bijlipay.SchedulerController.AxisSchedular;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.bijlipay.Utils.Constants.*;

public class UnzipUtility {
//         * Size of the buffer to read/write data
    private static final Logger log = LoggerFactory.getLogger(UnzipUtility.class);

    private final long milliseconds = 1000L;
    private static final int BUFFER_SIZE = 4096;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     *
     * @return
     */

    public static String unzip(String zipFilePath, String destDirectory) throws IOException {

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        String filePath = "";
        // iterates over entries in the zip file
        while (entry != null) {
            //filePath = destDirectory + File.separator + entry.getName();
            filePath = destDirectory + entry.getName();
            if (!entry.isDirectory()) {
                String fileName = entry.getName();
                if (fileName.contains("Bijlipay_Aggr_Report")) {
                    log.info("Entry is a file : ------------- : {}", entry.getName());
                    log.info("File Path : ------------- : {}", filePath);
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                }else{
                    log.info("File is not needed for processing {}",entry.getName());
                }
            } else {
                log.info("Entry is a Directory : ------------- : {}", entry.getName() );
                log.info("File Path : ------------- : {}", filePath );
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        System.gc();
        return filePath;
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
        System.gc();
    }

}

