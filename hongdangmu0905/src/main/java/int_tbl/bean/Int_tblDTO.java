package int_tbl.bean;

public class Int_tblDTO {
	private String user_code;       //-- 물품에 관심 있는 사람번호
	private String goods_num;       //-- 관심있는 물품번호
	private String int_date;                //-- 관심 등록 날짜
	
	public String getUser_code() {
		return user_code;
	}
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
	public String getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(String goods_num) {
		this.goods_num = goods_num;
	}
	public String getInt_date() {
		return int_date;
	}
	public void setInt_date(String int_date) {
		this.int_date = int_date;
	}
	
	
	
}
