package com.example.imagebank.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.example.imagebank.ui.main.SplashViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.ArgumentCaptor
import org.mockito.Captor



/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-23 <p/>
 *
 * https://github.com/fabioCollini/DaggerMock
 * https://stackoverflow.com/questions/50950654/androidviewmodel-and-unit-tests
 * https://fernandocejas.com/2014/04/08/unit-testing-asynchronous-methods-with-mockito/
 */

@RunWith(JUnit4::class)
class SplashViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var lifecycle: LifecycleRegistry

    @Mock
    lateinit var observer: Observer<Void>

    lateinit var viewModel: SplashViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockitoAnnotations.initMocks(this)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)

        viewModel = SplashViewModel()
    }

    @Test
    fun closeTest() {
        viewModel.closeSplashEvent.observe(lifecycleOwner, observer)
        viewModel.closeSplash()

        verify(observer).onChanged(null)
    }
}