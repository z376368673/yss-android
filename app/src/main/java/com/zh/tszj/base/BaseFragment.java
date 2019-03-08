package com.zh.tszj.base;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;

import com.android.baselib.ui.UBaseFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zh.tszj.activity.webview.ShopDetailsWebActivity;

public abstract class BaseFragment extends UBaseFragment  implements OnRefreshLoadMoreListener {

    public int curr = 1;
    public int limit = 10;
    private int classId = 0;
    public String TAG ="TAG";

    @Override
    public void onStart() {
        super.onStart();
        TAG = getClass().getSimpleName();
    }

    @Override
    public void handleMessage(Message message) {

    }
    public int getClassId() {
        return classId;
    }

    public void setClassId(int id){
        classId = id;
    }

    public void starToWeb(String id,String value){
        Intent intent = new Intent(getContext(),ShopDetailsWebActivity.class);
        intent.putExtra(ShopDetailsWebActivity.ID,id);
        intent.putExtra(ShopDetailsWebActivity.VALUE,value);
        startActivity(intent);
    }

    /**
     * 加载更多
     * @param refreshLayout
     */
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
       refreshLayout.finishLoadMore(true);
        curr++;
    }
    /**
     * 刷新
     * @param refreshLayout
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
       refreshLayout.finishRefresh(true);
        curr = 1;
    }
}
