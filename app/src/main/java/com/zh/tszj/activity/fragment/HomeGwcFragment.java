package com.zh.tszj.activity.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.baselib.dialog.RxDialogSureCancel;
import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UNavigationBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zh.tszj.R;
import com.zh.tszj.activity.order.ConfirmOrderActivity;
import com.zh.tszj.activity.order.CreateOrderBean;
import com.zh.tszj.adapter.GwcListAdapter;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseFragment;
import com.zh.tszj.bean.ShopCartBean;
import com.zh.tszj.config.CacheData;
import com.zh.tszj.config.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeGwcFragment extends BaseFragment {

    UNavigationBar uNavigationBar;
    SmartRefreshLayout refreshLayout;
    LinearLayout layout_shop_no;

    RecyclerView recyclerView;
    GwcListAdapter adapter;


    LinearLayout ll_choosedAll;
    ImageView is_chooseAll;
    TextView tv_total_price;//合计多少元
    TextView bt_pay;//结算
    TextView bt_delete;//删除
    boolean isAllChecked = false;

    @Override
    protected int onLayoutResID() {
        return R.layout.fm_home_gwc;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCartData(true);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        uNavigationBar.setTitle("购物车");
        uNavigationBar.setRightText("编辑");
        refreshLayout.setOnRefreshLoadMoreListener(this);
//        refreshLayout.setEnableRefresh(false);
        uNavigationBar.setRightTextOnClick(v -> {
            if ("编辑".equals(uNavigationBar.getRightTextView().getText())) {
                bt_delete.setVisibility(View.VISIBLE);
                bt_pay.setVisibility(View.GONE);
                uNavigationBar.setRightText("完成");
            } else {
                bt_delete.setVisibility(View.GONE);
                bt_pay.setVisibility(View.VISIBLE);
                uNavigationBar.setRightText("编辑");
            }
        });
        ViewUtils.initLinearRV(getContext(), recyclerView, true);
        adapter = new GwcListAdapter(getContext(), false, false);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 获取购物车信息
     */
    protected void getCartData(boolean isShow) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).cartData(CacheData.getToken(), curr, limit);
        HttpClient.enqueue(call, new ResultDataCallback(getActivity(), isShow) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    List<ShopCartBean> list = bean.getListData(ShopCartBean.class);
                    adapter.clearData();
                    adapter.addAll(list);
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
     * 删除购物车单个商品
     */
    protected void deleteGoodByCart(boolean isShow, String ids) {
        if (TextUtils.isEmpty(ids)){
            UToastUtil.showToastShort("你还未选中要删除商品");
            return ;
        };
        Call<ResponseBody> call = HttpClient.getApi(API.class).deleteGoodByCart(CacheData.getToken(), ids);
        HttpClient.enqueue(call, new ResultDataCallback(getActivity(), isShow) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    adapter.deleteChecked();
                    tv_total_price.setText(0 + "元");
                    tatolPrice = "0";
                    bt_pay.setText(String.format("结算(%s)", 0 + ""));
                    bt_delete.setText(String.format("删除(%s)", 0 + ""));

                    bt_delete.setVisibility(View.GONE);
                    bt_pay.setVisibility(View.VISIBLE);
                    uNavigationBar.setRightText("编辑");

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
     * 生成订单
     * @param isShow
     * @param total_money
     * @param real_total_money
     * @param goodsInfo  json
     */
    protected void addOrder(boolean isShow,String total_money,String real_total_money,String goodsInfo) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).addOrder(CacheData.getToken(),total_money,real_total_money,goodsInfo);
        HttpClient.enqueue(call, new ResultDataCallback(getActivity(), isShow) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                   String id   =  JSON.parseObject(bean.json).getString("id");
                   startTo(ConfirmOrderActivity.class,0,id);
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
     * 生成订单
     */
    private String cearteOrder() {
        String json = "";
        if (adapter == null) return json;
        List<CreateOrderBean> beanList = new ArrayList<>();
        List<ShopCartBean> list = adapter.getData();
        for (ShopCartBean shopCartBean : list) {
            for (ShopCartBean.GoodsListBean goodBean : shopCartBean.goodsList) {
                if (goodBean.isChecked){
                    CreateOrderBean bean = new CreateOrderBean();
                    bean.shop_id = shopCartBean.id;
                    bean.shop_name = goodBean.shop_name;
                    bean.goods_id = goodBean.id;
                    bean.goods_spec_id = goodBean.goods_spec_id;
                    bean.goods_name = goodBean.goods_name;
                    bean.goods_num = goodBean.goods_stock;
                    bean.goods_price = goodBean.market_price;
                    bean.goods_pic_url = goodBean.goods_img;
                    beanList.add(bean);
                }
            }
        }
        json = JSON.toJSONString(beanList);
        Log.e(TAG, "cearteOrder: "+json );
        return  json ;
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getCartData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore(false);
//        getCartData(false);
    }

    private String getCheckIds() {
        StringBuffer ids = new StringBuffer("");
        if (adapter == null) return ids.toString();
        List<ShopCartBean> list = adapter.getData();
        for (ShopCartBean shopCartBean : list) {
            for (ShopCartBean.GoodsListBean goodBean : shopCartBean.goodsList) {
                if (goodBean.isChecked)
                ids.append(goodBean.id+",");
            }
        }
        if (ids.toString().endsWith(",")){
            ids.deleteCharAt(ids.lastIndexOf(","));
        }
        Log.e(TAG, "getCheckIds: "+ids.toString());
        return ids.toString();
    }

    private void confirmDeletionDialog() {
        //提示弹窗
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());
//        rxDialogSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
        rxDialogSureCancel.setContent("你确定删除购物车商品吗？");
        rxDialogSureCancel.getSureView().setOnClickListener(v -> {
            rxDialogSureCancel.cancel();
            if (adapter != null) {
                String ids = getCheckIds();
                deleteGoodByCart(true,ids);
            }
        });
        rxDialogSureCancel.getCancelView().setOnClickListener(v -> rxDialogSureCancel.cancel());
        rxDialogSureCancel.show();
    }
    private String tatolPrice = "0";
    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        bt_pay.setOnClickListener(v -> {
           String json =  cearteOrder();
           addOrder(true,tatolPrice,tatolPrice,json);
        });
        bt_delete.setOnClickListener(v -> {
            confirmDeletionDialog();
        });
        adapter.setTotalPriceListener((price, count) -> {
            tv_total_price.setText(price + "元");
            tatolPrice = price+"";
            bt_pay.setText(String.format("结算(%s)", count + ""));
            bt_delete.setText(String.format("删除(%s)", count + ""));
        });
        ll_choosedAll.setOnClickListener(v -> {
            isAllChecked = !isAllChecked;
            if (isAllChecked) {
                is_chooseAll.setBackgroundResource(R.mipmap.ic_checkbox1_red);
                adapter.setAllChecked(isAllChecked);
            } else {
                is_chooseAll.setBackgroundResource(R.mipmap.ic_checkbox1_gary);
                adapter.setAllChecked(isAllChecked);
            }
        });
    }



    @Override
    public void onClick(View view) {
        if (view == bt_pay) {
            confirmDeletionDialog();
        }
    }
}
