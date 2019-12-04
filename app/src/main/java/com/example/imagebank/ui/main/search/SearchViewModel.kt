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
 * - 왜 daum 을 clone 했냐는 질문을 받음 -> just!! 하이브리드 앱 대상으로 제일 할만했었음
 *
 * - 자료구조... OTL...
 *  > hash code ??? 질문이 무엇인지 파악 안됨
 *
 * - test 에서 AndroidViewModel 을 이용 관련해서 질문을 받았는데 mock 을 질문하고 싶어서 그런건지?, 개인적으로 android 에서 pure 한 코드 이외는
 *   androidTest 로 동작 시켰는데 찾아보니 test 에서 하는게 빠르다고 해서 추가 함, network mock 만하다가 일반 mock 하는데 이거 회사 코드도
 *   다 바꿔야 되나 고민 중
 *
 * - diff util 의 경우 일반적으로 구현하는것과 다른거 같다고 해서 일단 찾아봤는데 diff util 이 알려지기 전 초기에 == 으로 비교해서 이를 참조
 *   하였지만 현재는 id 를 생성해서 이를 비교하는 형태가 주를 이루는거 같아서 리펙 함
 *
 * - single live event 를 쓰게 된 이유? 같은걸 질문 받았는데 딱히 이유가 안 떠올라 해당 클래스의 주석을 보여드렸다..
 *   마치 장금이가 홍시맛이 나서 홍시라고 이야기 한것 같은 심정으로
 *
 * - Rx 는 책을 사서 한번 보긴해야될듯 주먹 구구식으로 한듯 (일단 구매)
 *
 * - activity lifecycle 중 onStart 사용처? -> 개인적으로 사용하지 않았음 ->
 *   https://developer.android.com/guide/components/activities/activity-lifecycle
 *
 * - save Instance State 관련 ? -> 개인적으로는 shared preference 를 이용한다고 함 -> http://egloos.zum.com/skyswim42/v/3925726
 *
 * - imageview 에 contentDescription 써봤는지 -> 아니요 -> 문자열 바인딩 하는건 알고 있었는데 찾아보니 정말 그게 다더라 마치 html 의 alt 느낌?
 *
 * - recyclerview model 에서 adapter 에 layoutid 설정하는게 string 인 이유에 대해 물었고 invoke 하기 위함이였다고 이야기 했는데
 *   banner view model 을 일반화 하면서 보니 ViewDataBinding 으로 받되 setModel, setItem 을 그냥 호출할 수도 있어 코드 리펙 함
 *
 * - webview 와 okhttp 간 cookie 처리를 어케 하나? 라는 문의가 있었고
 *  cookie manager 로 하는데 webview 의 onpagefinished 에서 갱신 (flush) 시킵니다. 라고 한 뒤 이야기가 끝날 무렵
 *  전에 cookie sync manager 가 있었는데 이게 deprecated 되어서 분기 처리해서 사용하고 있다 라고 추가로 덧을 붙였는데
 *  cookie sync manager 는 단순 과거 저장이 되지 않아서 만들어진 것 아니냐? 라는 이야기 와 함께 나는 동공 지진을 일으키고..
 *  무슨 의미로 이야기를 하는지 몰라 당황한 가운데 그 이야기가 왜 나왔는지 잘은 모르겠으나 쿠키의 매커니즘은 서버에서 헤더에
 *  Set-Cookie: 블라블라; 해서 전달하면 클라가 쿠키 매니저를 이용해 setCookie 를 호출
 *  해서 보관하고 expired 타임이나 서버에서 쿠키를 지우면 지워지는 그런건데, 괜히 cookie sync manager 이야기 했다 싶었다. =_ =ㅋ 결론적으로 이제
 *  쿠키 싱크매니저는 deprecated 되었고 이와 관련된 코드는 내 소스 CookieGroup.kt 에서 확인 가능하다.
 *
 * - camera api 1 을 써봄 camerax 나온걸 이야기 했다. camera2 를 했다고 하면 이외의 질문이 있었을까? 2 의 경우 몇몇 메소드가 min version 이 지나치게
 *   높아 꺼려지긴했지만 flashlight 의 경우 camera1 / 2 를  분기 처리 하긴 했다. 그걸로 camera2 를 써보았다라고 하긴 뭐해서...
 *
 * - rsa 관련 간단한 설명을 함,
 *  > 인증서 관련해서 알고 있는지 문의가 왔으나 해보지 않음을 이야기 함 - https://opentutorials.org/course/228/4894
 *  > https 구조에 관련하여 질문 받음 - https://opentutorials.org/course/228/4894
 *  > 설명을 보는데 회사에서 사용하는 암호화 방식과 동일 함 대칭키 공개키 조합
 *
 * - ormlite 과 realm 을 사용했는데 둘간의 차이? 관련하여 묻길래 realm 이 빠른건 만 알고 있고 테스트는 안해봤다라고 이야기 함
 * - ormlite 의 경우 암호화 디비가 없냐 라고 물었고 암호화 디비관련 검색 했을 땐 realm 만 검색되어 작업을 했기 때문에 답변으로
 *   ormlite 관련해서는 안 찾아봤다고 이야기 했다. 궁금해서 이번에 찾아보니 있음, 근데 ormlite 가 ios 에 없을 터이니
 *   realm 으로 진행했던게 더 나을듯 싶다.
 *   https://stackoverflow.com/questions/10041131/android-encryption-decryption-with-ormlite-possible
 *   https://github.com/Andoctorey/ormlite-sqlcipher
 *
 * - http cache control 관련 질문 받음
 *  > 개인적으로 프로토콜 스팩을 중요시 생각 했지만, 막상 질문 받으니 하.나.도. 기억 안남
 *  (퍼센트 인코딩에서 GET 과 POST 가 다른 딱 하나의 단어같은거 물어봤으면 대답할수 있었을터인데!!!)
 *  (chucked 와 일반 방식의 차이점이라던지 OTL..)
 *    https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Cache-Control
 *
 * - atomic 변수들에대한 질문 받음 -> sync 관련해서 사용은 해봤다고는 했는데 어디에 사용했는지는 기억안난다고 이야기 함...
 * 나중에 생각해보니 single live event 도 atomic boolean !!
 *
 * - 책 & 인터넷 중 어디에서 자료를 참고하냐라는 질문을 받음 -> 주로 인터넷 참고하고 최신기술을 찾아보기 위해서는 인터넷이 먼저라고 이야기 하고
 * 구글블로그를 제일 많이 참고한다고 함
 *
 * - 소스나 문서 쓰는걸로 보면 잘 적는데 자기 소개는 왜케 성의 없게 적었냐고 들음 ->
 *  > 자기 소개 같은거 적는게 좀 어려워한다고 이야기 함.. 뭐랄까 내가 하고 싶다고해서 되는것도 아니니.. 흠.. 너무 현실적인건가?
 *
 * - 팀 코드룰 지킬꺼냐는 질문에 '당근입니다.' 이건 너무 당연한거라서 전 회사의 예를 들긴했지만 전 회사에서 몇몇은 이를 지키지 않아
 * 한명이 전체 코드를 리펙하는 일이 있긴 했다. 어쩌면 이건 피할수 없는 것일수도? 라는 생각이 들기도 한다.
 *
 * - 어느게 가장 우수하냐 라는 질문을 잘못 받아드려 여기 있는 사람들 중에 어느게 가장 우수한가로 잘못 인식하여 (내가 미쳤지 =_ =)
 *   답변을 제대로 못했다. (그런 질문을 할일은 없었겠지... =_ =) 되세김질을 하자면
 *   개발자가 편하게 비즈니스 코드에 집중할 수 있도록 베이스 코드를 설계하고 공통 모듈을 설계하는걸 가장 오래 해왔고
 *   가장 잘하는 부분이라고 하고 싶다. (개인적으로는 js, cpp, kotlin, java 구분 없이 코딩 가능 하다.
 *   물론 cpp 가 요즘 너무 많이 바뀌어서 최신 버전은 모름)
 *
 * - 팀에 와서 무얼 할수 있겠냐? - 거즘 추천으로만 회사를 옮겨다니다 보니 정말 오랬만에 받은 질문이라 머랄까?
 *   멀 할수 있을까? 라는 문제와 지금에 팀 구성원을 모르는 문제 때문에 제대로 답변을 못했다. 그냥 인터뷰에서는
 *   질러도 될거 같긴한데 이놈에 성격 =_ = 문제다. 뭐든 할수 있다고 해야 했나? 그렇다고 너무 과장하긴 싫고 이건 다음 트라이전까지는 고민해볼 문제다.
 *
 * - 질문하고 싶은게 있나? 라고 하여
 *  > 올 코틀린으로 갈 수 있나? 라는 질문에 '아니요 금융권이라 모든 부분을 테스트해야 되서 힘듭니다. 라고 답변 받았다.'
 *  > 일부 동일한 UI 가 각각 다른 형태를 가지는 경우에 대해서 왜 그런지 설명을 요청 했고 '개발 기간에 따라 다르게 작성되었다고 들은거 같다 (정확히 기억이 나지 않음)'
 *  > databinding + mvvm 이 보안상 위험하지 않나? 라는 질문에 절대 그렇지 않다 라고 답변 받았으나, 개인적으로 보안성 심사를 받았을때 해커가 런타임으로
 *    앱을 실시간으로 해킹하는걸 봐온지라 살짝 위험할거 같기도 하다..
 *    (해커들이 네트워크 중간단을 뒤지는게 아니고 뷰단을 뒤져서 메모리를 건들면서 작업하기에 아이디건 패스워드건 다 뒤저보더란..)
 *  > 현재 팀원은 14명으로 들었고 20명 까지 늘릴 예정이라고 한다.
 *  > 돌아돌아 들은 이야기로는 1차에 6명을 뽑았고 티오는 2명 남았다고 한다 2차에 아마 2명이 추가 될 듯 한데 먼가 좀 아쉽다. 에공..
 *
 * - 근데 왜 2차에서 OTL.... 뭐가 심기를 건드린걸까?
 *  > 회사 그만둔 이유 각각 이야기
 *  > 그 다음 부터는 CTO 분이 질문에 대한 답변으로 계속 꼬투리 잡는 식으로 질문을 이어 나감
 *  > 하이브리드+네이티브 방식 플랫폼을 만들었다 -> 왜 네이티브로 그냥하면 되는데 그리했나? ->
 *  초기에는 하이브리드만 있었는데 반응성이 느려서 네이티브 추가함을 이야기함
 *  > 주로 플랫폼을 작업해서 native ui 능력이 떨어져 보인다 -> 저도 그리 생각해서 개인 프로젝트를 좀 했다 -> 뭐했냐? -> daum clone -> 앱 시연 ->
 *    살짝 당황하시며 -> 아래 웹뷰죠 -> 네
 *  > 다른 한분이 다음 개발자도 많은데 왜 그걸 만들었냐 -> CTO 분이 '그게 아니라 .. ' 라고 이야기 하시며 대신 설명하심 ->
 *  여기서 내가 살짝 당황 (왜 두분이 싸우세요..)
 *  > 중앙에 젤로 높으신분으로 보이는 분이 갑자기 커트하시고 -> 현재 연봉은? -> 그대로 알려드리니 -> 엥? 하시면서 그옆에 분이 원천이야기 하라고 ->
 *  > 원천을 생각해보지 않아서 근사치 이야기 드리고 끝
 *  > 나오는데 출구에 버튼 누르는걸 못보고 헤메다가 나옴
 *
 * - 좋은 인력들과 논의하면서 개발해보고 싶었는데, 혼자하는게 힘든데 말이지 이젠 꿈도 희망도 =_ = 저 멀리 ~ 어쩌니?
 *
 * 복기 후 보이는 것들
 * - 자료구조 -_-;; php -> cpp -> java 로 넘어가서 그런지 기초가 없네?
 *
 * - 지나지게 낮아진 자존감 (뭘 해도 잘할 수 있다!! 그리 이야기 못한 느낌?, 이 회사에 와서 자존감만 낮아진건가?, 예전엔 안그랬는데 흠;;)
 *
 * - 다른곳에 가서 무얼 할 수 있는지?
 *   > 내가 잘하는건 베이스쪽 설계를 그려 그거 기반으로 공통적인 화면을 구성하게 할 수 있는 점 그걸로 인한 생산성 향상,
 *     이를 이용하는 개발자는 비즈니스 로직 개발에 집중해 좀더 좋은 코드를 생산해 내도록 해줄수 있다라고 이야기 하고 싶다.
 *
 * - test case 를 하고는 있지만 표출하지 않는 점? 뭐 잘 모르긴 한다. 해봐야 espresso, network mock 정도
 *
 * - platform / framework 위주로 개발하여 ui 가 약할 것 이라는 질문에 그렇게 생각하실수 있겠지만 작업해왔던것이 ui 와 동떨어져 있는게 아닌
 *   한번 더 생각하여 코드를 재활용성 있게 만들기 때문에 더 나은 효과를 낼수 있음을 이야기 해야할 것 같다. 이와 관련되어.
 *   recycler group 의 코드를 예로 들어봐야할 까?, 각각 구현하던걸 데이터에 인터페이스만 상속하면 그냥 됩니다! 라고
 *
 * - 언제 다시 트라이를 해볼수 있을지는 모르겠지만...반성하자! 팀장님이 어제 주신 책상에 놓여있는 '코틀린 마이크로서비스 개발' 책을 보며 복잡/미묘한 감정이다.
 */
