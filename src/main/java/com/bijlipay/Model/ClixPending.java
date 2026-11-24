package com.bijlipay.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "loan_pending$")
public class ClixPending {


    @Column(name = "[Merchant_Name]")
    private String merhcantName;

    @Id
    @Column(name = "[MID]")
    private String mid;

    @Column(name = "[Pending_Balance]")
    private Double pendingBalance;

    @Column(name = "[Date]")
    private Date date;
    
    @Column(name= "[Excess_Amt]")
    private Double excessamt;
    
    @Column(name = "caption")
    private String caption;
    
    
    public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

    public Double getExcessamt() {
		return excessamt;
	}

	public void setExcessamt(Double excessamt) {
		this.excessamt = excessamt;
	}

	public String getMerhcantName() {
        return merhcantName;
    }

    public void setMerhcantName(String merhcantName) {
        this.merhcantName = merhcantName;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String tid) {
        this.mid = tid;
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
