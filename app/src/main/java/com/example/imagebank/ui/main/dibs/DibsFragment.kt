package com.example.imagebank.ui.main.dibs

import brigitte.BaseDaggerFragment
import brigitte.di.dagger.module.injectOf
import brigitte.di.dagger.module.injectOfActivity
import brigitte.toColor
import brigitte.widget.ITabFocus
import brigitte.widget.observeTabFocus
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.R
import com.example.imagebank.databinding.DibsFragmentBinding
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class DibsFragment @Inject constructor()
    : BaseDaggerFragment<DibsFragmentBinding, DibsViewModel>()
    , ITabFocus {
    companion object {
        private val mLog = LoggerFactory.getLogger(DibsFragment::class.java)
    }

    init {
        mViewModelScope = SCOPE_ACTIVITY
    }

    private lateinit var mBannerViewModel: DibsBannerViewModel
    private lateinit var mColorModel: MainColorViewModel

    override fun layoutId() = R.layout.dibs_fragment

    override fun bindViewModel() {
        super.bindViewModel()

        mBannerViewModel = mViewModelFactory.injectOf(this)
        mColorModel      = mViewModelFactory.injectOfActivity(this)

        mBinding.bannerModel = mBannerViewModel
    }

    override fun initViewBinding() {

    }

    override fun initViewModelEvents() {
        observeTabFocus(mColorModel.focusedTabLiveData, this, R.string.tab_dibs)

        mBannerViewModel.pageChangeCallback.set {
            mBinding.dibsBannerIndicator.selection = it
            changeStatusColor(it)
        }
    }

    private fun changeStatusColor(selection: Int) {
        mBannerViewModel.items.get()?.let { list ->
            val current = list[selection]
            mColorModel.changeColor(current.statusColor.toColor(),
                current.bgcolor.toColor())
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TAB STATUS
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onTabFocusIn() {
        mBinding.dibsViewpager.currentItem.let { item ->
            mBinding.dibsBannerIndicator.selection = item
            changeStatusColor(item)
        }
    }

    override fun onTabFocusOut() {

    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Module
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun contributeDibsFragmentInjector(): DibsFragment
    }
}
