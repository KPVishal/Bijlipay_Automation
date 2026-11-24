package com.bijlipay.Model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Loan_Detail$")
public class ClixDetail {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "[S_No]",unique = true,nullable = false)
    private int sNo;

    @Column(name = "[ME_Name]")
    private String meName;

    @Column(name = "[TID]")
    private String tid;

    @Column(name = "[MID]")
    private String mid;

    @Column(name = "[Lender_captial_Share]")
    private Double clixCaptialShare;

    @Column(name = "[Loan_Tenure]")
    private String loanTenure;

    @Column(name = "[Loan_Account_Number]")
    private String loanAccountNumber;
    
    @Column(name = "caption")
    private String caption;
    
    
    public String getCaption() {
		return caption;
	}

    public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getsNo() {
		return sNo;
	}

	public void setsNo(int sNo) {
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

    public void setClixCaptialShare(Double clixCaptialShare) {
        this.clixCaptialShare = clixCaptialShare;
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
