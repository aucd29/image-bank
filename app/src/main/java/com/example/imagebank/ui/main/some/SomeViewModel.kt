package com.example.imagebank.ui.main.some

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import brigitte.CommandEventViewModel
import com.example.imagebank.R
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-16 <p/>
 */

class SomeViewModel @Inject constructor(application: Application

) : CommandEventViewModel(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeViewModel::class.java)
    }
}