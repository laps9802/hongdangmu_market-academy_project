package com.example.market;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.market.model.Main;

public class UserActivity extends AppCompatActivity {
    ImageView user_icon_imageView,user_mannerScore_imageView;
    TextView  user_nameId_tv,user_sell_tv;
    Button user_manner_btn,user_list_btn;
    LinearLayout user_sellList_layout,user_mannerList_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        user_icon_imageView = findViewById(R.id.user_icon_imageView);   // 유저 프로필
        user_mannerScore_imageView = findViewById(R.id.user_mannerScore_imageView); // 유저 평점
        user_nameId_tv = findViewById(R.id.user_nameId_tv); // 유저 이름과 ID
        user_sell_tv = findViewById(R.id.user_sell_tv); // 판매중 상품을 표시
        user_manner_btn = findViewById(R.id.user_manner_btn);   // 매너평가
        user_list_btn = findViewById(R.id.user_list_btn);       // 유저의 판매중 상품
        user_sellList_layout = findViewById(R.id.user_sellList_layout); // 판매상품 줄 레이아웃
        user_mannerList_layout = findViewById(R.id.user_mannerList_layout); // 매너평가 줄 레이아웃

        Main item = (Main) getIntent().getSerializableExtra("item");
        user_icon_imageView.setImageResource(R.drawable.mio);               // 서버연동후엔 파일 받아올 예정
        user_mannerScore_imageView.setImageResource(R.mipmap.ic_launcher);  // 서버연동후엔 파일 받아올 예정
        user_nameId_tv.setText(item.getUser_name() + "  " + item.getUser_code());
        user_sell_tv.setText("판매상품 " + item.getSales_count() + "개");


    }
}