class SearchViewModel @Inject constructor(application: Application,
    private val api: KakaoRestSearchService,
    private val config: Config
) : RecyclerViewModel<KakaoSearchResult>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SearchViewModel::class.java)

        const val V_TAB_SPANCOUNT   = 2

        // TODO Unknown pattern character 'X' 오류나서 변경 [aucd29][2019-07-02]
        const val DATE_FORMAT1       = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        const val DATE_FORMAT2       = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

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

    lateinit var layoutManager: StaggeredGridLayoutManager

    init {
        mDibsList.value = arrayListOf()
        mThreshold      = 6
        initAdapter(R.layout.search_item)
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

                if (mLog.isDebugEnabled) {
                    mLog.debug("FIND LAST VISIBLE ITEM POSITION $pos")
                }

                val next = isNextLoad(pos)
                if (mLog.isDebugEnabled) {
                    mLog.debug("IS NEXT : $next")
                }

                if (next) {
                    if (mLog.isDebugEnabled) {
                        mLog.debug("SEARCH NEXT")
                    }

                    search(mPage.inc())
                }
            }
        })
    }

    fun init() {
        layoutManager = StaggeredGridLayoutManager(V_TAB_SPANCOUNT,
            StaggeredGridLayoutManager.VERTICAL)
    }

    fun isNetworkConnected() = app.isNetworkConntected()

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

        if (mLog.isDebugEnabled) {
            mLog.debug("PAGE : $mPage")
        }

        if (!isNetworkConnected()) {
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
                                        it.datetime.toUnixTime(DATE_FORMAT1, DATE_FORMAT2),
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
                                it.datetime.toUnixTime(DATE_FORMAT1, DATE_FORMAT2),
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
                .subscribeOn(Schedulers.io())       
                // FIXME UI 가 버벅여서 ioThread 로 우회했었는데 먼가 빌드가 제대로 되지 않았던 상황이였었는지, 인터뷰때 이렇게 해도 잘 동작했다.
                // FIXME 낚임... 원래 알던대로 그냥 subscribe on 이 io 면 하위도 그냥 io 스레드에서 돔 흑;;; .observeOn(Schedulers.io())
                // 원래 알던대로 없어도 되었네 ㅋ 낚였....observeOn(Schedulers.io())         
                // FIXME map 에도 io 적용
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

        item.anim.set(ToLargeAlphaAnimParams(5f, endListener = {
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
