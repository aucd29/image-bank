package com.example.imagebank.ui.main.some

import brigitte.BaseDaggerFragment
import brigitte.di.dagger.module.injectOf
import brigitte.di.dagger.module.injectOfActivity
import brigitte.toColor
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.databinding.SomeFragmentBinding
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-16 <p/>
 */

class SomeFragment @Inject constructor(): BaseDaggerFragment<SomeFragmentBinding, SomeViewModel>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(SomeFragment::class.java)
    }

    @Inject lateinit var mColorModel: MainColorViewModel

    @Inject lateinit var mBannerViewModel: SomeBannerViewModel
    @Inject lateinit var mChipViewModel: SomeChipViewModel
    @Inject lateinit var mLinkModel: SomeLinkViewModel

    override fun bindViewModel() {
        super.bindViewModel()

        with (mViewModelFactory) {
            mColorModel      = injectOfActivity(this@SomeFragment)

            mBannerViewModel = injectOf(this@SomeFragment)
            mChipViewModel   = injectOf(this@SomeFragment)
            mLinkModel       = injectOf(this@SomeFragment)
        }

        with(mBinding) {
            bannerModel = mBannerViewModel
            chipModel   = mChipViewModel
            linkModel   = mLinkModel
        }
    }

    override fun initViewBinding() = mBinding.run {
        pageIndicatorView.selection = someBanner.currentItem
    }

    override fun initViewModelEvents() {
        mColorModel.someFragmentFocus = {
            val it = mBinding.someBanner.currentItem

            if (mLog.isDebugEnabled) {
                mLog.debug("SOME FRAGMENT FOCUS ($it)")
            }

            changeStatusColor(it)
        }

        mBannerViewModel.pageChangeCallback.set {
            mBinding.pageIndicatorView.selection = it
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
    // Module
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun contributeSomeFragmentInjector(): SomeFragment
    }
}
