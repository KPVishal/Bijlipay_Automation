package com.bijlipay.Service;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface ClixService {

	void createExcelForClix(String getname);

	Object processclix(String getname, HttpServletResponse response) throws Exception;

	void insertRecords() throws Exception;




	
}
