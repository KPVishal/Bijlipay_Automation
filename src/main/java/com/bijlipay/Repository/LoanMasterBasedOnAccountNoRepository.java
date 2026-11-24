package com.bijlipay.Repository;

import com.bijlipay.Model.DTO.CLIXACCDTO;
import com.bijlipay.Model.DTO.ClixMasterDTO;
import com.bijlipay.Model.LoanMasterBasedOnAccountNO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LoanMasterBasedOnAccountNoRepository extends JpaRepository<LoanMasterBasedOnAccountNO,Integer> {

    @Modifying
    @Transactional
        @Query(value = "Insert into  loan_master_based_on_account_number$ ([balance] ,\n" +
                "\t[lender_capital_share] ,\n" +
                "\t[lender_capital_share1] ,\n" +
                "\t[gst] ,\n" +
                "\t[mdr] ,\n" +
                "\t[merchant_share] ,\n" +
                "\t[net_transaction_value] ,\n" +
                "\t[rental] ,\n" +
                "\t[rental_collected] ,\n" +
                "\t[settled_with_lender_capital] ,\n" +
                "\t[total_disb_value] ,\n" +
                "\t[total_transaction_value],\n" +
                "\t[loan_account_number])SELECT\n" +
                "SUM([balance])balance,SUM([lender_capital_share]),SUM([lender_capital_share1]),\n" +
                "SUM([gst]),SUM(mdr),SUM([merchant_share]),SUM([net_transaction_value]),\n" +
                "SUM([rental]),SUM([rental_collected]),SUM([settled_with_lender_capital]),SUM([total_disb_value]),\n" +
                "SUM([total_transaction_value]),[loan_account_number] FROM clix_master_acc  GROUP BY  loan_account_number",nativeQuery = true)
        void insertIntoLoanMasterBasedOnAccountNo();

    @Modifying
    @Transactional
    @Query(value = "UPDATE   m SET m.bank = f.bank,m.ac_no = f.ac_no,m.ifsc_code=f.ifsc_code,m.merchantifsc=f.merchantifsc,m.instrument_type =f.instrument_type,m.count=f.count FROM  loan_master_based_on_account_number$ m   \n" +
                "  INNER JOIN (SELECT [loan_account_number],bank,ac_no,ifsc_code,merchantifsc,instrument_type,count FROM  clix_master_acc  )\n" +
                "  f ON m.[loan_account_number] = f.[loan_account_number] ;" +
            "Update m SET m.me_name = f.merchant_name FROM  [loan_master_based_on_account_number$] m\n" +
            "   inner join(SELECT[loan_account_number], merchant_name from clix_pending_acc) f ON m.[loan_account_number] = f.[loan_account_number] ;",nativeQuery = true)
    void updateMaster();

    @Modifying
    @Transactional
    @Query(value = "UPDATE m SET m.Lender_capital_share = (m.Lender_capital_share1 * m.count) + f.pending_balance FROM [dbo].[loan_master_based_on_account_number$] m INNER JOIN(SELECT loan_account_number, pending_balance FROM [dbo].[clix_pending_acc] )f ON m.loan_account_number = f.loan_account_number ", nativeQuery = true)
    int setclixcapital();

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[loan_master_based_on_account_number$] SET value_date = GETDATE() ", nativeQuery = true)
    int setvaluedate();

    @Query(value = "SELECT new com.bijlipay.Model.DTO.CLIXACCDTO(m.loanAccountNumber, m.clixCaptialShare, m.clixCaptialShare1, m.rental, m.rentcollect, m.netTransactionValue, m.merchantShare, m.count, x.pendingBalance,"
            + "x.excessamt) FROM LoanMasterBasedOnAccountNO m Left Join ClixPendingAcc x ON m.loanAccountNumber=x.loanAccountNumber  order by m.sNo")
    List<CLIXACCDTO> getallmaster();

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[loan_master_based_on_account_number$] SET net_transaction_value=?1 ,  rental=?2, rental_collected=?3 WHERE loan_account_number=?4", nativeQuery = true)
    int update(double fNet_transaction_Value, double rental, double rent_collected, String loan_account_number);

    @Query(value = "SELECT count(*) as count FROM dbo.loanhist_masterAcc$ WHERE value_date >= ?1 and value_date <= ?1 +' 23:59:59'",nativeQuery = true)
    Map<String,Object> isTodayLogicFinisedOrNot(String currentDate);

    @Modifying
    @Transactional
    @Query(value = "UPDATE [dbo].[loan_master_based_on_account_number$] SET file_created='NA' ", nativeQuery = true)
    void updateLoanMasterBasedOnAccNo();
}
