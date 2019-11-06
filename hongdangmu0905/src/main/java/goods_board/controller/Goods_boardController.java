package goods_board.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import goods_board.bean.Category;
import goods_board.bean.Goods_boardDTO;
import goods_board.bean.ReplyDTO;
import users.bean.UsersDTO;
import users.controller.UsersService;
import users.dao.UsersDAO;

@Controller
public class Goods_boardController {
	public final String ServerBaseURL = "http://192.168.0.69:8098/hongdangmu/storage/";
	
	@Autowired
	private Goods_boardService service;
	@Autowired
	private UsersService userService;
	// 웹 리스트용
	@RequestMapping(value = "/goods/boardList.do",method = RequestMethod.POST)
	public ModelAndView boardList(HttpServletRequest request) {
		
		String str_pg = request.getParameter("pg");

		int pg = 1;
		if (str_pg != null) {
			pg = Integer.parseInt(str_pg);
		}
		System.out.println("pg = " + pg);
		int endNum = pg * 10;
		int startNum = endNum - 9;

		List<Goods_boardDTO> list = service.boardList(startNum, endNum);

		System.out.println(list.size());
		int totalA = service.getTotalA(); // 총 글수
		int totalP = (totalA + 4) / 5; // 총페이지수

		int startPage = (pg - 1) / 3 * 3 + 1;
		int endPage = startPage + 2;
		if (totalP < endPage)
			endPage = totalP;

		String rt = null;
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

		if (total > 0) {
			JSONArray item = new JSONArray();
			for (int i = 0; i < list.size(); i++) {

				Goods_boardDTO boardDTO = list.get(i);

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
				temp.put("lat", boardDTO.getLat());
				temp.put("lng", boardDTO.getLng());
				temp.put("num", boardDTO.getNum());
				temp.put("price", boardDTO.getPrice());
				temp.put("readcount", boardDTO.getReadcount());
				temp.put("reply_count", boardDTO.getReply_count());
				temp.put("subject", boardDTO.getSubject());
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
		modelAndView.addObject("list", list);
		modelAndView.addObject("pg", pg);
		modelAndView.addObject("startPage", startPage);
		modelAndView.addObject("endPage", endPage);
		modelAndView.addObject("totalP", totalP);
		modelAndView.addObject("json", json);
		
		modelAndView.setViewName("/users/delete.jsp");
		return modelAndView;
	}
	// 답글 리스트
	@RequestMapping(value = "/goods/replyList.do")
	public ModelAndView replyList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_bno = request.getParameter("bno");
		int bno = 0;
		if (str_bno != null) {
			bno = Integer.parseInt(str_bno);
		}
		List<ReplyDTO> list = service.replyList(bno);
		String rt = null;
		int total = list.size(); // 조회된 데이터 수
		// 조회된 데이터 수로 성공/실패 판단
		if (total > 0) {
			rt = "OK";
		} else {
			rt = "FAIL";
		}
		// json 객체 생성
				JSONObject json = new JSONObject();
				json.put("rt", rt);
				json.put("total", total);

				if (total > 0) {
					JSONArray item = new JSONArray();
					for (int i = 0; i < list.size(); i++) {
						ReplyDTO replyDTO = list.get(i);
						JSONObject temp = new JSONObject();
						temp.put("rno", replyDTO.getRno());
						temp.put("user_photo", replyDTO.getUser_photo());
						temp.put("area", replyDTO.getArea());
						temp.put("bno", replyDTO.getBno());
						temp.put("reply", replyDTO.getReply());
						temp.put("user_name", replyDTO.getUser_name());
						temp.put("replyDate", replyDTO.getReplyDate());
						System.out.println("i = " + i);
						item.add(i, temp);
					}
					json.put("item", item);

				}
				System.out.println(json);

				modelAndView.addObject("list", list);
				modelAndView.addObject("json", json);
				modelAndView.setViewName("/goods/write.jsp");
				return modelAndView;
	}
	@RequestMapping(value = "/goods/replyWrite.do")
	public ModelAndView replyWrite(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		ReplyDTO replyDTO = new ReplyDTO();
		
		String user_photo =request.getParameter("user_photo");
		System.out.println("user_photo = " + user_photo);
		String user_area =request.getParameter("user_area");
		String str_bno = request.getParameter("bno");
		String reply = request.getParameter("reply");
		String user_name = request.getParameter("user_name");
		int bno = 0;
		if(str_bno != null) {
			bno = Integer.parseInt(str_bno);
		}
		replyDTO.setUser_photo(user_photo);
		replyDTO.setArea(user_area);
		replyDTO.setBno(bno);
		replyDTO.setReply(reply);
		replyDTO.setUser_name(user_name);
		String rt = null;
		int su = 0;
		su = service.replyWrite(replyDTO);
		if(su>0) {
			rt = "OK";
			service.updateReC(bno);
		}else {
			rt = "FAIL";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		modelAndView.setViewName("/goods/write.jsp");
		modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);
		// modelAndView.addObject("usersDTO", usersDTO);
		System.out.println("su = " + su);
		
		return modelAndView;
	}
	// 검색용 리스트
	@RequestMapping(value = "/goods/searchList.do")
	public ModelAndView searchList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_Mylat = request.getParameter("Mylat");
		String keyword1 = request.getParameter("keyword");
		String keyword2 = request.getParameter("keyword");
		List<Goods_boardDTO> searchList = service.searchList(keyword1,keyword2);
		double Mylat = 999.0;
		if (str_Mylat != null) {
			Mylat = Double.parseDouble(str_Mylat);
		}
		String str_Mylng = request.getParameter("Mylng");
		double Mylng = 999.0;
		if (str_Mylng != null) {
			Mylng = Double.parseDouble(str_Mylng);
		}
		
		// JSON
				String rt = null;
				int total = searchList.size(); // 조회된 데이터 수
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

					for (int i = 0; i < searchList.size(); i++) {
						Goods_boardDTO boardDTO = searchList.get(i);
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
							temp.put("reply_count", boardDTO.getReply_count());
							temp.put("subject", boardDTO.getSubject());
							System.out.println("i = " + i);

							/*
							 * }else { temp.put("none", "none"); }
							 */
							int j = 0;
							item.add(j++, temp);
						
						json.put("item", item);
					}
				}

