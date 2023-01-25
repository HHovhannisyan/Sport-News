package com.example.sportNewsAPI.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.example.sportNewsAPI.BR;
import com.example.sportNewsAPI.R;

public class FeedbackForm extends BaseObservable {
    private final FeedbackFields feedbackFields = new FeedbackFields();
    private final FeedbackErrorFields errors = new FeedbackErrorFields();
    private final MutableLiveData<FeedbackFields> buttonClick = new MutableLiveData<>();

    @Bindable
    public boolean isValid() {
        boolean valid = isSubjectValid(false);
        valid = isMsgValid(false) && valid;
        notifyPropertyChanged(BR.subjectError);
        notifyPropertyChanged(BR.msgError);
        return valid;
    }


    public boolean isSubjectValid(Boolean titleText) {
        String subject = feedbackFields.getSubject();
        if (subject!=null && subject.length()>2) {
            errors.setSubject(null);
            notifyPropertyChanged(BR.valid);
            return true;
        } else {
            if (titleText) {
                errors.setSubject(R.string.empty_subject);
                notifyPropertyChanged(BR.valid);
            }
            return false;
        }
    }


    public boolean isMsgValid(Boolean msgText) {
        String msg = feedbackFields.getMsg();
        if (msg!=null && msg.length()>5) {
            errors.setMsg(null);
            notifyPropertyChanged(BR.valid);
            return true;
        }else {
            if (msgText) {
                errors.setMsg(R.string.error_message);
                notifyPropertyChanged(BR.valid);
            }
            return false;
        }
    }


    public void onClick() {
        if (isValid()) {
            buttonClick.setValue(feedbackFields);
        }
    }


    public MutableLiveData<FeedbackFields> getFeedbackFields() {
        return buttonClick;
    }

    public FeedbackFields getFields() {
        return feedbackFields;
    }

    @Bindable
    public Integer getSubjectError() {
        return errors.getSubject();
    }

    @Bindable
    public Integer getMsgError() {
        return errors.getMsg();
    }
}
