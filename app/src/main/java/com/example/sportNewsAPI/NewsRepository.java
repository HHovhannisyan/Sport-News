package com.example.sportNewsAPI;

import androidx.lifecycle.MutableLiveData;

import com.example.sportNewsAPI.api.ApiClient;
import com.example.sportNewsAPI.api.ApiInterface;
import com.example.sportNewsAPI.model.News;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {
    private final ApiInterface apiInterface;

    public NewsRepository() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    public MutableLiveData<News> getNews(String keyword, String language, String domains, String sortBy, int pageSize, String apiKey) {
        MutableLiveData<News> newsMutableLiveData = new MutableLiveData<>();

        Single<News> newsObservable = apiInterface.getNews(keyword, language, domains, sortBy, pageSize, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        newsObservable.subscribe(new DisposableSingleObserver<News>() {

            @Override
            public void onSuccess(@NonNull News news) {
                    newsMutableLiveData.setValue(news);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                    newsMutableLiveData.setValue(null);
            }
        });
        return newsMutableLiveData;
    }
}
