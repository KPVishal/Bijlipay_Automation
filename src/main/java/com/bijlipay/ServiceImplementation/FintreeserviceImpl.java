package com.bijlipay.ServiceImplementation;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.bijlipay.Model.FintreeDetail;
import com.bijlipay.Model.FintreeMaster;
import com.bijlipay.Model.FintreePending;
import com.bijlipay.Model.Loan_rental;
import com.bijlipay.Model.DTO.FintreeMasterDTO;
import com.bijlipay.Repository.FintreeDetailRepository;
import com.bijlipay.Repository.FintreePendingRepository;
import com.bijlipay.Repository.FintreeRepo;
import com.bijlipay.Repository.RentalRepository;
import com.bijlipay.Service.FileStorageService;
import com.bijlipay.Service.FintreeService;
import com.bijlipay.Utils.Constants;

@Service
public class FintreeserviceImpl implements FintreeService{

	 private final Logger log = LoggerFactory.getLogger(ServiceImpl.class);
	    public boolean FIRSTROWDAILY = true;
	    String strMID = null;
	    String strTID = null;
	    double fClix_Capital_Share = 0;
	    double fNet_transaction_Value = 0;
	    double fMerchant_Share = 0;
	    double fptable = 0;
	    double fSettled_with_Clix_Capital = 0;
	    double fClix_capital_Share1 = 0;
	    double excess_amt = 0;
	    int icount = 0;
	    double _tmpFund = 0;
	    int flag = 0;
	    int updateCount = 0;
	    double rental;
	    double rent_collected;
	    List<String> result = null;
	    String update = null;
	    String updates = null;
	    String Filename = null;

	    @Autowired
	    private FileStorageService fileStorageService;
	    @Autowired
	    private RentalRepository rentalrepo;
	    @Autowired
	    private FintreeRepo fintreerepo;

	    @Autowired
	    private FintreeDetailRepository clixdtsrepo;

