package com.example.imagebank

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import brigitte.BaseDaggerActivity
import brigitte.exceptionCatcher
import brigitte.hideKeyboard
import com.example.imagebank.databinding.MainActivityBinding
import com.example.imagebank.model.remote.KakaoRestSearchService
import com.example.imagebank.ui.main.SectionsPagerAdapter
import com.example.imagebank.ui.main.SplashViewModel
import com.example.imagebank.ui.main.dibs.DibsFragment
import com.example.imagebank.ui.main.seach.SearchFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MainActivity : BaseDaggerActivity<MainActivityBinding, SplashViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        exceptionCatcher { mLog.error("ERROR: $it") }
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        hideKeyboard(mBinding.root)
    }

    override fun attachBaseContext(newBase: Context) {
        // https://github.com/InflationX/Calligraphy
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
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