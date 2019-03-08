package com.android.baselib.net2;

public interface ResultCallback {
    /**
     * 开始回调
     */
     void onStart();

    /**
     * 成功回调
     * @param json
     * @param error
     */
    void onResult(String json, String url, String error);



    /**
     * 进度回调
     * @param progress
     */
     void onProgress(int progress);

    /**
     * 错误回调
     * @param error
     */
    void onError(String error);

    /**
     * 失败回调
     * @param error
     */
    void onFail(String error);
    /**
     * 回调
     */
     void onFinish();
}