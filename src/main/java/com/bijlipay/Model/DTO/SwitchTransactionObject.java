package com.bijlipay.Model.DTO;

import java.util.Date;

public class SwitchTransactionObject {

    private String transactionId;
    private String batchNumber;
    private String invoiceNumber;
    private String merchantId;
    private String mti;
    private String rrnNumber;
    private Date responseReceivedTime;
    private String stan;
    private String terminalId;
    private float txnAmount;
    private String txnDate;
    private String txnResponseCode;
    private String txnTime;
    private String txnType;
    private float txnAdditionalAmount;
    private String urn;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getRrnNumber() {
        return rrnNumber;
    }

    public void setRrnNumber(String rrnNumber) {
        this.rrnNumber = rrnNumber;
    }

    public Date getResponseReceivedTime() {
        return responseReceivedTime;
    }

    public void setResponseReceivedTime(Date responseReceivedTime) {
        this.responseReceivedTime = responseReceivedTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public float getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(float txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnResponseCode() {
        return txnResponseCode;
    }

    public void setTxnResponseCode(String txnResponseCode) {
        this.txnResponseCode = txnResponseCode;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public float getTxnAdditionalAmount() {
        return txnAdditionalAmount;
    }

    public void setTxnAdditionalAmount(float txnAdditionalAmount) {
        this.txnAdditionalAmount = txnAdditionalAmount;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    @Override
    public String toString() {
        return "SwitchTransactionObject{" +
                "transactionId='" + transactionId + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", mti='" + mti + '\'' +
                ", rrnNumber='" + rrnNumber + '\'' +
                ", responseReceivedTime=" + responseReceivedTime +
                ", stan='" + stan + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", txnAmount=" + txnAmount +
                ", txnDate='" + txnDate + '\'' +
                ", txnResponseCode='" + txnResponseCode + '\'' +
                ", txnTime='" + txnTime + '\'' +
                ", txnType='" + txnType + '\'' +
                ", txnAdditionalAmount=" + txnAdditionalAmount +
                ", urn='" + urn + '\'' +
                '}';
    }
}
