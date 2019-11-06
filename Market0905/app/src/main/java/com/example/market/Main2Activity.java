package com.example.market;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.market.frag.ClearActivity;
import com.example.market.frag.DongnaeActivity;
import com.example.market.frag.PeopleActivity;
import com.example.market.frag.TradeActivity;
import com.example.market.frag.WriteActivity;

public class Main2Activity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction tran;
    TradeActivity tradeActivity;
    DongnaeActivity dongnaeActivity;
    PeopleActivity peopleActivity;
    ClearActivity clearActivity;
    TabLayout main2_tab_tabLayout;
    String keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();
        tradeActivity = new TradeActivity();
        dongnaeActivity = new DongnaeActivity();
        peopleActivity = new PeopleActivity();
        clearActivity = new ClearActivity();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>검색</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        tran.replace(R.id.main2_frame_frameLayout, clearActivity).commitAllowingStateLoss();
        main2_tab_tabLayout = findViewById(R.id.main2_tab_tabLayout);
        main2_tab_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                showTab(index);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                showTab(index);
            }
        });
    }

    private void showTab(int index) {
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();
        if (index==0){      // 중고거래
            tran.replace(R.id.main2_frame_frameLayout, TradeActivity.newInstance(keyword)).commitAllowingStateLoss();
        }else if (index==1){    // 사람
            tran.replace(R.id.main2_frame_frameLayout, PeopleActivity.newInstance(keyword)).commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        final SearchView searchView = (SearchView)menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("'지역명' 근처에서 검색");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                keyword = s;
                showTab(0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyword = null;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
