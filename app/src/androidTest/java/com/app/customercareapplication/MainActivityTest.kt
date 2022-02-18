package com.app.customercareapplication

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val emailMessage = "New Feedback!"

    /**
     *
     * This test demonstrates Espresso Intents using the IntentsTestRule, a class that extends
     * ActivityTestRule. IntentsTestRule initializes Espresso-Intents before each test that is annotated
     * with @Test and releases it once the test is complete. The designated Activity
     * is also terminated after each test.
     *
     */
    @get:Rule
    var mActivityRule: IntentsTestRule<MainActivity> = IntentsTestRule(
        MainActivity::class.java
    )

    @Before
    fun stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
    }

    @Test
    fun clickSendEmailButton_SendsEmail() {
        onView(withId(R.id.button_first)).perform(click())
        // intended(Matcher<Intent> matcher) asserts the given matcher matches one and only one
        // intent sent by the application.
        intended(
            allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasExtra(Intent.EXTRA_TEXT, emailMessage)
            )
        )
    }

    @Test
    fun clickCallButton_CallsCustomerCare() {
        /*onView(withId(R.id.fab)).perform(click())
        intended(
            allOf(
                hasAction(Intent.ACTION_CALL),
            )
        )*/

        // Build the result to return when the activity is launched.
        val resultData = Intent()
        val phoneNumber = "1800 202 6182"
        resultData.putExtra("phone", phoneNumber)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        // Set up result stubbing when an intent sent to "contacts" is seen.
        intending(toPackage("com.android.contacts")).respondWith(result)

        // User action that results in "contacts" activity being launched.
        // Launching activity expects phoneNumber to be returned and displayed.
        onView(withId(R.id.fab)).perform(click())

        // Assert that the data we set up above is shown.
//        onView(withId(R.id.phoneNumber)).check(matches(withText(phoneNumber)))
    }
}