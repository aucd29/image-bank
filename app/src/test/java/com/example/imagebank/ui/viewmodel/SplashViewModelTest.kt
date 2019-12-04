package com.example.imagebank.ui.viewmodel

import com.example.imagebank.ui.main.SplashViewModel
import com.example.imagebank.util.BaseJUnitViewModelTest
import com.example.imagebank.util.mockObserver
import com.example.imagebank.util.verifyChanged
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-23 <p/>
 *
 * https://github.com/fabioCollini/DaggerMock
 * https://stackoverflow.com/questions/50950654/androidviewmodel-and-unit-tests
 * https://fernandocejas.com/2014/04/08/unit-testing-asynchronous-methods-with-mockito/
 */

@RunWith(JUnit4::class)
class SplashViewModelTest: BaseJUnitViewModelTest<SplashViewModel>() {

    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = SplashViewModel()
    }

    @Test
    fun closeTest() {
        viewmodel.apply {
            mockObserver(closeSplashEvent).apply {
                closeSplash()

                verifyChanged(null)
            }
        }
    }
}