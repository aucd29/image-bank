package com.example.imagebank.ui.detail

import android.app.Application
import brigitte.CommandEventViewModel
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

class DetailViewModel @Inject constructor(application: Application

) : CommandEventViewModel(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(DetailViewModel::class.java)
    }
}