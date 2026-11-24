package com.bijlipay.Model.DTO;

public class MerchantDataDto {

    public String merchantName;
    public int format;
    public String mailId;
    public String mailIdBcc;
    public String mailIdCC;
    public String mid;
    public String verifiedMaildId;

    public String getVerifiedMaildId() {
        return verifiedMaildId;
    }

    public void setVerifiedMaildId(String verifiedMaildId) {
        this.verifiedMaildId = verifiedMaildId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMailIdBcc() {
        return mailIdBcc;
    }

    public void setMailIdBcc(String mailIdBcc) {
        this.mailIdBcc = mailIdBcc;
    }

    public String getMailIdCC() {
        return mailIdCC;
    }

    public void setMailIdCC(String mailIdCC) {
        this.mailIdCC = mailIdCC;
    }
}
