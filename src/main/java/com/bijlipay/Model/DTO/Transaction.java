package com.bijlipay.Model.DTO;

import java.util.List;

public class Transaction {

    public String TransactionID;

    public  String ProspectID;

    public  String MerchantName;

    public  String OrgName;

    public  String LoanAmount;

    public  String CollectionDate;

    public  String CollectionAmount;

    public Transaction(Object[] m) {
        this.MerchantName = m[2].toString();
        this.ProspectID = m[1].toString();
        this.CollectionDate = m[5].toString();
        this.OrgName = "Bijlipay";
        this.TransactionID = String.valueOf(System.currentTimeMillis());
        this.LoanAmount = m[3].toString();
        this.CollectionAmount = m[4].toString();
    }


    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public String getProspectID() {
        return ProspectID;
    }

    public void setProspectID(String prospectID) {
        ProspectID = prospectID;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getLoanAmount() {
        return LoanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        LoanAmount = loanAmount;
    }

    public String getCollectionDate() {
        return CollectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        CollectionDate = collectionDate;
    }

    public String getCollectionAmount() {
        return CollectionAmount;
    }

    public void setCollectionAmount(String collectionAmount) {
        CollectionAmount = collectionAmount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "TransactionID='" + TransactionID + '\'' +
                ", ProspectID='" + ProspectID + '\'' +
                ", MerchantName='" + MerchantName + '\'' +
                ", OrgName='" + OrgName + '\'' +
                ", LoanAmount='" + LoanAmount + '\'' +
                ", CollectionDate='" + CollectionDate + '\'' +
                ", CollectionAmount='" + CollectionAmount + '\'' +
                '}';
    }
}
