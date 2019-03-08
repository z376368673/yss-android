package com.android.baselib.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.baselib.R;

/**
 * 标题栏/导航栏
 */
public class UNavigationBar extends LinearLayout {

    private Context context;

    private View status_bar;


    private LinearLayout btn_left;
    private ImageView btn_left_img;
    private TextView btn_left_text;

    private RelativeLayout title_layout;
    private TextView title_tv;

    private LinearLayout btn_right;
    private ImageView btn_right_img;
    private TextView btn_right_text;

    public UNavigationBar(Context context) {
//        super(context);
        this(context,   null,0);
        initView(context);
    }
    public UNavigationBar(Context context, @Nullable AttributeSet attrs) {
//        super(context,null);
        this(context,   attrs,0);
        initView(context);
    }

    public UNavigationBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private  void initView(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.u_navigations, this);
        status_bar = findViewById(R.id.status_bar);
        btn_left = findViewById(R.id.btn_left);
        btn_left_img = findViewById(R.id.btn_left_img);
        btn_left_text = findViewById(R.id.btn_left_text);

        title_layout = findViewById(R.id.title_layout);
        title_tv = findViewById(R.id.title_tv);

        btn_right = findViewById(R.id.btn_right);
        btn_right_img = findViewById(R.id.btn_right_img);
        btn_right_text = findViewById(R.id.btn_right_text);
    }

    /**
     * 获取右边按钮布局
     * @return
     */
    public View getRightLayout() {
        return btn_right;
    }
    /**
     * 获取右边的文字TextView
     * @return
     */
    public TextView getRightTextView() {
        return btn_right_text;
    }

    /**
     * 获取标题布局
     * @return
     */
    public View getTitleLayout() {
        return title_layout;
    }
    /**
     * 获取左边按钮布局
     * @return
     */
    public View getLeftLayout() {
        return btn_left;
    }

    public void setTitle(String title){
        title_tv.setText(title);
    }

    /**
     * 设置右边文字
     * @param text
     */
    public void setRightText(String text){
        btn_right_text.setText(text);
        btn_right_text.setVisibility(VISIBLE);
        btn_right_img.setVisibility(GONE);
        btn_right.setVisibility(VISIBLE);
    }
    /**
     *设置右边文字点击
     * @param listener
     */
    public void setRightTextOnClick(OnClickListener listener){
        btn_right_text.setOnClickListener(listener);
    }
    /**
     * 设置右边图片
     * @param
     */
    public void setRightImg(int res){
        btn_right_img.setImageResource(res);
        btn_right_img.setVisibility(VISIBLE);
        btn_right_text.setVisibility(GONE);
        btn_right.setVisibility(VISIBLE);
    }
    /**
     *设置右边文字点击
     * @param listener
     */
    public void setRightImgOnClick(OnClickListener listener){
        btn_right_img.setOnClickListener(listener);
    }



    /**
     * 设置左边文字
     * @param text
     */
    public void setLeftText(String text){
        btn_left.setVisibility(VISIBLE);
        btn_left_text.setText(text);
        btn_left_text.setVisibility(VISIBLE);
        btn_left_img.setVisibility(GONE);
    }

    /**
     *设置左边文字点击
     * @param listener
     */
    public void setLeftTextOnClick(OnClickListener listener){
        btn_left_text.setOnClickListener(listener);
    }

    public void setBack(Activity activity){
        btn_left.setVisibility(VISIBLE);
        btn_left_img.setVisibility(VISIBLE);
        btn_left.setOnClickListener(v -> {
            activity.finish();
        });
    }

}
