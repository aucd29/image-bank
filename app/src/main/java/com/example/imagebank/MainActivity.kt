package com.example.imagebank

import android.os.Bundle
import android.view.View
import brigitte.BaseDaggerActivity
import brigitte.exceptionCatcher
import brigitte.hideKeyboard
import com.example.imagebank.databinding.MainActivityBinding
import com.example.imagebank.ui.main.SectionsPagerAdapter
import com.example.imagebank.ui.main.SplashViewModel
import org.slf4j.LoggerFactory

class MainActivity : BaseDaggerActivity<MainActivityBinding, SplashViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        exceptionCatcher { mLog.error("ERROR: $it") }
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        hideKeyboard(mBinding.root)
    }

    override fun initViewBinding() = mBinding.run {
        with (viewPager) {
            adapter = SectionsPagerAdapter(this@MainActivity, supportFragmentManager)
            tabs.setupWithViewPager(this)
        }

        mViewModel.closeSplash()
    }

    override fun initViewModelEvents() = mViewModel.run {
        observe(closeSplashEvent) {
            visibleSplash.set(View.GONE)

            mBinding.mainContainer.removeView(mBinding.splash)
        }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(MainActivity::class.java)
    }
}