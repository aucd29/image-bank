package com.example.imagebank.ui.main.dibs

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import brigitte.drawable
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

class BannerViewModel @Inject constructor(application: Application

) : AndroidViewModel(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(BannerViewModel::class.java)
    }

    fun convertColor(str: String) =
        Color.parseColor(str)

    fun convertImage(str: String) =
        drawable(str)
}