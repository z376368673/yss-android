package com.zh.tszj.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselib.image.UImage;
import com.android.baselib.ui.adapter.RCSingleAdapter;
import com.android.baselib.ui.adapter.viewholder.RCViewHolder;
import com.android.baselib.view.NoScrollListView;
import com.zh.tszj.R;
import com.zh.tszj.activity.home.StoreHomeActivity;
import com.zh.tszj.bean.ShopCartBean;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderAdapter extends RCSingleAdapter<ShopCartBean, RCViewHolder> {
    private Context context;
    boolean isOrderDetail;

    public ConfirmOrderAdapter(Context context,  boolean isOrderDetail) {
        super(R.layout.item_confirm_order_list, new ArrayList<>());
        this.context = context;
        this.isOrderDetail = isOrderDetail;
    }
    @Override
    protected void convert(RCViewHolder holder, ShopCartBean item) {
        super.convert(holder, item);
        LinearLayout layout_title = holder.getView(R.id.layout_title);
        ImageView iv_pic = holder.getView(R.id.iv_icon);
        TextView tv_title = holder.getView(R.id.tv_title);
        //设置图片 店铺名字
        UImage.getInstance().load(context, item.shop_logo, iv_pic);
        tv_title.setText(item.shop_name);
        //设置子类的商品list
        NoScrollListView listview = holder.getView(R.id.listview);
        ListAdapter adapter = new ListAdapter();
        List<ShopCartBean.GoodsListBean> listBeans = item.goodsList;
        adapter.addAll(listBeans);
        listview.setAdapter(adapter);
        layout_title.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreHomeActivity.class);
            intent.setAction(item.id);
            context.startActivity(intent);
        });
        setPrice();
    }
    private GwcListAdapter.TotalPriceListener totalPriceListener;

    public interface TotalPriceListener {
        void calculationPrice(int price, int count);
    }

    public void setTotalPriceListener(GwcListAdapter.TotalPriceListener totalPriceListener) {
        this.totalPriceListener = totalPriceListener;
    }
    private String setPrice() {
        int tatolPrice = 0;
        int tatolcount = 0;
        List<ShopCartBean> listBean = getData();
        for (ShopCartBean cartBean : listBean) {
            List<ShopCartBean.GoodsListBean> list = cartBean.goodsList;
            for (ShopCartBean.GoodsListBean bean : list) {
                    tatolPrice = tatolPrice + bean.market_price * bean.goods_stock;
                    tatolcount = tatolcount + bean.goods_stock;
                Log.e(TAG, "setPrice: " + tatolPrice);
            }
        }
        if (totalPriceListener!=null)totalPriceListener.calculationPrice(tatolPrice,tatolcount);
        return tatolPrice+"";
    }
    public class ListAdapter extends ArrayAdapter<ShopCartBean.GoodsListBean> {
        public ListAdapter() {
            super(context, 0);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_confirm_order_list_list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ShopCartBean.GoodsListBean bean = getItem(position);
            holder.initView(bean);
            return convertView;
        }
    }


    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_des;
        private TextView tv_price;
        private TextView tv_count;

        public ViewHolder(View view) {
            iv_img = view.findViewById(R.id.iv_img);
            tv_des = view.findViewById(R.id.tv_des);
            tv_price = view.findViewById(R.id.tv_price);
            tv_count = view.findViewById(R.id.tv_count);
        }

        private void initView(ShopCartBean.GoodsListBean bean) {
            tv_des.setText(bean.goods_name);
            tv_price.setText("￥" + bean.market_price);
            UImage.getInstance().load(context, bean.goods_img, iv_img);
            tv_count.setText("x " + bean.goods_stock);
        }
    }
}
