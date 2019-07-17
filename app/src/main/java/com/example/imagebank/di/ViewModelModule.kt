package com.example.imagebank.di

import androidx.lifecycle.ViewModel
import brigitte.di.dagger.module.ViewModelKey
import com.example.imagebank.MainViewModel
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.ui.detail.DetailViewModel
import com.example.imagebank.ui.main.SplashViewModel
import com.example.imagebank.ui.main.dibs.DibsViewModel
import com.example.imagebank.ui.main.dibs.DibsBannerViewModel
import com.example.imagebank.ui.main.navigation.NaviGridViewModel
import com.example.imagebank.ui.main.navigation.NaviMenuViewModel
import com.example.imagebank.ui.main.search.SearchViewModel
import com.example.imagebank.ui.main.some.*
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
    @ViewModelKey(MainColorViewModel::class)
    abstract fun bindStatusAndBannerModel(vm: MainColorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(vm: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(vm: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NaviGridViewModel::class)
    abstract fun bindNaviGridViewModel(vm: NaviGridViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NaviMenuViewModel::class)
    abstract fun bindNaviMenuViewModel(vm: NaviMenuViewModel): ViewModel

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DIBS
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Binds
    @IntoMap
    @ViewModelKey(DibsViewModel::class)
    abstract fun bindDibsViewModel(vm: DibsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DibsBannerViewModel::class)
    abstract fun bindBannerViewModel(vm: DibsBannerViewModel): ViewModel

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // SOME
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Binds
    @IntoMap
    @ViewModelKey(SomeViewModel::class)
    abstract fun bindSomeViewModel(vm: SomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SomeBannerViewModel::class)
    abstract fun bindSomeBannerViewModel(vm: SomeBannerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SomeChipViewModel::class)
    abstract fun bindSomeChipViewModel(vm: SomeChipViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SomeLinkViewModel::class)
    abstract fun bindSomeLinkViewModel(vm: SomeLinkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SomeQnaViewModel::class)
    abstract fun bindSomeQnaViewModel(vm: SomeQnaViewModel): ViewModel


    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DETAIL
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(vm: DetailViewModel): ViewModel

}
