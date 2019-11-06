package com.example.market;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ReplyActivity extends AppCompatActivity implements View.OnClickListener {
    EditText reply_reply_edit;
    ImageView reply_input_btn;

    AsyncHttpClient client;
    HttpResponse response;
    HttpResponse2 response2;
    Main item;
    Main users;
    int user_code;
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        user_code = pref.getInt("user_code",0);
        RequestParams params = new RequestParams();
        params.put("user_code",user_code);
        client.post(Constants.selectOne2URL,params,response2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 255, 255)));
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>댓글</font>"));
        actionBar.setDisplayHomeAsUpEnabled(true);

        reply_reply_edit = findViewById(R.id.reply_reply_edit);
        reply_input_btn = findViewById(R.id.reply_input_btn);

        client = new AsyncHttpClient();
        response = new HttpResponse(this);
        response2 = new HttpResponse2(this);

        item = (Main) getIntent().getSerializableExtra("item");
        reply_input_btn.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reply_input_btn){
            String reply = reply_reply_edit.getText().toString().trim();
            RequestParams params = new RequestParams();
            params.setForceMultipartEntityContentType(true);
            params.put("user_photo",users.getUser_photo());
            params.put("user_area",users.getArea());
            params.put("bno",item.getNum());
            params.put("reply",reply);
            params.put("user_name",users.getUser_name());
            client.post(Constants.ReplyActivityURL,params,response);
            finish();
        }
    }
    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
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
        public HttpResponse(Activity activity) {
            this.activity = activity;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                if (rt.equals("OK")){
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    Toast.makeText(activity,"저장성공",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity,"저장실패",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
    }
    class HttpResponse2 extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
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
        public HttpResponse2(Activity activity) {
            this.activity = activity;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                JSONArray items = json.getJSONArray("item");
                if (rt.equals("OK")){
                    JSONObject temp = items.getJSONObject(0);
                    users = new Main();
                    users.setUser_photo(temp.getString("user_photo"));
                    users.setUser_name(temp.getString("user_name"));
                    users.setArea(temp.getString("user_area"));
                } else {
                    Toast.makeText(activity,"저장실패",Toast.LENGTH_SHORT).show();
                }

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
