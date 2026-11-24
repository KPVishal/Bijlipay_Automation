package com.bijlipay.Model.DTO;

import java.util.List;

public class Request {

    public String TxnCount;
    public String TxnTotalValue;
    public List<Transaction> Transactions;

    public String getTxnCount() {
        return TxnCount;
    }

    public void setTxnCount(String txnCount) {
        TxnCount = txnCount;
    }

    public String getTxnTotalValue() {
        return TxnTotalValue;
    }

    public void setTxnTotalValue(String txnTotalValue) {
        TxnTotalValue = txnTotalValue;
    }

    public List<Transaction> getTransactions() {
        return Transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        Transactions = transactions;
    }

    @Override
    public String toString() {
        return "Test{" +
                "TxnCount='" + TxnCount + '\'' +
                ", TxnTotalValue='" + TxnTotalValue + '\'' +
                ", Transactions=" + Transactions +
                '}';
    }
}
