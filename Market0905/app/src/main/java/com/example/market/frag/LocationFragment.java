package com.example.market.frag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.market.ChoiceActivity;
import com.example.market.LocationSearchActivity;
import com.example.market.LocationSettingActivity;
import com.example.market.MainActivity;
import com.example.market.MapActivity;
import com.example.market.R;
import com.example.market.ResultActivity;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
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

import static com.example.market.constants.Constants.locationList_URL;

public class LocationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //전역데이터
    private final String TAG = "LocaFrag-";
    MainActivity mainActivity;
    private final String[] spinnerStringArr = {"미설정", "내 동네 설정"};

    //화면
    Spinner location_spinner;
    ArrayAdapter<String> spinnerAdapter;
    ImageView location_img_map2;
    ImageView location_img_category;
    ImageView location_search_searchInLocation;

    RecyclerView location_recycler_listView;
    ArrayList<Main> itemList;

    Button loca_btn_updateLocation;

    MenuItem mMenu;

    //일반
    double lat, lng;


    String[] categoryArr;

    //통신
    AsyncHttpClient client;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemList = new ArrayList<>();
        client = new AsyncHttpClient();

        SharedPreferences pref = mainActivity.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        lat = Double.parseDouble(pref.getString("lat", "0.0"));
        lng = Double.parseDouble(pref.getString("lng", "0.0"));


        categoryArr = new String[Constants.categorySize];
        int len = Constants.categorySize;
        for (int i = 0; i < len; i++) {
            categoryArr[i] = pref.getString("ch"+(i+1), "");
            Log.e(TAG+"프리퍼런스", "ch" +(i+1) + " = " + categoryArr[i]);
        }

        //액션바 숨기기
        mainActivity.getSupportActionBar().hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //액션바 보여주기
        mainActivity.getSupportActionBar().show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loca, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        location_img_map2 = v.findViewById(R.id.location_img_map2);
        location_img_map2.setOnClickListener(this);

        location_img_category = v.findViewById(R.id.location_img_category);
        location_img_category.setOnClickListener(this);

        location_search_searchInLocation = v.findViewById(R.id.location_search_searchInLocation);
        location_search_searchInLocation.setOnClickListener(this);

        location_recycler_listView = v.findViewById(R.id.location_recycler_itemList);
        location_recycler_listView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), location_recycler_listView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // event code
                        Main item = itemList.get(position);
                        Intent intent = new Intent(getContext(), ResultActivity.class);
                        intent.putExtra("list", itemList);
                        intent.putExtra("item",item);
                        intent.putExtra("num",item.getNum());
                        getContext().startActivity(intent);
                    }
                }
        ));


        //스피너 설정
        SharedPreferences pref = mainActivity.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String area = pref.getString("area", "");
        if(area != null && !area.equals("")){
            spinnerStringArr[0] = area;
        }

        location_spinner = v.findViewById(R.id.location_spinner);
