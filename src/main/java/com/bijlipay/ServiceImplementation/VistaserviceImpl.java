package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.DTO.ClixMasterDTO;
import com.bijlipay.Repository.ClixMasterRepository;
import com.bijlipay.Service.VistaService;
import com.bijlipay.Utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class VistaserviceImpl implements VistaService {

    private final Logger log = LoggerFactory.getLogger(VistaserviceImpl.class);
    String strMID = null;
    double fvista_Capital_Share = 0;
    double fNet_transaction_Value = 0;
    double fMerchant_Share = 0;
    double fptable = 0;
    double fSettled_with_vista_Capital = 0;
    double fvista_capital_Share1 = 0;
    double Excess_Amt = 0;
    int icount = 0;
    double _tmpFund = 0;
    int flag = 0;
    double rental;
    double rent_collected;
    @Autowired
    private ClixMasterRepository clixmstrrepo;

    @Override
    public void processvista(String getname, HttpServletResponse response) throws Exception {
        log.info("inside update query");
        clixmstrrepo.updateallquery(getname);
        List<ClixMasterDTO> query = clixmstrrepo.getallvistamaster(getname);
        if (query.size() > 0) {
            query.forEach(l -> {
                System.out.println("Calculate Merchant Share");
                flag = 0;
                icount = 0;
                Excess_Amt = 0;
                fvista_Capital_Share = 0;
                fNet_transaction_Value = 0;
                fMerchant_Share = 0;
                fptable = 0;
                fvista_capital_Share1 = 0;
                strMID = l.getMid();
                fvista_Capital_Share = l.getClixCaptialShare();
                fNet_transaction_Value = l.getNetTransactionValue();
                fMerchant_Share = l.getMerchantShare();
                icount = l.getCount();
                Excess_Amt = l.getExcess_amt();
                fvista_capital_Share1 = l.getClixCaptialShare1() * icount;
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
                        int txnupdate = clixmstrrepo.update(fNet_transaction_Value, rental, rent_collected, l.getMid());
                        System.out.println(txnupdate);
                    }
                }
                if (icount > 0) {
                    if (Excess_Amt > 0) {
                        if (fvista_Capital_Share <= Excess_Amt) {
                            Excess_Amt = Excess_Amt - fvista_Capital_Share;
                            fMerchant_Share = fNet_transaction_Value;
                            fvista_Capital_Share = 0;
                            flag = 2;
                        } else if (fvista_Capital_Share > Excess_Amt) {
                            _tmpFund = fvista_Capital_Share - Excess_Amt;
                            if (_tmpFund <= fNet_transaction_Value && fNet_transaction_Value > 0) {
                                fMerchant_Share = fNet_transaction_Value - _tmpFund;
                                fptable = 0;
                                Excess_Amt = 0;
                                fvista_Capital_Share = _tmpFund;
                                flag = 2;
                            } else if ((_tmpFund > (fNet_transaction_Value) && fNet_transaction_Value > 0)) {
                                fMerchant_Share = 0;
                                fptable = _tmpFund - fNet_transaction_Value;
                                fvista_Capital_Share = fNet_transaction_Value;
                                Excess_Amt = 0;
                                flag = 2;
                            } else if (fNet_transaction_Value == 0) {
                                fptable = fvista_capital_Share1 - Excess_Amt;
                                fvista_Capital_Share = fNet_transaction_Value;
                                fSettled_with_vista_Capital = fvista_Capital_Share;
                                Excess_Amt = 0;
                                flag = 2;
                            }
                        }

                    } else if ((fvista_Capital_Share <= fNet_transaction_Value) && fNet_transaction_Value > 0) {
                        fMerchant_Share = fNet_transaction_Value - fvista_Capital_Share;
                        fptable = 0;
                    } else if ((fvista_Capital_Share > fNet_transaction_Value) && fNet_transaction_Value > 0) {
                        fMerchant_Share = 0;
                        fptable = fvista_Capital_Share - fNet_transaction_Value;
                        fvista_Capital_Share = fNet_transaction_Value;
                    } else if (fNet_transaction_Value == 0) {
                        fptable = fvista_Capital_Share;
                        fvista_Capital_Share = fNet_transaction_Value;
                        fSettled_with_vista_Capital = fvista_Capital_Share;
                        flag = 1;
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
                        String update;
                        if (flag == 0) {
                            update = "UPDATE [dbo].[loan_master$] set merchant_share = " + fMerchant_Share + " ,lender_Capital_Share = " + fvista_Capital_Share + " where MID = '" + strMID + "';update [dbo].[loan_pending$] set pending_balance = " + fptable + " where MID = '" + strMID + "';UPDATE [dbo].[loan_master$]  SET Settled_with_lender_Capital = lender_capital_Share FROM [dbo].[loan_master$] where MID = '" + strMID + "'; update [dbo].[loan_master$] set balance = balance - lender_Capital_Share where MID = '" + strMID + "';";
                        } else if (flag == 1) {
                            update = "UPDATE [dbo].[loan_master$] set merchant_share = " + fMerchant_Share + " ,lender_Capital_Share = " + fvista_Capital_Share + " where MID = '" + strMID + "';update [dbo].[loan_pending$] set pending_balance = " + fptable + " where MID = '" + strMID + "';UPDATE [dbo].[loan_master$]  SET Settled_with_lender_Capital = lender_capital_Share FROM [dbo].[loan_master$] where MID = '" + strMID + "';update [dbo].[loan_master$] set balance = balance - lender_Capital_Share where MID = '" + strMID + "';";
                        } else if (flag == 2) {
                            update = "UPDATE [dbo].[loan_master$] set merchant_share = " + fMerchant_Share + " ,lender_Capital_Share = " + fvista_Capital_Share + " where MID = '" + strMID + "';update [dbo].[loan_pending$] set pending_balance = " + fptable + " , Excess_Amt = " + Excess_Amt + " where MID = '" + strMID + "';UPDATE [dbo].[loan_master$]  SET Settled_with_lender_Capital = lender_capital_Share FROM [dbo].[loan_master$] where MID = '" + strMID + "'; update [dbo].[loan_master$] set balance = balance - lender_Capital_Share where MID = '" + strMID + "';";
                        } else {
                            update = "UPDATE [dbo].[loan_master$] set merchant_share = 0 ,lender_Capital_Share = 0 , Settled_with_lender_Capital=0 where MID = '" + strMID + "'; update [dbo].[loan_pending$] set pending_balance = pending_balance + " + fptable + " where MID = '" + strMID + "';";
                        }
                        fptable = 0;
                        int updateCount = statement.executeUpdate(update);
                        log.info("Updated test_value successfully : " + updateCount);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
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
        String update;
        if (flag != 2) {
            update = "update [dbo].[loan_pending$] set Pending_Balance =  0 , Excess_Amt = Excess_Amt - Pending_Balance  where  Excess_Amt > 0 and   Pending_Balance > 0  and Pending_Balance <=  Excess_Amt and caption= '" + getname + "' ";
            int updateCount = statement.executeUpdate(update);
            log.info("Updated test_value successfully1 : " + updateCount);
            update = "update [dbo].[loan_pending$] set Pending_Balance = Pending_Balance - Excess_Amt , Excess_Amt =0 where  Excess_Amt > 0 and   Pending_Balance > 0  and Excess_Amt <= Pending_Balance and caption= '" + getname + "' ";
            updateCount = statement.executeUpdate(update);
            log.info("Updated test_value successfully2 : " + updateCount);
            update = "update  [dbo].[loan_pending$] set date = GETDATE() where caption= '" + getname + "'";
            String update3  ="Insert into [dbo].[loanhist_pending$](mid,caption,date,excess_amt,merchant_name,pending_balance) select mid,caption,date,excess_amt,merchant_name,pending_balance from " +
                    "[dbo].[loan_pending$] where caption= '" + getname + "'" ;
            String update4  =" Insert  INTO loanhist_master$(ME_Name,Loan_Account_Number,Total_transaction_Value,Net_transaction_Value,Merchant_Share,Lender_Capital_Share,Merchant_Share1,Lender_capital_Share1,Held_Amt,Loan_Tenure,Ac_no,IFSC_Code,Total_Disb_value,Settled_with_Lender_Capital,Balance,\n" +
                    " INSTRUMENT_TYPE,VALUE_DATE,MDR,GST,TID,MID,Rental,Rental_Collected,count,File_Created,BANK,MerchantIFSC,merchant_accountnumber,Caption,format,Mail_id) select ME_Name,Loan_Account_Number,Total_transaction_Value,Net_transaction_Value,Merchant_Share,\n" +
                    " Lender_Capital_Share,Merchant_Share1,Lender_capital_Share1,Held_Amt,Loan_Tenure,Ac_no,IFSC_Code,Total_Disb_value,Settled_with_Lender_Capital,Balance,\n" +
                    " INSTRUMENT_TYPE,VALUE_DATE,MDR,GST,TID,MID,Rental,Rental_Collected,count,File_Created,BANK,MerchantIFSC,merchant_accountnumber,Caption,format,Mail_id from [dbo].[loan_master$]\n where caption= '" + getname + "'";
            updateCount = statement.executeUpdate(update);
            log.info("Updated test_value successfully3 : " + updateCount);
            updateCount = statement.executeUpdate(update3);
            log.info("Updated test_value successfully3 : " + updateCount);
            updateCount = statement.executeUpdate(update4);
            log.info("Updated test_value successfully3 : " + updateCount);
        }
        log.info("Done");
        connection.close();
    }
}


