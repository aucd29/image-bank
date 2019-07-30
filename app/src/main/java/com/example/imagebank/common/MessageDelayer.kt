package com.example.imagebank.common

import android.os.Handler

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-30 <p/>
 */

object MessageDelayer {
    val DELAY_MILLIS = 3000L

    fun processMessage(message: String, callback: (text:String) -> Unit, idleResource: SimpleIdlingResource?) {
        idleResource?.setIdleState(false)

        val handler = Handler()
        handler.postDelayed({
            callback.invoke(message)
            idleResource?.setIdleState(true)
        }, DELAY_MILLIS)
    }
}