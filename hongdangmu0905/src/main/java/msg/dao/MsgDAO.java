package msg.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import msg.bean.MsgDTO;

@Repository
public class MsgDAO {
	@Autowired
	private SqlSessionTemplate sessionTemplate;
	public int msgWrite(MsgDTO msgDTO) {
		return sessionTemplate.insert("mybatis.MsgMapper.msgWrite",msgDTO);
	}
	public List<MsgDTO> msgList(String recipient){
		return sessionTemplate.selectList("mybatis.MsgMapper.msgList",recipient);
	}
	public List<MsgDTO> msgRoomList(String recipient,String sender){
		Map<String, String> map = new HashMap<String, String>();
		map.put("recipient", recipient);
		map.put("sender", sender);
		return sessionTemplate.selectList("mybatis.MsgMapper.msgRoomList",map);
	}
}
