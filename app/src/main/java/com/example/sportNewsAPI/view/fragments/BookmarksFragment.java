package com.example.sportNewsAPI.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.databinding.FragmentBookmarksBinding;
import com.example.sportNewsAPI.view.activities.WebActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.sportNewsAPI.view.activities.WebActivity.PREFERENCES;
import static com.example.sportNewsAPI.view.activities.WebActivity.WEB_LINKS;
import static com.example.sportNewsAPI.view.activities.WebActivity.WEB_TITLE;


public class BookmarksFragment extends Fragment {

    ArrayList<HashMap<String, String>> listRowData;

    public  final String TAG_TITLE = "title";
    public  final String TAG_LINK = "link";

    ListAdapter adapter;
    FragmentBookmarksBinding fragmentBookmarksBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentBookmarksBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_bookmarks, container,false);

        fragmentBookmarksBinding.swipeToRefresh.setColorSchemeResources(R.color.colorAccent);

        fragmentBookmarksBinding.swipeToRefresh.setOnRefreshListener(() -> {
            loadBookmarks();
            fragmentBookmarksBinding.swipeToRefresh.setRefreshing(false);
        });


        loadBookmarks();
        fragmentBookmarksBinding.listView.setOnItemClickListener((parent, view, position, id) -> {
            Object o = fragmentBookmarksBinding.listView.getAdapter().getItem(position);
            if (o instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) o;

                Intent in = new Intent(getContext(), WebActivity.class);
                in.putExtra("url", String.valueOf(map.get(TAG_LINK)));
                startActivity(in);
            }
        });


        fragmentBookmarksBinding.listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Object o = fragmentBookmarksBinding.listView.getAdapter().getItem(i);
            if (o instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) o;

                deleteBookmark(String.valueOf(map.get(TAG_TITLE)), String.valueOf(map.get(TAG_LINK)));
            }

            return true;
        });

        fragmentBookmarksBinding.setLifecycleOwner(this);

        return fragmentBookmarksBinding.getRoot();
    }


    private void loadBookmarks() {

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String jsonLink = sharedPreferences.getString(WEB_LINKS, null);
        String jsonTitle = sharedPreferences.getString(WEB_TITLE, null);
        listRowData = new ArrayList<>();

        if (jsonLink != null && jsonTitle != null) {

            Gson gson = new Gson();
            ArrayList<String> linkArray = gson.fromJson(jsonLink, new TypeToken<ArrayList<String>>() {
            }.getType());

            ArrayList<String> titleArray = gson.fromJson(jsonTitle, new TypeToken<ArrayList<String>>() {
            }.getType());


            for (int i = 0; i < linkArray.size(); i++) {
                HashMap<String, String> map = new HashMap<>();

                if (titleArray.get(i).length() == 0) {
                    map.put(TAG_TITLE, "Bookmark " + (i + 1));
                } else {
                    map.put(TAG_TITLE, titleArray.get(i));
                }

                map.put(TAG_LINK, linkArray.get(i));
                listRowData.add(map);
            }

            adapter = new SimpleAdapter(getContext(),
                    listRowData, R.layout.bookmark_list_row,
                    new String[]{TAG_TITLE, TAG_LINK},
                    new int[]{R.id.txt_title, R.id.txt_link});

            fragmentBookmarksBinding.listView.setAdapter(adapter);
        }

        fragmentBookmarksBinding.emptyList.setVisibility(View.VISIBLE);
        fragmentBookmarksBinding.listView.setEmptyView(fragmentBookmarksBinding.emptyList);
    }


    private void deleteBookmark(final String title, final String link) {

        new AlertDialog.Builder(requireActivity())
                .setTitle("DELETE")
                .setMessage("Confirm that you want to delete this bookmark?")
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                    String jsonLink = sharedPreferences.getString(WEB_LINKS, null);
                    String jsonTitle = sharedPreferences.getString(WEB_TITLE, null);

                    if (jsonLink != null && jsonTitle != null) {

                        Gson gson = new Gson();
                        ArrayList<String> linkArray = gson.fromJson(jsonLink, new TypeToken<ArrayList<String>>() {
                        }.getType());

                        ArrayList<String> titleArray = gson.fromJson(jsonTitle, new TypeToken<ArrayList<String>>() {
                        }.getType());

                        linkArray.remove(link);
                        titleArray.remove(title);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(WEB_LINKS, new Gson().toJson(linkArray));
                        editor.putString(WEB_TITLE, new Gson().toJson(titleArray));
                        editor.apply();

                        loadBookmarks();
                    }

                    dialogInterface.dismiss();
                }).setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentBookmarksBinding=null;
        if(adapter!=null){
            adapter=null;
        }
    }
}