package com.bijlipay.Model.DTO;

import java.util.Date;

public class ClixMasterDTO {

	private Double pendingBalance;
	
	private Double excess_amt;
	
	private String meName;
	
    private String loanAccountNumber;

    private Double totalTransactionValue;

    private Double netTransactionValue;

    private Double merchantShare;

    private float clixCaptialShare;

    private String merchantShare1;

    private float clixCaptialShare1;

    private String heldAmt;

    private String loanTenure;

    private String acNo;

    private String ifscCode;

    private Double totalDisbValue;

    private Double settledwithClixCapital;

    private Double balance;

    private String iNSTRUMENTTYPE;

    private Date vALUEDATE;

    private Double mdr;

    private Double gst;

    private String tid;

    private String mid;

    private int count;

    private String caption;

    private int format;

    private String mail_id;
    
    private Double txnamount;
    
    private Double netamount;
    
    public long rrn;
    
    private Double rental;
    
    private Double rentcollect;

    private String bank;
    private String merchantIFSC;
    private String merchantAccountnumber;


    public String getMeName() {
        return meName;
    }

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

	public ClixMasterDTO() {
	}

	public ClixMasterDTO(String mmid,float Lender_Capital_Share, float Lender_Capital_Share1,String heldAmt, Double net_transaction_Value,
			Double merchant_Share,int count, Double pending_balance, Double excess_amt, Double rental, Double rentcollect) {
		
    	this.mid=mmid;
		this.clixCaptialShare = Lender_Capital_Share;
		this.clixCaptialShare1 = Lender_Capital_Share1;
		this.netTransactionValue = net_transaction_Value;
		this.merchantShare = merchant_Share;
		this.pendingBalance = pending_balance;
		this.excess_amt = excess_amt;
		this.heldAmt=heldAmt;
		this.count = count;
		this.rental=rental;
		this.rentcollect=rentcollect;
		
	}
	    
	public ClixMasterDTO(String mmid,float Lender_Capital_Share, float Lender_Capital_Share1, Double net_transaction_Value,
			Double merchant_Share,int count, Double pending_balance, Double excess_amt) {
		
    	this.mid=mmid;
		this.clixCaptialShare = Lender_Capital_Share;
		this.clixCaptialShare1 = Lender_Capital_Share1;
		this.netTransactionValue = net_transaction_Value;
		this.merchantShare = merchant_Share;
		this.pendingBalance = pending_balance;
		this.excess_amt = excess_amt;
		this.count = count;
		
	}

	public ClixMasterDTO(String mmid,float Lender_Capital_Share, float Lender_Capital_Share1, Double rental, Double rentcollect, Double net_transaction_Value,
			Double merchant_Share,int count, Double pending_balance, Double excess_amt) {
		
    	this.mid=mmid;
		this.clixCaptialShare = Lender_Capital_Share;
		this.clixCaptialShare1 = Lender_Capital_Share1;
		this.rental=rental;
		this.rentcollect=rentcollect;
		this.netTransactionValue = net_transaction_Value;
		this.merchantShare = merchant_Share;
		this.pendingBalance = pending_balance;
		this.excess_amt = excess_amt;
		this.count = count;
		
	}
	
	public ClixMasterDTO( String meName,String mid,Double totalTransactionValue,Double netTransactionValue, Double merchantShare, float LenderCaptialShare,String mail_id,String loanAccountNumber) {
		
    	this.mid=mid;
		this.meName=meName;
		this.totalTransactionValue=totalTransactionValue;
		this.netTransactionValue=netTransactionValue;
		this.merchantShare=merchantShare;
		this.clixCaptialShare=LenderCaptialShare;
		this.mail_id=mail_id;
		this.loanAccountNumber=loanAccountNumber;
		
	}

	public long getRrn() {
		return rrn;
	}

	public void setRrn(long rrn) {
		this.rrn = rrn;
	}

	public Double getTxnamount() {
		return txnamount;
	}

	public void setTxnamount(Double txnamount) {
		this.txnamount = txnamount;
	}

	public Double getNetamount() {
		return netamount;
	}

	public void setNetamount(Double netamount) {
		this.netamount = netamount;
	}

	public Double getPendingBalance() {
		return pendingBalance;
	}

	public void setPendingBalance(Double pendingBalance) {
		this.pendingBalance = pendingBalance;
	}

	public Double getExcess_amt() {
		return excess_amt;
	}

	public void setExcess_amt(Double excess_amt) {
		excess_amt = excess_amt;
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

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
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

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getMerchantIFSC() {
		return merchantIFSC;
	}

	public void setMerchantIFSC(String merchantIFSC) {
		this.merchantIFSC = merchantIFSC;
	}

	public String getMerchantAccountnumber() {
		return merchantAccountnumber;
	}

	public void setMerchantAccountnumber(String merchantAccountnumber) {
		this.merchantAccountnumber = merchantAccountnumber;
	}

	public void setMeName(String meName) {
		this.meName = meName;
	}

}
