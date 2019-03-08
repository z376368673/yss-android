package com.zh.tszj.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.baselib.image.UImage;
import com.android.baselib.ui.adapter.RCSingleAdapter;
import com.android.baselib.ui.adapter.viewholder.RCViewHolder;
import com.zh.tszj.R;
import com.zh.tszj.activity.webview.ShopDetailsWebActivity;
import com.zh.tszj.bean.ShopDetails;

import java.util.ArrayList;

/**
 * Author:37636
 * QQ:376368673
 * Time:2018/11/13
 * Description:This is DemoAdapter
 */
public class ShopAdapter extends RCSingleAdapter<ShopDetails, RCViewHolder> {
    Activity act;

    public ShopAdapter(Activity activity) {
        //第一个参数必填 写adapter得 layout 布局id
        super(R.layout.item_shop, new ArrayList());
        act = activity;
    }
    @Override
    protected void convert(RCViewHolder helper, ShopDetails item) {
        super.convert(helper, item);
        ImageView iv_image = helper.getView(R.id.iv_image);
        TextView tv_des = helper.getView(R.id.tv_des);
        TextView tv_price = helper.getView(R.id.tv_price);
        if (item == null) return;
        UImage.getInstance().load(act, item.goods_img, iv_image);
        tv_des.setText(item.goods_name + "");
        tv_price.setText("￥"+item.market_price + "");
//        helper.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(act,ShopDetailsWebActivity.class);
//            intent.putExtra(ShopDetailsWebActivity.ID,item.id);
//            intent.putExtra(ShopDetailsWebActivity.VALUE,0);
//        });
    }
}
