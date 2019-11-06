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

public class ImageView2Fragment extends Fragment {
    private static final String IMAGE1 = "image1";
    String image1;
    ImageView frag_image2_imageView;
    ImageLoader imageLoader;
    public ImageView2Fragment(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null){
            image1 = getArguments().getString(IMAGE1);
        }

        return inflater.inflate(R.layout.activity_image_view2_fragment, container, false);

    }
    public static Fragment newInstance(String image1) {
        ImageView2Fragment fragment = new ImageView2Fragment();
        Bundle args = new Bundle();
        args.putString(IMAGE1, image1);
        Log.d("[이미지1]",""+image1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frag_image2_imageView = view.findViewById(R.id.frag_image2_imageView);
        imageLoaderInit();
        Log.d("[이미지뷰 2]",""+image1);
        imageLoader.displayImage(image1,frag_image2_imageView);
    }
    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getContext());
            imageLoader.init(configuration);
        }
    }
}