				System.out.println(json);
				modelAndView.setViewName("/goods/list.jsp");
				// modelAndView.addObject("su",su);
				modelAndView.addObject("json", json);
				
				// System.out.println("su = " + su);
		return modelAndView;
	}
	// 앱 리스트용
	@RequestMapping(value = "/goods/list.do")
	public ModelAndView list(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_Mylat = request.getParameter("Mylat");
		String category1 = request.getParameter("category1");
		String category2 = request.getParameter("category2");
		String category3 = request.getParameter("category3");
		String category4 = request.getParameter("category4");
		String category5 = request.getParameter("category5");
		String category6 = request.getParameter("category6");
		String category7 = request.getParameter("category7");
		String category8 = request.getParameter("category8");
		String category9 = request.getParameter("category9");
		String category10 = request.getParameter("category10");
		String category11 = request.getParameter("category11");
		String category12 = request.getParameter("category12");
		Category category = new Category();
		category.setCategory1(category1);
		category.setCategory2(category2);
		category.setCategory3(category3);
		category.setCategory4(category4);
		category.setCategory5(category5);
		category.setCategory6(category6);
		category.setCategory7(category7);
		category.setCategory8(category8);
		category.setCategory9(category9);
		category.setCategory10(category10);
		category.setCategory11(category11);
		category.setCategory12(category12);
		double Mylat = 999.0;
		if (str_Mylat != null) {
			Mylat = Double.parseDouble(str_Mylat);
		}
		String str_Mylng = request.getParameter("Mylng");

		double Mylng = 999.0;
		if (str_Mylng != null) {
			Mylng = Double.parseDouble(str_Mylng);
		}
		// DB
		List<Goods_boardDTO> list = service.list(category);

		// JSON
		String rt = null;
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
				Goods_boardDTO boardDTO = list.get(i);
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
					temp.put("reply_count", boardDTO.getReply_count());
					temp.put("subject", boardDTO.getSubject());
					System.out.println("i = " + i);

					/*
					 * }else { temp.put("none", "none"); }
					 */
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}

	@RequestMapping(value = "/goods/getTotalA.do", method = RequestMethod.POST)
	public ModelAndView getTotalA(HttpServletRequest request) {

		int su = service.getTotalA();
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

	@RequestMapping(value = "/goods/boardView.do", method = RequestMethod.POST)
	public ModelAndView boardView(HttpServletRequest request) {
		String str_num = request.getParameter("num");
		System.out.println("num = " + str_num);
		String user_name = request.getParameter("user_name");
		System.out.println("user_name = " + user_name);
		int num = 0;
		int userCode = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}
		System.out.println("num = " + num);
		System.out.println("userCode = " + userCode);
		Goods_boardDTO boardDTO = null;
		UsersDTO usersDTO = null;
		String rt = "";
		try {
			usersDTO = userService.selectOne(user_name);
			System.out.println("테스트1");
			boardDTO = service.boardView(num);
			System.out.println("테스트2");
			System.out.println("boardDto.getNum" + boardDTO.getNum() );
		} catch (Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		int RC = service.updateRC(num);

		if (boardDTO != null) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("RC", RC);
		JSONArray item = new JSONArray();
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
		temp.put("lat", boardDTO.getLat());
		temp.put("lng", boardDTO.getLng());
		temp.put("num", boardDTO.getNum());
		temp.put("price", boardDTO.getPrice());
		temp.put("readcount", boardDTO.getReadcount());
		temp.put("reply_count", boardDTO.getReply_count());
		temp.put("subject", boardDTO.getSubject());
		temp.put("hit", boardDTO.getReadcount());
		temp.put("manner", usersDTO.getManner());
		temp.put("user_photo",usersDTO.getUser_photo());
		item.add(0, temp);
		json.put("item", item);
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/goods/view.jsp");
		// modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);

		// System.out.println("su = " + su);

		return modelAndView;
	}

	@RequestMapping(value = "/goods/boardDelete.do")
	public ModelAndView boardDelete(HttpServletRequest request) {
		String str_num = request.getParameter("num");

		int num = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}

		int su = 0;
		String rt = "";
		try {
			su = service.boardDelete(num);
		} catch (Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}

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

		System.out.println("su = " + su);

		return modelAndView;
	}

	@RequestMapping(value = "/goods/requestupload2.do")
	public String requestupload2(MultipartHttpServletRequest mtfRequest) {
		List<MultipartFile> fileList = mtfRequest.getFiles("file");
		String src = mtfRequest.getParameter("src");
		System.out.println("src value : " + src);

		String path = "C:\\image\\";

		for (MultipartFile mf : fileList) {
			String originFileName = mf.getOriginalFilename(); // 원본 파일 명
			long fileSize = mf.getSize(); // 파일 사이즈

			System.out.println("originFileName : " + originFileName);
			System.out.println("fileSize : " + fileSize);

			String safeFile = path + System.currentTimeMillis() + originFileName;
			try {
				mf.transferTo(new File(safeFile));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "redirect:/";
	}

//	@RequestMapping(value = "/goods/boardModify.do", method = RequestMethod.POST)
//	public ModelAndView boardModify(MultipartHttpServletRequest mtfRequest) throws IllegalStateException, IOException {
//		// 데이터 처리
//		// post 방식에서의 한글 처리
//		try {
//			mtfRequest.setCharacterEncoding("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Goods_boardDTO boardDTO = new Goods_boardDTO();
//		List<MultipartFile> fileList = mtfRequest.getFiles("file");
//		System.out.println("fileList : " + fileList.size());
//		String image0 = "null";
//		String image1 = "null";
//		String image2 = "null";
//		if (fileList.size() > 0) {
//			String src = mtfRequest.getParameter("src");
//			System.out.println("src value : " + src);
//
//			String filePath = "C:\\storage\\";
//			int i = 0;
//			for (MultipartFile mf : fileList) {
//				String originFileName = mf.getOriginalFilename(); // 원본 파일 명
//				long fileSize = mf.getSize(); // 파일 사이즈
//
//				System.out.println("originFileName : " + originFileName);
//				System.out.println("fileSize : " + fileSize);
//
//				switch (i) {
//				case 0:
//					image0 = filePath + System.currentTimeMillis() + originFileName;
//					mf.transferTo(new File(image0));
//					break;
//				case 1:
//					image1 = filePath + System.currentTimeMillis() + originFileName;
//					mf.transferTo(new File(image1));
//					break;
//				case 2:
//					image2 = filePath + System.currentTimeMillis() + originFileName;
//					mf.transferTo(new File(image2));
//					break;
//
//				default:
//					System.out.println("갯수 초과");
//					break;
//				}
//				try {
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//
//				}
//				i++;
//			}
//		}
//		System.out.println("test1");
//		String str_lat = mtfRequest.getParameter("lat");
//		double lat = 999;
//		if (str_lat != null) {
//			lat = Double.parseDouble(str_lat);
//		}
//
//		System.out.println("test1");
//		String str_lng = mtfRequest.getParameter("lng");
//		double lng = 999;
//		if (str_lng != null) {
//			lng = Double.parseDouble(str_lng);
//		}
//		System.out.println("test1");
//		String user_name = mtfRequest.getParameter("user_name");
//		if (user_name == null) {
//			user_name = "null";
//		}
//		System.out.println("test1");
//		// String user_photo = request.getParameter("user_photo");
//		String subject = mtfRequest.getParameter("subject");
//		if (subject == null) {
//			subject = "null";
//		}
//		System.out.println("test1");
//		String category_code = mtfRequest.getParameter("category_code");
//		if (category_code == null) {
//			category_code = "null";
//		}
//		System.out.println("test1");
//		String area = mtfRequest.getParameter("area");
//		if (area == null) {
//			area = "null";
//		}
//		System.out.println("test1");
//		String content = mtfRequest.getParameter("content");
//		if (content == null) {
//			content = "null";
//		}
//		System.out.println("test1");
//		String str_price = mtfRequest.getParameter("price");
//		int price = 0;
//		if (str_price != null) {
//			price = Integer.parseInt(str_price);
//		}
//		System.out.println("test1");
//		// System.out.println("manner = " + manner);
//		// int reply_percent = Integer.parseInt(request.getParameter("reply_percent"));
//		String str_reply_count = mtfRequest.getParameter("reply_count");
//		int reply_count = 0;
//		if (str_reply_count != null) {
//			reply_count = Integer.parseInt(str_reply_count);
//		}
//		System.out.println("test1");
//		// System.out.println("reply_percent = " + reply_percent);
//		// int sales_count = Integer.parseInt(request.getParameter("sales_count"));
//		String str_interest_count = mtfRequest.getParameter("interest_count");
//		int interest_count = 0;
//		if (str_interest_count != null) {
//			interest_count = Integer.parseInt(str_interest_count);
//		}
//		System.out.println("test1");
//		// System.out.println("sales_count = " + sales_count);
//		// int purchase_count =
//		// Integer.parseInt(request.getParameter("purchase_count"));
//		String str_readcount = mtfRequest.getParameter("readcount");
//		int readcount = 0;
//		if (str_readcount != null) {
//			readcount = Integer.parseInt(str_readcount);
//		}
//
//		String str_num = mtfRequest.getParameter("num");
//		int num = 0;
//		if (str_num != null) {
//			num = Integer.parseInt(str_num);
//		}
//		System.out.println("test1");
//		// DB 처리
//
//		boardDTO.setInterest_count(interest_count);
//		boardDTO.setArea(area);
//		boardDTO.setCategory_code(category_code);
//		boardDTO.setContent(content);
//		boardDTO.setImage0(image0);
//		boardDTO.setImage1(image1);
//		boardDTO.setImage2(image2);
//		boardDTO.setPrice(price);
//		boardDTO.setReadcount(readcount);
//		boardDTO.setReply_count(reply_count);
//		boardDTO.setSubject(subject);
//		boardDTO.setLat(lat);
//		boardDTO.setLng(lng);
//		boardDTO.setUser_name(user_name);
//		boardDTO.setNum(num);
//		;
//		System.out.println("image0 = " + boardDTO.getImage0());
//		System.out.println("image1 = " + boardDTO.getImage1());
//		System.out.println("image2 = " + boardDTO.getImage2());
//		int su = 0;
//		String rt = "";
//		try {
//			su = service.boardModify(boardDTO);
//		} catch (Exception e) {
//			rt = "Fail";
//			e.printStackTrace();
//		}
//		System.out.println("su = " + su);
//
//		if (su > 0) {
//			rt = "OK";
//		} else {
//			rt = "Fail";
//		}
//
//		JSONObject json = new JSONObject();
//		json.put("rt", rt);
//		System.out.println(json);
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("/users/write.jsp");
//		modelAndView.addObject("su", su);
//		modelAndView.addObject("json", json);
//		// modelAndView.addObject("usersDTO", usersDTO);
//		System.out.println("su = " + su);
//
//		return modelAndView;
//	}
	@RequestMapping(value = "/goods/boardModify.do",method = RequestMethod.POST)
	public ModelAndView boardModify(MultipartFile img, MultipartFile img2, MultipartFile img3, HttpServletRequest request){
		// 데이터 처리
		// post 방식에서의 한글 처리
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str_num = request.getParameter("num");
		int num = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}
		
		Goods_boardDTO boardDTO = new Goods_boardDTO();
		String fileName = img.getOriginalFilename();
		String fileName2 = null;
		String fileName3 = null;
		if(img2 != null) {
			fileName2 = img2.getOriginalFilename();
		}
		if(img3 != null) {
			fileName3 = img3.getOriginalFilename();
		}
		try {
			fileName = uploadFile(fileName, img.getBytes());
			if(img2 != null) {
				fileName2 = uploadFile(fileName2, img2.getBytes());
				System.out.println("실험2" + fileName2);
			}
			if(img3 != null) {
				fileName3 = uploadFile(fileName3, img3.getBytes());
				System.out.println("실험3" + fileName3);
			}
		} catch (IOException e) {
			// TODO: handle exception
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("test1");
		String str_lat = request.getParameter("lat");
		double lat = 999;
		if (str_lat != null) {
			lat = Double.parseDouble(str_lat);
		}

		System.out.println("test2");
		String str_lng = request.getParameter("lng");
		double lng = 999;
		if (str_lng != null) {
			lng = Double.parseDouble(str_lng);
		}
		System.out.println("test3");
		String user_name = request.getParameter("user_name");
		if (user_name == null) {
			user_name = "null";
		}
		System.out.println("test4");
		// String user_photo = request.getParameter("user_photo");
		String subject = request.getParameter("subject");
		if (subject == null) {
			subject = "null";
		}
		System.out.println("test5");
		String category_code = request.getParameter("category_code");
		if (category_code == null) {
			category_code = "null";
		}
		System.out.println("test6");
		String area = request.getParameter("area");
		if (area == null) {
			area = "null";
		}
		System.out.println("test7");
		String content = request.getParameter("content");
		if (content == null) {
			content = "null";
		}
		System.out.println("test8");
		String str_price = request.getParameter("price");
		int price = 0;
		if (str_price != null) {
			price = Integer.parseInt(str_price);
		}
		System.out.println("test9");
		// System.out.println("manner = " + manner);
		// int reply_percent = Integer.parseInt(request.getParameter("reply_percent"));
		String str_reply_count = request.getParameter("reply_count");
		int reply_count = 0;
		if (str_reply_count != null) {
			reply_count = Integer.parseInt(str_reply_count);
		}
		System.out.println("test10");
		// System.out.println("reply_percent = " + reply_percent);
		// int sales_count = Integer.parseInt(request.getParameter("sales_count"));
		String str_interest_count = request.getParameter("interest_count");
		int interest_count = 0;
		if (str_interest_count != null) {
			interest_count = Integer.parseInt(str_interest_count);
		}
		System.out.println("test11");
		// System.out.println("sales_count = " + sales_count);
		// int purchase_count =
		// Integer.parseInt(request.getParameter("purchase_count"));
		String str_readcount = request.getParameter("readcount");
		int readcount = 0;
		if (str_readcount != null) {
			readcount = Integer.parseInt(str_readcount);
		}
		System.out.println("test12");
		// DB 처리
		boardDTO.setInterest_count(interest_count);
		boardDTO.setArea(area);
		boardDTO.setCategory_code(category_code);
		boardDTO.setContent(content);
		boardDTO.setImage0(ServerBaseURL + fileName);
		boardDTO.setImage1(ServerBaseURL + fileName2);
		boardDTO.setImage2(ServerBaseURL + fileName3);
		boardDTO.setPrice(price);
		boardDTO.setReadcount(readcount);
		boardDTO.setReply_count(reply_count);
		boardDTO.setSubject(subject);
		boardDTO.setLat(lat);
		boardDTO.setLng(lng);
		boardDTO.setUser_name(user_name);
		boardDTO.setNum(num);
		System.out.println("image0 = " + boardDTO.getImage0());
		System.out.println("image1 = " + boardDTO.getImage1());
		System.out.println("image2 = " + boardDTO.getImage2());
		int su = 0;
		String rt = "";
		try {
			su = service.boardModify(boardDTO);
		} catch (Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		System.out.println("su = " + su);

		if (su > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}

		JSONObject json = new JSONObject();
		json.put("rt", rt);
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/goods/write.jsp");
		modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);
		// modelAndView.addObject("usersDTO", usersDTO);
		System.out.println("su = " + su);

		return modelAndView;
	}
	@RequestMapping(value = "/goods/write.do",method = RequestMethod.POST)
	public ModelAndView write(MultipartFile img, MultipartFile img2,MultipartFile img3, HttpServletRequest request){
		// 데이터 처리
		// post 방식에서의 한글 처리
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Goods_boardDTO boardDTO = new Goods_boardDTO();
		String fileName = img.getOriginalFilename();
		String fileName2 = null;
		String fileName3 = null;
		if(img2 != null) {
			fileName2 = img2.getOriginalFilename();
		}
		if(img3 != null) {
			fileName3 = img3.getOriginalFilename();
		}
		try {
			System.out.println("uploadFile 진입");
			fileName = uploadFile(fileName, img.getBytes());
			if(img2 != null) {
				fileName2 = uploadFile(fileName2, img2.getBytes());
			}
			if(img3 != null) {
				fileName3 = uploadFile(fileName3, img3.getBytes());
			}
		} catch (IOException e) {
			// TODO: handle exception
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fileName = " + fileName);
		System.out.println("fileName2 = " + fileName2);
		System.out.println("fileName3 = " + fileName3);
		
		
		String str_lat = request.getParameter("lat");
		double lat = 999;
		if (str_lat != null) {
			lat = Double.parseDouble(str_lat);
		}

		String str_lng = request.getParameter("lng");
		double lng = 999;
		if (str_lng != null) {
			lng = Double.parseDouble(str_lng);
		}
		String user_name = request.getParameter("user_name");
		if (user_name == null) {
			user_name = "null";
		}
		// String user_photo = request.getParameter("user_photo");
		String subject = request.getParameter("subject");
		if (subject == null) {
			subject = "null";
		}
		String category_code = request.getParameter("category_code");
		if (category_code == null) {
			category_code = "null";
		}
		String area = request.getParameter("area");
		if (area == null) {
			area = "null";
		}
		String content = request.getParameter("content");
		if (content == null) {
			content = "null";
		}
		String str_price = request.getParameter("price");
		int price = 0;
		if (str_price != null) {
			price = Integer.parseInt(str_price);
		}
		// System.out.println("manner = " + manner);
		// int reply_percent = Integer.parseInt(request.getParameter("reply_percent"));
		String str_reply_count = request.getParameter("reply_count");
		int reply_count = 0;
		if (str_reply_count != null) {
			reply_count = Integer.parseInt(str_reply_count);
		}
		// System.out.println("reply_percent = " + reply_percent);
		// int sales_count = Integer.parseInt(request.getParameter("sales_count"));
		String str_interest_count = request.getParameter("interest_count");
		int interest_count = 0;
		if (str_interest_count != null) {
			interest_count = Integer.parseInt(str_interest_count);
		}
		// System.out.println("sales_count = " + sales_count);
		// int purchase_count =
		// Integer.parseInt(request.getParameter("purchase_count"));
		String str_readcount = request.getParameter("readcount");
		int readcount = 0;
		if (str_readcount != null) {
			readcount = Integer.parseInt(str_readcount);
		}
		
		
		// DB 처리
		boardDTO.setInterest_count(interest_count);
		boardDTO.setArea(area);
		boardDTO.setCategory_code(category_code);
		boardDTO.setContent(content);
		boardDTO.setImage0(ServerBaseURL + fileName);
		boardDTO.setImage1(ServerBaseURL + fileName2);
		boardDTO.setImage2(ServerBaseURL + fileName3);
		boardDTO.setPrice(price);
		boardDTO.setReadcount(readcount);
		boardDTO.setReply_count(reply_count);
		boardDTO.setSubject(subject);
		boardDTO.setLat(lat);
		boardDTO.setLng(lng);
		boardDTO.setUser_name(user_name);
		System.out.println("image0 = " + boardDTO.getImage0());
		System.out.println("image1 = " + boardDTO.getImage1());
		System.out.println("image2 = " + boardDTO.getImage2());
		int su = 0;
		String rt = "";
		try {
			su = service.boardWrite(boardDTO);
		} catch (Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}
		System.out.println("su = " + su);

		if (su > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}

		JSONObject json = new JSONObject();
		json.put("rt", rt);
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/goods/write.jsp");
		modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);
		// modelAndView.addObject("usersDTO", usersDTO);
		System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/saleList.do")
	public ModelAndView saleList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		// DB
		List<Goods_boardDTO> list = service.saleList();

		// JSON
		String rt = null;
		
		int total = list.size(); // 조회된 데이터 수
		System.out.println("list.size() = " + list.size());
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
				Goods_boardDTO boardDTO = list.get(i);
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
					temp.put("reply_count", boardDTO.getReply_count());
					temp.put("lat", boardDTO.getLat());
					temp.put("lng", boardDTO.getLng());
					temp.put("subject", boardDTO.getSubject());
					temp.put("sell_tf", boardDTO.getSell_tf());
					temp.put("hide_tf", boardDTO.getHide_tf());
					temp.put("review_tf", boardDTO.getReview_tf());
					System.out.println("i = " + i);

					/*
					 * }else { temp.put("none", "none"); }
					 */
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/saleCompleteList.do")
	public ModelAndView saleCompleteList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		// DB
		List<Goods_boardDTO> list = service.saleCompleteList();

		// JSON
		String rt = null;
		
		int total = list.size(); // 조회된 데이터 수
		System.out.println("list.size() = " + list.size());
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
				Goods_boardDTO boardDTO = list.get(i);
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
					temp.put("reply_count", boardDTO.getReply_count());
					temp.put("lat", boardDTO.getLat());
					temp.put("lng", boardDTO.getLng());
					temp.put("subject", boardDTO.getSubject());
					temp.put("sell_tf", boardDTO.getSell_tf());
					temp.put("hide_tf", boardDTO.getHide_tf());
					temp.put("review_tf", boardDTO.getReview_tf());
					System.out.println("i = " + i);

					/*
					 * }else { temp.put("none", "none"); }
					 */
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/mySaleList.do", method = RequestMethod.POST)
	public ModelAndView mySaleList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_name = request.getParameter("user_name");
		System.out.println("user_name = " + user_name);
		// DB
		List<Goods_boardDTO> list = service.mySaleList(user_name);

		// JSON
		String rt = null;
		
		int total = list.size(); // 조회된 데이터 수
		System.out.println("list.size() = " + list.size());
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
				Goods_boardDTO boardDTO = list.get(i);
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
					temp.put("reply_count", boardDTO.getReply_count());
					temp.put("lat", boardDTO.getLat());
					temp.put("lng", boardDTO.getLng());
					temp.put("subject", boardDTO.getSubject());
					temp.put("sell_tf", boardDTO.getSell_tf());
					temp.put("hide_tf", boardDTO.getHide_tf());
					temp.put("review_tf", boardDTO.getReview_tf());
					System.out.println("i = " + i);

					/*
					 * }else { temp.put("none", "none"); }
					 */
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/mySaleCompleteList.do",method = RequestMethod.POST)
	public ModelAndView mySaleCompleteList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_name = request.getParameter("user_name");
		// DB
		List<Goods_boardDTO> list = service.mySaleCompleteList(user_name);

		// JSON
		String rt = null;
		
		int total = list.size(); // 조회된 데이터 수
		System.out.println("list.size() = " + list.size());
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
				Goods_boardDTO boardDTO = list.get(i);
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
					temp.put("reply_count", boardDTO.getReply_count());
					temp.put("lat", boardDTO.getLat());
					temp.put("lng", boardDTO.getLng());
					temp.put("subject", boardDTO.getSubject());
					temp.put("sell_tf", boardDTO.getSell_tf());
					temp.put("hide_tf", boardDTO.getHide_tf());
					temp.put("review_tf", boardDTO.getReview_tf());
					System.out.println("i = " + i);

					/*
					 * }else { temp.put("none", "none"); }
					 */
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/hideList.do",method = RequestMethod.POST)
	public ModelAndView hideList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_name = request.getParameter("user_name");
		// DB
		List<Goods_boardDTO> list = service.hideList(user_name);

		// JSON
		String rt = null;
		
		int total = list.size(); // 조회된 데이터 수
		System.out.println("list.size() = " + list.size());
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
				Goods_boardDTO boardDTO = list.get(i);
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
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/reviewIngList.do",method = RequestMethod.POST)
	public ModelAndView reviewIngList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_name = request.getParameter("user_name");
		// DB
		List<Goods_boardDTO> list = service.reviewIngList(user_name);

		// JSON
		String rt = null;
		
		int total = list.size(); // 조회된 데이터 수
		System.out.println("list.size() = " + list.size());
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
				Goods_boardDTO boardDTO = list.get(i);
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
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/reviewCompleteList.do",method = RequestMethod.POST)
	public ModelAndView reviewCompleteList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_name = request.getParameter("user_name");
		// DB
		List<Goods_boardDTO> list = service.reviewCompleteList(user_name);

		// JSON
		String rt = null;
		
		int total = list.size(); // 조회된 데이터 수
		System.out.println("list.size() = " + list.size());
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
				Goods_boardDTO boardDTO = list.get(i);
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
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/hotList.do")
	public ModelAndView hotList(HttpServletRequest request) {
		System.out.println("관심순");
		ModelAndView modelAndView = new ModelAndView();
		String str_Mylat = request.getParameter("Mylat");
		String category1 = request.getParameter("category1");
		String category2 = request.getParameter("category2");
		String category3 = request.getParameter("category3");
		String category4 = request.getParameter("category4");
		String category5 = request.getParameter("category5");
		String category6 = request.getParameter("category6");
		String category7 = request.getParameter("category7");
		String category8 = request.getParameter("category8");
		String category9 = request.getParameter("category9");
		String category10 = request.getParameter("category10");
		String category11 = request.getParameter("category11");
		String category12 = request.getParameter("category12");
		Category category = new Category();
		category.setCategory1(category1);
		category.setCategory2(category2);
		category.setCategory3(category3);
		category.setCategory4(category4);
		category.setCategory5(category5);
		category.setCategory6(category6);
		category.setCategory7(category7);
		category.setCategory8(category8);
		category.setCategory9(category9);
		category.setCategory10(category10);
		category.setCategory11(category11);
		category.setCategory12(category12);
		double Mylat = 999.0;
		if (str_Mylat != null) {
			Mylat = Double.parseDouble(str_Mylat);
		}
		String str_Mylng = request.getParameter("Mylng");

		double Mylng = 999.0;
		if (str_Mylng != null) {
			Mylng = Double.parseDouble(str_Mylng);
		}
		// DB
		List<Goods_boardDTO> list = service.hotList(category);

		// JSON
		String rt = null;
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
				Goods_boardDTO boardDTO = list.get(i);
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
					temp.put("reply_count", boardDTO.getReply_count());
					temp.put("subject", boardDTO.getSubject());
					System.out.println("i = " + i);

					/*
					 * }else { temp.put("none", "none"); }
					 */
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/listLocation.do")
	public ModelAndView listLocation(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String category1 = request.getParameter("category1");
		String category2 = request.getParameter("category2");
		String category3 = request.getParameter("category3");
		String category4 = request.getParameter("category4");
		String category5 = request.getParameter("category5");
		String category6 = request.getParameter("category6");
		String category7 = request.getParameter("category7");
		String category8 = request.getParameter("category8");
		String category9 = request.getParameter("category9");
		String category10 = request.getParameter("category10");
		String category11 = request.getParameter("category11");
		String category12 = request.getParameter("category12");
		Category category = new Category();
		category.setCategory1(category1);
		category.setCategory2(category2);
		category.setCategory3(category3);
		category.setCategory4(category4);
		category.setCategory5(category5);
		category.setCategory6(category6);
		category.setCategory7(category7);
		category.setCategory8(category8);
		category.setCategory9(category9);
		category.setCategory10(category10);
		category.setCategory11(category11);
		category.setCategory12(category12);
		
		
		String rt = null;
		String strLat = request.getParameter("lat");
		if(strLat == null) {
			JSONObject json = new JSONObject();
			rt = "Location missed";
			json.put("rt", rt);
			modelAndView.setViewName("/goods/view.jsp");
			modelAndView.addObject("json", json);
			return modelAndView;
		}
		double lat = Double.parseDouble(strLat);
		
		String strLng = request.getParameter("lng");
		double lng = 0;
		if(strLng != null) {
			lng = Double.parseDouble(strLng);
		}
		category.setLat(lat);
		category.setLng(lng);
		
		
		List<Goods_boardDTO> list = service.listLocation(category);
		
		final double finalLat = lat;
		final double finalLng = lng;
		System.out.println("lat = " + finalLat);
		System.out.println("lng = " + finalLng);
		final ConvertToDistance convertToDistance = new ConvertToDistance();
		ArrayList<Goods_boardDTO> orderedList = 
				list
				.stream()
				.map(dto ->{
					double distance = convertToDistance.getDistance(finalLat, finalLng, dto.getLat(), dto.getLng());
					System.out.println("distance = " + distance);
					dto.setDist(distance);
					return dto;
				})
				.sorted((front, rear) -> {
					if(rear.getDist() < front.getDist()) return 1;
					else return -1;
				})
				.collect(Collectors.toCollection(ArrayList::new));
		
		
		// JSON
		int total = orderedList.size(); // 조회된 데이터 수
		
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
			
			for (int i = 0; i < orderedList.size(); i++) {
				Goods_boardDTO boardDTO = orderedList.get(i);
				
				JSONObject temp = new JSONObject();
				temp.put("user_name", boardDTO.getUser_name());
				temp.put("area", boardDTO.getArea());
				
				double distance = boardDTO.getDist();
	            String strDist="";
	            if(distance >= 1000) {
	               strDist = ((Math.floor(distance / 100)) / 10) + "km";
	            } else {
	               strDist = (int)Math.floor(distance) + "m";
	            }
	            temp.put("distance", strDist);

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
				temp.put("reply_count", boardDTO.getReply_count());
				temp.put("subject", boardDTO.getSubject());
				
				item.add(i, temp);
			}
			
			json.put("item", item);
		}

		System.out.println(json);
		modelAndView.addObject("json", json);
		modelAndView.setViewName("/goods/list.jsp");

		return modelAndView;
	}
	
	
	@RequestMapping(value = "/goods/locationSearch.do")
	public ModelAndView locationSearch(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		int categorySize = 12;
		
		String category1 = request.getParameter("category1");
		String category2 = request.getParameter("category2");
		String category3 = request.getParameter("category3");
		String category4 = request.getParameter("category4");
		String category5 = request.getParameter("category5");
		String category6 = request.getParameter("category6");
		String category7 = request.getParameter("category7");
		String category8 = request.getParameter("category8");
		String category9 = request.getParameter("category9");
		String category10 = request.getParameter("category10");
		String category11 = request.getParameter("category11");
		String category12 = request.getParameter("category12");
		String keyword1 = request.getParameter("keyword");
		System.out.println("keyword1 = " + keyword1);
		Category category = new Category();
		category.setCategory1(category1);
		category.setCategory2(category2);
		category.setCategory3(category3);
		category.setCategory4(category4);
		category.setCategory5(category5);
		category.setCategory6(category6);
		category.setCategory7(category7);
		category.setCategory8(category8);
		category.setCategory9(category9);
		category.setCategory10(category10);
		category.setCategory11(category11);
		category.setCategory12(category12);
		category.setKeyword1(keyword1);
		
		String str_Mylat = request.getParameter("lat");
		double Mylat = 999.0;
		if (str_Mylat != null) {
			Mylat = Double.parseDouble(str_Mylat);
		}
		String str_Mylng = request.getParameter("lng");
		double Mylng = 999.0;
		if (str_Mylng != null) {
			Mylng = Double.parseDouble(str_Mylng);
		}
		category.setLat(Mylat);
		category.setLng(Mylng);
		
		// DB
		List<Goods_boardDTO> list = service.listSearchInLocation(category);
		System.out.println("list.size() = " + list.size());

		
		final double finalLat = Mylat;
		final double finalLng = Mylng;
		System.out.println("lat = " + finalLat);
		System.out.println("lng = " + finalLng);
//		System.out.println("list.get(0).getLat() = " + list.get(0).getLat());
//		System.out.println("list.get(0).getLng() = " + list.get(0).getLng());
		final ConvertToDistance convertToDistance = new ConvertToDistance();
		ArrayList<Goods_boardDTO> orderedList = 
				list
				.stream()
				.map(dto ->{
					double distance = convertToDistance.getDistance(finalLat, finalLng, dto.getLat(), dto.getLng());
					System.out.println("distance = " + distance);
					dto.setDist(distance);
					return dto;
				})
				.sorted((front, rear) -> {
					if(rear.getDist() < front.getDist()) return 1;
					else return -1;
				})
				.collect(Collectors.toCollection(ArrayList::new));
		
		
		
		
		// JSON
		String rt = null;
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
			
			for (int i = 0; i < orderedList.size(); i++) {
				Goods_boardDTO boardDTO = orderedList.get(i);
				
				JSONObject temp = new JSONObject();
				temp.put("user_name", boardDTO.getUser_name());
				temp.put("area", boardDTO.getArea());
				
				double distance = boardDTO.getDist();
	            String strDist="";
	            if(distance >= 1000) {
	            	System.out.println("distance / 100 = " + distance / 100);
	               strDist = ((Math.floor(distance / 100)) / 10) + "km";
	            } else {
	               strDist = (int)Math.floor(distance) + "m";
	            }
	            temp.put("distance", strDist);

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
				temp.put("reply_count", boardDTO.getReply_count());
				temp.put("subject", boardDTO.getSubject());
				
				item.add(i, temp);
			}
			
			json.put("item", item);
		}

		System.out.println(json);
		modelAndView.addObject("json", json);
		modelAndView.setViewName("/goods/list.jsp");

		return modelAndView;
	}
	
	
	@RequestMapping(value = "/goods/selectAroundMarkers.do", method = RequestMethod.POST)
	public ModelAndView selectAroundMarkers(HttpServletRequest request) {
		final double LAT_RATE_500 = 0.00451;
		final double LNG_RATE_500 = 0.00563;
		final double LAT_RATE_1 = 0.00902;
		final double LNG_RATE_1 = 0.01125;
		final double LAT_RATE_3 = 0.02704;
		final double LNG_RATE_3 = 0.03375;
		
		
		String strLat = request.getParameter("lat");
		String rt="";
		ModelAndView modelAndView = new ModelAndView();
		if(strLat == null) {
			JSONObject json = new JSONObject();
			rt = "Location missed";
			json.put("rt", rt);
			modelAndView.setViewName("/goods/view.jsp");
			modelAndView.addObject("json", json);
			return modelAndView;
		}
		double lat = Double.parseDouble(strLat);
		
		String strLng = request.getParameter("lng");
		double lng = 0;
		if(strLng != null) {
			lng = Double.parseDouble(strLng);
		}
		
		String strCoverage = request.getParameter("coverage");
		int coverage = 0;
		if(strCoverage != null) {
			coverage = Integer.parseInt(strCoverage);
		}
		
		double latRate=0;
		double lngRate=0;
		if(coverage == 500) {
			latRate = LAT_RATE_500;
			lngRate = LNG_RATE_500;
		} else if(coverage == 1) {
			latRate = LAT_RATE_1;
			lngRate = LNG_RATE_1;
		} else if(coverage == 3) {
			latRate = LAT_RATE_3;
			lngRate = LNG_RATE_3;
		}
		
		String category1 = request.getParameter("category1");
		String category2 = request.getParameter("category2");
		String category3 = request.getParameter("category3");
		String category4 = request.getParameter("category4");
		String category5 = request.getParameter("category5");
		String category6 = request.getParameter("category6");
		String category7 = request.getParameter("category7");
		String category8 = request.getParameter("category8");
		String category9 = request.getParameter("category9");
		String category10 = request.getParameter("category10");
		String category11 = request.getParameter("category11");
		String category12 = request.getParameter("category12");
		
		Category category = new Category();
		category.setLat(lat);
		category.setLng(lng);
		category.setLatRate(latRate);
		category.setLngRate(lngRate);
		category.setCategory1(category1);
		category.setCategory2(category2);
		category.setCategory3(category3);
		category.setCategory4(category4);
		category.setCategory5(category5);
		category.setCategory6(category6);
		category.setCategory7(category7);
		category.setCategory8(category8);
		category.setCategory9(category9);
		category.setCategory10(category10);
		category.setCategory11(category11);
		category.setCategory12(category12);
		
		List<Goods_boardDTO> list = service.selectAroundMarkers(category);
		
		
		final double finalLat = lat;
		final double finalLng = lng;
		System.out.println("lat = " + finalLat);
		System.out.println("lng = " + finalLng);
		
		int filter = 0;
		if(coverage == 500) filter = 500;
		else if(coverage == 1) filter = 1000;
		else if(coverage == 3) filter = 3000;
		final int f_filter = filter;
		
		final ConvertToDistance convertToDistance = new ConvertToDistance();
		ArrayList<Goods_boardDTO> orderedList = 
			list
			.stream()
			.map(dto ->{
				double distance = convertToDistance.getDistance(finalLat, finalLng, dto.getLat(), dto.getLng());
				System.out.println("distance = " + distance);
				dto.setDist(distance);
				return dto;
			})
			.filter(dto -> {
					if(dto.getDist() > f_filter) return false;
					return true;
			})
			.sorted((front, rear) -> {
				if(rear.getDist() < front.getDist()) return 1;
				else return -1;
			})
			.collect(Collectors.toCollection(ArrayList::new));
		
		// JSON
		int total = orderedList.size(); // 조회된 데이터 수
		
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
			
			for (int i = 0; i < orderedList.size(); i++) {
				Goods_boardDTO boardDTO = orderedList.get(i);
				
				JSONObject temp = new JSONObject();
				temp.put("lat", boardDTO.getLat());
				temp.put("lng", boardDTO.getLng());
				
				temp.put("num", boardDTO.getNum());
	            temp.put("subject", boardDTO.getSubject());
	            temp.put("category_code", boardDTO.getCategory_code());
	            
	            
				temp.put("user_name", boardDTO.getUser_name());
				temp.put("area", boardDTO.getArea());
				
				double distance = boardDTO.getDist();
	            String strDist="";
	            if(distance >= 1000) {
	               strDist = ((int)(Math.floor(distance / 100)) / 10) + "km";
	            } else {
	               strDist = (int)Math.floor(distance) + "m";
	            }
	            temp.put("distance", strDist);
	            temp.put("price", boardDTO.getPrice());

	            
	            temp.put("content", boardDTO.getContent());
	            temp.put("image0", boardDTO.getImage0());
	            temp.put("image1", boardDTO.getImage1());
	            temp.put("image2", boardDTO.getImage2());
	            temp.put("board_date", boardDTO.getBoard_date());
	            
	            
				temp.put("readcount", boardDTO.getReadcount());
				temp.put("reply_count", boardDTO.getReply_count());
				temp.put("interest_count", boardDTO.getInterest_count());
				
				item.add(i, temp);
			}
			
			json.put("item", item);
		}

		System.out.println(json);
		modelAndView.addObject("json", json);
		modelAndView.setViewName("/goods/list.jsp");

		return modelAndView;
	}
	
	
	
	
	
	
	
	@RequestMapping(value = "/goods/interestList.do",method = RequestMethod.POST)
	public ModelAndView interestList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_num = request.getParameter("num");
		
		int num = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}
		// DB
		List<Goods_boardDTO> list = service.interestList(num);

		// JSON
		String rt = null;
		
		int total = list.size(); // 조회된 데이터 수
		System.out.println("list.size() = " + list.size());
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
				Goods_boardDTO boardDTO = list.get(i);
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
					int j = 0;
					item.add(j++, temp);
				
				json.put("item", item);
			}
		}

		System.out.println(json);
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/changeSellComplete.do",method = RequestMethod.POST)
	public ModelAndView changeSellComplete(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_num = request.getParameter("num");
		
		int num = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}
		// DB
		int total = service.changeSellComplete(num);
		String rt = null; 
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
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/changeSale.do",method = RequestMethod.POST)
	public ModelAndView changeSale(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_num = request.getParameter("num");
		
		int num = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}
		// DB
		int total = service.changeSale(num);
		String rt = null; 
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
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/hideOn.do",method = RequestMethod.POST)
	public ModelAndView hideOn(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_num = request.getParameter("num");
		
		int num = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}
		// DB
		int total = service.hideOn(num);
		String rt = null; 
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
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/hideOff.do",method = RequestMethod.POST)
	public ModelAndView hideOff(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_num = request.getParameter("num");
		
		int num = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}
		// DB
		int total = service.hideOff(num);
		String rt = null; 
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
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/reviewComplete.do",method = RequestMethod.POST)
	public ModelAndView reviewComplete(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String str_num = request.getParameter("num");
		
		int num = 0;
		if (str_num != null) {
			num = Integer.parseInt(str_num);
		}
		// DB
		int total = service.reviewComplete(num);
		String rt = null; 
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
		modelAndView.setViewName("/goods/list.jsp");
		// modelAndView.addObject("su",su);
		modelAndView.addObject("json", json);
		
		// System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/replyOneDelete.do")
	public ModelAndView replyOneDelete(HttpServletRequest request) {
		String str_rno = request.getParameter("rno");
		String str_bno = request.getParameter("bno");
		System.out.println("넘어온 수" + str_rno);
		System.out.println("넘어온 수" + str_bno);
		int rno = 0;
		if (str_rno != null) {
			rno = Integer.parseInt(str_rno);
		}
		int bno = 0;
		if (str_bno != null) {
			bno = Integer.parseInt(str_bno);
		}

		int su = 0;
		String rt = "";
		try {
			su = service.replyOneDelete(rno);
			service.downReC(bno);
		} catch (Exception e) {
			rt = "Fail";
			e.printStackTrace();
		}

		if (su > 0) {
			rt = "OK";
		} else {
			rt = "Fail";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		System.out.println(json);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/goods/list.jsp");
		modelAndView.addObject("su", su);
		modelAndView.addObject("json", json);

		System.out.println("su = " + su);

		return modelAndView;
	}
	
	@RequestMapping(value = "/goods/updateDate.do", method = RequestMethod.POST)
	public ModelAndView mannerUpdate(HttpServletRequest request) {
		// 데이터 처리
		// post 방식에서의 한글 처리
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String str_num = request.getParameter("num");
		
		int num = 0;
		if (str_num != null) {
			if (!str_num.trim().equals("") && str_num.matches("^[0-9]*$")) {
				num = Integer.parseInt(str_num);
			}
		}
		System.out.println("끌어올리기 = " + num);
	
		// DB 처리
		int su = 0;
		su = service.updateDate(num);
		System.out.println("date가 변화됨?" + num);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/users/modify.jsp");
		return modelAndView;
	}

	
	
	private String uploadFile(String fileName, byte[] fileData) throws Exception{
		String filePath = "D:\\android_3rd_kimsehoon\\spring\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\hongdangmu0902\\storage";
		UUID uuid = UUID.randomUUID();
		String saveName = uuid.toString() + "_" + fileName;
		File target = new File(filePath, saveName);
		FileCopyUtils.copy(fileData, target);
		
		return saveName;
		
	}
	
	

}
