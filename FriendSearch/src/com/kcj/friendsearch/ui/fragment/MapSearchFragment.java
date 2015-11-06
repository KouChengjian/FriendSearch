package com.kcj.friendsearch.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.CityInfo;
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
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
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
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.adapter.BusExpandAdapter;
import com.kcj.friendsearch.adapter.RoutePlanAdapter;
import com.kcj.friendsearch.adapter.XBaseAdapter;
import com.kcj.friendsearch.bean.Model;
import com.kcj.friendsearch.bean.RouteInfo;
import com.kcj.friendsearch.confi.BaiduMapSearchMessage;
import com.kcj.friendsearch.ui.FragmentBase;
import com.kcj.friendsearch.ui.MainActivity;
import com.kcj.friendsearch.view.list.XListView;
import com.kcj.friendsearch.view.list.XListView.IXListViewListener;
import com.xiapu.whereisfriend.R;

/**
 * @ClassName: MapSearchFragment
 * @Description: 
 * @author kcj
 * @date 
 */
public class MapSearchFragment extends FragmentBase implements IXListViewListener , OnChildClickListener
   ,OnGetPoiSearchResultListener ,OnGetGeoCoderResultListener,OnGetRoutePlanResultListener,OnGetBusLineSearchResultListener{
	
	XListView listView;
	private XBaseAdapter xBaseAdapter;
	// �ĸ�����
	LinearLayout llAllLikeTitle,llAllLikeTextTitle;
	ImageView imgCate ,imgGrogshop,imgRecreation,imgTraffic;
	LinearLayout llAllLikeChoss;
	ImageView imgChoosepicture;
	Button[] btnLikeContent;
	String[][] likeTextContent = new String[][]{
			{"�в�","���","����","���","�ϵ»�","����", "ţ��","�湦��","�����"},
			{"��ݾƵ�", "�Ǽ��Ƶ�", "��������", "�д���","�ù�",null,null,null,null},
			{"������", "�ư�","��ӰԺ", "KTV", "���", "����", "ϴԡ","����",null},
			{ "ͣ����", "��վ", "����վ", "����վ","����", "ҩ��", "ATM", "����" , "ҽԺ"}};
	// ����
	private ListView aloneListView;
	private RoutePlanAdapter aloneRouteAdapter = null;
	private ExpandableListView expandableListView = null;
	private BusExpandAdapter expandableAdapter = null;
    /**        ����                 */	
	// bool
	boolean poiBusSearch = false;
	boolean poiNearSearch = false;
	boolean b_imgbus,b_imgcar,b_imgwalk ,b_imgbusline;
	// ·�߹滮
	LinearLayout ll_route_search;
	private Button btnSearch;
	private EditText et_start;
	private EditText et_end;
	ImageView img_back ,img_car,img_walk,img_bus,img_exchange;
	// ����
	RelativeLayout ll_search;
	private EditText et_content;
	ImageView imgBusLineBreak,imgBusLineSearch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_mapsearch, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initMapSearchListUi();
		initMapSearch();
		initBroadcastReceiver();
	}
	
	// ����
	private PoiSearch mPoiSearch = null;
	//������ 
	LatLng ptCenter;
	private GeoCoder geoReverseCoder = null; 
	// ·���滮
	private RoutePlanSearch routeSearch = null;
	// ����·��
	private List<String> busLineIDList = new ArrayList<String>();
	private BusLineSearch mBusLineSearch = null;
	
	public void initMapSearch(){
		// ����
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		BaiduMapSearchMessage.getInstance().setPoiSearch(mPoiSearch);
		// ·��
		routeSearch = RoutePlanSearch.newInstance();
		routeSearch.setOnGetRoutePlanResultListener(this);
		// ����·��
		mBusLineSearch = BusLineSearch.newInstance();
		mBusLineSearch.setOnGetBusLineSearchResultListener(this);
		// ����
		geoReverseCoder = GeoCoder.newInstance();
		geoReverseCoder.setOnGetGeoCodeResultListener(this);
		if(!TextUtils.isEmpty(CustomApplcation.getInstance().getLatitude()) && 
				!TextUtils.isEmpty(CustomApplcation.getInstance().getLongtitude())){
			ptCenter = new LatLng(Double.valueOf(CustomApplcation.getInstance().getLatitude()).doubleValue(),
					Double.valueOf(CustomApplcation.getInstance().getLongtitude()).doubleValue());
			geoReverseCoder.reverseGeoCode(new ReverseGeoCodeOption()
			.location(ptCenter));		
		}else{
			ShowToast("��ǰ�޾�γ����Ϣ");
		}
	}
	
	public void initMapSearchListUi(){
		// ��������Ϣ
		BaiduMapSearchMessage.getInstance().setMapSearchFragment(this);
		// ��ʼ��·�������빫������ListView
		aloneListView = (ListView) findViewById(R.id.listview);
		expandableListView = (ExpandableListView) findViewById(R.id.expandable_listView);
		// listview  ���� header
		listView = (XListView) findViewById(R.id.listView_map);
		LinearLayout headView = (LinearLayout) mInflater.inflate(R.layout.include_map_search, null);
		initFindHeadView(headView);
		listView.addHeaderView(headView);
		xBaseAdapter = new XBaseAdapter(getActivity(), R.layout.xlistview_item,getActivity());
		listView.setAdapter(xBaseAdapter);
		listView.setXListViewListener(this);// ���XListView������������ˢ�¼�����
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
	}
	
	int selectNum = 0;
	String selectTitle ;
	public void initFindHeadView(LinearLayout headView){
		// bool ��ʼ��
		b_imgwalk = true;
		b_imgbus = b_imgcar = b_imgbusline = false;
		// ui-·��
		ll_route_search = (LinearLayout) headView.findViewById(R.id.ll_route_search);
		img_bus=(ImageView) headView.findViewById(R.id.img_route_bus);
		img_car=(ImageView) headView.findViewById(R.id.img_route_car);
		img_walk=(ImageView) headView.findViewById(R.id.img_route_walk);
		img_back=(ImageView) headView.findViewById(R.id.img_route_back);
		btnSearch=(Button) headView.findViewById(R.id.img_route_search);
		img_exchange=(ImageView) headView.findViewById(R.id.img_route_exchange);
		et_start=(EditText) headView.findViewById(R.id.et_route_start);
		et_end=(EditText) headView.findViewById(R.id.et_route_end);
		// ui-����
		ll_search = (RelativeLayout) headView.findViewById(R.id.rl_busline_search);
		et_content = (EditText) headView.findViewById(R.id.edt_busline_buscontent);
		imgBusLineBreak = (ImageView) headView.findViewById(R.id.img_busline_break);
		imgBusLineSearch = (ImageView) headView.findViewById(R.id.img_busline_search);
		// like
		llAllLikeTextTitle = (LinearLayout) headView.findViewById(R.id.ll_like_title);
		llAllLikeTitle = (LinearLayout) headView.findViewById(R.id.ll_like_content);
		imgCate = (ImageView) headView.findViewById(R.id.iv_cate);
		imgGrogshop = (ImageView) headView.findViewById(R.id.iv_grogshop);
		imgRecreation = (ImageView) headView.findViewById(R.id.iv_recreation);
		imgTraffic = (ImageView) headView.findViewById(R.id.iv_traffic);
		imgCate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(llAllLikeTitle.isEnabled()){
					selectNum = 0;
					setLikeAnimation(selectNum);
					imgChoosepicture.setBackgroundResource(R.drawable.search_near_eat);
				}
			}
		});
		imgGrogshop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(llAllLikeTitle.isEnabled()){
					selectNum = 1;
					setLikeAnimation(selectNum);
					imgChoosepicture.setBackgroundResource(R.drawable.search_near_grogshop);
				}
			}
		});
		imgRecreation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(llAllLikeTitle.isEnabled()){
					selectNum = 2;
					setLikeAnimation(selectNum);
					imgChoosepicture.setBackgroundResource(R.drawable.search_near_recreation);
				}
			}
		});
		imgTraffic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(llAllLikeTitle.isEnabled()){
					selectNum = 3;
					setLikeAnimation(selectNum);
					imgChoosepicture.setBackgroundResource(R.drawable.search_near_live);
				}
			}
		});
		llAllLikeChoss = (LinearLayout) headView.findViewById(R.id.ll_like_content_choose);
		imgChoosepicture = (ImageView) headView.findViewById(R.id.img_like_content_choose);
		btnLikeContent = new Button[9];
		btnLikeContent[0] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_1_1);
		btnLikeContent[1] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_1_2);
		btnLikeContent[2] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_1_3);
		btnLikeContent[3] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_2_1);
		btnLikeContent[4] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_2_2);
		btnLikeContent[5] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_2_3);
		btnLikeContent[6] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_3_1);
		btnLikeContent[7] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_3_2);
		btnLikeContent[8] = (Button) headView.findViewById(R.id.ll_like_content_choose_text_3_3);
		imgChoosepicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(imgChoosepicture.isEnabled()){
					llAllLikeTitle.setVisibility(View.VISIBLE);
					llAllLikeChoss.setVisibility(View.GONE);
				}
			}
		});
		
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				if (v.equals(btnLikeContent[0])){
					selectTitle = likeTextContent[selectNum][0];
					if(selectTitle == null){
						return;
					}
				}else if (v.equals(btnLikeContent[1])){
					selectTitle = likeTextContent[selectNum][1];
					if(selectTitle == null){
						return;
					}
				}else if (v.equals(btnLikeContent[2])){
					selectTitle = likeTextContent[selectNum][2];
					if(selectTitle == null){
						return;
					}
				}else if (v.equals(btnLikeContent[3])){
					selectTitle = likeTextContent[selectNum][3];
					if(selectTitle == null){
						return;
					}
				}else if (v.equals(btnLikeContent[4])){
					selectTitle = likeTextContent[selectNum][4];
					if(selectTitle == null){
						return;
					}
				}else if (v.equals(btnLikeContent[5])){
					selectTitle = likeTextContent[selectNum][5];
					if(selectTitle == null){
						return;
					}
				}else if (v.equals(btnLikeContent[6])){
					selectTitle = likeTextContent[selectNum][6];
					if(selectTitle == null){
						return;
					}
				}else if (v.equals(btnLikeContent[7])){
					selectTitle = likeTextContent[selectNum][7];
					if(selectTitle == null){
						return;
					}
				}else if (v.equals(btnLikeContent[8])){
					selectTitle = likeTextContent[selectNum][8];
					if(selectTitle == null){
						return;
					}
				}
				pagenum = 0;
				poiBusSearch = false;
				poiNearSearch= true;
				searchNearch(selectTitle);
			}
		};
		for(int i = 0; i < 9; i++){
			btnLikeContent[i].setOnClickListener(btnClickListener);
        }
		
		OnClickListener btnSearchClickListener = new OnClickListener() {
			public void onClick(View v) {
				if (v.equals(img_bus)){
					b_imgbus = true;
					b_imgwalk = b_imgcar = b_imgbusline = false;
					aloneListView.setVisibility(View.GONE);;
					expandableListView.setVisibility(View.VISIBLE);
					img_bus.setImageResource(R.drawable.common_topbar_route_bus_pressed);
					img_car.setImageResource(R.drawable.common_topbar_route_car_normal);
					img_walk.setImageResource(R.drawable.common_topbar_route_foot_normal);
				}else if (v.equals(img_car)){
					b_imgcar = true;
					b_imgbus = b_imgwalk = b_imgbusline = false;
					aloneListView.setVisibility(View.VISIBLE);;
					expandableListView.setVisibility(View.GONE);
					img_car.setImageResource(R.drawable.common_topbar_route_car_pressed);
					img_bus.setImageResource(R.drawable.common_topbar_route_bus_normal);
					img_walk.setImageResource(R.drawable.common_topbar_route_foot_normal);
				}else if (v.equals(img_walk)){
					b_imgwalk = true;
					b_imgbus = b_imgcar = b_imgbusline = false;
					aloneListView.setVisibility(View.VISIBLE);;
					expandableListView.setVisibility(View.GONE);
					img_walk.setImageResource(R.drawable.common_topbar_route_foot_pressed);
					img_bus.setImageResource(R.drawable.common_topbar_route_bus_normal);
					img_car.setImageResource(R.drawable.common_topbar_route_car_normal);
				}else if (v.equals(img_back)){
					llAllLikeTitle.setVisibility(View.VISIBLE);
					llAllLikeTextTitle.setVisibility(View.VISIBLE);
					ll_route_search.setVisibility(View.GONE);
					if(aloneRouteAdapter != null){
						aloneRouteAdapter.clear();
					}
					if(expandableAdapter != null){
						expandableAdapter.clear();
					}
				}else if (v.equals(btnSearch)){
					PlanNode stNode;
					PlanNode enNode;
					if(TextUtils.isEmpty(et_start.getText().toString())){
						ShowToast("��㲻Ϊ��");
						return;
					}
                    if(TextUtils.isEmpty(et_end.getText().toString())){
                    	ShowToast("�յ㲻Ϊ��");
                    	return;
					}
                    if(et_start.getText().toString().equals("�ҵ�λ��")){
            			stNode = PlanNode.withLocation(ptCenter);
            		}else{
            			stNode = PlanNode.withCityNameAndPlaceName(city , et_start.getText().toString());
            		}
            		
            		if(et_end.getText().toString().equals("����λ��")){
            			//enNode = PlanNode.withLocation(new LatLng(l1,l2));
            			enNode = PlanNode.withCityNameAndPlaceName(city,et_end.getText().toString());
            		}else{
            			enNode = PlanNode.withCityNameAndPlaceName(city,et_end.getText().toString());
            		}
					searchRoute(b_imgbus,b_imgcar,b_imgwalk,stNode,enNode);
				} else if (v.equals(imgBusLineBreak)){
					llAllLikeTitle.setVisibility(View.VISIBLE);
					llAllLikeTextTitle.setVisibility(View.VISIBLE);
					ll_search.setVisibility(View.GONE);
					if(expandableAdapter != null){
						expandableAdapter.clear();
					}
				}else if (v.equals(imgBusLineSearch)){
					if(poiBusSearch){
						next = 0;
						busLineSearchResult(et_content.getText().toString());
					}else{
						pagenum = 0;
						searchNearch(et_content.getText().toString());
					}
				} 
			}
		};
		img_bus.setOnClickListener(btnSearchClickListener);
		img_car.setOnClickListener(btnSearchClickListener);
		img_walk.setOnClickListener(btnSearchClickListener);
		img_back.setOnClickListener(btnSearchClickListener);
		btnSearch.setOnClickListener(btnSearchClickListener);
		imgBusLineBreak.setOnClickListener(btnSearchClickListener);
		imgBusLineSearch.setOnClickListener(btnSearchClickListener);
	}
	
	public void setLikeAnimation(int j){
		llAllLikeChoss.setVisibility(View.VISIBLE);
		llAllLikeTitle.setVisibility(View.GONE);
		for(int i = 0; i < 9; i++){
        	btnLikeContent[i].setText(likeTextContent[j][i]);
        }
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		refreshListViewInBackground();
	}
	void refreshListViewInBackground() {// ģ��ˢ�����
		xBaseAdapter.notifyDataSetChanged();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		pagenum++;
		searchNearch(selectTitle);
		listView.setSelection(xBaseAdapter.getCount() - 1);// ������ƶ������صĽ��紦
		onLoad();
	}
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("�ո�");
	}
	
	private int pagenum ;
	private ProgressDialog dialog;
	List<Model> searchNearInfo=  new ArrayList<Model>();
	/**
	 *  ��������
	 */
	public void searchNearch(String str){
		dialog=new ProgressDialog(getActivity());
		dialog.setMessage("������");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		//poi��ѯ
		mPoiSearch.searchNearby((new PoiNearbySearchOption(). keyword(str)
		.location(new LatLng( Double.valueOf(CustomApplcation.getInstance().getLatitude()) , 
				Double.valueOf(CustomApplcation.getInstance().getLongtitude()))).pageCapacity(10)
		.pageNum(pagenum)).radius(2000));
	}
	
	
	/**
	 *  ·������
	 */
	public void searchRoute(boolean b_imgbus,boolean b_imgcar,boolean b_imgwalk,
			PlanNode starNode,PlanNode endNode){
		if(!b_imgbus && !b_imgcar && !b_imgwalk){
			ShowToast("��ѡ��滮ģʽ");
			return;
		}
		dialog=new ProgressDialog(getActivity());
		dialog.setMessage("������");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		if(b_imgbus){
			routeSearch.transitSearch((new TransitRoutePlanOption()).city(city).
					from(starNode).to(endNode));
		}
        if(b_imgcar){
        	routeSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(starNode).to(endNode));
		}
        if(b_imgwalk){
        	routeSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(starNode).to(endNode));
        }
	}
	
	/**
	 *  ���й���·������
	 */
	public void busLineSearchResult(String strBus){
		dialog=new ProgressDialog(getActivity());
		dialog.setMessage("������");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		if(TextUtils.isEmpty(city)){
			dialog.dismiss();
			ShowToast("δ���ֵ�ǰ����");
			return;
		}
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(
				city).keyword(strBus));
	}
	
	int next = 0;
	/**
	 *  ��������·��
	 */
	public void SearchBusline() {
		if (busLineIDList.size() <= 0) {
			ShowToast("�޹�����Ϣ");
		}
		if(next < busLineIDList.size()){
			mBusLineSearch.searchBusLine((new BusLineSearchOption()
			.city(city).uid(busLineIDList.get(next))));
		}
	}

	/**
	 *  �ٶȵ�ͼ֮��������
	 */
	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) { 
			busLineIDList.clear();
			searchNearInfo.clear();
			for(PoiInfo info : result.getAllPoi()){ 
				if(poiBusSearch){
					if (info.type == PoiInfo.POITYPE.BUS_LINE|| info.type == PoiInfo.POITYPE.SUBWAY_LINE) {
						busLineIDList.add(info.uid);
					}
				}else if(poiNearSearch){
					double distance = DistanceOfTwoPoints(Double.valueOf(CustomApplcation.getInstance().getLatitude()) ,
						Double.valueOf(CustomApplcation.getInstance().getLongtitude() ), info.location.latitude , info.location.longitude);
					Model model = new Model(info.name,info.phoneNum,info.address,info.location,distance);
					searchNearInfo.add(model);
				}
			}
            if(poiNearSearch && !poiBusSearch){
            	BaiduMapSearchMessage.getInstance().setPoiResult(result);
    			dialog.dismiss();
    			listView.setPullLoadEnable(true);
    			initXListData();
    			Intent intent = new Intent();
    			intent.setAction("action.nearSearchPoi");
    			getActivity().sendBroadcast(intent);
            }
            if(poiBusSearch && !poiNearSearch){
            	SearchBusline();
            }
				
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			// ������ؼ����ڱ���û���ҵ���������������ҵ�ʱ�����ذ�ùؼ�����Ϣ�ĳ����б�
			String strInfo = "��";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "�ҵ����";
			Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}
	/**
	 *  �ٶȵ�ͼ֮����������ϸ��
	 */
	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		// TODO Auto-generated method stub
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(getActivity(), result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
			.show();
		}
	}
	
	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 *  �ٶȵ�ͼ֮������
	 */
	String province;
	String city = null;
	String district;
	String street;
	String streetNumber;
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "��Ǹ,δ��ȷ����ǰλ��", Toast.LENGTH_LONG)
					.show();
			return;
		}
		province = result.getAddressDetail().province;
		city = result.getAddressDetail().city;
		district = result.getAddressDetail().district;
		street = result.getAddressDetail().street;
		streetNumber = result.getAddressDetail().streetNumber;
		ShowToast(city);
	}
	
	/**
	 *  �ٶȵ�ͼ֮·��滮 �������ݳ�������
	 */
	String[] str;
	String nodeTitle;
	LatLng nodeLocation;
	List<LatLng> latLng  = new ArrayList<LatLng>();
	List<RouteInfo> routeInfo  = new ArrayList<RouteInfo>();
	
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			dialog.dismiss();
            Toast.makeText(getActivity(), "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {  
        	latLng.clear();
        	routeInfo.clear();
        	for(int i = 0; i < result.getRouteLines().size(); i++){
        		str = new String[result.getRouteLines().get(i).getAllStep().size()];
        		for(int j = 0; j < result.getRouteLines().get(i).getAllStep().size() ; j++){
        			//��ȡ�ڽ����Ϣ
                    nodeLocation = null;
                    nodeTitle = null;
                    Object step = result.getRouteLines().get(i).getAllStep().get(j);
                    if (step instanceof TransitRouteLine.TransitStep) {
                        nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation();
                        nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
                    }
                    if (nodeLocation == null || nodeTitle == null) {
                        return;
                    }
                    str[j] = nodeTitle;
                    latLng.add(nodeLocation);
        		}
        		RouteInfo info = new RouteInfo(str,result,latLng);
        		routeInfo.add(info);
        	}
        	dialog.dismiss();
        	initBusListData();
        }
	}
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			dialog.dismiss();
            Toast.makeText(getActivity(), "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	latLng.clear();
        	routeInfo.clear();
        	for(int i = 0; i < result.getRouteLines().size(); i++){
        		str = new String[result.getRouteLines().get(i).getAllStep().size()];
        		for(int j = 0; j < result.getRouteLines().get(i).getAllStep().size() ; j++){
        			//��ȡ�ڽ����Ϣ
                    nodeLocation = null;
                    nodeTitle = null;
                    Object step = result.getRouteLines().get(i).getAllStep().get(j);
                    if (step instanceof DrivingRouteLine.DrivingStep) {
                        nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation();
                        nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
                    }
                    if (nodeLocation == null || nodeTitle == null) {
                        return;
                    }
                    str[j] = nodeTitle;
                    latLng.add(nodeLocation);
        		}
        		RouteInfo info = new RouteInfo(str,result,latLng);
        		routeInfo.add(info);
        	}
        	dialog.dismiss();
        	initWalkCarListData();
        }
	}
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			dialog.dismiss();
            Toast.makeText(getActivity(), "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	latLng.clear();
        	routeInfo.clear();
        	for(int i = 0; i < result.getRouteLines().size(); i++){
        		str = new String[result.getRouteLines().get(i).getAllStep().size()];
        		for(int j = 0; j < result.getRouteLines().get(i).getAllStep().size() ; j++){
        			//��ȡ�ڽ����Ϣ
                    nodeLocation = null;
                    nodeTitle = null;
                    Object step = result.getRouteLines().get(i).getAllStep().get(j);
                    if (step instanceof WalkingRouteLine.WalkingStep) {
                        nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation();
                        nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
                    }
                    if (nodeLocation == null || nodeTitle == null) {
                        return;
                    }
                    str[j] = nodeTitle;
                    latLng.add(nodeLocation);
        		}
        		RouteInfo info = new RouteInfo(str,result,latLng);
        		routeInfo.add(info);
        	}
        	dialog.dismiss();
        	initWalkCarListData();
        }
	}
	
	/**
	 * ����·�߲�ѯ
	 */
	@Override
	public void onGetBusLineResult(BusLineResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			dialog.dismiss();
			Toast.makeText(getActivity(), "��Ǹ��δ�ҵ����",
					Toast.LENGTH_LONG).show();
			return;
		}
		// ģ���
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
		    //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
		    return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	// ��ȡ��һ·�ߵ�վ����
        	String[] str = new String[result.getStations().size()];
    		for(int i = 0;i < result.getStations().size();i++){
    			str[i] = result.getStations().get(i).getTitle();
    		}
    		RouteInfo info = new RouteInfo(result.getBusLineName() ,result.getStations().size() , str ,result);
    		routeInfo.add(info);
    		dialog.dismiss();
        	initBusListData();
        	next++;
        	SearchBusline();
		}
	}

	
	/**
	 * �������侭γ����꣨doubleֵ���������������룬
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return ���룺��λΪ��
	 */
	public static double DistanceOfTwoPoints(double lat1, double lng1,
			double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	private static final double EARTH_RADIUS = 6378137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
	
	void initXListData() {
		xBaseAdapter.clear();
		int num = searchNearInfo.size();
		for(int i = 0 ; i < num ; i++){
			Model model = searchNearInfo.get(i);
			Model model1= new Model();
			model1.setName(model.getName());
			model1.setTelephone(model.getTelephone());
			model1.setAddress(model.getAddress());
			model1.setDistance(model.getDistance());
			model1.setLatLng(model.getLatLng());
			xBaseAdapter.addModel(model1);
			xBaseAdapter.notifyDataSetChanged();
		}
	}
	
	void initWalkCarListData(){
    	aloneListView.setDividerHeight(0);
		aloneRouteAdapter = new RoutePlanAdapter(getActivity(), getWalkCarData());
		aloneListView.setAdapter(aloneRouteAdapter);
	}
	
	/**
	 *  ·���滮-������ݳ���ݶ�ȡ
	 */
	private List<Map<String, Object>> getWalkCarData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map ;
		for(int i = 0 ;i< routeInfo.size();i++){
			for(int j =0 ;j< routeInfo.get(i).getRouteInfo().length;j++){
				map = new HashMap<String, Object>();
			    map.put("title", routeInfo.get(i).getRouteInfo()[j]);
				list.add(map);
				Log.e("TAG",routeInfo.get(i).getRouteInfo()[j]);
			}
		}
		return list;
	}
	
	void initBusListData(){
		expandableListView.setVisibility(View.VISIBLE);
		expandableListView.setGroupIndicator(getResources().getDrawable( R.drawable.clicklist_expand));
		expandableAdapter = new BusExpandAdapter(getActivity(), getBusData());
		expandableListView.setAdapter(expandableAdapter);
		expandableListView.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
		expandableListView.setOnChildClickListener(this);
	}
	
	/**
	 *  ·���滮-������ݳ���ݶ�ȡ
	 */
	private List<List<RouteInfo>>  getBusData() {
		List<List<RouteInfo>> busRouteList = new ArrayList<List<RouteInfo>>();
		List<RouteInfo> list;
		for(int i = 0 ;i< routeInfo.size();i++){
			list = new ArrayList<RouteInfo>();
			for(int j =0 ;j < routeInfo.get(i).getRouteInfo().length;j++){
				RouteInfo item = new RouteInfo(routeInfo.get(i).getRouteInfo()[j]);
				list.add(item);
			}
			busRouteList.add(list);
		}
		return busRouteList;
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**      ��̬���ú���          */
	
	/**
	 * ·������
	 */
	public void showRouteSearch(){
		xBaseAdapter.clear();
    	listView.setPullLoadEnable(false);
		llAllLikeTitle.setVisibility(View.GONE);
		llAllLikeChoss.setVisibility(View.GONE);
		llAllLikeTextTitle.setVisibility(View.GONE);
		ll_route_search.setVisibility(View.VISIBLE);
	}
	
	public void showBusSearch(){
		poiBusSearch = true;
		poiNearSearch= false;
		xBaseAdapter.clear();
    	listView.setPullLoadEnable(false);
		llAllLikeTitle.setVisibility(View.GONE);
		llAllLikeChoss.setVisibility(View.GONE);
		llAllLikeTextTitle.setVisibility(View.GONE);
		ll_search.setVisibility(View.VISIBLE);
	}
	
	/**
	 * �ڶ��η�����
	 */
	public void againGetReverseGeoCodeResult(){
		ptCenter = new LatLng(Double.valueOf(CustomApplcation.getInstance().getLatitude()).doubleValue(),
				Double.valueOf(CustomApplcation.getInstance().getLongtitude()).doubleValue());
		geoReverseCoder.reverseGeoCode(new ReverseGeoCodeOption()
		.location(ptCenter));		
	}
	
	public void showNearSearch(){
		poiBusSearch = false;
		poiNearSearch= true;
		xBaseAdapter.clear();
    	listView.setPullLoadEnable(false);
		llAllLikeTitle.setVisibility(View.GONE);
		llAllLikeChoss.setVisibility(View.GONE);
		llAllLikeTextTitle.setVisibility(View.GONE);
		ll_search.setVisibility(View.VISIBLE);
	}

	/**
	 *  �㲥�¼�
	 */ 
	public void initBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.nearSearchPoiRoute");
		getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}
	
	/**
	 *  �㲥����
	 */
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("action.nearSearchPoiRoute")) {
				showRouteSearch();
				Model model = BaiduMapSearchMessage.getInstance().getModel();
				PlanNode stNode = PlanNode.withLocation(ptCenter);
				PlanNode enNode= PlanNode.withLocation(new LatLng(model.getLatLng().latitude,model.getLatLng().longitude));
				b_imgwalk = true;
				b_imgbus = b_imgcar = false;
				searchRoute(b_imgbus,b_imgcar,b_imgwalk,stNode,enNode);
			}
		}
	};	
}
