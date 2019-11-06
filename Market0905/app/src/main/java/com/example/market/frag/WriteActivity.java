package com.example.market.frag;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.constants.Constants;
import com.example.market.helper.FileUtils;
import com.example.market.helper.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;


public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    EditText write_subject_edit, write_price_edit, write_content_edit;
    TextView write_category_tv, write_area_tv, write_upload_tv;
    ImageView write_photo_imageView, write_select_imageView, write_select_imageView2, write_select_imageView3;
    LinearLayout write_imgae_layout;
    String filePath, filePath2, filePath3;

    AsyncHttpClient client;
    HttpResponse response;
    String user_name;
    int photoNum;
    double lat, lng;
    String area;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        user_name = pref.getString("user_name", null);
        lat = Double.parseDouble(pref.getString("lat", "0.0"));
        lng = Double.parseDouble(pref.getString("lng", "0.0"));
        area = pref.getString("area", "지역");
        write_area_tv.setText(area);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        write_subject_edit = findViewById(R.id.write_subject_edit);
        write_price_edit = findViewById(R.id.write_price_edit);
        write_content_edit = findViewById(R.id.write_content_edit);
        write_category_tv = findViewById(R.id.write_category_tv);
        write_area_tv = findViewById(R.id.write_area_tv);
        write_photo_imageView = findViewById(R.id.write_photo_imageView);
        write_upload_tv = findViewById(R.id.write_upload_tv);
        write_select_imageView = findViewById(R.id.write_select_imageView);
        write_select_imageView2 = findViewById(R.id.write_select_imageView2);
        write_select_imageView3 = findViewById(R.id.write_select_imageView3);
        write_imgae_layout = findViewById(R.id.write_image_layout);

        write_category_tv.setOnClickListener(this);
        write_photo_imageView.setOnClickListener(this);
        write_imgae_layout.setOnClickListener(this);
        write_select_imageView.setOnClickListener(this);
        write_select_imageView2.setOnClickListener(this);
        write_select_imageView3.setOnClickListener(this);

        client = new AsyncHttpClient();
        response = new HttpResponse(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>글쓰기</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("index",1);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.finish) {
            String subject = write_subject_edit.getText().toString().trim();
            String category_code = write_category_tv.getText().toString().trim();
            String area = write_area_tv.getText().toString().trim();
            String content = write_content_edit.getText().toString().trim();
            String str_price = write_price_edit.getText().toString().trim();
            int price = 0;
            if (!str_price.equals("")) {
                price = Integer.parseInt(str_price);
            }
            String err = null;
            if (err == null && !RegexHelper.getInstance().isValue(subject)) {
                err = "제목을 입력하세요.";
            }
            if (err == null && category_code.equals("카테고리")) {
                err = "카테고리를 선택하세요.";
            }
            if (err == null && !RegexHelper.getInstance().isValue(content)) {
                err = "내용을 입력하세요.";
            }
            if (err == null && filePath == null) {
                err = "사진을 첨부하세요.";
            }
            if (err != null) {
                Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
                return false;
            }

            RequestParams params = new RequestParams();
            params.setForceMultipartEntityContentType(true);
            params.put("lat", lat);
            params.put("lng", lng);
            params.put("user_name", user_name);
            params.put("subject", subject);
            params.put("category_code", category_code);
            params.put("area", area);
            params.put("content", content);
            params.put("price", price);
            try {
                params.put("img", new File(filePath));
                if (filePath2 != null) {
                    params.put("img2", new File(filePath2));
                } else {
                    params.put("img2", new File(""));
                }

                if (filePath3 != null) {
                    params.put("img3", new File(filePath3));
                } else {
                    params.put("img3", new File(""));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            client.post(Constants.WriteURL, params, response);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.write_category_tv) {
            showCategoryDialog();
        } else if (v.getId() == R.id.write_photo_imageView) {
            Intent intent = null;
            if (Build.VERSION.SDK_INT >= 19) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(intent, 100);
        } else if (v.getId() == R.id.write_select_imageView) {
            write_select_imageView.setVisibility(View.GONE);
            filePath = null;
            photoNum--;
            write_upload_tv.setText(photoNum + "/3");
        } else if (v.getId() == R.id.write_select_imageView2) {
            write_select_imageView2.setVisibility(View.GONE);
            filePath2 = null;
            photoNum--;
            write_upload_tv.setText(photoNum + "/3");
        } else if (v.getId() == R.id.write_select_imageView3) {
            write_select_imageView3.setVisibility(View.GONE);
            filePath3 = null;
            photoNum--;
            write_upload_tv.setText(photoNum + "/3");
        }
    }

    private void showCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = {"디지털/가전", "가구/인테리어", "유아동/유아도서", "생활/가공식품", "여성의류", "여성잡화", "뷰티/미용", "남성패션/잡화", "스포츠/레저", "게임/취미", "도서/티켓/음반", "반려동물용품"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(WriteActivity.this, items[which] + "가 눌렸습니다.", Toast.LENGTH_SHORT).show();
                if (items[which] == "디지털/가전") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "가구/인테리어") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "유아동/유아도서") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "생활/가공식품") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "여성의류") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "여성잡화") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "뷰티/미용") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "남성패션/잡화") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "스포츠/레저") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "게임/취미") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "도서/티켓/음반") {
                    write_category_tv.setText(items[which]);
                } else if (items[which] == "반려동물용품") {
                    write_category_tv.setText(items[which]);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (filePath == null) {
                    filePath = FileUtils.getPath(this, data.getData());
                    photoNum++;
                } else if (filePath2 == null) {
                    filePath2 = FileUtils.getPath(this, data.getData());
                    photoNum++;
                } else if (filePath3 == null) {
                    filePath3 = FileUtils.getPath(this, data.getData());
                    photoNum++;
                }

                Log.d("[INFO]", "filePath = " + filePath);
                if (filePath != null) {
                    write_imgae_layout.setVisibility(View.VISIBLE);
                    if (photoNum == 1) {
                        write_select_imageView.setVisibility(View.VISIBLE);
                        write_select_imageView.setImageURI(Uri.parse(filePath));
                    } else if (photoNum == 2) {
                        write_select_imageView.setVisibility(View.VISIBLE);
                        write_select_imageView2.setVisibility(View.VISIBLE);
                        write_select_imageView.setImageURI(Uri.parse(filePath));
                        write_select_imageView2.setImageURI(Uri.parse(filePath2));
                    } else if (photoNum == 3) {
                        write_select_imageView.setVisibility(View.VISIBLE);
                        write_select_imageView2.setVisibility(View.VISIBLE);
                        write_select_imageView3.setVisibility(View.VISIBLE);
                        write_select_imageView.setImageURI(Uri.parse(filePath));
                        Log.e("[에러]","" +filePath2 );
                        write_select_imageView2.setImageURI(Uri.parse(filePath2));
                        write_select_imageView3.setImageURI(Uri.parse(filePath3));
                    }
                    write_upload_tv.setText(photoNum + "/3");
                } else {
                    write_upload_tv.setText("0/3");
                }
            }
        }
    }

    class HttpResponse extends AsyncHttpResponseHandler {
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
                if (rt.equals("OK")) {
                    Intent intent = new Intent();
                    intent.putExtra("index",1);
                    setResult(RESULT_OK, intent);
                    Toast.makeText(activity, "저장성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(activity, "저장실패", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "연결 실패" + statusCode, Toast.LENGTH_SHORT).show();
        }
    }

}
