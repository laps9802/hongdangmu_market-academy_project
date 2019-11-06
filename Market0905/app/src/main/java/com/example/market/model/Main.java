package com.example.market.model;

import java.io.Serializable;

public class Main implements Serializable {
    // 공용
    private String user_name;    // -- 사용자 이름(닉네임)
    private String area;   //  -- 지역

    // 사용자 부분
    private int user_code;  //   -- 판매자 코드
    private String user_photo;  // -- 프로필 사진
    private double manner;     // -- 평점
    private double reply_percent; // -- 응답률
    private int sales_count;     // -- 판매 개수
    private int purchase_count; // -- 구매 개수
    private String join_date;  //   -- 가입 날짜
    private String user_tel; // 사용자 전화번호
    private String user_email1; // 사용자 이메일1
    private String user_email2; // 사용자 이메일2
    private double lat; // 위도
    private double lng; // 경도
    private String distance; //거리
    // 물품 부분
    private String image0;
    private String image1;
    private String image2;
    private int img;    // 메인이미지 테스트용이라 int 서버연동시 url이므로 string으로 변환해야함
    private int num;
    private String category_code; //  -- 물품분류
    private String goods_name;     //   -- 상품 이름
    private int goods_price;        //   --상품 가격
    private int bargain_tf;     // -- 흥정가능 유무
    private int chat_count;     //   -- 채팅 개수
    private int reply_count;   //  -- 댓글 갯수
    private int interest_count; //  -- 관심 갯수
    private int read_count; // -- 조회수
    private String reg_date; //  -- 작성 날짜
    private String users_pass; //  -- 글 비밀번호
    private String board_subject; // -- 글 제목
    private String content; // -- 글 내용
    private String board_file; // -- 첨부파일
    private int board_re_ref; // -- 관련글번호
    private int board_re_lev; // -- 답글 레벨
    private int board_re_seq; // -- 관련글 중 출력순서
    private String sell_tf;
    private String hide_tf;
    private String review_tf;

    private String re_chat;
    private String board_date;
    private int rno;

    // 거래 후기
    private String seller;          // 판매자
    private String buyer;           // 구매자
    private String review_content; // 거래 후기 내용
    private String reviewer;        // (buyer or seller)로 구매자와 판매자 중 누가 작성하였는지
    private String review_date;     // 작성일

    private int mno; // --쪽지 번호
    private String sender; // -- 보낸 사람
    private String recipient;// -- 받는 사람
    private String msgTitle; //-- 쪽지 제목
    private String msgContent; // --쪽지 내용
    private String msgDate; // -- 보낸 날짜

    public Main() {
    }

    public Main(String reg_date, String area, String user_name, String re_chat, int img) {
        this.reg_date = reg_date;
        this.area = area;
        this.user_name = user_name;
        this.re_chat = re_chat;
        this.img = img;
    }

    public Main(String user_name, String image0, int user_code) {
        this.user_name = user_name;
        this.image0 = image0;
        this.user_code = user_code;
    }

    public int getRead_count() {
        return read_count;
    }

    public int getRno() {
        return rno;
    }

    public void setRno(int rno) {
        this.rno = rno;
    }

    public Main(String image0, int goods_price, int read_count, String reg_date, String board_subject, String area, String category_code, String content, int user_code, String user_name, int interest_count, int chat_count, int sales_count, double manner, double reply_percent) {
        this.user_name = user_name;
        this.area = area;
        this.user_code = user_code;
        this.manner = manner;
        this.reply_percent = reply_percent;
        this.sales_count = sales_count;
        this.image0 = image0;
        this.category_code = category_code;
        this.goods_price = goods_price;
        this.content = content;
        this.chat_count = chat_count;
        this.interest_count = interest_count;
        this.read_count = read_count;
        this.reg_date = reg_date;
        this.board_subject = board_subject;
    }
    // 유저 부분 처리(세훈)
    public Main(int user_code, String user_name, String user_photo, String area, int manner, int reply_percent, int sales_count, int purchase_count, int interest_count, String join_date, String user_tel, String user_email1, String user_email2, int lat, int lng) {
        this.user_code = user_code;
        this.user_name = user_name;
        this.user_photo = user_photo;
        this.area = area;
        this.manner = manner;
        this.reply_percent = reply_percent;
        this.sales_count = sales_count;
        this.purchase_count = purchase_count;
        this.interest_count = interest_count;
        this.join_date = join_date;
        this.user_tel = user_tel;
        this.user_email1 = user_email1;
        this.user_email2 = user_email2;
        this.lat = lat;
        this.lng = lng;
    }

    public Main(int user_code) {
        this.user_code = user_code;
    }

