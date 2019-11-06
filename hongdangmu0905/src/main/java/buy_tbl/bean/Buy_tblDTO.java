package buy_tbl.bean;

public class Buy_tblDTO {
	private String user_code;      //-- 물품을 산 사람
	private String goods_num;      //-- 물품 번호
	private String buy_date;                //-- 관심 등록 날짜
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
	public String getBuy_date() {
		return buy_date;
	}
	public void setBuy_date(String buy_date) {
		this.buy_date = buy_date;
	}
	
	
}
