package com.bijlipay.Model.DTO;

public class Response {
  public LoanReceiptResponse[] LoanReceipt;

    public LoanReceiptResponse[] getLoanReceipt() {
        return LoanReceipt;
    }

    public void setLoanReceipt(LoanReceiptResponse[] loanReceipt) {
        LoanReceipt = loanReceipt;
    }
}
