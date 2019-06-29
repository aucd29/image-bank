package com.example.imagebank.ui.main.seach

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
                CMD_DIBS            -> if (data is KakaoMergeResult) toggleDibs(data)
                CMD_HIDE_KEYBOARD   -> hideKeyboard(mBinding.root)
            }
        }
    }

    private fun toggleDibs(item: KakaoMergeResult) {
        if (mLog.isDebugEnabled) {
            mLog.debug("TOGGLE DIBS")
        }

        mViewModel.dibsList.apply {
            val f = find { f -> f.thumbnail == item.thumbnail }
            val strid = if (f != null) {
                remove(f)
                R.string.search_remove_dibs
            } else {
                add(item)
                R.string.search_add_dibs
            }

            item.dibs.toggle()
            toast(strid)

            mDibsViewModel.toggleDibs(this)
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
