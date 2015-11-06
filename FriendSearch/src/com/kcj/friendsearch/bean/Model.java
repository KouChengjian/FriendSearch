package com.kcj.friendsearch.bean;


import com.baidu.mapapi.model.LatLng;

/**
 * @ClassName: Model
 * @Description: 附近搜索适配数据类
 * @author kcj
 * @date 
 */
public class Model {
	
	// 获取 poi 地址
	String  address;   
	// 获取 poi 签到数量
	int	checkinNum;
	//获取 poi 评论数量
	int	commentNum;
	//获取 poi 详情 url
	String	detailUrl;
	//获取 poi 环境评价
	double	environmentRating;
	//获取 poi 设施评价
	double	facilityRating;
	//获取 poi 喜欢数量
	int	favoriteNum;
	//获取 poi 团购数量
	//int	grouponNum;
	//获取 poi 卫生评价
	//double	hygieneRating;
	//获取 poi 图片数量
	//int	imageNum;
	//获取 poi 位置
	LatLng	location;
	//获取 poi 名称
	String	name;
	//获取 poi 综合评价
	double	overallRating;
	//获取 poi 价格
	double	price;
	//获取 poi 服务评价
	//double	serviceRating;
	//获取 poi 营业时间
	//String	shopHours;
	//获取 poi 标签
	//String	tag;
	//获取 poi 口味评价
	//double	tasteRating;
	//获取 poi 技术评价
	//double	technologyRating;
	//获取 poi 电话
	String	telephone;
	//获取 poi 类型， "hotel", "cater", "life"
	//String	type;
	//获取 poi 的 uid
	//String	uid;
	
	double distance;

	
	public Model(){
	}
	
	public Model(LatLng location , double distance){
		this.location = location;
		this.distance = distance;
	}
	
	public Model (String name , String	telephone ,String  address,LatLng location,double distance){
		this.name = name;
		this.telephone = telephone;
		this.address = address;
		this.location = location;
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public LatLng getLatLng() {
		return location;
	}
	public void setLatLng(LatLng getLocation) {
		this.location = getLocation;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getCheckinNum() {
		return checkinNum;
	}
	public void setCheckinNum(int checkinNum) {
		this.checkinNum = checkinNum;
	}
	
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	
	public double getEnvironmentRating() {
		return environmentRating;
	}
	public void setEnvironmentRating(double environmentRating) {
		this.environmentRating = environmentRating;
	}
	
	public double getFacilityRating() {
		return facilityRating;
	}
	public void setFacilityRating(double facilityRating) {
		this.facilityRating = facilityRating;
	}
	
	public int getFavoriteNum() {
		return favoriteNum;
	}
	public void setFavoriteNum(int favoriteNum) {
		this.favoriteNum = favoriteNum;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public double getOverallRating() {
		return overallRating;
	}
	public void setOverallRating(double overallRating) {
		this.overallRating = overallRating;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	
}
