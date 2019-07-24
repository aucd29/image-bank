package com.example.imagebank.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import brigitte.ICommandEventAware
import brigitte.findLastVisibleItemPosition
import brigitte.jsonParse
import brigitte.systemService
import com.example.imagebank.R
import com.example.imagebank.common.Config
import com.example.imagebank.model.remote.KakaoRestSearchService
import com.example.imagebank.model.remote.entity.KakaoImageSearch
import com.example.imagebank.model.remote.entity.KakaoVClipSearch
import com.example.imagebank.ui.main.search.SearchViewModel
import com.example.imagebank.util.testCommand
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import junit.framework.Assert.assertTrue
import org.mockito.Mockito.*
import org.slf4j.LoggerFactory


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-24 <p/>
 */

@RunWith(JUnit4::class)
class SearchViewModelTest {
    lateinit var viewModel: SearchViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()
        viewModel = SearchViewModel(context, api, config)
    }

    @Test
    fun networkError() {
        mockDisableNetwork()

        val events = arrayOf<Pair<String, Any>>(
            SearchViewModel.CMD_HIDE_KEYBOARD to -1,
            ICommandEventAware.CMD_SNACKBAR to INVALID_CONNECTIVITY)

        viewModel.apply { testCommand(events) { search(1) } }
    }

    @Test
    fun emptyKeywordError() {
        mockEnableNetwork()

        val events = arrayOf<Pair<String, Any>>(
            SearchViewModel.CMD_HIDE_KEYBOARD to -1,
            ICommandEventAware.CMD_SNACKBAR to SEARCH_PLS_INSERT_KEYWORD)

        viewModel.apply { testCommand(events) {
            keyword.set("")
            search(1)
        } }
    }

    @Test
    fun invalidImageSearch() {
        mockEnableNetwork()
        mockApi(RESPONSE_ERROR)

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)
            assertEquals(items.get()?.size ?: 0, 3)
        }
    }

    @Test
    fun invalidVclipSearch() {
        mockEnableNetwork()
        mockApi(responseVclip = RESPONSE_ERROR)

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)
            assertEquals(items.get()?.size ?: 0, 3)
        }
    }

    @Test
    fun invalidAllSearch() {
        mockEnableNetwork()
        mockApi(RESPONSE_ERROR, RESPONSE_ERROR)

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)
            assertEquals(items.get()?.size ?: 0, 0)
        }
    }

    @Test
    fun endImageSearch() {
        mockEnableNetwork()
        mockApi(RESPONSE_IMAGE.replace(""""is_end":false""", """"is_end":true"""))

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)
            assertEquals(mIsImageApiEnd, true)
            assertEquals(mIsVclipApiEnd, false)
        }
    }

    @Test
    fun endVclipSearch() {
        mockEnableNetwork()
        mockApi(responseVclip = RESPONSE_VCLIP.replace(""""is_end":false""", """"is_end":true"""))

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)
            assertEquals(mIsImageApiEnd, false)
            assertEquals(mIsVclipApiEnd, true)
        }
    }

    @Test
    fun endAllSearch() {
        mockEnableNetwork()
        mockApi(RESPONSE_IMAGE.replace(""""is_end":false""", """"is_end":true"""),
            RESPONSE_VCLIP.replace(""""is_end":false""", """"is_end":true"""))

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)
            assertEquals(mIsImageApiEnd, true)
            assertEquals(mIsVclipApiEnd, true)
        }
    }

    @Test
    fun defaultSearch() {
        mockEnableNetwork()
        mockApi()

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)
            assertEquals(items.get()?.size ?: 0, 6)

            val item = items.get()!!
            val firstItem = item[0]
            val endItem   = item[item.size - 1]

            assertTrue(firstItem.unixtime >= endItem.unixtime)

            // dibs check
            command(SearchViewModel.CMD_ANIM_STAR, firstItem)
            firstItem.anim.get()?.endListener?.invoke(null)
            assertTrue(mDibsList.value?.size == 1)

            command(SearchViewModel.CMD_ANIM_STAR, endItem)
            endItem.anim.get()?.endListener?.invoke(null)
            assertTrue(mDibsList.value?.size == 2)

            command(SearchViewModel.CMD_ANIM_STAR, firstItem)
            firstItem.anim.get()?.endListener?.invoke(null)
            assertTrue(mDibsList.value?.size == 1)
        }
    }

    @Test
    fun clickedSearchButtonFromSoftKeyboard() {
        mockEnableNetwork()
        mockApi()

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            editorAction.get()?.invoke(SEARCH_KEYWORD)

            assertEquals(items.get()?.size ?: 0, 6)
        }
    }

    @Test
    fun scrolling() {
        mockEnableNetwork()
        mockApi()

        val mockLayoutManager = spy(StaggeredGridLayoutManager(
            SearchViewModel.V_TAB_SPANCOUNT,
            StaggeredGridLayoutManager.VERTICAL))

        `when`(mockLayoutManager.findLastVisibleItemPositions(null)).thenReturn(arrayOf(5,6).toIntArray())

        viewModel.apply {
            keyword.set(SEARCH_KEYWORD)
            layoutManager = mockLayoutManager
            search(1)
            assertEquals(items.get()?.size ?: 0, 6)
            assertEquals(mPage, 1)

            Thread.sleep(1500)  // 로딩 완료 후 스크롤
            mockApi(RESPONSE_IMAGE.replace(""""is_end":false""", """"is_end":true"""),
                RESPONSE_VCLIP.replace(""""is_end":false""", """"is_end":true"""), page = "2")

            scrollListener.get()?.callback?.invoke(0, 4000, true)
            assertEquals(mPage, 2)
            assertEquals(mIsImageApiEnd, true)
            assertEquals(mIsVclipApiEnd, true)

            // mIsImageApiEnd, mIsVclipApiEnd 이 true 면 더 검색이 안됨
            Thread.sleep(1500)
            scrollListener.get()?.callback?.invoke(0, 6000, true)
            assertEquals(mPage, 2)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MOCK
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        const val RESPONSE_IMAGE = """{"documents":[{"collection":"blog","datetime":"2018-12-16T09:40:08.000+09:00","display_sitename":"티스토리","doc_url":"http://filternews.tistory.com/225","height":602,"image_url":"http://cfile23.uf.tistory.com/image/99CC573C5C159EE22F823F","thumbnail_url":"https://search4.kakaocdn.net/argon/130x130_85_c/9d3F8Bwbei3","width":400},{"collection":"blog","datetime":"2018-12-16T09:40:08.000+09:00","display_sitename":"티스토리","doc_url":"http://filternews.tistory.com/225","height":500,"image_url":"http://cfile2.uf.tistory.com/image/9956173D5C159EE23173EB","thumbnail_url":"https://search4.kakaocdn.net/argon/130x130_85_c/C9TzArGy0xR","width":400},{"collection":"blog","datetime":"2018-12-16T09:40:08.000+09:00","display_sitename":"티스토리","doc_url":"http://filternews.tistory.com/225","height":500,"image_url":"http://cfile4.uf.tistory.com/image/99C56E365C159EE32E6705","thumbnail_url":"https://search4.kakaocdn.net/argon/130x130_85_c/4Key6MXTFf9","width":400}],"meta":{"is_end":false,"pageable_count":3993,"total_count":645946}}"""
        const val RESPONSE_VCLIP = """{"documents":[{"author":"M2","datetime":"2018-05-31T19:12:47.000+09:00","play_time":251,"thumbnail":"https://search3.kakaocdn.net/argon/138x78_80_pr/9MY6kuvWARe","title":"[MPD직캠] 에이오에이 설현 직캠 4K '빙글뱅글(Bingle Bangle)' (AOA Seol Hyun FanCam) | @MCOUNTDOWN_2018.5.31","url":"http://www.youtube.com/watch?v=njmYkmMBrxs"},{"author":"M2","datetime":"2016-05-19T19:28:00.000+09:00","play_time":196,"thumbnail":"https://search1.kakaocdn.net/argon/138x78_80_pr/583QISb9pYq","title":"[MPD직캠] 에이오에이 설현 직캠 Good Luck AOA Seol Hyun Fancam @엠카운트다운_160519","url":"http://www.youtube.com/watch?v=hgvshqzQke4"},{"author":"JTBC Entertainment","datetime":"2018-06-02T09:37:26.000+09:00","play_time":176,"thumbnail":"https://search3.kakaocdn.net/argon/138x78_80_pr/LBKmSQcQMwY","title":"[선공개] 절친 김신영\u0026설현\u0026지민(Shin young\u0026Seol hyun\u0026Ji min)에게 심판받는 김희철(Hee chul)♨ 아는 형님(Knowing bros) 130회","url":"http://www.youtube.com/watch?v=hyhEQmlD_KI"}],"meta":{"is_end":false,"pageable_count":800,"total_count":8041}}"""
        const val RESPONSE_ERROR = """{"errorType":"InvalidArgument","message":"page is more than max"}"""

        const val SEARCH_KEYWORD = "설현"

        const val INVALID_CONNECTIVITY       = "invalid_connectivity"
        const val SEARCH_PLS_INSERT_KEYWORD  = "search_pls_insert_keyword"
    }

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    private fun initMock() {
        MockitoAnnotations.initMocks(this)
        mockContext()
        mockConfig()
        mockConnectivityManager()
        mockReactive()
        mockApi()
    }

    @Mock private lateinit var context: Application
    private fun mockContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock(Resources::class.java))
        `when`(context.getString(R.string.network_invalid_connectivity)).thenReturn(INVALID_CONNECTIVITY)
        `when`(context.getString(R.string.search_pls_insert_keyword)).thenReturn(SEARCH_PLS_INSERT_KEYWORD)
    }

    lateinit var config: Config
    private fun mockConfig() {
        config = Config(context)
        `when`(config.SCREEN).thenReturn(Point(1080, 1920))
    }

    @Mock private lateinit var connectivityManager: ConnectivityManager
    @Mock private lateinit var network: Network
    @Mock private lateinit var networkInfo: NetworkInfo
    private fun mockConnectivityManager() {
        `when`<ConnectivityManager>(context.systemService()).thenReturn(connectivityManager)
        `when`(connectivityManager.allNetworks).thenReturn(arrayOf(network))
        `when`(connectivityManager.getNetworkInfo(network)).thenReturn(networkInfo)
        `when`(networkInfo.state).thenReturn(NetworkInfo.State.CONNECTED)
        `when`(connectivityManager.allNetworkInfo).thenReturn(arrayOf(networkInfo))
    }

    private fun mockEnableNetwork() {
        `when`(networkInfo.state).thenReturn(NetworkInfo.State.CONNECTED)
    }

    private fun mockDisableNetwork() {
        `when`(networkInfo.state).thenReturn(NetworkInfo.State.DISCONNECTED)
    }

    private fun mockReactive() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Mock private lateinit var api: KakaoRestSearchService
    private fun mockApi(responseImage: String = RESPONSE_IMAGE, responseVclip: String = RESPONSE_VCLIP, page: String = "1") {
        val image = Observable.just(responseImage.jsonParse<KakaoImageSearch>())
            .subscribeOn(Schedulers.io())
        val vclip = Observable.just(responseVclip.jsonParse<KakaoVClipSearch>())
            .subscribeOn(Schedulers.io())

        `when`(api.image(SEARCH_KEYWORD, page)).thenReturn(image)
        `when`(api.vclip(SEARCH_KEYWORD, page)).thenReturn(vclip)
    }
}