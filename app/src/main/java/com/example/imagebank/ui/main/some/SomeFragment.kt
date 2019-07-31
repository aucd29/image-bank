package com.example.imagebank.ui.main.some

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import brigitte.*
import brigitte.di.dagger.module.injectOf
import brigitte.di.dagger.module.injectOfActivity
import brigitte.widget.ITabFocus
import brigitte.widget.observeTabFocus
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.R
import com.example.imagebank.common.Config
import com.example.imagebank.databinding.SomeFragmentBinding
import dagger.android.ContributesAndroidInjector
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-16 <p/>
 */

class SomeFragment @Inject constructor()
    : BaseDaggerFragment<SomeFragmentBinding, SomeViewModel>()
    , ITabFocus {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeFragment::class.java)

        private const val BANNER_DELAY = 4000L
    }

    @Inject lateinit var mConfig: Config
    private lateinit var mColorModel: MainColorViewModel

    private lateinit var mBannerViewModel: SomeBannerViewModel
    private lateinit var mChipViewModel: SomeChipViewModel
    private lateinit var mLinkModel: SomeLinkViewModel
    private lateinit var mQnaModel: SomeQnaViewModel
    private lateinit var mInfiniteBannerViewModel: SomeInfiniteBannerViewModel
    private lateinit var mGridModel: SomeGridViewModel
    private lateinit var mHorizontalModel: SomeHorizontalViewModel

    override fun layoutId() = R.layout.some_fragment

    override fun bindViewModel() {
        super.bindViewModel()

        with (mViewModelFactory) {
            mColorModel = injectOfActivity(this@SomeFragment)

            mBannerViewModel         = injectOf(this@SomeFragment)
            mChipViewModel           = injectOf(this@SomeFragment)
            mLinkModel               = injectOf(this@SomeFragment)
            mQnaModel                = injectOf(this@SomeFragment)
            mInfiniteBannerViewModel = injectOf(this@SomeFragment)
            mGridModel               = injectOf(this@SomeFragment)
            mHorizontalModel         = injectOf(this@SomeFragment)
        }

        with(mBinding) {
            bannerModel         = mBannerViewModel
            chipModel           = mChipViewModel
            linkModel           = mLinkModel
            qnaModel            = mQnaModel
            infiniteBannerModel = mInfiniteBannerViewModel
            gridModel           = mGridModel
            horModel            = mHorizontalModel
        }

        addCommandEventModels(mLinkModel, mInfiniteBannerViewModel, mGridModel, mHorizontalModel)
    }

    override fun initViewBinding() {
        mBinding.someHorizontalRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            OverScrollDecoratorHelper.setUpOverScroll(this@apply,
            OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
        }
    }

    override fun initViewModelEvents() {
        observeTabFocus(mColorModel.focusedTabLiveData, this, R.string.tab_some)

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
        mBinding.someInfiniteBanner.stopScroll()

        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        mColorModel.focusedTabLiveData.value?.let {
            if (it.text != string(R.string.tab_some)) {
                return@let
            }

            mBinding.someInfiniteBanner.startScroll(BANNER_DELAY)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TAB STATUS
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onTabFocusIn() {
        // 현재 탭일 경우만 베너를 활성화
        mBinding.apply {
            someAnimateOval.apply {
                val displayXy = displayXY()
                val top = displayXy[1] - mConfig.SCREEN.y / 2

                if (mLog.isDebugEnabled) {
                    mLog.debug("ANIMATE OVAL TOP 1 : ${displayXy[1]}, $top")
                }

                mViewModel.someAnimateOvalLayoutTop = top
            }

            someInfiniteBanner.startScroll(BANNER_DELAY)
            someBanner.currentItem.let { item ->
                someBannerIndicator.selection = item
                changeStatusColor(item)
            }
        }
    }

    override fun onTabFocusOut() {
        // 탭을 벗어날 경우 stop
        mBinding.someInfiniteBanner.stopScroll()
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
        if (mLog.isDebugEnabled) {
            mLog.debug("OPEN LINK ID : $id")
        }

//        startActivity(when (id) {
//            1 -> Intent(Intent.ACTION_CALL)
//            2 -> Intent(Intent.ACTION_SEND).apply {
//                type = "text/plain"
//                setPackage("com.kakao.talk")
//            }
//            else -> Intent(Intent.ACTION_CALL).apply {
//                type = "text/plain"
//                setPackage("com.kakao.talk")
//                // 1:1
//            }
//        })
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
