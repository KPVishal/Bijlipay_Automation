package com.bijlipay.Model.DTO;

import java.util.Date;

public class FrmDataDTO {
    private String eventType;
    private String eventSubType;
    private String eventName;
    private String msgBody;
    private String host_id;
    private String sys_time;
    private String event_id;
    private String channel;
    private String user_id;
    private String user_type;
    private String cust_id;
    private String device_id;
    private String ip_address;
    private String ip_country;
    private String ip_city;
    private String cust_name;
    private String succ_fail_flg;
    private String error_code;
    private String error_desc;
    private String dr_account_id;
    private float txn_amt;
    private float avl_bal;
    private String cr_account_id;
    private String cr_ifsc_code;
    private String payee_id;
    private String payee_name;
    private String tran_type;
    private String entry_mode;
    private String branch_id;
    private String acct_ownership;
    private String acct_open_date;
    private String part_tran_type;
    private String country_code;
    private String cust_card_id;
    private String bin;
    private float mcc_code;
    private String terminal_id;
    private String merchant_id;
    private String cust_mob_no;
    private String txn_secured_flag;
    private String product_code;
    private String dev_owner_id;
    private float atm_dom_limit;
    private float atm_intr_limit;
    private float pos_ecom_dom_limit;
    private float pos_ecom_intr_limit;
    private String tran_date;
    private String mask_card_no;
    private String state_code;
    private String card_type;
    private String tran_time;
    private String pos_entry_mode;
    private String interbankcard;
    private String decline_type;

    public FrmDataDTO() {
    }

