package goods_board.bean;

public class Goods_boardDTO {
	private int num;           //-- 글 번호
    private String image0;       //-- 상품 사진
    private String image1;       //-- 상품 사진
    private String image2;       //-- 상품 사진
    private String user_name;                       //-- 사용자 닉네임
	private String subject;   //-- 글 제목
    private String category_code;    //-- 물품분류
    private String area;             //-- 판매 지역
    private int price;            //--상품 가격
    private String content;  //-- 글 내용
    private int reply_count;          // -- 댓글 갯수
    private int interest_count;        //-- 관심 갯수
    private int readcount;       //-- 조회수
    private double lat;                             //-- 위도            
    private double lng;                             //-- 경도
    private double dist;                             //-- 거
    private String board_date;                         //-- 작성일
    private String sell_tf;		// 판매중(false) or 판매완료(true)
    private String hide_tf;		// 다시보이기(false) or 숨기기(true)
    private String review_tf;	// 후기 대기중(false) or 후기 작성 완료(true) 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getImage0() {
		return image0;
	}
	public void setImage0(String image0) {
		this.image0 = image0;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCategory_code() {
		return category_code;
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}
	public int getInterest_count() {
		return interest_count;
	}
	public void setInterest_count(int interest_count) {
		this.interest_count = interest_count;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
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
	public String getBoard_date() {
		return board_date;
	}
	public void setBoard_date(String board_date) {
		this.board_date = board_date;
	}
	public String getSell_tf() {
		return sell_tf;
	}
	public void setSell_tf(String sell_tf) {
		this.sell_tf = sell_tf;
	}
	public String getHide_tf() {
		return hide_tf;
	}
	public void setHide_tf(String hide_tf) {
		this.hide_tf = hide_tf;
	}
	public String getReview_tf() {
		return review_tf;
	}
	public void setReview_tf(String review_tf) {
		this.review_tf = review_tf;
	}
    
    
}
