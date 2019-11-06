package goods_board.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import goods_board.bean.Category;
import goods_board.bean.Goods_boardDTO;
import goods_board.bean.ReplyDTO;
import int_tbl.bean.Int_tblDTO;


@Repository
public class Goods_boardDAO {
	@Autowired
	private SqlSessionTemplate sessionTemplate;
	// 데이터 저장
		public int boardWrite(Goods_boardDTO boardDTO) {
			
			return sessionTemplate.insert("mybatis.goods_boardMapper.boardWrite", boardDTO);
		}
		
		public List<Goods_boardDTO> boardList(int startNum, int endNum) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("startNum", startNum);
			map.put("endNum", endNum);
			return sessionTemplate.selectList("mybatis.goods_boardMapper.boardList", map);
		}
		// 1줄 검색
		public Goods_boardDTO boardView(int num) {
			
			return sessionTemplate.selectOne("mybatis.goods_boardMapper.boardView", num);
		}
		// 전체 리스트 검색
		public List<Goods_boardDTO> list(Category category) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.list", category);
		}
		// 총 글수 구하기
		public int getTotalA() {
			
			return sessionTemplate.selectOne("mybatis.goods_boardMapper.getTotalA");
		}
		// 조회수 증가
		public int updateRC(int num) {
			
			return sessionTemplate.update("mybatis.goods_boardMapper.updateRC", num);
		}
		// 관심수 증가
		public int updateIC(int num) {
					
			return sessionTemplate.update("mybatis.goods_boardMapper.updateIC", num);
		}
		// 글삭제
		public int boardDelete(int num) {
			
			return sessionTemplate.delete("mybatis.goods_boardMapper.boardDelete", num);
		}
		
		public int boardModify(Goods_boardDTO boardDTO) {
			
			return sessionTemplate.update("mybatis.goods_boardMapper.boardModify", boardDTO);		
		}
		public List<Goods_boardDTO> searchList(String keyword1,String keyword2) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("keyword1", keyword1);
			map.put("keyword2", keyword2);
			return sessionTemplate.selectList("mybatis.goods_boardMapper.searchList",map);
		}
		// 답글입력
		public int replyWrite(ReplyDTO replyDTO) {
			return sessionTemplate.insert("mybatis.goods_boardMapper.replyWrite",replyDTO);
		}
		// 답글 수 증가
		public int updateReC(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.updateReC",num);
		}
		// 답글 리스트
		public List<ReplyDTO> replyList(int bno) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.replyList",bno);
		}
		// 판매 중
		public List<Goods_boardDTO> saleList() {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.saleList");
		}
		// 판매 완료
		public List<Goods_boardDTO> saleCompleteList() {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.saleCompleteList");
		}
		public List<Goods_boardDTO> mySaleList(String user_name) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.mySaleList", user_name);
		}

		public List<Goods_boardDTO> mySaleCompleteList(String user_name) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.mySaleCompleteList", user_name);
		}

		public List<Goods_boardDTO> reviewIngList(String user_name) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.reviewIngList", user_name);
		}

		public List<Goods_boardDTO> reviewCompleteList(String user_name) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.reviewCompleteList", user_name);
		}

		public List<Goods_boardDTO> hideList(String user_name) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.hideList", user_name);
		}
		
		// 물품을 등록한 사람 이름 바꾸기위해서
		public List<Goods_boardDTO> modifyName(String muser_name, String user_name) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("muser_name", muser_name);
			map.put("user_name", user_name);
			return sessionTemplate.selectList("mybatis.goods_boardMapper.modifyName", map);
		}
		
		public List<Goods_boardDTO> hotList(Category category) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.hotList", category);
		}
		
		public List<Goods_boardDTO> interestList(int num) {
			return sessionTemplate.selectList("mybatis.goods_boardMapper.interestList", num);
		}
		public int changeSellComplete(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.changeSellComplete", num);
		}
		public int changeSale(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.changeSale", num);
		}
		public int hideOn(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.hideOn", num);
		}
		public int hideOff(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.hideOff", num);
		}
		public int reviewComplete(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.reviewComplete", num);
		}
		public int interestOn(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.interestOn", num);
		}
		public int interestOff(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.interestOff", num);
		}
		// 답글 수 감소
		public int downReC(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.downReC",num);
		}
		public int replyOneDelete(int rno) {
			return sessionTemplate.delete("mybatis.goods_boardMapper.replyOneDelete", rno);
		}
		public int replyDelete(int bno) {
			return sessionTemplate.delete("mybatis.goods_boardMapper.replyDelete",bno);
		}
		public List<ReplyDTO> replyname(String muser_name, String user_name) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("muser_name", muser_name);
			map.put("user_name", user_name);
			return sessionTemplate.selectList("mybatis.goods_boardMapper.modifyName", map);
		}
		
		public List<Goods_boardDTO> listLocation(Category category) {
			// TODO Auto-generated method stub
			return sessionTemplate.selectList("mybatis.goods_boardMapper.listLocation", category);
		}
		
		public List<Goods_boardDTO> listSearchInLocation(Category category) {
			// TODO Auto-generated method stub
			return sessionTemplate.selectList("mybatis.goods_boardMapper.listSearchInLocation", category);
		}
		
		public List<Goods_boardDTO> selectAroundMarkers(Category category) {
			// TODO Auto-generated method stub
			return sessionTemplate.selectList("mybatis.goods_boardMapper.selectAroundMarkers", category);
		}
		
		
		public int updateDate(int num) {
			return sessionTemplate.update("mybatis.goods_boardMapper.updateDate", num);
		}
		
}
