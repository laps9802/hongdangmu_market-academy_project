package review.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import review.bean.ReviewDTO;
import users.bean.UsersDTO;
import users.controller.UsersService;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService service;
	@Autowired
	private UsersService userservice;
	@RequestMapping(value = "/review/write.do", method = RequestMethod.POST)
	public ModelAndView write(HttpServletRequest request) {
		// 데이터 처리
		// post 방식에서의 한글 처리
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String seller = request.getParameter("seller");
		if (seller == null) {
			seller = "null";
		}
		String buyer = request.getParameter("buyer");
		if (buyer == null) {
			buyer = "null";
		}
		String content = request.getParameter("content");
		if (content == null) {
			content = "null";
		}
		String reviewer = request.getParameter("reviewer");
		if (reviewer == null) {
			reviewer = "null";
		}
		ReviewDTO reviewDTO = new ReviewDTO();
		reviewDTO.setSeller(seller);
		reviewDTO.setBuyer(buyer);
		reviewDTO.setContent(content);
		reviewDTO.setReviewer(reviewer);
		
		int su = service.write(reviewDTO);
		System.out.println("bbb");
		String rt = "";
		if (su > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/users/delete.jsp");
		modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);
		return modelAndView;
	}
	@RequestMapping(value = "/review/listB.do", method = RequestMethod.POST)
	public ModelAndView listB(HttpServletRequest request) {
		String buyer = request.getParameter("buyer");

	
		List<ReviewDTO> list = null;
		
		String rt = "";
		int total = 0; // 조회된 데이터 수
		try {
			list = service.listB(buyer);
			 total = list.size(); // 조회된 데이터 수
		} catch(Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		
		if (list != null) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		
		if(total > 0) {
			JSONArray item = new JSONArray();
			for (int i=0; i< list.size(); i++) {
				ReviewDTO reviewDTO = list.get(i);
				JSONObject temp = new JSONObject();
				temp.put("seller", reviewDTO.getSeller());
				temp.put("buyer", reviewDTO.getBuyer());
				temp.put("content", reviewDTO.getContent());
				temp.put("reviewer", reviewDTO.getReviewer());
				temp.put("review_date", reviewDTO.getReview_date());
				item.add(i, temp);
				json.put("item", item);
		}
	}
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);

		// System.out.println("su = " + su);

		return modelAndView;
	}
	@RequestMapping(value = "/review/listS.do", method = RequestMethod.POST)
	public ModelAndView listS(HttpServletRequest request) {
		String seller = request.getParameter("seller");

	
		List<ReviewDTO> list = null;
		
		String rt = "";
		int total = 0; // 조회된 데이터 수
		try {
			list = service.listS(seller);
			 total = list.size(); // 조회된 데이터 수
		} catch(Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		
		if (list != null) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		
		if(total > 0) {
			JSONArray item = new JSONArray();
			for (int i=0; i< list.size(); i++) {
				ReviewDTO reviewDTO = list.get(i);
				JSONObject temp = new JSONObject();
				temp.put("seller", reviewDTO.getSeller());
				temp.put("buyer", reviewDTO.getBuyer());
				temp.put("content", reviewDTO.getContent());
				temp.put("reviewer", reviewDTO.getReviewer());
				temp.put("review_date", reviewDTO.getReview_date());
				item.add(i, temp);
				json.put("item", item);
		}
	}
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);

		// System.out.println("su = " + su);

		return modelAndView;
	}
