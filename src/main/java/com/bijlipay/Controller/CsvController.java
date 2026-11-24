package com.bijlipay.Controller;


import com.bijlipay.Model.DTO.FrmDataDTO;
import com.bijlipay.Service.FrmDataService;
import com.bijlipay.Utils.CreateExcelView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static com.bijlipay.Utils.Constants.*;
@RestController
@RequestMapping(value = "/api")
public class CsvController {
    private static final Logger log = LoggerFactory.getLogger(CsvController.class);

    @Autowired
    private FrmDataService frmDataService;


    @GetMapping("/download/merchants-csv")
    public void csv(HttpServletResponse response) throws IOException {

        String fileName = "NewCsvFile.csv";
        response.setContentType("application/octel-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        // frmDataService.deleteAllFrmData();
        // frmDataService.insertNewFrmData();
        // frmDataService.updateFrmData();
        List<FrmDataDTO> queryForOne = frmDataService.findAllFrmData();
        if (queryForOne.size() > 0) {
            StringBuffer fileWriter = CreateExcelView.createCsvFileReport(queryForOne);
            //fileWriter.append(response.getOutputStream());
            InputStream in = new ByteArrayInputStream(fileWriter.toString().getBytes(StandardCharsets.UTF_8));
            ServletOutputStream out = response.getOutputStream();
            byte[] outputbytes = new byte[BUFFER_SIZE];
            while (in.read(outputbytes, BINARY_FILE_TYPE, BUFFER_SIZE) != -1) {
                out.write(outputbytes, BINARY_FILE_TYPE, BUFFER_SIZE);
            }
            in.close();
            out.flush();
//            out.close();
        } else {
            log.info("File Not Created");
        }
    }
}



