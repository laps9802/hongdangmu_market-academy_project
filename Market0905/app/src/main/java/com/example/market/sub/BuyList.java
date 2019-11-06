package com.example.market.sub;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.SubFragment.ReviewCompleteFragment;
import com.example.market.SubFragment.ReviewWaitFragment;
import com.example.market.model.Main;

public class BuyList extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        View.OnClickListener {
    TabLayout tabLayout_buyList;
    FragmentTransaction tran;
    FragmentManager fm;
    Bundle bundle;

    ReviewWaitFragment reviewWaitFragment;
    ReviewCompleteFragment reviewCompleteFragment;
    Main users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 액션바 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>구매 내역</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        setContentView(R.layout.activity_buy_list);

        reviewWaitFragment = new ReviewWaitFragment();
        reviewCompleteFragment = new ReviewCompleteFragment();

        tabLayout_buyList = findViewById(R.id.tabLayout_buyList);
        tabLayout_buyList.addTab(tabLayout_buyList.newTab().setText("후기 대기중 " ));
        tabLayout_buyList.addTab(tabLayout_buyList.newTab().setText("후기 완료 "));
        tabLayout_buyList.addOnTabSelectedListener(this);
        bundle = new Bundle();
        users = (Main) getIntent().getSerializableExtra("users");

        setFragment(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFragment(int i) {
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        bundle.putString("user_name", users.getUser_name());
        Log.e("[name]", "user_name = " + users.getUser_name());
        switch (i) {
            case 0:
                reviewWaitFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_buyList, reviewWaitFragment);
                tran.commit();
                break;
            case 1:
                reviewCompleteFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_buyList, reviewCompleteFragment);
                tran.commit();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                Toast.makeText(this, "후기 대기중", Toast.LENGTH_LONG).show();
                setFragment(0);
                break;
            case 1:
                Toast.makeText(this, "후기 완료", Toast.LENGTH_LONG).show();
                setFragment(1);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    @Override
    public void onClick(View v) {
    }
}
