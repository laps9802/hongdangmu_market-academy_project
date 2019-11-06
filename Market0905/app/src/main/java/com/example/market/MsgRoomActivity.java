package com.example.market;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.market.adapter.MsgRoomAdapter;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.market.constants.Constants.msgActivityURL;
import static com.example.market.constants.Constants.msgRoomActivityURL;

public class MsgRoomActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView msgRoom_chat_list;
    MsgRoomAdapter adapter;
    AsyncHttpClient client;
    HttpResponse response;
    HttpResponse2 response2;
    String recipient;
    String sender;
    Thread th;
    boolean threadCh = true;
    ImageView msgRoom_send_btn;
    EditText msgRoom_chat_edit;
    String msgContent;
    String user_photo;
    String area;
    LinearLayoutManager layoutManager;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        threadCh = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_room);
        Main item = (Main) getIntent().getSerializableExtra("item");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>" + item.getUser_name() + "님과의 채팅방" + "</font>"));

        msgRoom_chat_list = findViewById(R.id.msgRoom_chat_list);
        msgRoom_send_btn = findViewById(R.id.msgRoom_send_btn);
        msgRoom_chat_edit = findViewById(R.id.msgRoom_chat_edit);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        msgRoom_chat_list.setLayoutManager(layoutManager);
        adapter = new MsgRoomAdapter(this);
        client = new AsyncHttpClient();
        response = new HttpResponse(this,adapter);
        response2 = new HttpResponse2(this);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        recipient = pref.getString("user_name",null);
        user_photo = pref.getString("user_photo",null);
        area = pref.getString("area",null);
        sender = item.getUser_name();
        msgRoom_send_btn.setOnClickListener(this);

        th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadCh) {  // threadCh가 true일동안만 실행
                    RequestParams params = new RequestParams();
                    params.put("recipient",recipient);
                    params.put("sender",sender);
                    client.post(msgRoomActivityURL,params,response);
                    adapter.clear();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            threadCh = false;
            Toast.makeText(this,"" + threadCh,Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        msgContent = msgRoom_chat_edit.getText().toString().trim();
        if (v.getId()==R.id.msgRoom_send_btn){
            RequestParams params = new RequestParams();
            params.put("user_photo",user_photo);
            params.put("user_area",area);
            params.put("bno",1);
            params.put("sender",recipient);
            params.put("recipient",sender);
            params.put("msgTitle","채팅^^");
            params.put("msgContent",msgContent);
            client.post(msgActivityURL,params,response2);
            msgRoom_chat_edit.setText("");

        }
    }

    private class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        MsgRoomAdapter adapter;


        public HttpResponse(Activity activity, MsgRoomAdapter adapter) {
            this.activity = activity;
            this.adapter = adapter;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {

                JSONObject json = new JSONObject(strJson);

                String rt = json.getString("rt");

                String total = json.getString("total");

                JSONArray item = json.getJSONArray("item");

                if (rt.equals("OK")) {
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject jsonObject = item.getJSONObject(i);
                        Main board = new Main();
                        board.setSender(jsonObject.getString("sender"));
                        board.setRecipient(jsonObject.getString("recipient"));
                        board.setMsgContent(jsonObject.getString("msgContent"));
                        adapter.add(board);
                    }
                }
                msgRoom_chat_list.setAdapter(adapter);
//                msgRoom_chat_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                        threadCh = false;
//                        if (msgRoom_chat_list.getScrollState()==adapter.getItemCount()){
//                            threadCh = true;
//                        }
//                    }
//                });
                msgRoom_chat_list.scrollToPosition(adapter.getItemCount()-1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
    }
    private class HttpResponse2 extends AsyncHttpResponseHandler {
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
                if (rt.equals("OK")){
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
}
