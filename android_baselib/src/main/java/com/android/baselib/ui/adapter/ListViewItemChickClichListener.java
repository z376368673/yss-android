package com.android.baselib.ui.adapter;

import android.view.View;

public interface ListViewItemChickClichListener<T extends Object> {
        void onClickItemListener(View view, T data, int p);
}
