package com.zh.tszj.activity.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.image.UImage;
import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.ui.adapter.ListViewItemChickClichListener;
import com.android.baselib.util.UScreenUtil;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UImageView;
import com.android.baselib.view.UNavigationBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zh.tszj.R;
import com.zh.tszj.adapter.ShopAdapter;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.ShopDetails;
import com.zh.tszj.bean.StoreDetails;
import com.zh.tszj.config.ViewUtils;
import com.zh.tszj.custom.FullyGridLayoutManager;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class StoreHomeActivity extends BaseActivity {

//    UNavigationBar uNavigationBar;
    LinearLayout layout_top;
    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerView;

    UImageView uImageView;
    TextView tv_name;
    TextView tv_store_intro;

    TextView tv_all;
    TextView tv_new;
    LinearLayout layout_price;
    TextView tv_price;
    ImageView iv_sort;
    String storeId  = "5579D3E4965B48C6A0D428802942CCFB";
    ShopAdapter adapter;
    StoreDetails objData;
    @Override
    protected int onLayoutResID() {
        return R.layout.act_store_home;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        storeId = getIntent().getAction();
        layout_price.setTag(true);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_top.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            params.topMargin = UScreenUtil.dp2px(30);
        }else {
            params.topMargin = UScreenUtil.dp2px(1);
        }
        layout_top.setLayoutParams(params);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        GridLayoutManager layoutManager = new FullyGridLayoutManager(context,2){
            @Override
            public int getSpanCount() {
                return super.getSpanCount();
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopAdapter(this);
        recyclerView.setAdapter(adapter);
        getShopInfo(storeId);
    }

    /**
     * 返回按钮
     * @param view
     */
    public void onBack(View view){
        finish();
    }
    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        tv_all.setOnClickListener( v -> {
            selectView(0);
        });
        tv_new.setOnClickListener( v -> {
            selectView(1);
        });
        layout_price.setOnClickListener( v -> {
            selectView(2);
        });
        adapter.setItemChickClichListener((view, data, p) -> {
            starToWeb(data.id,"0");
        });
    }

    private void selectView(int index) {
        switch (index) {
            case 0:
                tv_all.setTextColor(Color.parseColor("#1B1B1B"));
                tv_new.setTextColor(Color.parseColor("#999999"));
                tv_price.setTextColor(Color.parseColor("#999999"));
                break;
            case 1:
                tv_all.setTextColor(Color.parseColor("#999999"));
                tv_new.setTextColor(Color.parseColor("#1B1B1B"));
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
                tv_new.setTextColor(Color.parseColor("#999999"));
                tv_price.setTextColor(Color.parseColor("#1B1B1B"));
                break;
        }
    }
    /**
     * 获取商户信息
     */
    protected void getShopInfo(String id) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).shopInfo(id);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                     objData = bean.getObjData(StoreDetails.class);
                     if (objData!=null){
                         UImage.getInstance().load(activity,objData.shop_logo,uImageView);
                         tv_name.setText(objData.shop_name);
                         tv_store_intro.setText(objData.intro);
                         //获取商品信息
                         getShopList(objData);
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

    /**
     * 获取商户的商品列表
     * @param objData
     */
    protected void getShopList(StoreDetails objData) {
        if (objData==null){
            refreshLayout.finishRefresh(false);
            refreshLayout.finishLoadMore(false);
            return;
        }
        Call<ResponseBody> call = HttpClient.getApi(API.class).getGoods(objData.id,curr,limit);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    List list = bean.getListData(ShopDetails.class);
                    adapter.addData(list);
                    if (curr==1){
                        refreshLayout.finishRefresh(true);
                    }else {
                        refreshLayout.finishLoadMore(true);
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
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        curr++;
        getShopList(objData);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        curr = 1;
        getShopList(objData);
    }

    @Override
    public void onClick(View v) {

    }
}
