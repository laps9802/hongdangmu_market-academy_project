package users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import users.bean.UsersDTO;
import users.dao.UsersDAO;
@Service
public class UsersServiceImpl implements UsersService{
	@Autowired
	private UsersDAO dao;

	public int write(UsersDTO usersDTO) {
		// TODO Auto-generated method stub
		return dao.write(usersDTO);
	}

	public List<UsersDTO> login(String user_name, String user_tel) {
		// TODO Auto-generated method stub
		return dao.login(user_name, user_tel);
	}

	public String isExistId(int user_code) {
		// TODO Auto-generated method stub
		return dao.isExistId(user_code);
	}

	public UsersDTO selectOne(String user_name) {
		// TODO Auto-generated method stub
		return dao.selectOne(user_name);
	}
	
	public UsersDTO selectOne2(int user_code) {
		// TODO Auto-generated method stub
		return dao.selectOne2(user_code);
	}

	public int modify(UsersDTO usersDTO) {
		// TODO Auto-generated method stub
		return dao.modify(usersDTO);
	}

	public int delete(int user_code) {
		// TODO Auto-generated method stub
		return dao.delete(user_code);
	}

	public List<UsersDTO> selectList(int startNum, int endNum) {
		// TODO Auto-generated method stub
		return dao.selectList(startNum, endNum);
	}

	public int getTotalMember() {
		// TODO Auto-generated method stub
		return dao.getTotalMember();
	}

	public List<UsersDTO> list() {
		// TODO Auto-generated method stub
		return dao.list();
	}
	
	public List<UsersDTO> selectNameList(String user_name) {
		// TODO Auto-generated method stub
		return dao.selectNameList(user_name);
	}

	
	
	
	@Override
	public int selectIsMember(String users_tel) {
		// TODO Auto-generated method stub
		return dao.selectIsMember(users_tel);
	}

	@Override
	public int insertUser(UsersDTO usersDTO) {
		// TODO Auto-generated method stub 
		return dao.insertUser(usersDTO);
	}

	@Override
	public UsersDTO selectExistingUser(String users_tel) {
		return dao.selectExistingUser(users_tel);
	}

	@Override
	public int selectUserCode(String user_tel) {
		return dao.selectUserCode(user_tel);
	}

	
	
	@Override
	public int mannerUpdate(int manner, String user_name) {
		return dao.mannerUpdate(manner, user_name);
	}


}
