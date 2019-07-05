@file:Suppress("NOTHING_TO_INLINE", "unused")
package com.example.imagebank.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Description
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-04 <p/>
 */


inline fun displayedWithId(resid: Int) =
    onView(allOf(withId(resid), isDisplayed()))

inline fun performClick(resid: Int) {
    displayedWithId(resid)
        .perform(ViewActions.click())
}

inline fun hasRecyclerViewItem(resid: Int) {
    val matcher = object: TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {}
        override fun matchesSafely(item: View?): Boolean {
            return item?.let {
                if (it is RecyclerView) {
                    return it.adapter?.itemCount ?: 0 > 0
                } else false
            } ?: false
        }
    }

    displayedWithId(resid).check(matches(matcher))
//    onView(withId(resid)).check(matches(matcher))
}
