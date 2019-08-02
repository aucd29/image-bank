package com.example.imagebank.ui.main.search

import brigitte.BaseDaggerFragment
import brigitte.color
import brigitte.di.dagger.module.injectOfActivity
import brigitte.hideKeyboard
import brigitte.widget.ITabFocus
import brigitte.widget.observeTabFocus
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.R
import com.example.imagebank.databinding.SearchFragmentBinding
import com.example.imagebank.model.remote.entity.KakaoSearchResult
import com.example.imagebank.ui.ViewController
import com.example.imagebank.ui.main.dibs.DibsViewModel
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 *
    =_ = 음...

    curl -v -X GET "https://dapi.kakao.com/v2/search/image?page=81&size=1" \
    --data-urlencode "query=설현" \
    -H "Authorization: KakaoAK e302331ef568c1a4af2053c77eef1b89"

    으로 요청 시 오류가 나옴 최대 50이니
    근데 page 1 이던 2던 같은 값 반환 함... =_ = 버그인 듯..
 *
 */

class SearchFragment @Inject constructor()
    : BaseDaggerFragment<SearchFragmentBinding, SearchViewModel>()
    , ITabFocus {
    companion object {
        private val mLog = LoggerFactory.getLogger(SearchFragment::class.java)
    }

    @Inject lateinit var mViewController: ViewController

    private lateinit var mDibsViewModel: DibsViewModel
    private lateinit var mColorModel: MainColorViewModel

    override fun layoutId() = R.layout.search_fragment

    override fun bindViewModel() {
        super.bindViewModel()

        mDibsViewModel = inject()
        mColorModel    = inject()

        mViewModel.init()
    }

    override fun initViewBinding() {
        clearFocusKeyword()

        mBinding.searchRecycler.apply {
            // 분할화면에서 오류 발생 [aucd29][2019-07-04]
            // Caused by: java.lang.IllegalArgumentException:
            // LayoutManager androidx.recyclerview.widget.StaggeredGridLayoutManager@76a8ffb is already attached to a RecyclerView
            layoutManager = mViewModel.layoutManager
        }
    }

    override fun initViewModelEvents() {
        observeTabFocus(mColorModel.focusedTabLiveData, this, R.string.tab_search)

        observe(mViewModel.mDibsList) {
            if (mLog.isDebugEnabled) {
                mLog.debug("OBSERVE DIBS LIST : ${it.size}")
            }
            mDibsViewModel.items.set(it.toMutableList())
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TAB STATUS
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onTabFocusIn() {
        mColorModel.changeColor(color(R.color.colorPrimaryDark), color(R.color.colorPrimary))
    }

    override fun onTabFocusOut() {

    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ICommandEventAware
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onCommandEvent(cmd: String, data: Any) {
        SearchViewModel.apply {
            when (cmd) {
                CMD_HIDE_KEYBOARD -> {
                    clearFocusKeyword()
                    hideKeyboard(mBinding.root)
                }
                CMD_TOP_SCROLL    -> scrollToTop()
                CMD_SHOW_DETAIL   -> showDetail(data)
            }
        }
    }

    private fun showDetail(data: Any) {
        if (data is KakaoSearchResult) {
            if (mLog.isDebugEnabled) {
                mLog.debug("SHOW DETAIL ${data.title}")
            }

            mViewController.detailFragment(data)
        }
    }

    private fun clearFocusKeyword() {
        mBinding.keyword.apply {
            clearFocus()
            isCursorVisible = false
        }
    }

    private fun scrollToTop() {
        if (mLog.isDebugEnabled) {
            mLog.debug("SCROLL TO TOP")
        }

        mBinding.apply {
            scrollview.apply {
                postDelayed({
                    scrollview.smoothScrollTo(0,0)

                }, 200)
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Module
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun contributeSearchFragmentInjector(): SearchFragment
    }
}
