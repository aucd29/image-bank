package com.example.imagebank.ui.main.dibs

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import brigitte.drawable
import brigitte.widget.BannerViewModel
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

    val pageChangedLive = MutableLiveData<Int>()

    init {
        if (mLog.isDebugEnabled) {
            mLog.debug("INIT DIBS BANNER ")
        }

        pageChangeCallback.set {
            pageChangedLive.value = it
        }

        initAdapter("banner_layout")
        items.set(mPreConfig.bannerList)

        if (mLog.isDebugEnabled) {
            mLog.debug("DIBS BANNER ITEMS (${items.get()?.size})")
        }
    }
}