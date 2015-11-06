/*
 * $filename: BaseAdapter1.java,v $
 * $Date: 2014-4-27  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package com.kcj.friendsearch.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kcj.friendsearch.bean.Model;
import com.kcj.friendsearch.confi.BaiduMapSearchMessage;
import com.xiapu.whereisfriend.R;




import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: XBaseAdapter
 * @Description: 
 * @author kcj
 * @date 
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class XBaseAdapter extends BaseAdapter {

	private Context context;

	private List<Model> listViewData;

	private int layoutResId;// ListView每个Item的布局文件

	public XBaseAdapter(Context context, int layoutResId, Activity activity) {
		this.context = context;
		this.layoutResId = layoutResId;
		listViewData = new ArrayList<Model>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Model model = listViewData.get(position);
		ViewItemHolder viewItemHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(layoutResId,
					null);
			viewItemHolder = new ViewItemHolder();
			viewItemHolder.tvName = (TextView) convertView
					.findViewById(R.id.txtView_name);
			viewItemHolder.tvOverallRating = (TextView) convertView
					.findViewById(R.id.txtView_distance);
			viewItemHolder.tvAddress = (TextView) convertView
					.findViewById(R.id.txtView_type);
			
			viewItemHolder.tvGoTo = (TextView) convertView
					.findViewById(R.id.txtView_goto);

			convertView.setTag(viewItemHolder);
		} else {
			viewItemHolder = (ViewItemHolder) convertView.getTag();
		}
		viewItemHolder.tvName.setText(model.getName());
		viewItemHolder.tvOverallRating.setText(String.valueOf(model.getDistance()+"(米)"));
		viewItemHolder.tvAddress.setText(model.getAddress());
		viewItemHolder.tvGoTo.setOnClickListener(new ListViewButtonOnClickListener(position));
		return convertView;
	}

	class ViewItemHolder {
		TextView tvName;
		TextView tvOverallRating;
		TextView tvAddress;
		TextView tvGoTo;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listViewData.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null == listViewData) {
			return 0;
		}
		return listViewData.size();
	}

	/**
	 * 添加一条记录
	 * 
	 * @param model
	 */
	public void addModel(Model model) {
		listViewData.add(model);
	}

	/**
	 * 添加一条记录
	 * 
	 * @param model
	 * @param insertHead
	 *            true:插入在头部
	 */
	public void addModel(Model model, boolean insertHead) {
		if (insertHead) {
			listViewData.add(0, model);
		} else {
			listViewData.add(model);
		}
	}

	/**
	 * 获取一条记录
	 * 
	 * @param i
	 * @return
	 */
	public Model getModel(int i) {
		if (i < 0 || i > listViewData.size() - 1) {
			return null;
		}
		return listViewData.get(i);
	}

	/**
	 * 清除所有数据
	 */
	public void clear() {
		listViewData.clear();
	}


	class ListViewButtonOnClickListener implements OnClickListener {
		private int position;// 记录ListView中Button所在的Item的位置
		public ListViewButtonOnClickListener(int position) {
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.txtView_goto:
				//Model model = listViewData.get(position);
				// 设置坐标
				//PoiResultActivity.getInstance().setLatLng(model.getLatLng());
				//PoiResultActivity.getInstance().setName(model.getName());
				//PoiResultActivity.getInstance().showPoiSearch();
				BaiduMapSearchMessage.getInstance().setModel(listViewData.get(position));
				Intent intent = new Intent();
				intent.setAction("action.nearSearchPoiRoute");
				context.sendBroadcast(intent);
				break;
			default:
				break;
			}
		}
	}
}
