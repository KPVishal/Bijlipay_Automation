package com.bijlipay.Model.DTO;

import java.util.Date;


public class SwitchTransactions {
    private long id;
    private Date txnDate;
    private String mid;
    private String tid;
    private String merchantName;
    private String city;
    private String cardNo;
    private String cardType;
    private String rRN;
    private String associationName;
    private String approvalCode;
    private String batchCode;
    private float txnamount;
    private float mrlmdr;
    private float gst;
    private float netamount;
    private Date paymentdate;
    private String aggregatorName;
    private String tradename;
    private String terminalSerialNo;
    private String cardType1;
    private float assocationFee;
    private String narration;
    private String cardProduct;
    private float cardTypeA;
    private float new_IC_Fee;
    private String bank;
    private String location;
    private String state;
    private String mcc_Desc;
    private float mcc;
    private String ne_MDR;
    private float mdr_percentage;
    private float surcharge;
    private float new_non_ic_fee;
    private float cc_fuel_new_ic_fee;
    private String ifsc;
    private float mdr_perc_table;
    private float mdr_bj_calculation;
    private float incentive;
    private float surchargeNewOmc;
    private String email;
    private String status;
    private String shell;
    private String sorce;
    private String mti = "NA";
    private String cardTypeDesc;
    private String creditDebitFlag;
    private String createdAt;
    private String interchangeType;
    private String posEntryMode;
    private String stan;
    private float transactionCurrencyCode;
    private String transactionType;
    private String txnTime;
    private String month;
    private String authCode;
    private float mdrAmount;
    private float sgst;
    private float igst;
    private long tds;
    private long minUtilFees;
    private float merPayment;
    private float netPayment;
    private float txnSharingAmount;
    private String traceNo;
    private String msgType;
    private String respCode;
    private String authIdRsp;
    private Date setlDate;
    private Date uploadDate;
    private String onnusOffus;
    private String txnMode;
    private String network;
    private String remarks;
    private long insCode;
    private long prjSerialNumber;
    private String paymentTo;
    private String cardSubCategory;
    private String cardCategory;
    private float agrNetPayment;
    private String paymentMode;
    private String paymentId;
    private String switchType;
    private int txnCount;
    private float agrRslrMinFees;
    private float agrGrossAmount;
    private float merchantMdr;
    private float cgst;
    private float mdrDiscFee;
    private float agrRslrMdr;
    private float agrRslrDiscFees;
    private float agrRslrCgst;
    private float agrRslrSgst;
    private float agrRslrIgst;
    private float agrRslrInstv;
    private float agrRslrTds;
    private String account;
    private String instance;
    private String txnType;
    private String txnResponseCode;
    private Date responseReceivedTime;
    private String rRNumber;
    private String invoiceNumber;
    private String batchNumber;
    private Double TxnAdditionalAmount;
    private String urn;
    private String mti1;
    private String switchTxnDate;
    private float mdrSharingAmount;
    private String txn_Type;
    private Date insertedAt;

    public String getTxn_Type() {
        return txn_Type;
    }

    public void setTxn_Type(String txn_Type) {
        this.txn_Type = txn_Type;
    }

    public float getMdrSharingAmount() {
        return mdrSharingAmount;
    }

