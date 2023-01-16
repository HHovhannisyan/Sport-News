package com.example.sportNewsAPI.api;

import com.example.sportNewsAPI.model.News;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    String KEY = "YOUR KEY";
    String BASE_URL = "https://newsapi.org/v2/";
    String sources =
            "espn.com," +
                    "cbssports.com," +
                    "bleacherreport.com," +
                    "skysports.com," +
                    "eurosport.com," +
                    "101greatgoals.com";

    String language = "en";

    @GET("everything")
    Single<News> getNews(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("domains") String domains,
            @Query("sortBy") String sortBy,
            @Query("pageSize") Integer pageSize,
            @Query("apiKey") String apiKey
    );
}
