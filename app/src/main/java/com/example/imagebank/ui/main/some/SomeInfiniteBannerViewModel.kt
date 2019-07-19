package com.example.imagebank.ui.main.some

import android.app.Application
import brigitte.widget.BannerViewModel
import com.example.imagebank.R
import com.example.imagebank.common.PreloadConfig
import com.example.imagebank.model.local.Banner
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-16 <p/>
 */

@Singleton
class SomeInfiniteBannerViewModel @Inject constructor(app: Application,
    private val mPreConfig: PreloadConfig
) : BannerViewModel<Banner>(app) {

    init {
         initInfiniteAdapter(R.layout.some_infinite_banner_layout)
         items.set(mPreConfig.someList)
    }
}