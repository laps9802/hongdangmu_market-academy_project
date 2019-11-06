package com.example.market;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.market.helper.FileUtils;
import com.example.market.helper.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText login_name_edit,login_tel_edit;
    Button login_join_btn,login_login_btn;
    ImageView login_profile_imageView;
    AsyncHttpClient client;
    String URL = "http://192.168.0.69:8098/hongdangmu/users/write.do";
    String URL2 = "http://192.168.0.69:8098/hongdangmu/users/login.do";
    String filePath;
    HttpResponse response;
    HttpResponse2 response2;
    boolean login = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("로그인 및 회원가입");
        login_name_edit = findViewById(R.id.login_name_edit);
        login_tel_edit = findViewById(R.id.login_tel_edit);
        login_join_btn = findViewById(R.id.login_join_btn);
        login_login_btn = findViewById(R.id.login_login_btn);
        login_profile_imageView = findViewById(R.id.login_profile_imageView);

        client = new AsyncHttpClient();
        response = new HttpResponse(this);
        response2 = new HttpResponse2(this);

        login_join_btn.setOnClickListener(this);
        login_login_btn.setOnClickListener(this);
        login_profile_imageView.setOnClickListener(this);
        permissionCheck();
    }
    private void permissionCheck() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this ,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String user_name = login_name_edit.getText().toString().trim();
        String user_tel = login_tel_edit.getText().toString().trim();
        if (v.getId() == R.id.login_join_btn){
            String err = null;
            if (err == null &&  !RegexHelper.getInstance().isValue(user_name)){
                err = "닉네임을 입력하세요.";
            }
            if (err == null &&  !RegexHelper.getInstance().isValue(user_tel)){
                err = "번호를 입력하세요.";
            }
            if (err == null &&  filePath==null){
                err = "프로필을 추가하세요.";
            }
            if (err!=null){
                Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
                return;
            }
            if (err==null){
                RequestParams params = new RequestParams();
                try {
                    params.put("img",new File(filePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                params.put("user_name",user_name);
                params.put("user_are","서초구");
                params.put("user_tel",user_tel);
                params.put("user_email1","test");
                params.put("user_email2","test.com");
                params.put("manner",60);
                params.put("reply_percent",60);
                params.put("sales_count",60);
                params.put("purchase_count",60);
                params.put("interest_count",20);
                client.post(URL,params,response);
            }
        }else if (v.getId() == R.id.login_login_btn){
            String err = null;
            if (err == null &&  !RegexHelper.getInstance().isValue(user_name)){
                err = "닉네임을 입력하세요.";
            }
            if (err == null &&  !RegexHelper.getInstance().isValue(user_tel)){
                err = "번호를 입력하세요.";
            }
            if (err!=null){
                Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
                return;
            }
            if (err==null) {
                RequestParams params = new RequestParams();
                params.put("user_name", user_name);
                params.put("user_tel", user_tel);
                client.post(URL2, params, response2);
                Toast.makeText(this,"회원가입성공",Toast.LENGTH_SHORT).show();
            }
        }else if (v.getId() == R.id.login_profile_imageView){
            Intent intent = null;
            if (Build.VERSION.SDK_INT>=19){
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            }else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
            startActivityForResult(intent,100);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            if (resultCode==RESULT_OK){
                filePath = FileUtils.getPath(this,data.getData());
                Log.d("[INFO]","filePath = " + filePath);
                if (filePath!=null){
                    login_profile_imageView.setImageURI(Uri.parse(filePath));
                }
            }
        }
    }
    private class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
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
        public HttpResponse(Activity activity) {
            this.activity = activity;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                if (rt.equals("OK")){
                    Toast.makeText(activity,"회원가입성공",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(activity,"회원가입실패",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
    }
    private class HttpResponse2 extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
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
        public HttpResponse2(Activity activity) {
            this.activity = activity;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (rt.equals("OK")){
                    JSONArray item = json.getJSONArray("item");
                    for (int i=0; i<item.length();i++){
                        JSONObject jsonObject = item.getJSONObject(i);
                        editor.putString("user_name",jsonObject.getString("user_name"));
                        editor.putString("user_tel",jsonObject.getString("user_tel"));
                        editor.putString("join_date",jsonObject.getString("join_date"));
                        editor.putString("user_email1",jsonObject.getString("user_email1"));
                        editor.putInt("sales_count",jsonObject.getInt("sales_count"));
                        editor.putInt("purchase_count",jsonObject.getInt("purchase_count"));
                        editor.putString("user_email2",jsonObject.getString("user_email2"));
                        editor.putInt("manner",jsonObject.getInt("manner"));
                        editor.putInt("user_code",jsonObject.getInt("user_code"));
                        editor.putString("user_area",jsonObject.getString("user_area"));
                        editor.putInt("interest_count",jsonObject.getInt("interest_count"));
                        editor.putInt("reply_percent",jsonObject.getInt("reply_percent"));
                        editor.putString("user_photo",jsonObject.getString("user_photo"));
                        editor.putString("user_name",jsonObject.getString("user_name"));
                        editor.putInt("lng",jsonObject.getInt("lng"));
                        editor.putInt("lat",jsonObject.getInt("lat"));
                    }
                    Toast.makeText(activity,"로그인성공",Toast.LENGTH_SHORT).show();
                    editor.putBoolean("login", true);
                    editor.commit();
                    finish();
                } else {
                    Toast.makeText(activity,"로그인실패",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity,"로그인연결 실패" + statusCode,Toast.LENGTH_SHORT).show();
        }
    }
}
