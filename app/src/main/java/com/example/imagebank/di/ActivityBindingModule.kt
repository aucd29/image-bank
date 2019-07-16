package com.example.imagebank.di

import android.view.Window
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.imagebank.MainActivity
import com.example.imagebank.ui.ViewController
import com.example.imagebank.ui.main.SectionsPagerAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 6. <p/>
 */

@Module(includes = [])
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [
        FragmentModule::class,
        MainActivityModule::class
    ])
    abstract fun contributeMainActivity(): MainActivity

    @Singleton
    @Binds
    abstract fun bindViewController(controller: ViewController): Any

    @Singleton
    @Binds
    abstract fun bindSectionsPagerAdapter(adapter: SectionsPagerAdapter): FragmentPagerAdapter

//    @Binds
//    abstract fun bindDibsPagerAdapter(adapter: DibsPagerAdapter): PagerAdapter
}

// https://stackoverflow.com/questions/48533899/how-to-inject-members-in-baseactivity-using-dagger-android
@Module
class MainActivityModule {
    @Provides
    fun provideFragmentManager(activity: MainActivity): FragmentManager =
        activity.supportFragmentManager

    @Provides
    fun provideWindow(activity: MainActivity): Window =
        activity.window
}
