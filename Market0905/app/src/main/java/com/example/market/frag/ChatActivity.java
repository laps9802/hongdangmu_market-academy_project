package com.example.market.frag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.adapter.MsgAdapter;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.market.constants.Constants.chatActivityURL;


public class ChatActivity extends Fragment {
    RecyclerView chat_chat_list;
    MsgAdapter msgAdapter;
    AsyncHttpClient client;
    HttpResponse httpResponse;
    String recipient;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chat_chat_list = view.findViewById(R.id.chat_chat_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        chat_chat_list.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(getContext());
        client = new AsyncHttpClient();
        httpResponse = new HttpResponse((Activity) getContext(),msgAdapter);
        SharedPreferences pref = getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        recipient = pref.getString("user_name",null);
        RequestParams params = new RequestParams();
        params.put("recipient",recipient);
        client.post(chatActivityURL,params,httpResponse);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_chat, container, false);
        return rootView;
    }
    private class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        MsgAdapter msgAdapter;
        ProgressDialog dialog;


        public HttpResponse(Activity activity, MsgAdapter adapter) {
            this.activity = activity;
            this.msgAdapter = adapter;
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
                        board.setNum(jsonObject.getInt("bno"));
                        board.setMsgTitle(jsonObject.getString("msgtitle"));
                        board.setUser_name(jsonObject.getString("sender"));
                        board.setMsgContent(jsonObject.getString("msgContent"));
                        board.setMsgDate(jsonObject.getString("msgDate"));
                        board.setUser_photo(jsonObject.getString("user_photo"));
                        board.setArea(jsonObject.getString("user_area"));
                        msgAdapter.add(board);
                    }
                }
                chat_chat_list.setAdapter(msgAdapter);
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
