package review.bean;

public class ReviewDTO {
	 private String seller;            //-- 판매자
	 private String buyer;             //-- 구매자  
	 private String content;           //-- 리뷰 내용
	 private String reviewer;          //-- 판매자(sell),구매자(buy) 구분
	 private String review_date;       //-- 리뷰한 날짜
	
	 
	 public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getReview_date() {
		return review_date;
	}
	public void setReview_date(String review_date) {
		this.review_date = review_date;
	}
}
