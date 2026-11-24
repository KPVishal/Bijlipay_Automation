package com.bijlipay.Controller;


import com.bijlipay.Model.ClixMaster;
import com.bijlipay.Model.ClixPending;
import com.bijlipay.Model.DTO.MasterClixDTO;
import com.bijlipay.Repository.ClixDetailRepository;
import com.bijlipay.Repository.ClixMasterRepository;
import com.bijlipay.Repository.ClixPendingRepository;
import com.bijlipay.Repository.RentalRepository;
import com.bijlipay.Service.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;import static com.bijlipay.Utils.Constants.*;


@RestController
@RequestMapping("/api")
public class ClixController {

    private static final Logger log = LoggerFactory.getLogger(ClixController.class);
    public static boolean FIRSTROWDAILY = true;
    public int i = 1;
    String date = null;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ClixMasterRepository clixmstrrepo;
    @Autowired
    private ClixPendingRepository clixpendrepo;
    @Autowired
    private ClixService clixservice;
    @Autowired
    private FileStorageService filestore;
    @Autowired
    private FintreeService fintreeserv;
    @Autowired
    private VistaService vistaserv;
    @Autowired
    private CapitalService capitalserv;
    @Autowired
    private ClixDetailRepository clixdtsrepo;
    @Autowired
    private RentalRepository rentalrepo;

    @GetMapping(value = {"/getname"})
    public ResponseEntity<?> getMename(@PathVariable("searchTerm") Optional<String> searchTerm) {
        List<String> mename = null;
        List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
        if (searchTerm.isPresent()) {
            System.out.println("inside searchTerm");
            mename = clixmstrrepo.getMename(searchTerm);
            if (mename.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                mename.stream().forEach(l -> {
                    Map<String, String> tempMap = new HashMap<>();
                    tempMap.put("id", String.valueOf(i));
                    tempMap.put("name", l);
                    i++;
                    arrayList.add(tempMap);
                });
            }
        } else {
            mename = clixmstrrepo.getMename();
            mename.stream().forEach(l -> {
                Map<String, String> tempMap = new HashMap<>();
                tempMap.put("id", String.valueOf(i));
                tempMap.put("name", l);
                i++;
                arrayList.add(tempMap);
            });
        }
        return new ResponseEntity<>(arrayList, HttpStatus.OK);
    }

    @GetMapping(value = {"/getMid", "/getMid/{searchTerm}"})
    public ResponseEntity<?> getMid(@PathVariable("searchTerm") Optional<String> searchTerm) {
        List<String> mid = null;
        List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
        if (searchTerm.isPresent()) {
            System.out.println("inside searchTerm");
            mid = clixmstrrepo.getMid(searchTerm);
            if (mid.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                mid.stream().forEach(l -> {
                    Map<String, String> tempMap = new HashMap<>();
                    tempMap.put("id", String.valueOf(i));
                    tempMap.put("name", l);
                    i++;
                    arrayList.add(tempMap);
                });
            }
        } else {
            mid = clixmstrrepo.getMid();
            mid.stream().forEach(l -> {
                Map<String, String> tempMap = new HashMap<>();
                tempMap.put("id", String.valueOf(i));
                tempMap.put("name", l);
                i++;
                arrayList.add(tempMap);
            });
        }
        return new ResponseEntity<>(arrayList, HttpStatus.OK);

    }

    @DeleteMapping(value = "/deletemid/{getmid}")
    public ResponseEntity<?> deletemid(@PathVariable("getmid") String getmid) throws Exception {

        String[] midArray = getmid.split(",");
        List<String> arrayList = new ArrayList<>();
        for (String midArrayresult : midArray) {
            boolean mid = false;
            mid = clixmstrrepo.existsByMid(midArrayresult);
            if (mid) {
                clixmstrrepo.deletebyMid(midArrayresult);
            }
            mid = clixpendrepo.existsByMid(midArrayresult) ;
            if (mid) {
                clixmstrrepo.deletebyPendingMid(midArrayresult);
            }
            mid = clixdtsrepo.existsByMid(midArrayresult);
            if (mid) {
                clixmstrrepo.deletebyDetailsMid(midArrayresult);
            }
            mid = rentalrepo.existsByMid(midArrayresult);
            if (mid) {
                clixmstrrepo.deletebyRentalMid(midArrayresult);
            }
        }

        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }

    @GetMapping(value = "/update/{getname}")
    public ResponseEntity<?> clix(@PathVariable("getname") String getname, HttpServletResponse response) throws Exception {
        log.info("inside");
        System.out.println(getname);
        int format = clixmstrrepo.getformat(getname);
        if (format == 1) {

            clixservice.processclix(getname, response);
            if (getname.equalsIgnoreCase("Fintree")) {
                fintreeserv.processFintree(getname, response);
                fintreeserv.update3value(getname, response);
            }
        } else if (format == 2) {

            capitalserv.processcapital(getname, response);
        } else if (format == 3) {

            vistaserv.processvista(getname, response);
        }
        return ResponseEntity.ok("Success");


    }

