package com.example.imagebank.ui.main.dibs

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import brigitte.drawable
import brigitte.widget.BannerViewModel
import com.example.imagebank.R
import com.example.imagebank.common.PreloadConfig
import com.example.imagebank.model.local.Banner
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

@Singleton
class DibsBannerViewModel @Inject constructor(application: Application,
    private val mPreConfig: PreloadConfig
) : BannerViewModel<Banner>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(DibsBannerViewModel::class.java)
    }

    init {
        initAdapter(R.layout.dibs_banner_layout)
        items.set(mPreConfig.bannerList)
    }
}