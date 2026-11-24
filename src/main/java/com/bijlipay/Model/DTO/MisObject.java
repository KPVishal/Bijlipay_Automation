package com.bijlipay.Model.DTO;

import java.util.Date;

public class MisObject {

    private Date txnDate;

    private String mid;

    private String tid;

    private String merchantName;

    private String city;

    private String cardNo;

    private String cardType;

    private String rRN;

    private String associationName;

    private float txnamount;

    private float mrlmdr;

    private float gst;

    private float netamount;

    private Date paymentdate;

    private String narration;

    private String aggregatorName;

    private String tradename;

    private String cardType1;

    private String cardProduct;

    private String location;

    private String state;

    private float mdr_percentage;

    private Date txnTime;

    private float incentive;

    // One Four Twevel Nine Fourteen
    public MisObject(Date txnDate, String mid, String tid, String merchantName, String city, String cardNo,
                     String cardType, String rRN, String associationName,
                     float txnamount, float mrlmdr, float gst, float netamount, String cardType1,
                     String tradename, Date paymentdate,
                     String narration
    ) {
        this.txnDate = txnDate;
        this.mid = mid;
        this.tid = tid;
        this.merchantName = merchantName;
        this.city = city;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.rRN = rRN;
        this.associationName = associationName;
        this.txnamount = txnamount;
        this.mrlmdr = mrlmdr;
        this.gst = gst;
        this.netamount = netamount;
        this.paymentdate = paymentdate;
        this.narration = narration;
        this.tradename = tradename;
        this.cardType1 = cardType1;
    }

    // Ten Eleven
    public MisObject(Date txnDate, Date txnTime, String mid, String tid, String merchantName, String city, String cardNo,
                     String cardType, String rRN, String associationName,
                     float txnamount, float mrlmdr, float gst, float netamount, String cardType1,
                     String tradename, Date paymentdate,
                     String narration
    ) {
        this.txnDate = txnDate;
        this.txnTime = txnTime;
        this.mid = mid;
        this.tid = tid;
        this.merchantName = merchantName;
        this.city = city;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.rRN = rRN;
        this.associationName = associationName;
        this.txnamount = txnamount;
        this.mrlmdr = mrlmdr;
        this.gst = gst;
        this.netamount = netamount;
        this.paymentdate = paymentdate;
        this.narration = narration;
        this.tradename = tradename;
        this.cardType1 = cardType1;
    }

    // Two
    public MisObject(Date txnDate, String mid, String tid, String merchantName, String city, String cardNo,
                     String cardType, String rRN, String associationName,
                     float txnamount, float mrlmdr, float gst, float netamount, String cardType1,
                     String tradename, String location, Date paymentdate,
                     String narration
    ) {
        this.txnDate = txnDate;
        this.mid = mid;
        this.tid = tid;
        this.merchantName = merchantName;
        this.city = city;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.rRN = rRN;
        this.associationName = associationName;
        this.txnamount = txnamount;
        this.mrlmdr = mrlmdr;
        this.gst = gst;
        this.netamount = netamount;
        this.paymentdate = paymentdate;
        this.narration = narration;
        this.tradename = tradename;
        this.location = location;
        this.cardType1 = cardType1;
    }

    // Three
    public MisObject(Date txnDate, String mid, String tid, String merchantName, String city, String cardNo,
                     String cardType, String rRN, String associationName,
                     float txnamount, float mrlmdr, float gst, float netamount, String cardType1,
                     String tradename, float mdr_percentage, Date paymentdate,
                     String narration
    ) {
        this.txnDate = txnDate;
        this.mid = mid;
        this.tid = tid;
        this.merchantName = merchantName;
        this.city = city;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.rRN = rRN;
        this.associationName = associationName;
        this.txnamount = txnamount;
        this.mrlmdr = mrlmdr;
        this.gst = gst;
        this.netamount = netamount;
        this.paymentdate = paymentdate;
        this.narration = narration;
        this.tradename = tradename;
        this.mdr_percentage = mdr_percentage;
        this.cardType1 = cardType1;
    }

    // Five Six
    public MisObject(Date txnDate, String mid, String tid, String merchantName, String city, String cardNo,
                     String cardType, String rRN, String associationName,
                     float txnamount, float mrlmdr, float gst, float netamount, String cardType1,
                     String tradename, Date paymentdate, String aggregatorName,
                     String narration
    ) {
        this.txnDate = txnDate;
        this.mid = mid;
        this.tid = tid;
        this.merchantName = merchantName;
        this.city = city;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.rRN = rRN;
        this.associationName = associationName;
        this.txnamount = txnamount;
        this.mrlmdr = mrlmdr;
        this.gst = gst;
        this.netamount = netamount;
        this.paymentdate = paymentdate;
        this.narration = narration;
        this.tradename = tradename;
        this.aggregatorName = aggregatorName;
        this.cardType1 = cardType1;
    }

