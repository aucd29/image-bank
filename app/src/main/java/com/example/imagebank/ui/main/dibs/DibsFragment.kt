package com.example.imagebank.ui.main.dibs

import brigitte.BaseDaggerFragment
import com.example.imagebank.databinding.DibsFragmentBinding
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class DibsFragment : BaseDaggerFragment<DibsFragmentBinding, DibsViewModel>() {
    init {
        mViewModelScope = SCOPE_ACTIVITY
    }

    override fun initViewBinding() {
    }

    override fun initViewModelEvents() {
//        mViewModel.apply {
//            observe(listLive) {
//                if (mLog.isDebugEnabled) {
//                    mLog.debug("DIBS SIZE : ${listLive.value?.size}")
//                }
//
//                items.set(it)
//                items.notifyChange()
//            }
//        }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(DibsFragment::class.java)
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
