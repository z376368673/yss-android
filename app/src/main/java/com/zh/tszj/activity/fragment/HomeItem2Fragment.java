package com.zh.tszj.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.ui.adapter.ListViewItemChickClichListener;
import com.android.baselib.util.UScreenUtil;
import com.android.baselib.util.UToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zh.tszj.R;
import com.zh.tszj.adapter.Home2Adapter;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseFragment;
import com.zh.tszj.bean.ShopClass;
import com.zh.tszj.bean.ShopDetails;
import com.zh.tszj.config.ViewUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeItem2Fragment extends BaseFragment {

    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    Home2Adapter adapter;
    ShopClass shopClass;
    int type;
    int type2;

    @Override
    protected int onLayoutResID() {
        return R.layout.fm_home_item_1;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Bundle bundle = getArguments();
        shopClass = (ShopClass) bundle.getSerializable("key");
        type = shopClass.id;
        mHandler.sendEmptyMessageDelayed(1,300);
    }
    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        initRecyclerViewHeadView();
        adapter.setShopDetailsClickListener((view, data, p) -> starToWeb(data.id,"0"));
    }
    private void initRecyclerViewHeadView() {
        refreshLayout.setOnRefreshLoadMoreListener(this);
        adapter = new Home2Adapter(getActivity());
        adapter.setShopClass(shopClass);
        adapter.setShopClassClickListener((view, data, p) -> {
//            UToastUtil.showToastShort(shopClass.cat_name);
            type2 = data.id;
            getGoods(true,type,type2);
        });
        ViewUtils.initGridRV(getContext(), recyclerView, 2, true);
        recyclerView.addItemDecoration(new ShopItemDecoration(UScreenUtil.dp2px(10)));
        recyclerView.setAdapter(adapter);
        getGoods(true,type,type2);
    }
    /**
     *
     * 获取商品信息
     */
    protected void getGoods(boolean isShow,int type,int type2) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).getGoods(type,type2, curr, limit);
        HttpClient.enqueue(call, new ResultDataCallback(getActivity(), isShow) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    List list = bean.getListData(ShopDetails.class);
                    if (curr==1){
                        adapter.clear();
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
        getGoods(false,type,type2);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getGoods(false,type,type2);
    }

  //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (resultCode == Activity.RESULT_OK) {
            UToastUtil.showToastLong(data.getAction());
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 设置商品列表的边距 没办法 只能这么搞
     */
    class ShopItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public ShopItemDecoration(int space) {
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildPosition(view);
            //这个决定哪个item 开始制定边距
            if (position>=1){
                if (position%2==0){
                    outRect.right = space;
                    outRect.left = space/2;
                }else {
                    outRect.left = space;
                    outRect.right = space/2;
                }
            }
        }
    }
}
