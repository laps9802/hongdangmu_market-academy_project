package com.example.market.constants;

import java.net.URL;

public class Constants {
    //전역 데이터, 상수
    public static final int categorySize = 12;
    public static int memberType;
    public static String[] categoryArr = {"디지털/가전", "가구/인테리어", "유아동/유아도서", "생활/가공식품", "여성의류", "여성잡화",
            "뷰티/미용", "남성패션/잡화", "스포츠/레저", "게임/취미", "도서/티켓/음반","반려동물용품"};


    //요청 코드
    public static int LOCINIT_GPS_ACTIVATION = 1;
    public static int PERMISSION_REQUEST_CODE = 10;
    public static int SMS_AUTH_CODE = 99;

    //URL
    public static final String SellBuyAdapterUpdateDateURL = "http://192.168.0.7:8080/hongdangmu/goods/updateDate.doo";

    public static final String ReviewWaitFragment2URL = "http://192.168.0.7:8080/hongdangmu/purchase/list.do";
    public static final String SellCompleteActivityURL2 = "http://192.168.0.7:8080/hongdangmu/buy_tbl/myBuylist.do";
    public static final String SellCompleteAdapterURL = "http://192.168.0.7:8080/hongdangmu/purchase/write.do";
    public static final String ReviewWriteActivityURL_MANNER = "http://192.168.0.7:8080/hongdangmu/users/mannerUpdate.do";

    public static final String SellBuyAdapterURL1 = "http://192.168.0.7:8080/hongdangmu/goods/hideOff.do";
    public static final String SellBuyAdapterURL_hide = "http://192.168.0.7:8080/hongdangmu/goods/hideOn.do";
    public static final String SellBuyAdapterURL_sale = "http://192.168.0.7:8080/hongdangmu/goods/changeSale.do";
    public static final String SellBuyAdapterURL_delete = "http://192.168.0.7:8080/hongdangmu/goods/boardDelete.do";
    public static final String PeopleActivityURL = "http://192.168.0.7:8080/hongdangmu/users/selectNameList.do";
    public static final String SetActivityURL = "http://192.168.0.7:8080/hongdangmu/users/selectOne2.do";
    public static final String TradeActivityURL = "http://192.168.0.7:8080/hongdangmu/goods/searchList.do";
    public static final String ChangeProfileURL = "http://192.168.0.7:8080/hongdangmu/users/modify.do";
    public static final String ProfileURL = "http://192.168.0.7:8080/hongdangmu/goods/mySaleList.do";
    public static final String ProfileURL2 = "http://192.168.0.7:8080/hongdangmu/review/selectList.do";
    public static final String ReviewResultActivityURL = "http://192.168.0.7:8080/hongdangmu/goods/reviewComplete.do";
    public static final String ReviewWriteActivityURL = "http://192.168.0.7:8080/hongdangmu/review/write.do";
    public static final String ReviewWriteActivityURL2 = "http://192.168.0.7:8080/hongdangmu/goods/reviewComplete.do";
    public static final String SellCompleteActivityURL = "http://192.168.0.7:8080/hongdangmu/goods/changeSellComplete.do";
    public static final String AllReviewFragmentURL = "http://192.168.0.7:8080/hongdangmu/review/selectList.do";
    public static final String BuyerReviewFragmentURL = "http://192.168.0.7:8080/hongdangmu/review/bList.do";
    public static final String InterestFragmentURL = "http://192.168.0.7:8080/hongdangmu/int_tbl/interestList.do";
    public static final String ReviewCompleteFragmentURL = "http://192.168.0.7:8080/hongdangmu/goods/reviewCompleteList.do";
    public static final String ReviewWaitFragmentURL = "http://192.168.0.7:8080/hongdangmu/goods/reviewIngList.do";
    public static final String SaleFragmentURL = "http://192.168.0.7:8080/hongdangmu/goods/mySaleList.do";
    public static final String SellCompleteFragmentURL = "http://192.168.0.7:8080/hongdangmu/goods/mySaleCompleteList.do";
    public static final String SellerReviewFragmentURL = "http://192.168.0.7:8080/hongdangmu/review/sList.do";
    public static final String SellHideFragmentURL = "http://192.168.0.7:8080/hongdangmu/goods/hideList.do";
    public static final String ChatAllActivityURL = "http://192.168.0.7:8080/hongdangmu/goods/replyList.do";
    public static final String MainActivityurl2 = "http://192.168.0.7:8080/hongdangmu/goods/hotList.do"; //start프래그먼트에 넘길 url
    public static final String ReplyActivityURL = "http://192.168.0.7:8080/hongdangmu/goods/replyWrite.do";
    public static final String ShowAllActivityURL = "http://192.168.0.7:8080/hongdangmu/goods/mySaleList.do";

    public static final String InterestListAdapterURL_ON = "http://192.168.0.7:8080/hongdangmu/int_tbl/write.do";
    public static final String InterestListAdapterURL_OFF = "http://192.168.0.7:8080/hongdangmu/int_tbl/delete.do";

    public static final String ChatAdapterURL = "http://192.168.0.7:8080/hongdangmu/goods/replyOneDelete.do";


    public static final String ServerURL = "http://192.168.0.7:8080";

    public static final String AuthPhoneNumberURL = "http://192.168.0.7:8080/hongdangmu/users/selectIsMember.do";
    public static final String MainInsertUserURL = "http://192.168.0.7:8080/hongdangmu/users/insertUser.do";
    public static final String MainSelectExistingUserURL = "http://192.168.0.7:8080/hongdangmu/users/selectExistingUser.do";

    public static final String reverseGeocoding_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&orders=admcode&output=json";
    public static final String locationList_URL = "http://192.168.0.7:8080/hongdangmu/goods/listLocation.do";
    public static final String searchInLocationURL = "http://192.168.0.7:8080/hongdangmu/goods/locationSearch.do";
    public static final String startActivityURL = "http://192.168.0.7:8080/hongdangmu/goods/list.do";
    public static final String msgActivityURL = "http://192.168.0.7:8080/hongdangmu/msg/msgWrite.do";
    public static final String msgRoomActivityURL = "http://192.168.0.7:8080/hongdangmu/msg/msgRoomList.do";
    public static final String chatActivityURL = "http://192.168.0.7:8080/hongdangmu/msg/msgList.do";
    public static final String selectOne2URL   = "http://192.168.0.7:8080/hongdangmu/users/selectOne2.do";

    public static final String WriteURL = "http://192.168.0.7:8080/hongdangmu/goods/write.do";
    public static final String ModifyURL = "http://192.168.0.7:8080/hongdangmu/goods/boardView.do";
    public static final String ModifyURL2 = "http://192.168.0.7:8080/hongdangmu/goods/boardModify.do";

    public static final String ResultURL = "http://192.168.0.7:8080/hongdangmu/goods/boardView.do";
    public static final String ResultURL2 = "http://192.168.0.7:8080/hongdangmu/goods/boardDelete.do";
    public static final String ResultURL3 = "http://192.168.0.7:8080/hongdangmu/goods/replyList.do";
    public static final String ResultURL4 = "http://192.168.0.7:8080/hongdangmu/users/selectOne2.do";
    public static final String ResultURL5 = "http://192.168.0.7:8080/hongdangmu/goods/mySaleList.do";

    public static final String MapURL = "http://192.168.0.7:8080/hongdangmu/goods/selectAroundMarkers.do";


    //사용자 정보
    public static String nickname;
    public static String phoneNumber;
    public static String area;
    public static double lat;
    public static double lng;

}
