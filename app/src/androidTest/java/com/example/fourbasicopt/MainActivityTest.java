package com.example.fourbasicopt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void tap(int... buttonIds) {
        for (int id : buttonIds) {
            onView(withId(id)).perform(click());
        }
    }

    private void assertResult(String expected) {
        onView(withId(R.id.tvResult)).check(matches(withText(expected)));
    }

    private void assertExpression(String expected) {
        onView(withId(R.id.tvExpression)).check(matches(withText(expected)));
    }

    @Test
    public void showsZeroOnLaunch() {
        assertResult("0");
        assertExpression("");
    }

    @Test
    public void addition() {
        tap(R.id.btn1, R.id.btn2, R.id.btnAdd, R.id.btn3, R.id.btnEquals);
        assertResult("15");
        assertExpression("12 + 3 =");
    }

    @Test
    public void subtraction() {
        tap(R.id.btn7, R.id.btnSubtract, R.id.btn1, R.id.btn0, R.id.btnEquals);
        assertResult("-3");
        assertExpression("7 − 10 =");
    }

    @Test
    public void multiplication() {
        tap(R.id.btn6, R.id.btnMultiply, R.id.btn7, R.id.btnEquals);
        assertResult("42");
        assertExpression("6 × 7 =");
    }

    @Test
    public void division() {
        tap(R.id.btn9, R.id.btnDivide, R.id.btn4, R.id.btnEquals);
        assertResult("2.25");
        assertExpression("9 ÷ 4 =");
    }

    @Test
    public void everyDigitButtonEntersItsDigit() {
        tap(R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn0);
        assertResult("1234567890");
    }

    @Test
    public void decimalInput() {
        tap(R.id.btn0, R.id.btnDot, R.id.btn1, R.id.btnAdd,
                R.id.btn0, R.id.btnDot, R.id.btn2, R.id.btnEquals);
        assertResult("0.3");
    }

    @Test
    public void backspaceRemovesLastDigit() {
        tap(R.id.btn1, R.id.btn2, R.id.btn3, R.id.btnBackspace);
        assertResult("12");
    }

    @Test
    public void clearResetsDisplay() {
        tap(R.id.btn1, R.id.btn2, R.id.btnAdd, R.id.btn3, R.id.btnClear);
        assertResult("0");
        assertExpression("");
    }

    @Test
    public void divideByZeroShowsErrorMessage() {
        tap(R.id.btn5, R.id.btnDivide, R.id.btn0, R.id.btnEquals);
        onView(withId(R.id.tvResult)).check(matches(withText(R.string.error_divide_by_zero)));

        tap(R.id.btn3);
        assertResult("3");
    }

    @Test
    public void stateSurvivesRecreation() {
        tap(R.id.btn1, R.id.btn2, R.id.btnAdd, R.id.btn3);

        activityRule.getScenario().recreate();

        assertResult("3");
        assertExpression("12 +");
        tap(R.id.btnEquals);
        assertResult("15");
    }
}
