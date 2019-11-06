package com.example.market.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.model.Main;

import java.util.ArrayList;

public class MsgRoomAdapter extends RecyclerView.Adapter<MsgRoomAdapter.ViewHolder> {
    Context context;
    ArrayList<Main> main = new ArrayList<Main>();
    String sender;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        sender = pref.getString("user_name",null);
        Log.d("[채팅위치]","" + sender);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_msgroom,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Main mainV = main.get(i);
        viewHolder.setItem(mainV);
    }

    @Override
    public int getItemCount() {
        return main.size();
    }
    public MsgRoomAdapter(Context context){
        this.context = context;
    }
    public void clear(){
        main.clear();
    }
    public void add(Main item){
        main.add(item);
    }
    public Main getItem(int position){
        return main.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg_left_tv,msg_right_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg_left_tv = itemView.findViewById(R.id.msg_left_tv);
            msg_right_tv = itemView.findViewById(R.id.msg_right_tv);
        }
        public void setItem(Main mainV){
            if (!sender.equals(mainV.getRecipient())){
                msg_left_tv.setVisibility(View.GONE);
                msg_right_tv.setText(mainV.getMsgContent());
            }else {
                msg_right_tv.setVisibility(View.GONE);
                msg_left_tv.setText(mainV.getMsgContent());
            }
        }
    }
}
