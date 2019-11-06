package com.example.market.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.example.market.sub.ReviewWriteActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SellCompleteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    ArrayList<Main> buyers;
    Main item;
    int reviewer;

    int position;

    AsyncHttpClient client;
    Response response;

    public SellCompleteAdapter(Activity activity, ArrayList<Main> buyers, Main item, int reviewer) {
        this.activity = activity;
        this.buyers = buyers;
        this.item = item;
        this.reviewer = reviewer;
    }

    public void add(Main main) {
        buyers.add(main);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            LinearLayout linearLayout_sell_complete;
        ImageView imageView_user_photo;
        TextView textView_user_name;
        ImageLoader imageLoader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client = new AsyncHttpClient();
            response = new Response();
            imageLoader = ImageLoader.getInstance();

            linearLayout_sell_complete = itemView.findViewById(R.id.linearLayout_sell_complete);
            imageView_user_photo = itemView.findViewById(R.id.imageView_user_photo);
            textView_user_name = itemView.findViewById(R.id.textView_user_name);

            linearLayout_sell_complete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.linearLayout_sell_complete:
                    // 선택된 구매자포함 다 삭제한 후 선택된 구매자만 다시 테이블에 추가
                    position = getAdapterPosition();
                    Main review = buyers.get(position);
                    Log.d("[REVIEW]", "position = " + position);
                    Intent intent = new Intent(activity, ReviewWriteActivity.class);
                    intent.putExtra("review", review);
                    intent.putExtra("user_name", item.getUser_name());
                    intent.putExtra("other_name", review.getUser_name());
                    intent.putExtra("reviewer", reviewer);
                    intent.putExtra("return_code", 1);
                    intent.putExtra("review", item);
                    intent.putExtra("to", review.getUser_name());

                    // 9월 2일

                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_code", review.getUser_code());
                    requestParams.put("goods_num", item.getNum());
                    client.post(Constants.SellCompleteAdapterURL, requestParams, response);
                    activity.startActivity(intent);
                    break;
            }
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_sell_complete, viewGroup, false);
        ViewHolder vh = new ViewHolder(item);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Main item = buyers.get(i);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.imageLoader.displayImage(item.getUser_photo(), holder.imageView_user_photo);
        holder.textView_user_name.setText(item.getUser_name());
        Log.d("[trade]","item.getUser_photo() = " + item.getUser_photo());
        Log.d("[trade]","item.getUser_name() = " + item.getUser_name());
    }

    @Override
    public int getItemCount() {
        return buyers.size();
    }

    public class Response extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패", Toast.LENGTH_SHORT).show();
        }
    }
}
