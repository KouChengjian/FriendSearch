package com.kcj.friendsearch.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.bean.BusRoute;
import com.kcj.friendsearch.bean.RouteInfo;
import com.kcj.friendsearch.bean.User;
import com.kcj.friendsearch.ui.FragmentBase;
import com.kcj.friendsearch.ui.MainActivity;
import com.kcj.friendsearch.ui.PoiResultActivity;
import com.kcj.friendsearch.ui.SearchListActivity;
import com.kcj.friendsearch.view.ComposerLayout;
import com.kcj.friendsearch.view.HeaderLayout.onRightImageButtonClickListener;
import com.xiapu.whereisfriend.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class LocationFargment extends FragmentBase implements OnClickListener ,
OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener ,OnGetGeoCoderResultListener ,OnGetRoutePlanResultListener{
	// 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// UI相关
	//private OnCheckedChangeListener radioButtonListener;
	// 定位
	@SuppressWarnings("unused")
	private LinearLayout requestLocLayout;
	private ImageView requestLocImgview;
	boolean isFirstLoc = true;// 是否首次定位
	// 缩放
	private ImageView  imgBig , imgSmall;
	// 缩放级别  3~19
	private int  zoomLevel = 17;
	// 地图模式
	private LinearLayout mapShow;
	private LinearLayout btnShow;
	private ImageView imgViewMapMode , 
	imgViewMapModePlain , 
	imgViewMapModeSatellite ;
	// 路况
	private boolean bLoad = false;
	private LinearLayout loadLayout;
	private ImageView imgViewLoad;
	// 隐藏标题
	private LinearLayout hideTitle;
	// 显示路径
	private LinearLayout ll_luxian;
	private ImageView bus;
	private ImageView car;
	private ImageView walk;
	private ImageView back;
	private Button search;
	@SuppressWarnings("unused")
	private ImageView exchange;
	private EditText et_start;
	private EditText et_end;
	// 公交显示搜索
	private LinearLayout ll_search;
	private ImageView img_search;
	private EditText et_search;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_location, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// 标题
		initView();
		// 初始化控�?
		initFindViewListen();
		// 初始化地�?
		initMap();
		// 圆形菜单
		initMenu();
	}
	
	/** 公交搜索 */
	private List<String> busLineIDList = null;
	private int busLineIndex = 0;
	private PoiSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使�?
	private BusLineSearch mBusLineSearch = null;
	/** 路径规划 */ 
	private RoutePlanSearch routePlanSearch = null;
	/** 反编�?*/
	private GeoCoder geoReverseCoder = null; 
	
	/**
	 * 初始化地�?
	 */
	private void initMap() {
		// 地图初始�?
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// �?��定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始�?
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// 地图图层
		initCoverage("17");
		// 隐藏百度地图自带的放大缩�?和其�?
		hideZoomControls();
		
		/* 初始化公交搜�?*/
		mSearch = PoiSearch.newInstance();
		mSearch.setOnGetPoiSearchResultListener(this);
		mBusLineSearch = BusLineSearch.newInstance();
		mBusLineSearch.setOnGetBusLineSearchResultListener(this);
		busLineIDList = new ArrayList<String>();
		/* 路径规划 */
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(this);
		/* 反编�? 主要获取城市*/
		geoReverseCoder = GeoCoder.newInstance();
		geoReverseCoder.setOnGetGeoCodeResultListener(this);
	}
	
	/**
	 *  隐藏地图的缩�?和logo
	 */ 
	public void hideZoomControls(){
		// 隐藏缩放控件
        int childCount = mMapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
                View child = mMapView.getChildAt(i);
                if (child instanceof ZoomControls) {
                        zoom = child;
                        break;
                }
        }
        zoom.setVisibility(View.GONE);
        // 删除百度地图logo
        mMapView.removeViewAt(1);
	}
	
	/**
	 *  地图缩放的级�?
	 */
	public void initCoverage(String string){
		float zoomLevel = Float.parseFloat(string);
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(zoomLevel);
		mBaiduMap.animateMapStatus(u);
	}
	
	/**
	 *  反编译坐�?
	 */
	public void reverseGeoCoder(){
		LatLng ptCenter = new LatLng(latitude,longtitude);
		// 反Geo搜索
		geoReverseCoder.reverseGeoCode(new ReverseGeoCodeOption()
				.location(ptCenter));
	}
	
	double latitude = 0;
	double longtitude = 0;
	
	/**
	 * 定位SDK监听函数   定位经纬�?
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view �?��后不在处理新接收的位�?
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置�?��者获取到的方向信息，顺时�?-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			latitude = location.getLatitude();
			longtitude = location.getLongitude();
			CustomApplcation.getInstance().setLatitude(String.valueOf(latitude));
			CustomApplcation.getInstance().setLongtitude(String.valueOf(longtitude));
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		// 初始化广�?用于接收信息
		initBroadcastReceiver();
		// 地理反编译坐�? 延迟1s
		mHandler.sendEmptyMessageDelayed(REVERSE_GEO_CODER, 3000);
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// �?��时销毁定�?
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	
	/**
	 * 处理缩放 sdk 缩放级别范围�?[3.0,19.0]
	 */
	private void perfomZoom(String string) {
		try {
			float zoomLevel = Float.parseFloat(string);
			MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(zoomLevel);
			mBaiduMap.animateMapStatus(u);
		} catch (NumberFormatException e) {
			Toast.makeText(getActivity(), "请输入正确的缩放级别", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 *  广播事件
	 */ 
	public void initBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.refreshMapLocation");
		intentFilter.addAction("action.nearMapLocation");
		getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}
	
	
	
	@SuppressWarnings("unused")
	private boolean hidden;
	/**
	 * 是否被隐�? true 隐藏  false 显示
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if(!hidden){
			// 经纬度保�?
			//CustomApplcation.lastPoint.setLatitude(latitude);
			//CustomApplcation.lastPoint.setLongitude(longtitude);
		}else{
			updateInfo(latitude,longtitude);
		}
	}
	
	/** 添加经纬�?
	  * updateInfo
	  * @Title: updateInfo
	  * @return void
	  * @throws
	  */
	private void updateInfo(double latitude ,double longtitude) {
		final User user = userManager.getCurrentUser(User.class);
		user.setLatitude(latitude);
		user.setLongitude(longtitude);
		user.update(getActivity(), new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				//ShowToast("修改成功");
				Log.i("updateInfo","修改成功");
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("onFailure:" + arg1);
			}
		});
	}
	
	public void poiSearchMapDistribute(){
		mBaiduMap.clear();
		PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
		mBaiduMap.setOnMarkerClickListener(overlay);
		overlay.setData(PoiResultActivity.getInstance().getPoiResult());
		overlay.addToMap();
		overlay.zoomToSpan();
	}
	
	/**
	 *  吃喝玩乐的poi搜索
	 */
	private class MyPoiOverlay extends PoiOverlay {
		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}
		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			if(PoiResultActivity.getInstance().getPoiSearch() == null){
				return false;
			}
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			PoiResultActivity.getInstance().getPoiSearch().searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			return true;
		}
	}
	
	private ProgressDialog dialog;
	
	public void showProgressDialog(){
		dialog=new ProgressDialog(getActivity());
		dialog.setMessage("加载�?");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	public void hideProgressDialog(){
		dialog.dismiss();
	}
    
	// 公交 搜索两次 �?��
	public boolean busNextSearch = false;
	// bool判断公交，驾车，步行  -历史
	public boolean busBool = false;
	public boolean carBool = false;
	public boolean walkBool = false;
	// 当前为什么搜�? -历史
	public boolean busSearchBool = false;
	// 交换  
	//public boolean exchangeText = false;
	LatLng myLatLng;
	LatLng friendLatLng;
	/**
	 *  点击路径规划�?
	 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			routeLayout = false;
			ll_luxian.setVisibility(View.GONE);
			ll_luxian.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out));
			break;
		case R.id.search:
			String str1 =  et_start.getText().toString();
			String str2 =  et_end.getText().toString();
			PlanNode stNode;
			PlanNode enNode;
			if(str1.equals("我的位置")){
				myLatLng = new LatLng(latitude,longtitude);
				stNode = PlanNode.withLocation(myLatLng);
			}else{
				stNode = PlanNode.withCityNameAndPlaceName(city, et_start.getText().toString());
			}
			if(str2.equals("好友位置")){
				enNode = PlanNode.withLocation(MainActivity.getInstance().getLatLng());
			}else{
				enNode = PlanNode.withCityNameAndPlaceName(city, et_end.getText().toString());
			}
	        routeSearchResult(stNode,enNode);
			break;
		case R.id.bus:
			busBool = true;
			carBool = false;
			walkBool = false;
			busSearchBool = false;
			bus.setImageResource(R.drawable.common_topbar_route_bus_pressed);
			car.setImageResource(R.drawable.common_topbar_route_car_normal);
			walk.setImageResource(R.drawable.common_topbar_route_foot_normal);
			break;
		case R.id.car:
			busBool = false;
			carBool = true;
			walkBool = false;
			busSearchBool = false;
			car.setImageResource(R.drawable.common_topbar_route_car_pressed);
			bus.setImageResource(R.drawable.common_topbar_route_bus_normal);
			walk.setImageResource(R.drawable.common_topbar_route_foot_normal);
			break;
		case R.id.walk:
			busBool = false;
			carBool = false;
			walkBool = true;
			busSearchBool = false;
			walk.setImageResource(R.drawable.common_topbar_route_foot_pressed);
			bus.setImageResource(R.drawable.common_topbar_route_bus_normal);
			car.setImageResource(R.drawable.common_topbar_route_car_normal);
			break;
		case R.id.img_exchange:
			  
			if(!exchangeText){
				exchangeText = true;
				String str = et_start.getText().toString();
				et_start.setText(et_end.getText().toString());
				et_end.setText(str);
			}else{
				exchangeText = false;
				String str = et_start.getText().toString();
				et_start.setText(et_end.getText().toString());
				et_end.setText(str);
			}
			break;
			// 公交搜索
		case R.id.iv_search:
			// �?��搜索两次
			busNextSearch = false;
			// 现在为公交搜�?
			busSearchBool = true;
			busBool = false;
			carBool = false;
			walkBool = false;
			bus.setImageResource(R.drawable.common_topbar_route_bus_normal);
			car.setImageResource(R.drawable.common_topbar_route_car_normal);
			walk.setImageResource(R.drawable.common_topbar_route_foot_normal);
			busLineSearchResult(et_search.getText().toString());
			break;
		}
	}*/
	
	/**
	 *  路径搜索
	 */
	public void routeSearchResult(PlanNode starNode,PlanNode endNode){
		if(!busBool && !carBool && !walkBool){
			ShowToast("请�?择规划模�?");
			return;
		}
		showProgressDialog();
		if(busBool){
        	routePlanSearch.transitSearch((new TransitRoutePlanOption())
        			.city(city)
        			.from(starNode)
                    .to(endNode)
                    );   //.policy(TransitPolicy.EBUS_TRANSFER_FIRST)
        }else if(carBool){
        	routePlanSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(starNode)
                    .to(endNode));
        }else if(walkBool){
        	routePlanSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(starNode)
                    .to(endNode));
        }
		if(MainActivity.getInstance().getNearSearch() || MainActivity.getInstance().getFriendSearch()){
			MainActivity.getInstance().setNearSearch(false);
			MainActivity.getInstance().setFriendSearch(false);
		}
		else{
			routeLayout = false;
			ll_luxian.setVisibility(View.GONE);
			ll_luxian.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out));
		}
	}
	
	/**
	 *  城市公交搜索
	 */
	public void busLineSearchResult(String str){
		showProgressDialog();
		MainActivity.getInstance().getBusRouteList().clear();
		busLineIDList.clear();
		busLineIndex = 0;
		if(TextUtils.isEmpty(city)){
			ShowToast("未发现当前城�?");
			hideProgressDialog();
			return;
		}
		mSearch.searchInCity((new PoiCitySearchOption()).city(
				city).keyword(str));
		// 隐藏公交搜索
		busSearchLayout = false;
		ll_search.setVisibility(View.GONE);
		ll_search.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out));
	}
	
	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		// 遍历�?��poi，找到类型为公交线路的poi
		busLineIDList.clear();
		for (PoiInfo poi : result.getAllPoi()) {
			if (poi.type == PoiInfo.POITYPE.BUS_LINE|| poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
				busLineIDList.add(poi.uid);
			}
		}
		SearchNextBusline(null);
	}
	
	public void SearchNextBusline(View v) {
		if (busLineIndex >= busLineIDList.size()) {
			busLineIndex = 0;
		}
		if (busLineIndex >= 0 && busLineIndex < busLineIDList.size()
				&& busLineIDList.size() > 0) {
			mBusLineSearch.searchBusLine((new BusLineSearchOption()
					.city(city).uid(busLineIDList.get(busLineIndex))));
			busLineIndex++;
		}
	}
	
	/**
	 *  公交查询返回
	 */
	@Override
	public void onGetBusLineResult(BusLineResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			hideProgressDialog();
			Toast.makeText(getActivity(), "抱歉，未找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		// 模糊�?
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地�?��岐义，�?过以下接口获取建议查询信�?
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	String[] str = new String[result.getStations().size()];
    		for(int i = 0;i < result.getStations().size();i++){
    			str[i] = result.getStations().get(i).getTitle();
    		}
    		BusRoute busRoute = new BusRoute(result.getBusLineName() ,result.getStations().size() , str ,result); //
    		MainActivity.getInstance().getBusRouteList().add(busRoute);
    		if(!busNextSearch){
    			busNextSearch = true;
    			SearchNextBusline(null);
    			mHandler.sendEmptyMessageDelayed(HIDE_PROGRESS_DIALOG, 1000);
    		}
        }
	}

	/**
	 *  地理坐标编码
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
	
	String province;
	String city;
	String district;
	String street;
	String streetNumber;
	/**
	 *  地理坐标反编�?
	 */
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉,未能确认您当前位�?", Toast.LENGTH_LONG)
					.show();
			return;
		}
		/** mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));*/
		province = result.getAddressDetail().province;
		city = result.getAddressDetail().city;
		district = result.getAddressDetail().district;
		street = result.getAddressDetail().street;
		streetNumber = result.getAddressDetail().streetNumber;
		//Toast.makeText(getActivity(), result.getAddress(),Toast.LENGTH_LONG).show();
	}
	
    List<LatLng> latLng ;
	/**
	 *  路径规划 -公交
	 */
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			hideProgressDialog();
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地�?��岐义，�?过以下接口获取建议查询信�?
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	mBaiduMap.clear();
        	MainActivity.getInstance().getRouteList().clear();
        	for(int j = 0; j< result.getRouteLines().size(); j++){
        		latLng = new ArrayList<LatLng>();
        		LatLng nodeLocation = null;
        		String[] str = new String[result.getRouteLines().get(j).getAllStep().size()];
        		for(int i = 0; i < result.getRouteLines().get(j).getAllStep().size() ; i++){
            		//获取节结果信�?
                    nodeLocation = null;
                    String nodeTitle = null;
                    Object step = result.getRouteLines().get(j).getAllStep().get(i);
                    if (step instanceof TransitRouteLine.TransitStep) {
                        nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation();
                        nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
                    }
                    if (nodeLocation == null || nodeTitle == null) {
                        return;
                    }
                    str[i] = nodeTitle;
                    latLng.add(nodeLocation);
            	}
        		RouteInfo info = new RouteInfo(str,result,latLng);
                MainActivity.getInstance().getRouteList().add(info);
        	}
        	//mHandler.sendEmptyMessageDelayed(HIDE_PROGRESS_BUSDIALOG, 1000);
        	onMapShowRoute();
        }
	}
	
	/**
	 *  路径规划 -驾车
	 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			hideProgressDialog();
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地�?��岐义，�?过以下接口获取建议查询信�?
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	mBaiduMap.clear();
        	MainActivity.getInstance().getRouteList().clear();
        	for(int j = 0;j < result.getRouteLines().size();j++){
        		latLng = new ArrayList<LatLng>();
        		LatLng nodeLocation = null;
    			String[] str = new String[result.getRouteLines().get(j).getAllStep().size()];
    			for(int i = 0; i < result.getRouteLines().get(j).getAllStep().size() ; i++){
            		//获取节结果信�?
                    nodeLocation = null;
                    String nodeTitle = null;
                    Object step = result.getRouteLines().get(j).getAllStep().get(i);
                    if (step instanceof DrivingRouteLine.DrivingStep) {
                        nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation();
                        nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
                    }
                    if (nodeLocation == null || nodeTitle == null) {
                        return;
                    }
                    str[i] = nodeTitle;
                    latLng.add(nodeLocation);
            	}
    			RouteInfo info = new RouteInfo(str,result,latLng);
                MainActivity.getInstance().getRouteList().add(info);
    		}
        	//mHandler.sendEmptyMessageDelayed(HIDE_PROGRESS_DIRDIALOG, 1000);
        	onMapShowRoute();
        }
	}

	/**
	 *  路径规划 -步行
	 */
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			hideProgressDialog();
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地�?��岐义，�?过以下接口获取建议查询信�?
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) { 
        	mBaiduMap.clear();
        	MainActivity.getInstance().getRouteList().clear();
    		for(int j = 0;j < result.getRouteLines().size();j++){
    			latLng = new ArrayList<LatLng>();
    			String[] str = new String[result.getRouteLines().get(j).getAllStep().size()];
    			LatLng nodeLocation = null;
    			for(int i = 0; i < result.getRouteLines().get(j).getAllStep().size() ; i++){
            		//获取节结果信�?
                    nodeLocation = null;
                    String nodeTitle = null;
                    Object step = result.getRouteLines().get(j).getAllStep().get(i);
                    if (step instanceof WalkingRouteLine.WalkingStep) {
                        nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation();
                        nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
                    }
                    if (nodeLocation == null || nodeTitle == null) {
                        return;
                    }
                    str[i] = nodeTitle;
                    latLng.add(nodeLocation);
            	}
    			RouteInfo info = new RouteInfo(str,result,latLng);
                MainActivity.getInstance().getRouteList().add(info);
    		}
    		onMapShowRoute();	
        }
	}
	
	WalkingRouteOverlay overlayWalking;
	DrivingRouteOverlay overlayDriving;
	TransitRouteOverlay overlayTransit;
	/**
	 *  第一次搜索结束时路径规划
	 */
	private void onMapShowRoute(){
		hideProgressDialog();
		if(walkBool){
			overlayWalking = new MyWalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlayWalking);
			overlayWalking.setData(MainActivity.getInstance().getRouteList().get(0).
					getWalkingRouteResult().getRouteLines().get(0));
			overlayWalking.addToMap();
			overlayWalking.zoomToSpan();
		}else if(carBool){
			overlayDriving = new MyDrivingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlayDriving);
			overlayDriving.setData(MainActivity.getInstance().getRouteList().get(0).
            		getDrivingRouteResult().getRouteLines().get(0));
			overlayDriving.addToMap();
			overlayDriving.zoomToSpan();
		}else if(busBool){
			overlayTransit = new MyTransitRouteOverlay(mBaiduMap);
			overlayTransit.setData(MainActivity.getInstance().getRouteList().get(0).
					getTransitRouteResult().getRouteLines().get(0));
			overlayTransit.addToMap();
			overlayTransit.zoomToSpan();
		}
    	
        //移动节点至中�?
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(MainActivity.getInstance().getRouteList().get(0).getLatLngList().get(0)));
        // show popup
        popupText = new TextView(getActivity());
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(MainActivity.getInstance().getRouteList().get(0).getRouteInfo()[0]);
        mBaiduMap.showInfoWindow(new InfoWindow(popupText, MainActivity.getInstance().getRouteList().get(0).getLatLngList().get(0), 0));
	}
	
    private TextView popupText = null;//泡泡view
	
	/**
	 *  广播接收
	 */
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("action.refreshMapLocation")) {
				// 广播--路径规划�?
				if(busBool || carBool || walkBool){
					if(busBool){
						mBaiduMap.clear();
			        	TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
			        	mBaiduMap.setOnMarkerClickListener(overlay);
			            overlay.setData(MainActivity.getInstance().getRouteList().get(MainActivity.getInstance()
			            		.getgroupPosition()).getTransitRouteResult().getRouteLines().get(MainActivity.getInstance()
					            		.getgroupPosition()));
			            overlay.addToMap();
			            overlay.zoomToSpan();
					}
					//移动节点至中�?
		            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(MainActivity.getInstance().getRouteList().get(MainActivity.getInstance().getgroupPosition()).getLatLngList().get(MainActivity.getInstance().getchildPosition())));
		            // show popup
		            popupText = new TextView(getActivity());
		            popupText.setBackgroundResource(R.drawable.popup);
		            popupText.setTextColor(0xFF000000);
		            popupText.setText(MainActivity.getInstance().getRouteList().get(MainActivity.getInstance().getgroupPosition()).getRouteInfo()[MainActivity.getInstance().getchildPosition()]);
		            mBaiduMap.showInfoWindow(new InfoWindow(popupText, MainActivity.getInstance().getRouteList().get(MainActivity.getInstance().getgroupPosition()).getLatLngList().get(MainActivity.getInstance().getchildPosition()), 0));
				}
				// 广播--路公交搜索的
				else if(busSearchBool){
					// 显示路径
					mBaiduMap.clear();
					BusLineOverlay overlay = new BusLineOverlay(mBaiduMap);
					mBaiduMap.setOnMarkerClickListener(overlay);
					overlay.setData(MainActivity.getInstance().getBusLineResult());
					overlay.addToMap();
					overlay.zoomToSpan();
					// 移动到指定引索的坐标
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(MainActivity.getInstance().
							getBusLineResult().getStations().get(MainActivity.getInstance().getchildPosition()).getLocation()));
					// 弹出泡泡
					TextView popupText = new TextView(getActivity());
					popupText.setBackgroundResource(R.drawable.popup);
					popupText.setTextColor(0xff000000);
					popupText.setText(MainActivity.getInstance().getBusLineResult().getStations().get(MainActivity.getInstance().getchildPosition()).getTitle());
					mBaiduMap.showInfoWindow(new InfoWindow(popupText, MainActivity.getInstance().getBusLineResult().getStations()
							.get(MainActivity.getInstance().getchildPosition()).getLocation(), 0));
				}		
			}else if(action.equals("action.nearMapLocation")){
				busBool = false;
				carBool = false;
				walkBool = true;
				busSearchBool = false;
				walk.setImageResource(R.drawable.common_topbar_route_foot_pressed);
				bus.setImageResource(R.drawable.common_topbar_route_bus_normal);
				car.setImageResource(R.drawable.common_topbar_route_car_normal);
				if(MainActivity.getInstance().getNearSearch()){
					et_end.setText(PoiResultActivity.getInstance().getName());
					//MainActivity.getInstance().setNearSearch(false);
					LatLng myLatLng = new LatLng(latitude,longtitude);
					PlanNode stNode = PlanNode.withLocation(myLatLng);
			        PlanNode enNode = PlanNode.withLocation(PoiResultActivity.getInstance().getLatLng());
			        // 搜索
			        routeSearchResult(stNode,enNode);
				}
				else if(MainActivity.getInstance().getFriendSearch()){
					et_end.setText("好友位置");
					//MainActivity.getInstance().setFriendSearch(false);
					PlanNode stNode = PlanNode.withLocation(new LatLng(latitude,longtitude));
			        PlanNode enNode = PlanNode.withLocation(MainActivity.getInstance().getLatLng());
			        // 搜索
			        routeSearchResult(stNode,enNode);
				}
			}
		}
	};
	
	boolean useDefaultIcon = false;
	
	private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                //return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                //return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }
	
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                //return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
               // return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }
    
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                //return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
               //return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }
    
    /**
     *  初始化标题栏且监听事�?
     */
    public void initView() {
		hideTitle = (LinearLayout)findViewById(R.id.common_actionbar);
		initTopBarForBoth("定位", R.drawable.base_action_bar_history_bg_selector,
				new onRightImageButtonClickListener() {
					@Override
					public void onClick(View v) {
                        if(busSearchBool){
                        	Intent intent =new Intent(getActivity(),SearchListActivity.class);
            				intent.putExtra("busheadline", et_search.getText().toString());
            				startActivity(intent);
                        }else if(walkBool){
                        	Intent intent =new Intent(getActivity(),SearchListActivity.class);
                        	intent.putExtra("busheadline", "步行");
            				startActivity(intent);
                        }
                        else if(carBool){
                        	Intent intent =new Intent(getActivity(),SearchListActivity.class);
                        	intent.putExtra("busheadline", "驾车");
            				startActivity(intent);
                        }
                        else if(busBool){
                        	Intent intent =new Intent(getActivity(),SearchListActivity.class);
                        	intent.putExtra("busheadline", "公交");
            				startActivity(intent);
                        }else{
                        	ShowToast("无搜索历�?");
                        }
					}
				});
	}
	
    private int map_count = 0;
    /**
     *  初始化控件且监听控件
     */
	public void initFindViewListen(){
		// 放大缩小
		imgBig = (ImageView) findViewById(R.id.iv_big);
		imgSmall = (ImageView) findViewById(R.id.iv_small);
		// 定位
		requestLocLayout = (LinearLayout) findViewById(R.id.layout_mylocation);
		requestLocImgview = (ImageView) findViewById(R.id.iv_mylocation);
		// 地图显示模式  平面和卫�?
		mapShow = (LinearLayout) findViewById(R.id.ll_map_c);
		btnShow = (LinearLayout) findViewById(R.id.ll_map);
		imgViewMapMode = (ImageView) findViewById(R.id.iv_map);
		imgViewMapModePlain = (ImageView) findViewById(R.id.map_mode_plain);
		imgViewMapModeSatellite = (ImageView) findViewById(R.id.map_mode_satellite);
		// 路线状�?�?
		imgViewLoad = (ImageView) findViewById(R.id.iv_load);
		loadLayout = (LinearLayout) findViewById(R.id.ll_road);
		mCurrentMode = LocationMode.NORMAL;
		/**  
		// 路径规划
		ll_luxian=(LinearLayout) findViewById(R.id.ll_luxian);
		bus=(ImageView) findViewById(R.id.bus);
		car=(ImageView) findViewById(R.id.car);
		walk=(ImageView) findViewById(R.id.walk);
		back=(ImageView) findViewById(R.id.back);
		search=(Button) findViewById(R.id.search);
		exchange=(ImageView) findViewById(R.id.img_exchange);
		et_start=(EditText) findViewById(R.id.et_start);
		et_end=(EditText) findViewById(R.id.et_end);
		// 公交搜索
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
		img_search = (ImageView) findViewById(R.id.iv_search);
		et_search = (EditText) findViewById(R.id.et_bussearch);*/
		
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				if (v.equals(imgBig)){
					if(zoomLevel <= 19 && zoomLevel >= 3){
						zoomLevel++;
						perfomZoom(String.valueOf(zoomLevel));
					}else{
						zoomLevel = 19;
						ShowToast("已经放大到最�?");
					}
				}else if (v.equals(imgSmall)){
					if(zoomLevel <= 19 && zoomLevel >= 3){
						zoomLevel--;
						perfomZoom(String.valueOf(zoomLevel));
					}else{
						zoomLevel= 3;
						ShowToast("已经是最小啦");
					}
				}else if (v.equals(requestLocImgview)){
					switch (mCurrentMode) {
					case NORMAL:
						//requestLocButton.setText("跟随");
						mCurrentMode = LocationMode.FOLLOWING;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						break;
					case COMPASS:
						//requestLocButton.setText("普�?");
						mCurrentMode = LocationMode.NORMAL;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						break;
					case FOLLOWING:
						//requestLocButton.setText("罗盘");
						mCurrentMode = LocationMode.COMPASS;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						break;
					}
				} else if(v.equals(imgViewMapMode)){
					map_count++;
					if(map_count%2==0){
						imgViewMapMode.setImageResource(R.drawable.main_icon_maplayers);
						mapShow.setVisibility(View.GONE);
					}else{
						imgViewMapMode.setImageResource(R.drawable.main_icon_close1);
					    mapShow.setVisibility(View.VISIBLE);	
					}
				} else if(v.equals(imgViewMapModePlain)){
					map_count++;
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
					imgViewMapMode.setImageResource(R.drawable.main_icon_maplayers);
					mapShow.setVisibility(View.GONE);
				}
				 else if(v.equals(imgViewMapModeSatellite)){
					map_count++;
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
					imgViewMapMode.setImageResource(R.drawable.main_icon_maplayers);
					mapShow.setVisibility(View.GONE);
				} else if(v.equals(imgViewLoad)){
					if(!bLoad){
						bLoad = true;
						mBaiduMap.setTrafficEnabled(bLoad);
						imgViewLoad.setImageResource(R.drawable.main_icon_roadcondition_on);
					}else{
						bLoad = false;
						mBaiduMap.setTrafficEnabled(bLoad);
						imgViewLoad.setImageResource(R.drawable.main_icon_roadcondition_off);
					}
				}
			}
		};
		requestLocImgview.setOnClickListener(btnClickListener);
		imgBig.setOnClickListener(btnClickListener);
		imgSmall.setOnClickListener(btnClickListener);
		imgViewMapMode.setOnClickListener(btnClickListener);
		imgViewMapModePlain.setOnClickListener(btnClickListener);
		imgViewMapModeSatellite.setOnClickListener(btnClickListener);
		imgViewLoad.setOnClickListener(btnClickListener);
		
		bus.setOnClickListener(this);
		car.setOnClickListener(this);
		walk.setOnClickListener(this);
		search.setOnClickListener(this);
		back.setOnClickListener(this);
		
		img_search.setOnClickListener(this);
	}
	
	private ComposerLayout clayout;
	/**
	 *  弹出半圆菜单
	 */
	public void initMenu(){
		// 引用控件
		clayout = (ComposerLayout) findViewById(R.id.test);
		clayout.init(new int[] { R.drawable.composer_clermap,
				R.drawable.composer_coverageshow, R.drawable.composer_location,
				R.drawable.composer_bus, R.drawable.composer_route,
				R.drawable.composer_fullscreen }, R.drawable.composer_button,
				R.drawable.composer_icn_plus, ComposerLayout.RIGHTBOTTOM, 300,
				300);
		setListener();
	}
	
	// 隐藏tab
	boolean tabTitleLayout = false;
	// 路径规划
	boolean routeLayout = false;
	// 公交搜索
	boolean busSearchLayout = false;
	// 显示图层和路况的的设�?
	boolean otherLayout = false;
	
	/**
	 *  弹出半圆菜单的监�?
	 */
	public void setListener() {
		// 点击事件监听�?00+0对应composer_camera�?00+1对应composer_music…�?如此类推你有机个按钮就加几个按钮都行�?
				OnClickListener clickit = new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (v.getId() == 100 + 0) {
							mBaiduMap.clear();   // 餐厅 超市 银行
						} else if (v.getId() == 100 + 1) {
							if(!otherLayout){
								otherLayout = true;
								loadLayout.setVisibility(View.VISIBLE);
								btnShow.setVisibility(View.VISIBLE);
							}else{
								otherLayout = false;
								loadLayout.setVisibility(View.GONE);
								btnShow.setVisibility(View.GONE);
							}
						} else if (v.getId() == 100 + 2) {
							switch (mCurrentMode) {
							case NORMAL:
								//requestLocButton.setText("跟随");
								mCurrentMode = LocationMode.FOLLOWING;
								mBaiduMap
										.setMyLocationConfigeration(new MyLocationConfiguration(
												mCurrentMode, true, mCurrentMarker));
								break;
							case COMPASS:
								//requestLocButton.setText("普�?");
								mCurrentMode = LocationMode.NORMAL;
								mBaiduMap
										.setMyLocationConfigeration(new MyLocationConfiguration(
												mCurrentMode, true, mCurrentMarker));
								break;
							case FOLLOWING:
								//requestLocButton.setText("罗盘");
								mCurrentMode = LocationMode.COMPASS;
								mBaiduMap
										.setMyLocationConfigeration(new MyLocationConfiguration(
												mCurrentMode, true, mCurrentMarker));
								break;
							}
						} else if (v.getId() == 100 + 3) {
							if(!busSearchLayout){
								busSearchLayout = true;
								ll_search.setVisibility(View.VISIBLE);
								ll_search.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_in));
							    if(routeLayout){
							    	routeLayout = false;
									ll_luxian.setVisibility(View.GONE);
									ll_luxian.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out));
							    }
							}else{
								busSearchLayout = false;
								ll_search.setVisibility(View.GONE);
								ll_search.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out));
							}
						} else if (v.getId() == 100 + 4) {
							if(!routeLayout){
								routeLayout = true;
								ll_luxian.setVisibility(View.VISIBLE);
								ll_luxian.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_in));
								if(busSearchLayout){
							    	busSearchLayout = false;
									ll_search.setVisibility(View.GONE);
									ll_search.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out));
							    }
							}else{
								routeLayout = false;
								ll_luxian.setVisibility(View.GONE);
								ll_luxian.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out));
							}
							
						} else if (v.getId() == 100 + 5) {
							if(!tabTitleLayout){
								tabTitleLayout = true;
								hideTitle.setVisibility(View.GONE);
								hideTitle.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out));
								MainActivity.getInstance().hideLayoutMenu();
							}else{
								tabTitleLayout = false;
								hideTitle.setVisibility(View.VISIBLE);
								MainActivity.getInstance().showLayoutMenu();
							}
						}
					}
				};
				clayout.setButtonsOnClickListener(clickit);		
	}
    
    private static final int REVERSE_GEO_CODER = 100;  
    private static final int HIDE_PROGRESS_DIALOG = 200;  

    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REVERSE_GEO_CODER:
				// 地理编码和反编码
				reverseGeoCoder();
				break;
			case HIDE_PROGRESS_DIALOG:
				hideProgressDialog();
				Intent intent =new Intent(getActivity(),SearchListActivity.class);
				intent.putExtra("busheadline", et_search.getText().toString());
				startActivity(intent);
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
