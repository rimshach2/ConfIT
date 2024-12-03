package com.fyp.confit;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class second {

    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule<>(SplashPage.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.RECORD_AUDIO");

    @Test
    public void second() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edt_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_data),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("l164260@lhr.nu.edu.pk"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edt_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_data),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.img_eye),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_data),
                                        3),
                                2),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edt_password), withText("password"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_data),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.signInbtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        mActivityTestRule= new ActivityTestRule<>(MainActivity.class);

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.viewHistoryBtn), withText("View Detailed History"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        pressBack();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
