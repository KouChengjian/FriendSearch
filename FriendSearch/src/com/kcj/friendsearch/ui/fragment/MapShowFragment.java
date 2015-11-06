package com.kcj.friendsearch.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.bean.Model;
import com.kcj.friendsearch.confi.BaiduMapSearchMessage;
import com.kcj.friendsearch.ui.FragmentBase;
import com.kcj.friendsearch.ui.MainActivity;
import com.kcj.friendsearch.view.ComposerLayout;
import com.xiapu.whereisfriend.R;

/**
 * @ClassName: MapShowFragment
 * @Description: ��ͼ��ʾ����
 * @author kcj
 * @date 
 */
public class MapShowFragment extends FragmentBase {
	/** �� */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	//private BitmapDescriptor mCurrentMarker;
	public MyLocationListenner myListener = new MyLocationListenner();
	/** UI */
	// ��λ
	//private LinearLayout requestLocLayout;
	//private ImageView requestLocImgview;
	private boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	// ��ͼģʽ
	private LinearLayout mapShow;
	private LinearLayout btnShow;
	private ImageView imgViewMapMode, imgViewMapModePlain,
			imgViewMapModeSatellite;
	// ���ż��� 3~19
	//private int zoomLevel = 17;
	// ·��
	private boolean bLoad = false;
	private LinearLayout loadLayout;
	private ImageView imgViewLoad;
	private LocationMode mCurrentMode;
	// Բ���˵�
	private ComposerLayout clayout;
	// ��Ϣ��ʾ
	TextView tvInfoShow;
	TextView tvInfoShow2;
	private RelativeLayout rlInfoShow;
	private LinearLayout llInfoShowTradePage;
	Button btnTradePageleft,btnTradePageright,btnInfoShowLeft,btnInfoShowRight,btnInfoShowRoute;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub fragment_mapshow
		return inflater.inflate(R.layout.fragment_location, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initMapSDKAll();
		initMapShowAllUi();
		
	}

