package com.example.market.frag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.adapter.UsersAdapter;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PeopleActivity extends Fragment {
    RecyclerView people_list_recyclerView;
    private static final String KEYWORD = "keyword";
    String keyword;
    UsersAdapter adapter;
    AsyncHttpClient client;
    PeopleResponse response;


    @Override
    public void onResume() {
        super.onResume();
        RequestParams params = new RequestParams();
        params.put("user_name",keyword);
        client.post(Constants.PeopleActivityURL,params,response);
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        keyword=null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        people_list_recyclerView = view.findViewById(R.id.people_list_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        people_list_recyclerView.setLayoutManager(layoutManager);
        adapter = new UsersAdapter(getContext());
        client = new AsyncHttpClient();
        response = new PeopleResponse((Activity) getContext(),adapter);
    }
    public static Fragment newInstance(String keyword) {
        PeopleActivity fragment = new PeopleActivity();
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
        return inflater.inflate(R.layout.activity_people, container, false);
    }
    public class PeopleResponse extends AsyncHttpResponseHandler {
        Activity activity;
        UsersAdapter adapter;
        ProgressDialog dialog;


        public PeopleResponse(Activity activity, UsersAdapter adapter) {
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
                        board.setSales_count(jsonObject.getInt("sales_count"));
                        board.setPurchase_count(jsonObject.getInt("purchase_count"));
                        board.setManner(jsonObject.getDouble("manner"));
                        board.setUser_code(jsonObject.getInt("user_code"));
                        board.setArea(jsonObject.getString("user_area"));
                        board.setInterest_count(jsonObject.getInt("interest_count"));
                        board.setReply_percent(jsonObject.getInt("reply_percent"));
                        board.setUser_photo(jsonObject.getString("user_photo"));
                        adapter.add(board);
                    }
                    people_list_recyclerView.setAdapter(adapter);
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
