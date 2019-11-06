package com.example.market.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.model.Main;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Main> review = new ArrayList<>(); // items
    Activity activity;

    public ReviewAdapter(ArrayList<Main> review, Activity activity) {
        this.review = review;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_photo;
        TextView textView_user_name, textView_area, textView_review_content,
                textView_review_date;
        ImageLoader imageLoader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_photo = itemView.findViewById(R.id.imageView_photo);
            textView_user_name = itemView.findViewById(R.id.textView_user_name);
            textView_area = itemView.findViewById(R.id.textView_area);
            textView_review_content = itemView.findViewById(R.id.textView_review_content);
            textView_review_date = itemView.findViewById(R.id.textView_review_date);
            imageLoader = ImageLoader.getInstance();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View review = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_review, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(review);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Main item = review.get(i);
        ViewHolder holder = (ViewHolder) viewHolder;
        Log.d("[정보]", "holder.imageView_photo = " + holder.imageView_photo);
        holder.imageLoader.displayImage(item.getUser_photo(), holder.imageView_photo);
        holder.textView_user_name.setText(item.getUser_name());
        holder.textView_area.setText(item.getArea());
        holder.textView_review_content.setText(item.getReview_content());
        holder.textView_review_date.setText(item.getReview_date());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void add(Main item) {
        review.add(item);
    }

    @Override
    public int getItemCount() {
        return review.size();
    }

}
