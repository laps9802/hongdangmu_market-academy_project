package int_tbl.controller;

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

import goods_board.bean.Goods_boardDTO;
import goods_board.controller.Goods_boardService;
import goods_board.dao.Goods_boardDAO;
import int_tbl.bean.Int_tblDTO;
import int_tbl.dao.Int_tblDAO;
import users.controller.UsersService;
import users.dao.UsersDAO;

@Controller
public class Int_tblController {
	
	@Autowired
	private Int_tblService service;
	private Goods_boardService service2;
	
	// 유저가 관심갖는 물품 명
	@RequestMapping(value = "/int_tbl/listG.do", method = RequestMethod.POST)
	public ModelAndView listG(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_code = request.getParameter("user_code");
		// DB
		Int_tblDAO dao = new Int_tblDAO();
		List<Int_tblDTO> list = null;
		
		String rt = null;
		try {
			list = service.listG(user_code);
		} catch(Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		// JSON
		int total = list.size(); // 조회된 데이터 수
		// 조회된 데이터 수로 성공/실패 판단
		if (total > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		// json 객체 생성
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		// json 배열 만들기
		if (total > 0) {
			JSONArray item = new JSONArray();

			for (int i = 0; i < list.size(); i++) {
				Int_tblDTO int_tblDTO = list.get(i);
				JSONObject temp = new JSONObject();
				temp.put("user_code", int_tblDTO.getUser_code());
				temp.put("goods_num", int_tblDTO.getGoods_num());
				temp.put("int_date", int_tblDTO.getInt_date());
				item.add(i, temp);
			}
			// json 객체에 배열 추가
			json.put("item", item);
		}
		System.out.println(json);
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		// System.out.println("su = " + su);

		return modelAndView;
	}
	@RequestMapping(value = "/int_tbl/listU.do", method = RequestMethod.POST)
	public ModelAndView listU(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String goods_num = request.getParameter("goods_num");
		Int_tblDAO dao = new Int_tblDAO();
		List<Int_tblDTO> list = null;
		
		String rt = null;
		try {
			list = service.listU(goods_num);
		} catch(Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		// JSON
		int total = list.size(); // 조회된 데이터 수
		// 조회된 데이터 수로 성공/실패 판단
		if (total > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		// json 객체 생성
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		// json 배열 만들기
		if (total > 0) {
			JSONArray item = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				Int_tblDTO int_tblDTO = list.get(i);
				JSONObject temp = new JSONObject();
				temp.put("user_code", int_tblDTO.getUser_code());
				temp.put("goods_num", int_tblDTO.getGoods_num());
				temp.put("int_date", int_tblDTO.getInt_date());
				item.add(i, temp);
			}
			// json 객체에 배열 추가
			json.put("item", item);
		}
		System.out.println(json);
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		//System.out.println("su = " + su);
		return modelAndView;
	}
	@RequestMapping(value = "/int_tbl/getTotalGoods.do", method = RequestMethod.POST)
	public ModelAndView getTotalA(HttpServletRequest request) {
		String user_code = request.getParameter("user_code");
		int su = service.getTotalGoods(user_code);
		String rt = "";
		if (su > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		JSONArray item = new JSONArray();
		JSONObject temp = new JSONObject();
		temp.put("count", su);
		item.add(0, temp);
		json.put("item", item);
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);

		// System.out.println("su = " + su);

		return modelAndView;
	}
	@RequestMapping(value = "/int_tbl/getTotalUser.do", method = RequestMethod.POST)
	public ModelAndView getTotalUser(HttpServletRequest request) {
		String goods_num = request.getParameter("goods_num");
		int su = service.getTotalGoods(goods_num);
		String rt = "";
		if (su > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		JSONArray item = new JSONArray();
		JSONObject temp = new JSONObject();
		temp.put("count", su);
		item.add(0, temp);
		json.put("item", item);
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);

		// System.out.println("su = " + su);

		return modelAndView;
	}
	@RequestMapping(value = "/int_tbl/write.do", method = RequestMethod.POST)
	public ModelAndView write(HttpServletRequest request) {
		// 데이터 처리
		// post 방식에서의 한글 처리
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String user_code = request.getParameter("user_code");
		if (user_code == null) {
			user_code = "null";
		}
		String goods_num = request.getParameter("goods_num");
		if (goods_num == null) {
			goods_num = "null";
		}
		Int_tblDTO int_tblDTO = new Int_tblDTO();
		int_tblDTO.setUser_code(user_code);
		int_tblDTO.setGoods_num(goods_num);
		
		
		int su = service.write(int_tblDTO);
		String rt = "";
		if (su > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		
		if (rt.equals("OK")) {
			int num = Integer.parseInt(goods_num);
			System.out.println("num = " + num);
			int result = service.interestOn(num);
			System.out.println("result = " + result);
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
	@RequestMapping(value = "/int_tbl/delete.do", method = RequestMethod.POST)
	public ModelAndView boardDelete(HttpServletRequest request) {
		String goods_num = request.getParameter("goods_num");
		String user_code = request.getParameter("user_code");
		if (goods_num == null) {
			goods_num = "null";
		}
		if (user_code == null) {
			user_code = "null";
		}
		int su = 0;
		String rt = "";
		try {
			su = service.delete(user_code, goods_num);
		} catch (Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}

		if (su > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		if (rt.equals("OK")) {
			int num = Integer.parseInt(goods_num);
			System.out.println("num = " + num);
			int result = service.interestOff(num);
			System.out.println("result = " + result);
		} 
		
		Int_tblDTO int_tblDTO = new Int_tblDTO();
		int_tblDTO.setGoods_num(goods_num);
		System.out.println("goods_num = " + goods_num);
		int_tblDTO.setUser_code(user_code);
		System.out.println("user_code = " + user_code);
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/users/delete.jsp");
		modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);

		System.out.println("su = " + su);

		return modelAndView;
	}
	
	
	// 유저가 관심갖는 물품 명
	@RequestMapping(value = "/int_tbl/myInterestlist.do", method = RequestMethod.POST)
	public ModelAndView myInterestlist(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_code = request.getParameter("user_code");
		// DB
		Int_tblDAO dao = new Int_tblDAO();
		List<Int_tblDTO> list = null;
		
		String rt = null;
		try {
			list = service.myInterestlist(user_code);
		} catch(Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		// JSON
		int total = list.size(); // 조회된 데이터 수
		// 조회된 데이터 수로 성공/실패 판단
		if (total > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		if (total > 0) {
			for (int i = 0; i < list.size(); i++) {
				
			}	
		}
		modelAndView.addObject("goods_num", list);
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su",su);
		// System.out.println("su = " + su);

		return modelAndView;
	}
		
	@RequestMapping(value = "/int_tbl/interestList.do", method = RequestMethod.POST)
	public ModelAndView interestList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_code = request.getParameter("user_code");
		// DB
		Int_tblDAO dao = new Int_tblDAO();
		List<Int_tblDTO> list = null;
		List<Goods_boardDTO> boardList = null;
		
		String rt = null;
		try {
			list = service.myInterestlist(user_code);
		} catch(Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		// JSON
		int total = list.size(); // 조회된 데이터 수
		// 조회된 데이터 수로 성공/실패 판단
		if (total > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		// json 객체 생성
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		
		JSONArray item = new JSONArray();
		// json 배열 만들기
		Goods_boardDTO goods_boardDTO = new Goods_boardDTO();
		for (int j = 0; j<list.size(); j++) {
			System.out.println("list.size()? = " + list.size());
			int num = 0;
			Int_tblDTO int_tblDTO = list.get(j);
			String str_num = int_tblDTO.getGoods_num();
			if (str_num != null) {
				num = Integer.parseInt(str_num);
				System.out.println("num? = " + num);
				boardList = service.interestList(num);
//				System.out.println("boardList.get? = " + boardList.get(0).getNum());
			}	
				
				if (total > 0) {
					

					for (int i = 0; i < boardList.size(); i++) {
						Goods_boardDTO boardDTO = boardList.get(i);
						JSONObject temp = new JSONObject();
					
							temp.put("user_name", boardDTO.getUser_name());
							temp.put("area", boardDTO.getArea());
							temp.put("board_date", boardDTO.getBoard_date());
							temp.put("category_code", boardDTO.getCategory_code());
							temp.put("content", boardDTO.getContent());
							temp.put("image0", boardDTO.getImage0());
							temp.put("image1", boardDTO.getImage1());
							temp.put("image2", boardDTO.getImage2());
							temp.put("interest_count", boardDTO.getInterest_count());
							temp.put("num", boardDTO.getNum());
							temp.put("price", boardDTO.getPrice());
							temp.put("readcount", boardDTO.getReadcount());
							temp.put("lat", boardDTO.getLat());
							temp.put("lng", boardDTO.getLng());
							temp.put("reply_count", boardDTO.getReply_count());
							temp.put("subject", boardDTO.getSubject());
							temp.put("sell_tf", boardDTO.getSell_tf());
							temp.put("hide_tf", boardDTO.getHide_tf());
							temp.put("review_tf", boardDTO.getReview_tf());
							System.out.println("i = " + i);

							/*
							 * }else { temp.put("none", "none"); }
							 */
							int k = 0;
							item.add(k++, temp);
						
						json.put("item", item);
					}
				}
		}
		System.out.println(json);
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		// System.out.println("su = " + su);

		return modelAndView;
	}
		
	
	// 9월 2일
	@RequestMapping(value = "/int_tbl/listTF.do", method = RequestMethod.POST)
	public ModelAndView listTF(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_code = request.getParameter("user_code");
		String num = request.getParameter("num");
		System.out.println("user_code123 = " + user_code);
		System.out.println("num123 = " + num);
		Int_tblDAO dao = new Int_tblDAO();
		List<Int_tblDTO> list = null;
		
		String rt = null;
		try {
			list = service.listTF(user_code , num);
		} catch(Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		// JSON
		int total = list.size(); // 조회된 데이터 수
		System.out.println("total123 = " + total);
		// 조회된 데이터 수로 성공/실패 판단
		if (total > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		// json 객체 생성
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		// json 배열 만들기

		System.out.println(json);
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		//System.out.println("su = " + su);
		return modelAndView;
	}
}
