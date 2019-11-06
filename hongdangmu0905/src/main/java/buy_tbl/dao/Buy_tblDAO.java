package buy_tbl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import buy_tbl.bean.Buy_tblDTO;
import goods_board.bean.Goods_boardDTO;
import int_tbl.bean.Int_tblDTO;

@Repository
public class Buy_tblDAO {
	@Autowired
	private SqlSessionTemplate sessionTemplate;

	// 데이터 저장
	public int write(Buy_tblDTO buy_tblDTO) {

		return sessionTemplate.insert("mybatis.buy_tblMapper.write", buy_tblDTO);
	}

	// 데이터 삭제
	public int delete(String user_code, String goods_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_code", user_code);
		map.put("goods_num", goods_num);
		return sessionTemplate.delete("mybatis.buy_tblMapper.delete", map);
	}

	public List<Buy_tblDTO> myInterestlist(String goods_num) {
		return sessionTemplate.selectList("mybatis.buy_tblMapper.myInterestlist", goods_num);
	}
}
