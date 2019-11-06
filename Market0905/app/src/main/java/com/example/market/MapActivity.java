package com.example.market;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    private final String TAG = "MapActi-";

    private final String[] spinnerStringArr = {"500m", "1km", "3km"};
    Spinner map_spinner;
    ArrayAdapter<String> spinnerAdapter;


    NaverMap naverMap;
    SharedPreferences pref;
    double lat, lng;
    CircleOverlay circle;


    FragmentManager fm;

    AsyncHttpClient client;


    ArrayList<Main> itemList;
    String[] categoryArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        lat = Double.parseDouble(pref.getString("lat", "0.0"));
        lng = Double.parseDouble(pref.getString("lng", "0.0"));

        itemList = new ArrayList<>();

        categoryArr = new String[Constants.categorySize];
        int len = Constants.categorySize;
        for (int i = 0; i < len; i++) {
            categoryArr[i] = pref.getString("ch"+(i+1), "");
            Log.e(TAG+"프리퍼런스", "ch" +(i+1) + " = " + categoryArr[i]);
        }


        fm = getSupportFragmentManager();

        client = new AsyncHttpClient();
//        MapFragment mapFragment1 = (MapFragment)fm.findFragmentById(R.id.map);
//        if (mapFragment1 == null) {
//            mapFragment1 = MapFragment.newInstance();
//            fm.beginTransaction().add(R.id.map, mapFragment1).commit();      // != null 일때 비긴트랜잭션 하는지
//        }

        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(lat, lng), 15))       //초기 카메라
                .mapType(NaverMap.MapType.Basic)
                .enabledLayerGroups(NaverMap.LAYER_GROUP_BUILDING, NaverMap.LAYER_GROUP_TRANSIT)
                .indoorEnabled(true)
                .lightness(-0.2f)
                .symbolScale(0.8f);

        MapFragment mapFragment = MapFragment.newInstance(options);
        fm.beginTransaction().add(R.id.map_mapFragment_naverMap, mapFragment).commit();      // != null 일때 비긴트랜잭션 하는지

        mapFragment.getMapAsync(this);

        //액션바 숨기기
        getSupportActionBar().hide();

        //스피너 설정
        map_spinner = findViewById(R.id.map_spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, spinnerStringArr);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        map_spinner.setAdapter(spinnerAdapter);
        map_spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            Log.e(TAG+"스피너", "0번선택");

            if(circle == null || naverMap == null) return;

            circle.setRadius(500);
            naverMap.setCameraPosition(new CameraPosition(new LatLng(lat, lng), 15));


            //주변 마커
            RequestParams params = new RequestParams();
            params.put("coverage", "500");
            params.put("lat", ""+lat);
            params.put("lng", ""+lng);
            for (int i = 0; i < Constants.categorySize; i++) {
                params.put("category"+(i+1), categoryArr[i]);
            }
            final NaverMap finalNaverMap = naverMap;
            AsyncHttpResponseHandler response = new MapResponse(finalNaverMap);

            client.post(Constants.MapURL, params, response);
        }
        else if(position == 1){
            Log.e(TAG+"스피너", "1번선택");

            circle.setRadius(1000);
            naverMap.setCameraPosition(new CameraPosition(new LatLng(lat, lng), 14));


            //주변 마커
            RequestParams params = new RequestParams();
            params.put("coverage", "1");
            params.put("lat", ""+lat);
            params.put("lng", ""+lng);
            for (int i = 0; i < Constants.categorySize; i++) {
                params.put("category"+(i+1), categoryArr[i]);
            }
            final NaverMap finalNaverMap = naverMap;
            AsyncHttpResponseHandler response = new MapResponse(finalNaverMap);

            client.post(Constants.MapURL, params, response);
        }
        else if(position == 2){
            Log.e(TAG+"스피너", "2번선택");

            circle.setRadius(3000);
            naverMap.setCameraPosition(new CameraPosition(new LatLng(lat, lng), 12));


            //주변 마커
            RequestParams params = new RequestParams();
            params.put("coverage", "3");
            params.put("lat", ""+lat);
            params.put("lng", ""+lng);
            for (int i = 0; i < Constants.categorySize; i++) {
                params.put("category"+(i+1), categoryArr[i]);
            }
            final NaverMap finalNaverMap = naverMap;
            AsyncHttpResponseHandler response = new MapResponse(finalNaverMap);

            client.post(Constants.MapURL, params, response);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //액션바 보여주기
        getSupportActionBar().show();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
//        naverMap.setMapType(NaverMap.MapType.Basic);
//
//        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false);
//        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);
//        naverMap.setIndoorEnabled(true);
//
//        naverMap.setLightness(-0.2f);
//
//        naverMap.setSymbolScale(0.8f);

        /*
        내위치 카메라, 마커, 줌 레벨
        주변 영역 표시, 주변 마커

        상세보기 연결

        겹침
         */

//        NaverMapOptions options = new NaverMapOptions()
//                .camera(new CameraPosition(new LatLng(35.1798159, 129.0750222), 8))       //초기 카메라
//                .mapType(NaverMap.MapType.Terrain);


        circle = new CircleOverlay();
        circle.setCenter(new LatLng(lat, lng));
        circle.setRadius(500);
        circle.setOutlineWidth(8);
        circle.setOutlineColor(Color.rgb(63, 81, 181));
        circle.setColor(Color.argb(50, 63, 81, 181));
        circle.setMap(naverMap);





        Marker userMarker = new Marker();
        userMarker.setPosition(new LatLng(lat, lng));
        userMarker.setIcon(MarkerIcons.GREEN);
        userMarker.setCaptionAlign(Align.Top);
        userMarker.setWidth(150);
        userMarker.setHeight(300);
        userMarker.setCaptionTextSize(20);
        userMarker.setCaptionRequestedWidth(200);
        userMarker.setCaptionColor(Color.BLACK);
        userMarker.setCaptionText("내 위치");
        userMarker.setMap(naverMap);

        RequestParams params = new RequestParams();
        params.put("coverage", "500");
        params.put("lat", lat);
        params.put("lng", lng);
        for (int i = 0; i < Constants.categorySize; i++) {
            params.put("category"+(i+1), categoryArr[i]);
        }

        //주변 마커
        final NaverMap finalNaverMap = naverMap;
        AsyncHttpResponseHandler response = new MapResponse(finalNaverMap);
        client.post(Constants.MapURL, params, response);
//        client.post(Constants.MapURL, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.e(TAG+"주변마커통신", "***********************************************통신 성공");
//
//                String strJson = new String(responseBody);
//
//                try {
//                    JSONObject json = new JSONObject(strJson);
//                    JSONArray item = json.getJSONArray("item");
//                    int len = item.length();
//                    for (int i = 0; i < len; i++) {
//                        JSONObject article = item.getJSONObject(i);
//
//                        Main main = new Main();
//                        main.setLat(article.getDouble("lat"));
//                        main.setLng(article.getDouble("lng"));
//
//                        main.setNum(article.getInt("num"));
//                        main.setBoard_subject(article.getString("subject"));
//                        main.setCategory_code(article.getString("category_code"));
//
//                        main.setUser_name(article.getString("user_name"));
//                        main.setArea(article.getString("area"));
//                        main.setDistance(article.getString("distance"));
//                        main.setGoods_price(article.getInt("price"));
//
//                        main.setContent(article.getString("content"));
//                        main.setImage0(article.getString("image0"));     //not null
//                        main.setImage1(article.getString("image1"));
//                        main.setImage2(article.getString("image2"));
//                        main.setBoard_date(article.getString("board_date"));
//
//                        main.setRead_count(article.getInt("readcount"));
//                        main.setReply_count(article.getInt("reply_count"));
//                        main.setInterest_count(article.getInt("interest_count"));
//
//                        itemList.add(main);
//
//                        Marker aroundMarker = new Marker();
//                        aroundMarker.setPosition(new LatLng(article.getDouble("lat"), article.getDouble("lng")));
//                        aroundMarker.setIcon(MarkerIcons.BLACK);
//                        aroundMarker.setIconTintColor(Color.rgb(255, 148, 36));
//                        aroundMarker.setCaptionAlign(Align.Top);
//                        aroundMarker.setWidth(100);
//                        aroundMarker.setHeight(200);
//                        aroundMarker.setCaptionTextSize(18);
//                        aroundMarker.setCaptionRequestedWidth(300);
//                        aroundMarker.setCaptionColor(Color.rgb(63, 81, 181));
//                        aroundMarker.setCaptionText(article.getString("subject"));
//                        aroundMarker.setSubCaptionText(article.getString("category_code"));
//                        aroundMarker.setSubCaptionColor(Color.BLUE);
//                        aroundMarker.setSubCaptionHaloColor(Color.rgb(200, 255, 200));
//                        aroundMarker.setSubCaptionTextSize(16);
//
//                        aroundMarker.setMap(finalNaverMap);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.e(TAG+"주변마커통신", "***********************************************통신 실패");
//            }
//        });

    }



    class MapResponse extends AsyncHttpResponseHandler implements Overlay.OnClickListener{
        NaverMap naverMap;
        InfoWindow infoWindow;

        public MapResponse(NaverMap finalNaverMap) {
            this.naverMap = finalNaverMap;
            this.infoWindow = new InfoWindow();

            setOnMapClickListener();
        }

        private void setOnMapClickListener() {
            naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                    infoWindow.close();
                }
            });
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.e(TAG+"주변마커통신", "***********************************************통신 성공");

            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray item = json.getJSONArray("item");
                int len = item.length();
                for (int i = 0; i < len; i++) {
                    JSONObject article = item.getJSONObject(i);

                    Main main = new Main();
                    main.setLat(article.getDouble("lat"));
                    main.setLng(article.getDouble("lng"));

                    main.setNum(article.getInt("num"));
                    main.setBoard_subject(article.getString("subject"));
                    main.setCategory_code(article.getString("category_code"));

                    main.setUser_name(article.getString("user_name"));
                    main.setArea(article.getString("area"));
                    main.setDistance(article.getString("distance"));
                    main.setGoods_price(article.getInt("price"));

                    main.setContent(article.getString("content"));
                    main.setImage0(article.getString("image0"));     //not null
                    main.setImage1(article.getString("image1"));
                    main.setImage2(article.getString("image2"));
                    main.setBoard_date(article.getString("board_date"));

                    main.setRead_count(article.getInt("readcount"));
                    main.setReply_count(article.getInt("reply_count"));
                    main.setInterest_count(article.getInt("interest_count"));

                    itemList.add(main);

                    Marker aroundMarker = new Marker();
                    aroundMarker.setPosition(new LatLng(article.getDouble("lat"), article.getDouble("lng")));
                    aroundMarker.setIcon(MarkerIcons.BLACK);
                    aroundMarker.setIconTintColor(Color.rgb(255, 148, 36));
                    aroundMarker.setCaptionAlign(Align.Top);
                    aroundMarker.setWidth(100);
                    aroundMarker.setHeight(200);
                    aroundMarker.setCaptionTextSize(18);
                    aroundMarker.setCaptionRequestedWidth(300);
                    aroundMarker.setCaptionColor(Color.rgb(63, 81, 181));
                    aroundMarker.setCaptionText(article.getString("subject"));
                    aroundMarker.setSubCaptionText(article.getString("category_code"));
                    aroundMarker.setSubCaptionColor(Color.BLUE);
                    aroundMarker.setSubCaptionHaloColor(Color.rgb(200, 255, 200));
                    aroundMarker.setSubCaptionTextSize(16);

                    aroundMarker.setMap(naverMap);
                    aroundMarker.setTag((Integer)i);
                    aroundMarker.setOnClickListener(this);
                }

                infoWindow.setAdapter(new InfoWindowAdapter(MapActivity.this, itemList));
                infoWindow.setOnClickListener(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.e(TAG+"주변마커통신", "***********************************************통신 실패");
        }

        @Override
        public boolean onClick(@NonNull Overlay overlay) {
            if(overlay instanceof Marker){
                Toast.makeText(MapActivity.this, "1", Toast.LENGTH_SHORT).show();
                Marker marker = (Marker)overlay;

                if (marker.getInfoWindow() == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(marker);
                } else {
                    // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                    infoWindow.close();
                }

                return true;
            }
            else if(overlay instanceof InfoWindow){
                Toast.makeText(MapActivity.this, "2", Toast.LENGTH_SHORT).show();

                Main item = itemList.get((Integer)infoWindow.getMarker().getTag());
                Intent intent = new Intent(MapActivity.this, ResultActivity.class);
                intent.putExtra("list", itemList);
                intent.putExtra("item",item);
                intent.putExtra("num",item.getNum());
                MapActivity.this.startActivity(intent);

                return true;
            }

            return false;
        }
    }

    class InfoWindowAdapter extends InfoWindow.DefaultViewAdapter implements View.OnClickListener{
        ArrayList<Main> itemList;
        Activity activity;
        int i;

        ImageLoader imageLoader;
        DisplayImageOptions options;

        View view;
        ImageView mapItem_img_mainImage;
        TextView mapItem_text_subject;
        TextView mapItem_text_price;
        TextView mapItem_text_userName;
        TextView mapItem_text_distance;

        public InfoWindowAdapter(@NonNull Context context, ArrayList<Main> itemList) {
            super(context);

            this.activity = (Activity)context;
            this.itemList = itemList;

            viewAndSubviewInit();
            initImageLoader();
        }

        private void viewAndSubviewInit() {
            view = LayoutInflater.from(activity).inflate(R.layout.map_item_infowindow, new LinearLayout(activity));
            mapItem_img_mainImage = view.findViewById(R.id.mapItem_img_mainImage);
            mapItem_text_subject = view.findViewById(R.id.mapItem_text_subject);
            mapItem_text_price = view.findViewById(R.id.mapItem_text_price);
            mapItem_text_userName = view.findViewById(R.id.mapItem_text_userName);
            mapItem_text_distance = view.findViewById(R.id.mapItem_text_distance);
        }

        private void initImageLoader() {
            imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getContext());
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
        protected View getContentView(@NonNull InfoWindow infoWindow) {
            i = (Integer)infoWindow.getMarker().getTag();
            Main item = itemList.get(i);


            //TODO: 이미지 출력
//            mapItem_img_mainImage.setImageResource();
//            mapItem_img_mainImage.setImageURI();
//            mapItem_img_mainImage.setImageBitmap();
//            mapItem_img_mainImage.setImageDrawable();
            Glide.with(MapActivity.this)
                    .load(item.getImage0())
                    .into(mapItem_img_mainImage);
//            imageLoader.displayImage(item.getImage0(), mapItem_img_mainImage, options);
            Log.e(TAG+"getContentView", "item.getImage0() = " + item.getImage0());

            mapItem_text_subject.setText(item.getBoard_subject());
            mapItem_text_price.setText(item.getCategory_code());
            mapItem_text_userName.setText(item.getUser_name());
            mapItem_text_distance.setText(item.getDistance());

            return view;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(MapActivity.this, "3", Toast.LENGTH_SHORT).show();

            Main item = itemList.get(i);
            Intent intent = new Intent(getContext(), ResultActivity.class);
            intent.putExtra("list", itemList);
            intent.putExtra("item",item);
            intent.putExtra("num",item.getNum());
            getContext().startActivity(intent);
        }
    }



}



