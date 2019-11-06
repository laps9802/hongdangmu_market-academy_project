package review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import review.bean.ReviewDTO;
import review.dao.ReviewDAO;
@Service
public class ReviewServicelmpl implements ReviewService{
	@Autowired
	private ReviewDAO dao;

	public int write(ReviewDTO reviewDTO) {
		// TODO Auto-generated method stub
		return dao.write(reviewDTO);
	}

	public List<ReviewDTO> listB(String buyer) {
		// TODO Auto-generated method stub
		return dao.listB(buyer);
	}

	public List<ReviewDTO> listS(String seller) {
		// TODO Auto-generated method stub
		return dao.listS(seller);
	}

	public List<ReviewDTO> selectList(String user_name) {
		// TODO Auto-generated method stub
		return dao.selectList(user_name);
	}

	public int updateNameB(String muser_name, String user_name) {
		// TODO Auto-generated method stub
		return dao.updateNameB(muser_name, user_name);
	}

	public int updateNameS(String muser_name, String user_name) {
		// TODO Auto-generated method stub
		return dao.updateNameS(muser_name, user_name);
	}
}
