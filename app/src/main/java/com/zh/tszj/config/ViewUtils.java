package com.zh.tszj.config;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zh.tszj.custom.FullyGridLayoutManager;

public class ViewUtils {

    public static LinearLayoutManager initLinearRV(Context context,RecyclerView recyclerView,boolean isScroll){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context) ;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(isScroll);
        return  layoutManager;
    }

    public static RecyclerView.LayoutManager initGridRV(Context context, RecyclerView recyclerView,int spanCount,boolean isScroll){
        GridLayoutManager layoutManager = new FullyGridLayoutManager(context,spanCount){
            @Override
            public int getSpanCount() {
                return super.getSpanCount();
            }
        };
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(isScroll);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        return  layoutManager;
    }

}
