package com.example.market.sub;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.adapter.SellCompleteAdapter;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.example.market.response.SellCompleteResponse;
import com.example.market.sub.SellList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SellCompleteActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_back;
    TextView textView_subject;
    Main item;
    AsyncHttpClient client;
    int num;
    SellCompleteResponse response;
    int return_code;
    String subject;

    // 9월 2일
    SellCompleteAdapter adapter;
    Response response2;
    RecyclerView recyclerView_buy_tbl;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Main> buyer;
    int reviewer;

    ImageView imageView_image0;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_complete);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>구매자 선택</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        imageView_image0 = findViewById(R.id.imageView_image0);
        imageLoader = ImageLoader.getInstance();

        buyer = new ArrayList<>();
        item = (Main) getIntent().getSerializableExtra("item");
        reviewer = getIntent().getIntExtra("reviewer", 3);
        subject = item.getBoard_subject();
        num = item.getNum();
        return_code = getIntent().getIntExtra("return_code", 0);
        client = new AsyncHttpClient();
        response = new SellCompleteResponse(this);
        getData();

        // 9월 2일
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        int user_code = pref.getInt("user_code",0);
        adapter = new SellCompleteAdapter(this, buyer, item, reviewer);
        RequestParams requestParams = new RequestParams();
        requestParams.put("goods_num", num);
        response2 = new Response(this, adapter);
        client.post(Constants.SellCompleteActivityURL2, requestParams, response2);
        recyclerView_buy_tbl = findViewById(R.id.recyclerView_buy_tbl);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_buy_tbl.setLayoutManager(linearLayoutManager);
        recyclerView_buy_tbl.setHasFixedSize(true);

        imageLoader.displayImage(item.getImage0(), imageView_image0);
        textView_subject = findViewById(R.id.textView_subject);
        button_back = findViewById(R.id.button_back);


        textView_subject.setText(item.getBoard_subject());
        button_back.setOnClickListener(this);
    }

    private void getData() {
        RequestParams params = new RequestParams();
        Log.d("[REVIEW]", "NUM = " + num);
        params.put("num", num);
        client.post(Constants.SellCompleteActivityURL, params, response);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (return_code == 1) {
            intent = new Intent(this, SellList.class);
        } else if (return_code == 2) {
            intent = new Intent(this, BuyList.class);
        }
        switch (v.getId()) {
            case R.id.button_back:
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    public class Response extends AsyncHttpResponseHandler {
        Activity activity;
        SellCompleteAdapter adapter;

        public Response(Activity activity, SellCompleteAdapter adapter) {
            this.activity = activity;
            this.adapter = adapter;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);

            try {
                JSONObject json = new JSONObject(content);
                JSONArray item = json.getJSONArray("item");
                for (int i=0; i<item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);
                    String user_name = temp.getString("user_name");
                    Log.d("123user_name", "user_name = " + user_name);
                    String image0 = temp.getString("user_photo");
                    Log.d("123image0", "image0 = " + image0);

                    int user_code = temp.getInt("user_code");
                    Main main = new Main(user_name, image0, user_code);
                    adapter.add(main);
                }
                recyclerView_buy_tbl.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패", Toast.LENGTH_SHORT).show();
        }
    }
}