    // Eight
    public MisObject(Date txnDate, String mid, String tid, String merchantName, String city, String state, String cardNo,
                     String cardType, String rRN, String associationName,
                     float txnamount, float mrlmdr, float gst, float netamount, String cardType1,
                     String tradename, Date paymentdate, String narration
    ) {
        this.txnDate = txnDate;
        this.mid = mid;
        this.tid = tid;
        this.merchantName = merchantName;
        this.city = city;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.rRN = rRN;
        this.associationName = associationName;
        this.txnamount = txnamount;
        this.mrlmdr = mrlmdr;
        this.gst = gst;
        this.netamount = netamount;
        this.paymentdate = paymentdate;
        this.narration = narration;
        this.tradename = tradename;
        this.state = state;
        this.cardType1 = cardType1;
    }

    // Thirteen
    public MisObject(Date txnDate, String mid, String tid, String merchantName, String city, String cardNo,
                     String cardType, String rRN, String associationName,
                     float txnamount, float mrlmdr, String cardProduct, float gst, float netamount, String cardType1,
                     String tradename, Date paymentdate, String narration
    ) {
        this.txnDate = txnDate;
        this.mid = mid;
        this.tid = tid;
        this.merchantName = merchantName;
        this.city = city;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.rRN = rRN;
        this.associationName = associationName;
        this.txnamount = txnamount;
        this.mrlmdr = mrlmdr;
        this.gst = gst;
        this.netamount = netamount;
        this.paymentdate = paymentdate;
        this.narration = narration;
        this.tradename = tradename;
        this.cardProduct = cardProduct;
        this.cardType1 = cardType1;
    }

    // Fifteen
    public MisObject(Date txnDate, String mid, String tid, String merchantName, String city, String cardNo,
                     String cardType, String rRN, String associationName,
                     float txnamount, float mrlmdr, float gst, float netamount, String cardType1,
                     String tradename, Date paymentdate, String narration, float incentive
    ) {
        this.txnDate = txnDate;
        this.mid = mid;
        this.tid = tid;
        this.merchantName = merchantName;
        this.city = city;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.rRN = rRN;
        this.associationName = associationName;
        this.txnamount = txnamount;
        this.mrlmdr = mrlmdr;
        this.gst = gst;
        this.netamount = netamount;
        this.paymentdate = paymentdate;
        this.narration = narration;
        this.tradename = tradename;
        this.incentive = incentive;
        this.cardType1 = cardType1;
    }

    public MisObject() {

    }

    public Date getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(Date txnTime) {
        this.txnTime = txnTime;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setrRN(String rRN) {
        this.rRN = rRN;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    public void setTxnamount(float txnamount) {
        this.txnamount = txnamount;
    }

    public void setMrlmdr(float mrlmdr) {
        this.mrlmdr = mrlmdr;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public void setNetamount(float netamount) {
        this.netamount = netamount;
    }

    public void setPaymentdate(Date paymentdate) {
        this.paymentdate = paymentdate;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public void setAggregatorName(String aggregatorName) {
        this.aggregatorName = aggregatorName;
    }

    public void setTradename(String tradename) {
        this.tradename = tradename;
    }

    public void setCardType1(String cardType1) {
        this.cardType1 = cardType1;
    }

    public void setCardProduct(String cardProduct) {
        this.cardProduct = cardProduct;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMdr_percentage(float mdr_percentage) {
        this.mdr_percentage = mdr_percentage;
    }

    public void setIncentive(float incentive) {
        this.incentive = incentive;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public String getMid() {
        return mid;
    }

    public String getTid() {
        return tid;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getCity() {
        return city;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public String getrRN() {
        return rRN;
    }

    public String getAssociationName() {
        return associationName;
    }

    public float getTxnamount() {
        return txnamount;
    }

    public float getMrlmdr() {
        return mrlmdr;
    }

    public float getGst() {
        return gst;
    }

    public float getNetamount() {
        return netamount;
    }

    public Date getPaymentdate() {
        return paymentdate;
    }

    public String getNarration() {
        return narration;
    }

    public String getAggregatorName() {
        return aggregatorName;
    }

    public String getTradename() {
        return tradename;
    }

    public String getCardType1() {
        return cardType1;
    }

    public String getCardProduct() {
        return cardProduct;
    }

    public String getLocation() {
        return location;
    }

    public String getState() {
        return state;
    }

    public float getIncentive() {
        return incentive;
    }

    public float getMdr_percentage() {
        return mdr_percentage;
    }

    @Override
    public String toString() {
        return "MisObject{" +
                "txnDate=" + txnDate +
                ", mid='" + mid + '\'' +
                ", tid='" + tid + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", city='" + city + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardType='" + cardType + '\'' +
                ", rRN='" + rRN + '\'' +
                ", associationName='" + associationName + '\'' +
                ", txnamount=" + txnamount +
                ", mrlmdr=" + mrlmdr +
                ", gst=" + gst +
                ", netamount=" + netamount +
                ", paymentdate=" + paymentdate +
                ", narration='" + narration + '\'' +
                ", aggregatorName='" + aggregatorName + '\'' +
                ", tradename='" + tradename + '\'' +
                ", cardType1='" + cardType1 + '\'' +
                ", cardProduct='" + cardProduct + '\'' +
                ", location='" + location + '\'' +
                ", state='" + state + '\'' +
                ", mdr_percentage=" + mdr_percentage +
                ", txnTime=" + txnTime +
                ", incentive=" + incentive +
                '}';
    }
}