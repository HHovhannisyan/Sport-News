package com.example.sportNewsAPI;

import com.example.sportNewsAPI.model.FeedbackForm;
import com.example.sportNewsAPI.viewModel.FeedbackViewModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void fieldsAreCorrect() {
        FeedbackViewModel feedbackViewModel = new FeedbackViewModel();
        feedbackViewModel.init();
        FeedbackForm feedbackForm = feedbackViewModel.getForm();
        feedbackForm.getFields().setSubject("fvkjyu");
        feedbackForm.getFields().setMsg("dfhtrhrtfrdb greg bgerdwe weger");
        assertTrue("Form is not valid", feedbackForm.isValid());
    }


    @Test
    public void fieldsAreShort() {
        FeedbackViewModel feedbackViewModel = new FeedbackViewModel();
        feedbackViewModel.init();
        FeedbackForm feedbackForm = feedbackViewModel.getForm();
        feedbackForm.getFields().setSubject("f");
        feedbackForm.getFields().setMsg("hghrger");
        assertFalse("Subject should be invalid", feedbackForm.isSubjectValid(true));
        assertTrue("Message should be valid", feedbackForm.isMsgValid(false));
        assertFalse("Fields are short", feedbackForm.isValid());
        assertEquals("Error message should be \"Format is invalid\" not " + feedbackForm.getSubjectError(), "Format is invalid", feedbackForm.getSubjectError());
    }


    @Test
    public void fieldsAreEmpty() {
        FeedbackViewModel feedbackViewModel = new FeedbackViewModel();
        feedbackViewModel.init();
        FeedbackForm feedbackForm = feedbackViewModel.getForm();
        feedbackForm.getFields().setSubject("");
        feedbackForm.getFields().setMsg("");
        assertFalse("Subject should be invalid", feedbackForm.isSubjectValid(true));
        assertTrue("Message should be valid", feedbackForm.isMsgValid(false));
        assertFalse("Fields are empty", feedbackForm.isValid());
        assertEquals("Error message should be \"Format is invalid\" not " + feedbackForm.getSubjectError(), "Format is invalid", feedbackForm.getSubjectError());
    }
}