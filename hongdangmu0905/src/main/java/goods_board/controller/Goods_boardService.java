package goods_board.controller;

import java.util.List;

import goods_board.bean.Category;
import goods_board.bean.Goods_boardDTO;
import goods_board.bean.ReplyDTO;


public interface Goods_boardService {
	public int boardWrite(Goods_boardDTO boardDTO);
	public int boardDelete(int num);
	public int updateRC(int num);
	public int updateIC(int num);
	public int boardModify(Goods_boardDTO boardDTO);
	public Goods_boardDTO boardView(int num);
	public List<Goods_boardDTO> boardList(int startNum, int endNum);
	public int getTotalA();
	public List<Goods_boardDTO> list(Category category);
	public List<Goods_boardDTO> searchList(String keyword1,String keyword2);
	public int replyWrite(ReplyDTO replyDTO);
	public int updateReC(int num);
	public List<ReplyDTO> replyList(int bno);
	public List<Goods_boardDTO> saleList();
	public List<Goods_boardDTO> saleCompleteList();
	public List<Goods_boardDTO> mySaleList(String user_name);
	public List<Goods_boardDTO> mySaleCompleteList(String user_name);
	public List<Goods_boardDTO> hideList(String user_name);
	public List<Goods_boardDTO> reviewIngList(String user_name);
	public List<Goods_boardDTO> reviewCompleteList(String user_name);
	public List<Goods_boardDTO> modifyName(String muser_name, String user_name);
	public List<Goods_boardDTO> hotList(Category category);
	
	public List<Goods_boardDTO> listLocation(Category category);
	public List<Goods_boardDTO> listSearchInLocation(Category category);
	public List<Goods_boardDTO> selectAroundMarkers(Category category);
	
	
	public List<Goods_boardDTO> interestList(int num);
	public int changeSellComplete(int num);
	public int changeSale(int num);
	public int hideOn(int num);
	public int hideOff(int num);
	public int reviewComplete(int num);
	public int interestOn(int num);
	public int interestOff(int num);
	public int replyOneDelete(int rno);
	public int replyDelete(int bno);
	public int downReC(int num);
	public List<ReplyDTO> replyName(String muser_name, String user_name);

	
	public int updateDate(int num);
}