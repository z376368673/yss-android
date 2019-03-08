package com.zh.tszj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.android.baselib.net2.HttpClient;
import com.android.baselib.net2.ResultBean;
import com.android.baselib.net2.ResultDataCallback;
import com.android.baselib.util.UToastUtil;
import com.zh.tszj.R;
import com.zh.tszj.activity.login.LoginMain;
import com.zh.tszj.api.API;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.AdvertBean;
import com.zh.tszj.bean.HomeStoreBean;
import com.zh.tszj.bean.NoticeBean;
import com.zh.tszj.bean.ShopClass;
import com.zh.tszj.config.CacheConfig;
import com.zh.tszj.db.DatabaseUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class WelcomeActivity extends BaseActivity {

    private ImageView iv_welcome;

    @Override
    protected int onLayoutResID() {
        return R.layout.act_welcome;
    }

    @Override
    public void onStart(String url) {
        super.onStart(url);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        DatabaseUtils.initHelper(this,DatabaseUtils.DATA);
        handerMsg();
        goodsCatsData();
        carouselData();
        noticeData();
        indexShops();
    }

    private void handerMsg() {
        mHandler.sendEmptyMessageDelayed(1, 1500);
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (CacheConfig.isFirst()) {
            toMain();
        } else {
            toGuide();
        }
    }

    /**
     * 获取商品一级分类
     */
    protected void goodsCatsData() {
        Call<ResponseBody> call = HttpClient.getApi(API.class).goodsCatsData("-1");
        HttpClient.enqueue(call, new ResultDataCallback(this, false) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                   List<ShopClass> beans =  bean.getListData(ShopClass.class);
                    beans.add(0,new ShopClass(-1,"推荐",1));
                    DatabaseUtils.getHelper().saveAll(beans,false);
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
     *
     * 首页分类的四个商家
     */
    protected void indexShops() {
        Call<ResponseBody> call = HttpClient.getApi(API.class).indexShops();
        HttpClient.enqueue(call, new ResultDataCallback(this, false) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    List list = bean.getListData(HomeStoreBean.class);
                    DatabaseUtils.getHelper().saveAll(list);
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
     * 获取轮播图信息
     */
    protected void carouselData() {
        Call<ResponseBody> call = HttpClient.getApi(API.class).carouselData("-1");
        HttpClient.enqueue(call, new ResultDataCallback(this, false) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    DatabaseUtils.getHelper().saveAll(bean.getListData(AdvertBean.class));
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
     * 获取通知信息list
     */
    protected void noticeData() {
        Call<ResponseBody> call = HttpClient.getApi(API.class).noticeData();
        HttpClient.enqueue(call, new ResultDataCallback(this, false) {
            @Override
            public void onResult(ResultBean bean, String error) {
                if (bean.state == 1) {
                    DatabaseUtils.getHelper().saveAll(bean.getListData(NoticeBean.class));
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

    // 跳转的登录界面
    private void toLogin() {
        startActivity(new Intent(activity, LoginMain.class));
        finish();
    }

    // 跳转的登录界面
    private void toMain() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    // 跳转的引导页
    private void toGuide() {
        startActivity(new Intent(activity, GuideActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {

    }
}
