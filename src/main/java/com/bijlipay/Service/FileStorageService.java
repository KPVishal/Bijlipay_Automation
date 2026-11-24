package com.bijlipay.Service;

import com.bijlipay.Exception.FileStorageException;
import com.bijlipay.Exception.MyFileNotFoundException;
import com.bijlipay.properties.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bijlipay.Utils.Constants.PENDING_XLSX;
import static com.bijlipay.Utils.Constants.SFTPWORKINGDIR_MIS;


@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                    ex);
        }
    }

    public String storeFileReturnFileName(MultipartFile file) {
        try {
            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            String fileExtension = "";
            String mimeType = file.getContentType();
            System.out.println("File mimeType new  is: " + mimeType);
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }
            System.out.println("File extension new  is: " + fileExtension);
            if(fileExtension.equalsIgnoreCase(".xlsx")){
                String fileName = PENDING_XLSX;
                System.out.println(" fileName " + fileName);
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            }
            throw new FileStorageException("Sorry!,please upload .xlsx file");
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            String fileExtension = "";
            String mimeType = file.getContentType();
            System.out.println("File mimeType new  is: " + mimeType);
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }
            System.out.println("File extension new  is: " + fileExtension);
            Path path = Paths.get(SFTPWORKINGDIR_MIS+"/");
            if(fileExtension.equalsIgnoreCase(".xlsx")){
                Files.copy(file.getInputStream(),path.resolve(fileNameOriginal) , StandardCopyOption.REPLACE_EXISTING);
                return fileNameOriginal;
            }
            throw new FileStorageException("Sorry!,please upload .xlsx file");
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    //============================================================================================================================
    public String storemdrFileReturnFileName(MultipartFile file) {

        try {

            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            //System.out.println("Original Name " + fileNameOriginal);

            String fileExtension = "";

            String mimeType = file.getContentType();

            //System.out.println("File mimeType new  is: " + mimeType);

            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }

            int index = fileNameOriginal.lastIndexOf(".");
            if (index > 0) {
                fileExtension = fileNameOriginal.substring(index); // return ---> ".java"
                fileExtension = fileNameOriginal.substring(index + 1); // return ---> "java"
            }

            if (fileExtension.equals("xlsx")) {

                //System.out.println("File extension new  is: " + fileExtension);

                String fileNameNew = "MDRInvoice." + fileExtension;
                // Normalize file name
                String fileName = StringUtils.cleanPath(fileNameNew);

                //System.out.println("File Name " + fileNameNew);
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                return fileName;
            } else {
                return "";
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

    public String storemdrFileReturnFileNamefederal(MultipartFile file) {

        try {

            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            //System.out.println("Original Name " + fileNameOriginal);

            String fileExtension = "";

            String mimeType = file.getContentType();

            //System.out.println("File mimeType new  is: " + mimeType);

            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }

            int index = fileNameOriginal.lastIndexOf(".");
            if (index > 0) {
                fileExtension = fileNameOriginal.substring(index); // return ---> ".java"
                fileExtension = fileNameOriginal.substring(index + 1); // return ---> "java"
            }

            if (fileExtension.equals("xlsx")) {

                //System.out.println("File extension new  is: " + fileExtension);

                String fileNameNew = "MDRfederalInvoice." + fileExtension;
                // Normalize file name
                String fileName = StringUtils.cleanPath(fileNameNew);

                //System.out.println("File Name " + fileNameNew);
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                return fileName;
            } else {
                return "";
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

    public String storeFileReturnFileNameForPDF(MultipartFile file) {
        try {
            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            String fileExtension = "";
            String mimeType = file.getContentType();
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }
            int index = fileNameOriginal.lastIndexOf(".");
            if (index > 0) {
                fileExtension = fileNameOriginal.substring(index); // return ---> ".java"
                fileExtension = fileNameOriginal.substring(index + 1); // return ---> "java"
            }

            if (fileExtension.equals("xlsx")) {
                String fileNameNew = "NewInvoice." + fileExtension;
                String fileName = StringUtils.cleanPath(fileNameNew);
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } else {
                return "";
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

//    =========================================================================================================================

    public String clixStoreFileReturnFileName(MultipartFile file) {
        try {
            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            String fileExtension = "";
            String mimeType = file.getContentType();
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }
            int index = fileNameOriginal.lastIndexOf(".");
            if (index > 0) {
                fileExtension = fileNameOriginal.substring(index); // return ---> ".java"
                fileExtension = fileNameOriginal.substring(index + 1); // return ---> "java"
            }
            if (fileExtension.equals("xlsx")) {
                System.out.println("File extension new  is: " + fileExtension);
                String fileNameNew = "Clixfile." + fileExtension;
                String fileName = StringUtils.cleanPath(fileNameNew);
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(fileNameNew);
                return fileName;
            } else {
                return "";
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

    public String clixAccStoreFileReturnFileName(MultipartFile file) {
        try {
            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            String fileExtension = "";
            String mimeType = file.getContentType();
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }
            int index = fileNameOriginal.lastIndexOf(".");
            if (index > 0) {
                fileExtension = fileNameOriginal.substring(index); // return ---> ".java"
                fileExtension = fileNameOriginal.substring(index + 1); // return ---> "java"
            }
            if (fileExtension.equals("xlsx")) {
                System.out.println("File extension new  is: " + fileExtension);
                String fileNameNew = "ClixfileAcc." + fileExtension;
                String fileName = StringUtils.cleanPath(fileNameNew);
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(fileNameNew);
                return fileName;
            } else {
                return "";
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

    public String fintreeStoreFileReturnFileName(MultipartFile file) {
        try {
            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            String fileExtension = "";
            String mimeType = file.getContentType();
            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }
            int index = fileNameOriginal.lastIndexOf(".");
            if (index > 0) {
                fileExtension = fileNameOriginal.substring(index); // return ---> ".java"
                fileExtension = fileNameOriginal.substring(index + 1); // return ---> "java"
            }
            if (fileExtension.equals("xlsx")) {
                System.out.println("File extension new  is: " + fileExtension);
                String fileNameNew = "Fintreefile." + fileExtension;
                String fileName = StringUtils.cleanPath(fileNameNew);
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(fileNameNew);
                return fileName;
            } else {
                return "";
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

    public String storeBankFileName(MultipartFile file,String newFileName) {
        try {

            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            String fileExtension = "";

            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }

            int index = fileNameOriginal.lastIndexOf(".");
            if (index > 0) {
                fileExtension = fileNameOriginal.substring(index); // return ---> ".java"
                fileExtension = fileNameOriginal.substring(index + 1); // return ---> "java"
            }

            if (fileExtension.equals("xlsx")) {
                String fileNameNew = newFileName+"." + fileExtension;
                // Normalize file name
                String fileName = StringUtils.cleanPath(fileNameNew);

                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                return fileName;
            } else {
                throw new FileStorageException("Please try to upload .xlsx format file!");
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }

    public String fileUpload(MultipartFile file) {
        try {

            String fileNameOriginal = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileNameOriginal.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileNameOriginal);
            }
            String fileExtension = "";

            if (fileNameOriginal.contains(".") && fileNameOriginal.lastIndexOf(".") != 0) {
                fileExtension = fileNameOriginal.substring(fileNameOriginal.lastIndexOf("."));
            }

            int index = fileNameOriginal.lastIndexOf(".");
            if (index > 0) {
                fileExtension = fileNameOriginal.substring(index); // return ---> ".java"
                fileExtension = fileNameOriginal.substring(index + 1); // return ---> "java"
            }

            if (fileExtension.equals("xlsx")) {
                String fileNameNew = "monthwise." + fileExtension;
                // Normalize file name
                String fileName = StringUtils.cleanPath(fileNameNew);

                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                return fileName;
            } else {
                throw new FileStorageException("Please try to upload .xlsx format file!");
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file  Please try again!", ex);
        }
    }
}