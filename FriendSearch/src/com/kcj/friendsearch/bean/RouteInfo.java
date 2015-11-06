package com.kcj.friendsearch.bean;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * @ClassName: RouteInfo
 * @Description: Â·¾¶ËÑË÷Êý¾Ý´æ´¢
 * @author kcj
 * @date 
 */
public class RouteInfo {
	public String name;
	public String node;
	public String[] str;
	LatLng nodeLocation;
	public TransitRouteResult busresult;
	public DrivingRouteResult driresult;
	public WalkingRouteResult walkresult;
	List<LatLng> latLng = new ArrayList<LatLng>();
	
	int busLineSize;
	BusLineResult result;
	
	public RouteInfo(String[] str ,TransitRouteResult busresult ,List<LatLng> latLng ){
		this.busresult = busresult;
		this.str = str;
		this.latLng = latLng;

	}
	
	public RouteInfo(String[] str ,DrivingRouteResult driresult,List<LatLng> latLng){
		this.driresult = driresult;
		this.str = str;
		this.latLng = latLng;
	}
	
	public RouteInfo(String[] str ,WalkingRouteResult walkresult,List<LatLng> latLng){
		this.walkresult = walkresult;
		this.str = str;
		this.latLng = latLng;
	}
	
	public RouteInfo( String busLineName ,int busLineSize ,String[] str ,BusLineResult result) {
		this.name = busLineName;
		this.busLineSize = busLineSize;
		this.str = str;
		this.result = result;
	}
	
	public RouteInfo(String node){
		//this.node = node;
		this.node = node;
	}
	
	public RouteInfo(String name,String node){
		this.name = name;
		this.node = node;
	}
	
	public String[] getRouteInfo(){
		return str;
	}
	
	public String getNode(){
		return node;
	}
	
	public void setLatLngList(List<LatLng> latLng) {
		this.latLng = latLng;
	}

	public List<LatLng> getLatLngList() {
		return latLng;
	}
	
	public void setLatLng(LatLng nodeLocation) {
		this.nodeLocation = nodeLocation;
	}

	public LatLng getLatLng() {
		return nodeLocation;
	}
	
	public void setTransitRouteResult(TransitRouteResult busresult) {
		this.busresult = busresult;
	}

	public TransitRouteResult getTransitRouteResult() {
		return busresult;
	}
	
	public void setDrivingRouteResult(DrivingRouteResult driresult) {
		this.driresult = driresult;
	}

	public DrivingRouteResult getDrivingRouteResult() {
		return driresult;
	}
	
	public void setWalkingRouteResult(WalkingRouteResult walkresult) {
		this.walkresult = walkresult;
	}

	public WalkingRouteResult getWalkingRouteResult() {
		return walkresult;
	}

}
