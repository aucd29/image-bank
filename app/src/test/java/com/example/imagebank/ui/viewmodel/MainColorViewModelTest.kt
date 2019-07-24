package com.example.imagebank.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.R
import com.google.android.material.tabs.TabLayout
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-23 <p/>
 *
 * http://robolectric.org/
 *
 * http://juranosaurus.blogspot.com/2015/10/robolectric-1.html
 * https://github.com/nongdenchet/android-mvvm-with-tests
 * https://github.com/adibfara/Lives/blob/master/lives/src/test/java/com/snakydesign/livedataextensions/TransformingTest.kt
 */

@RunWith(JUnit4::class)
class MainColorViewModelTest {
    lateinit var viewModel: MainColorViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mockContext()

        viewModel = MainColorViewModel(context)
    }

    @Test
    fun defaultStatusColor() {
        val observer = mock(Observer::class.java) as Observer<Int>

        viewModel.statusColor.observeForever(observer)
        verify(observer).onChanged(COLOR_PRIMARY_DARK)
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun defaultViewColor() {
        assertEquals(viewModel.viewColor.get(), COLOR_PRIMARY)
    }

    @Test
    fun defaultTabIndicatorColor() {
        assertEquals(viewModel.tabIndicatorColor.get(), COLOR_ACCENT)
    }

    @Test
    fun changeTabPos0() {
        val tab = mock(TabLayout.Tab::class.java)

        `when`(tab.position).thenReturn(0)
        viewModel.tabChangedCallback.get()?.onTabSelected(tab)

        assertEquals(viewModel.tabIndicatorColor.get(), COLOR_ACCENT)
    }

    @Test
    fun changeTabPos1() {
        val tab = mock(TabLayout.Tab::class.java)

        `when`(tab.position).thenReturn(1)
        viewModel.tabChangedCallback.get()?.onTabSelected(tab)

        assertEquals(viewModel.tabIndicatorColor.get(), COLOR_WHITE)
    }

    @Test
    fun changeTabPos2() {
        val tab = mock(TabLayout.Tab::class.java)

        `when`(tab.position).thenReturn(2)
        viewModel.tabChangedCallback.get()?.onTabSelected(tab)

        assertEquals(viewModel.tabIndicatorColor.get(), COLOR_WHITE)
    }

    @Test
    fun changeTabPos0_to_2() {
        val tab = mock(TabLayout.Tab::class.java)

        `when`(tab.position).thenReturn(0)
        viewModel.tabChangedCallback.get()?.onTabSelected(tab)
        assertEquals(viewModel.tabIndicatorColor.get(), COLOR_ACCENT)

        `when`(tab.position).thenReturn(2)
        viewModel.tabChangedCallback.get()?.onTabSelected(tab)
        assertEquals(viewModel.tabIndicatorColor.get(), COLOR_WHITE)
    }

    @Test
    fun changeTabPos2_to_0() {
        val tab = mock(TabLayout.Tab::class.java)

        `when`(tab.position).thenReturn(2)
        viewModel.tabChangedCallback.get()?.onTabSelected(tab)
        assertEquals(viewModel.tabIndicatorColor.get(), COLOR_WHITE)

        `when`(tab.position).thenReturn(0)
        viewModel.tabChangedCallback.get()?.onTabSelected(tab)
        assertEquals(viewModel.tabIndicatorColor.get(), COLOR_ACCENT)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MOCK
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        const val COLOR_PRIMARY      = 11
        const val COLOR_PRIMARY_DARK = COLOR_PRIMARY + 1
        const val COLOR_ACCENT       = COLOR_PRIMARY_DARK + 1
        const val COLOR_WHITE        = COLOR_ACCENT + 1
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var context: Application
    private fun mockContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock(Resources::class.java))

        `when`(context.getColor(R.color.colorPrimary)).thenReturn(COLOR_PRIMARY)
        `when`(context.getColor(R.color.colorPrimaryDark)).thenReturn(COLOR_PRIMARY_DARK)
        `when`(context.getColor(R.color.colorAccent)).thenReturn(COLOR_ACCENT)
        `when`(context.getColor(android.R.color.white)).thenReturn(COLOR_WHITE)

        `when`(context.resources.getColor(R.color.colorPrimary)).thenReturn(COLOR_PRIMARY)
        `when`(context.resources.getColor(R.color.colorPrimaryDark)).thenReturn(COLOR_PRIMARY_DARK)
        `when`(context.resources.getColor(R.color.colorAccent)).thenReturn(COLOR_ACCENT)
        `when`(context.resources.getColor(android.R.color.white)).thenReturn(COLOR_WHITE)
    }
}