package com.example.market;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.constants.Constants;
import com.example.market.frag.WriteActivity;
import com.example.market.helper.FileUtils;
import com.example.market.helper.RegexHelper;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener {
    EditText modify_subject_edit,modify_price_edit,modify_content_edit;
    TextView modify_category_tv,modify_area_tv,modify_upload_tv;
    ImageView modify_photo_imageView,modify_select_imageView,modify_select_imageView2,modify_select_imageView3;
    LinearLayout modify_image_layout;
    String filePath,filePath2,filePath3;

    AsyncHttpClient client;
    HttpResponse response;
    boolean ch = false; // boardView 한번만 response에서 하도록
    int num;
    Main items;
    String user_name;
    int photoNum;
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        user_name = pref.getString("user_name",null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>글수정</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        modify_subject_edit = findViewById(R.id.modify_subject_edit);
        modify_price_edit = findViewById(R.id.modify_price_edit);
        modify_content_edit = findViewById(R.id.modify_content_edit);
        modify_category_tv = findViewById(R.id.modify_category_tv);
        modify_area_tv = findViewById(R.id.modify_area_tv);
        modify_upload_tv = findViewById(R.id.modify_upload_tv);
        modify_photo_imageView = findViewById(R.id.modify_photo_imageView);
        modify_select_imageView = findViewById(R.id.modify_select_imageView);
        modify_select_imageView2 = findViewById(R.id.modify_select_imageView2);
        modify_select_imageView3 = findViewById(R.id.modify_select_imageView3);
        modify_image_layout = findViewById(R.id.modify_image_layout);

        client = new AsyncHttpClient();
        response = new HttpResponse(this);
        items = (Main) getIntent().getSerializableExtra("item");
        RequestParams params = new RequestParams();
        params.put("num",items.getNum());
        Log.d("[Test]",items.getUser_name()+"");
        params.put("user_name",items.getUser_name());
        client.post(Constants.ModifyURL,params,response);
        ch = true;



        modify_category_tv.setOnClickListener(this);
        modify_photo_imageView.setOnClickListener(this);
        modify_image_layout.setOnClickListener(this);
        modify_select_imageView.setOnClickListener(this);
        modify_select_imageView2.setOnClickListener(this);
        modify_select_imageView3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.modify_category_tv){
            showCategoryDialog();
        }else if (v.getId() == R.id.modify_photo_imageView){
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
        }else if (v.getId() == R.id.modify_select_imageView){
            modify_select_imageView.setVisibility(View.GONE);
            filePath = null;
            photoNum--;
            modify_upload_tv.setText(photoNum+ "/3");
        }else if (v.getId() == R.id.modify_select_imageView2){
            modify_select_imageView2.setVisibility(View.GONE);
            filePath2 = null;
            photoNum--;
            modify_upload_tv.setText(photoNum+ "/3");
        }else if (v.getId() == R.id.modify_select_imageView3){
            modify_select_imageView3.setVisibility(View.GONE);
            filePath3 = null;
            photoNum--;
            modify_upload_tv.setText(photoNum+ "/3");
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            if (resultCode==RESULT_OK) {
                if (filePath == null) {
                    filePath = FileUtils.getPath(this, data.getData());
                    photoNum++;
                    modify_select_imageView.setVisibility(View.VISIBLE);
                } else if (filePath2 == null) {
                    filePath2 = FileUtils.getPath(this, data.getData());
                    photoNum++;
                    modify_select_imageView2.setVisibility(View.VISIBLE);
                } else if (filePath3 == null) {
                    filePath3 = FileUtils.getPath(this, data.getData());
                    photoNum++;
                    modify_select_imageView3.setVisibility(View.VISIBLE);
                }
                Log.d("[INFO]", "filePath = " + filePath);
                if (filePath != null) {
                    if (photoNum == 1) {
                        modify_select_imageView.setVisibility(View.VISIBLE);
                        modify_select_imageView.setImageURI(Uri.parse(filePath));
                    } else if (photoNum == 2) {
                        modify_select_imageView.setVisibility(View.VISIBLE);
                        modify_select_imageView2.setVisibility(View.VISIBLE);
                        modify_select_imageView.setImageURI(Uri.parse(filePath));
                        modify_select_imageView2.setImageURI(Uri.parse(filePath2));
                    } else if (photoNum == 3) {
                        modify_select_imageView.setVisibility(View.VISIBLE);
                        modify_select_imageView2.setVisibility(View.VISIBLE);
                        modify_select_imageView3.setVisibility(View.VISIBLE);
                        modify_select_imageView.setImageURI(Uri.parse(filePath));
                        Log.e("[에러]", "" + filePath2);
                        modify_select_imageView2.setImageURI(Uri.parse(filePath2));
                        modify_select_imageView3.setImageURI(Uri.parse(filePath3));
                    }
                    modify_upload_tv.setText(photoNum + "/3");
                } else {
                    modify_upload_tv.setText("0/3");
                }
            }
        }
    }
    private void showCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = {"디지털/가전","가구/인테리어","유아동/유아도서","생활/가공식품","여성의류","여성잡화","뷰티/미용","남성패션/잡화","스포츠/레저","게임/취미","도서/티켓/음반","반려동물용품"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ModifyActivity.this,items[which] + "가 눌렸습니다.",Toast.LENGTH_SHORT).show();
                if (items[which]=="디지털/가전"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="가구/인테리어"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="유아동/유아도서"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="생활/가공식품"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="여성의류"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="여성잡화"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="뷰티/미용"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="남성패션/잡화"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="스포츠/레저"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="게임/취미"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="도서/티켓/음반"){
                    modify_category_tv.setText(items[which]);
                }else if (items[which]=="반려동물용품"){
                    modify_category_tv.setText(items[which]);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }else if (item.getItemId() == R.id.finish){
            int lat = 0;
            int lng = 0;
            String subject = modify_subject_edit.getText().toString().trim();
            String category_code = modify_category_tv.getText().toString().trim();
            String area = modify_area_tv.getText().toString().trim();
            String content = modify_content_edit.getText().toString().trim();
            String str_price = modify_price_edit.getText().toString().trim();
            int price = 0;
            if (!str_price.equals("")){
                price = Integer.parseInt(str_price);
            }
            String err = null;
            if (err == null &&  !RegexHelper.getInstance().isValue(subject)){
                err = "제목을 입력하세요.";
            }
            if (err == null && category_code.equals("카테고리") ){
                err = "카테고리를 선택하세요.";
            }
            if (err == null &&  !RegexHelper.getInstance().isValue(content)){
                err = "내용을 입력하세요.";
            }
            if (err == null &&  filePath==null){
                err = "사진을 첨부하세요.";
            }
            if (err!=null){
                Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
            }
            if (err==null) {
                RequestParams params = new RequestParams();
                params.setForceMultipartEntityContentType(true);
                params.put("lat",lat);
                params.put("lng",lng);
                params.put("user_name",user_name);
                params.put("subject",subject);
                params.put("category_code",category_code);
                params.put("area",area);
                params.put("content",content);
                params.put("price",price);
                params.put("num",items.getNum());
                try {
                    params.put("img",new File(filePath));
                    if (filePath2!=null){
                        Log.e("[전송2]","" +filePath2 );
                        params.put("img2",new File(filePath2));
                    }else {
                        params.put("img2",new File(""));
                    }
                    if (filePath3!=null){
                        Log.e("[전송3]","" +filePath3 );
                        params.put("img3",new File(filePath3));
                    }else {
                        params.put("img3",new File(""));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                client.post(Constants.ModifyURL2,params,response);
                finish();
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write,menu);
        return true;
    }
    class HttpResponse extends AsyncHttpResponseHandler {
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
        public HttpResponse(Activity activity) {
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
                    if (item.length()>0){
                        if (ch=true) {
                            JSONObject temp = item.getJSONObject(0);
                            modify_subject_edit.setText(temp.getString("subject"));
                            modify_area_tv.setText(temp.getString("area"));
                            modify_category_tv.setText(temp.getString("category_code"));
                            modify_content_edit.setText(temp.getString("content"));
                            modify_price_edit.setText(temp.getString("price"));
                        }else {
                            Intent intent = new Intent();
                        }
                    }
                }

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
}
