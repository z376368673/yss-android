package com.android.baselib.ui.adapter.view;


import com.android.baselib.R;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * 默认列表底部控件
 *
 * @author PF-NAN
 * @date 2018/3/12
 */

public class RCLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.u_view_list_footer;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.rl_vlf_loading;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.rl_vlf_failure;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.rl_vlf_nodata;
    }
}
