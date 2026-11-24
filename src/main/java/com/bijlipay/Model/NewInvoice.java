package com.bijlipay.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "auto_NewInvoice")
public class NewInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Invoice_id")
    private long invoice_Id;

    @Column(name = "Invoice_Number")
    private String invoice_Number;

    @Column(name = "Merchant_name")
    private String name;

    @Column(name = "TID")
    private String tid;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "Merchant_address1 ")
    private String firstAddress;

    @Column(name = "merchant_Contact_No ")
    private String contactNumber;

    @Column(name = "GSTR_no")
    private String merchant_GstrNumber;

    @Column(name = "state_Code")
    private String stateCode;

    @Column(name = "value")
    private String value;

    @Column(name = "IGST")
    private String iGst;

    @Column(name = "SGST")
    private String stateGst;

    @Column(name = "CGST")
    private String centralGst;

    @Column(name = "date")
    private String date;

    @Column(name = "gstTotal")
    private String gstTotal;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getInvoice_Id() {
        return invoice_Id;
    }

    public void setInvoice_Id(long invoice_Id) {
        this.invoice_Id = invoice_Id;
    }

    public String getInvoice_Number() {
        return invoice_Number;
    }

    public void setInvoice_Number(String invoice_Number) {
        this.invoice_Number = invoice_Number;
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

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getiGst() {
        return iGst;
    }

    public void setiGst(String iGst) {
        this.iGst = iGst;
    }

    public String getStateGst() {
        return stateGst;
    }

    public void setStateGst(String stateGst) {
        this.stateGst = stateGst;
    }

    public String getCentralGst() {
        return centralGst;
    }

    public void setCentralGst(String centralGst) {
        this.centralGst = centralGst;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGstTotal() {
        return gstTotal;
    }

    public void setGstTotal(String gstTotal) {
        this.gstTotal = gstTotal;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}