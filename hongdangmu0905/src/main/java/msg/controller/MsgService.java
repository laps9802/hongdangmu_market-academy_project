package msg.controller;

import java.util.List;

import msg.bean.MsgDTO;

public interface MsgService {
	public int msgWrite(MsgDTO msgDTO);
	public List<MsgDTO> msgList(String recipient);
	public List<MsgDTO> msgRoomList(String recipient,String sender);
}
