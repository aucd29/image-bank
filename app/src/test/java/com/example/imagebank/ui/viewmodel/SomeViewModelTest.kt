package com.example.imagebank.ui.viewmodel

import android.animation.AnimatorSet
import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.imagebank.ui.main.some.SomeViewModel
import com.example.imagebank.util.mockReactiveX
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.slf4j.LoggerFactory

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-29 <p/>
 */


@RunWith(JUnit4::class)
class SomeViewModelTest {
    lateinit var viewModel: SomeViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewModel = SomeViewModel(context)
    }

    @Test
    fun scrollAndAnimate() {
        viewModel.apply {
            someAnimateOvalLayoutTop = 3900
            assertNull(ovalToRight.get())

            scrollListener.get()?.callback?.invoke(0, 4000, true)
            assertNotNull(ovalToRight.get())
            assertEquals(ovalToRight.get()?.value, SomeViewModel.TO_RIGHT_TRANSITION)
        }
    }

    // https://gist.github.com/dpmedeiros/7f7724fdf13fc5390bb05958448cdcad
    @Test
    fun statePause() {
        mockReactiveX()

        Single.just(mock(AnimatorSet::class.java))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewModel.apply {
                    ovalAniSet = it
                    onPause()

                    assertTrue(ovalAniSet?.isPaused ?: false)
                }
            }, {
                if (mLog.isDebugEnabled) {
                    it.printStackTrace()
                }

                mLog.error("ERROR: ${it.message}")

                assertNull(it)
            })
    }

    @Test
    fun stateResume() {
        mockReactiveX()

        Single.just(mock(AnimatorSet::class.java))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewModel.apply {
                    ovalAniSet = it
                    onResume()

                    assertFalse(ovalAniSet?.isPaused ?: true)
                }
            }, {
                if (mLog.isDebugEnabled) {
                    it.printStackTrace()
                }

                mLog.error("ERROR: ${it.message}")

                assertNull(it)
            })
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MOCK
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeViewModelTest::class.java)
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private fun initMock() {
        MockitoAnnotations.initMocks(this)
        mockContext()
    }

    @Mock
    private lateinit var context: Application

    private fun mockContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock(Resources::class.java))

    }
}