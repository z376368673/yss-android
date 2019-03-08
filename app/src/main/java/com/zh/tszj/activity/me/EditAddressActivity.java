package com.zh.tszj.activity.me;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UNavigationBar;
import com.android.baselib.view.checkAddress.AddrePickerView;
import com.zh.tszj.R;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.AddressBean;
import com.zh.tszj.config.CacheData;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class EditAddressActivity extends BaseActivity {

    public static String ADDRESS_ADD = "add";
    public static String ADDRESS_EDIT = "edit";

    UNavigationBar uNavigationBar;
    String action = "add"; // add ,edit;

    EditText tv_name;
    EditText tv_phone;
    TextView tv_address;
    EditText tv_address_des;
    CheckBox checkbox;
    AddressBean addressBean;
    AddrePickerView dataUtil;
    @Override
    protected int onLayoutResID() {
        return R.layout.act_edit_address;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initAddressData();
        action = getIntent().getAction();
        if (action != null && "edit".equals(action)) {
            uNavigationBar.setTitle("修改收货地址");
            addressBean = (AddressBean) getIntent().getSerializableExtra("AddressBean");
            action = "edit";
            setData(addressBean);
        } else {
            uNavigationBar.setTitle("添加收货地址");
            action = "add";
        }
        uNavigationBar.setBack(this);
        uNavigationBar.setRightText("保存");
        uNavigationBar.setRightTextOnClick(v -> {
            if (action.equals("edit"))
                addUserAddress(true);
            else addUserAddress(false);
        });

    }
    private void initAddressData(){
        new Thread(() -> {
            dataUtil = new AddrePickerView();
            dataUtil.initJsonData(this);
        }).start();
    }
    private void setData(AddressBean data) {
        if (data == null) return;
        tv_name.setText(data.user_name);
        tv_phone.setText(data.user_phone);
        String address = data.province + "," + data.city + "," + data.county;
        tv_address.setText(address);
        tv_address_des.setText(data.user_address);
        if (data.is_default == 1) {
            checkbox.setChecked(true);
        } else {
            checkbox.setChecked(false);
        }
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        tv_address.setOnClickListener(this);
    }

    /**
     * 根据类型获取商品
     */
    protected void addUserAddress(boolean isEdit) {
        String name = tv_name.getText().toString();
        String phone = tv_phone.getText().toString();
        String address = tv_address.getText().toString();
        String address_des = tv_address_des.getText().toString();
        String defaule = checkbox.isChecked() ? "1" : "2";
        if (TextUtils.isEmpty(name.trim())) {
            UToastUtil.showToastShort("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(phone.trim()) || phone.length() != 11) {
            UToastUtil.showToastShort("请输入11位手机号");
            return;
        }
        String[] addresss;
        if (TextUtils.isEmpty(address.trim())) {
            UToastUtil.showToastShort("请输入选择省市区地址");
            return;
        }
        addresss = address.split(" ");
        if (TextUtils.isEmpty(address_des.trim())) {
            UToastUtil.showToastShort("请输入详细地址");
            return;
        }
        String token = CacheData.getToken();
        Call<ResponseBody> call;
        if (isEdit) {
            if (addressBean == null) return;
            call = HttpClient.getApi(API.class).updateUserAddress(token.trim(), addressBean.id, name, phone, addresss[0], addresss[1], addresss[2], address_des, defaule);
        } else {
            call = HttpClient.getApi(API.class).addUserAddress(token.trim(), name, phone, addresss[0], addresss[1], addresss[2], address_des, defaule);
        }
        HttpClient.enqueue(call, new ResultDataCallback(this, true) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    String str = action.equals("edit") ? "编辑" : "添加";
                    UToastUtil.showToastShort(str + "地址成功");
                    setResult(Activity.RESULT_OK);
                    finish();
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
    public void onClick(View v) {
        if (v == tv_address) {
            if (dataUtil!=null){
                dataUtil.showPickerView();
                dataUtil.setPickerViewItemListener(address -> {
                    tv_address.setText(address);
                });
            }
        }
    }

}
