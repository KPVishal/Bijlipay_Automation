package com.bijlipay.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auto_daily_sett")
public class DailySettlement {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "MM/dd/yyyy")
    @Column(name = "Date", nullable = true)
    private Date date;

    @Column(name = "MID", nullable = true)
    private String mid;

    @Column(name = "TID", nullable = true)
    private String tid;

    @Column(name = "MerchantName", nullable = true)
    private String merchantName;

    @Column(name = "City", nullable = true)
    private String city;

    @Column(name = "[Account Number]", nullable = true)
    private String accountNumber;

    @Column(name = "[Branch Code]", nullable = true)
    private String branchCode;

    @Column(name = "Txncount", nullable = true)
    private float txncount;

    @Column(name = "Txnamount", nullable = true)
    private float txnamount;

    @Column(name = "Mrlmdr", nullable = true)
    private float mrlmdr;

    @Column(name = "GST", nullable = true)
    private float gst;

    @Column(name = "Netamount", nullable = true)
    private float netamount;

    @Column(name = "Cardproduct", nullable = true)
    private String cardproduct;

    @Column(name = "CardType", nullable = true)
    private String card_Type;

    @Column(name = "ASSOCIATIONfee", nullable = true)
    private float associationfee;

    @Transient
    private String txn_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public float getTxncount() {
        return txncount;
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

    public String getCardproduct() {
        return cardproduct;
    }

    public String getCard_Type() {
        return card_Type;
    }

    public float getAssociationfee() {
        return associationfee;
    }

    public String getTxn_type() {
        return txn_type;
    }

    public void setTxn_type(String txn_type) {
        this.txn_type = txn_type;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setTxncount(float txncount) {
        this.txncount = txncount;
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

    public void setCardproduct(String cardproduct) {
        this.cardproduct = cardproduct;
    }

    public void setCard_Type(String card_Type) {
        this.card_Type = card_Type;
    }

    public void setAssociationfee(float associationfee) {
        this.associationfee = associationfee;
    }

    public Date getDate() {
        return date;
    }
}
