package com.example.sportNewsAPI.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.ViewModel;

import com.example.sportNewsAPI.NewsRepository;
import com.example.sportNewsAPI.model.News;

import static com.example.sportNewsAPI.api.ApiInterface.KEY;
import static com.example.sportNewsAPI.api.ApiInterface.language;
import static com.example.sportNewsAPI.api.ApiInterface.sources;


public class NewsViewModel extends ViewModel {
    private final LiveData<News> newLiveData;
    private  final MutableLiveData<String>headlinesTxt;
    public NewsViewModel() {
        NewsRepository newsRepository = new NewsRepository();
        this.newLiveData = newsRepository.getNews("", language, sources, "publishedAt", 100, KEY);
        headlinesTxt=new MutableLiveData<>();
        headlinesTxt.setValue("Latest Headlines");
    }


    public LiveData<News> getArticleLiveData() {
        return newLiveData;
    }

    public LiveData<String> getHeadlinesTxt(){
        return  headlinesTxt;
    }

}
