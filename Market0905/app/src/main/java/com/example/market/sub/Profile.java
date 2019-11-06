package com.example.market.sub;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.MainActivity;
import com.example.market.R;
import com.example.market.adapter.ReviewAdapter;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView_photo;
    TextView textView_nickName, textView_code, textView_manner, textView_sellList,
            textView_review;
    LinearLayout linearLayout_sell, linearLayout_review;
    ImageLoader imageLoader;
    ProgressBar progressBar;
    // 통신 부분
    AsyncHttpClient client;
    Response response;
    Response2 response2;
    Main users;
    int manner; // 매너 온도
    String user_name;
    // list를 구하는 URL이지만 판매개수를 구하기 위해 사용



    // 거래 후기 리스트 생성 부분
    ArrayList<Main> review;
    RecyclerView recyclerView_review;
    LinearLayoutManager linearLayoutManager;
    ReviewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>프로필</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        setContentView(R.layout.activity_profile);
        users = (Main) getIntent().getSerializableExtra("users");
        Log.d("user_name", "user_name = " + users.getUser_name());

        client = new AsyncHttpClient();
        response = new Response(this);
        RequestParams params = new RequestParams();
        params.put("user_name", users.getUser_name());
        client.post(Constants.ProfileURL, params, response);

        imageView_photo = findViewById(R.id.imageView_photo);
        textView_nickName = findViewById(R.id.textView_nickName);
        textView_code = findViewById(R.id.textView_code);
        textView_manner = findViewById(R.id.textView_manner);
        textView_sellList = findViewById(R.id.textView_SellList);
        textView_review = findViewById(R.id.textView_review);
        linearLayout_sell = findViewById(R.id.linearLayout_sell);
        linearLayout_review = findViewById(R.id.linearLayout_review);
        progressBar = findViewById(R.id.progressBar);
        imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(users.getUser_photo(), imageView_photo);
        textView_nickName.setText(users.getUser_name());
        textView_code.setText("#" + users.getUser_code());

        progressBar.setMax(100);
        progressBar.setProgress(0);


        manner = (int) users.getManner();
        progressBar.setProgress(manner);
        textView_manner.setText(manner + "°");

        linearLayout_sell.setOnClickListener(this);
        linearLayout_review.setOnClickListener(this);

        // 거래 후기 부분
        review = new ArrayList<>();
        adapter = new ReviewAdapter(review, this);
        linearLayoutManager = new LinearLayoutManager(this);
        response2 = new Response2(this, adapter);
        user_name = users.getUser_name();
        RequestParams params2 = new RequestParams();
        params2.put("user_name", user_name);
        client.post(Constants.ProfileURL2, params, response2);
        recyclerView_review = findViewById(R.id.recyclerView_review);
        recyclerView_review.setLayoutManager(linearLayoutManager);
        recyclerView_review.setHasFixedSize(true);
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
        Intent intent = null;
        switch (v.getId()) {
            case R.id.linearLayout_sell:
                intent = new Intent(this, SellList.class);
                intent.putExtra("users", users);
                startActivity(intent);
                break;
            case R.id.linearLayout_review:
                intent = new Intent(this, ReviewListActivity.class);
                intent.putExtra("user_name", user_name);
                startActivity(intent);
                break;
        }
    }

    public class Response extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public Response(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세여...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String contents = new String(responseBody);
            try {
                Log.d("[INFO]", "contents = " + contents);
                JSONObject json = new JSONObject(contents);
                JSONArray item = json.getJSONArray("item");
                Log.d("item", "item = " + item.length());
                textView_sellList.setText("판매상품 " + item.length() + "개");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 " + statusCode , Toast.LENGTH_SHORT).show();
        }
    }
    // 내가 받은 거래 후기 목록
    public class Response2 extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
        ReviewAdapter adapter;

        public Response2(Activity activity, ReviewAdapter adapter) {
            this.activity = activity;
            this.adapter = adapter;
        }

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세여...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            try {
                JSONObject json = new JSONObject(content);
                JSONArray item = json.getJSONArray("item");
                textView_review.setText("받은 거래 후기 (" + item.length() + ")");
                for (int i=0; i<item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);
                    String reviewer_image = temp.getString("reviewer_image");
                    Log.d("[정보1]", "reviewer_image = " + reviewer_image);
                    String reviewer_area = temp.getString("reviewer_area");
                    Log.d("[정보1]", "reviewer_area = " + reviewer_area);
                    String reviewer = temp.getString("reviewer");
                    Log.d("[정보1]", "reviewer = " + reviewer);
                    String reviewer_content = temp.getString("reviewer_content");
                    Log.d("[정보1]", "reviewer_content = " + reviewer_content);
                    String review_date = temp.getString("review_date");
                    Log.d("[정보1]", "review_date = " + review_date);
                    Main items = new Main(reviewer_image, reviewer_area, reviewer, reviewer_content, review_date);
                    adapter.add(items);
                }

                recyclerView_review.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 " + statusCode , Toast.LENGTH_SHORT).show();
        }
    }
}
