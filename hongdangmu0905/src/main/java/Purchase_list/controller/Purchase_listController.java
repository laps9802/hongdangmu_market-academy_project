package Purchase_list.controller;

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

import Purchase_list.bean.Purchase_listDTO;
import goods_board.bean.Goods_boardDTO;
import goods_board.controller.Goods_boardService;
@Controller
public class Purchase_listController {
	@Autowired
	private Purchase_listService service;
	@Autowired
	private Goods_boardService Gservice;
	
	@RequestMapping(value = "/purchase/write.do", method = RequestMethod.POST)
	public ModelAndView write(HttpServletRequest request) {
		// 데이터 처리
		// post 방식에서의 한글 처리
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String user_code = request.getParameter("user_code");
		String goods_num = request.getParameter("goods_num");
		Purchase_listDTO pListDTO = new Purchase_listDTO();
		pListDTO.setGoods_num(goods_num);
		pListDTO.setUser_code(user_code);
		
		int su = service.write(pListDTO);
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
	@RequestMapping(value = "/purchase/update.do", method = RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request) {
		// 데이터 처리
		// post 방식에서의 한글 처리
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String goods_num = request.getParameter("goods_num");
		
		
		int su = service.update(goods_num);
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
	@RequestMapping(value = "/purchase/list.do", method = RequestMethod.POST)
	public ModelAndView list(HttpServletRequest request) {
		// 데이터 처리
		// post 방식에서의 한글 처리
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String user_code = request.getParameter("user_code");
		
		List<Purchase_listDTO> list=null;
		
		list = service.myPurchase_list(user_code);
		Goods_boardDTO goods_boardDTO = new Goods_boardDTO();
		Purchase_listDTO pListDTO = null;
		int total = 0;

		total=list.size();
		System.out.println("bbb");
		String rt = "";
		if (total > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		if (total > 0) {
			JSONArray item = new JSONArray();
			for (int i = 0; i < list.size(); i++) {

				pListDTO = list.get(i);
				goods_boardDTO = Gservice.boardView(Integer.parseInt(pListDTO.getGoods_num()));
				JSONObject temp = new JSONObject();
				
				temp.put("Puser_code", pListDTO.getUser_code());
				temp.put("Pgoods_num", pListDTO.getGoods_num());
				temp.put("Preview_tf", pListDTO.getReview_tf());
				temp.put("Pbuy_date", pListDTO.getBuy_date());
				temp.put("user_name", goods_boardDTO.getUser_name());
				temp.put("area", goods_boardDTO.getArea());
				temp.put("board_date", goods_boardDTO.getBoard_date());
				temp.put("category_code", goods_boardDTO.getCategory_code());
				temp.put("content", goods_boardDTO.getContent());
				temp.put("image0", goods_boardDTO.getImage0());
				temp.put("image1", goods_boardDTO.getImage1());
				temp.put("image2", goods_boardDTO.getImage2());
				temp.put("interest_count", goods_boardDTO.getInterest_count());
				temp.put("lat", goods_boardDTO.getLat());
				temp.put("lng", goods_boardDTO.getLng());
				temp.put("num", goods_boardDTO.getNum());
				temp.put("price", goods_boardDTO.getPrice());
				temp.put("readcount", goods_boardDTO.getReadcount());
				temp.put("reply_count", goods_boardDTO.getReply_count());
				temp.put("subject", goods_boardDTO.getSubject());
				System.out.println("i = " + i);

				/*
				 * }else { temp.put("none", "none"); }
				 */
				item.add(i, temp);
			}
			json.put("item", item);

		}
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/users/delete.jsp");
		modelAndView.addObject("json", json);
		return modelAndView;
	}
}
