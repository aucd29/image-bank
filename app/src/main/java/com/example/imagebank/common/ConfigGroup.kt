package com.example.imagebank.common

import android.content.Context
import android.graphics.Point
import brigitte.systemService
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import android.view.*
import brigitte.Json
import brigitte.jsonParse
import com.example.imagebank.model.local.Banner
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStream

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2019. 2. 15. <p/>
 */

@Singleton
class Config @Inject constructor(val context: Context) {
    val SCREEN = Point()
    init {
        //
        // W / H
        //
        val windowManager = context.systemService<WindowManager>()
        windowManager?.defaultDisplay?.getSize(SCREEN)
    }
}

////////////////////////////////////////////////////////////////////////////////////
//
// PreloadConfig
//
////////////////////////////////////////////////////////////////////////////////////

@Singleton
class PreloadConfig @Inject constructor(
    private val mDisposable: CompositeDisposable,
    private val mContext: Context
) {
    val bannerListSingle: Single<InputStream> = Single.just(mContext.assets.open("banner_info.json"))
        .subscribeOn(Schedulers.io())

    init {
        //            .map { ism -> ism.jsonParse<List<Banner>>()  }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(PreloadConfig::class.java)
    }
}
