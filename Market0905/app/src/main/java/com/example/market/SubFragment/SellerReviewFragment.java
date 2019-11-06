package com.example.market.SubFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.adapter.ReviewAdapter;
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

public class SellerReviewFragment extends Fragment {
    View view;
    RecyclerView recyclerView_seller_review;
    LinearLayoutManager linearLayoutManager;
    TextView textView_size;
    ArrayList<Main> review;
    ReviewAdapter adapter;
    AsyncHttpClient client;
    Response response;

    String user_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        review = new ArrayList<>();
        adapter = new ReviewAdapter(review, getActivity());
        response = new Response(getActivity(), adapter);
        client = new AsyncHttpClient();
        user_name = getArguments().getString("user_name");
        // 값 집어넣을 부분
        RequestParams params = new RequestParams();
        params.put("user_name", user_name);
        client.post(Constants.SellerReviewFragmentURL, params, response);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seller_review, container, false);
        textView_size = view.findViewById(R.id.textView_size);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_seller_review = view.findViewById(R.id.recyclerView_seller_review);
        recyclerView_seller_review.setLayoutManager(linearLayoutManager);
        recyclerView_seller_review.setHasFixedSize(true);

        return view;
    }

    // 나에게 작성된 판매자들의 후기
    public class Response extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
        ReviewAdapter adapter;

        public Response(Activity activity, ReviewAdapter adapter) {
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
            String content = new String(responseBody);
            try {
                JSONObject json = new JSONObject(content);
                JSONArray item = json.getJSONArray("item");
                textView_size.setText("후기 " + item.length() + "개");
                for (int i=0; i<item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);
                    String reviewer_image = temp.getString("reviewer_image");
                    Log.d("[정보1]", "reviewer_image = " + reviewer_image);
                    String reviewer_area = temp.getString("reviewer_area");
                    Log.d("[정보1]", "reviewer_area = " + reviewer_area);
                    String reviewer = temp.getString("reviewer");
                    Log.d("[정보1]", "reviewer = " + reviewer);
                    String reviewer_content = temp.getString("reviewer_content");
                    Log.d("[정보1]", "reviewer_content = " + reviewer_content);
                    String review_date = temp.getString("review_date");
                    Log.d("[정보1]", "review_date = " + review_date);
                    Main items = new Main(reviewer_image, reviewer_area, reviewer, reviewer_content, review_date);
                    adapter.add(items);
                }
                recyclerView_seller_review.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 " + statusCode , Toast.LENGTH_SHORT).show();
        }
    }
}
