package goods_board.bean;

public class ReplyDTO {
	 private int rno;   // -- 답글 번호
	  private int  bno; //  -- 게시판 번호 
	  private String reply;	// -- 답글 내용
	  private String user_name;	//-- 유저 닉네임
	  private String replyDate; //-- 답글 날짜
	  private String user_photo;	//--유저 프로필
	  private String area; 	// 유저 지역
	  
	  
	public ReplyDTO() {
		super();
	}
	public ReplyDTO(int rno, int bno, String reply, String user_name, String replyDate) {
		super();
		this.rno = rno;
		this.bno = bno;
		this.reply = reply;
		this.user_name = user_name;
		this.replyDate = replyDate;
	}
	public int getRno() {
		return rno;
	}
	public void setRno(int rno) {
		this.rno = rno;
	}
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getReplyDate() {
		return replyDate;
	}
	public void setReplyDate(String replyDate) {
		this.replyDate = replyDate;
	}
	public String getUser_photo() {
		return user_photo;
	}
	public void setUser_photo(String user_photo) {
		this.user_photo = user_photo;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	  
	  
}
