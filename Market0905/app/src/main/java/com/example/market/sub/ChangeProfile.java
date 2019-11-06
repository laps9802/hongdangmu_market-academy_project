package com.example.market.sub;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.MainActivity;
import com.example.market.R;
import com.example.market.constants.Constants;
import com.example.market.helper.FileUtils;
import com.example.market.helper.PhotoHelper;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class ChangeProfile extends AppCompatActivity implements View.OnClickListener {
    TextView textView_back;
    Button button_complete;
    ImageView imageView_photo;
    EditText editText_nickName;
    Bitmap photo;
    ImageLoader imageLoader;
    ModifyResponse response;
    AsyncHttpClient client;
    Main item;
    String user_photo;
    String user_name;
    String filePath;
    String filePath2;
    int modify_code;    // 갤러리에서 선택된 경우 1



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        // 액션바 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>프로필 설정</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        imageView_photo = findViewById(R.id.imageView_photo);
        editText_nickName = findViewById(R.id.editText_nickName);
        imageLoader = ImageLoader.getInstance();

        item = (Main) getIntent().getSerializableExtra("users");
        user_photo = "";
        user_name = "";
        filePath = "";
        filePath2 = item.getUser_photo();
        Log.d("[filePath]", "filePath2 = " + filePath2);
        modify_code = 0;


//        byte[] arr_photo = getIntent().getByteArrayExtra("photo");
//        photo = BitmapFactory.decodeByteArray(arr_photo, 0, arr_photo.length);

//        imageView_photo.setImageBitmap(photo);
//        String nickName = getIntent().getStringExtra("nickName");

        client = new AsyncHttpClient();
        response = new ModifyResponse();
        Log.d("[INFO]", "item.getUser_photo() = " + item.getUser_photo());
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        pref.edit().putString("user_photo", item.getUser_photo()).commit();
        imageLoader.displayImage(item.getUser_photo(), imageView_photo);
        editText_nickName.setText(item.getUser_name());

        imageView_photo.setOnClickListener(this);

        permissionCheck();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        user_name = editText_nickName.getText().toString().trim();
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case R.id.action_btn_complete:
                intent = new Intent(this, MainActivity.class);
                Log.d("[modify_code]", "modify_code = " + modify_code);
                if (modify_code == 1) {
                    paramData(filePath);
                } else if (modify_code == 0) {
                    int index = filePath2.lastIndexOf("_");
                    user_photo = filePath2.substring(index + 1);
                    paramData(filePath2);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imageView_photo:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                }
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri photoUri = data.getData();
                filePath = FileUtils.getPath(this, photoUri);
                Log.d("[INFO]", "filePath = " + filePath);
                imageView_photo.setImageBitmap(null);
                if (photo != null) {
                    photo.recycle();
                    photo = null;
                }
                photo = PhotoHelper.getInstance().getThumb(this, filePath);
                int index = filePath.lastIndexOf("/");
                user_photo = filePath.substring(index + 1);
                Log.d("[ChangeProfile]", "index = " + index);
//                user_photo = filePath.substring(filePath.lastIndexOf("/")+1);
                Log.d("[ChangeProfile]", "user_photo = " + user_photo);
                modify_code = 1;
                imageView_photo.setImageBitmap(photo);
            }
        }
    }

    private void paramData(String filePath) {
        RequestParams params = new RequestParams();
        Log.d("[INFO]", "테스트 filePath" + filePath);

        params.put("img", filePath);
        try {
            params.put("img", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("modify", "img = " + new File(filePath));
        params.put("user_name", item.getUser_name());
        Log.d("modify", "user_name = " + item.getUser_name());
        params.put("muser_name", user_name);
        Log.d("modify", "muser_name = " + user_name);
        params.put("user_photo", user_photo);
        Log.d("user_photo", "user_photo = " + user_photo);
        params.put("user_area", item.getArea());
        params.put("user_tel", item.getUser_tel());
        params.put("user_email1", item.getUser_email1());
        params.put("user_email2", item.getUser_email2());
        params.put("user_code", item.getUser_code());
        params.put("lat", item.getLat());
        params.put("lng", item.getLng());
        params.put("manner", item.getManner());
        params.put("reply_percent", item.getReply_percent());
        params.put("sales_count", item.getSales_count());
        params.put("purchase_count", item.getPurchase_count());
        params.put("interest_count", item.getInterest_count());
        client.post(Constants.ChangeProfileURL , params, response);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (photo != null) {
            photo.recycle();
            photo = null;
        }
    }

    public class ModifyResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }
}
