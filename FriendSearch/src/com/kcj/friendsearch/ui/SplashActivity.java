package com.kcj.friendsearch.ui;

import cn.bmob.im.BmobChat;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.confi.Config;
import com.xiapu.whereisfriend.R;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity{

	private LocationClient mLocationClient;  // ��λ��ȡ��ǰ�û��ĵ���λ��
	private static final int GO_HOME = 100;  
	private static final int GO_LOGIN = 200;  
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// BmobIM SDK��ʼ��--ֻ��Ҫ��һ�δ��뼴����ɳ�ʼ��
		// �뵽Bmob����(http://www.bmob.cn/)����ApplicationId,�����ַ:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
		BmobChat.getInstance(this).init(Config.applicationId);
		// �ٶȵ�ͼ������λ
		initLocClient();
		// ע�� SDK �㲥������
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
		if (userManager.getCurrentUser() != null) {
			// ÿ���Զ���½��ʱ�����Ҫ�����µ�ǰλ�úͺ��ѵ����ϣ���Ϊ���ѵ�ͷ���ǳ�ɶ���Ǿ����䶯�� SDK
			updateUserInfos();
			mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
		}
	}
	
	private SDKReceiver mReceiver;
	
	public void initLocClient(){
		mLocationClient = CustomApplcation.getInstance().mLocationClient;
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				startAnimActivity(MainActivity.class);
				finish();
				break;
			case GO_LOGIN:
			    startAnimActivity(LoginActivity.class);
			    finish();
			    break;
			}
		}
	};
	
	/**
	 * ����㲥�����࣬���� SDK key ��֤�Լ������쳣�㲥
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				ShowToast("key ��֤����! ���� AndroidManifest.xml �ļ��м�� key ����");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				ShowToast("�������");
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 *  ����
	 */
	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
