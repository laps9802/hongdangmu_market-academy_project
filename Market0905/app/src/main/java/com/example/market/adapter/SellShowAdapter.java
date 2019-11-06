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

import com.example.market.R;
import com.example.market.ResultActivity;
import com.example.market.model.Main;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class SellShowAdapter extends RecyclerView.Adapter<SellShowAdapter.ViewHolder> {
    Context context;
    ArrayList<Main> main = new ArrayList<Main>();
    AdapterView.OnItemClickListener listener;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    int j;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_sellshow, viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Main mainV = main.get(i);
        viewHolder.setItem(mainV);
        viewHolder.setOnItemClickListener(listener);
    }
    public SellShowAdapter(Context context, int j) {
        this.context = context;
        this.j = j;
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

    public Main getItem(int position){
        return main.get(position);
    }
    public void clear(){
        main.clear();
    }
    public void add(Main item){
        main.add(item);
    }
    @Override
    public int getItemCount() {
        if (j==1&&main.size()>4){
            return 5;
        }else {
            return main.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView list_sellShow_imageView;
        AdapterView.OnItemClickListener listener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_sellShow_imageView = itemView.findViewById(R.id.list_sellShow_imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Main item = getItem(position);
                    Intent intent = new Intent(v.getContext(), ResultActivity.class);
                    intent.putExtra("item",item);
                    intent.putExtra("num",item.getNum());
                    v.getContext().startActivity(intent);
                }
            });
        }
        public void setItem(Main mainV){
            imageLoader.displayImage(mainV.getImage0(),list_sellShow_imageView,options);
        }
        public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
            this.listener = listener;
        }
    }
}
