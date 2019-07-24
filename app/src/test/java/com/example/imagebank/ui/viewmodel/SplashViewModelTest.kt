package com.example.imagebank.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.imagebank.ui.main.SplashViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.verifyNoMoreInteractions


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-23 <p/>
 *
 * https://github.com/fabioCollini/DaggerMock
 * https://stackoverflow.com/questions/50950654/androidviewmodel-and-unit-tests
 * https://fernandocejas.com/2014/04/08/unit-testing-asynchronous-methods-with-mockito/
 */

@RunWith(JUnit4::class)
class SplashViewModelTest {
    lateinit var viewModel: SplashViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockitoAnnotations.initMocks(this)
//        mockLifecycle()

        viewModel = SplashViewModel()
    }

    @Test
    fun closeTest() {
        val observer = Mockito.mock(Observer::class.java) as Observer<Void>
        viewModel.closeSplashEvent.observeForever(observer)
        viewModel.closeSplash()

        verify(observer).onChanged(null)
        verifyNoMoreInteractions(observer)
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MOCK
    //
    ////////////////////////////////////////////////////////////////////////////////////
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

//    @Mock
//    lateinit var lifecycleOwner: LifecycleOwner
//    lateinit var lifecycle: LifecycleRegistry
//    private fun mockLifecycle() {
//        lifecycle = LifecycleRegistry(lifecycleOwner)
//        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
//        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
//    }
}