package com.zh.tszj.adapter.holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zh.tszj.R;

/**
 * 标题
 */
public class TitleViewHolder extends RecyclerView.ViewHolder {
    Activity activity;
    TextView tv_shopType;
    public TitleViewHolder(View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;
        tv_shopType = itemView.findViewById(R.id.tv_shopType);
        tv_shopType.setText("精选好物");
    }
    public void setData(String title) {
        tv_shopType.setText("精选好物");
    }
}