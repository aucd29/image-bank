package com.example.imagebank.common

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Point
import brigitte.systemService
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import android.view.*
import brigitte.jsonParse
import com.example.imagebank.model.local.Banner
import io.reactivex.Single

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
    private val mAssetManager: AssetManager
) {
    val bannerList: List<Banner> = Single.just(mAssetManager.open("banner_info.json")
        .readBytes())
        .map { it.jsonParse<List<Banner>>() }
        .blockingGet()

    val someList: List<Banner> = Single.just(mAssetManager.open("some_info.json")
        .readBytes())
        .map { it.jsonParse<List<Banner>>() }
        .blockingGet()
}
