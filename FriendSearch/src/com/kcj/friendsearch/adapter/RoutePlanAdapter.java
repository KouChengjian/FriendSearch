package com.kcj.friendsearch.adapter;

import java.util.List;
import java.util.Map;

import com.xiapu.whereisfriend.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: RoutePlanAdapter
 * @Description: 路径查询 - 驾车  -步行
 * @author kcj
 * @date 
 */
public class RoutePlanAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater inflater;

	public RoutePlanAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.listview_item1, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String titleStr = list.get(position).get("title").toString();
		viewHolder.title.setText(titleStr);
		return convertView;
	}

	static class ViewHolder {
		public TextView year;
		public TextView month;
		public TextView title;
	}
	
	/**
	 * 清除所有数据
	 */
	public void clear() {
		list.clear();
	}
}
