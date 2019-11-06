package com.example.market;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.adapter.ChatAdapter;
import com.example.market.adapter.ImageAdapter;
import com.example.market.adapter.SellShowAdapter;
import com.example.market.adapter.UserAdapter;
import com.example.market.constants.Constants;
import com.example.market.frag.ImageView1Fragment;
import com.example.market.frag.ImageView2Fragment;
import com.example.market.frag.ImageView3Fragment;
import com.example.market.model.Main;
import com.example.market.response.UsersResponse;
import com.example.market.sub.Profile;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    // 9월 2일
    ImageView result_icon_imageView_on, result_icon_imageView_off;

    ImageView result_image_imageView,result_icon_imageView,result_goods_imageView;
    TextView result_name_tv,result_location_tv,result_subject_tv,result_categoryTime_tv,result_content_tv,result_other_tv,result_manner_tv,result_user_product_tv,result_chatNum_tv,result_price_tv,result_reply_tv,result_showAllImage_tv,result_chatAllShow_tv;
    Button result_chat_btn;
    ViewPager result_image_viewPager,result_image2_viewPager;
    ImageAdapter adapter;
    SellShowAdapter sellAdapter;
    LinearLayout result_name_layout;
    ProgressBar result_manner_progress;
    RecyclerView result_chat_list,result_image2_recyclerView;
    ChatAdapter chatAdapter;
    AsyncHttpClient client;
    response response;
    response2 response2;
    UsersResponse response3;
    response4 response4;
    ArrayList<Main> list;
    UserAdapter userAdapter;
    int num;
    Main item;
    Main users;
    Main profile;
    int j = 0;
    String user_name;
    int user_code;
    ImageLoader imageLoader;

    // 9월 2일
    Response10 response10;
    Response11 response11;
    Response12 response12;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        // history stack 내용 지우기 옵션 설정
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // 대상 Activity가 존재한다면 새로 만들지 말고, 기존 것을 사용하겠다는 설정, 첫 번째 화면만 가능
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        user_name = pref.getString("user_name",null);
        user_code = pref.getInt("user_code",0);
        Log.e("[pref]", "user_code = " + user_code);
        Log.e("[pref]", "user_code = " + pref.getInt("user_code",0));
        RequestParams params = new RequestParams();
        params.put("num",item.getNum());
        Log.d("result", "item.getNum() = " + item.getNum());
        Log.d("[Test]",item.getUser_name()+"");
        params.put("user_name",item.getUser_name());
        client.post(Constants.ResultURL,params,response);
        client.post(Constants.ResultURL5,params,response4);
        chatAdapter.clear();
        chatAdapter.notifyDataSetChanged();
        sellAdapter.clear();
        result_chat_list.setVisibility(View.VISIBLE);
        params.put("bno",item.getNum());
        client.post(Constants.ResultURL3,params,response2);
        params.put("user_code", user_code);
        client.post(Constants.ResultURL4,params,response3);
        if (item.getUser_name().equals(user_name)){
            result_chat_btn.setVisibility(View.GONE);
        }

        // 9월 2일
        response10 = new Response10(this);
