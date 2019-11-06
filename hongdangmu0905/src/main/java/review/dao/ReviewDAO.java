package review.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import review.bean.ReviewDTO;
@Repository
public class ReviewDAO {
	@Autowired
	private SqlSessionTemplate sessionTemplate;
	public int write(ReviewDTO reviewDTO) {
		return sessionTemplate.insert("mybatis.reviewMapper.write", reviewDTO);
	}
	public List<ReviewDTO> listB(String buyer) {
		return sessionTemplate.selectList("mybatis.reviewMapper.listB", buyer);
	}
	public List<ReviewDTO> listS(String seller) {
		return sessionTemplate.selectList("mybatis.reviewMapper.listS", seller);
	}
	public List<ReviewDTO> selectList(String user_name) {
		return sessionTemplate.selectList("mybatis.reviewMapper.selectList", user_name);
	}
	public int updateNameB(String muser_name, String user_name) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("user_name", user_name);
		map.put("muser_name", muser_name);
		return sessionTemplate.update("mybatis.reviewMapper.updateNameB", map);
	}
	public int updateNameS(String muser_name, String user_name) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("user_name", user_name);
		map.put("muser_name", muser_name);
		return sessionTemplate.update("mybatis.reviewMapper.updateNameS", map);
	}
}
