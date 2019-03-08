package com.zh.tszj.activity.me;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.baselib.util.UScreenUtil;
import com.android.baselib.util.UToastUtil;
import com.android.baselib.view.UNavigationBar;
import com.zh.tszj.R;
import com.zh.tszj.adapter.OrderFragmentAdapter;
import com.zh.tszj.base.BaseActivity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends BaseActivity {

    UNavigationBar uNavigationBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private OrderFragmentAdapter topMenuAdapter;
    private List<String> names;
    @Override
    protected int onLayoutResID() {
        return R.layout.act_order;
    }
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        uNavigationBar.setTitle("我的订单");
        uNavigationBar.setBack(this);
        topMenuAdapter = new OrderFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(topMenuAdapter);
        tabLayout.setupWithViewPager(viewPager);
        names = new ArrayList<>();
        names.add("全部");
        names.add("待付款");
        names.add("待发货");
        names.add("待收货");
        names.add("待评价");
        topMenuAdapter.setList(names);
        reflex(tabLayout);
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        onEnd("");
        UToastUtil.showToastShort("清除成功");
    }

    /**
     * 关于曰十三
     * @param view
     */
    public void aboutOur(View view){

    }

    @Override
    public void onClick(View v) {

    }


    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(() -> {
            try {
                //拿到tabLayout的mTabStrip属性
                LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                int dp10 = UScreenUtil.dp2px(10);
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);
                    TextView mTextView = (TextView) mTextViewField.get(tabView);
                    tabView.setPadding(0, 0, 0, 0);
                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width ;
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);

                    tabView.invalidate();
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }
}
