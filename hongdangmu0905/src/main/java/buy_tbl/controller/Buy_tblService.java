package buy_tbl.controller;

import java.util.List;

import buy_tbl.bean.Buy_tblDTO;

public interface Buy_tblService {
	public int write(Buy_tblDTO buy_tblDTO);
	public int delete(String user_code, String goods_num);
	public List<Buy_tblDTO> myInterestlist(String goods_num); 
}
