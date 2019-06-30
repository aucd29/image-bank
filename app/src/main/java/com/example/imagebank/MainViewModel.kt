package com.example.imagebank

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import brigitte.*
import com.example.imagebank.model.remote.entity.UserInfo
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

        const val CMD_STATUS_BAR_COLOR = "cmd-status-bar-color"
        const val CMD_SHOW_NAVIGATION  = "cmd-shownavi"
    }

    val tabChanged = ObservableField<TabSelectedCallback>()
    val bgTopView  = ObservableInt(color(R.color.colorPrimary))
    val kakaoText  = ObservableField("kakao<b>Bank</b>".html())

    val userInfo   = ObservableField<UserInfo>()

    init {
        tabChanged.set(TabSelectedCallback {
            if (mLog.isDebugEnabled) {
                mLog.debug("TAB CHANGED ${it?.position}")
            }

            when (it?.position) {
                0 -> {
                    command(CMD_STATUS_BAR_COLOR, R.color.colorPrimaryDark)
                    bgTopView.set(color(R.color.colorPrimary))
                }
                else -> {
                    command(CMD_STATUS_BAR_COLOR, R.color.colorDarkGreen)
                    bgTopView.set(color(R.color.colorGreen))
                }
            }
        })

        userInfo.set(UserInfo("", "최철동",
            "마지막 접속 2019.06.30 11:25"))
    }
}