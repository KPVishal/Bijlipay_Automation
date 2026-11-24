package com.bijlipay.Model.DTO;

import java.util.Date;

public class BijlipaySettlementDto {


    private long id;

    private Date txnDate;

    private String mid;

    private String tid;

    private String merchantName;

    private String city;

    private String ifsc;

    private String cardNo;
    private String cardType;

    private String rrn;

    private String associationName;

    private float txnAmount;

    private double mdr;

    private float gst;

    private float netAmount;

    private Date paymentDate;

    private String instance;

    private Date uploadDate;

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    public float getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(float txnAmount) {
        this.txnAmount = txnAmount;
    }

    public double getMdr() {
        return mdr;
    }

    public void setMdr(double mdr) {
        this.mdr = mdr;
    }

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public float getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(float netAmount) {
        this.netAmount = netAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }


    @Override
    public String toString() {
        return "BijlipaySettlementDto{" +
                "id=" + id +
                ", txnDate=" + txnDate +
                ", mid='" + mid + '\'' +
                ", tid='" + tid + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", city='" + city + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardType='" + cardType + '\'' +
                ", rrn='" + rrn + '\'' +
                ", associationName='" + associationName + '\'' +
                ", txnAmount=" + txnAmount +
                ", mdr=" + mdr +
                ", gst=" + gst +
                ", netAmount=" + netAmount +
                ", paymentDate=" + paymentDate +
                ", instance='" + instance + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
