package msg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import msg.bean.MsgDTO;
import msg.dao.MsgDAO;

@Service
public class MsgServiceImpl implements MsgService {
	@Autowired
	private MsgDAO dao;

	public int msgWrite(MsgDTO msgDTO) {
		return dao.msgWrite(msgDTO);
	}

	public List<MsgDTO> msgList(String recipient) {
		return dao.msgList(recipient);
	}

	public List<MsgDTO> msgRoomList(String recipient, String sender) {
		return dao.msgRoomList(recipient, sender);
	}

}
