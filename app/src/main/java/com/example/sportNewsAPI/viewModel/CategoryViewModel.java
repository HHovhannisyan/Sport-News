package com.example.sportNewsAPI.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CategoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Category");
    }

    public LiveData<String> getCategoryTxt() {
        return mText;
    }
}