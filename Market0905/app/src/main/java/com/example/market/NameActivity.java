package com.example.market;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.market.constants.Constants;

public class NameActivity extends AppCompatActivity {
    Button name_btn_input;
    EditText name_edit_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>닉네임 설정</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        name_edit_nickname = findViewById(R.id.name_edit_nickname);
        name_btn_input = findViewById(R.id.name_btn_input);
        name_btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = name_edit_nickname.getText().toString().trim();
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                pref.edit()
                        .putString("nickname", nickname)
                        .putString("user_name", nickname)
                        .commit();
                Toast.makeText(NameActivity.this, pref.getString("nickname", ""), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NameActivity.this, LocationInitActivity.class));
                finish();
            }
        });
    }
}
