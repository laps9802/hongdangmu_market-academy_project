package Purchase_list.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Purchase_list.bean.Purchase_listDTO;
import review.bean.ReviewDTO;

@Repository
public class Purchase_listDAO {
	@Autowired
	private SqlSessionTemplate sessionTemplate;
	
	public int write(Purchase_listDTO pListDTO) {
		return sessionTemplate.insert("mybatis.Purchase_listMapper.write", pListDTO);
	}
	
	public int update(String goods_num) {
		
		return sessionTemplate.update("mybatis.Purchase_listMapper.update", goods_num);
	}
	
	public List<Purchase_listDTO> myPurchase_list(String user_code) {
		return sessionTemplate.selectList("mybatis.Purchase_listMapper.myPurchase_list", user_code);
	}
}
