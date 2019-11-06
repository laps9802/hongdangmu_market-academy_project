package com.example.market.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.model.Main;
import com.example.market.sub.BuyList;
import com.example.market.sub.ChangeProfile;
import com.example.market.sub.InterestList;
import com.example.market.sub.Profile;
import com.example.market.sub.SellList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<Main> {
    Activity activity;
    int resource;
    ImageLoader imageLoader;    // 서버에 이미지를 요청, 응답 처리를 함께함
    DisplayImageOptions options;    // 이미지로더 옵션 객체

    public UserAdapter(Context context, int resource, ArrayList<Main> objects) {
        super(context, resource);
        activity = (Activity) context;
        this.resource = resource;
        imageLoaderInit();
    }

    private void imageLoaderInit() {
        // ImageLoader 초기화
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {  // 초기화되어있지 않으면
            ImageLoaderConfiguration configuration =
                    ImageLoaderConfiguration.createDefault(activity);
            imageLoader.init(configuration);
        }
        // 이미지로더 옵션 설정
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        // 다운로드될 동안 표시할 임시 이미지
        builder.showImageOnLoading(R.drawable.ic_stub);
        // 이미지가 지정되지 않은 경우 사용될 이미지
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        // 다운로드 실패시에 사용할 이미지
        builder.showImageOnFail(R.drawable.ic_error);
        // option 만들기
        options = builder.build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        final Main main = getItem(position);

        if (main != null) {
            ImageView imageView_photo = convertView.findViewById(R.id.imageView_photo);
            TextView textView_nickName = convertView.findViewById(R.id.textView_nickName);
            TextView textView_local = convertView.findViewById(R.id.textView_local);
            TextView textView_code = convertView.findViewById(R.id.textView_code);

            LinearLayout linearLayout_profile = convertView.findViewById(R.id.linearLayout_profile);

            LinearLayout linearLayout_sellList = convertView.findViewById(R.id.linearLayout_sellList);
            ImageView imageViewWatchList = convertView.findViewById(R.id.imageViewWatchList);
            TextView textViewWatchList = convertView.findViewById(R.id.textViewWatchList);

            LinearLayout linearLayout_buyList = convertView.findViewById(R.id.linearLayout_buyList);
            ImageView imageViewBuy = convertView.findViewById(R.id.imageViewBuy);
            TextView textViewBuy = convertView.findViewById(R.id.textViewBuy);

            LinearLayout linearLayout_interestList = convertView.findViewById(R.id.linearLayout_interestList);
            ImageView imageViewSell = convertView.findViewById(R.id.imageViewSell);
            TextView textViewSell = convertView.findViewById(R.id.textViewSell);

            imageLoader.displayImage(main.getUser_photo(), imageView_photo, options);
            textView_nickName.setText(main.getUser_name());
            textView_local.setText(main.getArea());
            textView_code.setText(" " + main.getUser_code());

            imageView_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ChangeProfile.class);
                    intent.putExtra("users", main);
                    activity.startActivity(intent);
                    Toast.makeText(activity, "프로필 변경 이동", Toast.LENGTH_SHORT).show();
                }
            });

            linearLayout_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, Profile.class);
                    intent.putExtra("users", main);
                    activity.startActivity(intent);
                    Toast.makeText(activity, "프로필 이동", Toast.LENGTH_SHORT).show();
                }
            });

            linearLayout_sellList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, SellList.class);
                    intent.putExtra("users", main);
                    activity.startActivity(intent);
                    Toast.makeText(activity, "판매내역 이동", Toast.LENGTH_SHORT).show();
                }
            });

            linearLayout_buyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, BuyList.class);
                    intent.putExtra("users", main);
                    activity.startActivity(intent);
                    Toast.makeText(activity, "구매내역 이동", Toast.LENGTH_SHORT).show();
                }
            });

            linearLayout_interestList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, InterestList.class);
                    intent.putExtra("users", main);
                    activity.startActivity(intent);
                    Toast.makeText(activity, "관심목록 이동", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return convertView;
    }
}
