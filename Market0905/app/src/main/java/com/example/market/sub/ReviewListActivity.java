package com.example.market.sub;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.SubFragment.AllReviewFragment;
import com.example.market.SubFragment.BuyerReviewFragment;
import com.example.market.SubFragment.SellerReviewFragment;

public class ReviewListActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    TabLayout tabLayout_reviewList;
    FragmentTransaction tran;
    FragmentManager fm;

    Bundle bundle;

    AllReviewFragment allReviewFragment;
    SellerReviewFragment sellerReviewFragment;
    BuyerReviewFragment buyerReviewFragment;

    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        // 액션바 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>거래 후기 상세</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,255,255)));

        allReviewFragment = new AllReviewFragment();
        sellerReviewFragment = new SellerReviewFragment();
        buyerReviewFragment = new BuyerReviewFragment();

        tabLayout_reviewList = findViewById(R.id.tabLayout_reviewList);
        tabLayout_reviewList.addTab(tabLayout_reviewList.newTab().setText("전체 후기"));
        tabLayout_reviewList.addTab(tabLayout_reviewList.newTab().setText("구매자 후기"));
        tabLayout_reviewList.addTab(tabLayout_reviewList.newTab().setText("판매자 후기"));
        tabLayout_reviewList.addOnTabSelectedListener(this);

        bundle = new Bundle();
        user_name = getIntent().getStringExtra("user_name");
        
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
        bundle.putString("user_name", user_name);
        switch (i) {
            case 0:
                allReviewFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_reviewList, allReviewFragment);
                tran.commit();
                break;
            case 1:
                buyerReviewFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_reviewList, buyerReviewFragment);
                tran.commit();
                break;
            case 2:
                sellerReviewFragment.setArguments(bundle);
                tran.replace(R.id.frameLayout_reviewList, sellerReviewFragment);
                tran.commit();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                Toast.makeText(this, "전체 후기", Toast.LENGTH_LONG).show();
                setFragment(0);
                break;
            case 1:
                Toast.makeText(this, "구매자 후기", Toast.LENGTH_LONG).show();
                setFragment(1);
                break;
            case 2:
                Toast.makeText(this, "판매자 후기", Toast.LENGTH_LONG).show();
                setFragment(2);
                break;

        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    @Override
    public void onClick(View v) {}
}
