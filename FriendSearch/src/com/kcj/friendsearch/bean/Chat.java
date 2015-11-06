package com.kcj.friendsearch.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class Chat extends BmobChatUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * µØÀí×ø±ê
	 */
	private BmobGeoPoint location;//
	
	
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	
}
