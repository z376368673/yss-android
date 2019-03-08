package com.zh.tszj.adapter.holder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.baselib.view.NoScrollGridView;
import com.youth.banner.Banner;
import com.zh.tszj.R;
import com.zh.tszj.banner.GlideImageLoader;
import com.zh.tszj.bean.AdvertBean;
import com.zh.tszj.db.DatabaseUtils;

import java.util.List;

/**
 * 单图广告
 */
public class AdvSingleViewHolder extends RecyclerView.ViewHolder {
    Activity activity;
   ImageView advert_single;
    public AdvSingleViewHolder(View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;
        advert_single = itemView.findViewById(R.id.advert_single);
        setAdvertData();
    }

    public void setAdvertData() {

    }


}