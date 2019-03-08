package com.zh.tszj.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.baselib.dialog.RxDialogSureCancel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zh.tszj.R;
import com.zh.tszj.adapter.GwcListAdapter;
import com.zh.tszj.base.BaseFragment;
import com.zh.tszj.config.ViewUtils;

public class SearchFragment extends BaseFragment {
    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    GwcListAdapter adapter;

    @Override
    protected int onLayoutResID() {
        return R.layout.fm_my_order;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ViewUtils.initLinearRV(getContext(), recyclerView,true);
        adapter = new GwcListAdapter(getContext(), false, false);
        recyclerView.setAdapter(adapter);
    }

    private void showDialog(){
        //提示弹窗
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());
//        rxDialogSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
        rxDialogSureCancel.setContent("确认支付100000元");
        rxDialogSureCancel.getSureView().setOnClickListener(v -> rxDialogSureCancel.cancel());
        rxDialogSureCancel.getCancelView().setOnClickListener(v -> rxDialogSureCancel.cancel());
        rxDialogSureCancel.show();
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }
}
