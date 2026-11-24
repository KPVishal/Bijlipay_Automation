package com.bijlipay.Controller;

import com.bijlipay.Repository.*;
import com.bijlipay.Service.ClixAccService;
import com.bijlipay.Utils.DateUtil;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.bijlipay.Utils.Constants.yyyy_MM_dd;
import static com.bijlipay.Utils.Constants.yyyy_MM_dd_HH_MM_SS_AA;

@RestController
@RequestMapping("/api")
public class ClixAccController {

    private final Logger log = LoggerFactory.getLogger(ClixAccController.class);

    @Autowired
    private ClixMasterAccRepository clixMasterAccRepository;

    @Autowired
    private LoanMasterBasedOnAccountNoRepository loanMasterBasedOnAccountNoRepository;

    @Autowired
    private ClixAccService clixAccService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/executeTodayRental")
    public  ResponseEntity<?> clixAcc(HttpServletResponse response) throws SQLException {
        System.out.println("Date "+DateUtil.currentDate(yyyy_MM_dd) +" > Less Than "+DateUtil.currentDate(yyyy_MM_dd_HH_MM_SS_AA));
        Map<String,Object> today = loanMasterBasedOnAccountNoRepository.isTodayLogicFinisedOrNot(DateUtil.currentDate(yyyy_MM_dd));
        System.out.println("Count "+ (int)today.get("count"));
        boolean alreadyProcessd = (int)today.get("count") > 0;
        System.out.println("alreadyProcessd "+!alreadyProcessd);
        if(!alreadyProcessd) {
            System.out.println("Process Started");
            System.out.println("Delete records in loanMasterAccountNumber$");
            loanMasterBasedOnAccountNoRepository.deleteAll();
            System.out.println("Deleted loanMaster");
            log.info("ClixMasterAcc update query");
            int zero = clixMasterAccRepository.setzero();
            System.out.println(zero);
            int datedif = clixMasterAccRepository.setdatediff();
            System.out.println(datedif);
            int txn = clixMasterAccRepository.settxnvalue();
            System.out.println(txn);
            int nettxnval = clixMasterAccRepository.setnettxnvalue();
            System.out.println(nettxnval);
            int nettxnvalue = clixMasterAccRepository.setnettxnvaluezero();
            System.out.println(nettxnvalue);
            int mdr = clixMasterAccRepository.setmdr();
            System.out.println(mdr);
            int vDate = clixMasterAccRepository.setvaluedate();
            System.out.println(vDate);
            log.info("ClixMasterAcc updated");
            try {
                loanMasterBasedOnAccountNoRepository.insertIntoLoanMasterBasedOnAccountNo();
                loanMasterBasedOnAccountNoRepository.updateLoanMasterBasedOnAccNo();
            } catch (Exception e) {
                e.getMessage();
            }
            loanMasterBasedOnAccountNoRepository.updateMaster();
            int date = loanMasterBasedOnAccountNoRepository.setvaluedate();
            System.out.println(date);
            int capital = loanMasterBasedOnAccountNoRepository.setclixcapital();
            System.out.println(capital);
            log.info("loanMasterBasedOnAccountNo updated");

            log.info("loanMasterBasedOnAccountNo logic started");
            clixAccService.executingLogic();
            log.info("loanMasterBasedOnAccountNo logic Done");
            clixAccService.updateLenderCapticalShareAndMerchantShare();
            log.info("Update lender_capital_share and ");
            System.out.println("Processed");
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Already Processed", HttpStatus.OK);
    }

    @GetMapping(value = "/download/pending")
    public ResponseEntity<?> getMenamedwnld( HttpServletResponse response) {
        try {
            List<String> result1 = exportPendingDetails();
            if (result1.size() > 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String pdate = simpleDateFormat.format(new Date());
                List<String> header1 = Arrays.asList(" MERCHANT_NAME", "LOAN_ACCOUNT_NUMBER", "PENDING_BALANCE", "DATE", "EXCESS_AMT");
                response.addHeader("content-disposition", "attachment; filename=" + "Clix" + "_pending_" + pdate + ".xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(header1, result1, " MERCHANT_NAME, LOAN_ACCOUNT_NUMBER, PENDING_BALANCE, DATE, EXCESS_AMT", response.getOutputStream());
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

    @GetMapping(value = "/download/masterFile")
    public ResponseEntity<?> downloadClixMaster( HttpServletResponse response) {
        try {
            List<String> result = exportMasterDetails();
            if (result.size() > 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String mdate = simpleDateFormat.format(new Date());
                List<String> header = Arrays.asList(" ME_NAME", "LOAN_ACCOUNT_NUMBER", "TOTAL_TRANSACTION_VALUE", "NET_TRANSACTION_VALUE", "MERCHANT_SHARE",
                        "LENDER_SHARE_FOR_THE_DAY", "MERCHANT_SHARE1", "SPLIT_FUNDING_VALUE", "HELD_AMT", "LOAN_TENURE", "AC_NO", "IFSC_CODE", "TOTAL_DISB_VALUE", "SETTLED_WITH_LENDER",
                        "LOAN_OUTSTANDING", "INSTRUMENT_TYPE", "VALUE_DATE", "MDR", "GST", "RENTAL_COLLECTED");
                response.addHeader("content-disposition", "attachment; filename=" + "Clix" + "_master_" + mdate + ".xls");
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

    @GetMapping("/insertClicksAccRecords")
    public ResponseEntity<?> insertRecords() throws Exception {
        log.info("Inserting Started");
        clixAccService.insertClixFileAcc();
        log.info("Inserting Process Done");
        return new ResponseEntity<>("Inserted SuccessFully", HttpStatus.OK);
    }

    private List<String> exportPendingDetails() {
        String query = "select merchant_name,loan_account_number,pending_balance,REPLACE(CONVERT(VARCHAR(30), date, 101), ' ', '-') " +
                "AS date,excess_amt from [dbo].[clix_pending_acc] ";
        Query q = entityManager.createNativeQuery(query);
        List<String> results = (List<String>) q.getResultList();
        return results;
    }

    private List<String> exportLoanMasterDetails() {
        String query = "select me_name,loan_account_number,total_transaction_value,net_transaction_value,merchant_share merchant_share_for_the_day ,Lender_capital_share as Lender_capital_share_for_the_day,merchant_share1,\n" +
                " Lender_capital_share1 as Split_Funding_value,held_amt,loan_tenure,ac_no,ifsc_code,total_disb_value,settled_with_Lender_capital,balance as Loan_Outstanding,instrument_type as INSTRUMENT_TYPE,REPLACE(CONVERT(VARCHAR(30)\n" +
                ", value_date, 101), ' ', '-') AS value_date,mdr,gst,rental_collected from [dbo].[loan_master_based_on_account_number$]";
        Query q = entityManager.createNativeQuery(query);
        List<String> results = (List<String>) q.getResultList();
        return results;
    }

    private List<String> exportMasterDetails() {
        String query = "select me_name,loan_account_number,total_transaction_value,net_transaction_value,merchant_share merchant_share_for_the_day ,Lender_capital_share as Lender_capital_share_for_the_day,merchant_share1,\n" +
                " Lender_capital_share1 as Split_Funding_value,held_amt,loan_tenure,ac_no,ifsc_code,total_disb_value,settled_with_Lender_capital,balance as Loan_Outstanding,instrument_type as INSTRUMENT_TYPE,REPLACE(CONVERT(VARCHAR(30)\n" +
                ", value_date, 101), ' ', '-') AS value_date,mdr,gst,rental_collected from [dbo].[clix_master_acc]";
        Query q = entityManager.createNativeQuery(query);
        List<String> results = (List<String>) q.getResultList();
        return results;
    }

    @GetMapping(value = "/download/AccNoMaster")
    public ResponseEntity<?> downloadClix( HttpServletResponse response){
        try {
            List<String> result = exportLoanMasterDetails();
            if (result.size() > 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String mdate = simpleDateFormat.format(new Date());
                List<String> header = Arrays.asList(" ME_NAME", "LOAN_ACCOUNT_NUMBER", "TOTAL_TRANSACTION_VALUE", "NET_TRANSACTION_VALUE", "MERCHANT_SHARE",
                        "LENDER_SHARE_FOR_THE_DAY", "MERCHANT_SHARE1", "SPLIT_FUNDING_VALUE", "HELD_AMT", "LOAN_TENURE", "AC_NO", "IFSC_CODE", "TOTAL_DISB_VALUE", "SETTLED_WITH_LENDER",
                        "LOAN_OUTSTANDING", "INSTRUMENT_TYPE", "VALUE_DATE", "MDR", "GST", "RENTAL_COLLECTED");
                response.addHeader("content-disposition", "attachment; filename=" + "ClixLoanAcc" + "_master_" + mdate + ".xls");
                response.setContentType("application/vnd.ms-excel");
                new SimpleExporter().gridExport(header, result, "", response.getOutputStream());
            } else {
                return new ResponseEntity<>("nill data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.info("Could not update data to the database 1" + e.getMessage());
            return new ResponseEntity<>("Not downloaded", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("Processed");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


}
