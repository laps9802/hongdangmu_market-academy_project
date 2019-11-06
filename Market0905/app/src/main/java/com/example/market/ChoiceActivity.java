package com.example.market;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class ChoiceActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    CheckBox choice_1_ch,choice_2_ch,choice_3_ch,choice_4_ch,choice_5_ch,choice_6_ch,choice_7_ch,choice_8_ch,choice_9_ch,choice_10_ch,choice_11_ch,choice_12_ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>관심 카테고리 설정</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        choice_1_ch = findViewById(R.id.choice_1_ch); // 디지털/가전
        choice_2_ch = findViewById(R.id.choice_2_ch); // 가구/인테리어
        choice_3_ch = findViewById(R.id.choice_3_ch); // 유아동/유아도서
        choice_4_ch = findViewById(R.id.choice_4_ch); // 생활/가공식품
        choice_5_ch = findViewById(R.id.choice_5_ch); // 여성의류
        choice_6_ch = findViewById(R.id.choice_6_ch); // 여성잡화
        choice_7_ch = findViewById(R.id.choice_7_ch); // 뷰티/미용
        choice_8_ch = findViewById(R.id.choice_8_ch); // 남성패션/잡화
        choice_9_ch = findViewById(R.id.choice_9_ch); // 스포츠/레저
        choice_10_ch = findViewById(R.id.choice_10_ch); // 게임/취미
        choice_11_ch = findViewById(R.id.choice_11_ch); // 도서/티켓/음반
        choice_12_ch = findViewById(R.id.choice_12_ch); // 반려동물용품

        choice_1_ch.setOnCheckedChangeListener(this);
        choice_2_ch.setOnCheckedChangeListener(this);
        choice_3_ch.setOnCheckedChangeListener(this);
        choice_4_ch.setOnCheckedChangeListener(this);
        choice_5_ch.setOnCheckedChangeListener(this);
        choice_6_ch.setOnCheckedChangeListener(this);
        choice_7_ch.setOnCheckedChangeListener(this);
        choice_8_ch.setOnCheckedChangeListener(this);
        choice_9_ch.setOnCheckedChangeListener(this);
        choice_10_ch.setOnCheckedChangeListener(this);
        choice_11_ch.setOnCheckedChangeListener(this);
        choice_12_ch.setOnCheckedChangeListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        choice_1_ch.setChecked(pref.getBoolean("check1",false));
        choice_2_ch.setChecked(pref.getBoolean("check2",false));
        choice_3_ch.setChecked(pref.getBoolean("check3",false));
        choice_4_ch.setChecked(pref.getBoolean("check4",false));
        choice_5_ch.setChecked(pref.getBoolean("check5",false));
        choice_6_ch.setChecked(pref.getBoolean("check6",false));
        choice_7_ch.setChecked(pref.getBoolean("check7",false));
        choice_8_ch.setChecked(pref.getBoolean("check8",false));
        choice_9_ch.setChecked(pref.getBoolean("check9",false));
        choice_10_ch.setChecked(pref.getBoolean("check10",false));
        choice_11_ch.setChecked(pref.getBoolean("check11",false));
        choice_12_ch.setChecked(pref.getBoolean("check12",false));

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("check1", choice_1_ch.isChecked());
        editor.putBoolean("check2", choice_2_ch.isChecked());
        editor.putBoolean("check3", choice_3_ch.isChecked());
        editor.putBoolean("check4", choice_4_ch.isChecked());
        editor.putBoolean("check5", choice_5_ch.isChecked());
        editor.putBoolean("check6", choice_6_ch.isChecked());
        editor.putBoolean("check7", choice_7_ch.isChecked());
        editor.putBoolean("check8", choice_8_ch.isChecked());
        editor.putBoolean("check9", choice_9_ch.isChecked());
        editor.putBoolean("check10", choice_10_ch.isChecked());
        editor.putBoolean("check11", choice_11_ch.isChecked());
        editor.putBoolean("check12", choice_12_ch.isChecked());
        if (choice_1_ch.isChecked()){
            editor.putString("ch1",choice_1_ch.getText().toString().trim());
        }else {
            editor.putString("ch1",null);
        }
        if (choice_2_ch.isChecked()){
            editor.putString("ch2",choice_2_ch.getText().toString().trim());
        }else {
            editor.putString("ch2",null);
        }
        if (choice_3_ch.isChecked()){
            editor.putString("ch3",choice_3_ch.getText().toString().trim());
        }else {
            editor.putString("ch3",null);
        }
        if (choice_4_ch.isChecked()){
            editor.putString("ch4",choice_4_ch.getText().toString().trim());
        }else {
            editor.putString("ch4",null);
        }
        if (choice_5_ch.isChecked()){
            editor.putString("ch5",choice_5_ch.getText().toString().trim());
        }else {
            editor.putString("ch5",null);
        }
        if (choice_6_ch.isChecked()){
            editor.putString("ch6",choice_6_ch.getText().toString().trim());
        }else {
            editor.putString("ch6",null);
        }
        if (choice_7_ch.isChecked()){
            editor.putString("ch7",choice_7_ch.getText().toString().trim());
        }else {
            editor.putString("ch7",null);
        }
        if (choice_8_ch.isChecked()){
            editor.putString("ch8",choice_8_ch.getText().toString().trim());
        }else {
            editor.putString("ch8",null);
        }
        if (choice_9_ch.isChecked()){
            editor.putString("ch9",choice_9_ch.getText().toString().trim());
        }else {
            editor.putString("ch9",null);
        }
        if (choice_10_ch.isChecked()){
            editor.putString("ch10",choice_10_ch.getText().toString().trim());
        }else {
            editor.putString("ch10",null);
        }
        if (choice_11_ch.isChecked()){
            editor.putString("ch11",choice_11_ch.getText().toString().trim());
        }else {
            editor.putString("ch11",null);
        }
        if (choice_12_ch.isChecked()){
            editor.putString("ch12",choice_12_ch.getText().toString().trim());
        }else {
            editor.putString("ch12",null);
        }

        editor.commit();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!choice_1_ch.isChecked() && !choice_2_ch.isChecked() && !choice_3_ch.isChecked() && !choice_4_ch.isChecked() && !choice_5_ch.isChecked() && !choice_6_ch.isChecked() && !choice_7_ch.isChecked() && !choice_8_ch.isChecked() && !choice_9_ch.isChecked() && !choice_10_ch.isChecked() && !choice_11_ch.isChecked() && !choice_12_ch.isChecked()){
            buttonView.setChecked(true);
            showDialog();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            String msg = ""; // 내용을 생성해서 저장
            if(choice_1_ch.isChecked()){
                msg += choice_1_ch.getText().toString() + " ";
            }
            if(choice_2_ch.isChecked()){
                msg += choice_2_ch.getText().toString() + " ";
            }
            if(choice_3_ch.isChecked()){
                msg += choice_3_ch.getText().toString() + " ";
            }
            if(choice_3_ch.isChecked()){
                msg += choice_3_ch.getText().toString() + " ";
            }
            if(choice_4_ch.isChecked()){
                msg += choice_4_ch.getText().toString() + " ";
            }
            if(choice_5_ch.isChecked()){
                msg += choice_5_ch.getText().toString() + " ";
            }
            if(choice_6_ch.isChecked()){
                msg += choice_6_ch.getText().toString() + " ";
            }
            if(choice_7_ch.isChecked()){
                msg += choice_7_ch.getText().toString() + " ";
            }
            if(choice_8_ch.isChecked()){
                msg += choice_8_ch.getText().toString() + " ";
            }
            if(choice_9_ch.isChecked()){
                msg += choice_9_ch.getText().toString() + " ";
            }
            if(choice_10_ch.isChecked()){
                msg += choice_10_ch.getText().toString() + " ";
            }
            if(choice_11_ch.isChecked()){
                msg += choice_12_ch.getText().toString() + " ";
            }
            Intent intent = new Intent();
            intent.putExtra("msg",msg);
            setResult(RESULT_OK,intent);

            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고!");
        builder.setMessage("반드시 한 항목은 선택하세요!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
