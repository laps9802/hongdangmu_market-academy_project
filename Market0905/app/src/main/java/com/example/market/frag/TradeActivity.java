package com.example.market.frag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class TradeActivity extends Fragment {
    RecyclerView trade_list_recyclerView;
    StartAdapter adapter;
    AsyncHttpClient client;
    TradeResponse response;
    private static final String KEYWORD = "keyword";
    String keyword;
    @Override
    public void onResume() {
        super.onResume();
        RequestParams params = new RequestParams();
        params.put("keyword",keyword);
        client.post(Constants.TradeActivityURL ,params,response);
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trade_list_recyclerView = view.findViewById(R.id.trade_list_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        trade_list_recyclerView.setLayoutManager(layoutManager);
        adapter = new StartAdapter(getContext());
        client = new AsyncHttpClient();
        response = new TradeResponse((Activity) getContext(),adapter);

    }
    public static Fragment newInstance(String keyword) {
        TradeActivity fragment = new TradeActivity();
        Bundle args = new Bundle();
        args.putString(KEYWORD, keyword);
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null){
            keyword = getArguments().getString(KEYWORD);
        }

        return inflater.inflate(R.layout.activity_trade, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        keyword = null;
    }

    public class TradeResponse extends AsyncHttpResponseHandler {
        Activity activity;
        StartAdapter adapter;
        ProgressDialog dialog;


        public TradeResponse(Activity activity, StartAdapter adapter) {
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
                        board.getRead_count(jsonObject.getInt("readcount"));
                        board.setReply_count(jsonObject.getInt("reply_count"));
                        board.setBoard_subject(jsonObject.getString("subject"));
                        Log.d("[Test]",board.getUser_name());
                        adapter.add(board);
                    }
                    trade_list_recyclerView.setAdapter(adapter);
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
