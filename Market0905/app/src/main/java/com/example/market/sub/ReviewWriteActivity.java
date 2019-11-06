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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import cz.msebera.android.httpclient.Header;

public class ReviewWriteActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView_image0, imageView_bad_on, imageView_bad_off,
              imageView_good_on, imageView_good_off, imageView_excellent_on,
              imageView_excellent_off;
    TextView textView_back, textView_board_subject, textView_user_name;
    LinearLayout linearLayout_review;
    EditText editText_review_content;
    Button button_review_complete;
    ImageLoader imageLoader;
    Main review;
    String user_name;   // 로그인한 사람의 이름
    String review_content;
    int manner;
    AsyncHttpClient client;
    Response response;
    int reviewer;
    String to;

    int return_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>거래 후기 남기기</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        client = new AsyncHttpClient();
        response = new Response(this);

        Log.d("[REVIEW]", "리뷰 작성");
        setContentView(R.layout.activity_review_write);
        review = (Main) getIntent().getSerializableExtra("review");
        user_name = getIntent().getStringExtra("user_name");
        return_code = getIntent().getIntExtra("return_code", 0);
        reviewer = getIntent().getIntExtra("reviewer", 3);  // seller : 0 or buyer : 1 로 값이 넘어옴
        to = getIntent().getStringExtra("to");

        // 판매자 이름 review.getUser_name()

        imageView_image0 = findViewById(R.id.imageView_image0);
        imageView_bad_on = findViewById(R.id.imageView_bad_on);
        imageView_bad_off = findViewById(R.id.imageView_bad_off);
        imageView_good_on = findViewById(R.id.imageView_good_on);
        imageView_good_off = findViewById(R.id.imageView_good_off);
        imageView_excellent_on = findViewById(R.id.imageView_excellent_on);
        imageView_excellent_off = findViewById(R.id.imageView_excellent_off);
        textView_board_subject = findViewById(R.id.textView_board_subject);
        textView_user_name = findViewById(R.id.textView_user_name);
        linearLayout_review = findViewById(R.id.linearLayout_review);
        editText_review_content = findViewById(R.id.editText_review_content);
        button_review_complete = findViewById(R.id.button_review_complete);

        imageLoader = ImageLoader.getInstance();

        textView_board_subject.setText(review.getBoard_subject());
        imageLoader.displayImage(review.getImage0(), imageView_image0);

        button_review_complete.setOnClickListener(this);
        imageView_bad_off.setOnClickListener(this);
        imageView_good_off.setOnClickListener(this);
        imageView_excellent_off.setOnClickListener(this);
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
            case R.id.imageView_bad_off:
                imageView_bad_off.setVisibility(View.GONE);
                imageView_bad_on.setVisibility(View.VISIBLE);
                imageView_good_off.setVisibility(View.VISIBLE);
                imageView_good_on.setVisibility(View.GONE);
                imageView_excellent_off.setVisibility(View.VISIBLE);
                imageView_excellent_on.setVisibility(View.GONE);
                linearLayout_review.setVisibility(View.VISIBLE);
                // 매너값
                manner = 0;
                break;
            case R.id.imageView_good_off:
                imageView_bad_off.setVisibility(View.VISIBLE);
                imageView_bad_on.setVisibility(View.GONE);
                imageView_good_off.setVisibility(View.GONE);
                imageView_good_on.setVisibility(View.VISIBLE);
                imageView_excellent_off.setVisibility(View.VISIBLE);
                imageView_excellent_on.setVisibility(View.GONE);
                linearLayout_review.setVisibility(View.VISIBLE);
                // 매너값
                manner = 5;
                break;
            case R.id.imageView_excellent_off:
                imageView_bad_off.setVisibility(View.VISIBLE);
                imageView_bad_on.setVisibility(View.GONE);
                imageView_good_off.setVisibility(View.VISIBLE);
                imageView_good_on.setVisibility(View.GONE);
                imageView_excellent_off.setVisibility(View.GONE);
                imageView_excellent_on.setVisibility(View.VISIBLE);
                linearLayout_review.setVisibility(View.VISIBLE);
                // 매너값
                manner = 10;
                break;
            case R.id.button_review_complete:
                review_content = editText_review_content.getText().toString().trim();
                RequestParams params = new RequestParams();
                Log.e("reviewer", "reviewer = " + reviewer);
                // 매너 등록

                RequestParams mannerParams = new RequestParams();
                if (reviewer == 0) {
                    mannerParams.put("manner", manner);
                    mannerParams.put("user_name", to);
                    client.post(Constants.ReviewWriteActivityURL_MANNER, mannerParams, response);

                    params.put("buyer", to);
                    params.put("seller", user_name);
                    params.put("content", review_content);
                    params.put("reviewer", "sell");
                } else if (reviewer == 1) {
                    mannerParams.put("manner", manner);
                    mannerParams.put("user_name", review.getUser_name());
                    Log.d("매너값 들어갔나?", "상대 이름 = " + review.getUser_name());
                    client.post(Constants.ReviewWriteActivityURL_MANNER, mannerParams, response);

                    params.put("seller", review.getUser_name());
                    params.put("buyer", user_name);
                    params.put("content", review_content);
                    params.put("reviewer", "buy");

                    String URL_UPDATE = "http://192.168.0.69:8098/hongdangmu/purchase/update.do";
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("goods_num", review.getNum());
                    client.post(URL_UPDATE, requestParams, response);
                }
                // 받는 사람의 이름
                Log.e("[받는 사람의 이름은?]", "review = " + user_name);
                client.post(Constants.ReviewWriteActivityURL, params, response);
                RequestParams params2 = new RequestParams();
                params2.put("num", review.getNum());
                client.post(Constants.ReviewWriteActivityURL2, params2, response);
                Intent intent = new Intent(this, ReviewResultActivity.class);
                intent.putExtra("review", review);
                intent.putExtra("user_name", user_name);
                intent.putExtra("review_content", review_content);
                intent.putExtra("return_code", return_code);
                intent.putExtra("reviewer", reviewer);
                intent.putExtra("to", to);
//                intent.putExtra("manner", manner);

                startActivity(intent);
                break;
        }
    }

    public class Response extends AsyncHttpResponseHandler {
        Activity activity;

        public Response(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 = "+ statusCode, Toast.LENGTH_SHORT).show();
        }
    }
}
