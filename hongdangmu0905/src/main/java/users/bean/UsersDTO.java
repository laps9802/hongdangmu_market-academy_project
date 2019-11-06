package users.bean;

public class UsersDTO {
	private int user_code;		             //-- 사용자 코드	
    private String user_name;		          //-- 사용자 이름
    private String user_photo;                  //-- 프로필 사진
    private String user_area;                   //-- 지역 
    private int manner;                   //-- 평점	
    private int reply_percent;             //-- 응답률
    private int sales_count;              //-- 판매 개수
    private int purchase_count;           //-- 구매 개수
    private int interest_count;           //-- 관심목록 개수	
    private String join_date;                   //-- 가입 날짜
    private String user_tel;                    //-- 사용자 전화번호
    private String user_email1;                  // -- 사용자 이메일1
    private String user_email2;                  // -- 사용자 이메일2
    private double lat;                             //-- 위도       
    private double lng;                             //-- 경도
	public int getUser_code() {
		return user_code;
	}
	public void setUser_code(int user_code) {
		this.user_code = user_code;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_photo() {
		return user_photo;
	}
	public void setUser_photo(String user_photo) {
		this.user_photo = user_photo;
	}
	public String getUser_area() {
		return user_area;
	}
	public void setUser_area(String user_area) {
		this.user_area = user_area;
	}
	public int getManner() {
		return manner;
	}
	public void setManner(int manner) {
		this.manner = manner;
	}
	public int getReply_percent() {
		return reply_percent;
	}
	public void setReply_percent(int reply_percent) {
		this.reply_percent = reply_percent;
	}
	public int getSales_count() {
		return sales_count;
	}
	public void setSales_count(int sales_count) {
		this.sales_count = sales_count;
	}
	public int getPurchase_count() {
		return purchase_count;
	}
	public void setPurchase_count(int purchase_count) {
		this.purchase_count = purchase_count;
	}
	public int getInterest_count() {
		return interest_count;
	}
	public void setInterest_count(int interest_count) {
		this.interest_count = interest_count;
	}
	public String getJoin_date() {
		return join_date;
	}
	public void setJoin_date(String join_date) {
		this.join_date = join_date;
	}
	public String getUser_tel() {
		return user_tel;
	}
	public void setUser_tel(String user_tel) {
		this.user_tel = user_tel;
	}
	public String getUser_email1() {
		return user_email1;
	}
	public void setUser_email1(String user_email1) {
		this.user_email1 = user_email1;
	}
	public String getUser_email2() {
		return user_email2;
	}
	public void setUser_email2(String user_email2) {
		this.user_email2 = user_email2;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
    
}
