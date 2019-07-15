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
import io.reactivex.schedulers.Schedulers
import java.util.*



/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 *
 * 복기
 * - 일단 질문에서 의도하고자 하는 의미를 파악 못 한게 여럿 있는 듯
 *
 * - 왜 daum 을 clone 했냐는 질문을 받음 -> 네이버는 ui 바뀌고 어려워서 그런거라고 생각하면 되는 것이냐고 혼잣말 한분이 있는데
 *   물론 의도하지 않았겠지만 이분 남에게 상처주는 타입인듯 =_ =ㅋ 제일 질문도 많이 하는걸 보면 PL 인듯한 ?
 *
 * - 자료구조... OTL...
 *  > hash code ??? 질문의 의도 파악 안됨
 *  > 질문자분은 gdg 에서 뵌듯한 느낌? 성격좋아보임 일단 다이아임!! 먼가 같은 팀 이되었다면 편히 물어볼수 있을 듯한 느낌이랄까?
 *
 * - test 에서 AndroidViewModel 을 이용 관련해서 질문을 받았는데 mock 을 질문하고 싶어서 그런건지?, 개인적으로 android 에서 pure 한 코드 이외는
 *   androidTest 로 동작 시키는게 나을듯 싶은데 +_+? 내가 생각하는것과 먼가 다른게 있는걸까?
 *   > 질문자분는 전형적인 학자 스타일로 보임 조용히 핵심을 찌르는 스타일
 *
 * - single live event 를 쓰게 된 이유? 같은걸 질문 받았는데 딱히 이유가 안 떠올라 해당 클래스의 주석을 보여드렸다.. 마치 장금이가 홍시맛이 나서 홍시라고 이야기 한것 같은 심정으로
 *
 * - Rx 는 책을 사서 한번 보긴해야될듯 주먹 구구식으로 한듯 (일단 구매)
 *
 * - activity lifecycle 중 onStart 사용처? -> 개인적으로 사용하지 않았음 -> https://developer.android.com/guide/components/activities/activity-lifecycle
 * - save Instance State 관련 ? -> 개인적으로는 shared preference 를 이용한다고 함 -> http://egloos.zum.com/skyswim42/v/3925726
 *
 * - camera api 1 을 써봄 camerax 나온걸 이야기 했다. camera2 를 했다고 하면 이외의 질문이 있었을까? 2 의 경우 몇몇 메소드가 min version 이 지나치게
 *   높아 꺼려지긴했지만 flashlight 의 경우 camera1 / 2 를  분기 처리 하긴 했다. 그걸로 camera2 를 써보았다라고 하긴 뭐해서...
 *  > 질문자 분이 "잘생김" + 차분함
 *
 * - rsa 관련 간단한 설명을 함,
 *  > 인증서 관련해서 알고 있는지 문의가 왔으나 해보지 않음을 이야기 함 - https://opentutorials.org/course/228/4894
 *  > https 구조에 관련하여 질문 받음 - https://opentutorials.org/course/228/4894
 *  > 설명을 보는데 회사에서 사용하는 암호화 방식과 동일 함 대칭키 공개키 조합
 *
 * - http cache control 관련 질문 받음
 *  > 개인적으로 프로토콜 스팩을 중요시 생각 했지만, 막상 질문 받으니 하.나.도. 기억 안남
 *  (퍼센트 인코딩에서 GET 과 POST 가 다른 딱 하나의 단어같은거 물어봤으면 대답할수 있었을터인데!!!)
 *  (chucked 와 일반 방식의 차이점이라던지 OTL..)
 *    https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Cache-Control
 *
 * - atomic 변수들에대한 질문 받음 -> sync 관련해서 사용은 해봤다고는 했는데 어디에 사용했는지는 기억안난다고 이야기 함... 나중에 생각해보니 single live event 도 atomic boolean !!
 * - 책 & 인터넷 중 어디에서 자료를 참고하냐라는 질문을 받음 -> 주로 인터넷 참고하고 최신기술을 찾아보기 위해서는 인터넷이 먼저라고 이야기 하고 구글블로그를 제일 많이 참고한다고 함
 * - 소스나 문서 쓰는걸로 보면 잘 적는데 자기 소개는 왜케 성의 없게 적었냐고 들음 -> 팀장님이였고 서글서글한 느낌? 이야길 많이 안하셔서 잘은 모름
 *  > 자기 소개 같은거 적는게 좀 어려워한다고 이야기 함.. 뭐랄까 내가 하고 싶다고해서 되는것도 아니니.. 흠.. 너무 현실적인건가?
 *
 * - 팀 코드룰 지킬꺼냐는 질문에 '당근입니다.' 이건 너무 당연한거라서 전 회사의 예를 들긴했지만 전 회사에서 몇몇은 이를 지키지 않아 한명이 전체 코드를 리펙하는 일이 있긴 했다.
 *   어쩌면 이건 피할수 없는 것일수도? 라는 생각이 들기도 한다.
 *
 * - 질문하고 싶은게 있나? 라고 하여
 *  > 올 코틀린으로 갈 수 있나? 라는 질문에 '아니요 금융권이라 모든 부분을 테스트해야 되서 힘듭니다. 라고 답변 받았다.'
 *  > 일부 동일한 UI 가 각각 다른 형태를 가지는 경우에 대해서 왜 그런지 설명을 요청 했고 '개발 기간에 따라 다르게 작성되었다고 들은거 같다 (정확히 기억이 나지 않음)'
 *  > 현재 팀원은 14명으로 들었고 20명 까지 늘릴 예정이라고 한다.
 *
 * - 근데 왜 2차에서 OTL.... 뭐가 심기를 건드린걸까?
 *  > 회사 그만둔 이유 각각 이야기
 *  > 그 다음 부터는 CTO 분이 질문에 대한 답변으로 계속 꼬투리 잡는 식으로 질문을 이어 나감
 *  > 하이브리드+네이티브 방식 플랫폼을 만들었다 -> 왜 네이티브로 그냥하면 되는데 그리했나? -> 초기에는 하이브리드만 있었는데 반응성이 느려서 네이티브 추가함을 이야기함
 *  > 주로 플랫폼을 작업해서 native ui 능력이 떨어져 보인다 -> 저도 그리 생각해서 개인 프로젝트를 좀 했다 -> 뭐했냐? -> daum clone -> 앱 시연 ->
 *    살짝 당황하시며 -> 아래 웹뷰죠 -> 네
 *  > 다른 한분이 다음 개발자도 많은데 왜 그걸 만들었냐 -> CTO 분이 '그게 아니라 .. ' 라고 이야기 하시며 대신 설명하심 -> 여기서 내가 살짝 당황
 *  > 중앙에 젤로 높으신분으로 보이는 분이 갑자기 커트하시고 -> 현재 연봉은? -> 그대로 알려드리니 -> 엥? 하시면서 그옆에 분이 원천이야기 하라고 ->
 *  > 원천을 생각해보지 않아서 근사치 이야기 드리고 끝
 *  > 나오는데 출구에 버튼 누르는걸 못보고 헤메다가 나옴
 *
 * - 좋은 인력들과 논의하면서 개발해보고 싶었는데, 혼자하는게 힘든데 말이지 이젠 꿈도 희망도 =_ = 저 멀리 ~ 어쩌니?
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
//    val gridCount        = ObservableInt(2)

    val visibleProgress  = ObservableInt(View.GONE)
    val visibleTopScroll = ObservableInt(View.GONE)

    // FIXME live data 도 UI 에 바로 반영 될수 있도록 기능 추가 되었다고 함 (좋은 정보를 알게 됨!)
    // FIXME https://developer.android.com/jetpack/androidx/releases/lifecycle
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
            if (mLog.isTraceEnabled) {
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

//            ioThread {
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

                                mIsImageApiEnd     = image.meta?.is_end ?: false    // 빠진 부분 추가
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
                    .subscribeOn(Schedulers.io())       // FIXME UI 가 버벅여서 ioThread 로 우회했었는데 먼가 빌드가 제대로 되지 않았던 상황이였었는지, 인터뷰때 이렇게 해도 잘 동작했다.
                    .observeOn(Schedulers.io())         // FIXME map 에도 io 적용
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
                            it.find { f -> dibs.thumbnail == f.thumbnail }?.fillStar()
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
//                        mDp.add(Flowable.fromIterable(mCachingList)
//                            .subscribeOn(Schedulers.newThread())
//                            .map(::preloadImage)
//                            .delay(1, TimeUnit.SECONDS)
//                            .subscribe({
//                                if (mLog.isDebugEnabled) {
//                                    mLog.debug("CACHED : $it")
//                                }
//                            }))

                    }, {
                        if (mLog.isDebugEnabled) {
                            it.printStackTrace()
                        }

                        visibleProgress.gone()
                        mLog.error("ERROR: ${it.message}")
                        it.message?.let(::snackbar)
                    }))
            }
//        } ?: snackbar(R.string.search_pls_insert_keyword)
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

    private fun preloadImage(url: String): String {
        //http://bumptech.github.io/glide/doc/getting-started.html#background-threads
        //https://stackoverflow.com/questions/37964187/preload-multiple-images-with-glide

        if (mLog.isDebugEnabled) {
            mLog.debug("PRELOAD IMAGE $url")
        }

        Glide.with(app)
            .load(url)
            .preload()

        return url
    }

    override fun command(cmd: String, data: Any) {
        when (cmd) {
            CMD_ANIM_STAR -> checkDibsList(data as KakaoSearchResult)
            else          -> super.command(cmd, data)
        }
    }
}
