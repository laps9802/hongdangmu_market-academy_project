package users.controller;

import java.util.List;

import users.bean.UsersDTO;

public interface UsersService {
	public int write(UsersDTO usersDTO);
	public List<UsersDTO> login(String user_name, String user_tel);
	public String isExistId(int user_code);
	public UsersDTO selectOne(String user_name);
	public UsersDTO selectOne2(int user_code);
	public int modify(UsersDTO usersDTO);
	public int delete(int user_code);
	public List<UsersDTO> selectList(int startNum, int endNum);
	public int getTotalMember();
	public List<UsersDTO> list();
	public List<UsersDTO> selectNameList(String user_name);
	
	public int selectIsMember(String users_tel);
	public int insertUser(UsersDTO usersDTO);
	public UsersDTO selectExistingUser(String users_tel);
	
	public int selectUserCode(String user_tel);
	
	
	
	public int mannerUpdate(int manner, String user_name);
}
