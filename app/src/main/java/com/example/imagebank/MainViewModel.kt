package com.example.imagebank

import android.app.Application
import brigitte.CommandEventViewModel
import com.google.android.material.tabs.TabLayout
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class MainViewModel @Inject constructor(application: Application

) : CommandEventViewModel(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(MainViewModel::class.java)
    }


    fun onTabSelected(tab: TabLayout.Tab?) {
        if (mLog.isDebugEnabled) {
            mLog.debug("TAB CHANGED ${tab?.position}")
        }
    }
}