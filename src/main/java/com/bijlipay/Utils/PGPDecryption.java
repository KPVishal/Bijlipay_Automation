package com.bijlipay.Utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;


@Slf4j
public class PGPDecryption {

    public static String getFileExtension(String filePath) {
        // Get the file extension by extracting the part after the last dot
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            return filePath.substring(dotIndex); // Includes the dot
        } else {
            return ""; // No extension
        }
    }



    public static String decryptFile(String encryptedFilePath, String outputDirectory) {
        File encryptedFile = new File(encryptedFilePath);
        String fileNameWithoutExtension = encryptedFile.getName().replaceFirst("[.][^.]+$", ""); // Remove extension
        String decryptedFileName = outputDirectory + fileNameWithoutExtension + ".xlsx";
        log.info("decrypted file path{}",decryptedFileName);
        log.info("encrypted file path{}",encryptedFilePath);
        try {
            // Run the GPG decrypt command
            String command = "gpg --batch --yes --passphrase Worldline#2025 --output " + decryptedFileName + " --decrypt " + encryptedFilePath;
            log.info("executing command {}",command);
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("File decrypted successfully: " + decryptedFileName);
            } else {
                log.error("Failed to decrypt the file. Exit code: " + exitCode);
                throw new Exception("Decryption failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error executing GPG decryption command: ", e);
            throw new RuntimeException("Decryption failed.", e);
        }
        return decryptedFileName;
    }
}