    @GetMapping(value = {"/getname/dwnld/{getname}"})
    public ResponseEntity<?> getMenamedwnldg(@PathVariable("getname") String getname, HttpServletResponse response) throws IOException {
        try {
            List<String> result = exceldwnld(getname);
            if (result.size() > 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                date = simpleDateFormat.format(new Date());
                List<String> header = Arrays.asList(" ME_NAME", "LOAN_ACCOUNT_NUMBER", "TOTAL_TRANSACTION_VALUE", "NET_TRANSACTION_VALUE", "MERCHANT_SHARE_FOR_THE_DAY",
                        "LENDER_SHARE_FOR_THE_DAY", "MERCHANT_SHARE1", "SPLIT_FUNDING_VALUE", "HELD_AMT", "LOAN_TENURE", "AC_NO", "IFSC_CODE", "TOTAL_DISB_VALUE", "SETTLED_WITH_LENDER",
                        "LOAN_OUTSTANDING", "VALUE_DATE", "MDR", "GST", "MID", "RENTAL_COLLECTED");
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
        String query = "select me_name,loan_account_number,total_transaction_value	,net_transaction_value	," +
                "merchant_share merchant_share_for_the_day ,Lender_capital_share as Lender_capital_share_for_the_day,merchant_share1,"
                + "Lender_capital_share1 as Split_Funding_value,held_amt,loan_tenure,ac_no,ifsc_code," +
                "total_disb_value,settled_with_Lender_capital,balance as Loan_Outstanding,REPLACE(CONVERT(VARCHAR(30)" +
                ", value_date, 101), ' ', '-') AS value_date,mdr,gst,mid,rental_collected from [dbo].[loan_master$] " +
                "where caption=:caption";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("caption", getname);
        List<String> results = (List<String>) q.getResultList();
        return results;
    }

    @GetMapping(value = "/getname/dwnld1/{getname}")
    public ResponseEntity<?> getMenamedwnld(@PathVariable("getname") String getname, HttpServletResponse response) {
        try {
            List<String> result1 = exceldnld2(getname);
            if (result1.size() > 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                date = simpleDateFormat.format(new Date());
//					List<String> header1 = Arrays.asList("MID","Date","Excess_Amt"," Merchant_Name","Pending_Balance");
                List<String> header1 = Arrays.asList(" MERCHANT_NAME", "MID", "PENDING_BALANCE", "DATE", "EXCESS_AMT");
                response.addHeader("content-disposition", "attachment; filename=" + getname + "_pending_" + date + ".xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(header1, result1, " MERCHANT_NAME, MID, PENDING_BALANCE, DATE, EXCESS_AMT", response.getOutputStream());
                response.flushBuffer();
            } else {
                return new ResponseEntity<>("nill data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.info("Could not update data to the database 2" + e.getMessage());
            return new ResponseEntity<>("Not downloaded", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    private List<String> exceldnld2(String getname) {
        String query = "select merchant_name,mid,pending_balance,REPLACE(CONVERT(VARCHAR(30), date, 101), ' ', '-') " +
                "AS date,excess_amt from [dbo].[loan_pending$] where caption=:caption";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("caption", getname);
        List<String> results = (List<String>) q.getResultList();
        return results;
    }

    @GetMapping(value = "/getname/dwnld2/{getname}")
    public ResponseEntity<?> getMenamedwnld1(@PathVariable("getname") String getname, HttpServletResponse response) {
        try {
            List<String> result2 = result2 = exceldnld3(getname);
            if (result2.size() > 0) {
//					List<String> header2 = Arrays.asList("ME_Name","Loan_Account_Number","Total_transaction_Value","Net_transaction_Value","Merchant_Share_for_the_day","Lender_Share_for_the_day","merchant_share1",
//													"Split_Funding_value","held_amt","Loan_Tenure","Ac_no","IFSC_Code","Total_Disb_value","Settled_with_Lender","Loan_Outstanding","INSTRUMENT_TYPE","VALUE_DATE");
                List<String> header2 = Arrays.asList("ME_NAME", "LOAN_ACCOUNT_NUMBER", "TOTAL_TRANSACTION_VALUE", "NET_TRANSACTION_VALUE", "MERCHANT_SHARE_FOR_THE_DAY", "LENDER_SHARE_FOR_THE_DAY", "MERCHANT_SHARE1",
                        "SPLIT_FUNDING_VALUE", "HELD_AMT", "LOAN_TENURE", "AC_NO", "IFSC_CODE", "TOTAL_DISB_VALUE", "SETTLED_WITH_LENDER", "LOAN_OUTSTANDING", "INSTRUMENT_TYPE", "VALUE_DATE");
                response.addHeader("content-disposition", "attachment; filename=" + getname + "_master_merchant_" + date + ".xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(header2, result2, "", response.getOutputStream());
                response.flushBuffer();

            } else {
                return new ResponseEntity<>("nill data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.info("Could not update data to the database 3" + e.getMessage());
            return new ResponseEntity<>("Not downloaded", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    private List<String> exceldnld3(String getname) {
        String query = "select me_name,loan_account_number,total_transaction_value,net_transaction_value,Merchant_Share merchant_share_for_the_day,Lender_capital_share as Lender_capital_share_for_the_day,merchant_share1,"
                + "Lender_capital_share1 as Split_Funding_value,held_amt,loan_tenure,ac_no,ifsc_code,total_disb_value,settled_with_Lender_capital,balance as Loan_Outstanding,instrument_type,"
                + "REPLACE(CONVERT(VARCHAR(30), value_date, 101), ' ', '-') AS value_date from [dbo].[loan_master$] " +
                "where total_transaction_value >0 and caption=:caption";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("caption", getname);
        List<String> results = (List<String>) q.getResultList();
        return results;
    }

    @GetMapping(value = "/getname/dwnld3/{getname}")
    public ResponseEntity<?> getMenamedwnld2(@PathVariable("getname") String getname, HttpServletResponse response) {
        try {
            List<String> results = exceldnld4(getname);

            if (results.size() > 0) {
                List<String> headers = Arrays.asList("ME_NAME", "MID", "BANK", "MERCHANT_IFSC", "MERCHANT_ACCOUNT_NUMBER", "MERCHANT_SHARE_FOR_THE_DAY", "VALUE_DATE");
                response.addHeader("content-disposition", "attachment; filename=" + getname + "_master_payout" + date + ".xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(headers, results, "", response.getOutputStream());
                response.flushBuffer();
            } else {

                return new ResponseEntity<>("nill data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.info("Could not update data to the database 4 " + e.getMessage());
            return new ResponseEntity<>("Not downloaded", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    private List<String> exceldnld4(String getname) {
        String query = "select me_name,mid,bank,MerchantIFSC,merchant_accountnumber,merchant_share " +
                "merchant_share_for_the_day ," +
                "REPLACE(CONVERT(VARCHAR(30), value_date, 101), ' ', '-') AS value_date from [dbo].[loan_master$] " +
                "where merchant_share > 0 and caption=:caption";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("caption", getname);
        List<String> results = (List<String>) q.getResultList();
        return results;
    }

    @GetMapping(value = "/getname/mail/{getname}")
    public ResponseEntity<?> getMenamemail(@PathVariable("getname") String getname, HttpServletResponse response) throws IOException {
        clixservice.createExcelForClix(getname);
        if(new File(SFTPWORKINGDIR_MIS+"/"+getname).exists()){
            FileUtils.deleteDirectory(new File(SFTPWORKINGDIR_MIS+"/"+getname));
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping(value = "/getname/insert")
    public ResponseEntity<?> getMenameinsert(HttpServletResponse response) {
        try {
            System.out.println("inside insert");
            clixservice.insertRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteAllRecords() {
        try {
            clixmstrrepo.deleteAllInBatch();
            clixdtsrepo.deleteAllInBatch();
            clixpendrepo.deleteAllInBatch();
            rentalrepo.deleteAllInBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Scheduled(cron = "0 49 17 11 * *")
//	    @Scheduled(cron="0 15 10 22-30 * TUE")
    public void rent_add() {
        int rent = clixmstrrepo.setrent();
        System.out.println(rent);
        System.out.println("out");
        int rents=clixmstrrepo.makezero();
        System.out.println(rents);
        System.out.println("out");
    }

    @GetMapping(value = "/rental_TO_Master")
    public void rent_add_Manual() {
        int rent = clixmstrrepo.setrent();
        System.out.println(rent);
        System.out.println("out");
        int rents=clixmstrrepo.makezero();
        System.out.println(rents);
        System.out.println("out");
    }

    @GetMapping(value = "/getname/dwnld4")
    public ResponseEntity<?> getMenamedwnld3( HttpServletResponse response) {
        try {
            List<String> results = exceldnld5();

            if (results.size() > 0) {
                List<String> headers = Arrays.asList("ME_NAME", "MID", "BANK", "MERCHANT_IFSC", "MERCHANT_ACCOUNT_NUMBER", "LENDER_SHARE_FOR_THE_DAY", "VALUE_DATE");
                response.addHeader("content-disposition", "attachment; filename= capital_masterlender_payout" + date + ".xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(headers, results, "", response.getOutputStream());
                response.flushBuffer();
            } else {

                return new ResponseEntity<>("nill data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.info("Could not update data to the database 4 " + e.getMessage());
            return new ResponseEntity<>("Not downloaded", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    private List<String> exceldnld5() {
        String query = "select me_name,mid,bank,MerchantIFSC,merchant_accountnumber,lender_capital_share " +
                "LENDER_SHARE_FOR_THE_DAY ," +
                "REPLACE(CONVERT(VARCHAR(30), value_date, 101), ' ', '-') AS value_date from [dbo].[loan_master$] " +
                "where lender_capital_share > 0 and caption=:caption";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("caption", "capital");
        List<String> results = (List<String>) q.getResultList();
        return results;
    }
}