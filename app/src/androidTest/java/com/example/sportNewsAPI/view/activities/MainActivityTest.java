package com.example.sportNewsAPI.view.activities;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.sportNewsAPI.R;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true);

    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class);



    @Test
    public void drawerTest() throws InterruptedException {
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
        Thread.sleep(1000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_category));
        Thread.sleep(1000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_bookmarks));
        Thread.sleep(1000);


        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_feedback));
        Thread.sleep(1000);
    }


    @Test
    public void feedbackFragmentTest() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_feedback));
        onView(withId(R.id.title_input)).perform(click(), replaceText("title"));
        onView(withId(R.id.msg_input)).perform(click(), replaceText("message"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).check(matches(isEnabled()));
        onView(withId(R.id.submit_button)).perform(click());
    }



   @Test
    public void categoryFragmentTest(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_category));
        onView(withId(R.id.category_txt)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.category_txt)).check(matches(withText("Category")));
    }



    @Test
    public void homeFragmentTest() {
        onView(withId(R.id.topHeadlines)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.topHeadlines)).check(matches(withText("Latest Headlines")));
    }


    @Test
    public void testSampleRecyclerVisible() {
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_article))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }


    @Test
    public void testCaseForRecyclerClick() {
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_article))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mActivityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }



    @Test
    public void testCaseForRecyclerScroll() {

        // Get total item of RecyclerView
        RecyclerView recyclerView = mActivityRule.getActivity().findViewById(R.id.recyclerView_article);
        int itemCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();

        // Scroll to end of page with position
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_article))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mActivityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.scrollToPosition(itemCount - 1));
    }


    @Test
    public void testCaseForRecyclerItemView() {

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_article))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(withViewAtPosition(1, Matchers.allOf(
                        ViewMatchers.withId(R.id.article), isDisplayed()))));
    }


    public Matcher<View> withViewAtPosition(final int position, final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
