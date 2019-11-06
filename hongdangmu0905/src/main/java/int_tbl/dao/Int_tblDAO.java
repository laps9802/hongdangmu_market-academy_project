package int_tbl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import goods_board.bean.Goods_boardDTO;
import int_tbl.bean.Int_tblDTO;
import users.bean.UsersDTO;

@Repository
public class Int_tblDAO {
	@Autowired
	private SqlSessionTemplate sessionTemplate;

	// 데이터 저장
	public int write(Int_tblDTO int_tblDTO) {
		return sessionTemplate.insert("mybatis.int_tblMapper.write", int_tblDTO);
	}


	// 데이터 삭제
	public int delete(String user_code, String goods_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_code", user_code);
		map.put("goods_num", goods_num);
		return sessionTemplate.delete("mybatis.int_tblMapper.delete", map);
	}

	
	// 관심있는 물품 검색
	public List<Int_tblDTO> listG(String user_code) {
		return sessionTemplate.selectList("mybatis.int_tblMapper.listG", user_code);
	}
	
	// 관심갖는 사람 검색
	public List<Int_tblDTO> listU(String goods_num) {
		return sessionTemplate.selectList("mybatis.int_tblMapper.listU", goods_num);
	}

	// 관심있는 물품갯수(사람기준)
	public int getTotalGoods(String user_code) {
		return sessionTemplate.selectOne("mybatis.int_tblMapper.getTotalGoods", user_code);
	}
	
	// 관심갖는 사람 수(물품기준)
	public int getTotalUser(String goods_num) {
		return sessionTemplate.selectOne("mybatis.int_tblMapper.getTotalUser", goods_num);
	}
	
	public List<Int_tblDTO> myInterestlist(String user_code) {
		return sessionTemplate.selectList("mybatis.int_tblMapper.myInterestlist", user_code);
	}
	public List<Goods_boardDTO> interestList(int num) {
		return sessionTemplate.selectList("mybatis.int_tblMapper.interestList", num);
	}
	public int interestOn(int num) {
		return sessionTemplate.update("mybatis.int_tblMapper.interestOn", num);
	}
	public int interestOff(int num) {
		return sessionTemplate.update("mybatis.int_tblMapper.interestOff", num);
	}
	
	public List<Int_tblDTO> listTF(String user_code, String num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_code", user_code);
		map.put("num", num);
		return sessionTemplate.selectList("mybatis.int_tblMapper.listTF", map);
	}
}
