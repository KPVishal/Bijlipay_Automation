package com.bijlipay.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment_temp")
public class PaymentTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Column(name = "txnDate", nullable = true)
    private Date txnDate;

    @Column(name = "mid", nullable = true)
    private String mid;

    @Column(name = "tid", nullable = true)
    private String tid;

    @Column(name = "merchantName", nullable = true)
    private String merchantName;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "CardNo", nullable = true)
    private String cardNo;

    @Column(name = "cardType", nullable = true)
    private String cardType;

    @Column(name = "rRN", nullable = true)
    private String rRN;

    @Column(name = "AssociationName", nullable = true)
    private String associationName;

    @Column(name = "Approval_Code", nullable = true)
    private String approvalCode;

    @Column(name = "Batch_Code", nullable = true)
    private String batchCode;

    @Column(name = "txnamount", nullable = true)
    private float txnamount;

    @Column(name = "mdr", nullable = true)
    private float mrlmdr;

    @Column(name = "gst", nullable = true)
    private float gst;

    @Column(name = "Netamount", nullable = true)
    private float netamount;

    @Column(name = "Paymentdate", nullable = true)
    private Date paymentdate;

    @Column(name = "AggregatorName", nullable = true)
    private String aggregatorName;

    @Column(name = "Tradename", nullable = true)
    private String tradename;

    @Column(name = "TerminalSerialNo", nullable = true)
    private String terminalSerialNo;

    @Column(name = "CardType1", nullable = true)
    private String cardType1;

    @Column(name = "ASSOCIATION_fee", nullable = true)
    private float assocationFee;

    @Column(name = "Narration", nullable = true)
    private String narration;

    @Column(name = "CardProduct", nullable = true)
    private String cardProduct;

    @Column(name = "Card_type_A", nullable = true)
    private float cardTypeA;

    @Column(name = "New_IC_Fee", nullable = true)
    private float new_IC_Fee;

    @Column(name = "bank", nullable = true)
    private String bank;

    @Column(name = "location", nullable = true)
    private String location;

    @Column(name = "state", nullable = true)
    private String state;

    @Column(name = "MCC_Desc", nullable = true)
    private String mcc_Desc;

    @Column(name = "mcc", nullable = true)
    private float mcc;

    @Column(name = "ne_MDR", nullable = true)
    private String ne_MDR;

    @Column(name = "mdr_percentage", nullable = true)
    private float mdr_percentage;

    @Column(name = "surcharge", nullable = true)
    private float surcharge;

    @Column(name = "new_non_ic_fee", nullable = true)
    private float new_non_ic_fee;

    @Column(name = "cc_fuel_new_ic_fee", nullable = true)
    private float cc_fuel_new_ic_fee;

    @Column(name = "ifsc", nullable = true)
    private String ifsc;

    @Column(name = "mdr_perc_table", nullable = true)
    private float mdr_perc_table;

    @Column(name = "mdr_bj_calculation", nullable = true)
    private float mdr_bj_calculation;

    @Column(name = "incentive", nullable = true)
    private float incentive;

    @Column(name = "surcharge_new_omc", nullable = true)
    private float surchargeNewOmc;

    @Column(name = "email", nullable = true)
    private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getrRN() {
		return rRN;
	}

	public void setrRN(String rRN) {
		this.rRN = rRN;
	}

	public String getAssociationName() {
		return associationName;
	}

	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public float getTxnamount() {
		return txnamount;
	}

	public void setTxnamount(float txnamount) {
		this.txnamount = txnamount;
	}

	public float getMrlmdr() {
		return mrlmdr;
	}

	public void setMrlmdr(float mrlmdr) {
		this.mrlmdr = mrlmdr;
	}

	public float getGst() {
		return gst;
	}

	public void setGst(float gst) {
		this.gst = gst;
	}

	public float getNetamount() {
		return netamount;
	}

	public void setNetamount(float netamount) {
		this.netamount = netamount;
	}

	public Date getPaymentdate() {
		return paymentdate;
	}

	public void setPaymentdate(Date paymentdate) {
		this.paymentdate = paymentdate;
	}

	public String getAggregatorName() {
		return aggregatorName;
	}

	public void setAggregatorName(String aggregatorName) {
		this.aggregatorName = aggregatorName;
	}

	public String getTradename() {
		return tradename;
	}

	public void setTradename(String tradename) {
		this.tradename = tradename;
	}

	public String getTerminalSerialNo() {
		return terminalSerialNo;
	}

	public void setTerminalSerialNo(String terminalSerialNo) {
		this.terminalSerialNo = terminalSerialNo;
	}

	public String getCardType1() {
		return cardType1;
	}

	public void setCardType1(String cardType1) {
		this.cardType1 = cardType1;
	}

	public float getAssocationFee() {
		return assocationFee;
	}

	public void setAssocationFee(float assocationFee) {
		this.assocationFee = assocationFee;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getCardProduct() {
		return cardProduct;
	}

	public void setCardProduct(String cardProduct) {
		this.cardProduct = cardProduct;
	}

	public float getCardTypeA() {
		return cardTypeA;
	}

	public void setCardTypeA(float cardTypeA) {
		this.cardTypeA = cardTypeA;
	}

	public float getNew_IC_Fee() {
		return new_IC_Fee;
	}

	public void setNew_IC_Fee(float new_IC_Fee) {
		this.new_IC_Fee = new_IC_Fee;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMcc_Desc() {
		return mcc_Desc;
	}

	public void setMcc_Desc(String mcc_Desc) {
		this.mcc_Desc = mcc_Desc;
	}

	public float getMcc() {
		return mcc;
	}

	public void setMcc(float mcc) {
		this.mcc = mcc;
	}

	public String getNe_MDR() {
		return ne_MDR;
	}

	public void setNe_MDR(String ne_MDR) {
		this.ne_MDR = ne_MDR;
	}

	public float getMdr_percentage() {
		return mdr_percentage;
	}

	public void setMdr_percentage(float mdr_percentage) {
		this.mdr_percentage = mdr_percentage;
	}

	public float getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(float surcharge) {
		this.surcharge = surcharge;
	}

	public float getNew_non_ic_fee() {
		return new_non_ic_fee;
	}

	public void setNew_non_ic_fee(float new_non_ic_fee) {
		this.new_non_ic_fee = new_non_ic_fee;
	}

	public float getCc_fuel_new_ic_fee() {
		return cc_fuel_new_ic_fee;
	}

	public void setCc_fuel_new_ic_fee(float cc_fuel_new_ic_fee) {
		this.cc_fuel_new_ic_fee = cc_fuel_new_ic_fee;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public float getMdr_perc_table() {
		return mdr_perc_table;
	}

	public void setMdr_perc_table(float mdr_perc_table) {
		this.mdr_perc_table = mdr_perc_table;
	}

	public float getMdr_bj_calculation() {
		return mdr_bj_calculation;
	}

	public void setMdr_bj_calculation(float mdr_bj_calculation) {
		this.mdr_bj_calculation = mdr_bj_calculation;
	}

	public float getIncentive() {
		return incentive;
	}

	public void setIncentive(float incentive) {
		this.incentive = incentive;
	}

	public float getSurchargeNewOmc() {
		return surchargeNewOmc;
	}

	public void setSurchargeNewOmc(float surchargeNewOmc) {
		this.surchargeNewOmc = surchargeNewOmc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
