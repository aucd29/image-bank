package com.example.imagebank.ui.main.seach

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import brigitte.*
import com.example.imagebank.R
import com.example.imagebank.model.remote.KakaoRestSearchService
import com.example.imagebank.model.remote.entity.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory
import javax.inject.Inject
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class SearchViewModel @Inject constructor(application: Application,
    private val api: KakaoRestSearchService
) : RecyclerViewModel<KakaoMergeResult>(application) {

    val keyword         = ObservableField<String>("설현")
    val gridCount       = ObservableInt(2)
    val editorAction    = ObservableField<(String?) -> Boolean>()   // editor 에서 done 버튼 선택 시
    val visibleProgress = ObservableInt(View.GONE)

    val dp              = CompositeDisposable()

    init {
        initAdapter("search_item")
        editorAction.set {
            if (mLog.isDebugEnabled) {
                mLog.debug("EVENT DONE")
            }

            search()
            true
        }
    }

    fun search() {
        if (mLog.isDebugEnabled) {
            mLog.debug("SEARCH $keyword")
        }

        command(CMD_HIDE_KEYBOARD)

        if (!app.isNetworkConntected()) {
            toast(R.string.network_invalid_connectivity)
            return
        }

        visibleProgress.visibleToggle()

        keyword.get()?.let {
            if (it.isEmpty()) {
                toast(R.string.search_pls_insert_keyword)
                return@let
            }

            // 검색은 키워드 하나에 이미지 검색과 동영상 검색을 동시에 사용,
            dp.add(Observable.zip(api.image(it), api.vclip(it),
                BiFunction { image: KakaoImageSearch, vclip: KakaoVClipSearch ->
                    // 두 검색 결과를 합친 리스트를 사용합니다.
                    val result = arrayListOf<KakaoMergeResult>()

                    // 2018-12-16T09:40:08.000+09:00
                    image.documents.forEach {
                        it.thumbnail_url?.let { thumbnail ->
                            result.add(KakaoMergeResult(thumbnail, it.datetime,
                                it.datetime.toUnixTime(DATE_FORMAT)))
                        }
                    }

                    vclip.documents.forEach {
                        result.add(KakaoMergeResult(it.thumbnail, it.datetime,
                            it.datetime.toUnixTime(DATE_FORMAT)))
                    }

                        result
                })
                .subscribeOn(Schedulers.io())
                .map {
                    // 두 검색 결과를 datetime 필드를 이용해 최신순으로 나열하여 출력합니다.
                    it.sortWith(Comparator { o1, o2 ->
                        when {
                            o1.datetime > o2.datetime  -> -1
                            o1.datetime == o2.datetime -> 0
                            else                       -> 1
                        }
                    })

                    visibleProgress.visibleToggle()

                    it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (mLog.isDebugEnabled) {
                        mLog.debug("")
                    }

                    items.set(it)
                }, {
                    if (mLog.isDebugEnabled) {
                        it.printStackTrace()
                    }

                    mLog.error("ERROR: ${it.message}")
                    it.message?.let(::toast)
                }))
        } ?: toast(R.string.search_pls_insert_keyword)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ITextChanged
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        private val mLog = LoggerFactory.getLogger(SearchViewModel::class.java)

        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

        const val CMD_DIBS = "cmd-dibs"
        const val CMD_HIDE_KEYBOARD = "cmd-hide-keyboard"
    }
}
