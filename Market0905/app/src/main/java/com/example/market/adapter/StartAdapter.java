package com.example.market.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.ResultActivity;
import com.example.market.model.Main;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class StartAdapter extends RecyclerView.Adapter<StartAdapter.ViewHolder> {
    Context context;
    ArrayList<Main> main = new ArrayList<Main>();
    AdapterView.OnItemClickListener listener;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_main, viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Main mainV = main.get(i);
        viewHolder.setItem(mainV);
        viewHolder.setOnItemClickListener(listener);
    }

    public StartAdapter(Context context) {
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

    @Override
    public int getItemCount() {
        return main.size();
    }
    public void clear(){
        main.clear();
    }
    public  void add(Main item){
        main.add(item);
    }
    public Main getItem(int position){
        return  main.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView list_image_imageView,list_good_imageView;
        TextView list_subject_tv,list_locationTime_tv,list_price_tv,list_good_tv;
        AdapterView.OnItemClickListener listener;
        public ViewHolder(@NonNull final  View itemView){
            super(itemView);
            list_image_imageView = itemView.findViewById(R.id.list_image_imageView);
            list_subject_tv = itemView.findViewById(R.id.list_subject_tv);
            list_locationTime_tv = itemView.findViewById(R.id.list_locationTime_tv);
            list_price_tv = itemView.findViewById(R.id.list_price_tv);
            list_good_imageView = itemView.findViewById(R.id.list_good_imageView);
            list_good_tv = itemView.findViewById(R.id.list_good_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Main item = getItem(position);
                    Intent intent = new Intent(v.getContext(), ResultActivity.class);
                    intent.putExtra("list",main);
                    intent.putExtra("item",item);
                    Log.e("[test]", "user_name = " + item.getUser_name());
                    intent.putExtra("num",item.getNum());
                    v.getContext().startActivity(intent);
                }
            });
        }
        public void setItem(Main mainV){
            imageLoader.displayImage(mainV.getImage0(),list_image_imageView,options);
            list_subject_tv.setText(mainV.getBoard_subject());
            list_locationTime_tv.setText(mainV.getArea() + "/" + mainV.getBoard_date());
            list_price_tv.setText(mainV.getGoods_price() + "원");
            list_good_tv.setText(String.valueOf(mainV.getInterest_count()));
        }
        public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
            this.listener = listener;
        }
    }

}
