package com.example.imagebank.ui.main.some

import android.content.Intent
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

    lateinit var mColorModel: MainColorViewModel

    lateinit var mBannerViewModel: SomeBannerViewModel
    lateinit var mChipViewModel: SomeChipViewModel
    lateinit var mLinkModel: SomeLinkViewModel
    lateinit var mQnaModel: SomeQnaViewModel
    lateinit var mInfiniteBannerViewModel: SomeInfiniteBannerViewModel

    override fun bindViewModel() {
        super.bindViewModel()

        with (mViewModelFactory) {
            mColorModel = injectOfActivity(this@SomeFragment)

            mBannerViewModel         = injectOf(this@SomeFragment)
            mChipViewModel           = injectOf(this@SomeFragment)
            mLinkModel               = injectOf(this@SomeFragment)
            mQnaModel                = injectOf(this@SomeFragment)
            mInfiniteBannerViewModel = injectOf(this@SomeFragment)
        }

        with(mBinding) {
            bannerModel         = mBannerViewModel
            chipModel           = mChipViewModel
            linkModel           = mLinkModel
            qnaModel            = mQnaModel
            infiniteBannerModel = mInfiniteBannerViewModel
        }
    }

    override fun initViewBinding() {

    }

    override fun initViewModelEvents() {
        mColorModel.someFragmentFocus = {
            val it = mBinding.someBanner.currentItem

            // 이상하게 indicator 가 이걸 저장 못하네 ?
            mBinding.someBannerIndicator.selection = it

            if (mLog.isDebugEnabled) {
                mLog.debug("SOME FRAGMENT FOCUS ($it)")
            }

            changeStatusColor(it)
        }

        mBannerViewModel.pageChangeCallback.set {
            mBinding.someBannerIndicator.selection = it
            changeStatusColor(it)
        }

        val size = mInfiniteBannerViewModel.items.get()?.size ?: 0
        mBinding.someInfiniteBannerIndicator.count = size
        mInfiniteBannerViewModel.pageChangeCallback.set {
            mBinding.someInfiniteBannerIndicator.selection = it % size
        }
    }

    private fun changeStatusColor(selection: Int) {
        mBannerViewModel.items.get()?.let { list ->
            val current = list[selection]
            mColorModel.changeColor(current.statusColor.toColor(),
                current.bgcolor.toColor())
        }
    }

    override fun onPause() {
        mBinding.someInfiniteBanner.stopAutoScroll()

        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        mBinding.someInfiniteBanner.runAutoScroll(4000)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onCommandEvent(cmd: String, data: Any) {
        when (cmd) {
            SomeLinkViewModel.CMD_CLICK -> openLink(data as Int)
        }
    }

    private fun openLink(id: Int) {
        startActivity(when (id) {
            1 -> Intent(Intent.ACTION_CALL)
            2 -> Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.kakao.talk")
            }
            else -> Intent(Intent.ACTION_CALL).apply {
                type = "text/plain"
                setPackage("com.kakao.talk")
                // 1:1
            }
        })
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
