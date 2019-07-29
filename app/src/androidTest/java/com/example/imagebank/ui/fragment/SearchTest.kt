package com.example.imagebank.ui.fragment


import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.imagebank.MainActivity
import com.example.imagebank.R
import com.example.imagebank.util.childAtPosition
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.core.app.ActivityScenario
import org.junit.Before
import androidx.test.espresso.IdlingRegistry
import org.junit.After

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchTest {
//    @Rule
//    @JvmField
//    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private var mIdlingResource: IdlingResource? = null
    private var mActivity: MainActivity? = null

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
    fun searchFragmentTest() {
        searchIcon().perform(click())

        // 요건 좀더 찾아봐야함
        Thread.sleep(3000)

        dibsIcon(0).perform(click())
        dibsIcon(1).perform(click())

        tab(1, R.string.tab_dibs).perform(click())

        dibsImage(0).check(matches(withContentDescription(R.string.cd_dips_image_thumbnail)))
        dibsImage(1).check(matches(withContentDescription(R.string.cd_dips_image_thumbnail)))
    }

    private fun searchIcon() = onView(allOf(withId(R.id.search),
        withContentDescription(R.string.cd_search_search_icon),
        childAtPosition(childAtPosition(withId(R.id.scroll_container),0), 1),
        isDisplayed()))

    private fun dibsIcon(position: Int) = onView(allOf(withId(R.id.search_item_dibs),
        withContentDescription(R.string.cd_search_dibs_icon),
            childAtPosition(allOf(withId(R.id.search_item_container),
            childAtPosition(withId(R.id.search_recycler), position)), 1),
        isDisplayed()))

    private fun dibsImage(position: Int) = onView(allOf(withId(R.id.dibs_item_image),
        withContentDescription(R.string.cd_dips_image_thumbnail),
        childAtPosition(allOf(withId(R.id.dibs_item_container),
            childAtPosition(withId(R.id.dibs_recycler), position)), 0),
        isDisplayed()))

    private fun tab(position: Int, @StringRes description: Int) = onView(
        allOf(withContentDescription(description),
            childAtPosition(childAtPosition(withId(R.id.tabs), 0), position), isDisplayed())
    )
}
