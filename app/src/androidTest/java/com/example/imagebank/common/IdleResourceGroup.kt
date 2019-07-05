package com.example.imagebank.common

import androidx.test.espresso.IdlingResource

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-04 <p/>
 */

// https://medium.com/@elye.project/instrumental-test-better-espresso-without-sleep-d3391b19a581
class FetchingIdlingResource: IdlingResource, FetcherListener {
    private var mIdle = true
    private var mResCallback: IdlingResource.ResourceCallback? = null

    override fun doneFetching() {
        mIdle = true
        mResCallback?.onTransitionToIdle()
    }

    override fun beginFetching() {
        mIdle = false
    }

    override fun getName() = FetchingIdlingResource::class.java.simpleName
    override fun isIdleNow() = mIdle
    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        mResCallback = callback
    }
}

interface FetcherListener {
    fun doneFetching()
    fun beginFetching()
}