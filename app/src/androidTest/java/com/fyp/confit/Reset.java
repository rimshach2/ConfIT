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
public class Reset {

    @Rule
    public ActivityTestRule<SplashPage> mActivityTestRule = new ActivityTestRule<>(SplashPage.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.SEND_SMS",
                    "android.permission.RECORD_AUDIO");

    @Test
    public void reset() {
        ViewInteraction appCompatTextView = onView(
                allOf(withText("Forgot your Password?"),
                        childAtPosition(
                                allOf(withId(R.id.login_data),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                4),
                        isDisplayed()));
        appCompatTextView.perform(click());

        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.forgotButton),
                        childAtPosition(
                                allOf(withId(R.id.forgot_password_form),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatImageView.perform(click());

        try{
            Thread.sleep(500);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edt_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.forgot_password_form),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("l164260@lhr.nu.edu.pk"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edt_email), withText("l164260@lhr.nu.edu.pk"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.forgot_password_form),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());
        try{
            Thread.sleep(500);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.forgotButton),
                        childAtPosition(
                                allOf(withId(R.id.forgot_password_form),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        try{
            Thread.sleep(10000);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

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
