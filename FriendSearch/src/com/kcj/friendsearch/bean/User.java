package com.kcj.friendsearch.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/** ����BmobChatUser����������������Ҫ���ӵ����Կ��ڴ����
 * @ClassName: TextUser
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 ����6:15:45
 */
public class User extends BmobChatUser{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ��������
	 */
	private BmobGeoPoint location;//
	
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	
	/**
	 *  �µľ���
	 */
	private double latitude;//
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 *  �µ�γ��
	 */
	private double longitude;//
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 *  �Ƿ���������֪����ǰλ��   Ĭ��Ϊ���Ե�
	 */
	public boolean isAllowFriendKnowLocation = true;
	
	public boolean getAllowFriendKnowLocation() {
		return isAllowFriendKnowLocation;
	}
	public void setAllowFriendKnowLocation(boolean isAllowFriendKnowLocation) {
		this.isAllowFriendKnowLocation = isAllowFriendKnowLocation;
	}
	
	/**
	 * ��ʾ����ƴ��������ĸ
	 */
	private String sortLetters;
	
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
