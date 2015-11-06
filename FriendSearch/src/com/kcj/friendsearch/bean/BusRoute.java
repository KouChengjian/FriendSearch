package com.kcj.friendsearch.bean;

import com.baidu.mapapi.search.busline.BusLineResult;

/**
 * @ClassName: BusRoute
 * @Description: 公交搜索时数据存储
 * @author kcj
 * @date 
 */
public class BusRoute {

	public String busRoute;
	public int busRoutenum;
	public BusLineResult result;
	public String[] busRouteName;

	public BusRoute(String busRoute ) {
		this.busRoute = busRoute;
	}
	
	public BusRoute(String busRoute ,int busRoutenum ,String[] busRouteName,BusLineResult result) {
		this.busRoute = busRoute;
		this.busRoutenum = busRoutenum;
		this.busRouteName = busRouteName;
		this.result = result;
	}

	public void setBusRoute(String busRoute) {
		this.busRoute = busRoute;
	}

	public String getBusRoute() {
		return busRoute;
	}
	
	public void setBusRouteNum(int busRoutenum) {
		this.busRoutenum = busRoutenum;
	}

	public int getBusRouteNum() {
		return busRoutenum;
	}
	
	public void setBusRouteName(String[] busRouteName) {
		this.busRouteName = busRouteName;
	}

	public String[] getBusRouteName() {
		return busRouteName;
	}
	
	public void setBusLineResult(BusLineResult result) {
		this.result = result;
	}

	public BusLineResult getBusLineResult() {
		return result;
	}
}
