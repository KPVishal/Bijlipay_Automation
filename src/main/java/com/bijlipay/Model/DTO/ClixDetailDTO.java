package com.bijlipay.Model.DTO;

public class ClixDetailDTO {
    private float sNo;
    private String meName;
    private String tid;
    private String mid;
    private Double clixCaptialShare;
    private String loanTenure;
    private String loanAccountNumber;

    public float getsNo() {
		return sNo;
	}

	public void setsNo(float sNo) {
		this.sNo = sNo;
	}

	public String getMeName() {
        return meName;
    }

    public void setMeName(String meName) {
        this.meName = meName;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Double getClixCaptialShare() {
        return clixCaptialShare;
    }

    public void setClixCaptialShare(Double double1) {
        this.clixCaptialShare = double1;
    }

    public String getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(String loanTenure) {
        this.loanTenure = loanTenure;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }
}

