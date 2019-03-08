package com.android.baselib.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 正方形ImageView
 *
 * @author PF-NAN
 * @date 2018/11/8
 */
public class USquareImageView extends AppCompatImageView {

    public USquareImageView(Context context) {
        super(context);
    }

    public USquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public USquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