	    @Autowired
	    private FintreePendingRepository clixpndngrepo;

	
	public Object processFintree(String getname, HttpServletResponse response) throws Exception {
        log.info("inside update query");
        int zero = fintreerepo.setzero(getname);
        System.out.println(zero);
        int datedif = fintreerepo.setdatediff(getname);
        System.out.println(datedif);
        int txn = fintreerepo.settxnvalue(getname);
        System.out.println(txn);
        int nettxnval = fintreerepo.setnettxnvalue(getname);
        System.out.println(nettxnval);
        int nettxnvalue = fintreerepo.setnettxnvaluezero(getname);
        System.out.println(nettxnvalue);
        int mdr = fintreerepo.setmdr(getname);
        System.out.println(mdr);
        int capital = fintreerepo.setclixcapital(getname);
        System.out.println(capital);
        int date = fintreerepo.setvaluedate(getname);
        System.out.println(date);
        List<FintreeMasterDTO> query = fintreerepo.getallmaster(getname);
        if (query.size() > 0) {
            query.forEach(l -> {
                System.out.println(l.getMid());
                System.out.println(l.getClixCaptialShare());
                System.out.println(l.getNetTransactionValue());
                System.out.println(l.getMerchantShare());
                System.out.println(l.getCount());
                System.out.println(l.getExcess_amt());
                System.out.println("rental>>>>" + l.getRental());
                System.out.println("rentcollected>>>>" + l.getRentcollect());

                strMID = l.getMid();
                strTID=l.getTid();
                fClix_Capital_Share = l.getClixCaptialShare();
                fNet_transaction_Value = l.getNetTransactionValue();
                fMerchant_Share = l.getMerchantShare();
                icount = l.getCount();
                excess_amt = l.getExcess_amt();
                fClix_capital_Share1 = l.getClixCaptialShare1() * icount;
                rental = l.getRental();
                rent_collected = l.getRentcollect();
                if (rental != 0) {
                    if (fNet_transaction_Value > 0) {
                        if (fNet_transaction_Value > rental) {
                            rent_collected = rental;
                            fNet_transaction_Value = fNet_transaction_Value - rental;
                            rental = 0;
                            System.out.println("insite");
                        } else {
                            rent_collected = fNet_transaction_Value;
                            rental = rental - fNet_transaction_Value;
                            fNet_transaction_Value = 0;
                        }
                        System.out.println("inside update");
                        int txnupdate = fintreerepo.update(fNet_transaction_Value, rental, rent_collected, l.getMid());
                        System.out.println(txnupdate);
                    }
                }
                if (icount > 0) {
                    if (excess_amt > 0) {
                        if (fClix_Capital_Share <= excess_amt) {
                            excess_amt = excess_amt - fClix_Capital_Share;
                            fMerchant_Share = fNet_transaction_Value;
                            fClix_Capital_Share = 0;
                            flag = 2;
                            System.out.println(flag);
                        } else if (fClix_Capital_Share > excess_amt) {
                            _tmpFund = fClix_Capital_Share - excess_amt;
                            System.out.println(_tmpFund);
                            if (_tmpFund <= fNet_transaction_Value && fNet_transaction_Value > 0) {
                                fMerchant_Share = fNet_transaction_Value - _tmpFund;
                                fptable = 0;
                                excess_amt = 0;
                                fClix_Capital_Share = _tmpFund;
                                flag = 2;
                                System.out.println("_tmpfund" + flag);
                            } else if ((_tmpFund > (fNet_transaction_Value) && fNet_transaction_Value > 0)) {
                                fMerchant_Share = 0;
                                fptable = _tmpFund - fNet_transaction_Value;
                                fClix_Capital_Share = fNet_transaction_Value;
                                excess_amt = 0;
                                flag = 2;
                                System.out.println("_tmpfund2" + flag);
                            } else if (fNet_transaction_Value == 0) {
                                fptable = fClix_capital_Share1 - excess_amt;
                                fClix_Capital_Share = fNet_transaction_Value;
                                fSettled_with_Clix_Capital = fClix_Capital_Share;
                                excess_amt = 0;
                                flag = 2;
                                System.out.println("_tmpfund3" + flag);
                            }
                        }
                    } else if ((fClix_Capital_Share <= fNet_transaction_Value) && fNet_transaction_Value > 0) {
                        fMerchant_Share = fNet_transaction_Value - fClix_Capital_Share;
                        fptable = 0;
                        System.out.println(fptable);
                    } else if ((fClix_Capital_Share > fNet_transaction_Value) && fNet_transaction_Value > 0) {
                        fMerchant_Share = 0;
                        fptable = fClix_Capital_Share - fNet_transaction_Value;
                        fClix_Capital_Share = fNet_transaction_Value;
                        System.out.println(fClix_Capital_Share);
                    } else if (fNet_transaction_Value == 0) {
                        fptable = fClix_Capital_Share;
                        fClix_Capital_Share = fNet_transaction_Value;
                        fSettled_with_Clix_Capital = fClix_Capital_Share;
                        flag = 1;
                        System.out.println(flag);
                    }
                    Connection connection = null;
                    try {
                        connection = DriverManager.getConnection(Constants.URL_Auth, Constants.DBUSERNAME, Constants.DBPASSWORD);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    log.info("Successfully Connected to the database!");
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        if (flag == 0) {
                            System.out.println("flag0");
                            update = "UPDATE [dbo].[fintree_master$] set merchant_share = " + fMerchant_Share + " ,lender_Capital_Share = " + fClix_Capital_Share + " where TID = '" + strTID + "';"
                                    + "update [dbo].[Fintree_pending$] set pending_balance = " + fptable + " where TID = '" + strTID + "';"
                                    + "UPDATE [dbo].[fintree_master$]  SET Settled_with_lender_Capital = lender_capital_Share FROM [dbo].[fintree_master$] where TID = '" + strTID + "'; "
                                    + "update [dbo].[fintree_master$] set balance = balance - lender_Capital_Share where TID = '" + strTID + "';";
                        } else if (flag == 1) {
                            System.out.println("flag1");
                            update = "UPDATE [dbo].[fintree_master$] set merchant_share = " + fMerchant_Share + " ,lender_Capital_Share = " + fClix_Capital_Share + " where TID = '" + strTID + "';"
                                    + "update [dbo].[Fintree_pending$] set pending_balance = " + fptable + " where TID = '" + strTID + "';"
                                    + "UPDATE [dbo].[fintree_master$]  SET Settled_with_lender_Capital = lender_capital_Share FROM [dbo].[fintree_master$] where TID = '" + strTID + "';"
                                    + "update [dbo].[fintree_master$] set balance = balance - lender_Capital_Share where TID = '" + strTID + "';";

                        } else if (flag == 2) {
                            System.out.println("flag2");
                            update = "UPDATE [dbo].[fintree_master$] set merchant_share = " + fMerchant_Share + " ,lender_Capital_Share = " + fClix_Capital_Share + " where TID = '" + strTID + "';"
                                    + "update [dbo].[Fintree_pending$] set pending_balance = " + fptable + " , excess_amt = " + excess_amt + " where TID = '" + strTID + "';"
                                    + "UPDATE [dbo].[fintree_master$]  SET Settled_with_lender_Capital = lender_capital_Share FROM [dbo].[fintree_master$] where TID = '" + strTID + "'; "
                                    + "update [dbo].[fintree_master$] set balance = balance - lender_Capital_Share where TID = '" + strTID + "';";
                        } else {
                            System.out.println("else");
                            update = "UPDATE [dbo].[fintree_master$] set merchant_share = 0 ,lender_Capital_Share = 0 , Settled_with_lender_Capital=0 where TID = '" + strTID + "'; "
                                    + "update [dbo].[Fintree_pending$] set pending_balance = pending_balance + " + fptable + " where TID = '" + strTID + "';";
                        }
                        fptable = 0;
                        updateCount = statement.executeUpdate(update);
                        log.info("Updated test_value successfully : " + updateCount);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(">>>>>>>>>>>>");
                int eligiblevalue = fintreerepo.gettev(strMID);
                System.out.println(eligiblevalue);
             
            });
        }
        log.info("Next backup");
        log.info("Taking Backup");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(Constants.URL_Auth, Constants.DBUSERNAME, Constants.DBPASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("Successfully Connected to the database!");
        Statement statement = connection.createStatement();
        if (flag != 2) {
            updates = "update [dbo].[Fintree_pending$] set pending_balance =  0 , excess_amt = excess_amt - pending_balance  where  excess_amt > 0 and   pending_balance > 0  and pending_balance <=  excess_amt and caption= '" + getname + "' ";
            updateCount = statement.executeUpdate(updates);
            log.info("Updated test_value successfully1 : " + updateCount);

            String updates1 = "update [dbo].[Fintree_pending$] set pending_balance = pending_balance - excess_amt , excess_amt =0 where  excess_amt > 0 and   pending_balance > 0  and excess_amt <= pending_balance and caption= '" + getname + "' ";
            updateCount = statement.executeUpdate(updates1);
            log.info("Updated test_value successfully2 : " + updateCount);

            String update2 = "Update [dbo].[Fintree_pending$] set date = GETDATE() where caption= '" + getname + "' ";
            String update3  ="Insert into [dbo].[loanhist_pending$](mid,caption,date,excess_amt,merchant_name,pending_balance) select mid,caption,date,excess_amt,merchant_name,pending_balance from " +
                    "[dbo].[Fintree_pending$] where caption= '" + getname + "'" ;
            String update4  =" Insert  INTO loanhist_master$(ME_Name,Loan_Account_Number,Total_transaction_Value,Net_transaction_Value,Merchant_Share,Lender_Capital_Share,Merchant_Share1,Lender_capital_Share1,Held_Amt,Loan_Tenure,Ac_no,IFSC_Code,Total_Disb_value,Settled_with_Lender_Capital,Balance,\n" +
                    " INSTRUMENT_TYPE,VALUE_DATE,MDR,GST,TID,MID,Rental,Rental_Collected,count,File_Created,BANK,MerchantIFSC,merchant_accountnumber,Caption,format,Mail_id) select ME_Name,Loan_Account_Number,Total_transaction_Value,Net_transaction_Value,Merchant_Share,\n" +
                    " Lender_Capital_Share,Merchant_Share1,Lender_capital_Share1,Held_Amt,Loan_Tenure,Ac_no,IFSC_Code,Total_Disb_value,Settled_with_Lender_Capital,Balance,\n" +
                    " INSTRUMENT_TYPE,VALUE_DATE,MDR,GST,TID,MID,Rental,Rental_Collected,count,File_Created,BANK,MerchantIFSC,merchant_accountnumber,Caption,format,Mail_id from [dbo].[fintree_master$]\n where caption= '" + getname + "'";
            updateCount = statement.executeUpdate(update2);
            log.info("Updated test_value successfully3 : " + updateCount);
            updateCount = statement.executeUpdate(update3);
            log.info("Updated test_value successfully4 : " + updateCount);
            updateCount = statement.executeUpdate(update4);
            log.info("Updated test_value successfully5 : " + updateCount);
            log.info("Done");
        }
        
        
        connection.close();
		return null;
        
    }


	@Override
	public Object update3value(String getname, HttpServletResponse response) {
		
		 List<FintreeMasterDTO> txn=fintreerepo.nettxn();
		 if (txn.size() > 0) {
			 txn.forEach(l -> {
		   if(l.getTotal_eligible_value() >0.0 ) {
			   
			   System.out.println("insideupdate3value");
        int value_allocated_perc = fintreerepo.value_allocated_perc(l.getTid(),l.getMid());
        System.out.println(value_allocated_perc);
        
        int value_allocated = fintreerepo.value_allocated(l.getTid());
        System.out.println(value_allocated);
		   }
			 });
			 System.out.println("outsideupdate3value");
		 }
		 int merchant_share = fintreerepo.merchant_share();
		return null;
	}
	

    public void insertRecords() throws Exception {

        Resource resource = fileStorageService.loadFileAsResource("Fintreefile.xlsx");
        Workbook wb = new XSSFWorkbook(new FileInputStream(resource.getFile().getAbsolutePath()));
        List<FintreeMaster> map = new ArrayList<>();

        for (Sheet sheet : wb) {
            FIRSTROWDAILY = true;
            if (sheet.getSheetName().equals("loan_master")) {

                for (Row row : sheet) {
                    if (FIRSTROWDAILY) {
                        FIRSTROWDAILY = false;
                        continue;
                    }
                    FintreeMaster misTable = new FintreeMaster();
                    int cellValue = 1;
                    misTable.setMeName(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("1");
                    misTable.setLoanAccountNumber(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("2");
                    misTable.setTotalTransactionValue(Double.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("3");
                    misTable.setNetTransactionValue(Double.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("4");
                    misTable.setMerchantShare(Double.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("5");
                    misTable.setClixCaptialShare(Float.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("6");
                    misTable.setMerchantShare1(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("7");
                    misTable.setClixCaptialShare1(Float.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("8");
                    misTable.setHeldAmt(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("9");
                    misTable.setLoanTenure(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("18");
                    misTable.setAcNo(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("19");
                    misTable.setIfscCode(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("10");
                    misTable.setTotalDisbValue(Double.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("11");
                    misTable.setSettledwithClixCapital(Double.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("12");
                    misTable.setBalance(Double.valueOf(String.format("%.2f", row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("13");
                    misTable.setiNSTRUMENTTYPE(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("14");
                    misTable.setvALUEDATE(row.getCell(cellValue++).getDateCellValue());
                    System.out.println("15");
                    misTable.setMdr(Double.valueOf(String.format("%.2f", row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("16");
                    misTable.setGst(Double.valueOf(String.format("%.2f", row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("17");
                    misTable.setMid(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("20");
                    misTable.setCount(Integer.valueOf((int) row.getCell(cellValue++).getNumericCellValue()));
                    System.out.println("21");
                    misTable.setCreatedat(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("22");
                    misTable.setBank(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("23");
                    misTable.setMerchantifsc(
                            row.getCell(cellValue++).getStringCellValue());
                    System.out.println("24");
                    misTable.setMeaccntno(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("25");
                    misTable.setCaption(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("26");
                    misTable.setFormat(Integer.valueOf((int) row.getCell(cellValue++).getNumericCellValue()));
                    System.out.println("27");
                    misTable.setMail_id(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("28");
                    misTable.setTid(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("29");
                    misTable.setRental(Double.valueOf(String.format("%.2f", row.getCell(cellValue++).getNumericCellValue())));
                    misTable.setRentcollect(Double.valueOf(String.format("%.2f", row.getCell(cellValue++).getNumericCellValue())));
                    map.add(misTable);
                }
                fintreerepo.saveAll(map);
                System.out.println("master done");
            } else if (sheet.getSheetName().equals("loan_details")) {

                List<FintreeDetail> mapdetails = new ArrayList<>();

                FIRSTROWDAILY = true;
                for (Row row : sheet) {
                    if (FIRSTROWDAILY) {
                        FIRSTROWDAILY = false;
                        continue;
                    }
                    FintreeDetail misdetails = new FintreeDetail();
                    int cellValue = 1;
                    misdetails.setMeName(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("1");
                    misdetails.setTid(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("2");
                    misdetails.setMid(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("3");
                    misdetails.setClixCaptialShare(Double.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("4");
                    misdetails.setLoanTenure(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("5");
                    misdetails.setLoanAccountNumber(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("6");
                    misdetails.setCaption(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("7");
                    mapdetails.add(misdetails);
                }
                System.out.println("details");
                clixdtsrepo.saveAll(mapdetails);
            } else if (sheet.getSheetName().equals("loan_pending")) {
                List<FintreePending> mappendings = new ArrayList<>();

                FIRSTROWDAILY = true;
                for (Row row : sheet) {
                    if (FIRSTROWDAILY) {
                        FIRSTROWDAILY = false;
                        continue;
                    }
                    FintreePending misTable = new FintreePending();
                    int cellValue = 0;

                    misTable.setMerhcantName(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("1");
                    misTable.setMid(row.getCell(cellValue++).getStringCellValue());
                    misTable.setTid(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("2");
                    misTable.setPendingBalance(Double.valueOf(String.format("%.2f",
                            row.getCell(cellValue++).getNumericCellValue())));
                    System.out.println("3");
                    misTable.setDate(row.getCell(cellValue++).getDateCellValue());
                    System.out.println("4");
                    misTable.setExcessamt(row.getCell(cellValue++).getNumericCellValue());
                    System.out.println("5");
                    misTable.setCaption(row.getCell(cellValue++).getStringCellValue());

                    mappendings.add(misTable);
                }
                clixpndngrepo.saveAll(mappendings);
            } else if (sheet.getSheetName().equals("loan_rental")) {
                List<Loan_rental> rental = new ArrayList<>();

                FIRSTROWDAILY = true;
                for (Row row : sheet) {
                    if (FIRSTROWDAILY) {
                        FIRSTROWDAILY = false;
                        continue;
                    }
                    Loan_rental loanrental = new Loan_rental();
                    int cellValue = 0;
                    loanrental.setMid(row.getCell(cellValue++).getStringCellValue());
                    System.out.println("1");
                    loanrental.setRent(row.getCell(cellValue++).getNumericCellValue());
                    System.out.println("2");
                    loanrental.setMetype(row.getCell(cellValue++).getStringCellValue());
                    rental.add(loanrental);
                }
                rentalrepo.saveAll(rental);
            }
        }


    }
}
