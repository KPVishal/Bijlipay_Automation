package com.bijlipay.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "federalReport")
public class FederalDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s_no", nullable = false)
    private long id;

    private Date txnDate;

    private String mid;

    private String tid;

    private String merchantName;

    private String city;

    private String cardNo;

    private String cardType;

    private String rRN;

    private String associationName;

    private String approvalCode;

    private String batchCode;

    private float txnamount;

    private float mrlmdr;

    private float gst;

    private float netamount;

    private Date paymentdate;

    private String aggregatorName;

    private String tradename;

    private String terminalSerialNo;

    private String cardType1;

    private float assocationFee;

    private String cardProduct;

    private String narration;

    private String month;

    @Column(name = "created_at")
    private String date;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inserted_at")
    @JsonIgnore
    private Date dataInserted;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
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

    public String getrRN() {
        return rRN;
    }

    public void setrRN(String rRN) {
        this.rRN = rRN;
    }

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public float getTxnamount() {
        return txnamount;
    }

    public void setTxnamount(float txnamount) {
        this.txnamount = txnamount;
    }

    public float getMrlmdr() {
        return mrlmdr;
    }

    public void setMrlmdr(float mrlmdr) {
        this.mrlmdr = mrlmdr;
    }

    public float getNetamount() {
        return netamount;
    }

    public void setNetamount(float netamount) {
        this.netamount = netamount;
    }

    public Date getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(Date paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getAggregatorName() {
        return aggregatorName;
    }

    public void setAggregatorName(String aggregatorName) {
        this.aggregatorName = aggregatorName;
    }

    public String getTradename() {
        return tradename;
    }

    public void setTradename(String tradename) {
        this.tradename = tradename;
    }

    public String getTerminalSerialNo() {
        return terminalSerialNo;
    }

    public void setTerminalSerialNo(String terminalSerialNo) {
        this.terminalSerialNo = terminalSerialNo;
    }

    public String getCardType1() {
        return cardType1;
    }

    public void setCardType1(String cardType1) {
        this.cardType1 = cardType1;
    }

    public float getAssocationFee() {
        return assocationFee;
    }

    public void setAssocationFee(float assocationFee) {
        this.assocationFee = assocationFee;
    }

    public String getCardProduct() {
        return cardProduct;
    }

    public void setCardProduct(String cardProduct) {
        this.cardProduct = cardProduct;
    }

    @Override
    public String toString() {
        return "FederalDTO{" +
                "id=" + id +
                ", txnDate=" + txnDate +
                ", mid='" + mid + '\'' +
                ", tid='" + tid + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", city='" + city + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardType='" + cardType + '\'' +
                ", rRN='" + rRN + '\'' +
                ", associationName='" + associationName + '\'' +
                ", approvalCode='" + approvalCode + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", txnamount=" + txnamount +
                ", mrlmdr=" + mrlmdr +
                ", gst=" + gst +
                ", netamount=" + netamount +
                ", paymentdate=" + paymentdate +
                ", aggregatorName='" + aggregatorName + '\'' +
                ", tradename='" + tradename + '\'' +
                ", terminalSerialNo='" + terminalSerialNo + '\'' +
                ", cardType1='" + cardType1 + '\'' +
                ", assocationFee=" + assocationFee +
                ", cardProduct='" + cardProduct + '\'' +
                ", narration='" + narration + '\'' +
                ", month='" + month + '\'' +
                ", date='" + date + '\'' +
                ", dataInserted=" + dataInserted +
                '}';
    }
}
