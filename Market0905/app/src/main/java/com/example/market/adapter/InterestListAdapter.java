package com.example.market.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class InterestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    ArrayList<Main> items = new ArrayList<Main>();
    AsyncHttpClient client;
    Response response;

    Main item2;
    int user_code;
    int position;
    int goods_num;
    int interest_count;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView_image0, imageView_image1, imageView_image2,
                imageView_interest_on, imageView_interest_off, imageView_complet_trade;
        TextView textView_user_name, textView_board_subject, textView_board_content,
                textView_area, textView_price, textView_content, textView_reply_count,
                textView_interest_count;
        ImageLoader imageLoader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client = new AsyncHttpClient();
            response = new Response(activity);
            item2 = new Main();
            imageView_image0 = itemView.findViewById(R.id.imageView_goods_image);
            imageView_interest_on = itemView.findViewById(R.id.imageView_interest_on);
            imageView_interest_off = itemView.findViewById(R.id.imageView_interest_off);
            imageView_complet_trade = itemView.findViewById(R.id.imageView_complet_trade);
            textView_board_subject = itemView.findViewById(R.id.textView_board_subject);
            textView_board_content = itemView.findViewById(R.id.textView_board_content);
            textView_price = itemView.findViewById(R.id.textView_price);
            textView_area = itemView.findViewById(R.id.textView_area);
            textView_interest_count = itemView.findViewById(R.id.textView_interest_count);
            textView_reply_count = itemView.findViewById(R.id.textView_reply_count);

            imageLoader = ImageLoader.getInstance();
            Log.d("[INFO]", "ViewHolder interestList");
            imageView_interest_off.setVisibility(View.VISIBLE);
            imageView_interest_on.setOnClickListener(this);
            imageView_interest_off.setOnClickListener(this);

            imageView_complet_trade.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            position = getAdapterPosition();    // 어댑처 위치 값
            interest_count = items.get(position).getInterest_count();
            switch (v.getId()) {
                case R.id.imageView_interest_on:
                    goods_num = items.get(position).getNum();
                    Log.d("[INTEREST]", "board_num = " + goods_num);
                    Log.d("[INTEREST]", "1");
                    RequestParams params = new RequestParams();
                    params.put("user_code", user_code);
                    params.put("goods_num", goods_num);
                    client.post(Constants.InterestListAdapterURL_ON, params, response);
                    Toast.makeText(activity, "관심목록에 추가되었습니당", Toast.LENGTH_SHORT).show();
                    textView_interest_count.setText(" " + interest_count);
                    imageView_interest_on.setVisibility(View.GONE);
                    imageView_interest_off.setVisibility(View.VISIBLE);
                    break;
                case R.id.imageView_interest_off:
                    position = getAdapterPosition();    // 어댑처 위치 값
                    goods_num = items.get(position).getNum();
                    Log.d("[INTEREST]", "2");
                    RequestParams params2 = new RequestParams();
                    params2.put("user_code", user_code);
                    params2.put("goods_num", goods_num);
                    client.post(Constants.InterestListAdapterURL_OFF, params2, response);
                    Toast.makeText(activity, "관심목록에서 해제되었습니당", Toast.LENGTH_SHORT).show();
                    textView_interest_count.setText(" " + (interest_count - 1));
                    imageView_interest_off.setVisibility(View.GONE);
                    imageView_interest_on.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public InterestListAdapter(ArrayList<Main> items, Activity activity, int user_code) {
        this.items = items;
        this.activity = activity;
        this.user_code = user_code;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_interest, viewGroup, false);
        ViewHolder vh = new ViewHolder(item);
        position = vh.getAdapterPosition();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Main item = items.get(i);
        item2 = items.get(i);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.imageLoader.displayImage(item.getImage0(), holder.imageView_image0);
        holder.textView_board_subject.setText(item.getBoard_subject());
        holder.textView_board_content.setText(item.getContent());
        Log.d("[trade]","item.getSell_tf() = " + item.getSell_tf());
        if (item.getSell_tf().equals("true")) {
            holder.imageView_complet_trade.setVisibility(View.VISIBLE);
        }
        holder.textView_price.setText(item.getGoods_price() + "원");
        holder.textView_area.setText(item.getArea());
        holder.textView_interest_count.setText(" " + item.getInterest_count());
        holder.textView_reply_count.setText(" " + item.getReply_count());
//        Log.d("[INTEREST", "goods_num["+ i + "] = " + goods_num[i]);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(Main item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getItemViewType(int position) {
        this.position = position;
        return position;
    }

    public class Response extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public Response(Activity activity) {
            this.activity = activity;
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 = "+ statusCode, Toast.LENGTH_SHORT).show();
        }
    }
}
