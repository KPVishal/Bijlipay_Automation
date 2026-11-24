package com.bijlipay.Model.DTO;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "kvb_report")
public class KVBDTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s_no", nullable = false)
    private long id;

    private Date txnDate;

    private String mid;

    private String tid;

    private String merchantName;

    private String tradename;

    private String city;

    private String cardNo;

    private String cardType;

    private String rRN;

    private String associationName;

    private String authCode;

    private String batchCode;

    private float txnamount;

    private float mdramount;

    private float gstamount;

    private float netamount;

    private Date paymentdate;

    private String aggregatorName;

    private String cardType1;

    private String cardProduct;

    private float assocationcost;

    private String transactiontype;

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

    public String getTradename() {
        return tradename;
    }

    public void setTradename(String tradename) {
        this.tradename = tradename;
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

    public Date getDataInserted() {
        return dataInserted;
    }

    public void setDataInserted(Date dataInserted) {
        this.dataInserted = dataInserted;
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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
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

    public float getMdramount() {
        return mdramount;
    }

    public void setMdramount(float mdramount) {
        this.mdramount = mdramount;
    }

    public float getGstamount() {
        return gstamount;
    }

    public void setGstamount(float gstamount) {
        this.gstamount = gstamount;
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

    public String getCardType1() {
        return cardType1;
    }

    public void setCardType1(String cardType1) {
        this.cardType1 = cardType1;
    }

    public String getCardProduct() {
        return cardProduct;
    }

    public void setCardProduct(String cardProduct) {
        this.cardProduct = cardProduct;
    }

    public float getAssocationcost() {
        return assocationcost;
    }

    public void setAssocationcost(float assocationcost) {
        this.assocationcost = assocationcost;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    @Override
    public String toString() {
        return "KVBDTo{" +
                "id=" + id +
                ", txnDate=" + txnDate +
                ", mid='" + mid + '\'' +
                ", tid='" + tid + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", tradename='" + tradename + '\'' +
                ", city='" + city + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardType='" + cardType + '\'' +
                ", rRN='" + rRN + '\'' +
                ", associationName='" + associationName + '\'' +
                ", authCode='" + authCode + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", txnamount=" + txnamount +
                ", mdramount=" + mdramount +
                ", gstamount=" + gstamount +
                ", netamount=" + netamount +
                ", paymentdate=" + paymentdate +
                ", aggregatorName='" + aggregatorName + '\'' +
                ", cardType1='" + cardType1 + '\'' +
                ", cardProduct='" + cardProduct + '\'' +
                ", assocationcost=" + assocationcost +
                ", transactiontype='" + transactiontype + '\'' +
                ", dataInserted='" + dataInserted + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
