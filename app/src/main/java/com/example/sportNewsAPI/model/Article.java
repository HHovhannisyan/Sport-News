package com.example.sportNewsAPI.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

    @SerializedName("source")
    @Expose
    public Source source;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("urlToImage")
    @Expose
    public String urlToImage;

    @SerializedName("publishedAt")
    @Expose
    public String publishedAt;


    public Source getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }
}
