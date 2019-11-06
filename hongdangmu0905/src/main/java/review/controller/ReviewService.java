package review.controller;


import java.util.List;

import review.bean.ReviewDTO;

public interface ReviewService {
	public int write(ReviewDTO reviewDTO);
	public List <ReviewDTO> listB(String buyer);
	public List <ReviewDTO> listS(String seller);
	public List <ReviewDTO> selectList(String user_name);
	public int updateNameB(String muser_name, String user_name);
	public int updateNameS(String muser_name, String user_name);
}
