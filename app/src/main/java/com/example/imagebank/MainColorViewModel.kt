package com.example.imagebank

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import brigitte.TabSelectedCallback
import brigitte.color
import brigitte.widget.StatusBarViewModel
import com.google.android.material.tabs.TabLayout
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-16 <p/>
 */

class MainColorViewModel @Inject constructor(app: Application) : StatusBarViewModel(app) {
    companion object {
        private val mLog = LoggerFactory.getLogger(MainColorViewModel::class.java)
    }

    val viewColor          = ObservableInt(color(R.color.colorPrimary))
    val tabChangedCallback = ObservableField<TabSelectedCallback>()
    val tabIndicatorColor  = ObservableInt(color(R.color.colorAccent))
    var focusedTabLiveData = MutableLiveData<TabLayout.Tab?>()

    init {
        statusColor.value = color(R.color.colorPrimaryDark)

        tabChangedCallback.set(TabSelectedCallback {
            if (mLog.isDebugEnabled) {
                mLog.debug("TAB CHANGED ${it?.position}")
            }

            focusedTabLiveData.value = it

            when (it?.position) {
                0    -> tabIndicatorColor.set(color(R.color.colorAccent))
                else -> tabIndicatorColor.set(color(android.R.color.white))
            }
        })
    }

    fun changeColor(color: Int, viewColor: Int) {
        this.statusColor.value = color
        this.viewColor.set(viewColor)
    }
}
