package com.example.market;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.market.constants.Constants.reverseGeocoding_URL;

public class LocationSettingActivity extends AppCompatActivity implements View.OnClickListener {
    //전역, 상수
    private final String TAG = "LocSetActi-";

    //화면
    TextView locSet_text_gpsPlaceName;

    Button locSet_btn_ok, locSet_btn_cancel;

    //일반
    LocationManager locationManager;
    Location mainLocation;

    SharedPreferences pref;

    String adminArea;

    //통신
    AsyncHttpClient client;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>위치 설정</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        locSet_text_gpsPlaceName = findViewById(R.id.locSet_text_gpsPlaceName);

        locSet_btn_ok = findViewById(R.id.locSet_btn_ok);
        locSet_btn_cancel = findViewById(R.id.locSet_btn_cancel);

        locSet_btn_ok.setOnClickListener(this);
        locSet_btn_cancel.setOnClickListener(this);

        client = new AsyncHttpClient();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.locSet_btn_ok:
                if(mainLocation != null && adminArea != null && !adminArea.equals("")) {
                    pref.edit()
                            .putString("lat", "" + mainLocation.getLatitude())
                            .putString("lng", "" + mainLocation.getLongitude())
                            .putString("area", adminArea)
                            .commit();
                }
                finish();
                break;
            case R.id.locSet_btn_cancel:
                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkActivation();
    }

    private void checkActivation() {
        if(locationManager == null) locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.e(TAG + "GPS활성화검사", "GPS Provider enabled : " + isGPSEnabled);
        Log.e(TAG + "GPS활성화검사", "Network Provider enabled : " + isNetworkEnabled);

//        if(isNetworkEnabled){
//
//        }
        if (isGPSEnabled) {
            updateLocation();
        } else {
            //TODO: GPS 설정 화면이동
            //돌아오면 (start, resume) GPS 기능 활성화 검사 후 updateLocation()
            alertIsGPSSetting();
        }

    }

    private void alertIsGPSSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치정보서비스(GPS) 설정")
                .setMessage("위치정보서비스(GPS)를 켜면 위치 인증에 사용되는 위치를 자동으로 불러올 수 있습니다")
                .setCancelable(false)
                .setPositiveButton("설정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                })
                .show();

    }


    private void updateLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                Log.e(TAG + "위치업데이트", "ACCESS_FINE_LOCATION 없음");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                Log.e(TAG + "위치업데이트", "ACCESS_COARSE_LOCATION 없음");
            Log.e(TAG + "위치업데이트", "권한없음");
            permissionCheck();
            return;
        }

        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            Log.e(TAG + "위치업데이트", "시작");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocatingListener());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class LocatingListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                Log.e(TAG + "위치업데이트", "실패");
                return;
            }
            mainLocation = location;
            Log.e(TAG + "위치업데이트", location.getLatitude() + "  " + location.getLongitude());

            try {
                locationManager.removeUpdates(this);
                convertToPlaceName(location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void convertToPlaceName(Location location) {
        if (location == null) {
            Log.e(TAG + "지명변환", "location 널값");
            return;
        }
        client.addHeader("X-NCP-APIGW-API-KEY-ID", "9jccfk8410");
        client.addHeader("X-NCP-APIGW-API-KEY", "KGgaao4JSir6haZWxAc4InHI097FsfJKMzjlgsyM");
        RequestParams params = new RequestParams();
        params.put("coords", location.getLongitude() + "," + location.getLatitude());

        client.get(reverseGeocoding_URL, params, new AsyncHttpResponseHandler() {           //네이버 - get만 받음
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(TAG + "통신성공", "*******************************************************statusCode = " + statusCode);
                String response = new String(responseBody);

                try {
                    JSONObject acceptClass = new JSONObject(response);
                    if (!(acceptClass.getJSONArray("results") instanceof JSONArray) || acceptClass.getJSONArray("results").length() <= 0) {
                        Log.e(TAG + "응답", "응답 아이템 없음");
                        return;
                    }
                    JSONArray items = acceptClass.getJSONArray("results");
                    JSONObject geocode = items.getJSONObject(0);
                    JSONObject region = geocode.getJSONObject("region");
                    JSONObject area1 = region.getJSONObject("area1");
                    JSONObject area2 = region.getJSONObject("area2");
                    JSONObject area3 = region.getJSONObject("area3");

                    String sido = area1.getString("name");
                    String gu = area2.getString("name");
                    String adm_area = area3.getString("name");

                    locSet_text_gpsPlaceName.setText(sido + " " + gu + " " + adm_area);

                    adminArea = adm_area;
                    Log.e(TAG + "행정동얻음", "adm_area = " + adm_area);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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











    private boolean permissionCheck() {
        int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int cam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
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
                    updateLocation();
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
