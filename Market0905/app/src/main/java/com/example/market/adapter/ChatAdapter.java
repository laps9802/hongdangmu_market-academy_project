package com.example.market.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;
    ArrayList<Main> main = new ArrayList<>();
    ImageLoader imageLoader;
    DisplayImageOptions options;
    AdapterView.OnItemClickListener listener;
    int j;
    AsyncHttpClient client;
    HttpResponse response;
    int position;
    Main item;
    String user_name;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_chat,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Main mainV = main.get(i);
        viewHolder.setItem(mainV);
        viewHolder.setOnItemClickListener(listener);
    }
    public ChatAdapter(Context context,int j){
        this.context = context;
        this.j = j;
        client = new AsyncHttpClient();
        response = new HttpResponse((Activity) context);
        SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        user_name = pref.getString("user_name",null);
        imageLoaderInit();
    }
    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
            imageLoader.init(configuration);
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);

        options = builder.build();
    }
    public void clear(){
        main.clear();
    }

    @Override
    public int getItemCount() {
        if (j==1&&main.size()>4){
            return 5;
        }else {
            return main.size();
        }
    }
    public void add(Main item){
        main.add(item);
    }
    public Main getItem(int position){
        return main.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView chat_image_imageView;
        TextView chat_name_tv, chat_local_tv, chat_chat_tv, chat_date_tv,chat_delete_tv;
        AdapterView.OnItemClickListener listener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chat_image_imageView = itemView.findViewById(R.id.chat_image_imageView);
            chat_name_tv = itemView.findViewById(R.id.chat_name_tv);
            chat_local_tv = itemView.findViewById(R.id.chat_local_tv);
            chat_chat_tv = itemView.findViewById(R.id.chat_chat_tv);
            chat_date_tv = itemView.findViewById(R.id.chat_date_tv);
            chat_delete_tv = itemView.findViewById(R.id.chat_delete_tv);
            chat_delete_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    item = getItem(position);
                    showLoginDialog();
                }
            });
        }
        public void setItem(Main mainV){
            imageLoader.displayImage(mainV.getUser_photo(),chat_image_imageView,options);
            Log.d("[확인]","" + mainV.getUser_photo());
            chat_name_tv.setText(mainV.getUser_name());
            Log.d("[확인]","" + mainV.getUser_name());
            chat_local_tv.setText(mainV.getArea());
            chat_chat_tv.setText(mainV.getRe_chat());
            chat_date_tv.setText(mainV.getReg_date());
            if (mainV.getUser_name().equals(user_name)){
                chat_delete_tv.setVisibility(View.VISIBLE);
            }
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
            this.listener = listener;
        }
    }
    class HttpResponse extends AsyncHttpResponseHandler {
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
        public HttpResponse(Activity activity) {
            this.activity = activity;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                if (rt.equals("OK")){
                    Toast.makeText(activity,"삭제성공",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity,"삭제실패",Toast.LENGTH_SHORT).show();
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
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("삭제");
        builder.setMessage("한번 지우면 복구는 불가능합니다.\n삭제 하시겠습니까?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = new RequestParams();
                params.put("bno",item.getNum());
                params.put("rno",item.getRno());
                client.post(Constants.ChatAdapterURL,params,response);
                Intent intent = new Intent(context, Clear2Activity.class);
                context.startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
