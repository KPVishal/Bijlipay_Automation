package com.bijlipay.Model.DTO;

import java.util.Date;

public class MasterClixDTO {

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

    private String caption;

    private String iNSTRUMENTTYPE;

    private Date vALUEDATE;

    public MasterClixDTO(String meName, String loanAccountNumber, Double totalTransactionValue,
                         Double netTransactionValue, Double merchantShare, float clixCaptialShare,
                         String merchantShare1, float clixCaptialShare1, String heldAmt, String loanTenure,
                         String acNo, String ifscCode, Double totalDisbValue, Double settledwithClixCapital,
                         Double balance, String iNSTRUMENTTYPE, Date vALUEDATE) {
        this.meName = meName;
        this.loanAccountNumber = loanAccountNumber;
        this.totalTransactionValue = totalTransactionValue;
        this.netTransactionValue = netTransactionValue;
        this.merchantShare = merchantShare;
        this.clixCaptialShare = clixCaptialShare;
        this.merchantShare1 = merchantShare1;
        this.clixCaptialShare1 = clixCaptialShare1;
        this.heldAmt = heldAmt;
        this.loanTenure = loanTenure;
        this.acNo = acNo;
        this.ifscCode = ifscCode;
        this.totalDisbValue = totalDisbValue;
        this.settledwithClixCapital = settledwithClixCapital;
        this.balance = balance;
        this.iNSTRUMENTTYPE = iNSTRUMENTTYPE;
        this.vALUEDATE = vALUEDATE;

    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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
}
