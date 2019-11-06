package com.example.market.frag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.adapter.UserAdapter;
import com.example.market.constants.Constants;
import com.example.market.model.Main;
import com.example.market.response.UsersResponse;
import com.example.market.sub.Profile;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

public class SetActivity extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    View view;
    ArrayList<Main> list;
    UserAdapter adapter;
    UsersResponse response;
    AsyncHttpClient client;
    ListView listView;

    int user_code;

    @Override
    public void onResume() {
        super.onResume();
        getData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new UserAdapter(getActivity(), R.layout.list_users, list);
        response = new UsersResponse(getActivity(), adapter);
        client = new AsyncHttpClient();
        SharedPreferences pref = getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        user_code = pref.getInt("user_code",0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "생성?", Toast.LENGTH_LONG).show();
        view = inflater.inflate(R.layout.activity_set, container, false);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    private void getData() {
        RequestParams params = new RequestParams();
        params.put("user_code", user_code);
        client.post(Constants.SetActivityURL, params, response);
        adapter.clear();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Main item = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), Profile.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
