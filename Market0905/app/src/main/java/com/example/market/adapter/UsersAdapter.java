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

import com.example.market.R;
import com.example.market.UserActivity;
import com.example.market.model.Main;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    Context context;
    ArrayList<Main> main = new ArrayList<Main>();
    AdapterView.OnItemClickListener listener;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_user, viewGroup,false);
        return new ViewHolder(itemView);
    }

    public UsersAdapter(Context context) {
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
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder viewHolder, int i) {
        Main mainV = main.get(i);
        viewHolder.setItem(mainV);
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
    @Override
    public int getItemCount() {
        return main.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView list_profile_imageView;
        TextView list_nameCode_tv,list_area_tv;
        LinearLayout list_user_layout;
        AdapterView.OnItemClickListener listener;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            list_user_layout = itemView.findViewById(R.id.list_user_layout);
            list_profile_imageView = itemView.findViewById(R.id.list_profile_imageView);
            list_nameCode_tv = itemView.findViewById(R.id.list_nameCode_tv);
            list_area_tv = itemView.findViewById(R.id.list_area_tv);
            list_user_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Main item = getItem(position);
                    Intent intent = new Intent(context, UserActivity.class);
                    intent.putExtra("item",item);
                    context.startActivity(intent);
                }
            });
        }

        public void setItem(Main mainV) {
            imageLoader.displayImage(mainV.getUser_photo(),list_profile_imageView,options);
            list_nameCode_tv.setText(mainV.getUser_name() + "  " + mainV.getUser_code());
            list_area_tv.setText(mainV.getArea());
        }
    }
}
