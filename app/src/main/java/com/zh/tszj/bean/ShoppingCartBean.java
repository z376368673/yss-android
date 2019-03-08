package com.zh.tszj.bean;

import java.io.Serializable;
import java.util.List;

/**购物车实体*/
public class ShoppingCartBean implements Serializable {
    private List<GoodsInfoBean> goods;
    private StoreBean sp;
    private String postMethod="快递 免邮";//固定
    public boolean isChecked=false;


    public List<GoodsInfoBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfoBean> goods) {
        this.goods = goods;
    }

    public StoreBean getSp() {
        return sp;
    }

    public void setSp(StoreBean sp) {
        this.sp = sp;
    }

    public String getPostMethod() {
        return postMethod;
    }

    public void setPostMethod(String postMethod) {
        this.postMethod = postMethod;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
