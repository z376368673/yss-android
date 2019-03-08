package com.zh.tszj.activity.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.android.baselib.util.UToastUtil;
import com.just.agentweb.AgentWeb;
import com.zh.tszj.R;
import com.zh.tszj.activity.login.LoginMain;
import com.zh.tszj.config.CacheData;
import com.zh.tszj.popwindow.CustomPopWindow;
import com.zh.tszj.wxapi.WXShareUtils;


public class ShopDetailsWebActivity extends Activity {
    private String TAG = "";
    private String id = "";
    private String status = "";
    private String URL = "http://192.168.0.27:8080/goods?id=%s&status=%s";
    private String url = "";
    public final static String VALUE = "values";
    public final static String ID = "shopId";
    private LinearLayout layout;
    AgentWeb mAgentWeb;
    Activity context;
    WXShareUtils shareUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TAG = getLocalClassName();
        setContentView(R.layout.act_shop_webview);
        context  = this;
        id = getIntent().getStringExtra(ID);
        status = getIntent().getStringExtra(VALUE);
        url = String.format(URL, id, status);
        initView();
        shareUtils = new WXShareUtils(this);
    }

    public String getURL() {
        return url;
    }

    private void initView() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(getURL());
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(mAgentWeb, this));
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.e(TAG, "onProgressChanged: " + url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.e(TAG, "onLoadResource: " + url);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Log.e(TAG, "shouldInterceptRequest: " + request);
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.post(() -> {
                String token = CacheData.getToken();
                mAgentWeb.getJsAccessEntrace().quickCallJs("getInfo", token);
            });
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
            Log.e(TAG, "onProgressChanged: ");
        }
    };

    /**
     * JS调用java的类
     */
    private class AndroidInterface {
        public AndroidInterface(AgentWeb mAgentWeb, ShopDetailsWebActivity shopDetailsWebActivity) {
        }

        @JavascriptInterface
        public void getAddress(String id) {

        }

        @JavascriptInterface
        public void backAction(String url) {
//            UToastUtil.showToastShort("backAction");
            finish();
        }

        @JavascriptInterface
        public void share(String title,String iconUrl) {
            shareUtils.title = title;
            shareUtils.shareIcon = iconUrl;
            String url = String.format(URL, id, 2);
            shareUtils.webpageUrl = url;
            shareUtils.WXshare();
            UToastUtil.showToastShort(title + "分享");
        }

        @JavascriptInterface
        public void jumpLogin (String url) {
//            UToastUtil.showToastShort(url + "去登陆");
            Intent intent = new Intent(context,LoginMain.class);
            context.startActivityForResult(intent,1011);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=Activity.RESULT_OK)return;
        if (requestCode==1011){
            initView();
        }
    }

    @Override
    protected void onPause() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
        if (mAgentWeb.back()){
            mAgentWeb.back();
        }else {
            super.onBackPressed();
        }
    }


}
