package com.kcj.friendsearch.adapter;

import java.util.List;

import com.kcj.friendsearch.bean.RouteInfo;
import com.xiapu.whereisfriend.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * @ClassName: BusExpandAdapter
 * @Description: 路径公交与公交路线
 * @author kcj
 * @date 
 */
public class BusExpandAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private String[]   mGroupStrings = null;
    private List<List<RouteInfo>>   mData = null;

    public BusExpandAdapter(Context ctx, List<List<RouteInfo>> list) {
        mContext = ctx;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupStrings = new String[list.size()];
        for(int i = 0; i < list.size() ; i++){
        	int num = i+1;
        	mGroupStrings[i] = "路线  " + num +" ";
        }
        mData = list;
    }

    public void setData(List<List<RouteInfo>> list) {
        mData = list;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition).size();
    }

    @Override
    public List<RouteInfo> getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition);
    }

    @Override
    public RouteInfo getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item_layout, null);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.mGroupName = (TextView) convertView
                .findViewById(R.id.group_name);
        holder.mGroupName.setText(mGroupStrings[groupPosition]);
        holder.mGroupCount = (TextView) convertView
                .findViewById(R.id.group_count);
        holder.mGroupCount.setText("[" + mData.get(groupPosition).size() + "]");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item1, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        //holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
        //holder.mIcon.setBackgroundResource(getChild(groupPosition,childPosition).getImageId());
        holder.mChildName = (TextView) convertView.findViewById(R.id.title);
        holder.mChildNum = (TextView) convertView.findViewById(R.id.show_time);
        holder.mChildName.setText(getChild(groupPosition, childPosition).getNode());
        //holder.mChildNum.setText(String.valueOf(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    private class GroupViewHolder {
        TextView mGroupName;
        TextView mGroupCount;
    }

    private class ChildViewHolder {
        TextView mChildName;
        TextView mChildNum;
    }
    
    /**
	 * 清除所有数据
	 */
	public void clear() {
		mData.clear();
	}

}
