package com.example.imagebank

import android.os.Bundle
import android.view.View
import brigitte.*
import brigitte.di.dagger.module.injectOf
import brigitte.di.dagger.module.injectOfActivity
import com.example.imagebank.databinding.MainActivityBinding
import com.example.imagebank.ui.main.SectionsPagerAdapter
import com.example.imagebank.ui.main.SplashViewModel
import com.google.android.material.tabs.TabLayout
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MainActivity : BaseDaggerActivity<MainActivityBinding, MainViewModel>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(MainActivity::class.java)
    }

    private val mTabChanged = TabSelectedCallback {
        it?.let {
            if (mLog.isDebugEnabled) {
                mLog.debug("TAB CHANGED : ${it.position}")
            }
            changeTopLayoutColor(it)
        }
    }

    @Inject lateinit var mSplashModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        exceptionCatcher { mLog.error("ERROR: $it") }
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        hideKeyboard(mBinding.root)
    }

    override fun onDestroy() {
        mBinding.tabs.removeOnTabSelectedListener(mTabChanged)

        super.onDestroy()
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

        tabs.addOnTabSelectedListener(mTabChanged)

        mSplashModel.closeSplash()

    }

    override fun initViewModelEvents() = mSplashModel.run {
        observe(closeSplashEvent) {
            visibleSplash.set(View.GONE)

            mBinding.mainContainer.removeView(mBinding.splash)
        }
    }

    private fun changeTopLayoutColor(tab: TabLayout.Tab) {
        tab.apply {
            val res = when (position) {
                0 -> R.color.colorPrimaryDark to R.color.colorPrimary
                else -> R.color.colorDarkGreen to R.color.colorGreen
            }

            changeStatusBarColor(res.first)
            mBinding.apply {
                appbar.setBackgroundResource(res.second)
                tabs.setBackgroundResource(res.second)
            }
        }
    }
}