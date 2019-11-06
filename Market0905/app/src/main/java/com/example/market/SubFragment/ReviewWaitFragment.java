package com.example.market.SubFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.adapter.SellBuyAdapter;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ReviewWaitFragment extends Fragment {
    View view;
    RecyclerView recyclerView_reviewWait;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Main> list;
    SellBuyAdapter adapter;
    AsyncHttpClient client;
    Response response;
    int button_code;    // 0, 1, 2, 3, 4에 따른 버튼 생성
    String user_name;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button_code = 3;
        list = new ArrayList<>();
        user_name = getArguments().getString("user_name");
        Log.e("[이름]", "user_name = " + user_name);
        adapter = new SellBuyAdapter(list, button_code, getActivity(), user_name);
        response = new Response(getActivity(), adapter);
        client = new AsyncHttpClient();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review_wait, container, false);
        recyclerView_reviewWait = view.findViewById(R.id.recyclerView_reviewWait);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView_reviewWait.setLayoutManager(linearLayoutManager);
        recyclerView_reviewWait.setHasFixedSize(true);

        return view;
    }

    private void getData() {
//        String user_name = getArguments().getString("user_name");
//        Log.e("[INFOsd]", "user_name = " + user_name);
//        RequestParams params = new RequestParams();
//        params.put("user_name", user_name);
//        client.post(Constants.ReviewWaitFragmentURL, params, response);
        SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        int user_code = pref.getInt("user_code",0);

        RequestParams params = new RequestParams();
        params.put("user_code", user_code);
        client.post(Constants.ReviewWaitFragment2URL, params, response);
    }

    public class Response extends AsyncHttpResponseHandler {
        Activity activity;
        SellBuyAdapter adapter;
        ProgressDialog dialog;

        public Response(Activity activity, SellBuyAdapter adapter) {
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
            Toast.makeText(activity, "통신 3", Toast.LENGTH_SHORT).show();
            String contents = new String(responseBody);
            try {
                JSONObject json = new JSONObject(contents);
                JSONArray item = json.getJSONArray("item");
                for (int i=0; i<item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);
                    String user_name = temp.getString("user_name");
                    Log.d("[INFO]", "user_name = " + user_name);
                    String area = temp.getString("area");
                    Log.d("[INFO]", "area = " + area);
                    String board_date = temp.getString("board_date");
                    Log.d("[INFO]", "board_date = " + board_date);
                    String category_code = temp.getString("category_code");
                    Log.d("[INFO]", "category_code = " + category_code);
                    String content = temp.getString("content");
                    Log.d("[INFO]", "content = " + content);
                    String image0 = temp.getString("image0");
                    Log.d("[INFO]", "image0 = " + image0);
                    String image1 = temp.getString("image1");
                    Log.d("[INFO]", "image1 = " + image1);
                    String image2 = temp.getString("image2");
                    Log.d("[INFO]", "image2 = " + image2);
                    String subject = temp.getString("subject");
                    Log.d("[INFO]", "subject = " + subject);
                    int num = temp.getInt("num");
                    Log.d("[INFO]", "num = " + num);
                    int price = temp.getInt("price");
                    Log.d("[INFO]", "price = " + price);
                    int interest_count = temp.getInt("interest_count");
                    Log.d("[INFO]", "interest_count = " + interest_count);
                    int reply_count = temp.getInt("reply_count");
                    Log.d("[INFO]", "reply_count = " + reply_count);
                    int readcount = temp.getInt("readcount");
                    Log.d("[INFO]", "readcount = " + readcount);
                    int lat = temp.getInt("lat");
                    Log.d("[INFO]", "lat = " + lat);
                    int lng = temp.getInt("lng");
                    Log.d("[INFO]", "lng = " + lng);
                    if (temp.getString("Preview_tf").equals("false")) {
                        Main items = new Main(num,  image0, image1, image2, user_name, subject,
                                category_code, area, price, content, reply_count, interest_count,
                                readcount, lat, lng, board_date);
                        adapter.add(items);
                    }
                }
                recyclerView_reviewWait.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.clear();
        adapter.notifyDataSetChanged();
    }
}
