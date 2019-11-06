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

public class ImageView3Fragment extends Fragment {
    private static final String IMAGE2 = "image2";
    String image2;
    ImageView frag_image3_imageView;
    ImageLoader imageLoader;
    public ImageView3Fragment(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null){
            image2 = getArguments().getString(IMAGE2);
        }

        return inflater.inflate(R.layout.activity_image_view3_fragment, container, false);

    }
    public static Fragment newInstance(String image2) {
        ImageView3Fragment fragment = new ImageView3Fragment();
        Bundle args = new Bundle();
        args.putString(IMAGE2, image2);
        Log.d("[이미지3]",""+image2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frag_image3_imageView = view.findViewById(R.id.frag_image3_imageView);
        imageLoaderInit();
        Log.d("[이미지뷰 3]",""+image2);
        imageLoader.displayImage(image2,frag_image3_imageView);
    }
    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getContext());
            imageLoader.init(configuration);
        }
    }

}
