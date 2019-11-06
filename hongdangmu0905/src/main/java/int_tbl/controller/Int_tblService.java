package int_tbl.controller;

import java.util.List;

import goods_board.bean.Goods_boardDTO;
import int_tbl.bean.Int_tblDTO;

public interface Int_tblService {
	public int write(Int_tblDTO int_tblDTO);
	public int delete(String user_code, String goods_num);
	public List<Int_tblDTO> listG(String user_code);
	public List<Int_tblDTO> listU(String goods_num);
	public int getTotalGoods(String user_code);
	public int getTotalUser(String goods_num);
	public List<Int_tblDTO> myInterestlist(String user_code); 
	public List<Goods_boardDTO> interestList(int num);
	public int interestOn(int num);
	public int interestOff(int num);
	
	// 9월 2일
	public List<Int_tblDTO> listTF(String user_code, String num);
}