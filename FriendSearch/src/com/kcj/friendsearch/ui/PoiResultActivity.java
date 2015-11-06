package com.kcj.friendsearch.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.adapter.XBaseAdapter;
import com.kcj.friendsearch.bean.Model;
import com.kcj.friendsearch.view.HeaderLayout.onRightImageButtonClickListener;
import com.kcj.friendsearch.view.list.XListView;
import com.kcj.friendsearch.view.list.XListView.IXListViewListener;
import com.xiapu.whereisfriend.R;

public class PoiResultActivity extends BaseActivity implements
OnGetPoiSearchResultListener  , IXListViewListener ,OnGetShareUrlResultListener{
	XListView listView;

	private XBaseAdapter xBaseAdapter;
	public static PoiResultActivity poiresule;
	List<Model> searchInfo=  new ArrayList<Model>();
	private ProgressDialog dialog;
	private PoiSearch mPoiSearch = null;
	private int pagenum = 0;
	private String headline;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poiresult);
		headline = getIntent().getStringExtra("headline");
		if(!TextUtils.isEmpty(headline)){
			initView();
			initSearch();
			searchNear();
		}
	}
	
	public void initView(){
		poiresule = this;
		if(headline.equals("��") || headline.equals("ʳ") || headline.equals("ס") ||headline.equals("��") ){
			initTopBarForBoth(headline, R.drawable.base_action_bar_add_bg_selector,
					new onRightImageButtonClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(headline.equals("��")){
								clothingDialog();
							}else if(headline.equals("ʳ")){
								edibleDialog();
							}
					        else if(headline.equals("ס")){
					        	liveDialog();
							}
					        else if(headline.equals("��")){
					        	lineDialog();
					        }
						}
					});
		}
		else{
			initTopBarForLeft(headline);
		}
		
	}
	
	public void initListview(){
		listView = (XListView) findViewById(R.id.listView);
		xBaseAdapter = new XBaseAdapter(this, R.layout.xlistview_item,this);
		listView.setAdapter(xBaseAdapter);
		listView.setXListViewListener(this);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
	}
	
	String str = null;
	/**
	 *  ��ʼ������
	 */
	public void initSearch(){
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
	}
	
	public void searchNear(){
		if(headline.equals("��")){
			str = "����";
			//img_background.setImageResource(R.drawable.near_clothing);
		}else if(headline.equals("ʳ")){
			str = "����";
			//img_background.setImageResource(R.drawable.near_eat);
		}
        else if(headline.equals("ס")){
        	str = "�Ƶ�";
		}
        else if(headline.equals("��")){
        	str = "����վ";
        }
        else {
        	str = headline;
        }
		search(str);
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		// TODO Auto-generated method stub
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PoiResultActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(PoiResultActivity.this, result.getName() + " : " + result.getAddress(), Toast.LENGTH_SHORT).show();
			//Log.e("PoiDetailResult",result.getImage() + "  ��"+  result.getDetailUrl());
		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			setPoiResult(result);
			searchInfo.clear();
			for(PoiInfo info : result.getAllPoi()){ 
				double distance = DistanceOfTwoPoints(Double.valueOf(CustomApplcation.getInstance().getLatitude()) ,
						Double.valueOf(CustomApplcation.getInstance().getLongtitude() ), info.location.latitude , info.location.longitude);
				Model model = new Model(info.name,info.phoneNum,info.address,info.location,distance);
				searchInfo.add(model);
			}
			dialog.dismiss();
			initListview();
			initXListData();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			String strInfo = "��";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "�ҵ����";
			Toast.makeText(PoiResultActivity.this, strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}
	
	@Override
	public void onGetLocationShareUrlResult(ShareUrlResult arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(PoiResultActivity.this, "0", Toast.LENGTH_LONG)
		.show();
	}

	@Override
	public void onGetPoiDetailShareUrlResult(ShareUrlResult arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(PoiResultActivity.this, "1", Toast.LENGTH_LONG)
		.show();
	}
	
	/**
	 * ��ѯ���
	 */
	public void search(String choice){
		dialog=new ProgressDialog(PoiResultActivity.this);
		dialog.setMessage("������");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		//poi��ѯ
		mPoiSearch.searchNearby((new PoiNearbySearchOption()
		. keyword(choice)
		.location(new LatLng( Double.valueOf(CustomApplcation.getInstance().getLatitude()) , 
				Double.valueOf(CustomApplcation.getInstance().getLongtitude()))).pageCapacity(10)
		.pageNum(pagenum)).radius(10000));
	}
	
	void initXListData() {
		int num = searchInfo.size();
		for(int i = 0 ; i < num ; i++){
			Model model = searchInfo.get(i);
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
		loadMoreInBackground();
	}
	
	void loadMoreInBackground() {
		//xBaseAdapter.clear();
		pagenum++;
		search(str);
		listView.setSelection(xBaseAdapter.getCount() - 1);// ������ƶ������صĽ��紦
		onLoad();
	}
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("�ո�");
	}
	
	/**
	 *  ��
	 */
	private void clothingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(PoiResultActivity.this);
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "�̳�", "����", "���", "�г�" },
				0, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							str = "�̳�";
						} else if (which == 1) {
							str = "����";
						}else if (which == 2) {
							str = "���";
						}else if (which == 3) {
							str = "�г�";
						}
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				xBaseAdapter.clear();
				search(str);
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	/**
	 *  ʳ
	 */
	private void edibleDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(PoiResultActivity.this);
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "����", "�в���", "�����", "������",
				"���ⷿ", "�ϵ»�", "����", "��ʤ��", "�����" }, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							str = "����";
						} else if (which == 1) {
							str = "�в���";
						}else if (which == 2) {
							str = "�����";
						}else if (which == 3) {
							str = "������";
						}else if (which == 4) {
							str = "���ⷿ";
						}else if (which == 5) {
							str = "�ϵ»�";
						}else if (which == 6) {
							str = "����";
						}else if (which == 7) {
							str = "��ʤ��";
						}else if (which == 8) {
							str = "�����";
						}
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				xBaseAdapter.clear();
				search(str);
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	/**
	 *  ס
	 */
	private void liveDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(PoiResultActivity.this);
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "����", "�Ƶ�", "����", "�ù�"},
				0, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							str = "����";
						} else if (which == 1) {
							str = "�Ƶ�";
						}else if (which == 2) {
							str = "����";
						}else if (which == 3) {
							str = "�ù�";
						}
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				xBaseAdapter.clear();
				search(str);	
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	/**
	 *  ��
	 */
	private void lineDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(PoiResultActivity.this);
		builder.setTitle("��ѯ");
		builder.setSingleChoiceItems(new String[] { "��", "��վ", "����վ", "ͣ����"
				, "���վ", "����վ", "��;��վ", "��Ʊ���۵�", "�ɻ�Ʊ���۵�", "��ӰԺ", "KTV", "ϴԡ", "���"
				, "̨����", "�ư�", "���", "����" ,"��԰", },
				0, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							str = "��";
						} else if (which == 1) {
							str = "��վ";
						}else if (which == 2) {
							str = "����վ";
						}else if (which == 3) {
							str = "ͣ����";
						}else if (which == 4) {
							str = "���վ";
						}else if (which == 5) {
							str = "����վ";
						}else if (which == 6) {
							str = "��;��վ";
						}else if (which == 7) {
							str = "��Ʊ���۵�";
						}else if (which == 8) {
							str = "�ɻ�Ʊ���۵�";
						}else if (which == 9) {
							str = "��ӰԺ";
						}else if (which == 10) {
							str = "KTV";
						}else if (which == 11) {
							str = "ϴԡ";
						}else if (which == 12) {
							str = "���";
						}else if (which == 13) {
							str = "̨����";
						}else if (which == 14) {
							str = "�ư�";
						}else if (which == 15) {
							str = "���";
						}else if (which == 16) {
							str = "����";
						}else if (which == 17) {
							str = "��԰";
						}
					}
				});
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				xBaseAdapter.clear();
				search(str);
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	
	public static PoiResultActivity getInstance() {
		return poiresule;
	}
	
	public PoiSearch getPoiSearch(){
		return mPoiSearch;
	}
	
	/**
	 *  ����poi��Χ������
	 */
	PoiResult result = null;
	public void setPoiResult(PoiResult result){
		this.result = result;
	}
	public PoiResult getPoiResult(){
		return result;
	}
	
	/**
	 *  ��ǰѡ������
	 */
	LatLng	location;
	public LatLng getLatLng() {
		return location;
	}
	public void setLatLng(LatLng getLocation) {
		this.location = getLocation;
	}
	
	/**
	 *  ��ǰѡ�������
	 */
	String 	name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 *  ��ͼ��ʾ����
	 */
	public void showPoiSearch(){
		MainActivity.getInstance().setIndex(0);
		MainActivity.getInstance().setNearSearch(true);
		startAnimActivity(MainActivity.class);
		Intent intent = new Intent();
		intent.setAction("action.nearMapLocation");
		sendBroadcast(intent);
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
}
