package com.bijlipay.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bijlipay.Service.FintreeService;

@RestController
@RequestMapping("/file")
public class FintreeController {

	 private static final Logger log = LoggerFactory.getLogger(ClixController.class);
	    public static boolean FIRSTROWDAILY = true;
	    public int i = 1;
	    String date = null;
	    @PersistenceContext
	    private EntityManager entityManager;
	    @Autowired
	    private FintreeService clixservice;
	   

	    @GetMapping(value = {"/Fintree/dwnld/{getname}"})
	    public ResponseEntity<?> getMenamedwnldg(@PathVariable("getname") String getname, HttpServletResponse response) throws IOException {
	        try {
	            List<String> result = exceldwnld(getname);
	            if (result.size() > 0) {
	                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	                date = simpleDateFormat.format(new Date());
	                List<String> header = Arrays.asList(" ME_NAME", "MID", "TID", "LOAN_ACCOUNT_NUMBER", "TOTAL_TRANSACTION_VALUE", "NET_TRANSACTION_VALUE", "MERCHANT_SHARE1", "SPLIT_FUNDING_VALUE", "HELD_AMT", "LOAN_TENURE", "AC_NO", "IFSC_CODE", "TOTAL_DISB_VALUE", "SETTLED_WITH_LENDER",
	                        "LOAN_OUTSTANDING", "INSTRUMENT_TYPE", "VALUE_DATE", "TOTAL_ELIGIBLE_VALUE","VALUE_ALLOCATED_PERCT","VALUE_ALLOCATED");
	                response.addHeader("content-disposition", "attachment; filename=" + getname + "_master_" + date + ".xls");
	                response.setContentType("application/vnd.ms-excel");
	                new SimpleExporter().gridExport(header, result, "", response.getOutputStream());
	            } else {
	                return new ResponseEntity<>("nill data", HttpStatus.BAD_REQUEST);
	            }
	        } catch (Exception e) {
	            log.info("Could not update data to the database 1" + e.getMessage());
	            return new ResponseEntity<>("Not downloaded", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	        return new ResponseEntity<>("Success", HttpStatus.OK);
	    }
	    
	    private List<String> exceldwnld(String getname) {
	        String query = "select me_name,mid,tid,loan_account_number,total_transaction_value	,net_transaction_value	," +
	                "merchant_share1,Lender_capital_share1 as Split_Funding_value,held_amt,loan_tenure,ac_no,ifsc_code," +
	                "total_disb_value,settled_with_Lender_capital,balance as Loan_Outstanding,instrument_type,REPLACE(CONVERT(VARCHAR(30)" +
	                ", value_date, 101), ' ', '-') AS value_date,total_eligible_value,value_allocated_perct,value_allocated from [dbo].[fintree_master$] " +
	                "where caption=:caption";
	        Query q = entityManager.createNativeQuery(query);
	        q.setParameter("caption", getname);
	        List<String> results = (List<String>) q.getResultList();
	        return results;
	    }
}
