package com.example.imagebank.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.imagebank.R
import com.example.imagebank.common.Config
import com.example.imagebank.model.remote.KakaoRestSearchService
import com.example.imagebank.ui.main.search.SearchViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-24 <p/>
 */

@RunWith(JUnit4::class)
class SearchViewModelTest {
    @Mock lateinit var api: KakaoRestSearchService
    @Mock lateinit var config: Config

    lateinit var viewModel: SearchViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mockContext()


        viewModel = SearchViewModel(context, api, config)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MOCK
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
//        const val COLOR_PRIMARY      = 11
//        const val COLOR_PRIMARY_DARK = COLOR_PRIMARY + 1
//        const val COLOR_ACCENT       = COLOR_PRIMARY_DARK + 1
//        const val COLOR_WHITE        = COLOR_ACCENT + 1
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var context: Application
    private fun mockContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(Mockito.mock(Resources::class.java))

//        Mockito.`when`(context.getColor(R.color.colorPrimary)).thenReturn(COLOR_PRIMARY)
//        Mockito.`when`(context.getColor(R.color.colorPrimaryDark)).thenReturn(COLOR_PRIMARY_DARK)
//        Mockito.`when`(context.getColor(R.color.colorAccent)).thenReturn(COLOR_ACCENT)
//        Mockito.`when`(context.getColor(android.R.color.white)).thenReturn(COLOR_WHITE)
//
//        Mockito.`when`(context.resources.getColor(R.color.colorPrimary)).thenReturn(COLOR_PRIMARY)
//        Mockito.`when`(context.resources.getColor(R.color.colorPrimaryDark)).thenReturn(COLOR_PRIMARY_DARK)
//        Mockito.`when`(context.resources.getColor(R.color.colorAccent)).thenReturn(COLOR_ACCENT)
//        Mockito.`when`(context.resources.getColor(android.R.color.white)).thenReturn(COLOR_WHITE)
    }
}