//        user_code = pref.getInt("user_code",0);
        String URL10 = "http://192.168.0.69:8098/hongdangmu/int_tbl/listTF.do";
        RequestParams params10 = new RequestParams();
        params10.put("user_code", user_code);
        Log.d("item.getNum() = ", "item.getNum() = " + item.getNum());
        params10.put("num", item.getNum());
        client.post(URL10, params10, response10);
        response11 = new Response11(this);

        response12 = new Response12(this);
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_name", item.getUser_name());
        client.post("http://192.168.0.69:8098/hongdangmu/users/selectOne.do", requestParams, response12);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        setContentView(R.layout.activity_result);
        result_image_imageView = findViewById(R.id.result_image_imageView);
        result_icon_imageView = findViewById(R.id.result_icon_imageView);

        // 9월 2일
        result_icon_imageView_on = findViewById(R.id.result_icon_imageView_on);
        result_icon_imageView_off = findViewById(R.id.result_icon_imageView_off);

        result_icon_imageView_on.setOnClickListener(this);
        result_icon_imageView_off.setOnClickListener(this);

        result_name_tv = findViewById(R.id.result_name_tv);
        result_location_tv = findViewById(R.id.result_location_tv);
        result_subject_tv = findViewById(R.id.result_subject_tv);
        result_categoryTime_tv = findViewById(R.id.result_categoryTime_tv);
        result_content_tv = findViewById(R.id.result_content_tv);
        result_other_tv = findViewById(R.id.result_other_tv);
        result_image2_viewPager = findViewById(R.id.result_image2_viewPager);
        result_name_layout = findViewById(R.id.result_name_layout);
        result_manner_progress = findViewById(R.id.result_manner_progress);
        result_manner_tv = findViewById(R.id.result_manner_tv);
        result_chat_list = findViewById(R.id.result_chat_list);
        result_user_product_tv = findViewById(R.id.result_user_product_tv);
        result_chatNum_tv = findViewById(R.id.result_chatNum_tv);
        result_price_tv = findViewById(R.id.result_price_tv);
        result_chat_btn = findViewById(R.id.result_chat_btn);
        result_reply_tv = findViewById(R.id.result_reply_tv);
        result_goods_imageView = findViewById(R.id.result_goods_imageView);
        result_image2_recyclerView = findViewById(R.id.result_image2_recyclerView);
        result_showAllImage_tv = findViewById(R.id.result_showAllImage_tv);
        result_chatAllShow_tv = findViewById(R.id.result_chatAllShow_tv);

        item = (Main) getIntent().getSerializableExtra("item");

        result_image_viewPager = findViewById(R.id.result_image_viewPager);
        result_image_viewPager.setOffscreenPageLimit(3);

        PageViewAdapter pageViewAdapter = new PageViewAdapter(getSupportFragmentManager());

        Fragment imageView1Fragment = new ImageView1Fragment().newInstance(item.getImage0());
        pageViewAdapter.addItem(imageView1Fragment);
        Log.e("[이미지1]","" + item.getImage1());
        if (!item.getImage1().equals(Constants.ServerURL + "/hongdangmu/storage/null")){
            Fragment imageView2Fragment = new ImageView2Fragment().newInstance(item.getImage1());
            pageViewAdapter.addItem(imageView2Fragment);

        }
        Log.e("[이미지2]","" + item.getImage2());
        if (!item.getImage2().equals(Constants.ServerURL + "/hongdangmu/storage/null")){
            Fragment imageView3Fragment = new ImageView3Fragment().newInstance(item.getImage2());
            pageViewAdapter.addItem(imageView3Fragment);
        }

        result_image_viewPager.setAdapter(pageViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        result_chat_list.setLayoutManager(layoutManager);
        result_image2_recyclerView.setLayoutManager(layoutManager1);
        chatAdapter = new ChatAdapter(this,1);
        userAdapter = new UserAdapter(this, R.layout.list_users, list);
        sellAdapter = new SellShowAdapter(this,1);
        client = new AsyncHttpClient();
        response = new response(this);
        response2 = new response2(this);
        response3 = new UsersResponse(this, userAdapter);
        response4 = new response4(this);
        // 뷰페이저 테스트라 main에서 넘긴 list 갯수만큼 옆으로 넘어가짐
        ArrayList<Main> list = (ArrayList<Main>)getIntent().getSerializableExtra("list");
        if (item.getReply_count()>5){
            result_chatAllShow_tv.setVisibility(View.VISIBLE);
        }

        actionBar.setTitle(Html.fromHtml("<font color='#000000'>" + item.getBoard_subject() + "</font>"));
        //adapter = new ImageAdapter(this, list);

         //result_image_viewPager.setAdapter(adapter); // 해당상품 이미지뷰
        // result_image2_viewPager.setAdapter(adapter);    // 유저의 다른 상품들 테스트라 위뷰와 같은 뷰사용


        imageLoaderInit();

        result_name_layout.setOnClickListener(this);
        result_reply_tv.setOnClickListener(this);
        result_showAllImage_tv.setOnClickListener(this);
        result_chatAllShow_tv.setOnClickListener(this);
        result_chat_btn.setOnClickListener(this);
    }

    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
            imageLoader.init(configuration);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.result_name_layout){
            Intent intent = new Intent(this, Profile.class);
            intent.putExtra("users",profile);
            startActivity(intent);
        }else if (v.getId() == R.id.result_reply_tv){
            Intent intent = new Intent(this, ReplyActivity.class);
            Main item = (Main) getIntent().getSerializableExtra("item");
            intent.putExtra("item",item);
            intent.putExtra("users",users);
            startActivity(intent);
        }else if(v.getId() == R.id.result_showAllImage_tv){
            Intent intent = new Intent(this, ShowAllActivity.class);
            Main item = (Main) getIntent().getSerializableExtra("item");
            intent.putExtra("name",item.getUser_name());
            startActivity(intent);
        }else if (v.getId() == R.id.result_chatAllShow_tv){
            Intent intent = new Intent(this, ChatAllActivity.class);
            Main item = (Main) getIntent().getSerializableExtra("item");
            intent.putExtra("bno",item.getNum());
            startActivity(intent);
        }else if (v.getId() == R.id.result_chat_btn){
             // 9월 2일
            String URL92 = "http://192.168.0.69:8098/hongdangmu/buy_tbl/write.do";
            RequestParams params = new RequestParams();
            params.put("user_code", user_code);
            params.put("goods_num", item.getNum());
            client.post(URL92, params, response11);
            Intent intent = new Intent(this, MsgRoomActivity.class);
            Main item = (Main) getIntent().getSerializableExtra("item");
            intent.putExtra("item",item);
            startActivity(intent);
        }

        // 9월 2일
        switch (v.getId()) {
            case R.id.result_icon_imageView_off:
                String URL_ON = "http://192.168.0.69:8098/hongdangmu/int_tbl/write.do";
                RequestParams requestParams1 = new RequestParams();
                requestParams1.put("user_code", user_code);
                requestParams1.put("goods_num", item.getNum());
                client.post(URL_ON, requestParams1, response11);
                result_icon_imageView_off.setVisibility(View.GONE);
                result_icon_imageView_on.setVisibility(View.VISIBLE);
                break;
            case R.id.result_icon_imageView_on:
                String URL_OFF = "http://192.168.0.69:8098/hongdangmu/int_tbl/delete.do";
                RequestParams requestParams2 = new RequestParams();
                requestParams2.put("user_code", user_code);
                requestParams2.put("goods_num", item.getNum());
                client.post(URL_OFF, requestParams2, response11);
                result_icon_imageView_off.setVisibility(View.VISIBLE);
                result_icon_imageView_on.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            // history stack 내용 지우기 옵션 설정
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // 대상 Activity가 존재한다면 새로 만들지 말고, 기존 것을 사용하겠다는 설정, 첫 번째 화면만 가능
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else if (item.getItemId() == R.id.delete){
            showLoginDialog();
        }else if (item.getItemId() == R.id.modify){
            Intent intent = new Intent(this, ModifyActivity.class);
            Main items = (Main) getIntent().getSerializableExtra("item");
            intent.putExtra("item",items);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result,menu);
        if (user_name.equals(item.getUser_name())){
            menu.findItem(R.id.delete).setVisible(true);
            menu.findItem(R.id.modify).setVisible(true);
        }

        return true;
    }
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제");
        builder.setMessage("한번 지우면 복구는 불가능합니다.\n삭제 하시겠습니까?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = new RequestParams();
                num = getIntent().getIntExtra("num",0);
                params.put("num",num);
                client.post(Constants.ResultURL2,params,response);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class response extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
        ImageLoader imageLoader;


        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("불러오는중");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }

        public response(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            imageLoaderInit();
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                JSONArray item = json.getJSONArray("item");
                if (rt.equals("OK")){
                    if (item.length()>0) {
                        JSONObject temp = item.getJSONObject(0);
                        Main board1 = new Main();
                        imageLoader.displayImage(temp.getString("user_photo"),result_icon_imageView);
                        imageLoader.displayImage(temp.getString("image0"),result_goods_imageView);
                        result_name_tv.setText(temp.getString("user_name"));
                        result_subject_tv.setText(temp.getString("subject"));
                        result_location_tv.setText(temp.getString("area"));
                        result_categoryTime_tv.setText(temp.getString("category_code") + "/" + temp.getString("board_date"));
                        result_content_tv.setText(temp.getString("content"));
                        result_other_tv.setText("조회 " + temp.getString("readcount") + " * 관심 " + temp.getString("interest_count") + " * 채팅 " + temp.getString("reply_count")); //조회수 관심수 댓글수
                        result_manner_tv.setText("" + temp.getString("manner") + "º");
                        result_user_product_tv.setText(temp.getString("user_name") + "님의 판매상품");
                        result_chatNum_tv.setText("댓글 수 " + temp.getString("reply_count"));
                        result_manner_progress.setProgress(temp.getInt("manner"));
                        result_price_tv.setText(temp.getString("price") + "원");
                    }
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject jsonObject = item.getJSONObject(i);
                        Main board1 = new Main();
                        board1.setUser_name(jsonObject.getString("user_name"));
                        Log.d("[CHAT]", "user_name = " + jsonObject.getString("user_name"));
                        board1.setRe_chat(jsonObject.getString("reply"));
                        Log.d("[CHAT]", "reply = " + jsonObject.getString("reply"));
                        board1.setReg_date(jsonObject.getString("replyDate"));
                        Log.d("[CHAT]", "replyDate = " + jsonObject.getString("replyDate"));
                        chatAdapter.add(board1);
                    }
                }
                result_chat_list.setAdapter(chatAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
        private void imageLoaderInit() {
            imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
                imageLoader.init(configuration);
            }
        }
    }
    class response2 extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
        ImageLoader imageLoader;

        public response2(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            imageLoaderInit();
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                JSONArray item = json.getJSONArray("item");
                if (rt.equals("OK")){
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject jsonObject = item.getJSONObject(i);
                        Main board1 = new Main();
                        board1.setArea(jsonObject.getString("area"));
                        board1.setUser_photo(jsonObject.getString("user_photo"));
                        board1.setUser_name(jsonObject.getString("user_name"));
                        board1.setRe_chat(jsonObject.getString("reply"));
                        board1.setReg_date(jsonObject.getString("replyDate"));
                        board1.setRno(jsonObject.getInt("rno"));
                        board1.setNum(jsonObject.getInt("bno"));
                        chatAdapter.add(board1);
                    }
                }
                result_chat_list.setAdapter(chatAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
        private void imageLoaderInit() {
            imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
                imageLoader.init(configuration);
            }
        }
    }
    class response4 extends AsyncHttpResponseHandler {
        Activity activity;
        ImageLoader imageLoader;

        public response4(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            imageLoaderInit();
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                JSONArray item = json.getJSONArray("item");
                if (rt.equals("OK")){
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject jsonObject = item.getJSONObject(i);
                        Main board1 = new Main();
                        board1.setUser_name(jsonObject.getString("user_name"));
                        board1.setArea(jsonObject.getString("area"));
                        board1.setBoard_date(jsonObject.getString("board_date"));
                        board1.setCategory_code(jsonObject.getString("category_code"));
                        board1.setContent(jsonObject.getString("content"));
                        board1.setImage0(jsonObject.getString("image0"));
                        board1.setInterest_count(jsonObject.getInt("interest_count"));
                        board1.setNum(jsonObject.getInt("num"));
                        board1.setGoods_price(jsonObject.getInt("price"));
                        board1.setRead_count(jsonObject.getInt("readcount"));
                        board1.setReply_count(jsonObject.getInt("reply_count"));
                        board1.setBoard_subject(jsonObject.getString("subject"));
                        sellAdapter.add(board1);
                    }
                }
                result_image2_recyclerView.setAdapter(sellAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
        private void imageLoaderInit() {
            imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
                imageLoader.init(configuration);
            }
        }
    }

    // 9월 2일
    public class Response10 extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public Response10(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세여...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Toast.makeText(activity, "통신 3", Toast.LENGTH_SHORT).show();
            String contents = new String(responseBody);
            try {
                Log.d("[INFO]", "contents = " + contents);
                JSONObject json = new JSONObject(contents);
                int total = json.getInt("total");
                Log.d("[total]", "total = " + total);

                if (total == 0) {
                    result_icon_imageView_off.setVisibility(View.VISIBLE);
                    result_icon_imageView_on.setVisibility(View.GONE);
                } else {
                    result_icon_imageView_off.setVisibility(View.GONE);
                    result_icon_imageView_on.setVisibility(View.VISIBLE);
                }
                if (item.getUser_name().equals(user_name)){
                    result_icon_imageView_off.setVisibility(View.GONE);
                    result_icon_imageView_on.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패", Toast.LENGTH_SHORT).show();
        }
    }

    public class Response11 extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public Response11(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세여...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }


        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            try {
                JSONObject json = new JSONObject(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 = "+ statusCode, Toast.LENGTH_SHORT).show();
        }
    }



    public class Response12 extends AsyncHttpResponseHandler {
        Activity activity;
        UserAdapter adapter;
        ProgressDialog dialog;

        public Response12(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세여...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.d("[INFO]", "responseBody = " + responseBody);
            String content = new String(responseBody);
            Log.d("[INFO]", "content = " + content);
            try {
                Log.d("[INFO]", "통신성공 1");
                JSONObject json = new JSONObject(content);
                Log.d("[INFO]", "통신성공 2");
                JSONArray item = json.getJSONArray("item");
                Log.d("[INFO]", "item.length() = " + item.length());
                for (int i=0; i<item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);
                    int user_code = temp.getInt("user_code");
                    Log.d("[INFO]", "user_code = " + user_code);
                    String user_name = temp.getString("user_name");
                    Log.d("[INFO]", "user_name = " + user_name);
                    String user_photo = temp.getString("user_photo");
                    Log.d("[INFO]", "user_photo = " + user_photo);
                    String user_area = temp.getString("user_area");
                    Log.d("[INFO]", "user_area = " + user_area);
                    String user_tel = temp.getString("user_tel");
                    Log.d("[INFO]", "user_tel = " + user_tel);
                    String user_email1 = temp.getString("user_email1");
                    Log.d("[INFO]", "user_email1 = " + user_email1);
                    String user_email2 = temp.getString("user_email2");
                    Log.d("[INFO]", "user_email2 = " + user_email2);
                    int manner = temp.getInt("manner");
                    Log.d("[INFO]", "manner = " + manner);
                    int reply_percent = temp.getInt("reply_percent");
                    Log.d("[INFO]", "reply_percent = " + reply_percent);
                    int sales_count = temp.getInt("sales_count");
                    Log.d("[INFO]", "sales_count = " + sales_count);
                    int purchase_count = temp.getInt("purchase_count");
                    Log.d("[INFO]", "purchase_count = " + purchase_count);
                    int interest_count = temp.getInt("interest_count");
                    Log.d("[INFO]", "interest_count = " + interest_count);
                    int lat = temp.getInt("lat");
                    Log.d("[INFO]", "lat = " + lat);
                    int lng = temp.getInt("lng");
                    Log.d("[INFO]", "lng = " + lng);
                    String join_date = temp.getString("join_date");
                    Log.d("[INFO]", "join_date = " + join_date);

                    Main users = new Main(user_code, user_name, user_photo, user_area, manner, reply_percent, sales_count, purchase_count,
                            interest_count, join_date, user_tel, user_email1, user_email2, lat, lng);
                    profile = users;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패"+ statusCode, Toast.LENGTH_SHORT).show();
        }
    }


    private class PageViewAdapter extends FragmentStatePagerAdapter{
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public PageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return items.get(i);
        }
        public void addItem(Fragment item){
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}

