package com.example.sportNewsAPI.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.api.AdsWebClient;
import com.monstertechno.adblocker.AdBlockerWebView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class AdsWebActivity extends AppCompatActivity {
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_web);
         webView=findViewById(R.id.webView_ads);

        Intent i = getIntent();
        Bundle b=i.getExtras();
        String data=b.getString("adsLink");

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        new AdBlockerWebView.init(this).initializeWebView(webView);
        webView.setWebViewClient(new AdsWebClient());
        webView.loadUrl(data);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}