package com.android.baselib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.android.baselib.R;
import com.android.baselib.util.UScreenUtil;

/**
 * 横向进度条
 *
 * @author PF-NAN
 * @date 2018/11/28
 */
public class UProgressView extends View {
    private Paint mPaint = new Paint();

    /*背景色*/
    private int backgroundColor = 0xFFE0E0E0;
    /*进度色*/
    private int progressColor = 0xFF999999;
    /*原点颜色*/
    private int dotColor = 0xFF999999;
    /*线条高度*/
    private int lineHeight = UScreenUtil.dp2px(2);
    /*圆点尺寸*/
    private int dotSize = UScreenUtil.dp2px(4);
    /*进度*/
    private int progress;
    /*最大进度*/
    private int maxProgress = 100;
    /*左边开始位置*/
    private int leftX;
    /*右边结束位置*/
    private int rightX;
    /*距离顶部间隙*/
    private int topY;

    public UProgressView(Context context) {
        this(context, null);
    }

    public UProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        initBaseData();
    }

    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UProgressView, defStyleAttr, 0);
        backgroundColor = typedArray.hasValue(R.styleable.UProgressView_backgroundColor) ? typedArray.getColor(R.styleable.UProgressView_backgroundColor, backgroundColor) : backgroundColor;
        progressColor = typedArray.hasValue(R.styleable.UProgressView_progressColor) ? typedArray.getColor(R.styleable.UProgressView_progressColor, progressColor) : progressColor;
        dotColor = typedArray.hasValue(R.styleable.UProgressView_dotColor) ? typedArray.getColor(R.styleable.UProgressView_dotColor, dotColor) : dotColor;
        lineHeight = typedArray.hasValue(R.styleable.UProgressView_lineHeight) ? typedArray.getDimensionPixelSize(R.styleable.UProgressView_lineHeight, lineHeight) : lineHeight;
        dotSize = typedArray.hasValue(R.styleable.UProgressView_dotSize) ? typedArray.getDimensionPixelSize(R.styleable.UProgressView_dotSize, dotSize) : dotSize;
        typedArray.recycle();
    }


    private void initBaseData() {
        progress = 0;
        leftX = 0;
        topY = dotSize;
    }

    public void setColor(@ColorRes int backgroundColor, @ColorRes int progressColor, @ColorRes int dotColor) {
        this.backgroundColor = getResources().getColor(backgroundColor);
        this.progressColor = getResources().getColor(progressColor);
        this.dotColor = getResources().getColor(dotColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rightX = getWidth();
        mPaint.setAntiAlias(true);//消除锯齿
        /*第一条线*/
        mPaint.setColor(backgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(lineHeight);
        canvas.drawLine(leftX, topY, rightX, topY, mPaint);
        /*第二条线*/
        rightX = rightX / maxProgress * progress;
        if (progress == maxProgress) rightX = getWidth() - 8;
        mPaint.setColor(progressColor);
        canvas.drawLine(leftX, topY, rightX, topY, mPaint);
        /*圆点*/
        mPaint.setColor(dotColor);
        mPaint.setStyle(Paint.Style.FILL);//描边
        mPaint.setStrokeWidth(dotSize);
        if (rightX != 0) {
            canvas.drawCircle(rightX, topY, 8, mPaint);
        } else {
            canvas.drawCircle(8, topY, 8, mPaint);
        }
    }

    /**
     * 设置进度百分比
     *
     * @param progress 0-maxProgress
     */

    public void setProgress(int progress) {
        if (progress >= maxProgress) {
            this.progress = maxProgress;
        } else if (progress <= 0) {
            this.progress = 0;
        } else {
            this.progress = progress;
        }
        invalidate();
    }

    /**
     * 设置最大值
     *
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
