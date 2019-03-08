package com.zh.tszj.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.baselib.dialog.RxDialogSureCancel;
import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.ui.adapter.ListViewItemChickClichListener;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UNavigationBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zh.tszj.R;
import com.zh.tszj.adapter.AddressMangeAdapter;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.AddressBean;
import com.zh.tszj.config.CacheData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 地址管理
 */
public class AddressManageActivity extends BaseActivity {
    public final static String FLAG_DATA = "FLAG_DATA";
    private UNavigationBar uNavigationBar;
    public RefreshLayout refreshLayout;
    public RecyclerView recyclerView;
    private AddressMangeAdapter adapter;

    //标志 点击选中 则返回上一个界面 并携带数据
    boolean isChoice = false;
    @Override
    protected int onLayoutResID() {
        return R.layout.act_address_manage;
    }

    @Override
    public void onStart(String url) {
        super.onStart(url);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String action = getIntent().getAction();
        if (!TextUtils.isEmpty(action)){
            isChoice = true;
        }
        uNavigationBar.setBack(this);
        uNavigationBar.setTitle("地址管理");
        uNavigationBar.setRightText("添加");
        uNavigationBar.setRightTextOnClick(v ->
                startTo(EditAddressActivity.class, Activity.BIND_ADJUST_WITH_ACTIVITY, EditAddressActivity.ADDRESS_ADD)
        );
        adapter = new AddressMangeAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getListData();
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        adapter.setOnItemClickListener((adapter, view, position) -> {
//            ListViewItemChickClichListener

        });
        adapter.setItemChickClichListener((view, data, p) -> {
            if (view.getId() == R.id.layout_edit) {
//                startTo(EditAddressActivity.class, Activity.BIND_ADJUST_WITH_ACTIVITY, EditAddressActivity.ADDRESS_EDIT);
                Intent intent = new Intent(activity, EditAddressActivity.class);
                intent.setAction(EditAddressActivity.ADDRESS_EDIT);
                Bundle bundle = new Bundle();
                bundle.putSerializable("AddressBean",data);
                intent.putExtras(bundle);
                startActivityForResult(intent, Activity.BIND_ADJUST_WITH_ACTIVITY);
            } else if (view.getId() == R.id.layout_del) {
                deleDialog(data);
            } else if (view.getId() == R.id.tv_default) {
                selUserAddress(data);
            }else {
                if (isChoice){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(FLAG_DATA,data);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void deleDialog(AddressBean addressBean) {
        //提示弹窗
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(this);
//        rxDialogSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
        rxDialogSureCancel.setContent("确认删除此地址？");
        rxDialogSureCancel.getSureView().setOnClickListener(v -> {
            rxDialogSureCancel.cancel();

        });
        rxDialogSureCancel.getCancelView().setOnClickListener(v -> rxDialogSureCancel.cancel());
        rxDialogSureCancel.show();
    }

    /**
     * 获取地址列表
     */
    protected void getListData() {
        Call<ResponseBody> call = HttpClient.getApi(API.class).userAddressData(CacheData.getToken(), curr, limit);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    List list = bean.getListData(AddressBean.class);
                    adapter.clearData();
                    adapter.addData(list);
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
     * 设置默认地址
     */
    protected void selUserAddress(AddressBean beans) {
        Call<ResponseBody> call = HttpClient.getApi(API.class).selUserAddress(CacheData.getToken(), beans.id);
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    UToastUtil.showToastShort("设置默认地址成功");
                    adapter.selectIndex(beans);
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
    public void onClick(View view) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult "+requestCode, "resultCode: "+resultCode);
        if (resultCode != Activity.RESULT_OK)return;
        if (requestCode==Activity.BIND_ADJUST_WITH_ACTIVITY){
            getListData();
        }

    }
}
