package com.bijlipay.Model.DTO;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class ClixPendingDTO {

	    private String merhcantName;
	    private String tid;
	    private Double pendingBalance;
	    private Date date;

	    public String getMerhcantName() {
	        return merhcantName;
	    }

	    public void setMerhcantName(String merhcantName) {
	        this.merhcantName = merhcantName;
	    }

	    public String getTid() {
	        return tid;
	    }

	    public void setTid(String tid) {
	        this.tid = tid;
	    }

	    public Double getPendingBalance() {
	        return pendingBalance;
	    }

	    public void setPendingBalance(Double pendingBalance) {
	        this.pendingBalance = pendingBalance;
	    }

	    public Date getDate() {
	        return date;
	    }

	    public void setDate(Date date) {
	        this.date = date;
	    }
	
}