//        spinnerAdapter = new CustomAdapter(mainActivity, spinnerStringArr);
        spinnerAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_dropdown_item_1line, spinnerStringArr);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location_spinner.setAdapter(spinnerAdapter);

        location_spinner.setOnItemSelectedListener(this);

        Log.e(TAG+"리스트", "불러오기");
        loadList();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences pref = mainActivity.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String area = pref.getString("area", "");
        if(area != null && !area.equals("")){
            spinnerStringArr[0] = area;
        }
        spinnerAdapter.notifyDataSetChanged();

        location_spinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 1){
            Log.e(TAG+"스피너", "1번선택");
            Intent locSetIntent = new Intent(mainActivity, LocationSettingActivity.class);
            startActivity(locSetIntent);

            parent.setSelection(0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_img_map2:
                startActivity(new Intent(mainActivity, MapActivity.class));
                break;
            case R.id.location_img_category:
                Intent intent = new Intent(mainActivity, ChoiceActivity.class);
                intent.putExtra("ch1", true);
                startActivityForResult(intent, 100);
                break;
            case R.id.location_search_searchInLocation:
                startActivity(new Intent(mainActivity, LocationSearchActivity.class));
                break;
        }
    }



















































    private void loadList() {
        RequestParams params = new RequestParams();
        for (int i = 0; i < Constants.categorySize; i++) {
            params.put("category"+(i+1), categoryArr[i]);
        }
        params.put("lat", ""+lat);
        params.put("lng", ""+lng);
        client.post(locationList_URL, params, new AsyncHttpResponseHandler() {           //post로 통일
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(TAG + "통신성공", "*******************************************************statusCode = " + statusCode);
                itemList.clear();
                String response = new String(responseBody);
                Log.e(TAG + "통신성공", "*******************************************************response = " + response);

                try {
                    JSONObject acceptClass = new JSONObject(response);
                    String rt = acceptClass.getString("rt");
                    if(rt.equals("Fail")){
                        Log.e(TAG + "통신성공", "얻은 값 없음");
                        return;
                    } else if(rt.equals("Location missed")){
                        Log.e(TAG + "통신성공", "위치정보 안보냄");
                        return;
                    }
                    JSONArray articleArray = acceptClass.getJSONArray("item");

                    int len = articleArray.length();
                    //30개씩 불러오기
                    for (int i = 0; i < len; i++) {
                        JSONObject articleOne = (JSONObject) articleArray.get(i);
                        Main main = new Main();
                        main.setNum(articleOne.getInt("num"));
                        main.setUser_name(articleOne.getString("user_name"));
                        main.setArea(articleOne.getString("area"));
                        main.setDistance(articleOne.getString("distance"));
                        main.setCategory_code(articleOne.getString("category_code"));
                        main.setGoods_price(articleOne.getInt("price"));

                        main.setBoard_subject(articleOne.getString("subject"));
                        main.setContent(articleOne.getString("content"));
                        main.setImage0(articleOne.getString("image0"));     //not null
                        main.setImage1(articleOne.getString("image1"));
                        main.setImage2(articleOne.getString("image2"));
                        main.setBoard_date(articleOne.getString("board_date"));

                        main.setRead_count(articleOne.getInt("readcount"));
                        main.setReply_count(articleOne.getInt("reply_count"));
                        main.setInterest_count(articleOne.getInt("interest_count"));

                        itemList.add(main);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (itemList.isEmpty()) {
                    Log.e(TAG + "아이템리스트", "아이템 리스트 얻기 실패");
                    return;
                }
                location_recycler_listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                location_recycler_listView.setAdapter(new LocationRecyclerAdapter(itemList));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody);
                Log.e(TAG + "통신실패", "*******************************************************response = " + response);
                Log.e(TAG + "통신실패", "statusCode = " + statusCode);
                Log.e(TAG + "통신실패", error.getLocalizedMessage());
                Log.e(TAG + "통신실패", error.getMessage());
            }
        });

    }

    class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder> {
        ArrayList<Main> itemList;
        ImageLoader imageLoader;
        DisplayImageOptions options;

        public LocationRecyclerAdapter(ArrayList<Main> itemList) {
            this.itemList = itemList;
            initImageLoader();
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
        public LocationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_location, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationRecyclerAdapter.ViewHolder viewHolder, int position) {
            Main main = itemList.get(position);

            //이미지
            imageLoader.displayImage(main.getImage0(), viewHolder.location_img_mainImage, options);

            viewHolder.location_text_subject.setText(main.getBoard_subject());
            viewHolder.location_text_area.setText(main.getArea());
            viewHolder.location_text_distance.setText(main.getDistance());
            viewHolder.location_text_price.setText(main.getGoods_price() + "원");
            viewHolder.location_text_time.setText(main.getBoard_date());
            viewHolder.location_text_good.setText(""+main.getInterest_count());
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView location_img_mainImage;
            TextView location_text_subject, location_text_distance, location_text_area, location_text_time, location_text_price, location_text_good;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                location_img_mainImage = itemView.findViewById(R.id.location_img_mainImage);

                location_text_subject = itemView.findViewById(R.id.location_text_subject);

                location_text_distance = itemView.findViewById(R.id.location_text_distance);
                location_text_area = itemView.findViewById(R.id.location_text_area);

                location_text_price = itemView.findViewById(R.id.location_text_price);
                location_text_time = itemView.findViewById(R.id.location_text_time);

                location_text_good = itemView.findViewById(R.id.location_text_good);
            }
        }
    }

















    private boolean isNull(Object object){
        if(object == null) return true;
        else return false;
    }











}







class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector detector;
    private OnItemClickListener listener;

    public RecyclerItemClickListener(Context context, RecyclerView recyclerView, OnItemClickListener listener) {
        this.listener = listener;
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }



    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
        View childView = view.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (childView != null && listener != null && detector.onTouchEvent(motionEvent)) {
            try {
                listener.onItemClick(childView, view.getChildAdapterPosition(childView));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {
    }
}
