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
import com.example.market.SubFragment.InterestFragment;
import com.example.market.model.Main;

public class InterestList extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        View.OnClickListener {
    TabLayout tabLayout_interestList;
    FragmentTransaction tran;
    FragmentManager fm;

    Bundle bundle;

    InterestFragment interestFragment;

    Main users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        // 액션바 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>관심 목록</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        interestFragment = new InterestFragment();

        tabLayout_interestList = findViewById(R.id.tabLayout_InterestList);
        tabLayout_interestList.addTab(tabLayout_interestList.newTab().setText("중고거래"));
        tabLayout_interestList.addOnTabSelectedListener(this);

        bundle = new Bundle();
        users = (Main) getIntent().getSerializableExtra("users");
        Log.e("[INFO]", "user_code = " + users.getUser_code());

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
        bundle.putInt("user_code", users.getUser_code());
        Log.e("[NFO]SD", "user_code = " + users.getUser_code());
        switch (i) {
            case 0:
                interestFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_interestList, interestFragment);
                tran.commit();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                Toast.makeText(this, "관심 목록1", Toast.LENGTH_LONG).show();
                setFragment(0);
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
