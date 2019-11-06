package com.example.market;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.constants.Constants;
import com.example.market.model.ArticleInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.example.market.constants.Constants.searchInLocationURL;

public class LocationSearchActivity extends AppCompatActivity {
    private final String TAG = "LocaSearch-";

    RecyclerView locationSearch_recycler_searchList;
    ArrayList<ArticleInfo> itemList;

    String query;

    AsyncHttpClient client;

    String[] categoryArr;
    SharedPreferences pref;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationsearch);

        locationSearch_recycler_searchList = findViewById(R.id.locationSearch_recycler_searchList);
        itemList = new ArrayList<>();

        client = new AsyncHttpClient();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>검색</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        lat = Double.parseDouble(pref.getString("lat", "0.0"));
        lng = Double.parseDouble(pref.getString("lng", "0.0"));
        categoryArr = new String[Constants.categorySize];
        int len = Constants.categorySize;
        for (int i = 0; i < len; i++) {
            categoryArr[i] = pref.getString("ch"+(i+1), "");
            Log.e(TAG+"프리퍼런스", "ch" +(i+1) + " = " + categoryArr[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.setClickable(true);
//        searchView.requestFocus();
//        searchView.callOnClick();
        searchView.performClick();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("'거리순'으로 검색");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = s;
                searchLocationItem(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }










    private void searchLocationItem(String keyword) {
        RequestParams params = new RequestParams("keyword", keyword);
        for (int i = 0; i < Constants.categorySize; i++) {
            params.put("category"+(i+1), categoryArr[i]);
        }
        params.put("lat", ""+lat);
        params.put("lng", ""+lng);
        client.post(searchInLocationURL, params, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(TAG + "통신성공", "*******************************************************statusCode = " + statusCode);
                itemList.clear();
                String response = new String(responseBody);
                Log.e(TAG + "통신성공", "*******************************************************response = " + response);

                try {
                    JSONObject acceptClass = new JSONObject(response);
                    JSONArray articleArray = acceptClass.getJSONArray("item");

                    int len = articleArray.length();
                    //30개씩 불러오기
                    for (int i = 0; i < len; i++) {
                        JSONObject articleOne = (JSONObject) articleArray.get(i);
                        ArticleInfo articleInfo = new ArticleInfo();
                        articleInfo.setNum(articleOne.getInt("num"));
                        articleInfo.setUser_name(articleOne.getString("user_name"));
                        articleInfo.setArea(articleOne.getString("area"));
                        articleInfo.setCategory_code(articleOne.getString("category_code"));
                        articleInfo.setPrice(articleOne.getInt("price"));

                        articleInfo.setSubject(articleOne.getString("subject"));
                        articleInfo.setContent(articleOne.getString("content"));
                        articleInfo.setImage0(articleOne.getString("image0"));     //not null
                        articleInfo.setBoard_date(articleOne.getString("board_date"));

                        articleInfo.setReadcount(articleOne.getInt("readcount"));
                        articleInfo.setReply_count(articleOne.getInt("reply_count"));
                        articleInfo.setInterest_count(articleOne.getInt("interest_count"));

                        itemList.add(articleInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (itemList.isEmpty()) {
                    Log.e(TAG + "아이템리스트", "아이템 리스트 얻기 실패");
                    return;
                }
                locationSearch_recycler_searchList.setLayoutManager(new LinearLayoutManager(LocationSearchActivity.this, LinearLayoutManager.VERTICAL, false));
                locationSearch_recycler_searchList.setAdapter(new LocationSearchRecyclerAdapter(itemList));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody);
                Log.e(TAG + "통신실패", "*******************************************************response = " + response);
                Log.e(TAG + "통신실패", error.getLocalizedMessage());
                Log.e(TAG + "통신실패", error.getMessage());
            }
        });

    }



    private class LocationSearchRecyclerAdapter extends RecyclerView.Adapter<LocationSearchRecyclerAdapter.ViewHolder> {
        ArrayList<ArticleInfo> itemList;
        ImageLoader imageLoader;
        DisplayImageOptions options;


        public LocationSearchRecyclerAdapter(ArrayList<ArticleInfo> itemList) {
            this.itemList = itemList;
            initImageLoader();
        }

        private void initImageLoader() {
            imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getApplicationContext());
                imageLoader.init(configuration);
            }
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
            builder.showImageOnLoading(R.drawable.ic_stub);
            builder.showImageForEmptyUri(R.drawable.ic_empty);
            builder.showImageOnFail(R.drawable.ic_error);
            options = builder.build();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.item_location, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            ArticleInfo articleInfo = itemList.get(i);

            //이미지
            imageLoader.displayImage(articleInfo.getImage0(), viewHolder.location_img_mainImage, options);

            viewHolder.location_text_subject.setText(articleInfo.getSubject());
            viewHolder.location_text_distance.setText(articleInfo.getDistance());
            viewHolder.location_text_area.setText(articleInfo.getArea());
            viewHolder.location_text_time.setText(articleInfo.getBoard_date());
            viewHolder.location_text_price.setText(""+articleInfo.getPrice());
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView location_img_mainImage;
            TextView location_text_subject, location_text_distance, location_text_area, location_text_time, location_text_price;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                location_img_mainImage = itemView.findViewById(R.id.location_img_mainImage);

                location_text_subject = itemView.findViewById(R.id.location_text_subject);

                location_text_distance = itemView.findViewById(R.id.location_text_distance);
                location_text_area = itemView.findViewById(R.id.location_text_area);

                location_text_time = itemView.findViewById(R.id.location_text_time);
                location_text_price = itemView.findViewById(R.id.location_text_price);
            }
        }
    }







    @Override
    protected void onDestroy() {
        super.onDestroy();
        query = null;
    }


}
