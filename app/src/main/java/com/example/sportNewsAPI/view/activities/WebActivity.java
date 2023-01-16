package com.example.sportNewsAPI.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.sportNewsAPI.api.AdsWebClient;
import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.api.ApiClient;
import com.example.sportNewsAPI.databinding.ActivityWebBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monstertechno.adblocker.AdBlockerWebView;

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends AppCompatActivity {

    public static final String PREFERENCES = "PREFERENCES_NAME";
    public static final String WEB_LINKS = "links";
    public static final String WEB_TITLE = "title";

    private EditText findBox;
    String webUrl;
    String currentUrl;

    ActivityWebBinding activityWebBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWebBinding = DataBindingUtil.setContentView(this, R.layout.activity_web);

        setSupportActionBar(activityWebBinding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        activityWebBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        activityWebBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());


        activityWebBinding.swipeToRefresh.setOnRefreshListener(() -> {
            activityWebBinding.webView.reload();
            activityWebBinding.swipeToRefresh.setRefreshing(false);
        });


        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("bookmarkURL")) {

            currentUrl = getIntent().getStringExtra("bookmarkURL");
            initWebView(currentUrl, getApplicationContext());

        } else if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("listURL")) {

            currentUrl = getIntent().getStringExtra("listURL");
            initWebView("https://" + currentUrl, getApplicationContext());

        } else if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("adsLink")) {
            currentUrl = getIntent().getStringExtra("adsLink");
            initWebView("https://www.google.com/", getApplicationContext());
        } else {
            currentUrl = getIntent().getStringExtra("url");
            initWebView(currentUrl, getApplicationContext());
        }

        if (savedInstanceState != null)
            activityWebBinding.webView.restoreState(savedInstanceState);
        else {
            initWebView(activityWebBinding.webView.getUrl(), getApplicationContext());
        }

        activityWebBinding.setLifecycleOwner(this);
    }


    private void initWebView(String currentUrl, Context context) {

        activityWebBinding.webView.setWebViewClient(new AdsWebClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                invalidateOptionsMenu();
            }
        });


        new AdBlockerWebView.init(this).initializeWebView(activityWebBinding.webView);
        activityWebBinding.webView.setWebChromeClient(new ChromeClient());

        activityWebBinding.webView.getSettings().setLoadsImagesAutomatically(true);
        activityWebBinding.webView.getSettings().setJavaScriptEnabled(true);
        activityWebBinding.webView.getSettings().setDomStorageEnabled(true);
        activityWebBinding.webView.getSettings().setSupportZoom(true);
        activityWebBinding.webView.getSettings().setBuiltInZoomControls(true);
        activityWebBinding.webView.getSettings().setDisplayZoomControls(false);
        activityWebBinding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        activityWebBinding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        activityWebBinding.webView.getSettings().setAllowFileAccess(true);
        activityWebBinding.webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        activityWebBinding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        if (!ApiClient.isConnected(context)) { // loading offline
            activityWebBinding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        activityWebBinding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        activityWebBinding.webView.loadUrl(currentUrl);
        requestPermissions();
    }


    @Override
    public void onBackPressed() {
        if (activityWebBinding.webView.canGoBack()) {
            activityWebBinding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.MODIFY_AUDIO_SETTINGS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        //  Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.INTERNET
                }, 0);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String links = sharedPreferences.getString(WEB_LINKS, null);

        if (links != null) {

            Gson gson = new Gson();
            ArrayList<String> linkList = gson.fromJson(links, new TypeToken<ArrayList<String>>() {
            }.getType());

            if (linkList.contains(activityWebBinding.webView.getUrl())) {
                menu.getItem(2).setIcon(R.drawable.ic_bookmark_black_24dp);
            } else {
                menu.getItem(2).setIcon(R.drawable.ic_bookmark_border_black_24dp);
            }

        } else {
            menu.getItem(2).setIcon(R.drawable.ic_bookmark_border_black_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void bookmark() {
        String message;

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String jsonLink = sharedPreferences.getString(WEB_LINKS, null);
        String jsonTitle = sharedPreferences.getString(WEB_TITLE, null);

        if (jsonLink != null && jsonTitle != null) {

            Gson gson = new Gson();
            ArrayList<String> linkList = gson.fromJson(jsonLink, new TypeToken<ArrayList<String>>() {
            }.getType());

            ArrayList<String> titleList = gson.fromJson(jsonTitle, new TypeToken<ArrayList<String>>() {
            }.getType());


            if (linkList.contains(activityWebBinding.webView.getUrl())) {

                linkList.remove(activityWebBinding.webView.getUrl());
                titleList.remove(activityWebBinding.webView.getTitle().trim());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(WEB_LINKS, new Gson().toJson(linkList));
                editor.putString(WEB_TITLE, new Gson().toJson(titleList));
                editor.apply();

                message = "Bookmark Removed";

            } else {
                linkList.add(activityWebBinding.webView.getUrl());
                titleList.add(activityWebBinding.webView.getTitle().trim());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(WEB_LINKS, new Gson().toJson(linkList));
                editor.putString(WEB_TITLE, new Gson().toJson(titleList));
                editor.apply();

                message = "Bookmarked";
            }
        } else {

            ArrayList<String> linkList = new ArrayList<>();
            ArrayList<String> titleList = new ArrayList<>();
            linkList.add(activityWebBinding.webView.getUrl());
            titleList.add(activityWebBinding.webView.getTitle());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(WEB_LINKS, new Gson().toJson(linkList));
            editor.putString(WEB_TITLE, new Gson().toJson(titleList));
            editor.apply();

            message = "Bookmarked";
        }

        Snackbar snackbar = Snackbar.make(activityWebBinding.mainContent, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.share) {
            try {
                webUrl = activityWebBinding.webView.getUrl();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, webUrl);
                startActivity(Intent.createChooser(intent, "Shared via Sport News App"));
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong with sharing", Toast.LENGTH_SHORT).show();
            }
        }


        if (id == R.id.action_bookmark) {
            bookmark();
            invalidateOptionsMenu();
        }


        if (id == R.id.search) {
            search();
        }

        return super.onOptionsItemSelected(item);
    }


    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        private static final int FULL_SCREEN_SETTING = View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE;

        ChromeClient() { }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        }


        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }


        @Override
        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow()
                    .getDecorView())
                    .addView(this.mCustomView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            getWindow().getDecorView().setSystemUiVisibility(FULL_SCREEN_SETTING);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            this.mCustomView.setOnSystemUiVisibilityChangeListener(visibility -> updateControls());
        }


        void updateControls() {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.mCustomView.getLayoutParams();
            params.bottomMargin = 0;
            params.topMargin = 0;
            params.leftMargin = 0;
            params.rightMargin = 0;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            this.mCustomView.setLayoutParams(params);
            getWindow().getDecorView().setSystemUiVisibility(FULL_SCREEN_SETTING);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        activityWebBinding.webView.onPause();
        activityWebBinding.webView.pauseTimers();
    }


    @Override
    protected void onResume() {
        super.onResume();
        activityWebBinding.webView.onResume();
        activityWebBinding.webView.resumeTimers();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        activityWebBinding.webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        activityWebBinding.webView.restoreState(savedInstanceState);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ViewParent parent = activityWebBinding.webView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(activityWebBinding.webView);
        }

        activityWebBinding.webView.stopLoading();
        activityWebBinding.webView.getSettings().setJavaScriptEnabled(false);
        activityWebBinding.webView.clearHistory();
        activityWebBinding.webView.destroyDrawingCache();
        activityWebBinding.webView.removeAllViews();
        activityWebBinding.webView.destroy();
        activityWebBinding = null;
    }


    public void search() {
        Button nextButton = new Button(this);
        nextButton.setBackgroundResource(R.drawable.next);
        nextButton.setOnClickListener(v -> {
            activityWebBinding.webView.findAllAsync(findBox.getText().toString());
            activityWebBinding.webView.findNext(true);
        });

        activityWebBinding.layoutId.addView(nextButton);
        Button closeButton = new Button(this);
        closeButton.setBackgroundResource(R.drawable.close);
        closeButton.setOnClickListener(v -> activityWebBinding.layoutId.removeAllViews());
        activityWebBinding.layoutId.addView(closeButton);

        findBox = new EditText(this);
        findBox.setMinEms(30);
        findBox.setSingleLine(true);
        findBox.setHint("Search");

        findBox.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && ((keyCode == KeyEvent.KEYCODE_ENTER))) {
                activityWebBinding.webView.findAllAsync(findBox.getText().toString());
                activityWebBinding.webView.findNext(true);
            }
            return false;
        });

        activityWebBinding.layoutId.addView(findBox);
    }
}

