package com.bijlipay.Service;

import javax.servlet.http.HttpServletResponse;

public interface FintreeService {

	Object processFintree(String getname, HttpServletResponse response) throws Exception;

	Object update3value(String getname, HttpServletResponse response);

	void insertRecords() throws Exception;

}
