package com.example.foodapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragementTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testBottomNavigationItems() {
        Thread.sleep(5000)

        onView(withId(R.id.bottomnav)).check(matches(isDisplayed()))

        // click each nav item
        onView(withId(R.id.grid)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.menu3)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.bell3)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.profile3)).perform(click())
        Thread.sleep(1000)
    }
}
