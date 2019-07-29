package com.example.imagebank.ui.main.some

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import brigitte.CommandEventViewModel
import brigitte.ILifeCycle
import brigitte.ScrollChangeListener
import brigitte.bindingadapter.AnimParams
import brigitte.bindingadapter.AnimationBindingAdapter
import brigitte.bindingadapter.AnimatorParams
import brigitte.bindingadapter.AnimatorSetParams
import brigitte.dpToPx
import com.example.imagebank.R
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-16 <p/>
 */

typealias AnimOption = AnimationBindingAdapter

class SomeViewModel @Inject constructor(
    application: Application
) : CommandEventViewModel(application), ILifeCycle {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeViewModel::class.java)

        const val TO_RIGHT_TRANSITION = 20f
        const val TO_LEFT_TRANSITION = TO_RIGHT_TRANSITION * -1
        const val TRANSITION_DURATION = 500L
    }

    val ovalAniSetParams      = ObservableField<AnimatorSetParams>()
    val ovalRandAnimSetParams = ObservableField<AnimatorSetParams>()
    val ovalToRight           = ObservableField<AnimParams>()
    val ovalToLeft            = ObservableField<AnimParams>()
    var ovalAniSet: AnimatorSet?     = null
    var ovalRandAniSet: AnimatorSet? = null

    val scrollListener = ObservableField<ScrollChangeListener>()
    var someAnimateOvalLayoutTop: Int = 0

    init {
        scrollListener.set(ScrollChangeListener { x, y, isBottom ->
            if (mLog.isTraceEnabled) {
                mLog.trace("SCROLL VALUE : $y ($someAnimateOvalLayoutTop) (${someAnimateOvalLayoutTop - y})")
            }

            if (someAnimateOvalLayoutTop != 0 && y > someAnimateOvalLayoutTop) {
                if (ovalToRight.get() == null) {
                    initOvalTransition()
                    initOvalAnimation()
                    initRandOvalAnimation()
                }
            }
        })
    }

    fun initOvalTransition() {
        ovalToRight.set(AnimParams(TO_RIGHT_TRANSITION, duration = TRANSITION_DURATION))
        ovalToLeft.set(AnimParams(TO_LEFT_TRANSITION, duration = TRANSITION_DURATION))
    }

    fun initOvalAnimation() {
        ovalAniSetParams.set(AnimatorSetParams(mapOf(
            AnimOption.K_ALPHA to AnimatorParams(0f, 1f, ObjectAnimator.INFINITE),
            AnimOption.K_SCALE_X to AnimatorParams(3f, 0f, ObjectAnimator.INFINITE),
            AnimOption.K_SCALE_Y to AnimatorParams(3f, 0f, ObjectAnimator.INFINITE)
        ), 4000, callback = { ovalAniSet = it }))
    }

    fun initRandOvalAnimation() {
        var randValue = Random().nextInt(15)
        if (randValue <= 2) randValue = 5

        if (mLog.isInfoEnabled) {
            mLog.info("RAND OVAL VALUE $randValue")
        }

        ovalRandAnimSetParams.set(AnimatorSetParams(mapOf(
            AnimOption.K_ALPHA to AnimatorParams(0f, 1f),
            AnimOption.K_SCALE_X to AnimatorParams(3f, 0f),
            AnimOption.K_SCALE_Y to AnimatorParams(3f, 0f)
        ), randValue * 1000L, callback = { ovalRandAniSet = it }, endListener = {
            if (mLog.isInfoEnabled) {
                mLog.info("END RAND OVAL ANIMATION")
            }

//            ovalRandAniSet?.end()
            ovalRandAniSet = null

            // 시간을 재 계산해서 다시 애니메이션 시작
            initRandOvalAnimation()
        }))
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ILifeCycle
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onPause() {
        ovalAniSet?.pause()
        ovalRandAniSet?.pause()
    }

    override fun onResume() {
        ovalAniSet?.resume()
        ovalRandAniSet?.resume()
    }
}