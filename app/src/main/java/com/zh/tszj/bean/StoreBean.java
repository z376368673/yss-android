package com.zh.tszj.bean;

import java.io.Serializable;

public class StoreBean implements Serializable {
    public String sp_no;//商铺编号
    public int bh;//商铺主键
    public String sp_mc;//商铺名称
    public String sp_logo;//商铺logo
    public String jydz;//经营地址
    public String jydz_sx;//经营地址手写

    public int getBh() {
        return bh;
    }

    public void setBh(int bh) {
        this.bh = bh;
    }

    public String getSp_mc() {
        return sp_mc;
    }

    public void setSp_mc(String sp_mc) {
        this.sp_mc = sp_mc;
    }

    public String getSp_logo() {
        return sp_logo;
    }

    public void setSp_logo(String sp_logo) {
        this.sp_logo = sp_logo;
    }

    public String getSp_no() {
        return sp_no;
    }

    public void setSp_no(String sp_no) {
        this.sp_no = sp_no;
    }

    public String getJydz() {
        return jydz;
    }

    public void setJydz(String jydz) {
        this.jydz = jydz;
    }

    public String getJydz_sx() {
        return jydz_sx;
    }

    public void setJydz_sx(String jydz_sx) {
        this.jydz_sx = jydz_sx;
    }
}
