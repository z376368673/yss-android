package com.zh.tszj.bean;

import java.io.Serializable;

public class GoodsInfoBean implements Serializable {


    /**
     * gd_kc : 10
     * gd_mc : 汉中绿茶
     * gd_gg_bh : 克
     * gd_price : 122
     * bh : 2
     * gd_screen_pic : /MTIwLjc5LjIxNy40OQ==/yssds/images/sj/2/SP/goods/1548210154296.webp
     * gd_bq : 1
     * gd_szd_vl : 四川省,广元市
     * sp_mc : 茗茶
     * gd_xx_js : 乐品乐茶碧螺春绿茶2018新茶特级茶叶散装明前苏州春茶嫩芽125g*2
     * gd_bh : 12
     * gd_zdy_bq :
     * gd_xx_pic : /MTIwLjc5LjIxNy40OQ==/yssds/images/sj/2/SP/goods/1548210154829.webp,/MTIwLjc5LjIxNy40OQ==/yssds/images/sj/2/SP/goods/1548210155281.webp,/MTIwLjc5LjIxNy40OQ==/yssds/images/sj/2/SP/goods/1548210155727.webp,/MTIwLjc5LjIxNy40OQ==/yssds/images/sj/2/SP/goods/1548210156180.webp
     * gd_gg : 20
     * gd_cnfhsj : 3
     */

    private int sp_bh;//商品所在商铺编号
    private String sp_mc;//商品所在商铺名称
    private String sp_logo;//商品所在商铺logo
    private int gd_kc;//库存数量
    private String gd_mc;//商品名称
    private String gd_gg_bh;//商品规格
    private String gd_price;//商品价格
    private String bh;//购物车该条信息
    private String gd_screen_pic;//商品主屏幕
    private String gd_bq;//商品标签（1正品
    private String gd_szd_vl;//商品所在地
    private String gd_xx_js;//商品详情介绍
    private int gd_bh;//商品编号
    private int gd_id;////商品编号
    private String gd_zdy_bq;//商品自定义标签
    private String gd_xx_pic;//商品详情图片
    private String gd_gg;//商品规格
    private String gd_cnfhsj;//
    private String gd_zbt;//商品主标题
    private int gm_num;//购买数量
    public boolean isChecked=false;

    public int getGd_kc() {
        return gd_kc;
    }

    public void setGd_kc(int gd_kc) {
        this.gd_kc = gd_kc;
    }

    public String getGd_mc() {
        return gd_mc;
    }

    public void setGd_mc(String gd_mc) {
        this.gd_mc = gd_mc;
    }

    public String getGd_gg_bh() {
        return gd_gg_bh;
    }

    public void setGd_gg_bh(String gd_gg_bh) {
        this.gd_gg_bh = gd_gg_bh;
    }

    public String getGd_price() {
        return gd_price;
    }

    public void setGd_price(String gd_price) {
        this.gd_price = gd_price;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getGd_screen_pic() {
        return gd_screen_pic;
    }

    public void setGd_screen_pic(String gd_screen_pic) {
        this.gd_screen_pic = gd_screen_pic;
    }

    public String getGd_bq() {
        return gd_bq;
    }

    public void setGd_bq(String gd_bq) {
        this.gd_bq = gd_bq;
    }

    public String getGd_szd_vl() {
        return gd_szd_vl;
    }

    public void setGd_szd_vl(String gd_szd_vl) {
        this.gd_szd_vl = gd_szd_vl;
    }

    public String getSp_mc() {
        return sp_mc;
    }

    public void setSp_mc(String sp_mc) {
        this.sp_mc = sp_mc;
    }

    public String getGd_xx_js() {
        return gd_xx_js;
    }

    public void setGd_xx_js(String gd_xx_js) {
        this.gd_xx_js = gd_xx_js;
    }

    public int getGd_bh() {
        return gd_bh;
    }

    public void setGd_bh(int gd_bh) {
        this.gd_bh = gd_bh;
    }

    public String getGd_zdy_bq() {
        return gd_zdy_bq;
    }

    public void setGd_zdy_bq(String gd_zdy_bq) {
        this.gd_zdy_bq = gd_zdy_bq;
    }

    public String getGd_xx_pic() {
        return gd_xx_pic;
    }

    public void setGd_xx_pic(String gd_xx_pic) {
        this.gd_xx_pic = gd_xx_pic;
    }

    public String getGd_gg() {
        return gd_gg;
    }

    public void setGd_gg(String gd_gg) {
        this.gd_gg = gd_gg;
    }

    public String getGd_cnfhsj() {
        return gd_cnfhsj;
    }

    public void setGd_cnfhsj(String gd_cnfhsj) {
        this.gd_cnfhsj = gd_cnfhsj;
    }

    public String getGd_zbt() {
        return gd_zbt;
    }

    public void setGd_zbt(String gd_zbt) {
        this.gd_zbt = gd_zbt;
    }

    public int getGm_num() {
        return gm_num;
    }

    public void setGm_num(int gm_num) {
        this.gm_num = gm_num;
    }

    public int getSp_bh() {
        return sp_bh;
    }

    public void setSp_bh(int sp_bh) {
        this.sp_bh = sp_bh;
    }

    public String getSp_logo() {
        return sp_logo;
    }

    public void setSp_logo(String sp_logo) {
        this.sp_logo = sp_logo;
    }

    public int getGd_id() {
        return gd_id;
    }

    public void setGd_id(int gd_id) {
        this.gd_id = gd_id;
    }
}
