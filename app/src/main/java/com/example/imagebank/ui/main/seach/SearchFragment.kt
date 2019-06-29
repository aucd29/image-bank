package com.example.imagebank.ui.main.seach

import androidx.core.widget.NestedScrollView
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.module.injectOfActivity
import brigitte.hideKeyboard
import brigitte.toast
import brigitte.toggle
import com.example.imagebank.R
import com.example.imagebank.databinding.SearchFragmentBinding
import com.example.imagebank.model.remote.entity.KakaoMergeResult
import com.example.imagebank.ui.main.dibs.DibsViewModel
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
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
//        mBinding.scrollview.apply {
//            setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
//                if (mLog.isDebugEnabled) {
//                    mLog.debug("SCROLL INFO ")
//                }
//            })
//        }
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
                CMD_DIBS            -> mDibsViewModel.toggleDibs(mViewModel.dibsList)
                CMD_HIDE_KEYBOARD   -> hideKeyboard(mBinding.root)
                CMD_TOP_SCROLL      -> mBinding.scrollview.scrollTo(0, 0)
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
