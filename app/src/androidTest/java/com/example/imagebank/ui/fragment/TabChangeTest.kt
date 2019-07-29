package com.example.imagebank.ui.fragment


import androidx.annotation.StringRes
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import brigitte.toColor
import com.example.imagebank.MainActivity
import com.example.imagebank.R
import com.example.imagebank.util.childAtPosition
import com.example.imagebank.util.withBackgroundColor
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TabChangeTest {
    private var mIdlingResource: IdlingResource? = null

    @Before
    fun registerIdlingResource() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity { activity ->
            mIdlingResource = activity.getIdlingResource()
            // To prove that the test fails, omit this call:
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @After
    fun unregisterIdlingResource() {
        mIdlingResource?.let { IdlingRegistry.getInstance().unregister(it) }
    }

    @Test
    fun tabAndStatusbarColorTest() {
        tab(1, R.string.tab_dibs).perform(click())
        dibsViewPager().perform(ViewActions.swipeLeft())
        dibsViewPager().perform(ViewActions.swipeLeft())
        matchesStatusBarColor("#3A6276".toColor())

        tab(2, R.string.tab_some).perform(click())
        someBanner().perform(ViewActions.swipeLeft())
        matchesStatusBarColor("#437684".toColor())

        tab(1, R.string.tab_dibs).perform(click())
        matchesStatusBarColor("#3A6276".toColor())

        tab(2, R.string.tab_some).perform(click())
        matchesStatusBarColor("#437684".toColor())

        someBanner().perform(ViewActions.swipeLeft())
        matchesStatusBarColor("#3A6276".toColor())
    }

    private fun tab(position: Int, @StringRes description: Int) = onView(
        allOf(withContentDescription(description),
            childAtPosition(childAtPosition(withId(R.id.tabs), 0), position), isDisplayed())
    )

    // https://stackoverflow.com/questions/38183251/can-i-test-status-bar-tint-color-using-espresso
    private fun matchesStatusBarColor(color: Int) =  onView(withId(android.R.id.statusBarBackground))
        .check(matches(withBackgroundColor(color)))

    private fun dibsViewPager() = onView(allOf(withId(R.id.dibs_viewpager),
        childAtPosition(allOf(withId(R.id.dibs_contents_container)),0)))

    private fun someBanner() = onView(allOf(withId(R.id.some_banner),
        childAtPosition(allOf(withId(R.id.some_container)),0)))
}
