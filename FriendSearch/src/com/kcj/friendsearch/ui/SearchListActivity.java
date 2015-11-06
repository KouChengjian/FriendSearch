package com.kcj.friendsearch.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.kcj.friendsearch.adapter.BusExpandAdapter;
import com.kcj.friendsearch.adapter.RoutePlanAdapter;
import com.kcj.friendsearch.bean.RouteInfo;
import com.xiapu.whereisfriend.R;

/**
 * @ClassName: SearchListActivity
 * @Description: ���������б�-��������ʷ
 * @author kcj
 * @date 
 */
public class SearchListActivity extends BaseActivity implements OnChildClickListener{
	
	String bushead;
	//private List<BusItem>  busList = null;
	private ExpandableListView mListView = null;
	private BusExpandAdapter mAdapter = null;
	 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchlist);
		judgeView();
		initView();
	}
	
	boolean busSearch = false;
	boolean busRoute = false;
	boolean carRoute = false;
	boolean walkRoute = false;
	
	public void judgeView(){
		bushead = getIntent().getStringExtra("busheadline");
		if(TextUtils.isEmpty(bushead)){
			bushead = "δ��������������";
			return;
		}
		if(bushead.equals("����")){
			busSearch = false;
			busRoute = true;
			carRoute = false;
			walkRoute = false;
			//initBusRoutePlan();
		}else if(bushead.equals("�ݳ�") || bushead.equals("����")){
			if(bushead.equals("�ݳ�")){
				busSearch = false;
				busRoute = false;
				carRoute = true;
				walkRoute = false;
			}else{
				busSearch = false;
				busRoute = false;
				carRoute = false;
				walkRoute = true;
			}
			initRoutePlan();
		}else {
			busSearch = true;
			busRoute = false;
			carRoute = false;
			walkRoute = false;
			//initListView();
		}
	}
	
	private ListView listView;
	private RoutePlanAdapter routePlanAdapter;
	
	public void initView(){
		initTopBarForLeft(bushead);
	}
	
	
	/**
	 *·���滮 - ������ݳ�
	 */
	public void initRoutePlan(){
		listView = (ListView) this.findViewById(R.id.listview);
		listView.setDividerHeight(0);
		routePlanAdapter = new RoutePlanAdapter(this, getData());
		listView.setAdapter(routePlanAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(carRoute){
					MainActivity.getInstance().setchildPosition(position);
					// ���͹㲥
					Intent intent = new Intent();
					intent.setAction("action.refreshMapLocation");
					sendBroadcast(intent);
					finish();
				}else if(walkRoute){
					MainActivity.getInstance().setchildPosition(position);
					// ���͹㲥
					Intent intent = new Intent();
					intent.setAction("action.refreshMapLocation");
					sendBroadcast(intent);
					finish();
				}
			}
		});
	}
	/**
	 * ���й�������
	 
	public void initListView(){
		initData();
		mListView = (ExpandableListView)findViewById(R.id.listView1);
		mListView.setVisibility(View.VISIBLE);
		mListView.setGroupIndicator(getResources().getDrawable(
                R.drawable.clicklist_expand));
		mAdapter = new BusExpandAdapter(this, mData);
		mListView.setAdapter(mAdapter);
	    mListView.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
	    mListView.setOnChildClickListener(this);
	}*/
	/**
	 *
	private BusRouteAdapter busRouteAdapter = null;
	  ·���滮 -����
	 
	public void initBusRoutePlan(){
		initBusRouteData();
		mListView = (ExpandableListView)findViewById(R.id.listView1);
		mListView.setVisibility(View.VISIBLE);
		mListView.setGroupIndicator(getResources().getDrawable( R.drawable.clicklist_expand));
		busRouteAdapter = new BusRouteAdapter(this, busRouteList);
		mListView.setAdapter(busRouteAdapter);
	    mListView.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
	    mListView.setOnChildClickListener(this);
	}*/
	/**  
	public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
		if(busSearch){
			BusItem item = mAdapter.getChild(groupPosition, childPosition);
			MainActivity.getInstance().setBusLineResult(MainActivity.getInstance().getBusRouteList().get(groupPosition).getBusLineResult());
			MainActivity.getInstance().setPitchOnTitle(item.getName());
			MainActivity.getInstance().setgroupPosition(groupPosition);
			MainActivity.getInstance().setchildPosition(childPosition);;
			// ���͹㲥
			Intent intent = new Intent();
			intent.setAction("action.refreshMapLocation");
			sendBroadcast(intent);
			finish();
		}
		else if(busRoute){
			MainActivity.getInstance().setgroupPosition(groupPosition);
			MainActivity.getInstance().setchildPosition(childPosition);
			// ���͹㲥
			Intent intent = new Intent();
			intent.setAction("action.refreshMapLocation");
			sendBroadcast(intent);
			finish();
		}
        return true;
	}
	*/
	/**
	 *  ���й������ݶ�ȡ
	 
	private List<List<BusItem>> mData = new ArrayList<List<BusItem>>();
	private void initData() {
        for (int i = 0; i < MainActivity.getInstance().getBusRouteList().size(); i++) {
            List<BusItem> list = new ArrayList<BusItem>();
            for (int j = 0; j <  MainActivity.getInstance().getBusRouteList().get(i).getBusRouteNum(); j++) {
                BusItem item = new BusItem(R.drawable.busitem, MainActivity.getInstance().getBusRouteList().get(i).getBusRouteName()[j]);
                list.add(item);
            }   
            mData.add(list);
        }
    }
	*/
	/**
	 *  ���й������ݶ�ȡ
	 
	private List<List<BusItem>> mData = new ArrayList<List<BusItem>>();
	private void initData() {
        for (int i = 0; i < 2; i++) {
            List<BusItem> list = new ArrayList<BusItem>();
            for (int j = 0; j <  MainActivity.getInstance().getBusRouteList().get(i).getBusRouteNum(); j++) {
                BusItem item = new BusItem(R.drawable.busitem, MainActivity.getInstance().getBusRouteList().get(i).getBusRouteName()[j]);
                list.add(item);
            }   
            mData.add(list);
        }
    }*/
	
	/**·���滮�����������ݶ�ȡ
	 *  ·��
	 */
	private List<List<RouteInfo>> busRouteList = new ArrayList<List<RouteInfo>>();
	private void initBusRouteData() {
		for(int i = 0 ;i< MainActivity.getInstance().getRouteList().size();i++){
			List<RouteInfo> list = new ArrayList<RouteInfo>();
			for(int j =0 ;j< MainActivity.getInstance().getRouteList().get(i).getRouteInfo().length;j++){
				RouteInfo item = new RouteInfo(MainActivity.getInstance().getRouteList().get(i).getRouteInfo()[j]);
				list.add(item);
			}
			busRouteList.add(list);
		}
    }
	
	/**
	 *  ·���滮-������ݳ����ݶ�ȡ
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map ;
		for(int i = 0 ;i< MainActivity.getInstance().getRouteList().size();i++){
			for(int j =0 ;j< MainActivity.getInstance().getRouteList().get(i).getRouteInfo().length;j++){
				map = new HashMap<String, Object>();
			    map.put("title", MainActivity.getInstance().getRouteList().get(i).getRouteInfo()[j]);
				list.add(map);
			}
		}
		return list;
	}


	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		return false;
	}
}
