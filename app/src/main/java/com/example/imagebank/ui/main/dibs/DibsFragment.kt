package com.example.imagebank.ui.main.dibs

import brigitte.BaseDaggerFragment
import brigitte.di.dagger.module.injectOf
import brigitte.di.dagger.module.injectOfActivity
import com.example.imagebank.MainViewModel
import com.example.imagebank.common.PreloadConfig
import com.example.imagebank.databinding.DibsFragmentBinding
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class DibsFragment @Inject constructor() : BaseDaggerFragment<DibsFragmentBinding, DibsViewModel>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(DibsFragment::class.java)
    }

    init {
        mViewModelScope = SCOPE_ACTIVITY
    }

    @Inject lateinit var mPreConfig: PreloadConfig
    @Inject lateinit var mMainViewModel: MainViewModel
    @Inject lateinit var mBannerViewModel: DibsBannerViewModel

    override fun bindViewModel() {
        super.bindViewModel()

        mBannerViewModel = mViewModelFactory.injectOf(this)
        mMainViewModel   = mViewModelFactory.injectOfActivity(this)

        mBinding.bannerModel = mBannerViewModel
    }

    override fun initViewBinding() {

    }

    override fun initViewModelEvents() {
        observe(mBannerViewModel.pageChangedLive) {
            mBinding.pageIndicatorView.setSelected(it)
            topViewColorChange(it)
        }
    }

    private fun topViewColorChange(pos: Int) {
        mPreConfig.bannerList.let {
            val current = it[pos]
            mMainViewModel.apply {
                bgStatusLastColor  = mBannerViewModel.convertColor(current.statusColor)
                bgTopViewLastColor = mBannerViewModel.convertColor(current.bgcolor)

                command(MainViewModel.CMD_STATUS_BAR_COLOR, bgStatusLastColor!!)
                bgTopViewColor.set(bgTopViewLastColor!!)
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
        abstract fun contributeDibsFragmentInjector(): DibsFragment
    }
}
