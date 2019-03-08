package com.zh.tszj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baselib.image.UImage;
import com.android.baselib.util.UScreenUtil;
import com.android.baselib.util.UToolsUtils;
import com.zh.tszj.R;
import com.zh.tszj.bean.GoodsInfoBean;

import java.util.ArrayList;
import java.util.List;

public class GwcListItemAdapter extends BaseAdapter {
	private Context context;
	int totalWidth;
	public List<GoodsInfoBean> goods_datas=new ArrayList<>() ;
	int item_position;
	boolean isShowChoose;
	public GwcListItemAdapter(Context context, int position, boolean isShowChoose){
		this.context=context;
		this.item_position=position;
		goods_datas =new ArrayList<>();
		this.totalWidth= UScreenUtil.getScreenWidth();
		this.isShowChoose=isShowChoose;
	}

	public void setData(List<GoodsInfoBean> datas) {
		this.goods_datas = datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return goods_datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View currentView, ViewGroup arg2) {
		HolderView holderView=null;

		if (currentView==null) {
			holderView = new HolderView();
			currentView = LayoutInflater.from(context).inflate(R.layout.cart_list_item, null);
			holderView.content=currentView.findViewById(R.id.content);
			holderView.btn_checkBox= currentView.findViewById(R.id.ll_select_area);
			holderView.checkBox = currentView.findViewById(R.id.checkbox);
			holderView.iv_pic = currentView.findViewById(R.id.img_icon);
			holderView.tv_title = currentView.findViewById(R.id.tv_title);
			holderView.tv_content = currentView.findViewById(R.id.tv_content);
			holderView.tv_price =  currentView.findViewById(R.id.tv_price);
			holderView.ll_count =  currentView.findViewById(R.id.ll_count);
			holderView.button_in =  currentView.findViewById(R.id.button_in);
			holderView.button_out =  currentView.findViewById(R.id.button_out);
			holderView.goods_count =  currentView.findViewById(R.id.goods_count);
			holderView.sum_shop =  currentView.findViewById(R.id.tv_count);
			currentView.setTag(holderView);
		}else {
			holderView=(HolderView) currentView.getTag();
		}

		if(isShowChoose){
			holderView.btn_checkBox.setVisibility(View.VISIBLE);
			holderView.content.setBackgroundColor(context.getResources().getColor(R.color.white));
			holderView.ll_count.setVisibility(View.VISIBLE);
			holderView.goods_count.setVisibility(View.GONE);
		}else{
			holderView.btn_checkBox.setVisibility(View.GONE);
			holderView.content.setBackgroundColor(context.getResources().getColor(R.color.gray));
			holderView.ll_count.setVisibility(View.GONE);
			holderView.goods_count.setVisibility(View.VISIBLE);
		}

		UToolsUtils.setTextMediumStyle(holderView.tv_title,false);
		UToolsUtils.setTextMediumStyle(holderView.tv_content,false);
		UToolsUtils.setTextMediumStyle(holderView.tv_price,true);

		final GoodsInfoBean bean = goods_datas.get(position);
		UImage.getInstance().load(context,bean.getGd_screen_pic(),R.mipmap.iv_default_img,holderView.iv_pic);
		holderView.tv_content.setText(bean.getGd_gg());
		holderView.tv_price.setText("¥ "+bean.getGd_price());
		holderView.sum_shop.setText(bean.getGm_num()+"");
		holderView.goods_count.setText("x"+bean.getGm_num());

		if (bean.isChecked) {
			holderView.checkBox.setImageResource(R.mipmap.ic_checkbox1_gary);
		} else {
			holderView.checkBox.setImageResource(R.mipmap.ic_checkbox1_red);
		}

		holderView.button_in.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count_num=bean.getGm_num();
				bean.setGm_num(++count_num);
//				((ShoppingCartActivity)context).freshBottomData();
//				notifyDataSetChanged();
			}
		});

		holderView.button_out.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int count_num=bean.getGm_num();
				if(count_num==1){
					Toast.makeText(context,"数量不能小于1",Toast.LENGTH_SHORT).show();
					return;
				}
				bean.setGm_num(--count_num);
//				((ShoppingCartActivity) context).freshBottomData();
//				notifyDataSetChanged();
			}
		});


		holderView.btn_checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bean.isChecked=!bean.isChecked;
				goods_datas.get(position).isChecked=bean.isChecked;
//				((ShoppingCartActivity)context).freshStoreChoosedAll(item_position);
			}
		});

		return currentView;
	}

	public void chooseAll(boolean isChoosed){
		for(int i=0;i<goods_datas.size();i++){
			goods_datas.get(i).isChecked=isChoosed;
		}
		notifyDataSetChanged();
	}



	public class HolderView {
		RelativeLayout content;
		LinearLayout btn_checkBox,ll_count,button_in,button_out;
		private ImageView checkBox,iv_pic;
		private TextView tv_title,tv_content,tv_price,sum_shop,goods_count;

	}

}
