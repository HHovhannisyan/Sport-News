package com.example.sportNewsAPI.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {

    @SerializedName("articles")
    @Expose
    public List<Article> article;

    public List<Article> getArticle() {
        return article;
    }

    @SerializedName("URL")
    @Expose
    public List<String> URL;

    public List<String> getURL() {
        return URL;
    }

}
