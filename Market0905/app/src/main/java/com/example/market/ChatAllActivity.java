package com.example.market;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.adapter.ChatAdapter;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChatAllActivity extends AppCompatActivity {
    RecyclerView list_chatAll_list;

    ChatAdapter chatAdapter;
    AsyncHttpClient client;
    response response;

    @Override
    protected void onResume() {
        super.onResume();
        RequestParams params = new RequestParams();
        int bno = getIntent().getIntExtra("bno",0);
        params.put("bno",bno);
        client.post(Constants.ChatAllActivityURL,params,response);
        chatAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_all);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 255, 255)));
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>댓글</font>"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        list_chatAll_list = findViewById(R.id.list_chatAll_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        list_chatAll_list.setLayoutManager(layoutManager);
        client = new AsyncHttpClient();
        response = new response(this);
        chatAdapter = new ChatAdapter(this,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class response extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
        ImageLoader imageLoader;

        public response(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            imageLoaderInit();
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                JSONArray item = json.getJSONArray("item");
                if (rt.equals("OK")){
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject jsonObject = item.getJSONObject(i);
                        Main board1 = new Main();
                        board1.setArea(jsonObject.getString("area"));
                        board1.setUser_photo(jsonObject.getString("user_photo"));
                        board1.setUser_name(jsonObject.getString("user_name"));
                        board1.setRe_chat(jsonObject.getString("reply"));
                        board1.setReg_date(jsonObject.getString("replyDate"));
                        chatAdapter.add(board1);
                    }
                }
                list_chatAll_list.setAdapter(chatAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
        private void imageLoaderInit() {
            imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
                imageLoader.init(configuration);
            }
        }
    }
}
