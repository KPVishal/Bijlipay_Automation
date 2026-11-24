package com.bijlipay.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auto_Invoice_logs")
public class Invoice_Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	private int logId;

	@Column(name = "TID", nullable = false)
	private String tid;

	@Column(name = "Month")
	private String month;

//	@Column(name = "description", nullable = false)
//	private String description;

	@Column(name = "Invoice_No")
	private long invoiceno;

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}

	public long getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(long invoiceno) {
		this.invoiceno = invoiceno;
	}

	public static Invoice_Log to(String tid, String month, long count) {

		Invoice_Log log = new Invoice_Log();
		log.setTid(tid);
		log.setMonth(month);
		log.setInvoiceno(count);

		return log;

	}

}
