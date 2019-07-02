package com.example.imagebank.ui.main.dibs

import android.app.Application
import androidx.databinding.ObservableInt
import brigitte.RecyclerViewModel
import com.example.imagebank.model.remote.entity.KakaoSearchResult
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class DibsViewModel @Inject constructor(application: Application
) : RecyclerViewModel<KakaoSearchResult>(application) {
    companion object {
        private val mLog = LoggerFactory.getLogger(DibsViewModel::class.java)
    }

    val gridCount = ObservableInt(2)

    init {
        initAdapter("dibs_item")
        items.set(arrayListOf())
    }
}
