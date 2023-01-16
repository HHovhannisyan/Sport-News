package com.example.sportNewsAPI.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportNewsAPI.GlideApp;
import com.example.sportNewsAPI.databinding.FragmentHomeBinding;
import com.example.sportNewsAPI.viewModel.NewsViewModel;
import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.api.ApiClient;
import com.example.sportNewsAPI.view.activities.NewsDetailActivity;
import com.example.sportNewsAPI.view.adapters.Adapter;
import com.example.sportNewsAPI.model.Article;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements LifecycleOwner {

    private final List<Article> articleList = new ArrayList<>();
    private Adapter adapter;
    public Context mContext;
    NewsViewModel newsViewModel;
    NetworkInfo wifiCheck;
    ConnectivityManager connectionManager;
    FragmentHomeBinding fragmentHomeBinding;
    AlertDialog.Builder alertDialogBuilder;

    public HomeFragment() {
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        connectionManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        fragmentHomeBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        getNewsArticles();

        fragmentHomeBinding.swipeRefreshLayout.setOnRefreshListener(this::getNewsArticles);

        Log.d("MainActivity", "MainActivity");

        newsViewModel.getHeadlinesTxt().observe(getViewLifecycleOwner(), s -> fragmentHomeBinding.topHeadlines.setText(s));
        fragmentHomeBinding.setLifecycleOwner(this);

        return fragmentHomeBinding.getRoot();
    }


    private void getNewsArticles() {

        fragmentHomeBinding.swipeRefreshLayout.setRefreshing(true);
        newsViewModel.getArticleLiveData().observe(getViewLifecycleOwner(), articleResponse -> {

            if (!ApiClient.isConnected(mContext) && articleResponse != null) {

                fragmentHomeBinding.errorLayoutId.errorImg.setVisibility(View.INVISIBLE);
                fragmentHomeBinding.topHeadlines.setVisibility(View.VISIBLE);
                List<Article> articles = articleResponse.getArticle();
                articleList.addAll(articles);


                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                fragmentHomeBinding.recyclerViewArticle.setLayoutManager(layoutManager);
                fragmentHomeBinding.recyclerViewArticle.setItemAnimator(new DefaultItemAnimator());
                fragmentHomeBinding.recyclerViewArticle.setNestedScrollingEnabled(false);
                fragmentHomeBinding.recyclerViewArticle.setHasFixedSize(true);
                fragmentHomeBinding.recyclerViewArticle.setItemViewCacheSize(20);
                fragmentHomeBinding.recyclerViewArticle.setDrawingCacheEnabled(true);
                fragmentHomeBinding.recyclerViewArticle.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                adapter = new Adapter(mContext, articleList);
                fragmentHomeBinding.recyclerViewArticle.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                initListener();

            } else {

                WifiManager wifi = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);
                alertDialogBuilder = new AlertDialog.Builder(requireContext());
                alertDialogBuilder.setMessage("Internet Not Available or poor Internet connection. Please, connect the Internet and enter again");
                alertDialogBuilder.setPositiveButton("OK",
                        (dialog, which) -> requireActivity().finish());

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                showErrorMessage("Oops...", "Network failure, try again later.\n");
            }

            fragmentHomeBinding.swipeRefreshLayout.setRefreshing(false);

        });
    }

    private void initListener() {
        adapter.setOnItemClickListener((view, position) -> {

            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            Article article = articleList.get(position);
            intent.putExtra("url", article.getUrl());
            intent.putExtra("title", article.getTitle());
            intent.putExtra("img", article.getUrlToImage());
            intent.putExtra("source", article.getSource().getName());
            intent.putExtra("description", article.getDescription());

            startActivity(intent);
        });
    }

    public void showErrorMessage(String title, String message) {

        if (fragmentHomeBinding.errorLayoutId.errorLayout.getVisibility() == View.GONE) {
            fragmentHomeBinding.errorLayoutId.errorLayout.setVisibility(View.VISIBLE);
        }

        fragmentHomeBinding.errorLayoutId.errorImg.setImageResource(R.drawable.no_result);
        fragmentHomeBinding.errorLayoutId.errorTitle.setText(title);
        fragmentHomeBinding.errorLayoutId.errorMsg.setText(message);
        fragmentHomeBinding.errorLayoutId.errorBttn.setOnClickListener(v -> getNewsArticles());
    }

    @Override
    public void onStop() {
        super.onStop();
        GlideApp.get(requireActivity()).clearMemory();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding.recyclerViewArticle.setAdapter(null);

        fragmentHomeBinding = null;

        if (adapter != null) {
            adapter = null;
        }

        if (mContext != null) {
            mContext = null;
        }

        GlideApp.get(requireActivity()).clearMemory();
        Toast.makeText(getContext(), "HomeFrag onDestroyView", Toast.LENGTH_LONG).show();
    }
}

