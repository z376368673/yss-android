package com.android.baselib.net2;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HttpClient {
    public static HttpClient httpClient;
    private static String BASE_URL;

    /**
     * @param url
     */
    private HttpClient(String url) {
        BASE_URL = url;
    }

    /**
     * @param url
     * @return
     */
    public static HttpClient getIntens(String url) {
        if (httpClient == null) {
            httpClient = new HttpClient(url);
        }
        return httpClient;
    }

    /**
     * @param cla
     * @param <T>
     * @return
     */
    public static <T> T getApi(Class<T> cla) {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .build();
        return retrofit.create(cla);
    }


    /**
     * 发送请求
     *
     * @param responseBodyCall
     * @param jsonCallback
     */
    public static void enqueue(Call<ResponseBody> responseBodyCall, final ResultCallback jsonCallback) {
        jsonCallback.onStart();
        try {
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    HttpClient.onResponse(response, jsonCallback);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    HttpClient.onFailure(call, throwable, jsonCallback);
                }
            });
        } catch (Exception e) {
            jsonCallback.onError(e.getMessage());
        }
    }

    /**
     * 成功回调
     *
     * @param response
     * @param jsonCallback
     */
    private static void onResponse(Response<ResponseBody> response, ResultCallback jsonCallback) {
        try {
            if (response.body() == null) {
                jsonCallback.onError("获取数据为空");
                Log.e("onResponseUrl", response.raw().toString() + "\n获取数据为空");
            } else {
                String data = response.body().string();
                data = TextUtils.isEmpty(data)?"返回数据为空":data;
                Log.e("onResponseUrl", response.raw().toString() + "\n" + data);
                jsonCallback.onResult(data, response.raw().toString(), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage() == null ? response.raw().toString() + "\n数据解析失败" : response.raw().toString() + "\n" + e.getMessage();
            jsonCallback.onError(msg);
        } finally {
            jsonCallback.onFinish();
        }
    }

    /**
     * 失败回调
     *
     * @param call
     * @param throwable
     * @param jsonCallback
     */
    private static void onFailure(Call<ResponseBody> call, Throwable throwable, ResultCallback jsonCallback) {
        try {
            String msg = throwable.getMessage();
            //String msg = throwable.getMessage() == null ? "获取数据失败" : throwable.getMessage();
            HttpUrl httpUrl = call.request().url();
            Log.e("error", msg + "---" + httpUrl.encodedPath() + httpUrl.encodedUsername() + httpUrl.encodedQuery());
            jsonCallback.onFail(msg);
        } catch (Exception e) {
            jsonCallback.onError(e.getMessage());
        } finally {
            jsonCallback.onFinish();
        }
    }
}
