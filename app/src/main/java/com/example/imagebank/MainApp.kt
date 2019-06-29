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
 *
 * - 이미지를 검색해서 보관함에 수집하는 안드로이드 앱을 작성해 주세요.
　　 (적혀있지 않은 내용은 자유롭게 작성하시면 됩니다.)

　 - 검색은 키워드 하나에 이미지 검색과 동영상 검색을 동시에 사용, 두 검색 결과를 합친 리스트를 사용합니다.
    　　구체적인 사용 필드는 아래와 같습니다.

　　　　· 이미지 검색 API ( https://developer.kakao.com/docs/restapi/search#이미지-검색 ) 의 thumbnail_url 필드
　　　　· 동영상 검색 API ( https://developer.kakao.com/docs/restapi/search#동영상-검색 ) 의 thumbnail 필드
　　　　· 두 검색 결과를 datetime 필드를 이용해 최신순으로 나열하여 출력합니다.

　 - UI는 ViewPager와 TabLayout을 활용하여 아래와 같이 구성합니다.
　　　　· 탭1 : 검색 결과
　　　　　＊ 검색된 이미지 리스트가 나타납니다. 리스트에서 특정 이미지 선택하여 '내 보관함' 으로 저장할 수 있습니다.
　　　　· 탭2 : 내 보관함
　　　　　＊ 검색 결과에서 보관했던 이미지들이 보입니다.
　　　　　＊ 보관한 이미지 리스트는 DB 등으로 보관되지 않아도 됩니다. (앱 종료 시에는 사라져도 무방합니다.)

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

        if (mLog.isInfoEnabled) {
            mLog.info("== IMAGE BANK START ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})  ==")
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

