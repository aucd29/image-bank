package com.example.imagebank.espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.imagebank.MainActivity
import com.example.imagebank.R
import com.example.imagebank.common.RecyclerViewMatchers.hasItemCount
import com.example.imagebank.common.RecyclerViewMatchers.hasItems
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-04 <p/>
 */

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class SearchTest {
    @Test
    fun search() {
        activityTestRule.launchActivity(null)

        onView(withId(R.id.search)).perform(click())
        //https://stackoverflow.com/questions/36399787/how-to-count-recyclerview-items-with-espresso
        onView(withId(R.id.search_recycler)).check(matches(hasItems()))
    }

    companion object {
        @BeforeClass
        fun setup() {
            // Setting up
        }
    }

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)
}