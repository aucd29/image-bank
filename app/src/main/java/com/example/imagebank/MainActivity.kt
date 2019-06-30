package com.example.imagebank

import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import brigitte.*
import brigitte.di.dagger.module.injectOfActivity
import com.example.imagebank.common.Config
import com.example.imagebank.databinding.MainActivityBinding
import com.example.imagebank.model.local.NavigationGridItem
import com.example.imagebank.model.local.NavigationItem
import com.example.imagebank.ui.main.SectionsPagerAdapter
import com.example.imagebank.ui.main.SplashViewModel
import com.example.imagebank.ui.main.navigation.NaviGridViewModel
import com.example.imagebank.ui.main.navigation.NaviMenuViewModel
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MainActivity : BaseDaggerActivity<MainActivityBinding, MainViewModel>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(MainActivity::class.java)

        private val NAVI_GRAVITY = GravityCompat.START
    }

    @Inject lateinit var config: Config
    @Inject lateinit var mSplashModel: SplashViewModel
    @Inject lateinit var mNavGridModel: NaviGridViewModel
    @Inject lateinit var mNavMenuModel: NaviMenuViewModel

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

        mSplashModel  = mViewModelFactory.injectOfActivity(this)
        mNavGridModel = mViewModelFactory.injectOfActivity(this)
        mNavMenuModel = mViewModelFactory.injectOfActivity(this)

        mBinding.splashModel  = mSplashModel
        mBinding.navGridModel = mNavGridModel
        mBinding.navMenuModel = mNavMenuModel

        mCommandEventModels.add(mNavGridModel)
        mCommandEventModels.add(mNavMenuModel)
    }

    override fun initViewBinding() = mBinding.run {
        with (viewPager) {
            adapter = SectionsPagerAdapter(this@MainActivity, supportFragmentManager)
            tabs.setupWithViewPager(this)
        }

        // navigation view width 조정
        navView.layoutWidth(config.SCREEN.x * .9f)
        mSplashModel.closeSplash()
    }

    override fun initViewModelEvents() = mSplashModel.run {
        observe(closeSplashEvent) {
            visibleSplash.set(View.GONE)

            mBinding.mainContainer.removeView(mBinding.splash)
        }
    }

    override fun onCommandEvent(cmd: String, data: Any) {
        when(cmd) {
            MainViewModel.CMD_STATUS_BAR_COLOR -> changeStatusBarColor(data as Int)
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
}