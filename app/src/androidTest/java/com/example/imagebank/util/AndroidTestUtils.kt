@file:Suppress("NOTHING_TO_INLINE", "unused")
package com.example.imagebank.util

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.internal.util.Checks
import com.example.imagebank.MainApp
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import org.junit.rules.TestRule
import org.junit.runners.model.Statement


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-29 <p/>
 */

inline fun Int.isDisplayed() {
    onView(withId(this)).check(matches(ViewMatchers.isDisplayed()))
}

inline fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int
): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent)
                    && view == parent.getChildAt(position)
        }
    }
}

fun withBackgroundColor(color: Int): Matcher<View> {
    Checks.checkNotNull(color)
    return object : BoundedMatcher<View, View>(View::class.java) {
        override fun matchesSafely(view: View): Boolean {
            val backGroundColor = (view.background as ColorDrawable).color
            return color == backGroundColor
        }

        override fun describeTo(description: Description) {}
    }
}

//https://medium.com/insiden26/okhttp-idling-resource-for-espresso-462ef2417049
//https://github.com/chiragkunder/espresso-okhttp-idling-resource?source=post_page---------------------------
//    @get:Rule
//    var rule = OkHttpIdlingResourceRule(mActivityTestRule.activity.okhttp)
//class OkHttpIdlingResourceRule(okhttp: OkHttpClient): TestRule {
//    private val resource : IdlingResource = OkHttp3IdlingResource.create("okhttp", okhttp)
//
//    override fun apply(base: Statement?, description: org.junit.runner.Description?): Statement {
//        return object: Statement() {
//            override fun evaluate() {
//                IdlingRegistry.getInstance().register(resource)
//                base?.evaluate()
//                IdlingRegistry.getInstance().unregister(resource)
//            }
//        }
//    }
//}
//
////https://medium.com/@yair.kukielka/idlingresource-dagger-and-junit-rules-198e3ae791ff
//class ThreadPoolIdlingResourceRule: TestRule {
//    private val resource: IdlingResource? = null
//
//    override fun apply(base: Statement?, description: org.junit.runner.Description?): Statement {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//}