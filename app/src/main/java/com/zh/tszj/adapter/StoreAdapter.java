package com.zh.tszj.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.ui.adapter.RCSingleAdapter;
import com.android.baselib.ui.adapter.viewholder.RCViewHolder;
import com.zh.tszj.R;
import com.zh.tszj.activity.home.StoreHomeActivity;
import com.zh.tszj.bean.StoreDetails;

import java.util.ArrayList;

/**
 * Author:37636
 * QQ:376368673
 * Time:2018/11/13
 * Description:This is DemoAdapter
 */
public class StoreAdapter extends RCSingleAdapter<StoreDetails, RCViewHolder> {
    Activity act;
    private boolean isQjd = false;
    public StoreAdapter(Activity activity) {
        //第一个参数必填 写adapter得 layout 布局id
        super(R.layout.item_store, new ArrayList());
        act = activity;
    }

    public void setQjd(boolean qjd) {
        isQjd = qjd;
    }

    @Override
    protected void convert(RCViewHolder helper, StoreDetails item) {
        super.convert(helper, item);
        ImageView iv_store_head = helper.getView(R.id.iv_store_head);

        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_count = helper.getView(R.id.tv_count);
        TextView tv_sell = helper.getView(R.id.tv_sell);
        TextView tv_button = helper.getView(R.id.tv_button);
        LinearLayout layout_qjd = helper.getView(R.id.layout_qjd);

        ImageView iv_image1 = helper.getView(R.id.iv_image1);
        ImageView iv_image2 = helper.getView(R.id.iv_image2);
        ImageView iv_image3 = helper.getView(R.id.iv_image3);

        tv_name.setText(item.shop_name);
        tv_sell.setText(String.format(" %s 件在售中",item.sale_num+""));
//        tv_button.setOnClickListener(v -> {
//            Intent intent = new Intent(act, StoreHomeActivity.class);
//            intent.setAction(item.id);
//            act.startActivity(intent);
//        });
        if (isQjd){
            layout_qjd.setVisibility(View.VISIBLE);
        }else {
            layout_qjd.setVisibility(View.INVISIBLE);
        }
        helper.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(act, StoreHomeActivity.class);
            intent.setAction(item.id);
            act.startActivity(intent);
        });
    }
}
