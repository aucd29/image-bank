package com.example.imagebank.di

import android.app.Application
import brigitte.di.dagger.module.ContextModule
import com.example.imagebank.MainApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 5. <p/>
 *
 * https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe
 * https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample/app/src/main/java/com/android/example/github
 */

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class
    , ContextModule::class
    , AppModule::class
    , ActivityBindingModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: MainApp)
}