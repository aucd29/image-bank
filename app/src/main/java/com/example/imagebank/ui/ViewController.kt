package com.example.imagebank.ui

import androidx.fragment.app.FragmentManager
import com.example.imagebank.R
import kotlinx.android.synthetic.main.main_activity.view.*
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */


class ViewController @Inject constructor(private val manager: FragmentManager) {
    // 나만의 룰을 만들었더니만 navigation editor 나와버림 =_ = ㅋ

    companion object {
        private val mLog = LoggerFactory.getLogger(ViewController::class.java)

        const val CONTAINER = R.id.main_container
    }

    fun detail() {
    }
}