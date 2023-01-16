package com.example.sportNewsAPI.view.activities;

import android.content.Intent;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.sportNewsAPI.GlideApp;
import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.databinding.ActivityNewsDetailBinding;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;

import android.widget.Toast;

import java.util.Objects;


public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private String mUrl;
    ActivityNewsDetailBinding activityNewsDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityNewsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        setSupportActionBar(activityNewsDetailBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityNewsDetailBinding.collapsingToolbar.setTitle("");
        activityNewsDetailBinding.appbar.addOnOffsetChangedListener(this);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        String mImg = intent.getStringExtra("img");
        String mTitle = intent.getStringExtra("title");
        String mSource = intent.getStringExtra("source");
        String description = intent.getStringExtra("description");

        activityNewsDetailBinding.bttnReadMore.setOnClickListener(v -> {
            Intent intentReadMore = new Intent(NewsDetailActivity.this, WebActivity.class);
            intentReadMore.putExtra("url", mUrl);
            startActivity(intentReadMore);
        });


        RequestOptions requestOptions = new RequestOptions();

        GlideApp.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerInside()
                .placeholder(R.drawable.top_shadow)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.img_error)
                .into(activityNewsDetailBinding.backdrop);

        activityNewsDetailBinding.txtSource.setText(mSource);
        activityNewsDetailBinding.title.setText(mTitle);
        activityNewsDetailBinding.descriptionTxt.setText(description);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        MenuItem bookmarkItem = menu.findItem(R.id.action_bookmark);
        searchItem.setVisible(false);
        bookmarkItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mUrl);
                startActivity(Intent.createChooser(intent, "Shared via Sport News App"));
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong with sharing", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityNewsDetailBinding=null;
        GlideApp.get(this).clearMemory();
    }
}
