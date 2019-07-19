package com.example.imagebank.ui.main.dibs

import brigitte.BaseDaggerFragment
import brigitte.di.dagger.module.injectOf
import brigitte.di.dagger.module.injectOfActivity
import brigitte.toColor
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.common.PreloadConfig
import com.example.imagebank.databinding.DibsFragmentBinding
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject
import kotlin.math.log

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

    lateinit var mBannerViewModel: DibsBannerViewModel
    lateinit var mColorModel: MainColorViewModel

    override fun bindViewModel() {
        super.bindViewModel()

        mBannerViewModel = mViewModelFactory.injectOf(this)
        mColorModel      = mViewModelFactory.injectOfActivity(this)

        mBinding.bannerModel = mBannerViewModel
    }

    override fun initViewBinding() {

    }

    override fun initViewModelEvents() {
        mColorModel.dibsFragmentFocus = {
            val it = mBinding.dibsViewpager.currentItem

            // 이상하게 indicator 가 이걸 저장 못하네 ?
            mBinding.dibsBannerIndicator.selection = it

            if (mLog.isDebugEnabled) {
                mLog.debug("DIBS FRAGMENT FOCUS $it")
            }

            changeStatusColor(it)
        }

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
    // Module
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun contributeDibsFragmentInjector(): DibsFragment
    }
}
