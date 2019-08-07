package com.example.imagebank.ui.viewmodel

import brigitte.color
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.R
import com.example.imagebank.util.*
import com.google.android.material.tabs.TabLayout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-23 <p/>
 *
 * http://robolectric.org/
 *
 * http://juranosaurus.blogspot.com/2015/10/robolectric-1.html
 * https://github.com/nongdenchet/android-mvvm-with-tests
 * https://github.com/adibfara/Lives/blob/master/lives/src/test/java/com/snakydesign/livedataextensions/TransformingTest.kt
 */

@RunWith(RobolectricTestRunner::class)
class MainColorViewModelTest: BaseRoboViewModelTest<MainColorViewModel>() {
    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = MainColorViewModel(app)
    }

    @Test
    fun defaultStatusColor() {
        viewmodel.apply {
            mockObserver<Int>(statusColor).apply {
                verifyChanged(color(R.color.colorPrimaryDark))
            }
        }
    }

    @Test
    fun defaultViewColor() {
        viewmodel.viewColor.get().assertEquals(color(R.color.colorPrimary))
    }

    @Test
    fun defaultTabIndicatorColor() {
        viewmodel.tabIndicatorColor.get().assertEquals(color(R.color.colorAccent))
    }

    @Test
    fun changeTabPos0() {
        val tab = mock(TabLayout.Tab::class.java)
        tab.position.mockReturn(0)

        viewmodel.apply {
            tabChangedCallback.get()?.onTabSelected(tab)
            tabIndicatorColor.get().assertEquals(color(R.color.colorAccent))
        }
    }

    @Test
    fun changeTabPos1() {
        val tab = mock(TabLayout.Tab::class.java)
        tab.position.mockReturn(1)

        viewmodel.apply {
            tabChangedCallback.get()?.onTabSelected(tab)
            tabIndicatorColor.get().assertEquals(color(android.R.color.white))
        }
    }

    @Test
    fun changeTabPos2() {
        val tab = mock(TabLayout.Tab::class.java)
        tab.position.mockReturn(2)

        viewmodel.apply {
            tabChangedCallback.get()?.onTabSelected(tab)
            tabIndicatorColor.get().assertEquals(color(android.R.color.white))
        }
    }

    @Test
    fun changeTabPos0_to_2() {
        val tab = mock(TabLayout.Tab::class.java)

        tab.position.mockReturn(0)
        viewmodel.apply {
            tabChangedCallback.get()?.onTabSelected(tab)
            tabIndicatorColor.get().assertEquals(color(R.color.colorAccent))
        }

        tab.position.mockReturn(2)
        viewmodel.apply {
            tabChangedCallback.get()?.onTabSelected(tab)
            tabIndicatorColor.get().assertEquals(color(android.R.color.white))
        }
    }

    @Test
    fun changeTabPos2_to_0() {
        val tab = mock(TabLayout.Tab::class.java)

        tab.position.mockReturn(2)
        viewmodel.apply {
            tabChangedCallback.get()?.onTabSelected(tab)
            tabIndicatorColor.get().assertEquals(color(android.R.color.white))
        }

        tab.position.mockReturn(0)
        viewmodel.apply {
            tabChangedCallback.get()?.onTabSelected(tab)
            tabIndicatorColor.get().assertEquals(color(R.color.colorAccent))
        }
    }
}