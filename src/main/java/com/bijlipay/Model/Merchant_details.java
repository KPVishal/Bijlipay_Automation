package com.bijlipay.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auto_Merchants")
public class Merchant_details {

    @Id
    @Column(name = "TID")
    private String tid;

    @Column(name = "[Terminals_MID]")
    private String mid;

    @Column(name = "[Create Date]")
    private Date createDate;

    @Column(name = "Merchant_name")
    private String name;

    @Column(name = "[email]")
    private String email;

    @Column(name = "[merchant_Contact_No]")
    private String contactNumber;

    @Column(name = "[Merchant_address1]")
    private String firstAddress;

    @Column(name = "[Merchant_address2]")
    private String secondAddress;

    @Column(name = "[Merchants_City_Town]")
    private String city;

    @Column(name = "[Merchants_Taluk]")
    private String merchantTaluk;

    @Column(name = "[Merchants_Region]")
    private String merchantRegion;

    @Column(name = "[Merchants_District]")
    private String merchantDist;

    @Column(name = "[Merchants_State]")
    private String merchantState;

    @Column(name = "Merchants_Pincode")
    private String pincode;

    @Column(name = "[GSTR_no]")
    private String merchant_GstrNumber;

    @Column(name = "state_Code")
    private int stateCode;

    @Transient
    private String category;

    @Transient
    private Double transaction_value;

    @Transient
    private Double mdr;

    @Transient
    private Double gst;

    @Transient
    private Double mdrTotal;

    @Transient
    private Double gstTotal;

    @Transient
    private Double greatertransaction_value;

    @Transient
    private Double greatermdr;

    @Transient
    private Double greatergst;

    @Transient
    private Double txnamount;

    @Transient
    private Double stateGst;

    @Transient
    private Double centralGst;

    @Transient
    private Double iGst;

    @Transient
    private long date;

    @Transient
    private long invoice_number;

    @Transient
    private String dateForInvoice;

    @Transient
    private String dateForMail;

    @Transient
    private String dateinvoice;

    public String getDateinvoice() {
        return dateinvoice;
    }

    public void setDateinvoice(String dateinvoice) {
        this.dateinvoice = dateinvoice;
    }

    public String getDateForMail() {
        return dateForMail;
    }

    public void setDateForMail(String dateForMail) {
        this.dateForMail = dateForMail;
    }

    public long getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(long invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getDateForInvoice() {
        return dateForInvoice;
    }

    public void setDateForInvoice(String dateForInvoice) {
        this.dateForInvoice = dateForInvoice;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Double getStateGst() {
        return stateGst;
    }

    public void setStateGst(Double stateGst) {
        this.stateGst = stateGst;
    }

    public Double getCentralGst() {
        return centralGst;
    }

    public void setCentralGst(Double centralGst) {
        this.centralGst = centralGst;
    }

    public Double getiGst() {
        return iGst;
    }

    public void setiGst(Double iGst) {
        this.iGst = iGst;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public Double getGreatertransaction_value() {
        return greatertransaction_value;
    }

    public void setGreatertransaction_value(Double greatertransaction_value) {
        this.greatertransaction_value = greatertransaction_value;
    }

    public Double getGreatermdr() {
        return greatermdr;
    }

    public void setGreatermdr(Double greatermdr) {
        this.greatermdr = greatermdr;
    }

    public Double getGreatergst() {
        return greatergst;
    }

    public void setGreatergst(Double greatergst) {
        this.greatergst = greatergst;
    }

    public Double getMdrTotal() {
        return mdrTotal;
    }

    public void setMdrTotal(Double mdrTotal) {
        this.mdrTotal = mdrTotal;
    }

    public Double getGstTotal() {
        return gstTotal;
    }

    public void setGstTotal(Double gstTotal) {
        this.gstTotal = gstTotal;
    }

    public Double getTxnamount() {
        return txnamount;
    }

    public void setTxnamount(Double txnamount) {
        this.txnamount = txnamount;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getTransaction_value() {
        return transaction_value;
    }

    public void setTransaction_value(Double transaction_value) {
        this.transaction_value = transaction_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getFirstAddress() {
        return firstAddress;
    }

    public void setFirstAddress(String firstAddress) {
        this.firstAddress = firstAddress;
    }

    public String getSecondAddress() {
        return secondAddress;
    }

    public void setSecondAddress(String secondAddress) {
        this.secondAddress = secondAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMerchant_GstrNumber() {
        return merchant_GstrNumber;
    }

    public void setMerchant_GstrNumber(String merchant_GstrNumber) {
        this.merchant_GstrNumber = merchant_GstrNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMerchantTaluk() {
        return merchantTaluk;
    }

    public void setMerchantTaluk(String merchantTaluk) {
        this.merchantTaluk = merchantTaluk;
    }

    public String getMerchantRegion() {
        return merchantRegion;
    }

    public void setMerchantRegion(String merchantRegion) {
        this.merchantRegion = merchantRegion;
    }

    public String getMerchantDist() {
        return merchantDist;
    }

    public void setMerchantDist(String merchantDist) {
        this.merchantDist = merchantDist;
    }

    public String getMerchantState() {
        return merchantState;
    }

    public void setMerchantState(String merchantState) {
        this.merchantState = merchantState;
    }
}