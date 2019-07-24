@file:Suppress("NOTHING_TO_INLINE", "unused")
package com.example.imagebank.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-23 <p/>
 *
 * android-architecture 참조
 */

@Throws(InterruptedException::class)
inline fun <T> awaitObserveValue(livedata: LiveData<T>, timeout: Long = 1): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object: Observer<T> {
        override fun onChanged(t: T) {
            data = t

            latch.countDown()
            livedata.removeObserver(this)
        }
    }

    livedata.observeForever(observer)
    latch.await(timeout, TimeUnit.SECONDS)

    return data
}