package com.bijlipay.Model.DTO;

public class LoanReceiptResponse {
    public String RESULT;
    public String CODE;
    public String MSG;
    public String LAN;
    public String LoanAmount;
    public String BalanceAmount;

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public String getLAN() {
        return LAN;
    }

    public void setLAN(String LAN) {
        this.LAN = LAN;
    }

    public String getLoanAmount() {
        return LoanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        LoanAmount = loanAmount;
    }

    public String getBalanceAmount() {
        return BalanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        BalanceAmount = balanceAmount;
    }

    @Override
    public String toString() {
        return "LoanReceipt{" +
                "RESULT='" + RESULT + '\'' +
                ", CODE='" + CODE + '\'' +
                ", MSG='" + MSG + '\'' +
                ", LAN='" + LAN + '\'' +
                ", LoanAmount='" + LoanAmount + '\'' +
                ", BalanceAmount='" + BalanceAmount + '\'' +
                '}';
    }
}
