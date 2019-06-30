package com.example.imagebank.ui.main.seach

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import brigitte.*
import com.example.imagebank.R
import com.example.imagebank.common.Config
import com.example.imagebank.model.remote.KakaoRestSearchService
import com.example.imagebank.model.remote.entity.*
import io.reactivex.Observable
import io.reactivex.Single
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
    private val api: KakaoRestSearchService,
    private val config: Config
) : RecyclerViewModel<KakaoMergeResult>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SearchViewModel::class.java)

        const val V_TAB_SPANCOUNT   = 2
        const val DATE_FORMAT       = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

        const val CMD_DIBS          = "cmd-dibs"
        const val CMD_HIDE_KEYBOARD = "cmd-hide-keyboard"
        const val CMD_TOP_SCROLL    = "cmd-top-scroll"
    }

    val keyword             = ObservableField<String>("아이유")
    val editorAction        = ObservableField<(String?) -> Boolean>()   // editor 에서 done 버튼 선택 시
    val scrollListener      = ObservableField<ScrollChangeListener>()

    val visibleProgress     = ObservableInt(View.GONE)
    val visibleTopScroll    = ObservableInt(View.GONE)

    val mDibsList = arrayListOf<KakaoMergeResult>()
    var mPage     = 1
    val mDp       = CompositeDisposable()

    val layoutManager = StaggeredGridLayoutManager(V_TAB_SPANCOUNT,
        StaggeredGridLayoutManager.VERTICAL)

    init {
        initAdapter("search_item")
        adapter.get()?.isScrollToPosition = false

        editorAction.set {
            if (mLog.isDebugEnabled) {
                mLog.debug("EVENT DONE")
            }

            search(1)
            true
        }
        scrollListener.set(ScrollChangeListener { x, y, isBottom ->
            if (mLog.isTraceEnabled()) {
                mLog.trace("SCROLL Y : $y")
            }

            visibleTopScroll.apply {
                if (y > config.SCREEN.y) {
                    if (!isVisible()) { visible() }
                } else {
                    if (isVisible()) { gone() }
                }
            }

            // 페이지 처리를 하려고 하는데
            // 데이터가 add 시 top 으로 이동하는 문제 존재
            // 이런건 또 처음 보네 =_=
//            // nested scroll 이라 recycler view 에 add scroll listener 에 넣는게 의미가 없어
//            // 이곳에서 처리
//            if (isBottom && mPage <= 50) {
//                val pos = layoutManager.findLastVisibleItemPosition()
//                if (isNextLoad(pos)) {
//                    search(mPage.inc())
//                }
//            }
        })
    }

//    var old = 0
//    var new = 0

    fun search(p: Int) {
        mPage = p
        dataLoading = true
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

            if (mLog.isDebugEnabled) {
                mLog.debug("SEARCH $it")
            }

            ioThread {
                // 검색은 키워드 하나에 이미지 검색과 동영상 검색을 동시에 사용,
                mDp.add(Observable.zip(api.image(it, mPage.toString()), api.vclip(it, mPage.toString()),
                    BiFunction { image: KakaoImageSearch, vclip: KakaoVClipSearch ->
                        // 두 검색 결과를 합친 리스트를 사용합니다.
                        val result = arrayListOf<KakaoMergeResult>()

                        // FIXME 현재 kakao api 버그로 페이징에 문제가 존재 image 의 경우 항상 같은 데이터가 들어온다.
                        // FIXME 데이터를 제외 해볼까 싶었는데 정력 낭비로 생각하고 1페이지만 데이터를 참조하고
                        // FIXME 버그가 수정되면 살리는 형태로 하도록 수정
                        if (image.message == null && mPage <=1) {
                            // 2018-12-16T09:40:08.000+09:00
                            image.documents?.forEach {
                                it.thumbnail_url?.let { thumbnail ->
                                    result.add(KakaoMergeResult(thumbnail, it.datetime,
                                        it.datetime.toUnixTime(DATE_FORMAT)))
                                }
                            }
                        } else {
                            mLog.error("ERROR: ${image.message}")
                        }

                        if (vclip.message == null) {
                            vclip.documents?.forEach {
                                result.add(KakaoMergeResult(it.thumbnail, it.datetime,
                                    it.datetime.toUnixTime(DATE_FORMAT)))
                            }
                        } else {
                            mLog.error("ERROR: ${vclip.message}")
                        }

                        result
                    })
//                    .map {
//                        // 버그 문제로 중복 데이터 제거
//                        if (mPage > 1) {
//                            val itr = it.iterator()
//                            while (itr.hasNext()) {
//                                val item = itr.next()
//                                items.get()?.find { l -> l.thumbnail == item.thumbnail }?.let { _ ->
//                                    itr.remove()
//                                }
//                            }
//                        }
//
//                        it
//                    }
                    .map {
                        // 두 검색 결과를 datetime 필드를 이용해 최신순으로 나열하여 출력합니다.
                        it.sortWith(Comparator { o1, o2 ->
                            when {
                                o1.datetime > o2.datetime  -> -1
                                o1.datetime == o2.datetime -> 0
                                else                       -> 1
                            }
                        })

                        if (mPage > 1) {
//                            old = items.get()?.size ?: 0
//                            new = it.size

                            it.addAll(0, items.get()!!)
                        }

                        // 찜에 넣어둔 경우 이를 검사해서 활성화 시켜준다.
                        mDibsList.forEach { dibs ->
                            it.find { f -> dibs.thumbnail == f.thumbnail }?.dibs?.toggle()
                        }

                        it
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (mLog.isDebugEnabled) {
                            mLog.debug("SEARCHED DATA SET!! ${it.size}")
                        }

                        items.set(it)
//                        if (old > 0) {
//                            adapter.get()?.notifyDataSetChanged()
//                        }

                        mDp.add(delay {
                            dataLoading = false
                            visibleProgress.visibleToggle()
                        })
                    }, {
                        if (mLog.isDebugEnabled) {
                            it.printStackTrace()
                        }

                        mLog.error("ERROR: ${it.message}")
                        it.message?.let(::toast)
                    }))
            }
        } ?: toast(R.string.search_pls_insert_keyword)
    }

    private fun checkDibsList(item: KakaoMergeResult) {
        mDp.add(Single.just(mDibsList)
            .subscribeOn(Schedulers.io())
            .map {
                val f = it.find { f -> f.thumbnail == item.thumbnail }
                if (f != null) {
                    it.remove(f)
                    R.string.search_remove_dibs
                } else {
                    it.add(item)
                    R.string.search_add_dibs
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resid ->
                item.dibs.toggle()
                toast(resid)
            })
    }

    override fun command(cmd: String, data: Any) {
        when (cmd) {
            CMD_DIBS -> checkDibsList(data as KakaoMergeResult)
        }

        super.command(cmd, data)
    }
}