    public FrmDataDTO(String eventType, String eventSubType, String eventName, String msgBody, String host_id, String sys_time, String event_id,
                      String channel, String user_id, String user_type, String cust_id, String device_id, String ip_address, String ip_country,
                      String ip_city, String cust_name, String succ_fail_flg, String error_code, String error_desc, String dr_account_id, float txn_amt,
                      float avl_bal, String cr_account_id, String cr_ifsc_code, String payee_id, String payee_name, String tran_type, String entry_mode,
                      String branch_id, String acct_ownership, String acct_open_date, String part_tran_type, String country_code, String cust_card_id,
                      String bin, float mcc_code, String terminal_id, String merchant_id, String cust_mob_no, String txn_secured_flag, String product_code,
                      String dev_owner_id, float atm_dom_limit, float atm_intr_limit, float pos_ecom_dom_limit, float pos_ecom_intr_limit,
                      String tran_date, String mask_card_no, String state_code, String card_type, String tran_time, String pos_entry_mode,
                      String interbankcard, String decline_type) {
        this.eventType = eventType;
        this.eventSubType = eventSubType;
        this.eventName = eventName;
        this.msgBody = msgBody;
        this.host_id = host_id;
        this.sys_time = sys_time;
        this.event_id = event_id;
        this.channel = channel;
        this.user_id = user_id;
        this.user_type = user_type;
        this.cust_id = cust_id;
        this.device_id = device_id;
        this.ip_address = ip_address;
        this.ip_country = ip_country;
        this.ip_city = ip_city;
        this.cust_name = cust_name;
        this.succ_fail_flg = succ_fail_flg;
        this.error_code = error_code;
        this.error_desc = error_desc;
        this.dr_account_id = dr_account_id;
        this.txn_amt = txn_amt;
        this.avl_bal = avl_bal;
        this.cr_account_id = cr_account_id;
        this.cr_ifsc_code = cr_ifsc_code;
        this.payee_id = payee_id;
        this.payee_name = payee_name;
        this.tran_type = tran_type;
        this.entry_mode = entry_mode;
        this.branch_id = branch_id;
        this.acct_ownership = acct_ownership;
        this.acct_open_date = acct_open_date;
        this.part_tran_type = part_tran_type;
        this.country_code = country_code;
        this.cust_card_id = cust_card_id;
        this.bin = bin;
        this.mcc_code = mcc_code;
        this.terminal_id = terminal_id;
        this.merchant_id = merchant_id;
        this.cust_mob_no = cust_mob_no;
        this.txn_secured_flag = txn_secured_flag;
        this.product_code = product_code;
        this.dev_owner_id = dev_owner_id;
        this.atm_dom_limit = atm_dom_limit;
        this.atm_intr_limit = atm_intr_limit;
        this.pos_ecom_dom_limit = pos_ecom_dom_limit;
        this.pos_ecom_intr_limit = pos_ecom_intr_limit;
        this.tran_date = tran_date;
        this.mask_card_no = mask_card_no;
        this.state_code = state_code;
        this.card_type = card_type;
        this.tran_time = tran_time;
        this.pos_entry_mode = pos_entry_mode;
        this.interbankcard = interbankcard;
        this.decline_type = decline_type;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventSubType() {
        return eventSubType;
    }

    public void setEventSubType(String eventSubType) {
        this.eventSubType = eventSubType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getSys_time() {
        return sys_time;
    }

    public void setSys_time(String sys_time) {
        this.sys_time = sys_time;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getIp_country() {
        return ip_country;
    }

    public void setIp_country(String ip_country) {
        this.ip_country = ip_country;
    }

    public String getIp_city() {
        return ip_city;
    }

    public void setIp_city(String ip_city) {
        this.ip_city = ip_city;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getSucc_fail_flg() {
        return succ_fail_flg;
    }

    public void setSucc_fail_flg(String succ_fail_flg) {
        this.succ_fail_flg = succ_fail_flg;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public String getDr_account_id() {
        return dr_account_id;
    }

    public void setDr_account_id(String dr_account_id) {
        this.dr_account_id = dr_account_id;
    }

    public float getTxn_amt() {
        return txn_amt;
    }

    public void setTxn_amt(float txn_amt) {
        this.txn_amt = txn_amt;
    }

    public float getAvl_bal() {
        return avl_bal;
    }

    public void setAvl_bal(float avl_bal) {
        this.avl_bal = avl_bal;
    }

    public String getCr_account_id() {
        return cr_account_id;
    }

    public void setCr_account_id(String cr_account_id) {
        this.cr_account_id = cr_account_id;
    }

    public String getCr_ifsc_code() {
        return cr_ifsc_code;
    }

    public void setCr_ifsc_code(String cr_ifsc_code) {
        this.cr_ifsc_code = cr_ifsc_code;
    }

    public String getPayee_id() {
        return payee_id;
    }

    public void setPayee_id(String payee_id) {
        this.payee_id = payee_id;
    }

    public String getPayee_name() {
        return payee_name;
    }

    public void setPayee_name(String payee_name) {
        this.payee_name = payee_name;
    }

    public String getTran_type() {
        return tran_type;
    }

    public void setTran_type(String tran_type) {
        this.tran_type = tran_type;
    }

    public String getEntry_mode() {
        return entry_mode;
    }

    public void setEntry_mode(String entry_mode) {
        this.entry_mode = entry_mode;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getAcct_ownership() {
        return acct_ownership;
    }

    public void setAcct_ownership(String acct_ownership) {
        this.acct_ownership = acct_ownership;
    }

    public String getAcct_open_date() {
        return acct_open_date;
    }

    public void setAcct_open_date(String acct_open_date) {
        this.acct_open_date = acct_open_date;
    }

    public String getPart_tran_type() {
        return part_tran_type;
    }

    public void setPart_tran_type(String part_tran_type) {
        this.part_tran_type = part_tran_type;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCust_card_id() {
        return cust_card_id;
    }

    public void setCust_card_id(String cust_card_id) {
        this.cust_card_id = cust_card_id;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public float getMcc_code() {
        return mcc_code;
    }

    public void setMcc_code(float mcc_code) {
        this.mcc_code = mcc_code;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getCust_mob_no() {
        return cust_mob_no;
    }

    public void setCust_mob_no(String cust_mob_no) {
        this.cust_mob_no = cust_mob_no;
    }

    public String getTxn_secured_flag() {
        return txn_secured_flag;
    }

    public void setTxn_secured_flag(String txn_secured_flag) {
        this.txn_secured_flag = txn_secured_flag;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getDev_owner_id() {
        return dev_owner_id;
    }

    public void setDev_owner_id(String dev_owner_id) {
        this.dev_owner_id = dev_owner_id;
    }

    public float getAtm_dom_limit() {
        return atm_dom_limit;
    }

    public void setAtm_dom_limit(float atm_dom_limit) {
        this.atm_dom_limit = atm_dom_limit;
    }

    public float getAtm_intr_limit() {
        return atm_intr_limit;
    }

    public void setAtm_intr_limit(float atm_intr_limit) {
        this.atm_intr_limit = atm_intr_limit;
    }

    public float getPos_ecom_dom_limit() {
        return pos_ecom_dom_limit;
    }

    public void setPos_ecom_dom_limit(float pos_ecom_dom_limit) {
        this.pos_ecom_dom_limit = pos_ecom_dom_limit;
    }

    public float getPos_ecom_intr_limit() {
        return pos_ecom_intr_limit;
    }

    public void setPos_ecom_intr_limit(float pos_ecom_intr_limit) {
        this.pos_ecom_intr_limit = pos_ecom_intr_limit;
    }

    public String getTran_date() {
        return tran_date;
    }

    public void setTran_date(String tran_date) {
        this.tran_date = tran_date;
    }

    public String getMask_card_no() {
        return mask_card_no;
    }

    public void setMask_card_no(String mask_card_no) {
        this.mask_card_no = mask_card_no;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getTran_time() {
        return tran_time;
    }

    public void setTran_time(String tran_time) {
        this.tran_time = tran_time;
    }

    public String getPos_entry_mode() {
        return pos_entry_mode;
    }

    public void setPos_entry_mode(String pos_entry_mode) {
        this.pos_entry_mode = pos_entry_mode;
    }

    public String getInterbankcard() {
        return interbankcard;
    }

    public void setInterbankcard(String interbankcard) {
        this.interbankcard = interbankcard;
    }

    public String getDecline_type() {
        return decline_type;
    }

    public void setDecline_type(String decline_type) {
        this.decline_type = decline_type;
    }
}