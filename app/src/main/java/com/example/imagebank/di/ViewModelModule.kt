package com.example.imagebank.di

import androidx.lifecycle.ViewModel
import brigitte.di.dagger.module.ViewModelKey
import com.example.imagebank.MainSharedViewModel
import com.example.imagebank.ui.main.SplashViewModel
import com.example.imagebank.ui.main.dibs.DibsViewModel
import com.example.imagebank.ui.main.seach.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 6. <p/>
 */
@Module
abstract class ViewModelModule {
    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MAIN
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(vm: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainSharedViewModel::class)
    abstract fun bindMainSharedViewModel(vm: MainSharedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DibsViewModel::class)
    abstract fun bindDibsViewModel(vm: DibsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(vm: SearchViewModel): ViewModel

}
