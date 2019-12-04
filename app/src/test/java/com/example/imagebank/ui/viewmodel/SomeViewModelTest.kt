package com.example.imagebank.ui.viewmodel

import android.animation.AnimatorSet
import com.example.imagebank.ui.main.some.SomeViewModel
import com.example.imagebank.util.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-29 <p/>
 */


@RunWith(RobolectricTestRunner::class)
class SomeViewModelTest: BaseRoboViewModelTest<SomeViewModel>() {
    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = SomeViewModel(app)
    }

    @Test
    fun scrollAndAnimateTest() {
        viewmodel.apply {
            someAnimateOvalLayoutTop = 3900
            ovalToRight.get().assertNull()

            scrollListener.get()?.callback?.invoke(0, 4000, true)
            ovalToRight.get().assertNotNull()

            ovalToRight.get()?.value.assertEquals(SomeViewModel.TO_RIGHT_TRANSITION)
        }
    }

    // https://gist.github.com/dpmedeiros/7f7724fdf13fc5390bb05958448cdcad
    @Test
    fun statePauseTest() {
        mockReactiveX()

        Single.just(mock(AnimatorSet::class.java))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewmodel.apply {
                    ovalAniSet = it
                    onPause()

                    ovalAniSet?.isPaused?.assertTrue() ?: assert(false)
                }
            }, {
                assertNull(it)
            })
    }

    @Test
    fun stateResumeTest() {
        mockReactiveX()

        Single.just(mock(AnimatorSet::class.java))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewmodel.apply {
                    ovalAniSet = it
                    onResume()

                    ovalAniSet?.isPaused?.assertFalse() ?: assert(false)
                }
            }, {
                assertNull(it)
            })
    }
}