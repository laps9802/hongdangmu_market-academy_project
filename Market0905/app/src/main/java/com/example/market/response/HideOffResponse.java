package com.example.market.response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HideOffResponse extends AsyncHttpResponseHandler {
    Activity activity;
    ProgressDialog dialog;

    public HideOffResponse(Activity activity) {
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
        try {
            JSONObject json = new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("[INFO]", "content = " + content);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Toast.makeText(activity, "통신실패 = "+ statusCode, Toast.LENGTH_SHORT).show();
    }
}
