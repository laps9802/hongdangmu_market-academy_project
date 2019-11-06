    package com.example.market;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.example.market.constants.Constants;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AuthActivity extends AppCompatActivity {
    private final String TAG = "AuthActi-";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>닉네임 작성</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(permissionCheck()) {
            AccessToken accessToken = AccountKit.getCurrentAccessToken();
            if (accessToken == null
                    || pref.getString("phoneNumber", null) == null
                    || pref.getString("nickname", null) == null
                    || pref.getString("lat", null) == null
                    || pref.getString("lng", null) == null
                    || pref.getString("area", null) == null
              ) {
//                pref.edit().putInt("memberType", 1).commit();
                phoneLogin();
            }
            else{         // 모든 프리퍼런스 다 있을 때만 넘어가게
                pref.edit().putInt("memberType", 2).commit();
                Toast.makeText(AuthActivity.this, ""+pref.getInt("memberType", -1), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }

    }



    public void phoneLogin() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN

        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());

        startActivityForResult(intent, Constants.SMS_AUTH_CODE);
    }



    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SMS_AUTH_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
//                showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>(){
                    @Override
                    public void onSuccess(final Account account) {
                        Log.e(TAG+"폰번호얻기", "***********************************************통신성공");
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        if(phoneNumber == null) {
                            Toast.makeText(AuthActivity.this, "일시적인 통신오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG+"폰번호얻기", "폰번호 널값");
                            return;
                        }
                        Log.e(TAG+"폰번호얻기", "폰번호 획득");
                        pref.edit().putString("phoneNumber", phoneNumber.toString()).commit();
                        Toast.makeText(AuthActivity.this, pref.getString("phoneNumber", ""), Toast.LENGTH_SHORT).show();

                        new AsyncHttpClient().post(Constants.AuthPhoneNumberURL, new RequestParams("phoneNumber", pref.getString("phoneNumber", "")), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Log.e(TAG+"회원여부조회", "***********************************************통신성공");
                                String strJson = new String(responseBody);

                                try {
                                    JSONObject json = new JSONObject(strJson);
                                    int searchedCount = json.getInt("searchedCount");
                                    if(searchedCount == 0) {
                                        pref.edit().putInt("memberType", 1).commit();
                                        Toast.makeText(AuthActivity.this, ""+pref.getInt("memberType", -1), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AuthActivity.this, NameActivity.class));
                                        finish();
                                    }
                                    else if(searchedCount == 1) {
                                        pref.edit().putInt("memberType", 3).commit();
                                        Toast.makeText(AuthActivity.this, ""+pref.getInt("memberType", -1), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                        finish();
                                    }
                                    else Log.e(TAG+"회원여부조회", "회원 중복");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.e(TAG+"회원여부조회", "***********************************************통신실패");
                                Toast.makeText(AuthActivity.this, "일시적인 통신오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e(TAG+"폰번호얻기", "***********************************************통신실패");
                        Log.e(TAG+"폰번호얻기", accountKitError.getUserFacingMessage());
                        Log.e(TAG+"폰번호얻기", accountKitError.toString());
                        Toast.makeText(AuthActivity.this, "일시적인 통신오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Constants.PERMISSION_REQUEST_CODE);
            return false;
        }

        Log.e(TAG+"퍼미션", "퍼미션 이미 획득");
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG+"퍼미션", "퍼미션 요청 결과");
        if(requestCode == Constants.PERMISSION_REQUEST_CODE){
            //모든 퍼미션 획독 완료 시 - 화면 초기화
            if(grantResults.length > 0) {
                boolean isGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) isGranted = false;
                }
                if (isGranted) {
                    Log.e(TAG+"퍼미션", "퍼미션 없음 but 획득");
                    AccessToken accessToken = AccountKit.getCurrentAccessToken();
                    if (accessToken == null) {
                        phoneLogin();
                    }
                    else{
                        startActivity(new Intent(this, NameActivity.class));
                        finish();
                    }
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



