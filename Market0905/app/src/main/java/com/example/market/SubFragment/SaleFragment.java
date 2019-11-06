package com.example.market.SubFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class SaleFragment extends Fragment implements RecyclerView.OnItemTouchListener {
    View view;
    RecyclerView recyclerView_sale;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Main> list;
    SellBuyAdapter adapter;
    AsyncHttpClient client;
    Response response;
//    private GestureDetector gestureDetector;
    int button_code;    // 0, 1, 2, 3, 4에 따른 버튼 생성
    String user_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("[INFO]", "판매리스트 선언");
        super.onCreate(savedInstanceState);
        button_code = 0;
        list = new ArrayList<>();
        user_name = getArguments().getString("user_name");
        adapter = new SellBuyAdapter(list, button_code, getActivity(), user_name);
        response = new Response(getActivity(), adapter);
        client = new AsyncHttpClient();
//        gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//           public boolean onSingleTapUp(MotionEvent e) {
//               return true;
//           }
//        });
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("[INFO]", "판매리스트 생성");
        view = inflater.inflate(R.layout.fragment_sale, container, false);
        recyclerView_sale = view.findViewById(R.id.recyclerView_sale);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView_sale.setLayoutManager(linearLayoutManager);
        recyclerView_sale.setHasFixedSize(true);
        recyclerView_sale.addOnItemTouchListener(this);
        return view;
    }

    private void getData() {
        String user_name = getArguments().getString("user_name");
        Log.e("[INFOsd]", "user_name = " + user_name);
        RequestParams params = new RequestParams();
        params.put("user_name", user_name);
        client.post(Constants.SaleFragmentURL, params, response);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//        View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
//        if (childView != null && gestureDetector.onTouchEvent(motionEvent)) {
//            int currentPosition = recyclerView.getChildAdapterPosition(childView);
//            Main item = list.get(currentPosition);
//            Toast.makeText(getActivity(), "가져온 item의 이름 = " + item.getBoard_subject(),
//                    Toast.LENGTH_SHORT).show();
//            Intent intent = null;
//            intent = new Intent(getActivity(), ResultActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.putExtra("user_name", item.getUser_name());
//            intent.putExtra("num", item.getNum());
//            startActivity(intent);
//            return true;
//        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

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
            String contents = new String(responseBody);
            try {
                Log.d("[INFO]", "contents = " + contents);
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
                    double lat = temp.getInt("lat");
                    Log.d("[INFO]", "lat = " + lat);
                    double lng = temp.getInt("lng");
                    Log.d("[INFO]", "lng = " + lng);
                    String sell_tf = temp.getString("sell_tf");
                    Log.d("[INFO]", "sell_tf = " + sell_tf);
                    String hide_tf = temp.getString("hide_tf");
                    Log.d("[INFO]", "hide_tf = " + hide_tf);
                    String review_tf = temp.getString("review_tf");
                    Log.d("[INFO]", "review_tf = " + review_tf);
                    Main items = new Main(num,  image0, image1, image2, user_name, subject,
                            category_code, area, price, content, reply_count, interest_count,
                            readcount, lat, lng, board_date, sell_tf, hide_tf, review_tf);
                    adapter.add(items);
                }
                recyclerView_sale.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 " + statusCode , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.clear();
        adapter.notifyDataSetChanged();
    }
}
