package buy_tbl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import buy_tbl.bean.Buy_tblDTO;
import buy_tbl.dao.Buy_tblDAO;

@Service
public class Buy_tblServiceImpl implements Buy_tblService{
	@Autowired
	private Buy_tblDAO dao;
	
	public int write(Buy_tblDTO buy_tblDTO) {
		// TODO Auto-generated method stub
		return dao.write(buy_tblDTO);
	}

	public int delete(String user_code, String goods_num) {
		// TODO Auto-generated method stub
		return dao.delete(user_code, goods_num);
	}

	public List<Buy_tblDTO> myInterestlist(String goods_num) {
		// TODO Auto-generated method stub
		return dao.myInterestlist(goods_num);
	}

}
