package com.zh.tszj.activity.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baselib.view.UNavigationBar;
import com.zh.tszj.R;
import com.zh.tszj.base.BaseActivity;
import com.zh.tszj.bean.ShopClass;
import com.zh.tszj.db.DatabaseUtils;
import com.zh.tszj.view.handygridview.GridViewAdapter;
import com.zh.tszj.view.handygridview.HandyGridView;
import com.zh.tszj.view.handygridview.listener.OnItemCapturedListener;

import java.util.List;

public class ShopClassActivity extends BaseActivity {

    UNavigationBar uNavigationBar;
    HandyGridView handyGridView;
    ImageView iv_close;
    GridViewAdapter<ShopClass> adapter;

    @Override
    protected int onLayoutResID() {
        return R.layout.act_shop_class;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        uNavigationBar.setTitle("菜单排序");
        uNavigationBar.setBack(this);
        uNavigationBar.setRightText("保存");
        uNavigationBar.setRightTextOnClick(v -> {
            saveData();
        });
//        String sql = " parent_id = ? order by cat_sort ";
        String sql = " parent_id = ?  ";
        List<ShopClass> list = DatabaseUtils.getHelper().queryBySql(ShopClass.class, sql, new String[]{"-1"});
        adapter = new GridViewAdapter<ShopClass>(this, list) {
            @Override
            public String getText(ShopClass data) {
                if (data != null) {
                    return data.cat_name;
                }
                return "你是谁？";
            }
        };
        handyGridView.setAdapter(adapter);
        handyGridView.setAutoOptimize(true);
        //当gridview可以滚动并且被拖动的item位于gridview的顶部或者底部时，设置gridview滚屏的速度，
        // 每秒移动的像素点个数，默认750，可不设置。
        handyGridView.setScrollSpeed(750);
        handyGridView.setMode(HandyGridView.MODE.TOUCH);
        handyGridView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (!handyGridView.isTouchMode() && !handyGridView.isNoneMode() && !adapter.isFixed(position)) {//long press enter edit mode.
                handyGridView.setMode(HandyGridView.MODE.TOUCH);
                return true;
            }
            return false;
        });
        handyGridView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(activity, "click item at " + position, Toast.LENGTH_SHORT).show());
        handyGridView.setOnItemCapturedListener(new OnItemCapturedListener() {
            @Override
            public void onItemCaptured(View v, int position) {
                v.setScaleX(1.2f);
                v.setScaleY(1.2f);
            }

            @Override
            public void onItemReleased(View v, int position) {
                v.setScaleX(1f);
                v.setScaleY(1f);
            }
        });
    }

    private void saveData() {
        if (adapter == null) return;
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            ShopClass shopClass = adapter.getItem(i);
            shopClass.cat_sort = i;
            Log.e("saveData", "saveData: " + shopClass.cat_name + shopClass.cat_sort);
            DatabaseUtils.getHelper().save(shopClass);
        }
        setResult(Activity.RESULT_OK);
        finish();
//        List<ShopClass> list = DatabaseUtils.getHelper().queryBySql(ShopClass.class, " parent_id = ? order by cat_sort", new String[]{"-1"});
//        for (int i = 0; i <list.size() ; i++) {
//            ShopClass shopClass = list.get(i);
//            Log.e("saveData", "saveData: " + shopClass.cat_name + shopClass.cat_sort);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void initEvent(@Nullable Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        iv_close.setOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View v) {

    }
}
