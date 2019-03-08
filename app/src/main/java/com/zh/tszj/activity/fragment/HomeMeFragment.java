package com.zh.tszj.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.image.UImage;
import com.android.baselib.view.UImageView;
import com.android.baselib.view.UNavigationBar;
import com.zh.tszj.R;
import com.zh.tszj.activity.login.AddUserInfoAct;
import com.zh.tszj.activity.login.LoginMain;
import com.zh.tszj.activity.me.AddressManageActivity;
import com.zh.tszj.activity.me.EditAddressActivity;
import com.zh.tszj.activity.me.MyOrderActivity;
import com.zh.tszj.activity.me.SettingActivity;
import com.zh.tszj.base.BaseFragment;
import com.zh.tszj.config.CacheData;

public class HomeMeFragment extends BaseFragment {
    UNavigationBar uNavigationBar;
    UImageView uImageView;
    TextView tv_name;
    LinearLayout layout_userInfo;
    LinearLayout layout_my_wallet;
    LinearLayout layout_my_order;
    LinearLayout layout_address_manage;
    LinearLayout layout_become_store;
    LinearLayout layout_become_hhr;
    LinearLayout layout_login_out;

    @Override
    protected int onLayoutResID() {
        return R.layout.fm_home_me;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        uNavigationBar.setTitle("个人中心");
        uNavigationBar.setRightImg(R.mipmap.ic_me_setting);
        uNavigationBar.setRightImgOnClick(v -> {
            startTo(SettingActivity.class);
        });
        upUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        upUserInfo();
    }

    private void upUserInfo(){
        if (tv_name!=null&&uImageView!=null){
            String url =  CacheData.getUser().avatar;
            String name =  CacheData.getUser().nikename!=null?CacheData.getUser().nikename:"请点击登陆";
            tv_name.setText(name);
            UImage.getInstance().load(getContext(),url,uImageView);
        }
    }
    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        uImageView.setOnClickListener(this);
        layout_userInfo.setOnClickListener(this);
        layout_my_wallet.setOnClickListener(this);
        layout_my_order.setOnClickListener(this);
        layout_address_manage.setOnClickListener(this);
        layout_become_store.setOnClickListener(this);
        layout_become_hhr.setOnClickListener(this);
        layout_login_out.setOnClickListener(v -> startTo(LoginMain.class));
    }

    @Override
    public void onClick(View view) {
        if (view == uImageView) {
            if (TextUtils.isEmpty(CacheData.getToken())) startTo(LoginMain.class);
            else {
                // startTo(AddUserInfoAct.class);
            }
        }
        if (view == layout_userInfo) {
            if (TextUtils.isEmpty(CacheData.getToken())) startTo(LoginMain.class);
            else {
                Intent intent = new Intent(getContext(),AddUserInfoAct.class);
                getActivity().startActivityFromFragment(this,intent,Activity.RESULT_FIRST_USER);
//                startTo(AddUserInfoAct.class);
            }
        }
        if (view == layout_my_wallet) {
            if (TextUtils.isEmpty(CacheData.getToken())) startTo(LoginMain.class);
            else   startTo(EditAddressActivity.class);
        }
        if (view == layout_my_order) {
            if (TextUtils.isEmpty(CacheData.getToken())) startTo(LoginMain.class);
            else  startTo(MyOrderActivity.class);
        }
        if (view == layout_address_manage) {
            if (TextUtils.isEmpty(CacheData.getToken())) startTo(LoginMain.class);
            else  startTo(AddressManageActivity.class);
        }
        if (view == layout_become_store) {

        }
        if (view == layout_become_hhr) {
//            startTo(AddressManageActivity.class);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
