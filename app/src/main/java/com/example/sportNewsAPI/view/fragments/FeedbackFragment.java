package com.example.sportNewsAPI.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.databinding.FragmentFeedbackBinding;
import com.example.sportNewsAPI.viewModel.FeedbackViewModel;


public class FeedbackFragment extends Fragment {

    private FeedbackViewModel feedbackViewModel;
    private FragmentFeedbackBinding fragmentFeedbackBinding;

    @VisibleForTesting
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentFeedbackBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feedback, container, false);

        feedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
        if (savedInstanceState == null) {
            feedbackViewModel.init();
        }


        fragmentFeedbackBinding.setModel(feedbackViewModel);
        setupButtonClick();

        fragmentFeedbackBinding.setLifecycleOwner(this);

        // Inflate the layout for this fragment
        return fragmentFeedbackBinding.getRoot();
    }


    private void setupButtonClick() {
        feedbackViewModel.getFeedbackFields().observe(getViewLifecycleOwner(), feedbackFields -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");

            String address = "joytech@gmail.com";
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, feedbackFields.getSubject());
            emailIntent.putExtra(Intent.EXTRA_TEXT, feedbackFields.getMsg());
            emailIntent.setPackage("com.google.android.gm");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(requireContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();

        Toast.makeText(requireContext(), "Feedback  onPause", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStop() {
        super.onStop();

        Toast.makeText(requireContext(), "Feedback  onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      /*  fragmentFeedbackBinding.titleInput.clearFocus();
        fragmentFeedbackBinding.titleInput.clearFocus();*/
        fragmentFeedbackBinding = null;
    }
}