package com.bijlipay.Model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import org.hibernate.annotations.GeneratorType;

@Entity
@Table(name = "loan_master$")
public class ClixMaster {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "[S_No]")
    private int sNo;

    @Column(name = "[ME_Name]")
    private String meName;

    @Column(name = "[Loan_Account_Number]")
    private String loanAccountNumber;

    @Column(name = "[Total_transaction_Value]")
    private Double totalTransactionValue;

    @Column(name = "[Net_transaction_Value]")
    private Double netTransactionValue;

    @Column(name = "[Merchant_Share]")
    private Double merchantShare;

    @Column(name = "[Lender_Capital_Share]")
    private float clixCaptialShare;

    @Column(name = "[Merchant_Share1]")
    private String merchantShare1;

    @Column(name = "[Lender_capital_Share1]")
    private float clixCaptialShare1;

    @Column(name = "[Held_Amt]")
    private String heldAmt;

    @Column(name = "[Loan_Tenure]")
    private String loanTenure;

    @Column(name = "[Ac_no]")
    private String acNo;

    @Column(name = "[IFSC_Code]")
    private String ifscCode;

    @Column(name = "[Total_Disb_value]")
    private Double totalDisbValue;

    @Column(name = "[Settled_with_Lender_Capital]")
    private Double settledwithClixCapital;

    @Column(name = "[Balance]")
    private Double balance;

    @Column(name = "[INSTRUMENT_TYPE]")
    private String iNSTRUMENTTYPE;

    @Column(name = "[VALUE_DATE]")
    private Date vALUEDATE;

    @Column(name = "[MDR]")
    private Double mdr;

    @Column(name = "[GST]")
    private Double gst;

    @Column(name = "[TID]")
    private String tid;

    @Column(name = "[MID]")
    private String mid;
    
    @Column(name = "[Rental]")
    private Double rental;
    
    @Column(name = "[Rental_Collected]")
    private Double rentcollect;

    @Column(name = "[count]")
    private int count;
    
    @Column(name="[File_Created]")
    private String createdat;
    
    @Column(name="BANK")
    private String bank;
    
    @Column(name="MerchantIFSC")
    private String merchantifsc;
    
    @Column(name="MerchantAccountnumber")
    private String meaccntno;
    
    @Column(name = "[Caption]")
    private String caption;

    @Column(name = "[Format]")
    private int format;

    @Column(name = "[Mail_id]")
    private String mail_id;
    
//    @Column(name="[Txn_Count]")
//    private Double txncount;
//    
//	public Double getTxncount() {
//		return txncount;
//	}
//
//	public void setTxncount(Double txncount) {
//		this.txncount = txncount;
//	}

	public Double getRental() {
		return rental;
	}

	public void setRental(Double rental) {
		this.rental = rental;
	}

	public Double getRentcollect() {
		return rentcollect;
	}

	public void setRentcollect(Double rentcollect) {
		this.rentcollect = rentcollect;
	}

	public String getCreatedat() {
		return createdat;
	}

	public void setCreatedat(String createdat) {
		this.createdat = createdat;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getMerchantifsc() {
		return merchantifsc;
	}

	public void setMerchantifsc(String merchantifsc) {
		this.merchantifsc = merchantifsc;
	}

	public String getMeaccntno() {
		return meaccntno;
	}

	public void setMeaccntno(String meaccntno) {
		this.meaccntno = meaccntno;
	}

	public int getFormat() {
		return format;
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public String getMail_id() {
		return mail_id;
	}

	public void setMail_id(String mail_id) {
		this.mail_id = mail_id;
	}

	public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getsNo() {
        return sNo;
    }

    public void setsNo(int sNo) {
        this.sNo = sNo;
    }

    public String getMeName() {
        return meName;
    }

    public void setMeName(String meName) {
        this.meName = meName;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public Double getTotalTransactionValue() {
        return totalTransactionValue;
    }

    public void setTotalTransactionValue(Double totalTransactionValue) {
        this.totalTransactionValue = totalTransactionValue;
    }

    public Double getNetTransactionValue() {
        return netTransactionValue;
    }

    public void setNetTransactionValue(Double netTransactionValue) {
        this.netTransactionValue = netTransactionValue;
    }

    public Double getMerchantShare() {
        return merchantShare;
    }

    public void setMerchantShare(Double merchantShare) {
        this.merchantShare = merchantShare;
    }

    public float getClixCaptialShare() {
        return clixCaptialShare;
    }

    public void setClixCaptialShare(float clixCaptialShare) {
        this.clixCaptialShare = clixCaptialShare;
    }

    public String getMerchantShare1() {
        return merchantShare1;
    }

    public void setMerchantShare1(String merchantShare1) {
        this.merchantShare1 = merchantShare1;
    }

    public float getClixCaptialShare1() {
        return clixCaptialShare1;
    }

    public void setClixCaptialShare1(float clixCaptialShare1) {
        this.clixCaptialShare1 = clixCaptialShare1;
    }

    public String getHeldAmt() {
        return heldAmt;
    }

    public void setHeldAmt(String heldAmt) {
        this.heldAmt = heldAmt;
    }

    public String getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(String loanTenure) {
        this.loanTenure = loanTenure;
    }

    public String getAcNo() {
        return acNo;
    }

    public void setAcNo(String acNo) {
        this.acNo = acNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public Double getTotalDisbValue() {
        return totalDisbValue;
    }

    public void setTotalDisbValue(Double totalDisbValue) {
        this.totalDisbValue = totalDisbValue;
    }

    public Double getSettledwithClixCapital() {
        return settledwithClixCapital;
    }

    public void setSettledwithClixCapital(Double settledwithClixCapital) {
        this.settledwithClixCapital = settledwithClixCapital;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getiNSTRUMENTTYPE() {
        return iNSTRUMENTTYPE;
    }

    public void setiNSTRUMENTTYPE(String iNSTRUMENTTYPE) {
        this.iNSTRUMENTTYPE = iNSTRUMENTTYPE;
    }

    public Date getvALUEDATE() {
        return vALUEDATE;
    }

    public void setvALUEDATE(Date vALUEDATE) {
        this.vALUEDATE = vALUEDATE;
    }

    public Double getMdr() {
        return mdr;
    }

    public void setMdr(Double mdr) {
        this.mdr = mdr;
    }

    public Double getGst() {
        return gst;
    }

    public void setGst(Double gst) {
        this.gst = gst;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
