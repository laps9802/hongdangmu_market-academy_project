package msg.controller;

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
import msg.bean.MsgDTO;
@Controller
public class MsgController {
	@Autowired
	private MsgService service;

	@RequestMapping(value = "/msg/msgWrite.do", method = RequestMethod.POST)
	public ModelAndView writeMsg(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String user_photo = request.getParameter("user_photo");
		String user_area = request.getParameter("user_area");
		String str_bno = request.getParameter("bno");
		String sender = request.getParameter("sender");
		String recipient = request.getParameter("recipient");
		String msgTitle = request.getParameter("msgTitle");
		String msgContent = request.getParameter("msgContent");

		int bno = 0;
		if(str_bno != null) {
			bno = Integer.parseInt(str_bno);
		}
		MsgDTO msgDTO = new MsgDTO();
		msgDTO.setUser_photo(user_photo);
		msgDTO.setUser_area(user_area);
		msgDTO.setBno(bno);
		msgDTO.setSender(sender);
		msgDTO.setRecipient(recipient);
		msgDTO.setMsgTitle(msgTitle);
		msgDTO.setMsgContent(msgContent);
		int su = service.msgWrite(msgDTO);
		String rt;
		if(su>0) {
			rt = "OK";
		}else {
			rt = "FAIL";
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rt", rt);
		modelAndView.setViewName("/goods/list.jsp");
		modelAndView.addObject("su", su);
		modelAndView.addObject("json", jsonObject);
		System.out.println("su = " + su);
		return modelAndView;
	}
	@RequestMapping(value = "/msg/msgList.do", method = RequestMethod.POST)
	public ModelAndView msgList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		
		String recipient = request.getParameter("recipient");
		System.out.println("recipient = " + recipient);
		MsgDTO msgDTO = new MsgDTO();
		List<MsgDTO> list = service.msgList(recipient);
		int total = list.size();
		String rt;
		if(total>0) {
			rt = "OK";
		}else {
			rt = "FAIL";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);

		if (total > 0) {
			JSONArray item = new JSONArray();
			for (int i = 0; i < list.size(); i++) {

				msgDTO = list.get(i);

				JSONObject temp = new JSONObject();
				temp.put("bno", msgDTO.getBno());
				temp.put("sender", msgDTO.getSender());
				temp.put("msgtitle", msgDTO.getMsgTitle());
				temp.put("msgDate",msgDTO.getMsgDate());
				temp.put("msgContent",msgDTO.getMsgContent());
				temp.put("user_photo",msgDTO.getUser_photo());
				temp.put("user_area",msgDTO.getUser_area());
				System.out.println("i = " + i);

				/*
				 * }else { temp.put("none", "none"); }
				 */
				item.add(i, temp);
			}
			json.put("item", item);

		}
		System.out.println(json);
		modelAndView.addObject("list", list);
		modelAndView.addObject("json", json);
		
		modelAndView.setViewName("/goods/list.jsp");
		return modelAndView;
	}
	@RequestMapping(value = "/msg/msgRoomList.do", method = RequestMethod.POST)
	public ModelAndView msgRoomList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		
		String recipient = request.getParameter("recipient");
		System.out.println("recipient = " + recipient);
		String sender = request.getParameter("sender");
		System.out.println("sender = " + sender);
		MsgDTO msgDTO = new MsgDTO();
		List<MsgDTO> list = service.msgRoomList(recipient, sender);
		int total = list.size();
		String rt;
		if(total>0) {
			rt = "OK";
		}else {
			rt = "FAIL";
		}
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);

		if (total > 0) {
			JSONArray item = new JSONArray();
			for (int i = 0; i < list.size(); i++) {

				msgDTO = list.get(i);

				JSONObject temp = new JSONObject();
				temp.put("bno", msgDTO.getBno());
				temp.put("sender", msgDTO.getSender());
				temp.put("msgtitle", msgDTO.getMsgTitle());
				temp.put("msgDate",msgDTO.getMsgDate());
				temp.put("msgContent",msgDTO.getMsgContent());
				temp.put("user_photo",msgDTO.getUser_photo());
				temp.put("user_area",msgDTO.getUser_area());
				temp.put("recipient",msgDTO.getRecipient());
				System.out.println("i = " + i);

				/*
				 * }else { temp.put("none", "none"); }
				 */
				item.add(i, temp);
			}
			json.put("item", item);

		}
		System.out.println(json);
		modelAndView.addObject("list", list);
		modelAndView.addObject("json", json);
		
		modelAndView.setViewName("/goods/list.jsp");
		return modelAndView;
	}
}
