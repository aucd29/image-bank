package com.example.imagebank

import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.core.view.GravityCompat
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import brigitte.*
import brigitte.di.dagger.module.injectOfActivity
import com.example.imagebank.common.Config
import com.example.imagebank.common.SimpleIdlingResource
import com.example.imagebank.databinding.MainActivityBinding
import com.example.imagebank.model.local.NavigationGridItem
import com.example.imagebank.model.local.NavigationItem
import com.example.imagebank.ui.main.SectionsPagerAdapter
import com.example.imagebank.ui.main.SplashViewModel
import com.example.imagebank.ui.main.navigation.NaviGridViewModel
import com.example.imagebank.ui.main.navigation.NaviMenuViewModel
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MainActivity : BaseDaggerActivity<MainActivityBinding, MainViewModel>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(MainActivity::class.java)

        private val NAVI_GRAVITY = GravityCompat.START
    }

    @Inject lateinit var mConfig: Config
    @Inject lateinit var mAdapter: SectionsPagerAdapter

    lateinit var mSplashModel: SplashViewModel
    lateinit var mNavGridModel: NaviGridViewModel
    lateinit var mNavMenuModel: NaviMenuViewModel
    lateinit var mColorModel: MainColorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        exceptionCatcher { mLog.error("ERROR: $it") }
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(NAVI_GRAVITY)) {
            mBinding.drawerLayout.closeDrawer(NAVI_GRAVITY)
            return
        }

        super.onBackPressed()
    }

    override fun bindViewModel() {
        super.bindViewModel()

        mViewModelFactory.apply {
            mSplashModel    = injectOfActivity(this@MainActivity)
            mNavGridModel   = injectOfActivity(this@MainActivity)
            mNavMenuModel   = injectOfActivity(this@MainActivity)
            mColorModel     = injectOfActivity(this@MainActivity)
        }

        mBinding.apply {
            splashModel  = mSplashModel
            navGridModel = mNavGridModel
            navMenuModel = mNavMenuModel
            colorModel   = mColorModel
        }

        mCommandEventModels.add(mNavGridModel)
        mCommandEventModels.add(mNavMenuModel)
    }

    override fun initViewBinding() = mBinding.run {
        with (viewPager) {
            adapter = mAdapter
            tabs.setupWithViewPager(this)
        }

        // navigation view width 조정
        navView.layoutWidth(mConfig.SCREEN.x * .9f)
        mSplashModel.closeSplash()
    }

    override fun initViewModelEvents() {
        observe(mColorModel.statusColor) { changeStatusBarColor(it) }

        with(mSplashModel) {
            observe(closeSplashEvent) {
                visibleSplash.set(View.GONE)

                mBinding.mainContainer.removeView(mBinding.splash)
            }
        }
    }

    override fun onCommandEvent(cmd: String, data: Any) {
        when(cmd) {
            MainViewModel.CMD_SHOW_NAVIGATION  -> {
                hideKeyboard(mBinding.root)
                mBinding.drawerLayout.openDrawer(NAVI_GRAVITY)
            }
            NaviGridViewModel.CMD_NAV_GRID_EVENT -> if (mLog.isDebugEnabled) {
                val menu = data as NavigationGridItem
                mLog.debug("CLICKED NAVIGATION GRID MENU ${menu.name}")
            }
            NaviMenuViewModel.CMD_NAV_MENU_EVENT -> if (mLog.isDebugEnabled) {
                val menu = data as NavigationItem
                mLog.debug("CLICKED NAVIGATION MENU ${menu.name}")
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TEST
    //
    ////////////////////////////////////////////////////////////////////////////////////

    // https://github.com/chiuki/espresso-samples/tree/master/idling-resource-okhttp
    @VisibleForTesting
    @Inject lateinit var okhttp: OkHttpClient
}