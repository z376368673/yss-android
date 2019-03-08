package com.zh.tszj.activity.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UNavigationBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zh.tszj.R;
import com.zh.tszj.adapter.StoreAdapter;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.StoreDetails;
import com.zh.tszj.config.ViewUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 畅销榜 优选好货 .。。。。
 */
public class StoreTypeActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    UNavigationBar uNavigationBar;

    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    StoreAdapter adapter;
    String type = "-1";
    @Override
    protected int onLayoutResID() {
        return R.layout.act_store_type;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String title = getIntent().getAction();
        uNavigationBar.setTitle(title);
        uNavigationBar.setBack(this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        ViewUtils.initLinearRV(this,recyclerView,   true);
        adapter = new StoreAdapter(this);
        recyclerView.setAdapter(adapter);
         type = getIntent().getStringExtra("type");
        getStore(type);
        adapter.setQjd(false);
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
    }
    @Override
    public void onClick(View v) {

    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        getStore(type);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getStore(type);
    }
    /**
     * 商家列表
     */
    protected void getStore(String type) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).getShops(type,curr,limit);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    List list = bean.getListData(StoreDetails.class);
                    if (curr==1){
                        adapter.clearData();
                        adapter.addAll(list);
                    }else {
                        adapter.addAll(list);
                    }
                } else {
                    UToastUtil.showToastShort(error);
                }
            }
            @Override
            public void onFail(String error) {
                super.onFail(error);
            }
        });
    }

}
