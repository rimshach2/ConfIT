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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignUpTest {

    @Rule
    public ActivityTestRule<SplashPage> mActivityTestRule = new ActivityTestRule<>(SplashPage.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.RECORD_AUDIO");

    @Test
    public void signUpTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.SignUpTxt), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.signInbtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView.perform(click());

        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.SUname),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("nimra"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.SUname), withText("nimra"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.signInbtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.SUemail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("l164313@lhr.nu.pk"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.SUemail), withText("l164313@lhr.nu.pk"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.signInbtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView3.perform(click());

        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.SUphone),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        3),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("321440412"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.SUedt_password1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        4),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.signInbtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView4.perform(click());

        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.SUedt_password1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        4),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.SUedt_password1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        4),
                                1),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.SUedt_password1), withText("123456"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        4),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(pressImeActionButton());

        ViewInteraction appCompatImageView5 = onView(
                allOf(withId(R.id.eye1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        4),
                                2),
                        isDisplayed()));
        appCompatImageView5.perform(click());

        ViewInteraction appCompatImageView6 = onView(
                allOf(withId(R.id.signInbtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView6.perform(click());

        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.SUedt_password2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        5),
                                1),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("122345"), closeSoftKeyboard());

        ViewInteraction appCompatImageView7 = onView(
                allOf(withId(R.id.eye2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        5),
                                2),
                        isDisplayed()));
        appCompatImageView7.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.SUedt_password2), withText("122345"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        5),
                                1),
                        isDisplayed()));
        appCompatEditText11.perform(pressImeActionButton());

        ViewInteraction appCompatImageView8 = onView(
                allOf(withId(R.id.signInbtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView8.perform(click());

        try{
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.SUedt_password2), withText("122345"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        5),
                                1),
                        isDisplayed()));
        appCompatEditText12.perform(click());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.SUedt_password2), withText("122345"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        5),
                                1),
                        isDisplayed()));
        appCompatEditText13.perform(replaceText("123456"));

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.SUedt_password2), withText("123456"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        5),
                                1),
                        isDisplayed()));
        appCompatEditText14.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.SUedt_password2), withText("123456"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        5),
                                1),
                        isDisplayed()));
        appCompatEditText15.perform(pressImeActionButton());

        ViewInteraction appCompatImageView9 = onView(
                allOf(withId(R.id.eye1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        4),
                                2),
                        isDisplayed()));
        appCompatImageView9.perform(click());

        ViewInteraction appCompatImageView10 = onView(
                allOf(withId(R.id.eye2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.SUdata),
                                        5),
                                2),
                        isDisplayed()));
        appCompatImageView10.perform(click());

        ViewInteraction appCompatImageView11 = onView(
                allOf(withId(R.id.signInbtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView11.perform(click());

        try{
            Thread.sleep(3000);
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
