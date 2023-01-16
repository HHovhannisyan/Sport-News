package com.example.sportNewsAPI;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public class MyAppGlideModule  extends AppGlideModule {
    public void applyOptions(@NonNull Context context, GlideBuilder builder) {
        builder.setDefaultRequestOptions(
                new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
    }
}
