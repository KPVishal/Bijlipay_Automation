package com.bijlipay.Model;

import javax.persistence.*;

@Entity
@Table(name = "auto_Invoice_Number")
public class Invoice_Number {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Invoice_id")
	private long invoice_Id;

	@Column(name = "Invoice_Number")
	private long invoice_Number;

	public long getInvoice_Id() {
		return invoice_Id;
	}

	public void setInvoice_Id(long invoice_Id) {
		this.invoice_Id = invoice_Id;
	}

	public long getInvoice_Number() {
		return invoice_Number;
	}

	public void setInvoice_Number(long invoice_Number) {
		this.invoice_Number = invoice_Number;
	}

}
