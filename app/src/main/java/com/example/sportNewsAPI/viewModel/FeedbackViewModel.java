package com.example.sportNewsAPI.viewModel;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.VisibleForTesting;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportNewsAPI.model.FeedbackFields;
import com.example.sportNewsAPI.model.FeedbackForm;


public class FeedbackViewModel extends ViewModel {
    private FeedbackForm feedbackForm;
    private View.OnFocusChangeListener onFocusSubject;
    private View.OnFocusChangeListener onFocusMsg;

    @VisibleForTesting
    public void init() {
        feedbackForm = new FeedbackForm();
        onFocusSubject = (view, focused) -> {
            EditText et = (EditText) view;
            if (et.getText().length() > 2 && !focused) {
                feedbackForm.isSubjectValid(true);
            }
        };

        onFocusMsg = (view, focused) -> {
            EditText et = (EditText) view;

            if (et.getText().length() > 5 && !focused) {
                feedbackForm.isMsgValid(true);
            }
        };
    }

    public View.OnFocusChangeListener getSubjectOnFocusChangeListener() {
        return onFocusSubject;
    }

    public View.OnFocusChangeListener getMsgOnFocusChangeListener() {
        return onFocusMsg;
    }

    public void onButtonClick() {
        feedbackForm.onClick();
    }

    public MutableLiveData<FeedbackFields> getFeedbackFields() {
        return feedbackForm.getFeedbackFields();
    }

    public FeedbackForm getForm() {
        return feedbackForm;
    }

    @BindingAdapter("error")
    public static void setError(EditText editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }
    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText editText, View.OnFocusChangeListener onFocusChangeListener) {
        if (editText.getOnFocusChangeListener() == null) {
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        feedbackForm = null;
        onFocusSubject = null;
        onFocusMsg = null;
    }
}
