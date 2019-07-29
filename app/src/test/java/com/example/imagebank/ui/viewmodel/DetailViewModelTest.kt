package com.example.imagebank.ui.viewmodel

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Html
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import brigitte.html
import brigitte.numberFormat
import com.example.imagebank.MainColorViewModel
import com.example.imagebank.R
import com.example.imagebank.ui.detail.DetailViewModel
import com.example.imagebank.util.setFinalStatic
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-29 <p/>
 */

@RunWith(JUnit4::class)
class DetailViewModelTest {
    lateinit var viewModel: DetailViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewModel = DetailViewModel(context)
    }

    @Config(minSdk = Build.VERSION_CODES.N)
    @TargetApi(Build.VERSION_CODES.N)
    @Test
    fun testConvertAmount1FromN() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), Build.VERSION_CODES.N)
        testConvertAmount1()
    }

    @Config(minSdk = Build.VERSION_CODES.N)
    @TargetApi(Build.VERSION_CODES.N)
    @Test
    fun testConvertAmount2FromN() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), Build.VERSION_CODES.N)
        testConvertAmount2()
    }

    @Config(minSdk = Build.VERSION_CODES.M)
    @TargetApi(Build.VERSION_CODES.M)
    @Test
    fun testConvertAmount1FromM() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), Build.VERSION_CODES.M)
        testConvertAmount1()
    }

    @Config(minSdk = Build.VERSION_CODES.M)
    @TargetApi(Build.VERSION_CODES.M)
    @Test
    fun testConvertAmount2FromM() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), Build.VERSION_CODES.M)
        testConvertAmount2()
    }

    @Test
    fun testConvertBalance() {
        val amount = 1000000
        val converted = viewModel.convertBalance(amount)
        val result = "${amount.numberFormat()}$SZ_WON"

        assertEquals(converted, result)
    }

    private inline fun testConvertAmount1() {
        val amount = 1000000
        val converted = viewModel.convertAmount(amount)
        val result = """<b><font color="#087BCD">${amount.numberFormat()}$SZ_WON</font></b>""".html()

        assertEquals(converted, result)
    }

    private inline fun testConvertAmount2() {
        val amount = -11695
        val converted = viewModel.convertAmount(amount)
        val result = """<b><font color="#000000">${amount.numberFormat()}$SZ_WON</font></b>""".html()

        assertEquals(converted, result)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MOCK
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        const val SZ_WON = "원"
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private fun initMock() {
        MockitoAnnotations.initMocks(this)
        mockContext()
    }

    @Mock
    private lateinit var context: Application
    private fun mockContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(Mockito.mock(Resources::class.java))
        `when`(context.getString(R.string.detail_money_unit)).thenReturn(SZ_WON)
    }
}