    public void setMdrSharingAmount(float mdrSharingAmount) {
        this.mdrSharingAmount = mdrSharingAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String getSorce() {
        return sorce;
    }

    public void setSorce(String sorce) {
        this.sorce = sorce;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getCardTypeDesc() {
        return cardTypeDesc;
    }

    public void setCardTypeDesc(String cardTypeDesc) {
        this.cardTypeDesc = cardTypeDesc;
    }

    public String getCreditDebitFlag() {
        return creditDebitFlag;
    }

    public void setCreditDebitFlag(String creditDebitFlag) {
        this.creditDebitFlag = creditDebitFlag;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getInterchangeType() {
        return interchangeType;
    }

    public void setInterchangeType(String interchangeType) {
        this.interchangeType = interchangeType;
    }

    public String getPosEntryMode() {
        return posEntryMode;
    }

    public void setPosEntryMode(String posEntryMode) {
        this.posEntryMode = posEntryMode;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public float getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(float transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public float getMdrAmount() {
        return mdrAmount;
    }

    public void setMdrAmount(float mdrAmount) {
        this.mdrAmount = mdrAmount;
    }

    public float getSgst() {
        return sgst;
    }

    public void setSgst(float sgst) {
        this.sgst = sgst;
    }

    public float getIgst() {
        return igst;
    }

    public void setIgst(float igst) {
        this.igst = igst;
    }

    public long getTds() {
        return tds;
    }

    public void setTds(long tds) {
        this.tds = tds;
    }

    public long getMinUtilFees() {
        return minUtilFees;
    }

    public void setMinUtilFees(long minUtilFees) {
        this.minUtilFees = minUtilFees;
    }

    public float getMerPayment() {
        return merPayment;
    }

    public void setMerPayment(float merPayment) {
        this.merPayment = merPayment;
    }

    public float getNetPayment() {
        return netPayment;
    }

    public void setNetPayment(float netPayment) {
        this.netPayment = netPayment;
    }

    public float getTxnSharingAmount() {
        return txnSharingAmount;
    }

    public void setTxnSharingAmount(float txnSharingAmount) {
        this.txnSharingAmount = txnSharingAmount;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getAuthIdRsp() {
        return authIdRsp;
    }

    public void setAuthIdRsp(String authIdRsp) {
        this.authIdRsp = authIdRsp;
    }

    public Date getSetlDate() {
        return setlDate;
    }

    public void setSetlDate(Date setlDate) {
        this.setlDate = setlDate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getOnnusOffus() {
        return onnusOffus;
    }

    public void setOnnusOffus(String onnusOffus) {
        this.onnusOffus = onnusOffus;
    }

    public String getTxnMode() {
        return txnMode;
    }

    public void setTxnMode(String txnMode) {
        this.txnMode = txnMode;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getInsCode() {
        return insCode;
    }

    public void setInsCode(long insCode) {
        this.insCode = insCode;
    }

    public long getPrjSerialNumber() {
        return prjSerialNumber;
    }

    public void setPrjSerialNumber(long prjSerialNumber) {
        this.prjSerialNumber = prjSerialNumber;
    }

    public String getPaymentTo() {
        return paymentTo;
    }

    public void setPaymentTo(String paymentTo) {
        this.paymentTo = paymentTo;
    }

    public String getCardSubCategory() {
        return cardSubCategory;
    }

    public void setCardSubCategory(String cardSubCategory) {
        this.cardSubCategory = cardSubCategory;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public float getAgrNetPayment() {
        return agrNetPayment;
    }

    public void setAgrNetPayment(float agrNetPayment) {
        this.agrNetPayment = agrNetPayment;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getSwitchType() {
        return switchType;
    }

    public void setSwitchType(String switchType) {
        this.switchType = switchType;
    }

    public int getTxnCount() {
        return txnCount;
    }

    public void setTxnCount(int txnCount) {
        this.txnCount = txnCount;
    }

    public float getAgrRslrMinFees() {
        return agrRslrMinFees;
    }

    public void setAgrRslrMinFees(float agrRslrMinFees) {
        this.agrRslrMinFees = agrRslrMinFees;
    }

    public float getAgrGrossAmount() {
        return agrGrossAmount;
    }

    public void setAgrGrossAmount(float agrGrossAmount) {
        this.agrGrossAmount = agrGrossAmount;
    }

    public float getMerchantMdr() {
        return merchantMdr;
    }

    public void setMerchantMdr(float merchantMdr) {
        this.merchantMdr = merchantMdr;
    }

    public float getCgst() {
        return cgst;
    }

    public void setCgst(float cgst) {
        this.cgst = cgst;
    }

    public float getMdrDiscFee() {
        return mdrDiscFee;
    }

    public void setMdrDiscFee(float mdrDiscFee) {
        this.mdrDiscFee = mdrDiscFee;
    }

    public float getAgrRslrMdr() {
        return agrRslrMdr;
    }

    public void setAgrRslrMdr(float agrRslrMdr) {
        this.agrRslrMdr = agrRslrMdr;
    }

    public float getAgrRslrDiscFees() {
        return agrRslrDiscFees;
    }

    public void setAgrRslrDiscFees(float agrRslrDiscFees) {
        this.agrRslrDiscFees = agrRslrDiscFees;
    }

    public float getAgrRslrCgst() {
        return agrRslrCgst;
    }

    public void setAgrRslrCgst(float agrRslrCgst) {
        this.agrRslrCgst = agrRslrCgst;
    }

    public float getAgrRslrSgst() {
        return agrRslrSgst;
    }

    public void setAgrRslrSgst(float agrRslrSgst) {
        this.agrRslrSgst = agrRslrSgst;
    }

    public float getAgrRslrIgst() {
        return agrRslrIgst;
    }

    public void setAgrRslrIgst(float agrRslrIgst) {
        this.agrRslrIgst = agrRslrIgst;
    }

    public float getAgrRslrInstv() {
        return agrRslrInstv;
    }

    public void setAgrRslrInstv(float agrRslrInstv) {
        this.agrRslrInstv = agrRslrInstv;
    }

    public float getAgrRslrTds() {
        return agrRslrTds;
    }

    public void setAgrRslrTds(float agrRslrTds) {
        this.agrRslrTds = agrRslrTds;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnResponseCode() {
        return txnResponseCode;
    }

    public void setTxnResponseCode(String txnResponseCode) {
        this.txnResponseCode = txnResponseCode;
    }

    public Date getResponseReceivedTime() {
        return responseReceivedTime;
    }

    public void setResponseReceivedTime(Date responseReceivedTime) {
        this.responseReceivedTime = responseReceivedTime;
    }

    public String getrRNumber() {
        return rRNumber;
    }

    public void setrRNumber(String rRNumber) {
        this.rRNumber = rRNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Double getTxnAdditionalAmount() {
        return TxnAdditionalAmount;
    }

    public void setTxnAdditionalAmount(Double txnAdditionalAmount) {
        TxnAdditionalAmount = txnAdditionalAmount;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getMti1() {
        return mti1;
    }

    public void setMti1(String mti1) {
        this.mti1 = mti1;
    }

    public String getSwitchTxnDate() {
        return switchTxnDate;
    }

    public void setSwitchTxnDate(String switchTxnDate) {
        this.switchTxnDate = switchTxnDate;
    }

    public Date getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Date insertedAt) {
        this.insertedAt = insertedAt;
    }

    @Override
    public String toString() {
        return "SwitchTransactions{" +
                "id=" + id +
                ", txnDate=" + txnDate +
                ", mid='" + mid + '\'' +
                ", tid='" + tid + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", city='" + city + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardType='" + cardType + '\'' +
                ", rRN='" + rRN + '\'' +
                ", associationName='" + associationName + '\'' +
                ", approvalCode='" + approvalCode + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", txnamount=" + txnamount +
                ", mrlmdr=" + mrlmdr +
                ", gst=" + gst +
                ", netamount=" + netamount +
                ", paymentdate=" + paymentdate +
                ", aggregatorName='" + aggregatorName + '\'' +
                ", tradename='" + tradename + '\'' +
                ", terminalSerialNo='" + terminalSerialNo + '\'' +
                ", cardType1='" + cardType1 + '\'' +
                ", assocationFee=" + assocationFee +
                ", narration='" + narration + '\'' +
                ", cardProduct='" + cardProduct + '\'' +
                ", cardTypeA=" + cardTypeA +
                ", new_IC_Fee=" + new_IC_Fee +
                ", bank='" + bank + '\'' +
                ", location='" + location + '\'' +
                ", state='" + state + '\'' +
                ", mcc_Desc='" + mcc_Desc + '\'' +
                ", mcc=" + mcc +
                ", ne_MDR='" + ne_MDR + '\'' +
                ", mdr_percentage=" + mdr_percentage +
                ", surcharge=" + surcharge +
                ", new_non_ic_fee=" + new_non_ic_fee +
                ", cc_fuel_new_ic_fee=" + cc_fuel_new_ic_fee +
                ", ifsc='" + ifsc + '\'' +
                ", mdr_perc_table=" + mdr_perc_table +
                ", mdr_bj_calculation=" + mdr_bj_calculation +
                ", incentive=" + incentive +
                ", surchargeNewOmc=" + surchargeNewOmc +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", shell='" + shell + '\'' +
                ", sorce='" + sorce + '\'' +
                ", mti='" + mti + '\'' +
                ", cardTypeDesc='" + cardTypeDesc + '\'' +
                ", creditDebitFlag='" + creditDebitFlag + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", interchangeType='" + interchangeType + '\'' +
                ", posEntryMode='" + posEntryMode + '\'' +
                ", stan='" + stan + '\'' +
                ", transactionCurrencyCode=" + transactionCurrencyCode +
                ", transactionType='" + transactionType + '\'' +
                ", txnTime='" + txnTime + '\'' +
                ", month='" + month + '\'' +
                ", authCode='" + authCode + '\'' +
                ", mdrAmount=" + mdrAmount +
                ", sgst=" + sgst +
                ", igst=" + igst +
                ", tds=" + tds +
                ", minUtilFees=" + minUtilFees +
                ", merPayment=" + merPayment +
                ", netPayment=" + netPayment +
                ", txnSharingAmount=" + txnSharingAmount +
                ", traceNo='" + traceNo + '\'' +
                ", msgType='" + msgType + '\'' +
                ", respCode='" + respCode + '\'' +
                ", authIdRsp='" + authIdRsp + '\'' +
                ", setlDate=" + setlDate +
                ", uploadDate=" + uploadDate +
                ", onnusOffus='" + onnusOffus + '\'' +
                ", txnMode='" + txnMode + '\'' +
                ", network='" + network + '\'' +
                ", remarks='" + remarks + '\'' +
                ", insCode=" + insCode +
                ", prjSerialNumber=" + prjSerialNumber +
                ", paymentTo='" + paymentTo + '\'' +
                ", cardSubCategory='" + cardSubCategory + '\'' +
                ", cardCategory='" + cardCategory + '\'' +
                ", agrNetPayment=" + agrNetPayment +
                ", paymentMode='" + paymentMode + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", switchType='" + switchType + '\'' +
                ", txnCount=" + txnCount +
                ", agrRslrMinFees=" + agrRslrMinFees +
                ", agrGrossAmount=" + agrGrossAmount +
                ", merchantMdr=" + merchantMdr +
                ", cgst=" + cgst +
                ", mdrDiscFee=" + mdrDiscFee +
                ", agrRslrMdr=" + agrRslrMdr +
                ", agrRslrDiscFees=" + agrRslrDiscFees +
                ", agrRslrCgst=" + agrRslrCgst +
                ", agrRslrSgst=" + agrRslrSgst +
                ", agrRslrIgst=" + agrRslrIgst +
                ", agrRslrInstv=" + agrRslrInstv +
                ", agrRslrTds=" + agrRslrTds +
                ", account='" + account + '\'' +
                ", instance='" + instance + '\'' +
                ", txnType='" + txnType + '\'' +
                ", txnResponseCode='" + txnResponseCode + '\'' +
                ", responseReceivedTime=" + responseReceivedTime +
                ", rRNumber='" + rRNumber + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", TxnAdditionalAmount=" + TxnAdditionalAmount +
                ", urn='" + urn + '\'' +
                ", mti1='" + mti1 + '\'' +
                ", switchTxnDate='" + switchTxnDate + '\'' +
                ", mdrSharingAmount=" + mdrSharingAmount +
                ", txn_Type='" + txn_Type + '\'' +
                ", insertedAt=" + insertedAt +
                '}';
    }
}
