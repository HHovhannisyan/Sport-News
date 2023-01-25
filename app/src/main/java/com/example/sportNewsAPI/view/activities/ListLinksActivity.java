package com.example.sportNewsAPI.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.databinding.ActivityListLinksBinding;

import java.util.ArrayList;
import java.util.Objects;


public class ListLinksActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    ActivityListLinksBinding activityListLinksBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityListLinksBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_links);
        activityListLinksBinding.setLifecycleOwner(this);

        setSupportActionBar(activityListLinksBinding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        ArrayList<String> list = b.getStringArrayList("linkList");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);


        activityListLinksBinding.listView.setAdapter(adapter);


        activityListLinksBinding.listView.setOnItemClickListener((parent, view, position, id) -> {

            Log.d(TAG, "team: " + parent.getItemAtPosition(position));

            Intent i1 = new Intent(getApplicationContext(), WebActivity.class);
            String url = (String) parent.getItemAtPosition(position);
            Log.d(TAG, "url: " + url);

            i1.putExtra("listURL", url);
            startActivity(i1);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityListLinksBinding = null;
    }
}