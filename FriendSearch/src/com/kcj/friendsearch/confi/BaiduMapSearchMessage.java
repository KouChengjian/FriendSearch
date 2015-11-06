package com.kcj.friendsearch.confi;

import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.kcj.friendsearch.bean.Model;
import com.kcj.friendsearch.ui.fragment.MapSearchFragment;


public class BaiduMapSearchMessage {
	public static BaiduMapSearchMessage baiDuMapSearchMessage;
	public BaiduMapSearchMessage(){
		baiDuMapSearchMessage = this;
	}
	public static BaiduMapSearchMessage getInstance() {
		return baiDuMapSearchMessage;
	}
	
	Model model;
	PoiResult result;
	PoiSearch mPoiSearch;
	MapSearchFragment mapSearchFragment;
	public void setModel(Model model){
		this.model = model;
	}
	public Model getModel(){
		return model;
	}
	
	public void setPoiSearch(PoiSearch mPoiSearch){
		this.mPoiSearch = mPoiSearch;
	}
	public PoiSearch getPoiSearch(){
		return mPoiSearch;
	}
	
	public void setPoiResult(PoiResult result){
		this.result = result;
	}
	public PoiResult getPoiResult(){
		return result;
	}
	
	public void setMapSearchFragment(MapSearchFragment mapSearchFragment){
		this.mapSearchFragment = mapSearchFragment;
	}
	public MapSearchFragment getMapSearchFragment(){
		return mapSearchFragment;
	}
}
