package Purchase_list.controller;

import java.util.List;

import Purchase_list.bean.Purchase_listDTO;
import review.bean.ReviewDTO;

public interface Purchase_listService {
	public int write(Purchase_listDTO pListDTO);
	public int update(String goods_num);
	public List<Purchase_listDTO> myPurchase_list(String user_code);
}
