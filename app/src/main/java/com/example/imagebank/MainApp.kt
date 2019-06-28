package com.example.imagebank

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.example.imagebank.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.github.inflationx.viewpump.ViewPump
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 */

class MainApp : MultiDexApplication(), HasActivityInjector {
    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var viewPump: ViewPump


    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        ViewPump.init(viewPump)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // HasActivityInjector
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun activityInjector() = activityInjector

    companion object {
        private val mLog = LoggerFactory.getLogger(MainApp::class.java)
    }
}

