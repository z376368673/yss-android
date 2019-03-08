package com.zh.tszj.activity.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.ui.adapter.ListViewItemChickClichListener;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UNavigationBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zh.tszj.R;
import com.zh.tszj.adapter.ShopAdapter;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.ShopDetails;
import com.zh.tszj.config.ViewUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 畅销榜 分享有奖 .。。。。
 */
public class ShopTypeActivity extends BaseActivity {
    UNavigationBar uNavigationBar;

    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    ShopAdapter adapter;

    TextView tv_all;
    TextView tv_volume;
    LinearLayout layout_price;
    TextView tv_price;
    ImageView iv_sort;
    private int type;

    @Override
    protected int onLayoutResID() {
        return R.layout.act_shop_type;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String title = getIntent().getAction();
        uNavigationBar.setTitle(title);
        uNavigationBar.setBack(this);
        layout_price.setTag(true);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        ViewUtils.initGridRV(this,recyclerView,2,true);
        adapter = new ShopAdapter(this);
        recyclerView.setAdapter(adapter);
        if ("畅销榜".equals(title)){
            type = 1;
            vipGoods(1);
        }else {
            type = 2;
        }
        vipGoods(type);
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        tv_all.setOnClickListener( v -> {
            selectView(0);
        });
        tv_volume.setOnClickListener( v -> {
            selectView(1);
        });
        layout_price.setOnClickListener( v -> {
            selectView(2);
        });
        adapter.setItemChickClichListener(new ListViewItemChickClichListener<ShopDetails>() {
            @Override
            public void onClickItemListener(View view, ShopDetails data, int p) {
                starToWeb(data.id,"0");
            }
        });
    }

    private void selectView(int index) {
        switch (index) {
            case 0:
                tv_all.setTextColor(Color.parseColor("#1B1B1B"));
                tv_volume.setTextColor(Color.parseColor("#999999"));
                tv_price.setTextColor(Color.parseColor("#999999"));
                break;
            case 1:
                tv_all.setTextColor(Color.parseColor("#999999"));
                tv_volume.setTextColor(Color.parseColor("#1B1B1B"));
                tv_price.setTextColor(Color.parseColor("#999999"));
                break;
            case 2:
                boolean flag = (boolean) layout_price.getTag();
                if (flag) {
                    layout_price.setTag(false);
                    iv_sort.setImageResource(R.mipmap.ic_sort_down);
                } else {
                    layout_price.setTag(true);
                    iv_sort.setImageResource(R.mipmap.ic_sort_up);
                }
                tv_all.setTextColor(Color.parseColor("#999999"));
                tv_volume.setTextColor(Color.parseColor("#999999"));
                tv_price.setTextColor(Color.parseColor("#1B1B1B"));
                break;
        }
    }

    /**
     * 根据类型获取商品
     */
    protected void vipGoods(int type) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).vipGoods(type, curr, limit);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    List list = bean.getListData(ShopDetails.class);
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
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        vipGoods(type);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        vipGoods(type);
    }
    @Override
    public void onClick(View v) {

    }
}
