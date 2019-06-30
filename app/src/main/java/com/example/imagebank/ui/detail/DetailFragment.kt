package com.example.imagebank.ui.detail

import brigitte.BaseDaggerFragment
import com.example.imagebank.databinding.DetailFragmentBinding
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

class DetailFragment : BaseDaggerFragment<DetailFragmentBinding, DetailViewModel>() {
    override fun initViewBinding() {
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
