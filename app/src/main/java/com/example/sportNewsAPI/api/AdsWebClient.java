package com.example.sportNewsAPI.api;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.monstertechno.adblocker.AdBlockerWebView;
import com.monstertechno.adblocker.util.AdBlocker;

public class AdsWebClient extends WebViewClient {
    public AdsWebClient() { }


    @SuppressWarnings("deprecation")
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        return AdBlockerWebView.blockAds(view, url) ? AdBlocker.createEmptyResource() :
                super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }


    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}


