package com.bijlipay.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Current_Report")
public class CurrentReport {

    @Column(name = "txn_date")
    private String txn_date;

    @Column(name = "mid")
    private String mid;

    @Column(name = "tid")
    private String tid;

    @Column(name = "merchant_name")
    private String merchant_name;

    @Column(name = "city")
    private String city;

    @Column(name = "card_no")
    private String card_no;

    @Column(name = "card_type")
    private String card_type;

    @Id
    @Column(name = "rrn")
    private String rrn;

    @Column(name = "association_name")
    private String association_name;

    @Column(name = "approval_code")
    private float approval_code;

    @Column(name = "batch_code")
    private String batch_code;

    @Column(name = "txnamount")
    private float txnamount;


    @Column(name = "mdr")
    private float mdr;

    @Column(name = "gst")
    private float gst;

    @Column(name = "netamount")
    private float netamount;

    @Column(name = "paymentdate")
    private String paymentdate;


    @Column(name = "card_type1")
    private String card_type1;


    @Column(name = "narration")
    private String narration;

    @Column(name = "card_product")
    private String card_product;

    public String getTxn_date() {
        return txn_date;
    }

    public void setTxn_date(String txn_date) {
        this.txn_date = txn_date;
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

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getAssociation_name() {
        return association_name;
    }

    public void setAssociation_name(String association_name) {
        this.association_name = association_name;
    }

    public float getApproval_code() {
        return approval_code;
    }

    public void setApproval_code(float approval_code) {
        this.approval_code = approval_code;
    }

    public String getBatch_code() {
        return batch_code;
    }

    public void setBatch_code(String batch_code) {
        this.batch_code = batch_code;
    }

    public float getTxnamount() {
        return txnamount;
    }

    public void setTxnamount(float txnamount) {
        this.txnamount = txnamount;
    }

    public float getMdr() {
        return mdr;
    }

    public void setMdr(float mdr) {
        this.mdr = mdr;
    }

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public float getNetamount() {
        return netamount;
    }

    public void setNetamount(float netamount) {
        this.netamount = netamount;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getCard_type1() {
        return card_type1;
    }

    public void setCard_type1(String card_type1) {
        this.card_type1 = card_type1;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getCard_product() {
        return card_product;
    }

    public void setCard_product(String card_product) {
        this.card_product = card_product;
    }
}
