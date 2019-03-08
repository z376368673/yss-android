package com.zh.tszj.adapter;

import com.android.baselib.ui.adapter.RCSingleAdapter;
import com.android.baselib.ui.adapter.viewholder.RCViewHolder;

import java.util.ArrayList;

/**
 *  使用实例
 *
 * Author:37636
 * QQ:376368673
 * Time:2018/11/13
 * Description:This is DemoAdapter
 */
public class DemoAdapter extends RCSingleAdapter<Object, RCViewHolder> {

    public DemoAdapter() {
        //第一个参数必填 写adapter得 layout 布局id
        super(0, new ArrayList());
    }
//
    @Override
    protected void convert(RCViewHolder helper, Object item) {
       super.convert(helper, item);
//        TextView  textView =  helper.getView(R.id.tv_msg_count);
//        textView.setText(item.stationName);
    }
}
