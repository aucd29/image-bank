package com.example.imagebank.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-23 <p/>
 *
 * http://robolectric.org/
 *
 * http://juranosaurus.blogspot.com/2015/10/robolectric-1.html
 * https://github.com/nongdenchet/android-mvvm-with-tests
 */

//@RunWith(RobolectricTestRunner::class)
@RunWith(JUnit4::class)
class MainColorViewModelTest {
    @Mock
    lateinit var observer: Observer<Int>
    lateinit var viewModel: MainColorViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mockContext()
        mockLifecycle()

        viewModel = MainColorViewModel(context)
    }

    @Test
    fun testStatusColor() {
        viewModel.statusColor.observe(lifecycleOwner, observer)

        verify(observer).onChanged(COLOR_PRIMARY_DARK)
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

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var lifecycle: LifecycleRegistry
    private fun mockLifecycle() {
        lifecycle = LifecycleRegistry(lifecycleOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
    }

    @Mock private lateinit var context: Application
    private fun mockContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock(Resources::class.java))
        `when`(context.getColor(R.color.colorPrimary)).thenReturn(COLOR_PRIMARY)
        `when`(context.getColor(R.color.colorPrimaryDark)).thenReturn(COLOR_PRIMARY_DARK)
        `when`(context.getColor(android.R.color.white)).thenReturn(COLOR_WHITE)
    }

}