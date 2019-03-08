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
import com.chad.library.adapter.base.BaseViewHolder;
import com.zh.tszj.R;
import com.zh.tszj.activity.home.StoreHomeActivity;
import com.zh.tszj.bean.ShopCartBean;

import java.util.ArrayList;
import java.util.List;

public class GwcListAdapter extends RCSingleAdapter<ShopCartBean, RCViewHolder> {
    private Context context;
    boolean isShowChoose;
    boolean isOrderDetail;

    public GwcListAdapter(Context context, boolean isShowChoose, boolean isOrderDetail) {
        super(R.layout.item_gwc_list, new ArrayList<>());
        this.context = context;
        this.isShowChoose = isShowChoose;
        this.isOrderDetail = isOrderDetail;
    }
    @Override
    protected void convert(RCViewHolder holder, ShopCartBean item) {
        super.convert(holder, item);
        LinearLayout layout_title = holder.getView(R.id.layout_title);
        ImageView checkBox = holder.getView(R.id.checkbox);
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
        item.isChecked = getIsCheced(item);
        if (item.isChecked) {
            checkBox.setImageResource(R.mipmap.ic_checkbox1_red);
        } else {
            checkBox.setImageResource(R.mipmap.ic_checkbox1_gary);
        }
        checkBox.setOnClickListener(v -> {
            item.isChecked = !item.isChecked;
            isCheckedAll(item.isChecked, listBeans);
        });
        layout_title.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreHomeActivity.class);
            intent.setAction(item.id);
            context.startActivity(intent);
        });
        setPrice();
    }

    //判断此商铺是否应该选中 如果此店铺的商品都被选中 则 选中
    private boolean getIsCheced(ShopCartBean item) {
        boolean allChecked = false;
        List<ShopCartBean.GoodsListBean> listBeans = item.goodsList;
        for (ShopCartBean.GoodsListBean goodsListBean : listBeans) {
            if (!goodsListBean.isChecked) {
                allChecked = false;
                return allChecked;
            } else {
                allChecked = true;
            }
        }
        return allChecked;
    }

    //选择中商铺的下的所有商品
    private void isCheckedAll(boolean b, List<ShopCartBean.GoodsListBean> listBeans) {
        for (ShopCartBean.GoodsListBean bean : listBeans) {
            bean.isChecked = b;
        }
        notifyDataSetChanged();
    }

    //删除选中的商品
    public void deleteChecked() {
        List<ShopCartBean> shopCartBeanList = getData();
        List<ShopCartBean> list = new ArrayList<>();
        for (int i = 0; i <shopCartBeanList.size() ; i++) {
            ShopCartBean shopCartBean =  shopCartBeanList.get(i);
            if (!shopCartBean.isChecked){
                List<ShopCartBean.GoodsListBean> goodsList = shopCartBean.goodsList;
                List< ShopCartBean.GoodsListBean> listBeans = new ArrayList<>();
                for (int j = 0; j <goodsList.size() ; j++) {
                    ShopCartBean.GoodsListBean goodBean = goodsList.get(j);
                    if (!goodBean.isChecked){
                        listBeans.add(goodBean);
                    }
                }
                shopCartBean.goodsList = listBeans;
                list.add(shopCartBean);
            }
        }
//        List<ShopCartBean> list = new ArrayList<>();
//        list.addAll(shopCartBeanList);
//        for (int i = 0; i <shopCartBeanList.size() ; i++) {
//            ShopCartBean shopCartBean =  shopCartBeanList.get(i);
//            if (!shopCartBean.isChecked){
//                list.add(shopCartBeanList.get(i));
//            }else {
//
//            }
//        }
        clearData();
        addData(list);
        notifyDataSetChanged();
    }


    //设置全选 或者非全选
    public void setAllChecked(boolean isCheck) {
        List<ShopCartBean> listBean = getData();
        for (ShopCartBean cartBean : listBean) {
            cartBean.isChecked = isCheck;
            List<ShopCartBean.GoodsListBean> list = cartBean.goodsList;
            for (ShopCartBean.GoodsListBean bean : list) {
                bean.isChecked = isCheck;
            }
        }
        notifyDataSetChanged();
    }

    private void setPrice() {
        int tatolPrice = 0;
        int tatolcount = 0;
        List<ShopCartBean> listBean = getData();
        for (ShopCartBean cartBean : listBean) {
            List<ShopCartBean.GoodsListBean> list = cartBean.goodsList;
            for (ShopCartBean.GoodsListBean bean : list) {
                if (bean.isChecked) {
                    tatolPrice = tatolPrice + bean.market_price * bean.goods_stock;
                    tatolcount = tatolcount + bean.goods_stock;
                }
                Log.e(TAG, "setPrice: " + tatolPrice);
            }
        }
        totalPriceListener.calculationPrice(tatolPrice, tatolcount);
    }

    private TotalPriceListener totalPriceListener;

    public interface TotalPriceListener {
        void calculationPrice(int price, int count);
    }

    public void setTotalPriceListener(TotalPriceListener totalPriceListener) {
        this.totalPriceListener = totalPriceListener;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_gwc_list_list, null);
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
        private ImageView checkbox;
        private ImageView iv_img;
        private TextView tv_des;
        private TextView tv_price;
        private TextView btn_left;
        private TextView tv_count;
        private TextView btn_right;

        public ViewHolder(View view) {
            checkbox = view.findViewById(R.id.checkbox);
            iv_img = view.findViewById(R.id.iv_img);
            tv_des = view.findViewById(R.id.tv_des);
            tv_price = view.findViewById(R.id.tv_price);
            btn_left = view.findViewById(R.id.btn_left);
            tv_count = view.findViewById(R.id.tv_count);
            btn_right = view.findViewById(R.id.btn_right);
        }

        int num = 1;

        private void initView(ShopCartBean.GoodsListBean bean) {
            num = bean.goods_stock;
            if (bean.isChecked) {
                checkbox.setImageResource(R.mipmap.ic_checkbox1_red);
            } else {
                checkbox.setImageResource(R.mipmap.ic_checkbox1_gary);
            }
            checkbox.setOnClickListener(v -> {
                bean.isChecked = !bean.isChecked;
//                reNotifyDataSetChanged();
                notifyDataSetChanged();
            });
            tv_des.setText(bean.goods_name);
            tv_price.setText("￥" + bean.market_price);
            UImage.getInstance().load(context, bean.goods_img, iv_img);
            btn_left.setOnClickListener(v -> {
                num--;
                if (num < 1)
                    num = 1;
                bean.goods_stock = num;
                tv_count.setText("" + num);
                notifyDataSetChanged();
            });
            btn_right.setOnClickListener(v -> {
                num++;
                if (num > 99)
                    num = 99;
                bean.goods_stock = num;
                tv_count.setText("" + num);
                notifyDataSetChanged();
            });
            tv_count.setText("" + bean.goods_stock);
        }

    }

}
