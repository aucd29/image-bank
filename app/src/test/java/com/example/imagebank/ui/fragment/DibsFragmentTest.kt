package com.example.imagebank.ui.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import com.example.imagebank.BuildConfig
import com.example.imagebank.R
import com.example.imagebank.ui.main.dibs.DibsFragment
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController
import org.robolectric.util.FragmentTestUtil
import org.robolectric.util.FragmentTestUtil.startFragment

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-29 <p/>
 */

@RunWith(RobolectricTestRunner::class)
class FragmentTest {
    lateinit var fragment: DibsFragment

    @Before
    fun setup() {
        // 어머나 androidx =_ =;;
    }
}