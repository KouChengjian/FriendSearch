package com.kcj.friendsearch.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/** 重载BmobChatUser对象：若还有其他需要增加的属性可在此添加
 * @ClassName: TextUser
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 下午6:15:45
 */
public class User extends BmobChatUser{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 地理坐标
	 */
	private BmobGeoPoint location;//
	
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	
	/**
	 *  新的经度
	 */
	private double latitude;//
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 *  新的纬度
	 */
	private double longitude;//
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 *  是否允许被好友知道当前位置   默认为可以的
	 */
	public boolean isAllowFriendKnowLocation = true;
	
	public boolean getAllowFriendKnowLocation() {
		return isAllowFriendKnowLocation;
	}
	public void setAllowFriendKnowLocation(boolean isAllowFriendKnowLocation) {
		this.isAllowFriendKnowLocation = isAllowFriendKnowLocation;
	}
	
	/**
	 * 显示数据拼音的首字母
	 */
	private String sortLetters;
	
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
