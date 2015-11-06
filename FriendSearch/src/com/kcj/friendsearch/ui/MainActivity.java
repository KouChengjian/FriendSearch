package com.kcj.friendsearch.ui;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.kcj.friendsearch.MyMessageReceiver;
import com.kcj.friendsearch.bean.BusRoute;
import com.kcj.friendsearch.bean.RouteInfo;
import com.kcj.friendsearch.confi.BaiduMapSearchMessage;
import com.kcj.friendsearch.ui.fragment.ContactFragment;
import com.kcj.friendsearch.ui.fragment.LocationFargment;
import com.kcj.friendsearch.ui.fragment.MapSearchFragment;
import com.kcj.friendsearch.ui.fragment.MapShowFragment;
import com.kcj.friendsearch.ui.fragment.NearbyFragment;
import com.kcj.friendsearch.view.TitleBarView;
import com.xiapu.whereisfriend.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;



public class MainActivity extends SlidingFragmentActivity implements
		EventListener {
	private Button[] mTabs;
	// 三个界面
	private LocationFargment locationFragment;
	private ContactFragment contactFragment;
	private NearbyFragment nearbyFragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	// 隐藏菜单
    LinearLayout hideMenu; 
    
    BaiduMapSearchMessage baiduMesssage;
    public static MainActivity mainControlFargment ;
    private TitleBarView mTitleBarView;
    private MapShowFragment mapShowFragment;
    private MapSearchFragment mapSearchFragment;
    // popupWindow
    private View mPopView;
    private PopupWindow mPopupWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化sliding Sliding
		initSlidingMenu();
		// 初始化fargment
		initFargment();
		// 初始化标题
		initMapTitle();
		// 初始化控件
		//initView();
	}
	
	private void initFargment() {
		mainControlFargment = this;
		mapShowFragment = new MapShowFragment();
	    mapSearchFragment = new MapSearchFragment();
	    fragments = new Fragment[] { mapShowFragment, mapSearchFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, mapShowFragment)
				.add(R.id.fragment_container, mapSearchFragment)
				.hide(mapSearchFragment).show(mapShowFragment).commit();
	}
	
	private ImageView mChats, mShare, mCamera, mScan;
	
	private void initMapTitle() {
		// 公共
		baiduMesssage = new BaiduMapSearchMessage();
		// popup
		LayoutInflater inflater = LayoutInflater.from(this);
		mPopView = inflater.inflate(R.layout.fragment_news_pop, null);
		mChats = (ImageView) mPopView.findViewById(R.id.pop_chat);
		mShare = (ImageView) mPopView.findViewById(R.id.pop_sangzhao);
		mCamera = (ImageView) mPopView.findViewById(R.id.pop_camera);
		mScan = (ImageView) mPopView.findViewById(R.id.pop_scan);
		mTitleBarView=(TitleBarView) findViewById(R.id.common_actionbar);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.VISIBLE,
				View.VISIBLE);
		mTitleBarView.getTitleLeft().setEnabled(false);
		mTitleBarView.setBtnRight(R.drawable.skin_conversation_title_right_btn);
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTitleBarView.setPopWindow(mPopupWindow, mTitleBarView);
			}
		});
		
		mTitleBarView.setBtnLeft(R.drawable.home_page_heard_title_left_n,0);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		
		mTitleBarView.getTitleLeft().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showMapFargment();
			}
		});

		mTitleBarView.getTitleRight().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSearchFargment();
			}
		});
		
		mPopupWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mTitleBarView.setBtnRight(R.drawable.skin_conversation_title_right_btn);
			}
		});
		
		mChats.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		mShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSearchFargment();
				BaiduMapSearchMessage.getInstance().getMapSearchFragment().showBusSearch();
				mPopupWindow.dismiss();
			}
		});
		mCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSearchFargment();
				BaiduMapSearchMessage.getInstance().getMapSearchFragment().showRouteSearch();
				mPopupWindow.dismiss();
			}
		});
		mScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSearchFargment();
				BaiduMapSearchMessage.getInstance().getMapSearchFragment().showNearSearch();
				mPopupWindow.dismiss();
			}
		});
	}
	
	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu() {
		// 设置滑动菜单的视图
		setBehindContentView(R.layout.main_left_menuframe);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new ContactFragment()).commit();
		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动阴影的图像资源
		sm.setShadowDrawable(R.drawable.tab_main_menu_show);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值 0.35f
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}
	
	public void showMapFargment(){
		if (mTitleBarView.getTitleLeft().isEnabled()) {
			mTitleBarView.getTitleLeft().setEnabled(false);
			mTitleBarView.getTitleRight().setEnabled(true);
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[1]);
			if (!fragments[0].isAdded()) {
				trx.add(R.id.fragment_container, fragments[0]);
			}
			trx.show(fragments[0]).commit();
		}
	}
	
    public void showSearchFargment(){
    	if (mTitleBarView.getTitleRight().isEnabled()) {
			mTitleBarView.getTitleLeft().setEnabled(true);
			mTitleBarView.getTitleRight().setEnabled(false);
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[0]);
			if (!fragments[1].isAdded()) {
				trx.add(R.id.fragment_container, fragments[1]);
			}
			trx.show(fragments[1]).commit();
		}
	}

	private void initView() {
		hideMenu = (LinearLayout)findViewById(R.id.main_bottom);
		mTabs = new Button[3];
		mTabs[0] = (Button) findViewById(R.id.btn_message);
		mTabs[1] = (Button) findViewById(R.id.btn_contract);
		mTabs[2] = (Button) findViewById(R.id.btn_set);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
	}

	private void initTab() {
		locationFragment = new LocationFargment();
		contactFragment = new ContactFragment();
		nearbyFragment = new NearbyFragment();

		fragments = new Fragment[] { locationFragment, contactFragment,
				nearbyFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, locationFragment)
				.add(R.id.fragment_container, contactFragment)
				.hide(contactFragment).show(locationFragment).commit();
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 *            kcj 按钮切换
	 */
	public void onTabSelect(View view) {
		switch (view.getId()) {
		case R.id.btn_message:
			index = 0;
			break;
		case R.id.btn_contract:
			index = 1;
			break;
		case R.id.btn_set:
			index = 2;
			break;
		}
		fragmentCurrentShow();
	}
	
	public void fragmentCurrentShow(){
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	public void showMenu() {
		toggle();
	}

	public static MainActivity getInstance() {
		return mainControlFargment;
	}

	// 开始
	@Override
	public void onResume() {
		super.onResume();
		//fragmentCurrentShow();
		if (BmobDB.create(this).hasNewInvite()) {
		} else {
		}
		MyMessageReceiver.ehList.add(this);// 监听推送的消息
		// 清空
		MyMessageReceiver.mNewNum = 0;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MyMessageReceiver.ehList.remove(this);// 取消监听推送的消息
	}

	@Override
	public void onAddUser(BmobInvitation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(BmobMsg arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetChange(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOffline() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReaded(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}
	
	/* 切换到 */
	public void setIndex(int index){
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
    
	public void hideLayoutMenu(){
		hideMenu.setVisibility(View.GONE);
		hideMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_out));
	}
	
	public void showLayoutMenu(){
		hideMenu.setVisibility(View.VISIBLE);
	}
	
	/*------------------ 公交搜索地图显示 ------------------------------*/
	List<BusRoute> busList= new ArrayList<BusRoute>();
	
	public List<BusRoute> getBusRouteList(){
		return busList;
	} 
	// 保存驾车/步行路线数据的变量，供浏览节点时使用
	private BusLineResult route = null;
	public  BusLineResult getBusLineResult(){
		return route;
	} 
	public void setBusLineResult(BusLineResult route){
		this.route = route;
	} 
	// 点击公交位置
	String pitchOnTitle; // 名称节点
	int groupPosition;
	int childPosition;
	public void setPitchOnTitle(String pitchOnTitle){
		this.pitchOnTitle = pitchOnTitle;
	}
	public String getPitchOnTitle(){
		return pitchOnTitle;
	}
	public void setgroupPosition(int groupPosition){
		this.groupPosition = groupPosition;
	}
	public int getgroupPosition(){
		return groupPosition;
	}
	public void setchildPosition(int childPosition){
		this.childPosition = childPosition;
	}
	public int getchildPosition(){
		return childPosition;
	}
	/*---------------------- end ----------------------------*/
	
	
	
	/*------------------ 路径规划 ------------------------------*/
	List<RouteInfo> routeList = new ArrayList<RouteInfo>();
	// 路径 
	public void setRouteList(List<RouteInfo> routeList){
		this.routeList = routeList;
	}
	public List<RouteInfo> getRouteList(){
		return routeList;
	}
	/*---------------------- end -----------------------------*/
	
	/*---------------- 附近----------------------------------- */
	public boolean nearSearch = false;
	
	public boolean getNearSearch(){
		return nearSearch;
	}
	
	public void setNearSearch(boolean nearSearch){
		this. nearSearch = nearSearch; 
	}
	/*---------------------- end -----------------------------*/
	
	/*---------------- 好友----------------------------------- */
	public boolean nearFriendSearch = false;
	
	public boolean getFriendSearch(){
		return nearFriendSearch;
	}
	public void setFriendSearch(boolean nearFriendSearch){
		this. nearFriendSearch = nearFriendSearch; 
	}
	
	LatLng	location;
	public LatLng getLatLng() {
		return location;
	}
	public void setLatLng(LatLng getLocation) {
		this.location = getLocation;
	}
	/*---------------------- end -----------------------------*/
}