	private void initMapSDKAll() {
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // �����������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// ��ͼͼ��
		float zoomLevel = Float.parseFloat("17");
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(zoomLevel);
		mBaiduMap.animateMapStatus(u);
		// ���ذٶȵ�ͼ�Դ�ķŴ���С ������
        int childCount = mMapView.getChildCount();// �������ſؼ�
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
                View child = mMapView.getChildAt(i);
                if (child instanceof ZoomControls) {
                        zoom = child;
                        break;
                }
        }
        zoom.setVisibility(View.GONE);
        // ɾ��ٶȵ�ͼlogo
        mMapView.removeViewAt(1);
	}
	
	private void initMapShowAllUi() {
		findViewById();
		findViewByIdListen();
	}

	private int map_count = 0;

	private void findViewByIdListen() {
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				/**  
				// ��λ
				if (v.equals(requestLocImgview)) {
					switch (mCurrentMode) {
					case NORMAL:
						// requestLocButton.setText("����");
						mCurrentMode = LocationMode.FOLLOWING;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						break;
					case COMPASS:
						// requestLocButton.setText("��ͨ");
						mCurrentMode = LocationMode.NORMAL;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						break;
					case FOLLOWING:
						// requestLocButton.setText("����");
						mCurrentMode = LocationMode.COMPASS;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						break;
					}
				}
				// ��ͼ��ʾģʽ
				else */
					if (v.equals(imgViewMapMode)) {
					map_count++;
					if (map_count % 2 == 0) {
						imgViewMapMode
								.setImageResource(R.drawable.main_icon_maplayers);
						mapShow.setVisibility(View.GONE);
					} else {
						imgViewMapMode
								.setImageResource(R.drawable.main_icon_close1);
						mapShow.setVisibility(View.VISIBLE);
					}
				} else if (v.equals(imgViewMapModePlain)) {
					map_count++;
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
					imgViewMapMode
							.setImageResource(R.drawable.main_icon_maplayers);
					mapShow.setVisibility(View.GONE);
				} else if (v.equals(imgViewMapModeSatellite)) {
					map_count++;
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
					imgViewMapMode
							.setImageResource(R.drawable.main_icon_maplayers);
					mapShow.setVisibility(View.GONE);
				} else if (v.equals(imgViewLoad)) {
					if (!bLoad) {
						bLoad = true;
						mBaiduMap.setTrafficEnabled(bLoad);
						imgViewLoad
								.setImageResource(R.drawable.main_icon_roadcondition_on);
					} else {
						bLoad = false;
						mBaiduMap.setTrafficEnabled(bLoad);
						imgViewLoad
								.setImageResource(R.drawable.main_icon_roadcondition_off);
					}
				}
			}
		};
		//requestLocImgview.setOnClickListener(btnClickListener);
		imgViewMapMode.setOnClickListener(btnClickListener);
		imgViewMapModePlain.setOnClickListener(btnClickListener);
		imgViewMapModeSatellite.setOnClickListener(btnClickListener);
		imgViewLoad.setOnClickListener(btnClickListener);
		// Բ���˵�����
		OnClickListener clickit = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == 100 + 0) {
					mBaiduMap.clear();
					rlInfoShow.setVisibility(View.GONE);
				} else if (v.getId() == 100 + 1) {
					
				} else if (v.getId() == 100 + 2) {
					
				} else if (v.getId() == 100 + 3) {
				
				} else if (v.getId() == 100 + 4) {
					
					
				} else if (v.getId() == 100 + 5) {
					
				}
			}
		};
		clayout.setButtonsOnClickListener(clickit);	
	}

	private void findViewById() {
		// ��λ
		//requestLocLayout = (LinearLayout) findViewById(R.id.layout_mylocation);
		//requestLocImgview = (ImageView) findViewById(R.id.iv_mylocation);
		// ��ͼ��ʾģʽ ƽ�������
		mapShow = (LinearLayout) findViewById(R.id.ll_map_c);
		btnShow = (LinearLayout) findViewById(R.id.ll_map);
		imgViewMapMode = (ImageView) findViewById(R.id.iv_map);
		imgViewMapModePlain = (ImageView) findViewById(R.id.map_mode_plain);
		imgViewMapModeSatellite = (ImageView) findViewById(R.id.map_mode_satellite);
		// ·��״̬ͼ
		imgViewLoad = (ImageView) findViewById(R.id.iv_load);
		loadLayout = (LinearLayout) findViewById(R.id.ll_road);
		mCurrentMode = LocationMode.NORMAL;
		// ��Ϣ��ʾ
		rlInfoShow = (RelativeLayout) findViewById(R.id.rl_showinfo);
		llInfoShowTradePage = (LinearLayout) findViewById(R.id.ll_showinfo_tradepage);
		btnTradePageleft = (Button) findViewById(R.id.btn_showinfo_left);
		btnTradePageright = (Button) findViewById(R.id.btn_showinfo_right);
		btnInfoShowLeft = (Button) findViewById(R.id.btn_showinfo_content_left);
		btnInfoShowRight = (Button) findViewById(R.id.btn_showinfo_content_right);
		tvInfoShow = (TextView) findViewById(R.id.tv_showinfo_content_text);
		tvInfoShow2 = (TextView) findViewById(R.id.tv_showinfo_content_text2);
		btnInfoShowRoute = (Button) findViewById(R.id.btn_showinfo_content_text_route);
		// ���ÿؼ�
		clayout = (ComposerLayout) findViewById(R.id.test);
		clayout.init(new int[] { R.drawable.composer_clermap,
				R.drawable.composer_coverageshow, R.drawable.composer_location,
				R.drawable.composer_bus, R.drawable.composer_route,
				R.drawable.composer_fullscreen }, R.drawable.composer_button,
				R.drawable.composer_icn_plus, ComposerLayout.RIGHTCENTER, 300,
				300);
	}
	
	double latitude = 0;
	double longtitude = 0;
	boolean b = false;
	/**
	 * ��λSDK������   ��λ��γ��
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ��ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
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
			if(!b){
				b = true;
				BaiduMapSearchMessage.getInstance().getMapSearchFragment().againGetReverseGeoCodeResult();
			}
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
		// ��ʼ���㲥-���ڽ�����Ϣ
		initBroadcastReceiver();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// �˳�ʱ��ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	
	/**
	 *  �㲥�¼�
	 */ 
	public void initBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.nearSearchPoi");
		intentFilter.addAction("action.nearSearchPoiRouteShow");
		getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}
	
	/**
	 *  �㲥����
	 */
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("action.nearSearchPoi")) {
				MainActivity.getInstance().showMapFargment();
				mBaiduMap.clear();
				PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
				mBaiduMap.setOnMarkerClickListener(overlay);
				overlay.setData(BaiduMapSearchMessage.getInstance().getPoiResult());
				overlay.addToMap();
				overlay.zoomToSpan();
			}
			if (action.equals("action.nearSearchPoiRoute")) {
				Model model = BaiduMapSearchMessage.getInstance().getModel();
				
			}
		}
	};	
	
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			if(rlInfoShow.isEnabled()){
				rlInfoShow.setVisibility(View.VISIBLE);
				llInfoShowTradePage.setVisibility(View.GONE);
				btnInfoShowLeft.setVisibility(View.GONE);
				btnInfoShowRight.setVisibility(View.GONE);
				//btnInfoShowRight.setBackgroundResource(R.drawable.main_icon_result_go);
			}
			
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			tvInfoShow.setText(poi.name);
			tvInfoShow.setGravity(Gravity.CENTER);
			tvInfoShow2.setText(poi.address);
			//Toast.makeText(getActivity(), poi.name, 1).show();
			// if (poi.hasCaterDetails) {
			//BaiduMapSearchMessage.getInstance().getPoiSearch().searchPoiDetail((new PoiDetailSearchOption())
			//			.poiUid(poi.uid));
			// }
			return true;
		}
	}
}
