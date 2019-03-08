package com.zh.tszj.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.view.UNavigationBar;
import com.zh.tszj.R;
import com.zh.tszj.activity.me.AddressManageActivity;
import com.zh.tszj.adapter.ConfirmOrderAdapter;
import com.zh.tszj.adapter.GwcListAdapter;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.AddressBean;
import com.zh.tszj.bean.OrderInfo;
import com.zh.tszj.bean.ShopCartBean;
import com.zh.tszj.config.CacheData;
import com.zh.tszj.config.ViewUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ConfirmOrderActivity extends BaseActivity {

    UNavigationBar uNavigationBar;
    LinearLayout layout_address;
    RecyclerView recyclerView;
    TextView tv_name;
    TextView tv_phone;
    TextView tv_address;

    ImageView layout_edit;
    ImageView iv_address;
    TextView tv_total_price;
    TextView bt_submit;

    ConfirmOrderAdapter adapter;

    String id = "3E4022C00130470F8B5C056E6D3E75F0";
    @Override
    protected int onLayoutResID() {
        return R.layout.act_confirm_order;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        uNavigationBar.setTitle("确认订单");
        uNavigationBar.setBack(this);
       // id = getIntent().getAction();
        ViewUtils.initLinearRV(this, recyclerView, true);
        adapter = new ConfirmOrderAdapter(this, false);
        recyclerView.setAdapter(adapter);
        orderData();
    }
    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        layout_address.setOnClickListener(v->{
            //action 随便写 只要有action 就能选择地址
            startTo(AddressManageActivity.class,1001,"action");
        });
    }

    /**
     * 获取订单信息
     */
    private void orderData() {
        Call<ResponseBody> call = HttpClient.getApi(API.class).orderData(CacheData.getToken(),id);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                Log.e("ResultBean", "onResult: "+bean.json);
                List<OrderInfo> list = bean.getListData(OrderInfo.class);
//                adapter.addAll(list);
            }
            @Override
            public void onFail(String error) {
                super.onFail(error);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=Activity.RESULT_OK)return;
        if (requestCode==1001){
           AddressBean bean = (AddressBean)data.getSerializableExtra(AddressManageActivity.FLAG_DATA);
           if (bean==null)return;
            tv_name.setText(bean.user_name);
            tv_phone.setText(bean.user_phone);
            String addre = bean.province+bean.city+bean.county+bean.user_address;
            tv_address.setText(addre);
        }
    }
}
