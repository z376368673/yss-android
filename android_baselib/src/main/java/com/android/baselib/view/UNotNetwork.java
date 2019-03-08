package com.android.baselib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.android.baselib.R;


/**
 * 无网络
 */
public class UNotNetwork extends LinearLayout {
    private Context context;

    public UNotNetwork(Context context) {
        this(context, null, 0);
        initView(context);
    }

    public UNotNetwork(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }

    public UNotNetwork(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.u_not_network, this);
    }


}
