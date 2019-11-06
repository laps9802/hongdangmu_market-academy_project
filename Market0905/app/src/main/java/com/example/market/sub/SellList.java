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
import com.example.market.SubFragment.SaleFragment;
import com.example.market.SubFragment.SellCompleteFragment;
import com.example.market.SubFragment.SellHideFragment;
import com.example.market.model.Main;


public class SellList extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    TabLayout tabLayout_sellList;
    FragmentTransaction tran;
    FragmentManager fm;

    Bundle bundle;

    SaleFragment saleFragment;
    SellCompleteFragment sellCompleteFragment;
    SellHideFragment sellHideFragment;
    Main users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 액션바 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>판매 내역</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        setContentView(R.layout.activity_sell_list);

        saleFragment = new SaleFragment();
        sellCompleteFragment = new SellCompleteFragment();
        sellHideFragment = new SellHideFragment();

        tabLayout_sellList = findViewById(R.id.tabLayout_sellList);
        tabLayout_sellList.addTab(tabLayout_sellList.newTab().setText("판매중"));
        tabLayout_sellList.addTab(tabLayout_sellList.newTab().setText("거래완료"));
        tabLayout_sellList.addTab(tabLayout_sellList.newTab().setText("숨김"));
        tabLayout_sellList.addOnTabSelectedListener(this);

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
        switch (i) {
            case 0:
                saleFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_sellList, saleFragment);
                tran.commit();
                break;
            case 1:
                sellCompleteFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_sellList, sellCompleteFragment);
                tran.commit();
                break;
            case 2:
                sellHideFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_sellList, sellHideFragment);
                tran.commit();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d("[INFO]", "판매 리스트 탭 입장");
        switch (tab.getPosition()) {
            case 0:
                Toast.makeText(this, "판매중", Toast.LENGTH_LONG).show();
//                saleFragment.showItemList();
                setFragment(0);
                break;
            case 1:
                Toast.makeText(this, "거래완료", Toast.LENGTH_LONG).show();
//                sellCompleteFragment.showItemList();
                setFragment(1);
                break;
            case 2:
                Toast.makeText(this, "숨김", Toast.LENGTH_LONG).show();
                setFragment(2);
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
