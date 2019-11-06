package Purchase_list.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Purchase_list.bean.Purchase_listDTO;
import Purchase_list.dao.Purchase_listDAO;
import review.bean.ReviewDTO;
@Service
public class Purchase_listServiceImpl implements Purchase_listService{
@Autowired
	private Purchase_listDAO dao;
	@Override
	public int write(Purchase_listDTO pListDTO) {
		// TODO Auto-generated method stub
		return dao.write(pListDTO);
	}

	@Override
	public int update(String goods_num) {
		// TODO Auto-generated method stub
		return dao.update(goods_num);
	}

	@Override
	public List<Purchase_listDTO> myPurchase_list(String user_code) {
		// TODO Auto-generated method stub
		return dao.myPurchase_list(user_code);
	}

}