//	@RequestMapping(value = "/review/selectList.do", method = RequestMethod.POST)
//	public ModelAndView selectList(HttpServletRequest request) {
//		String user_name = request.getParameter("user_name");
//	
//		List<ReviewDTO> list = null;
//		
//		String rt = "";
//		int total = 0; // 조회된 데이터 수
//		try {
//			list = service.selectList(user_name);
//			 total = list.size(); // 조회된 데이터 수
//		} catch(Exception e) {
//			rt = "Fail";
//			e.printStackTrace();
//		}
//		
//		if (list != null) {
//			rt = "OK";
//		} else {
//			rt = "Fail";
//		}
//		JSONObject json = new JSONObject();
//		json.put("rt", rt);
//		json.put("total", total);
//		
//		if(total > 0) {
//			JSONArray item = new JSONArray();
//			for (int i=0; i < list.size(); i++) {
//				ReviewDTO reviewDTO = list.get(i);
//				JSONObject temp = new JSONObject();
//				temp.put("seller", reviewDTO.getSeller());
//				temp.put("buyer", reviewDTO.getBuyer());
//				temp.put("content", reviewDTO.getContent());
//				temp.put("reviewer", reviewDTO.getReviewer());
//				temp.put("review_date", reviewDTO.getReview_date());
//				item.add(i, temp);
//				json.put("item", item);
//		}
//	}
//		System.out.println(json);
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("/users/delete.jsp");
//		// modelAndView.addObject("su", su);
//		modelAndView.addObject("json", json);
//
//		// System.out.println("su = " + su);
//
//		return modelAndView;
//	}
	
	@RequestMapping(value = "/review/selectList.do", method = RequestMethod.POST)
	   public ModelAndView selectList(HttpServletRequest request) {
	      String user_name = request.getParameter("user_name");
	      
	      List<ReviewDTO> list = null;
	      UsersDTO usersDTO = null;
	      UsersDTO usersDTO2 = null;
	      String rt = "";
	      int total = 0; // 조회된 데이터 수
	      try {
	         list = service.selectList(user_name);
	          total = list.size(); // 조회된 데이터 수
	      } catch(Exception e) {
	         rt = "Fail";
	         e.printStackTrace();
	      }
	      
	      if (list != null) {
	         rt = "OK";
	      } else {
	         rt = "Fail";
	      }
	      usersDTO2 =userservice.selectOne(user_name);
	      JSONObject json = new JSONObject();
	      json.put("rt", rt);
	      json.put("total", total);
	      json.put("user_code", usersDTO2.getUser_code());
	      json.put("user_name", usersDTO2.getUser_name());
	      json.put("user_photo", usersDTO2.getUser_photo());
	      json.put("user_area", usersDTO2.getUser_area());
	      json.put("user_tel", usersDTO2.getUser_tel());
	      json.put("user_email1", usersDTO2.getUser_email1());
	      json.put("user_email2", usersDTO2.getUser_email2());
	      json.put("manner", usersDTO2.getManner());
	      json.put("reply_percent", usersDTO2.getReply_percent());
	      json.put("sales_count", usersDTO2.getSales_count());
	      json.put("purchase_count", usersDTO2.getPurchase_count());
	      json.put("interest_count", usersDTO2.getInterest_count());
	      json.put("lat", usersDTO2.getLat());
	      json.put("lng", usersDTO2.getLng());
	      json.put("join_date", usersDTO2.getJoin_date());
	      if(total > 0) {
	         JSONArray item = new JSONArray();
	         for (int i=0; i< list.size(); i++) {
	            ReviewDTO reviewDTO = list.get(i);
	            System.out.println("reviewDTO.getReviewer() = " + reviewDTO.getReviewer());
	            System.out.println("reviewDTO.getBuyer() = " + reviewDTO.getBuyer());
	            if(reviewDTO.getReviewer().equals("buy")) {
	               usersDTO = userservice.selectOne(reviewDTO.getBuyer());
	            }else if(reviewDTO.getReviewer().equals("sell")) {
	               usersDTO = userservice.selectOne(reviewDTO.getSeller());
	            }
	            JSONObject temp = new JSONObject();
	            temp.put("reviewer_image", usersDTO.getUser_photo());
	            System.out.println("reviewer_image = " + usersDTO.getUser_photo());
	            temp.put("reviewer_area", usersDTO.getUser_area());
	            if(reviewDTO.getReviewer().equals("buy")) {
	            	temp.put("reviewer", reviewDTO.getBuyer());
	            } else if (reviewDTO.getReviewer().equals("sell")) {
	            	temp.put("reviewer", reviewDTO.getSeller());
	            }
//	            temp.put("seller", reviewDTO.getSeller());
//	            temp.put("buyer", reviewDTO.getBuyer());
	            temp.put("reviewer_content", reviewDTO.getContent());
//	            temp.put("reviewer", reviewDTO.getReviewer());
	            temp.put("review_date", reviewDTO.getReview_date());
	            item.add(i, temp);
	            json.put("item", item);
	      }
	   }
	      System.out.println(json);
	      ModelAndView modelAndView = new ModelAndView();
	      modelAndView.setViewName("/users/delete.jsp");
	      // modelAndView.addObject("su", su);
	      modelAndView.addObject("json", json);

	      // System.out.println("su = " + su);

	      return modelAndView;
	   }
	
	@RequestMapping(value = "/review/bList.do", method = RequestMethod.POST)
	   public ModelAndView bList(HttpServletRequest request) {
	      String user_name = request.getParameter("user_name");
	      
	      List<ReviewDTO> list = null;
	      UsersDTO usersDTO = null;
	      UsersDTO usersDTO2 = null;
	      String rt = "";
	      int total = 0; // 조회된 데이터 수
	      try {
	         list = service.listB(user_name);
	          total = list.size(); // 조회된 데이터 수
	      } catch(Exception e) {
	         rt = "Fail";
	         e.printStackTrace();
	      }
	      
	      if (list != null) {
	         rt = "OK";
	      } else {
	         rt = "Fail";
	      }
	      usersDTO2 =userservice.selectOne(user_name);
	      JSONObject json = new JSONObject();
	      json.put("rt", rt);
	      json.put("total", total);
	      json.put("user_code", usersDTO2.getUser_code());
	      json.put("user_name", usersDTO2.getUser_name());
	      json.put("user_photo", usersDTO2.getUser_photo());
	      json.put("user_area", usersDTO2.getUser_area());
	      json.put("user_tel", usersDTO2.getUser_tel());
	      json.put("user_email1", usersDTO2.getUser_email1());
	      json.put("user_email2", usersDTO2.getUser_email2());
	      json.put("manner", usersDTO2.getManner());
	      json.put("reply_percent", usersDTO2.getReply_percent());
	      json.put("sales_count", usersDTO2.getSales_count());
	      json.put("purchase_count", usersDTO2.getPurchase_count());
	      json.put("interest_count", usersDTO2.getInterest_count());
	      json.put("lat", usersDTO2.getLat());
	      json.put("lng", usersDTO2.getLng());
	      json.put("join_date", usersDTO2.getJoin_date());
	      if(total > 0) {
	         JSONArray item = new JSONArray();
	         for (int i=0; i< list.size(); i++) {
	            ReviewDTO reviewDTO = list.get(i);
	            System.out.println("reviewDTO.getReviewer() = " + reviewDTO.getReviewer());
	            System.out.println("reviewDTO.getBuyer() = " + reviewDTO.getBuyer());
	            if(reviewDTO.getReviewer().equals("buy")) {
	               usersDTO = userservice.selectOne(reviewDTO.getBuyer());
	            }else if(reviewDTO.getReviewer().equals("sell")) {
	               usersDTO = userservice.selectOne(reviewDTO.getSeller());
	            }
	            JSONObject temp = new JSONObject();
	            temp.put("reviewer_image", usersDTO.getUser_photo());
	            System.out.println("reviewer_image = " + usersDTO.getUser_photo());
	            temp.put("reviewer_area", usersDTO.getUser_area());
	            if(reviewDTO.getReviewer().equals("buy")) {
	            	temp.put("reviewer", reviewDTO.getBuyer());
	            } else if (reviewDTO.getReviewer().equals("sell")) {
	            	temp.put("reviewer", reviewDTO.getSeller());
	            }
//	            temp.put("seller", reviewDTO.getSeller());
//	            temp.put("buyer", reviewDTO.getBuyer());
	            temp.put("reviewer_content", reviewDTO.getContent());
//	            temp.put("reviewer", reviewDTO.getReviewer());
	            temp.put("review_date", reviewDTO.getReview_date());
	            item.add(i, temp);
	            json.put("item", item);
	      }
	   }
	      System.out.println(json);
	      ModelAndView modelAndView = new ModelAndView();
	      modelAndView.setViewName("/users/delete.jsp");
	      // modelAndView.addObject("su", su);
	      modelAndView.addObject("json", json);

	      // System.out.println("su = " + su);

	      return modelAndView;
	   }
	
	@RequestMapping(value = "/review/sList.do", method = RequestMethod.POST)
	   public ModelAndView sList(HttpServletRequest request) {
	      String user_name = request.getParameter("user_name");
	      
	      List<ReviewDTO> list = null;
	      UsersDTO usersDTO = null;
	      UsersDTO usersDTO2 = null;
	      String rt = "";
	      int total = 0; // 조회된 데이터 수
	      try {
	         list = service.listS(user_name);
	          total = list.size(); // 조회된 데이터 수
	      } catch(Exception e) {
	         rt = "Fail";
	         e.printStackTrace();
	      }
	      
	      if (list != null) {
	         rt = "OK";
	      } else {
	         rt = "Fail";
	      }
	      usersDTO2 =userservice.selectOne(user_name);
	      JSONObject json = new JSONObject();
	      json.put("rt", rt);
	      json.put("total", total);
	      json.put("user_code", usersDTO2.getUser_code());
	      json.put("user_name", usersDTO2.getUser_name());
	      json.put("user_photo", usersDTO2.getUser_photo());
	      json.put("user_area", usersDTO2.getUser_area());
	      json.put("user_tel", usersDTO2.getUser_tel());
	      json.put("user_email1", usersDTO2.getUser_email1());
	      json.put("user_email2", usersDTO2.getUser_email2());
	      json.put("manner", usersDTO2.getManner());
	      json.put("reply_percent", usersDTO2.getReply_percent());
	      json.put("sales_count", usersDTO2.getSales_count());
	      json.put("purchase_count", usersDTO2.getPurchase_count());
	      json.put("interest_count", usersDTO2.getInterest_count());
	      json.put("lat", usersDTO2.getLat());
	      json.put("lng", usersDTO2.getLng());
	      json.put("join_date", usersDTO2.getJoin_date());
	      if(total > 0) {
	         JSONArray item = new JSONArray();
	         for (int i=0; i< list.size(); i++) {
	            ReviewDTO reviewDTO = list.get(i);
	            System.out.println("reviewDTO.getReviewer() = " + reviewDTO.getReviewer());
	            System.out.println("reviewDTO.getBuyer() = " + reviewDTO.getBuyer());
	            if(reviewDTO.getReviewer().equals("buy")) {
	               usersDTO = userservice.selectOne(reviewDTO.getBuyer());
	            }else if(reviewDTO.getReviewer().equals("sell")) {
	               usersDTO = userservice.selectOne(reviewDTO.getSeller());
	            }
	            JSONObject temp = new JSONObject();
	            temp.put("reviewer_image", usersDTO.getUser_photo());
	            System.out.println("reviewer_image = " + usersDTO.getUser_photo());
	            temp.put("reviewer_area", usersDTO.getUser_area());
	            if(reviewDTO.getReviewer().equals("buy")) {
	            	temp.put("reviewer", reviewDTO.getBuyer());
	            } else if (reviewDTO.getReviewer().equals("sell")) {
	            	temp.put("reviewer", reviewDTO.getSeller());
	            }
//	            temp.put("seller", reviewDTO.getSeller());
//	            temp.put("buyer", reviewDTO.getBuyer());
	            temp.put("reviewer_content", reviewDTO.getContent());
//	            temp.put("reviewer", reviewDTO.getReviewer());
	            temp.put("review_date", reviewDTO.getReview_date());
	            item.add(i, temp);
	            json.put("item", item);
	      }
	   }
	      System.out.println(json);
	      ModelAndView modelAndView = new ModelAndView();
	      modelAndView.setViewName("/users/delete.jsp");
	      // modelAndView.addObject("su", su);
	      modelAndView.addObject("json", json);

	      // System.out.println("su = " + su);

	      return modelAndView;
	   }

}
