package com.example.sportNewsAPI.view.activities;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.sportNewsAPI.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class NewsDetailActivityTest {

    @Rule
    public final ActivityTestRule<NewsDetailActivity> mainActivityTestRule = new ActivityTestRule<>(NewsDetailActivity.class, true);

    @Test
    public void newsDetailTest(){
        Espresso.onView(withId(R.id.content_coordin)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.bttn_read_more)).check(matches(ViewMatchers.isDisplayed()));
    }
}