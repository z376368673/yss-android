package com.zh.tszj.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.ui.adapter.ListViewItemChickClichListener;
import com.android.baselib.ui.adapter.viewholder.RCViewHolder;
import com.android.baselib.ui.adapter.RCSingleAdapter;
import com.zh.tszj.R;
import com.zh.tszj.bean.AddressBean;

import java.util.ArrayList;

/**
 *  地址管理
 *
 * Author:37636
 * QQ:376368673
 * Time:2018/11/13
 * Description:This is DemoAdapter
 */
public class AddressMangeAdapter extends RCSingleAdapter<AddressBean, RCViewHolder> {
    Activity activity;

    public AddressMangeAdapter(Activity activity) {
        //第一个参数必填 写adapter得 layout 布局id
        super(R.layout.item_address_manage, new ArrayList());
        this.activity = activity;
    }
    private  AddressBean indexBean ;
    public void  selectIndex(AddressBean bean){
         if (indexBean!=null){
             indexBean.is_default = 2;
         }
        bean.is_default = 1;
    }
    @Override
    protected void convert(RCViewHolder helper, AddressBean item) {
       super.convert(helper, item);
        TextView tv_name =  helper.getView(R.id.tv_name);
        TextView tv_phone =  helper.getView(R.id.tv_phone);
        TextView tv_address =  helper.getView(R.id.tv_address);
        ImageView iv_default =  helper.getView(R.id.iv_default);
        TextView tv_default =  helper.getView(R.id.tv_default);

        tv_name.setText(item.user_name);
        tv_phone.setText(item.user_phone);
        tv_address.setText(item.province+" "+item.city+" "+item.county+" "+item.user_address);
        if (item.is_default==1){
            tv_default.setTag(true);
            iv_default.setImageResource(R.mipmap.ic_checkbox1_red);
            indexBean  = item;
        }else {
            tv_default.setTag(false);
            iv_default.setImageResource(R.mipmap.ic_checkbox1_gary);
        }
        tv_default.setOnClickListener(v -> {
            if (itemChickClichListener!=null){
                boolean tag = (boolean) tv_default.getTag();
                if (tag){
                    iv_default.setImageResource(R.mipmap.ic_checkbox1_gary);
                }else {
                    iv_default.setImageResource(R.mipmap.ic_checkbox1_red);
                }
                tv_default.setTag(!tag);
                itemChickClichListener.onClickItemListener(tv_default,item,helper.getAdapterPosition());
            }
        });

        LinearLayout layout_edit =  helper.getView(R.id.layout_edit);
        LinearLayout layout_del =  helper.getView(R.id.layout_del);
        layout_edit.setOnClickListener(v -> {
            if (itemChickClichListener!=null)
                itemChickClichListener.onClickItemListener(layout_edit,item,helper.getAdapterPosition());
        });
        layout_del.setOnClickListener(v -> {
            if (itemChickClichListener!=null)
                itemChickClichListener.onClickItemListener(layout_del,item,helper.getAdapterPosition());
        });
        helper.itemView.setOnClickListener(v -> {
            if (itemChickClichListener!=null)
                itemChickClichListener.onClickItemListener(helper.itemView,item,helper.getAdapterPosition());
        });
    }
}
