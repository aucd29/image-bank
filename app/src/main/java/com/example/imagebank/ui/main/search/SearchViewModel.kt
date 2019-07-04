package com.example.imagebank.ui.main.search

import android.annotation.SuppressLint
import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import brigitte.*
import brigitte.bindingadapter.ToLargeAlphaAnimParams
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.imagebank.R
import com.example.imagebank.common.Config
import com.example.imagebank.model.remote.KakaoRestSearchService
import com.example.imagebank.model.remote.entity.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory
import javax.inject.Inject
import io.reactivex.functions.BiFunction
import java.util.*


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class SearchViewModel @Inject constructor(application: Application,
    private val api: KakaoRestSearchService,
    private val config: Config
) : RecyclerViewModel<KakaoSearchResult>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SearchViewModel::class.java)

        const val V_TAB_SPANCOUNT   = 2

        // TODO Unknown pattern character 'X' 오류나서 변경 [aucd29][2019-07-02]
//        const val DATE_FORMAT       = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        const val DATE_FORMAT       = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

        const val CMD_ANIM_STAR     = "cmd-anim-star"
        const val CMD_HIDE_KEYBOARD = "cmd-hide-keyboard"
        const val CMD_TOP_SCROLL    = "cmd-top-scroll"
        const val CMD_SHOW_DETAIL   = "cmd-show-detail"

        const val V_SORT_ACCURACY = "accuracy"
        const val V_SORT_RECENCY  = "recency"
    }

    val keyword          = ObservableField<String>("설현")
    val sort             = ObservableInt(R.string.search_sort_accuracy)
    val totalCount       = ObservableField<String>()

    val editorAction     = ObservableField<(String?) -> Boolean>()   // editor 에서 done 버튼 선택 시
    val scrollListener   = ObservableField<ScrollChangeListener>()
    val gridCount        = ObservableInt(2)

    val visibleProgress  = ObservableInt(View.GONE)
    val visibleTopScroll = ObservableInt(View.GONE)

    val mDibsList    = MutableLiveData<ArrayList<KakaoSearchResult>>()
    val mCachingList = arrayListOf<String>()
    var mPage        = 1
    val mDp          = CompositeDisposable()

    // api 에서 더 이상 데이터가 없음을 알려줄 경우 api call 을 진행 하지 않기 위한 플래그
    var mIsImageApiEnd = false
    var mIsVclipApiEnd = false

    // FIXME API 오류로 데이터의 첫번째가 동일하면 이를 중복 데이터로 본다.
    var mKakaoImageResult: KakaoImageResult? = null

    lateinit var layoutManager:StaggeredGridLayoutManager

    init {
        mDibsList.value = arrayListOf()
        mThreshold      = 6
        initAdapter("search_item")
        adapter.get()?.isScrollToPosition = false

        editorAction.set {
            if (mLog.isDebugEnabled) {
                mLog.debug("EVENT DONE")
            }

            search(1)
            true
        }

        // nested scroll 의 scroll 정보를 전달 받음
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

            // nested scroll 이라 recycler view 에 add scroll listener 에 넣는게 의미가 없어
            // 이곳에서 처리
            if (isBottom && mPage <= 50 && !(mIsVclipApiEnd && mIsImageApiEnd)) {
                val pos = layoutManager.findLastVisibleItemPosition()
                if (isNextLoad(pos)) {
                    search(mPage.inc())
                }
            }
        })
    }

    fun init() {
        layoutManager = StaggeredGridLayoutManager(V_TAB_SPANCOUNT,
            StaggeredGridLayoutManager.VERTICAL)
    }

    @SuppressLint("StringFormatMatches")
    fun search(p: Int) {
        if (p == 1) {
            mIsImageApiEnd    = false
            mIsVclipApiEnd    = false
            mKakaoImageResult = null
        }

        mPage        = p
        mDataLoading = true
        command(CMD_HIDE_KEYBOARD)

        if (!app.isNetworkConntected()) {
            snackbar(R.string.network_invalid_connectivity)
            return
        }

        keyword.get()?.let {
            if (it.isEmpty()) {
                snackbar(R.string.search_pls_insert_keyword)
                return@let
            }

            visibleProgress.visible()

            if (mLog.isDebugEnabled) {
                mLog.debug("SEARCH $it")
            }

            var totalSearchedCount = 0

            ioThread {
                // 검색은 키워드 하나에 이미지 검색과 동영상 검색을 동시에 사용,
                mDp.add(Observable.zip(api.image(it, mPage.toString(), sortOption()),
                    api.vclip(it, mPage.toString(), sortOption()),
                    BiFunction { image: KakaoImageSearch, vclip: KakaoVClipSearch ->
                        // 두 검색 결과를 합친 리스트를 사용합니다.
                        val result = arrayListOf<KakaoSearchResult>()

                        // FIXME 현재 kakao api 버그로 페이징에 문제가 존재 image 의 경우 항상 같은 데이터가 들어온다.
                        // FIXME 데이터를 제외 해볼까 싶었는데 정력 낭비로 생각하고 1페이지만 데이터를 참조하고
                        // FIXME 버그가 수정되면 살리는 형태로 하도록 수정
                        if (image.message == null && !mIsImageApiEnd) {
                            // FIXME 임시코드
                            // FIXME 페이징 시 동일한 데이터가 들어오면 이를 무시하도록 처리 한다.
                            if (mKakaoImageResult == null) {
                                mKakaoImageResult = image.documents?.get(0)
                            } else {
                                val tmp = image.documents?.get(0)
                                if (tmp != null && mKakaoImageResult!!.image_url == tmp.image_url) {
                                    // API 오류로 1번만 호출하고 종료 시킨다.
                                    mIsImageApiEnd = true
                                }
                            }

                            if (!mIsImageApiEnd) {
                                // 2018-12-16T09:40:08.000+09:00
                                image.documents?.forEach {
                                    it.image_url?.let { img -> mCachingList.add(img) }
                                    it.thumbnail_url?.let { thumbnail ->
                                        result.add(KakaoSearchResult(thumbnail, it.datetime,
                                            it.datetime.toUnixTime(DATE_FORMAT),
                                            it.image_url, it.display_sitename
                                        ))
                                    }
                                }

                                totalSearchedCount = image.meta?.total_count ?: 0
                            }
                        } else {
                            mLog.error("ERROR: ${image.message}")
                        }

                        if (vclip.message == null) {
                            vclip.documents?.forEach {
                                result.add(KakaoSearchResult(it.thumbnail, it.datetime,
                                    it.datetime.toUnixTime(DATE_FORMAT),
                                    it.url, it.title,
                                    KakaoSearchResult.T_VCLIP))
                            }

                            mIsVclipApiEnd = vclip.meta?.is_end ?: false
                            totalSearchedCount += image.meta?.total_count ?: 0
                        } else {
                            mLog.error("ERROR: ${vclip.message}")
                        }

                        result
                    })
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
                            it.addAll(0, items.get()!!)
                        }

                        // 찜에 넣어둔 경우 이를 검사해서 활성화 시켜준다.
                        mDibsList.value?.forEach { dibs ->
                            it.find { f -> dibs.thumbnail == f.thumbnail }?.dibs?.set(R.drawable.ic_star_yellow_24dp)
                        }

                        it
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (mLog.isDebugEnabled) {
                            mLog.debug("SEARCHED DATA SET!! ${it.size}")
                        }

                        totalCount.set(app.getString(R.string.search_total_searched_count,
                            totalSearchedCount.numberFormat()))

                        items.set(it)
                        mDp.add(delay {
                            mDataLoading = false
                            visibleProgress.gone()
                        })

                        // 의도한대로 되지 않음 ;; 삭제
//                        // caching
//                        mDp.add(Flowable.fromIterable(mCachingList)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(Schedulers.io())
//                            .delay(300, TimeUnit.MILLISECONDS)
//                            .map(::preloadImage)
//                            .subscribe())

                    }, {
                        if (mLog.isDebugEnabled) {
                            it.printStackTrace()
                        }

                        visibleProgress.gone()
                        mLog.error("ERROR: ${it.message}")
                        it.message?.let(::snackbar)
                    }))
            }
        } ?: snackbar(R.string.search_pls_insert_keyword)
    }

    fun toggleSort() {
        sort.set(if (sort.get() == R.string.search_sort_accuracy) {
            R.string.search_sort_recency
        } else {
            R.string.search_sort_accuracy
        })

        search(1)
    }

    // url 예 https://search4.kakaocdn.net/argon/130x130_85_c/9d3F8Bwbei3
    private fun parseThumbnailSize(str: String) =
        if (str.contains("130x130")) {
            130 to 130
        } else if (str.contains("138x78")) {
            138 to 138
        } else {
            -1 to -1
        }

    private fun sortOption() = if (sort.get() == R.string.search_sort_accuracy) V_SORT_ACCURACY else V_SORT_RECENCY

    private fun toggleDibsItem(item: KakaoSearchResult) =
        mDibsList.value?.let {
            val f = it.find { f -> f.thumbnail == item.thumbnail }
            if (f != null) {
                if (mLog.isDebugEnabled) {
                    mLog.debug("REMOVE DIBS")
                }

                it.remove(f)
            } else {
                if (mLog.isDebugEnabled) {
                    mLog.debug("ADD DIBS")
                }

                it.add(item)
            }

            it
        }

    private fun checkDibsList(item: KakaoSearchResult) {
        if (item.isFilledStar()) {
            vibrate(longArrayOf(0, 1, 100, 1), 1)
        } else {
            vibrate(1)
        }

        item.anim.set(ToLargeAlphaAnimParams(5f, endListener = { _, _ ->
            mDibsList.value = toggleDibsItem(item)
            item.toggleDibs()
        }))
    }

    private fun preloadImage(url: String) {
        // https://stackoverflow.com/questions/37964187/preload-multiple-images-with-glide
        // https://bumptech.github.io/glide/int/recyclerview.html

        if (mLog.isDebugEnabled) {
            mLog.debug("PRELOAD IMAGE $url")
        }

        Glide.with(app)
            .load(url)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .submit()
    }

    override fun command(cmd: String, data: Any) {
        when (cmd) {
            CMD_ANIM_STAR -> checkDibsList(data as KakaoSearchResult)
            else          -> super.command(cmd, data)
        }
    }
}