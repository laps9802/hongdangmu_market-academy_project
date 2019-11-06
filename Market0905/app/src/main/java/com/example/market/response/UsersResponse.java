package com.example.market.response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.market.adapter.UserAdapter;
import com.example.market.model.Main;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UsersResponse extends AsyncHttpResponseHandler {
    Activity activity;
    UserAdapter adapter;
    ProgressDialog dialog;

    public UsersResponse(Activity activity, UserAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
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
                adapter.add(users);
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
