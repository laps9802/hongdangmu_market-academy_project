package com.example.market;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.market.adapter.StartAdapter;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ShowAllActivity extends AppCompatActivity {
    RecyclerView showAll_sell_list;
    AsyncHttpClient client;
    response response;
    StartAdapter adapter;
    String name;
    @Override
    protected void onResume() {
        super.onResume();

        RequestParams params = new RequestParams();
        params.put("user_name",name);
        client.post(Constants.ShowAllActivityURL,params,response);
        adapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        name = getIntent().getStringExtra("name");
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(name + "님의 판매목록");
        showAll_sell_list = findViewById(R.id.showAll_sell_list);

        adapter = new StartAdapter(this);
        client = new AsyncHttpClient();
        response = new response(this,adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        showAll_sell_list.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class response extends AsyncHttpResponseHandler {
        Activity activity;
        StartAdapter adapter;
        ProgressDialog dialog;


        public response(Activity activity, StartAdapter adapter) {
            this.activity = activity;
            this.adapter = adapter;
        }

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("불러오는중");
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
            String strJson = new String(responseBody);
            try {

                JSONObject json = new JSONObject(strJson);

                String rt = json.getString("rt");

                String total = json.getString("total");

                JSONArray item = json.getJSONArray("item");

                if (rt.equals("OK")){
                    for (int i=0;i<item.length();i++){
                        JSONObject jsonObject = item.getJSONObject(i);
                        Main board = new Main();
                        board.setUser_name(jsonObject.getString("user_name"));
                        board.setArea(jsonObject.getString("area"));
                        board.setBoard_date(jsonObject.getString("board_date"));
                        board.setCategory_code(jsonObject.getString("category_code"));
                        board.setContent(jsonObject.getString("content"));
                        board.setImage0(jsonObject.getString("image0"));
                        board.setInterest_count(jsonObject.getInt("interest_count"));
                        board.setNum(jsonObject.getInt("num"));
                        board.setGoods_price(jsonObject.getInt("price"));
                        board.setRead_count(jsonObject.getInt("readcount"));
                        board.setReply_count(jsonObject.getInt("reply_count"));
                        board.setBoard_subject(jsonObject.getString("subject"));
                        Log.d("[Test]",board.getUser_name());
                        adapter.add(board);
                    }
                }
                showAll_sell_list.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
    }
}
