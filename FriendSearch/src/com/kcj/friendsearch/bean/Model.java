package com.kcj.friendsearch.bean;


import com.baidu.mapapi.model.LatLng;

/**
 * @ClassName: Model
 * @Description: ������������������
 * @author kcj
 * @date 
 */
public class Model {
	
	// ��ȡ poi ��ַ
	String  address;   
	// ��ȡ poi ǩ������
	int	checkinNum;
	//��ȡ poi ��������
	int	commentNum;
	//��ȡ poi ���� url
	String	detailUrl;
	//��ȡ poi ��������
	double	environmentRating;
	//��ȡ poi ��ʩ����
	double	facilityRating;
	//��ȡ poi ϲ������
	int	favoriteNum;
	//��ȡ poi �Ź�����
	//int	grouponNum;
	//��ȡ poi ��������
	//double	hygieneRating;
	//��ȡ poi ͼƬ����
	//int	imageNum;
	//��ȡ poi λ��
	LatLng	location;
	//��ȡ poi ����
	String	name;
	//��ȡ poi �ۺ�����
	double	overallRating;
	//��ȡ poi �۸�
	double	price;
	//��ȡ poi ��������
	//double	serviceRating;
	//��ȡ poi Ӫҵʱ��
	//String	shopHours;
	//��ȡ poi ��ǩ
	//String	tag;
	//��ȡ poi ��ζ����
	//double	tasteRating;
	//��ȡ poi ��������
	//double	technologyRating;
	//��ȡ poi �绰
	String	telephone;
	//��ȡ poi ���ͣ� "hotel", "cater", "life"
	//String	type;
	//��ȡ poi �� uid
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
