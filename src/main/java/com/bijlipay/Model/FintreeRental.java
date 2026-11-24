package com.bijlipay.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Fintree_rental$")
public class FintreeRental {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "[S_No]")
		private int sNo;

		@Column(name = "[MID]")
		private String mid;

		@Column(name = "[Renttobecollect]")
		private Double rent;

		@Column(name = "[Merchant_Type]")
		private String Metype;
		
		
		@CreationTimestamp
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "[File_Created]")
		private Date createdat;

		public int getsNo() {
			return sNo;
		}

		public void setsNo(int sNo) {
			this.sNo = sNo;
		}

		public String getMid() {
			return mid;
		}

		public void setMid(String mid) {
			this.mid = mid;
		}

		public Double getRent() {
			return rent;
		}

		public void setRent(Double rent) {
			this.rent = rent;
		}

		public String getMetype() {
			return Metype;
		}

		public void setMetype(String metype) {
			Metype = metype;
		}

		public Date getCreatedat() {
			return createdat;
		}

		public void setCreatedat(Date createdat) {
			this.createdat = createdat;
		}

}
