package com.zh.tszj.adapter.holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.baselib.image.UImage;
import com.android.baselib.ui.adapter.ListViewItemChickClichListener;
import com.android.baselib.view.UImageView;
import com.zh.tszj.R;
import com.zh.tszj.bean.ShopDetails;

public class ShopHolder extends RecyclerView.ViewHolder{

    UImageView iv_image;
    TextView tv_des;
    TextView tv_price;
    Activity activity;
    public ShopHolder(Activity activity, View itemView) {
        super(itemView);
        this.activity = activity;
        iv_image = itemView.findViewById(R.id.iv_image);
        tv_des = itemView.findViewById(R.id.tv_des);
        tv_price = itemView.findViewById(R.id.tv_price);
    }

    public void setData(ShopDetails data,int p){
        if (data==null)return;
        UImage.getInstance().load(activity,data.goods_img,iv_image);
        tv_des.setText(data.goods_name+"");
        tv_price.setText("￥"+data.market_price+"");
        itemView.setOnClickListener(v -> {
            if (itemChickClichListener!=null)
                itemChickClichListener.onClickItemListener(itemView,data,p);
        });
    }

    ListViewItemChickClichListener itemChickClichListener;
    public void setItemChickClichListener(ListViewItemChickClichListener itemChickClichListener) {
        this.itemChickClichListener = itemChickClichListener;
    }
}
