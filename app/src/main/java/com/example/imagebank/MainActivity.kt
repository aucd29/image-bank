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
import com.example.imagebank.databinding.MainActivityBinding
import com.example.imagebank.model.remote.KakaoRestSearchService
import com.example.imagebank.ui.main.SectionsPagerAdapter
import com.example.imagebank.ui.main.SplashViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MainActivity : BaseDaggerActivity<MainActivityBinding, SplashViewModel>() {
    @Inject
    lateinit var rest: KakaoRestSearchService

    override fun onCreate(savedInstanceState: Bundle?) {
        exceptionCatcher { mLog.error("ERROR: $it") }
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        mDisposable.add(rest.image("설현")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            })
    }

    override fun attachBaseContext(newBase: Context) {
        // https://github.com/InflationX/Calligraphy
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun initViewBinding() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
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