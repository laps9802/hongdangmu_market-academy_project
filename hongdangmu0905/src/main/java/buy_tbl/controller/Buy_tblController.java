package buy_tbl.controller;

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

import buy_tbl.bean.Buy_tblDTO;
import buy_tbl.dao.Buy_tblDAO;
import goods_board.bean.Goods_boardDTO;
import goods_board.controller.Goods_boardService;
import int_tbl.bean.Int_tblDTO;
import int_tbl.dao.Int_tblDAO;
import users.bean.UsersDTO;
import users.controller.UsersService;

@Controller
public class Buy_tblController {

	@Autowired
	private Buy_tblService service;
	@Autowired
	private Goods_boardService Gservice;
	@Autowired
	private UsersService Uservice;

	@RequestMapping(value = "/buy_tbl/write.do", method = RequestMethod.POST)
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
			user_code = "0";
		}
		String goods_num = request.getParameter("goods_num");
		if (goods_num == null) {
			goods_num = "0";
		}
		Buy_tblDTO buyDto = new Buy_tblDTO();
		buyDto.setUser_code(user_code);
		buyDto.setGoods_num(goods_num);

		int su = service.write(buyDto);
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

	@RequestMapping(value = "/buy_tbl/delete.do", method = RequestMethod.POST)
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
			int result = service.delete(user_code, goods_num);
			System.out.println("result = " + result);
		}

		Buy_tblDTO buyDto = new Buy_tblDTO();
		buyDto.setGoods_num(goods_num);
		System.out.println("goods_num = " + goods_num);
		buyDto.setUser_code(user_code);
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

	// 물품을 살 사람 목록
	@RequestMapping(value = "/buy_tbl/myBuylist.do", method = RequestMethod.POST)
	public ModelAndView myBuylist(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String goods_num = request.getParameter("goods_num");
		// DB
		Buy_tblDAO dao = new Buy_tblDAO();
		List<Buy_tblDTO> list = null;
		List<Goods_boardDTO> boardList = null;

		String rt = null;
		try {
			list = service.myInterestlist(goods_num);
		} catch (Exception e) {
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
		UsersDTO usersDTO = new UsersDTO();
		
		// json 객체 생성
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);

		JSONArray item = new JSONArray();
		// json 배열 만들기
		Goods_boardDTO goods_boardDTO = new Goods_boardDTO();
		if (total > 0) {
		for (int j = 0; j < list.size(); j++) {
			
			System.out.println("list.size()? = " + list.size());
			int num = 0;
			Buy_tblDTO buy_tblDTO = list.get(j);
			String str_num = buy_tblDTO.getUser_code();
			if (str_num != null) {
				num = Integer.parseInt(str_num);
				System.out.println("num? = " + num);
				
				usersDTO = Uservice.selectOne2(Integer.parseInt(buy_tblDTO.getUser_code()));
				System.out.println("Username.이름? = " + usersDTO.getUser_name());
			
					JSONObject temp = new JSONObject();
					temp.put("user_name", usersDTO.getUser_name());
					temp.put("user_code", usersDTO.getUser_code());
					temp.put("user_area", usersDTO.getUser_area());
					temp.put("user_tel", usersDTO.getUser_tel());
					temp.put("user_photo", usersDTO.getUser_photo());
					System.out.println("Username.get? = " + usersDTO.getUser_name());
					System.out.println("Usercode.get? = " + usersDTO.getUser_code());
					item.add(j, temp);
				
				
			}
			
			json.put("item", item);
		}
			

			
		}
		System.out.println(json);
		modelAndView.setViewName("/users/delete.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		// System.out.println("su = " + su);
		return modelAndView;
	}
}
