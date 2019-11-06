package goods_board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import goods_board.bean.Category;
import goods_board.bean.Goods_boardDTO;
import goods_board.bean.ReplyDTO;
import goods_board.dao.Goods_boardDAO;
@Service
public class Goods_boardServiceImpl implements Goods_boardService{
	@Autowired
	private Goods_boardDAO dao;
	public int boardWrite(Goods_boardDTO boardDTO) {
		return dao.boardWrite(boardDTO);
	}

	public int boardDelete(int num) {
		return dao.boardDelete(num);
	}

	public int updateRC(int num) {
		return dao.updateRC(num);
	}

	public int updateIC(int num) {
		return dao.updateIC(num);
	}

	public int boardModify(Goods_boardDTO boardDTO) {
		return dao.boardModify(boardDTO);
	}

	public Goods_boardDTO boardView(int num) {
		return dao.boardView(num);
	}

	public List<Goods_boardDTO> boardList(int startNum, int endNum) {
		return dao.boardList(startNum, endNum);
	}

	public int getTotalA() {
		return dao.getTotalA();
	}

	public List<Goods_boardDTO> list(Category category) {
		return dao.list(category);
	}
	public List<Goods_boardDTO> searchList(String keyword1,String keyword2){
		return dao.searchList(keyword1,keyword2);
		
	}
	public int replyWrite(ReplyDTO replyDTO) {
		return dao.replyWrite(replyDTO);
	}
	public int updateReC(int num) {
		return dao.updateReC(num);
	}
	public List<ReplyDTO> replyList(int bno){
		return dao.replyList(bno);
	}

	public List<Goods_boardDTO> saleList() {
		return dao.saleList();
	}

	public List<Goods_boardDTO> saleCompleteList() {
		return dao.saleCompleteList();
	}

	public List<Goods_boardDTO> modifyName(String muser_name, String user_name) {
		return dao.modifyName(muser_name, user_name);
	}

	public List<Goods_boardDTO> mySaleList(String user_name) {
		return dao.mySaleList(user_name);
	}

	public List<Goods_boardDTO> mySaleCompleteList(String user_name) {
		return dao.mySaleCompleteList(user_name);
	}

	public List<Goods_boardDTO> hideList(String user_name) {
		return dao.hideList(user_name);
	}

	public List<Goods_boardDTO> reviewIngList(String user_name) {
		return dao.reviewIngList(user_name);
	}

	public List<Goods_boardDTO> reviewCompleteList(String user_name) {
		return dao.reviewCompleteList(user_name);
	}

	public List<Goods_boardDTO> hotList(Category category) {
		return dao.hotList(category);
	}

	public List<Goods_boardDTO> interestList(int num) {
		return dao.interestList(num);
	}

	public int changeSellComplete(int num) {
		return dao.changeSellComplete(num);
	}

	public int changeSale(int num) {
		return dao.changeSale(num);
	}
	
	public int hideOn(int num) {
		return dao.hideOn(num);
	}

	public int hideOff(int num) {
		return dao.hideOff(num);
	}
	
	public int reviewComplete(int num) {
		return dao.reviewComplete(num);
	}

	public int interestOn(int num) {
		return dao.interestOn(num);
	}

	public int interestOff(int num) {
		return dao.interestOff(num);
	}

	public int replyDelete(int bno) {
		return dao.replyDelete(bno);
	}
	public int replyOneDelete(int rno) {
		return dao.replyOneDelete(rno);
	}

	public int downReC(int num) {
		return dao.downReC(num);
	}
	public List<ReplyDTO> replyName(String muser_name, String user_name) {  
		return dao.replyname(muser_name, user_name);
	}

	public List<Goods_boardDTO> listLocation(Category category) {
		return dao.listLocation(category);
	}
	
	public List<Goods_boardDTO> listSearchInLocation(Category category) {
		return dao.listSearchInLocation(category);
	}
	@Override
	public List<Goods_boardDTO> selectAroundMarkers(Category category) {
		// TODO Auto-generated method stub
		return dao.selectAroundMarkers(category);
	}
	

	@Override
	public int updateDate(int num) {
		return dao.updateDate(num);
	}
}
