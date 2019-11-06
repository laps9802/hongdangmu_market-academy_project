package com.example.market;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.market.constants.Constants;
import com.example.market.frag.CategoryActivity;
import com.example.market.frag.ChatActivity;
import com.example.market.frag.LocationFragment;
import com.example.market.frag.SetActivity;
import com.example.market.frag.StartActivity;
import com.example.market.frag.TradeActivity;
import com.example.market.frag.WriteActivity;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity  {
    private final String TAG = "MainActi-";
    int currentTabIndex;

    FragmentManager fm;
    FragmentTransaction tran;
    LocationFragment locationFragment;
    StartActivity startActivity;
    ChatActivity chatActivity;
    CategoryActivity categoryActivity;
    WriteActivity writeActivity;
    SetActivity setActivity;
    Menu mMenu;
    BottomNavigationView main_navigation_view;
    int index;
    static String ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8,ch9,ch10,ch11,ch12;  // 카테고리 저장한것을 파일에서 꺼내서 저장용

    AsyncHttpClient client;

    @Override
    protected void onResume() {
        super.onResume();
        if (index==1){
            main_navigation_view.setSelectedItemId(R.id.navigation_menu1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        pref.edit().putString("user_name", pref.getString("nickname", "")).commit();
        Log.e("[pref]", "nickname = " + pref.getString("nickname", ""));
        Log.e("[pref]", "user_name = " + pref.getString("user_name", ""));
//        Toast.makeText(this,
//                "\nphoneNumber: " + pref.getString("phoneNumber", "")
//                        + "\nnickname: " + pref.getString("nickname", "")
//                        + "\nlat: " + pref.getString("lat", "")
//                        + "\nlng: " + pref.getString("lng", "")
//                        + "\narea: " + pref.getString("area", ""), Toast.LENGTH_LONG).show();

        //프리퍼런스: 카테고리, 폰번호, 닉네임, 위경도, 지역명
        //최초 회원: 정적 변수(폰 번호, 닉네임, 위경도, 지역명)로 DB 저장 - 프리퍼런스 저장
        //인증된 기존 회원: 프리퍼런스 조회
        //재인증한 기존 회원: 폰번호로 DB 조회 - 프리퍼런스 저장

        client = new AsyncHttpClient();
        int memberType = pref.getInt("memberType", -1);
        if (memberType == 1) {              //최초 회원
            RequestParams params = new RequestParams();
            params.put("phoneNumber", pref.getString("phoneNumber", ""));
            params.put("nickname", pref.getString("nickname", ""));
            params.put("lat", pref.getString("lat", ""));
            params.put("lng", pref.getString("lng", ""));
            params.put("area", pref.getString("area", ""));
            client.post(Constants.MainInsertUserURL, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.e(TAG + "최초저장", "*************************************통신 성공");
                    String strJson = new String(responseBody);
                    Log.e(TAG + "최초저장", "strJson = " + strJson);

                    try {
                        JSONObject json = new JSONObject(strJson);
                        Log.e(TAG + "최초저장", "su = " + json.getInt("su"));
                        Log.e(TAG + "최초저장", "user_code = " + json.getInt("user_code"));

                        if (json.getInt("su") == 1) {
                            Toast.makeText(MainActivity.this, "최초저장 성공", Toast.LENGTH_SHORT).show();
                            Log.e(TAG + "최초저장", "저장 성공");

                            if (json.getInt("user_code") == -1) {
                                Log.e(TAG + "최초저장", "유저코드 얻기 실패");
                            } else {
                                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                                pref.edit().putInt("user_code", json.getInt("user_code")).commit();
                                Log.e(TAG + "최초저장", "유저코드 얻기 성공 = " + json.getInt("user_code"));
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "최초저장 실패", Toast.LENGTH_SHORT).show();
                            Log.e(TAG + "최초저장", "저장 실패");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(MainActivity.this, "최초저장 통신 실패", Toast.LENGTH_SHORT).show();
                    Log.e(TAG + "최초저장", "*************************************통신 실패");
                }
            });
        } else if (memberType == 2) {         //인증된 회원
            ch1 = pref.getString("ch1", "");
            ch2 = pref.getString("ch2", "");
            ch3 = pref.getString("ch3", "");
            ch4 = pref.getString("ch4", "");
            ch5 = pref.getString("ch5", "");
            ch6 = pref.getString("ch6", "");
            ch7 = pref.getString("ch7", "");
            ch8 = pref.getString("ch8", "");
            ch9 = pref.getString("ch9", "");
            ch10 = pref.getString("ch10", "");
            ch11 = pref.getString("ch11", "");
            ch12 = pref.getString("ch12", "");
        } else if (memberType == 3) {         //재인증한 회원
            client.post(Constants.MainSelectExistingUserURL, new RequestParams("phoneNumber", pref.getString("phoneNumber", "")), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.e(TAG + "기존회원조회", "*************************************통신 성공");
                    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    pref.edit()
                            .putString("phoneNumber", "")
                            .putString("nickname", "")
                            .putString("lat", "")
                            .putString("lng", "")
                            .putString("area", "")
                            .commit();
                    String strJson = new String(responseBody);

                    try {
                        JSONObject json = new JSONObject(strJson);
                        String rt = json.getString("rt");
                        if (rt.equals("FAIL")) {
                            Log.e(TAG + "기존회원조회", "rt = FAIL");
                            return;
                        }
                        int user_code = json.getInt("user_code");
                        String phoneNumber = json.getString("phoneNumber");
                        String nickname = json.getString("nickname");
                        String lat = json.getString("lat");
                        String lng = json.getString("lng");
                        String area = json.getString("area");

                        SharedPreferences pref1 = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref1.edit();
                        editor
                                .putInt("user_code", user_code)
                                .putString("phoneNumber", phoneNumber)
                                .putString("nickname", nickname)
                                .putString("user_name", nickname)
                                .putString("lat", lat)
                                .putString("lng", lng)
                                .putString("area", area)
                                .commit();

                        Log.e(TAG + "기존회원조회", "*************************************" + pref1.getString("user_name", "no"));
                        Toast.makeText(MainActivity.this,
                                "\nphoneNumber: " + pref.getString("phoneNumber", "")
                                        + "\nuser_name: " + pref.getString("user_name", "")
                                        + "\nlat: " + pref.getString("lat", "")
                                        + "\nlng: " + pref.getString("lng", "")
                                        + "\narea: " + pref.getString("area", ""), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e(TAG + "기존회원조회", "*************************************통신 실패");

                }
            });
        } else {
            Log.e(TAG + "기존회원조회", "*************************************멤버 타입 에러");
        }

        SharedPreferences pref2 = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        pref2.edit().putString("user_name", pref2.getString("nickname", "")).commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 255, 255)));
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>물품 목록</font>"));

        locationFragment = new LocationFragment();
        startActivity = new StartActivity();
        chatActivity = new ChatActivity();
        categoryActivity = new CategoryActivity();
        writeActivity = new WriteActivity();
        setActivity = new SetActivity();

        if (permissionCheck()) {
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.replace(R.id.main_frame_frameLayout, startActivity).commitAllowingStateLoss();
        }

        main_navigation_view = findViewById(R.id.main_navigation_view);
        main_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fm = getSupportFragmentManager();
                tran = fm.beginTransaction();
                // 액션바 설정
                ActionBar actionBar = getSupportActionBar();
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 255, 255)));
                if (menuItem.getItemId() == R.id.navigation_menu1) { // 메인
                    actionBar.show();
                    mMenu.findItem(R.id.hot).setVisible(true);
                    mMenu.findItem(R.id.search).setVisible(true);
                    mMenu.findItem(R.id.category).setVisible(true);
                    actionBar.setTitle(Html.fromHtml("<font color='#000000'>물품 목록</font>"));
                    tran.replace(R.id.main_frame_frameLayout, startActivity).commitAllowingStateLoss();
                } else if (menuItem.getItemId() == R.id.navigation_menu2) {   // 위치
                    actionBar.hide();
                    tran.replace(R.id.main_frame_frameLayout, locationFragment).commitAllowingStateLoss();
                } else if (menuItem.getItemId() == R.id.navigation_menu3) {   // 글쓰기
                    Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                    startActivityForResult(intent,100);
                } else if (menuItem.getItemId() == R.id.navigation_menu4) {   // 채팅
                    actionBar.show();
                    mMenu.findItem(R.id.hot).setVisible(false);
                    mMenu.findItem(R.id.news).setVisible(false);
                    mMenu.findItem(R.id.search).setVisible(false);
                    mMenu.findItem(R.id.category).setVisible(false);
                    actionBar.setTitle(Html.fromHtml("<font color='#000000'>채팅</font>"));
                    tran.replace(R.id.main_frame_frameLayout, chatActivity).commitAllowingStateLoss();
                } else if (menuItem.getItemId() == R.id.navigation_menu5) {   // 설정
                    actionBar.show();
                    mMenu.findItem(R.id.hot).setVisible(false);
                    mMenu.findItem(R.id.news).setVisible(false);
                    mMenu.findItem(R.id.search).setVisible(false);
                    mMenu.findItem(R.id.category).setVisible(false);
                    actionBar.setTitle(Html.fromHtml("<font color='#000000'>나의 당근</font>"));
                    tran.replace(R.id.main_frame_frameLayout, setActivity).commitAllowingStateLoss();
                }
                return true;
            }
        });
    }

    // 타이틀바에 메뉴 추가 : 리턴값 true : 메뉴 표시, false : 메뉴 표시 안됨
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        Toast.makeText(this, "currentTabIndex = " + currentTabIndex, Toast.LENGTH_SHORT).show();

        if(currentTabIndex == 0) {
            MenuItem hot = mMenu.findItem(R.id.hot);
            MenuItem news = mMenu.findItem(R.id.news);
            Log.e(TAG+"menu_location", "1");

            if (item.getItemId() == R.id.search) {
                Intent intent = new Intent(this, Main2Activity.class);
                startActivityForResult(intent, 102);
                return true;
            } else if (item.getItemId() == R.id.category) {
                Intent intent = new Intent(this, ChoiceActivity.class);
                intent.putExtra("ch1", true);
                startActivityForResult(intent, 100);
                return true;
            } else if (item.getItemId() == R.id.hot) {
                hot.setVisible(false);
                news.setVisible(true);
                tran.replace(R.id.main_frame_frameLayout, StartActivity.newInstanceHot(Constants.MainActivityurl2, "1")).commitAllowingStateLoss();
                return true;
            } else if (item.getItemId() == R.id.news) {
                hot.setVisible(true);
                news.setVisible(false);
                tran.replace(R.id.main_frame_frameLayout, StartActivity.newInstanceNews()).commitAllowingStateLoss();
                return true;
            }
        }
        else if(currentTabIndex == 1){
            if(item.getItemId() == R.id.location_menuItem_search){
                Log.e(TAG+"menu_location", "2");
                Intent intent = new Intent(this, LocationSearchActivity.class);
                startActivity(intent);
                return true;
            }else if(item.getItemId() == R.id.location_menuItem_category){
                Log.e(TAG+"menu_location", "3");
                Intent intent = new Intent(this, ChoiceActivity.class);
                intent.putExtra("ch1", true);
                startActivityForResult(intent, 110);
                return true;
            }
        }

        return  false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode==100){
                index = data.getIntExtra("index",0);
            }else if (requestCode == 101){

            }else if (requestCode == 102){

            }
        }
    }



    private boolean permissionCheck() {
        int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        int cam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (cam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            Log.e(TAG+"퍼미션", "퍼미션 없음 & 요청");
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 10);
            return false;
        }

        Log.e(TAG+"퍼미션", "퍼미션 이미 획득");
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG+"퍼미션", "퍼미션 요청 결과");
        if(requestCode == 10){
            //모든 퍼미션 획독 완료 시 - 화면 초기화
            if(grantResults.length > 0) {
                boolean isGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) isGranted = false;
                }
                if (isGranted) {
                    Log.e(TAG+"퍼미션", "퍼미션 없음 but 획득");
                    FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                    tran.replace(R.id.main_frame_frameLayout, startActivity).commitAllowingStateLoss();
                }
                else {
                    Log.e(TAG+"퍼미션", "퍼미션 요청 but 퍼미션 부족");
                    permissionCheck();

//                    Toast.makeText(this, "권한을 획득하지 못했습니다. 잠시 후 종료됩니다.", Toast.LENGTH_SHORT).show();
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    finish();
                }
            }
        }

    }



}
