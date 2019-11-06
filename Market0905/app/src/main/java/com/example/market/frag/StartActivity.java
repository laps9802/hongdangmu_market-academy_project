package com.example.market.frag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.market.LoginActivity;
import com.example.market.R;

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



public class StartActivity extends Fragment{
    RecyclerView start_list_recyclerView;
    StartAdapter adapter;
    AsyncHttpClient client;
    StartResponse response;
    private static final String URL2 = "url2";
    static String ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8,ch9,ch10,ch11,ch12;  // 카테고리 저장한것을 파일에서 꺼내서 저장용
    private  static final String CH = "ch";
    String url2;
    String ch;
    int i=0;
    boolean login = false;
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        ch1 = pref.getString("ch1","");
        ch2 = pref.getString("ch2","");
        ch3 = pref.getString("ch3","");
        ch4 = pref.getString("ch4","");
        ch5 = pref.getString("ch5","");
        ch6 = pref.getString("ch6","");
        ch7 = pref.getString("ch7","");
        ch8 = pref.getString("ch8","");
        ch9 = pref.getString("ch9","");
        ch10 = pref.getString("ch10","");
        ch11 = pref.getString("ch11","");
        ch12 = pref.getString("ch12","");
        login = pref.getBoolean("login",false);
        Log.d("[로그인]","" + login);
//        if (login==false){
//            Intent intent = new Intent(getContext(), LoginActivity.class);
//            startActivity(intent);
//        }


        RequestParams params = new RequestParams();
        params.put("category1",ch1);
        params.put("category2",ch2);
        params.put("category3",ch3);
        params.put("category4",ch4);
        params.put("category5",ch5);
        params.put("category6",ch6);
        params.put("category7",ch7);
        params.put("category8",ch8);
        params.put("category9",ch9);
        params.put("category10",ch10);
        params.put("category11",ch11);
        params.put("category12",ch12);
        if (ch==null){
            client.post(Constants.startActivityURL,params,response);
        } else {
            client.post(url2,params,response);
        }

        adapter.notifyDataSetChanged();
        adapter.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start_list_recyclerView = view.findViewById(R.id.start_list_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        start_list_recyclerView.setLayoutManager(layoutManager);
        adapter = new StartAdapter(getContext());
        client = new AsyncHttpClient();
        response = new StartResponse((Activity) getContext(),adapter);


    }
    public static Fragment newInstanceHot(String url2,String ch) {
        StartActivity fragment = new StartActivity();
        Bundle args = new Bundle();
        args.putString(URL2, url2);
        args.putString(CH,ch);
        fragment.setArguments(args);
        return fragment;
    }
    public static Fragment newInstanceNews() {
        StartActivity fragment = new StartActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null){
            url2 = getArguments().getString(URL2);
            ch = getArguments().getString(CH);
        }
        return inflater.inflate(R.layout.activity_start, container, false);

    }


    public class StartResponse extends AsyncHttpResponseHandler {
        Activity activity;
        StartAdapter adapter;
        ProgressDialog dialog;


        public StartResponse(Activity activity, StartAdapter adapter) {
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
                        board.setImage1(jsonObject.getString("image1"));
                        board.setImage2(jsonObject.getString("image2"));
                        board.setInterest_count(jsonObject.getInt("interest_count"));
                        board.setNum(jsonObject.getInt("num"));
                        board.setGoods_price(jsonObject.getInt("price"));
                        board.setRead_count(jsonObject.getInt("readcount"));
                        board.setReply_count(jsonObject.getInt("reply_count"));
                        board.setBoard_subject(jsonObject.getString("subject"));
                        Log.d("[Test1]",board.getUser_name());
                        adapter.add(board);
                    }
                }
                start_list_recyclerView.setAdapter(adapter);
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
