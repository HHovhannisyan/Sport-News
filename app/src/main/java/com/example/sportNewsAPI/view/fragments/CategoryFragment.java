package com.example.sportNewsAPI.view.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.databinding.FragmentCategoryBinding;
import com.example.sportNewsAPI.viewModel.CategoryViewModel;
import com.example.sportNewsAPI.view.activities.ListLinksActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CategoryFragment extends Fragment {

    Animation animSport;
    FragmentCategoryBinding fragmentCategoryBinding;
    View view;
    TextView tv;
    ArrayAdapter<String> spinnerArrayAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        fragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);


        Resources res = requireActivity().getResources();

        String[] arrayCategories = res.getStringArray(R.array.categories);

        final List<String> categoryList = new ArrayList<>(Arrays.asList(arrayCategories));


        // Initializing an ArrayAdapter
        spinnerArrayAdapter = new ArrayAdapter<String>(
                fragmentCategoryBinding.getRoot().getContext(), R.layout.spinner_item, categoryList) {


            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }


            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                view = super.getDropDownView(position, convertView, parent);
                tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    tv.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentCategoryBinding.spinner.setAdapter(spinnerArrayAdapter);

        fragmentCategoryBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);


                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText(getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }

                switch (position) {

                    case 1:
                        animateSportImg(R.mipmap.ic_football);
                        getIntentSportLinks("football");
                        // animateClickOnImg();
                        break;

                    case 2:

                        animateSportImg(R.mipmap.ic_basketball);
                        getIntentSportLinks("basketball");
                        // animateClickOnImg();
                        break;

                    case 3:

                        animateSportImg(R.mipmap.ic_launcher_round);
                        getIntentSportLinks("tennis");
                        // animateClickOnImg();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        categoryViewModel.getCategoryTxt().observe(getViewLifecycleOwner(), fragmentCategoryBinding.categoryTxt::setText);

        Toast.makeText(getContext(), "CategoryFragment's  onCreateView", Toast.LENGTH_SHORT).show();


        return fragmentCategoryBinding.getRoot();
    }

    public void animateSportImg(int imgId) {
        fragmentCategoryBinding.imgSport.setImageResource(imgId);
        animSport = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        fragmentCategoryBinding.imgSport.startAnimation(animSport);
        animSport.cancel();
    }

    public void getIntentSportLinks(String sport) {
        fragmentCategoryBinding.imgSport.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ListLinksActivity.class);
            List<String> sportListLinks = getSelectedSportLinks(sport);
            Collections.sort(sportListLinks);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("linkList", (ArrayList<String>) sportListLinks);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }


    public List<String> getSelectedSportLinks(String sport) {
        List<String> items = new ArrayList<>();
        try {
            String jsonString = getAssetsJSON("sport.json");
            // items = new ArrayList<>();
            JSONObject root = new JSONObject(jsonString);
            // Log.d(TAG, "Json: " + jsonString);

            JSONArray array = root.getJSONArray(sport);

            for (int index = 0; index < array.length(); index++) {
                JSONObject object = array.getJSONObject(index);
                items.add(object.getString("URL"));
            }
            //Log.d(TAG, "team: " + items);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }


    /* Get File in Assets Folder */
    public String getAssetsJSON(String fileName) {
        String json = null;
        try {
            InputStream inputStream = requireActivity().getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];


            if (inputStream.read(buffer) > 0) {
                inputStream.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


    @Override
    public void onPause() {
        super.onPause();
        spinnerArrayAdapter = null;
        Toast.makeText(getContext(), " Category onPause", Toast.LENGTH_LONG).show();
    }



    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getContext(), " Category onStop", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentCategoryBinding = null;
        view = null;
        tv = null;

        spinnerArrayAdapter = null;

        Toast.makeText(getContext(), " Category onDestroyView", Toast.LENGTH_LONG).show();
    }
}