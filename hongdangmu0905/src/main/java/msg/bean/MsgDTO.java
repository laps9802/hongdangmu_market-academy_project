package msg.bean;

public class MsgDTO {
	  private String user_photo;	// -- 프로필
	  private String user_area;	// 지역
	  private int bno; // -- 글 번호
	  private int mno; // --쪽지 번호
	  private String sender; // -- 보낸 사람
	  private String recipient;// -- 받는 사람
	  private String msgTitle; //-- 쪽지 제목
	  private String msgContent; // --쪽지 내용
	  private String msgDate; // -- 보낸 날짜

	
	public MsgDTO() {
		super();
	}

	
	
	
	public MsgDTO(String user_photo, String user_area, int bno, int mno, String sender, String recipient,
			String msgTitle, String msgContent, String msgDate) {
		super();
		this.user_photo = user_photo;
		this.user_area = user_area;
		this.bno = bno;
		this.mno = mno;
		this.sender = sender;
		this.recipient = recipient;
		this.msgTitle = msgTitle;
		this.msgContent = msgContent;
		this.msgDate = msgDate;
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
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public int getMno() {
		return mno;
	}
	public void setMno(int mno) {
		this.mno = mno;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}
	  
	  
}
