package com.example.imagebank

import android.app.Application
import androidx.annotation.ColorInt
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import brigitte.*
import com.example.imagebank.model.remote.entity.UserInfo
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class MainViewModel @Inject constructor(application: Application

) : CommandEventViewModel(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(MainViewModel::class.java)
        const val CMD_SHOW_NAVIGATION  = "cmd-shownavi"
    }

    val kakaoText = ObservableField("kakao<b>Bank</b>".html())
    val userInfo  = ObservableField<UserInfo>()

    val offscreenLimit = ObservableInt(3)

    init {
        userInfo.set(UserInfo("", "최철동", "마지막 접속 2019.06.30 11:25"))
    }
}