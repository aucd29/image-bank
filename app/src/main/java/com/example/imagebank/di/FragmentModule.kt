package com.example.imagebank.di

import com.example.imagebank.ui.detail.DetailFragment
import com.example.imagebank.ui.main.dibs.DibsFragment
import com.example.imagebank.ui.main.search.SearchFragment
import com.example.imagebank.ui.main.some.SomeFragment
import dagger.Module

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 5. <p/>
 */

@Module(includes = [
    //
    // MAIN
    //
    SearchFragment.Module::class,
    DibsFragment.Module::class,
    SomeFragment.Module::class,

    // DETAIL
    DetailFragment.Module::class
])
class FragmentModule {

}