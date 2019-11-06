package com.example.market.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.market.MsgRoomActivity;
import com.example.market.R;
import com.example.market.model.Main;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    Context context;
    ArrayList<Main> main = new ArrayList<Main>();
    ImageLoader imageLoader;
    DisplayImageOptions options;
    AdapterView.OnItemClickListener listener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_msg,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Main mainV = main.get(i);
        viewHolder.setItem(mainV);
        viewHolder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return main.size();
    }
    public MsgAdapter(Context context){
        this.context = context;
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
    public void add(Main item){
        main.add(item);
    }
    public Main getItem(int position){
        return main.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView msg_image_imageView;
        TextView msg_nameAreaDate_tv,msg_view_tv;
        LinearLayout msg_layout_layout;
        AdapterView.OnItemClickListener listener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg_image_imageView = itemView.findViewById(R.id.msg_image_imageView);
            msg_nameAreaDate_tv = itemView.findViewById(R.id.msg_nameAreaDate_tv);
            msg_layout_layout = itemView.findViewById(R.id.msg_layout_layout);
            msg_view_tv = itemView.findViewById(R.id.msg_view_tv);
            msg_layout_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Main item = getItem(position);
                    Intent intent = new Intent(v.getContext(), MsgRoomActivity.class);
                    intent.putExtra("list",main);   // 전체
                    intent.putExtra("item",item);   // 해당
                    v.getContext().startActivity(intent);
                }
            });
        }
        public void setItem(Main mainV){
            imageLoader.displayImage(mainV.getUser_photo(),msg_image_imageView,options);
            msg_nameAreaDate_tv.setText(mainV.getUser_name() + "/" + mainV.getArea() + "/" + mainV.getMsgDate());
            msg_view_tv.setText(mainV.getMsgContent());
        }
        public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
            this.listener = listener;
        }
    }
}
