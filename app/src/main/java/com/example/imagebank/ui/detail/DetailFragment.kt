package com.example.imagebank.ui.detail

import brigitte.BaseDaggerFragment
import com.example.imagebank.databinding.DetailFragmentBinding
import com.example.imagebank.model.remote.entity.KakaoSearchResult
import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

class DetailFragment : BaseDaggerFragment<DetailFragmentBinding, DetailViewModel>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(DetailFragment::class.java)

        const val K_ITEM = "item"
    }

    override fun initViewBinding() {
        arguments?.getSerializable(K_ITEM)?.let {
            if (it is KakaoSearchResult) {
                if (mLog.isDebugEnabled) {
                    mLog.debug(it.trace())
                }

                mBinding.item = it
            }
        }
    }

    override fun initViewModelEvents() {

    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Module
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun contributeDetailFragmentInjector(): DetailFragment
    }
}
