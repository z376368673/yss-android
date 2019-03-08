package com.zh.tszj.activity.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.zh.tszj.adapter.Home1Adapter;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseFragment;
import com.zh.tszj.bean.ShopClass;
import com.zh.tszj.bean.ShopDetails;
import com.zh.tszj.config.ViewUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

public class HomeItem1Fragment extends BaseFragment {

    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    Home1Adapter adapter;
    ShopClass shopClass;
    int type = 0;

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
        initRecyclerViewHeadView();
        getGoods(true,type);
    }
    private void initRecyclerViewHeadView(){
        refreshLayout.setOnRefreshLoadMoreListener(this);
        adapter = new Home1Adapter(getActivity());
        ViewUtils.initGridRV(getContext(),recyclerView,2,true);
        recyclerView.addItemDecoration(new ShopItemDecoration(UScreenUtil.dp2px(10)));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled: "+dx+"------------------------"+dy);
                if (dx>20||dy>20){
                    recyclerView.setBackgroundResource(R.color.white);
                }

                if(recyclerView.canScrollVertically(1)){
                    Log.i(TAG, "direction 1: true");
                }else {
                    Log.i(TAG, "direction 1: false");//滑动到底部
                }
                if(recyclerView.canScrollVertically(-1)){
                    Log.i(TAG, "direction -1: true");
                }else {
                    Log.i(TAG, "direction -1: false");//滑动到顶部
                    recyclerView.setBackgroundResource(R.color.transparent);
                }


            }
        });

    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        adapter.setItemChickClichListener((ListViewItemChickClichListener<ShopDetails>) (view, data, p) -> starToWeb(data.id,"0"));
    }


    /**
     * 获取通知信息list
     */
    protected void getGoods(boolean isShow,int type) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).vipGoods(3, curr, limit);
        HttpClient.enqueue(call, new ResultDataCallback(getActivity(), isShow) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    if (curr==1){
                        adapter.clear();
                        adapter.addAll(bean.getListData(ShopDetails.class));
                    }else {
                        adapter.addAll(bean.getListData(ShopDetails.class));
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
        getGoods(false,type);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        getGoods(false,type);
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
            if (position>=3){
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
