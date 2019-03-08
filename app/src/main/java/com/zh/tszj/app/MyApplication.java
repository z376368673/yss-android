package com.zh.tszj.app;

import com.android.baselib.UBaseApplication;
import com.android.baselib.net2.HttpClient;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zh.tszj.R;
import com.zh.tszj.wxapi.Constants;

public class MyApplication extends UBaseApplication {
     private String API_URL = "http://192.168.0.26/";
    // private String API_URL = "http://120.79.217.49:8891";
    @Override
    public void onCreate() {
        super.onCreate();
        initHttp();
        final IWXAPI api = WXAPIFactory.createWXAPI(this, null,false);
        // 将该app注册到微信
        api.registerApp(Constants.APP_ID);
        initView();
    }

    /**
     * 初始化OkHttp配置
     */
    private void initHttp() {
        HttpClient.getIntens(API_URL);
    }
    private void initView(){
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.gray_99, android.R.color.white);//全局设置主题颜色
            MaterialHeader header =  new MaterialHeader(context);
            return header;
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }
}
