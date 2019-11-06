package com.example.market.adapter;

import android.app.Activity;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.model.Main;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class ImageAdapter extends PagerAdapter {
    Activity activity;
    List<Main> list;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    public ImageAdapter(Activity activity, List<Main> list) {
        this.activity = activity;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.list_result,null);
        final Main main = list.get(position);
        imageLoaderInit();
        ImageView result_image_imageView = itemView.findViewById(R.id.result_image_imageView);
        Log.d("[1]",main.getImage0());
        imageLoader.displayImage(main.getImage0(),result_image_imageView,options);
        container.addView(itemView,0);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
    private void imageLoaderInit() {
        Log.d("[Test]","초기화");
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
            imageLoader.init(configuration);
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);

        options = builder.build();
    }
}