    // 물품(세훈)
    public Main(int num, String image0, String image1, String image2, String user_name, String board_subject, String category_code, String area, int goods_price, String content, int reply_count, int interest_count, int read_count, double lat, double lng, String board_date, String sell_tf, String hide_tf, String review_tf) {
        this.num = num;
        this.image0 = image0;
        this.image1 = image1;
        this.image2 = image2;
        this.user_name = user_name;
        this.board_subject = board_subject;
        this.category_code = category_code;
        this.area = area;
        this.goods_price = goods_price;
        this.content = content;
        this.reply_count = reply_count;
        this.interest_count = interest_count;
        this.read_count = read_count;
        this.lat = lat;
        this.lng = lng;
        this.board_date = board_date;
        this.sell_tf = sell_tf;
        this.hide_tf = hide_tf;
        this.review_tf = review_tf;
    }

    public Main(int num, String image0, String image1, String image2, String user_name, String board_subject, String category_code, String area, int goods_price, String content, int reply_count, int interest_count, int read_count, double lat, double lng, String board_date) {
        this.num = num;
        this.image0 = image0;
        this.image1 = image1;
        this.image2 = image2;
        this.user_name = user_name;
        this.board_subject = board_subject;
        this.category_code = category_code;
        this.area = area;
        this.goods_price = goods_price;
        this.content = content;
        this.reply_count = reply_count;
        this.interest_count = interest_count;
        this.read_count = read_count;
        this.lat = lat;
        this.lng = lng;
        this.board_date = board_date;
    }

    public Main(String reviewer_image, String reviewer_area, String reviewer, String reviewer_content, String review_date) {
        this.user_photo = reviewer_image;
        this.area = reviewer_area;
        this.user_name = reviewer;
        this.review_content = reviewer_content;
        this.review_date = review_date;
    }

    // 자신이 받은 후기 목록


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getUser_code() {
        return user_code;
    }

    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public double getManner() {
        return manner;
    }

    public void setManner(double manner) {
        this.manner = manner;
    }

    public double getReply_percent() {
        return reply_percent;
    }

    public void setReply_percent(double reply_percent) {
        this.reply_percent = reply_percent;
    }

    public int getSales_count() {
        return sales_count;
    }

    public void setSales_count(int sales_count) {
        this.sales_count = sales_count;
    }

    public int getPurchase_count() {
        return purchase_count;
    }

    public void setPurchase_count(int purchase_count) {
        this.purchase_count = purchase_count;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public String getUser_email1() {
        return user_email1;
    }

    public void setUser_email1(String user_email1) {
        this.user_email1 = user_email1;
    }

    public String getUser_email2() {
        return user_email2;
    }

    public void setUser_email2(String user_email2) {
        this.user_email2 = user_email2;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getImage0() {
        return image0;
    }

    public void setImage0(String image0) {
        this.image0 = image0;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(int goods_price) {
        this.goods_price = goods_price;
    }

    public int getBargain_tf() {
        return bargain_tf;
    }

    public void setBargain_tf(int bargain_tf) {
        this.bargain_tf = bargain_tf;
    }

    public int getChat_count() {
        return chat_count;
    }

    public void setChat_count(int chat_count) {
        this.chat_count = chat_count;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public int getInterest_count() {
        return interest_count;
    }

    public void setInterest_count(int interest_count) {
        this.interest_count = interest_count;
    }

    public int getRead_count(int readcount) {
        return read_count;
    }

    public void setRead_count(int read_count) {
        this.read_count = read_count;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getUsers_pass() {
        return users_pass;
    }

    public void setUsers_pass(String users_pass) {
        this.users_pass = users_pass;
    }

    public String getBoard_subject() {
        return board_subject;
    }

    public void setBoard_subject(String board_subject) {
        this.board_subject = board_subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBoard_file() {
        return board_file;
    }

    public void setBoard_file(String board_file) {
        this.board_file = board_file;
    }

    public int getBoard_re_ref() {
        return board_re_ref;
    }

    public void setBoard_re_ref(int board_re_ref) {
        this.board_re_ref = board_re_ref;
    }

    public int getBoard_re_lev() {
        return board_re_lev;
    }

    public void setBoard_re_lev(int board_re_lev) {
        this.board_re_lev = board_re_lev;
    }

    public int getBoard_re_seq() {
        return board_re_seq;
    }

    public void setBoard_re_seq(int board_re_seq) {
        this.board_re_seq = board_re_seq;
    }

    public String getSell_tf() {
        return sell_tf;
    }

    public void setSell_tf(String sell_tf) {
        this.sell_tf = sell_tf;
    }

    public String getHide_tf() {
        return hide_tf;
    }

    public void setHide_tf(String hide_tf) {
        this.hide_tf = hide_tf;
    }

    public String getReview_tf() {
        return review_tf;
    }

    public void setReview_tf(String review_tf) {
        this.review_tf = review_tf;
    }

    public String getRe_chat() {
        return re_chat;
    }

    public void setRe_chat(String re_chat) {
        this.re_chat = re_chat;
    }

    public String getBoard_date() {
        return board_date;
    }

    public void setBoard_date(String board_date) {
        this.board_date = board_date;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public int getMno() {
        return mno;
    }

    public void setMno(int mno) {
        this.mno = mno;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
