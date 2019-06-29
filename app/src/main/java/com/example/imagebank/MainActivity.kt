package com.example.imagebank

import android.os.Bundle
import android.view.View
import brigitte.BaseDaggerActivity
import brigitte.di.dagger.module.injectOf
import brigitte.di.dagger.module.injectOfActivity
import brigitte.exceptionCatcher
import brigitte.hideKeyboard
import com.example.imagebank.databinding.MainActivityBinding
import com.example.imagebank.ui.main.SectionsPagerAdapter
import com.example.imagebank.ui.main.SplashViewModel
import com.google.android.material.tabs.TabLayout
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MainActivity : BaseDaggerActivity<MainActivityBinding, MainViewModel>() {
    private val mTabChanged = object: TabLayout.OnTabSelectedListener {
        override fun onTabReselected(p0: TabLayout.Tab?) { }
        override fun onTabUnselected(p0: TabLayout.Tab?) { }
        override fun onTabSelected(tab: TabLayout.Tab?) {
        }
    }

    @Inject lateinit var mSplashModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        exceptionCatcher { mLog.error("ERROR: $it") }
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        hideKeyboard(mBinding.root)
    }

    override fun bindViewModel() {
        super.bindViewModel()

        mSplashModel = mViewModelFactory.injectOfActivity(this)
    }

    override fun initViewBinding() = mBinding.run {
        with (viewPager) {
            adapter = SectionsPagerAdapter(this@MainActivity, supportFragmentManager)
            tabs.setupWithViewPager(this)
        }

        mSplashModel.closeSplash()
    }

    override fun initViewModelEvents() = mSplashModel.run {
        observe(closeSplashEvent) {
            visibleSplash.set(View.GONE)

            mBinding.mainContainer.removeView(mBinding.splash)
        }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(MainActivity::class.java)
    }
}