package com.example.imagebank.ui.main.dibs

import androidx.viewpager.widget.ViewPager
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.module.injectOf
import brigitte.di.dagger.module.injectOfActivity
import brigitte.hideKeyboard
import com.example.imagebank.MainViewModel
import com.example.imagebank.common.PreloadConfig
import com.example.imagebank.databinding.DibsFragmentBinding
import com.example.imagebank.model.local.Banner
import com.example.imagebank.ui.main.dibs.banner.BannerViewModel
import dagger.android.ContributesAndroidInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class DibsFragment : BaseDaggerFragment<DibsFragmentBinding, DibsViewModel>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(DibsFragment::class.java)
    }

    init {
        mViewModelScope = SCOPE_ACTIVITY
    }

    @Inject lateinit var mPreConfig: PreloadConfig
    @Inject lateinit var mBannerViewModel: BannerViewModel
    @Inject lateinit var mMainViewModel: MainViewModel

    private var mItems: List<Banner>? = null

    override fun bindViewModel() {
        super.bindViewModel()

        mBannerViewModel = mViewModelFactory.injectOf(this)
        mMainViewModel   = mViewModelFactory.injectOfActivity(this)
    }

    override fun initViewBinding() {
        mBinding.apply {
            mDisposable.add(mPreConfig.bannerListSingle
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    mItems = list
                    dibsViewpager.adapter = DibsPagerAdapter(requireContext(), list, mBannerViewModel)
                })

            dibsViewpager.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    if (mLog.isDebugEnabled) {
                        mLog.debug("BANNER PAGE SELECTED : $position")
                    }

                    pageIndicatorView.setSelected(position)
                    topViewColorChange(position)
                }
            })
        }
    }

    override fun initViewModelEvents() {

    }

    private fun topViewColorChange(pos: Int) {
        mItems?.let {
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
