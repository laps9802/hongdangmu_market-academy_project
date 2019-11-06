package com.example.market.sub;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.MainActivity;
import com.example.market.R;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class ReviewResultActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView_back, textView_To, textView_content, textView_who_trade,
             textView_from;
    Button button_close;
    Main review;
    String user_name;
    String review_content;
    int reviewer;
    String to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_result);
        // 액션바 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>내가 남긴 거래 후기</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        review = (Main) getIntent().getSerializableExtra("review");
        user_name = getIntent().getStringExtra("user_name");
        review_content = getIntent().getStringExtra("review_content");
        reviewer = getIntent().getIntExtra("reviewer", 3);
//        manner = getIntent().getIntExtra("manner", 0);

        textView_To = findViewById(R.id.textView_To);
        textView_content = findViewById(R.id.textView_content);
        textView_who_trade = findViewById(R.id.textView_who_trade);
        textView_from = findViewById(R.id.textView_from);
        button_close = findViewById(R.id.button_close);

        if (reviewer == 0) {
            to = getIntent().getStringExtra("to");
            textView_To.setText("To. " + to);
            textView_who_trade.setText(to + "님과 "
                    + review.getBoard_subject() + "을(를) 거래헀어요");
        } else if (reviewer == 1) {
            textView_To.setText("To. " + review.getUser_name());
            textView_who_trade.setText(review.getUser_name() + "님과 "
                    + review.getBoard_subject() + "을(를) 거래헀어요");
        }
        textView_content.setText(review_content);

        textView_from.setText("From. " + user_name);

        button_close.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_close:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

}
