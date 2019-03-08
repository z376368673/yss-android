package com.zh.tszj.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.baselib.ui.UBaseActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zh.tszj.activity.webview.ShopDetailsWebActivity;

public abstract class BaseActivity extends UBaseActivity implements OnRefreshLoadMoreListener {
    public int curr = 1;
    public int limit = 10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity  = this;
        context  = this;
    }

    @Override
    public void handleMessage(Message message) {

    }
    public void starToWeb(String id,String value){
        Intent intent = new Intent(this,ShopDetailsWebActivity.class);
        intent.putExtra(ShopDetailsWebActivity.ID,id);
        intent.putExtra(ShopDetailsWebActivity.VALUE,value);
        startActivity(intent);
    }
    /**
     * 加载更多
     * @param refreshLayout
     */
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
       refreshLayout.finishLoadMore(true);
        curr++;
    }
    /**
     * 刷新
     * @param refreshLayout
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
       refreshLayout.finishRefresh(true);
        curr=1;
    }

}
