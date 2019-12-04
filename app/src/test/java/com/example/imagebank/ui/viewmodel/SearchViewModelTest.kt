package com.example.imagebank.ui.viewmodel

import android.graphics.Point
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import brigitte.*
import com.example.imagebank.common.Config
import com.example.imagebank.model.remote.KakaoRestSearchService
import com.example.imagebank.model.remote.entity.KakaoImageSearch
import com.example.imagebank.model.remote.entity.KakaoVClipSearch
import com.example.imagebank.ui.main.search.SearchViewModel
import com.example.imagebank.util.*
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import com.example.imagebank.R

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-24 <p/>
 */

@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest: BaseRoboViewModelTest<SearchViewModel>() {
    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = SearchViewModel(app, api, config)
    }

    @Test
    fun networkErrorTest() {
        mockDisableNetwork()

        viewmodel.apply {
            mockObserver<Pair<String, Any>>(commandEvent).apply {
                search(1)

                verifyChanged(
                    SearchViewModel.CMD_HIDE_KEYBOARD to -1,
                    ICommandEventAware.CMD_SNACKBAR to app.string(R.string.network_invalid_connectivity))
            }
        }
    }

    @Test
    fun emptyKeywordError() {
        mockEnableNetwork()

        viewmodel.apply {
            mockObserver<Pair<String, Any>>(commandEvent).apply {
                keyword.set("")
                search(1)

                verifyChanged(
                    SearchViewModel.CMD_HIDE_KEYBOARD to -1,
                    ICommandEventAware.CMD_SNACKBAR to app.string(R.string.search_pls_insert_keyword))
            }
        }
    }

    @Test
    fun invalidImageSearch() {
        mockEnableNetwork()
        mockApi(RESPONSE_ERROR)

        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)

            items.get()?.size.assertEquals(3)
        }
    }

    @Test
    fun invalidVclipSearch() {
        mockEnableNetwork()
        mockApi(responseVclip = RESPONSE_ERROR)

        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)

            items.get()?.size.assertEquals(3)
        }
    }

    @Test
    fun invalidAllSearch() {
        mockEnableNetwork()
        mockApi(RESPONSE_ERROR, RESPONSE_ERROR)

        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)

            items.get()?.size.assertEquals(0)
        }
    }

    @Test
    fun endImageSearch() {
        mockEnableNetwork()
        mockApi(RESPONSE_IMAGE.replace(""""is_end":false""", """"is_end":true"""))

        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)

            mIsImageApiEnd.assertTrue()
            mIsVclipApiEnd.assertFalse()
        }
    }

    @Test
    fun endVclipSearch() {
        mockEnableNetwork()
        mockApi(responseVclip = RESPONSE_VCLIP.replace(""""is_end":false""", """"is_end":true"""))

        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)

            mIsImageApiEnd.assertFalse()
            mIsVclipApiEnd.assertTrue()
        }
    }

    @Test
    fun endAllSearch() {
        mockEnableNetwork()
        mockApi(RESPONSE_IMAGE.replace(""""is_end":false""", """"is_end":true"""),
            RESPONSE_VCLIP.replace(""""is_end":false""", """"is_end":true"""))

        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)

            mIsImageApiEnd.assertTrue()
            mIsVclipApiEnd.assertTrue()
        }
    }

    @Test
    fun defaultSearch() {
        mockEnableNetwork()
        mockApi()

        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            search(1)

            items.get()?.size.assertEquals(6)

            val item = items.get()!!
            val firstItem = item[0]
            val endItem   = item[item.size - 1]
            (firstItem.unixtime >= endItem.unixtime).assertTrue()

            // dibs check
            command(SearchViewModel.CMD_ANIM_STAR, firstItem)
            firstItem.anim.get()?.endListener?.invoke(null)
            mDibsList.value?.size.assertEquals(1)

            command(SearchViewModel.CMD_ANIM_STAR, endItem)
            endItem.anim.get()?.endListener?.invoke(null)
            mDibsList.value?.size.assertEquals(2)

            command(SearchViewModel.CMD_ANIM_STAR, firstItem)
            firstItem.anim.get()?.endListener?.invoke(null)
            mDibsList.value?.size.assertEquals(1)
        }
    }

    @Test
    fun clickedSearchButtonFromSoftKeyboard() {
        mockEnableNetwork()
        mockApi()

        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            editorAction.get()?.invoke(SEARCH_KEYWORD)

            items.get()?.size.assertEquals(6)
        }
    }

    @Test
    fun scrolling() {
        mockEnableNetwork()
        mockApi()

        val mockLayoutManager = spy(StaggeredGridLayoutManager(
            SearchViewModel.V_TAB_SPANCOUNT,
            StaggeredGridLayoutManager.VERTICAL))

        mockLayoutManager.findLastVisibleItemPositions(null)
            .mockReturn(arrayOf(5,6).toIntArray())


        viewmodel.apply {
            keyword.set(SEARCH_KEYWORD)
            layoutManager = mockLayoutManager
            search(1)

            items.get()?.size.assertEquals(6)

            Thread.sleep(1500)  // 로딩 완료 후 스크롤
            mockApi(RESPONSE_IMAGE.replace(""""is_end":false""", """"is_end":true"""),
                RESPONSE_VCLIP.replace(""""is_end":false""", """"is_end":true"""), page = "2")

            scrollListener.get()?.callback?.invoke(0, 4000, true)
            mPage.assertEquals(2)
            mIsImageApiEnd.assertTrue()
            mIsVclipApiEnd.assertTrue()

            // mIsImageApiEnd, mIsVclipApiEnd 이 true 면 더 검색이 안됨
            Thread.sleep(1500)
            scrollListener.get()?.callback?.invoke(0, 6000, true)
            mPage.assertEquals(2)
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
    }

    override fun initMock() {
        super.initMock()

        mockReactiveX()
        mockConfig()
        mockNetwork()
        mockApi()
    }

    @Mock lateinit var config: Config

    private fun mockConfig() {
        config.SCREEN.mockReturn(Point(1080, 1920))
    }

    @Mock private lateinit var api: KakaoRestSearchService
    private fun mockApi(responseImage: String = RESPONSE_IMAGE, responseVclip: String = RESPONSE_VCLIP, page: String = "1") {
        val image = Observable.just(responseImage.jsonParse<KakaoImageSearch>())
        val vclip = Observable.just(responseVclip.jsonParse<KakaoVClipSearch>())

        api.image(SEARCH_KEYWORD, page).mockReturn(image)
        api.vclip(SEARCH_KEYWORD, page).mockReturn(vclip)
    }
}