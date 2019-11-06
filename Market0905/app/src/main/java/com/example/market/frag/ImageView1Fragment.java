package com.example.market.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.market.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageView1Fragment extends Fragment {
    private static final String IMAGE0 = "image0";
    String image0;
    ImageView frag_image1_imageView;
    ImageLoader imageLoader;
    public ImageView1Fragment(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null){
            image0 = getArguments().getString(IMAGE0);
            Log.d("[이미지1]",""+image0);
        }

        return inflater.inflate(R.layout.activity_image_view1_fragment, container, false);

    }
    public static Fragment newInstance(String image0) {
        ImageView1Fragment fragment = new ImageView1Fragment();
        Bundle args = new Bundle();
        args.putString(IMAGE0, image0);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frag_image1_imageView = view.findViewById(R.id.frag_image1_imageView);
        imageLoaderInit();
        Log.d("[이미지2]",""+image0);
        imageLoader.displayImage(image0,frag_image1_imageView);
    }
    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getContext());
            imageLoader.init(configuration);
        }
    }
}
