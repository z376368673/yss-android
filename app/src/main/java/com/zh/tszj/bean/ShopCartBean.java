package com.zh.tszj.bean;

import java.util.List;

public class ShopCartBean {

    /**
     * msg : null
     * msgCode : null
     * curr : null
     * limit : null
     * id : 2EFF8EE922E14B9592F9F322D1E753A1
     * type : null
     * brand_type : null
     * shop_name : 小店
     * province : null
     * city : null
     * county : null
     * address : null
     * linkman : null
     * linkman_tel : null
     * intro : null
     * shop_logo : http://120.79.217.49:8889/upload/20190304/1551691286404.png
     * shop_background_img : null
     * sale_num : null
     * goodsList : [{"msg":null,"msgCode":null,"curr":null,"limit":null,"id":null,"goods_sn":null,"product_no":null,"goods_name":"储存","goods_img":"http://120.79.217.49:8889/upload/20190304/1551691321210.png","shop_id":null,"market_price":100,"shop_price":null,"warn_stock":null,"goods_stock":null,"goods_unit":null,"is_sale":null,"is_best":null,"is_hot":null,"is_new":null,"is_recom":null,"goods_cat_idpath":null,"goods_cat_id":null,"shop_cat_id1":null,"shop_cat_id2":null,"brand_id":null,"goods_status":null,"sale_num":null,"sale_time":null,"visit_num":null,"appraise_num":null,"is_spec":null,"goods_seo_keywords":null,"del_flag":null,"create_date":null,"update_date":null,"is_show_stock":null,"province":null,"city":null,"county":null,"deliver_date":null,"commission":null,"tag":null,"custom_tag":null,"stock_way":null,"gallery":null,"goods_info":null,"append_address":null,"type":null}]
     * goodsImg : null
     */

    public String id;
    public String type;
    public String brand_type;
    public String shop_name;
    public String province;
    public String city;
    public String county;
    public String address;
    public String linkman;
    public String linkman_tel;
    public String intro;
    public String shop_logo;
    public String shop_background_img;
    public int sale_num;
    public String goodsImg;
    public boolean isChecked = false;
    public List<GoodsListBean> goodsList;

    public void setCheckeds(boolean checked) {
        isChecked = checked;
    }

    public class GoodsListBean {
        /**
         * msg : null
         * msgCode : null
         * curr : null
         * limit : null
         * id : null
         * goods_sn : null
         * product_no : null
         * goods_name : 储存
         * goods_img : http://120.79.217.49:8889/upload/20190304/1551691321210.png
         * shop_id : null
         * market_price : 100
         * shop_price : null
         * warn_stock : null
         * goods_stock : null
         * goods_unit : null
         * is_sale : null
         * is_best : null
         * is_hot : null
         * is_new : null
         * is_recom : null
         * goods_cat_idpath : null
         * goods_cat_id : null
         * shop_cat_id1 : null
         * shop_cat_id2 : null
         * brand_id : null
         * goods_status : null
         * sale_num : null
         * sale_time : null
         * visit_num : null
         * appraise_num : null
         * is_spec : null
         * goods_seo_keywords : null
         * del_flag : null
         * create_date : null
         * update_date : null
         * is_show_stock : null
         * province : null
         * city : null
         * county : null
         * deliver_date : null
         * commission : null
         * tag : null
         * custom_tag : null
         * stock_way : null
         * gallery : null
         * goods_info : null
         * append_address : null
         * type : null
         * goods_spec_id : "id"//规格
         */

        public String id;
        public String goods_sn;
        public String product_no;
        public String goods_name;//商品名称
        public String shop_name;//商铺名字
        public String goods_img;
        public String goods_spec_id;
        public String shop_id;
        public int market_price;
        public String shop_price;
        public String warn_stock;
        public int goods_stock;
        public String goods_unit;
        public String is_sale;
        public String is_best;
        public String is_hot;
        public String is_new;
        public String is_recom;
        public String goods_cat_idpath;
        public String goods_cat_id;
        public String shop_cat_id1;
        public String shop_cat_id2;
        public String brand_id;
        public String goods_status;
        public String sale_num;
        public String sale_time;
        public String visit_num;
        public String appraise_num;
        public String is_spec;
        public String goods_seo_keywords;
        public String del_flag;
        public String create_date;
        public String update_date;
        public String is_show_stock;
        public String province;
        public String city;
        public String county;
        public String deliver_date;
        public String commission;
        public String tag;
        public String custom_tag;
        public String stock_way;
        public String gallery;
        public String goods_info;
        public String append_address;
        public String type;
        public boolean isChecked = false;

        public void setChecked(boolean checked) {
            this.isChecked = checked;
            setCheckeds(checked);
        }
    }
}
