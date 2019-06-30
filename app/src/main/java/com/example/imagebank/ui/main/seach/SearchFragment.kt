package com.example.imagebank.ui.main.seach

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.module.injectOfActivity
import brigitte.hideKeyboard
import com.example.imagebank.databinding.SearchFragmentBinding
import com.example.imagebank.ui.main.dibs.DibsViewModel
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 *
    =_ = 음...

    curl -v -X GET "https://dapi.kakao.com/v2/search/image?page=81&size=1" \
    --data-urlencode "query=아이유" \
    -H "Authorization: KakaoAK e302331ef568c1a4af2053c77eef1b89"

    으로 요청 시 오류가 나옴 최대 50이니
    근데 page 1 이던 2던 같은 값 반환 함... =_ = 버그인 듯..
 *
 */

class SearchFragment : BaseDaggerFragment<SearchFragmentBinding, SearchViewModel>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(SearchFragment::class.java)
    }

    lateinit var mDibsViewModel: DibsViewModel

    override fun bindViewModel() {
        super.bindViewModel()

        mDibsViewModel = mViewModelFactory.injectOfActivity(this)
    }

    override fun initViewBinding() {
        mBinding.recycler.apply {
            layoutManager = mViewModel.layoutManager
//            (layoutManager as StaggeredGridLayoutManager).isAutoMeasureEnabled = false
//            setHasFixedSize(false)
//            isNestedScrollingEnabled = false
        }
    }

    override fun initViewModelEvents() {
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ICommandEventAware
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onCommandEvent(cmd: String, data: Any) {
        SearchViewModel.apply {
            when (cmd) {
                CMD_DIBS            -> mDibsViewModel.toggleDibs(mViewModel.mDibsList)
                CMD_HIDE_KEYBOARD   -> hideKeyboard(mBinding.root)
                CMD_TOP_SCROLL      -> scrollToTop()
            }
        }
    }

    private fun scrollToTop() {
        if (mLog.isDebugEnabled) {
            mLog.debug("SCROLL TO TOP")
        }

        mBinding.apply {
            recycler.scrollToPosition(0)
            scrollview.apply {
                postDelayed({ scrollY = 0 }, 200)
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