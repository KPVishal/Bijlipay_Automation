package com.bijlipay.Model.DTO;

public class ReportSDTO {
    private String tid;
    private String date;
    private String name;
    private float netamout;

    public String getTid() {
        return tid;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public float getNetamout() {
        return netamout;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNetamout(float netamout) {
        this.netamout = netamout;
    }
}
