package com.example.imagebank.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.text.toHtml
import brigitte.html
import brigitte.numberFormat
import com.example.imagebank.R
import com.example.imagebank.ui.detail.DetailViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-29 <p/>
 */

@RunWith(RobolectricTestRunner::class)
class DetailViewModelTest {
    lateinit var viewModel: DetailViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewModel = DetailViewModel(context)
    }

    @Test
    @Config(sdk=[24], manifest = "src/main/AndroidManifest.xml")
    fun testConvertAmount_color087BCD_sdk24() {
        val amount = 1000000
        val converted = viewModel.convertAmount(amount)
        val result = """<b><font color="#087BCD">${amount.numberFormat()}$SZ_WON</font></b>""".html()

        assertEquals(converted?.toHtml(), result?.toHtml())
    }

    @Test
    @Config(sdk=[22], manifest = "src/main/AndroidManifest.xml")
    fun testConvertAmount_color087BCD_sdk22() {
        val amount = 1000000
        val converted = viewModel.convertAmount(amount)
        val result = """<b><font color="#087BCD">${amount.numberFormat()}$SZ_WON</font></b>""".html()

        assertEquals(converted?.toHtml(), result?.toHtml())
    }

    @Test
    @Config(sdk=[24], manifest = "src/main/AndroidManifest.xml")
    fun testConvertAmount_color000000_sdk24() {
        val amount = -11695
        val converted = viewModel.convertAmount(amount)
        val result = """<b><font color="#000000">${amount.numberFormat()}$SZ_WON</font></b>""".html()

        assertEquals(converted?.toHtml(), result?.toHtml())
    }

    @Test
    @Config(sdk=[22], manifest = "src/main/AndroidManifest.xml")
    fun testConvertAmount_color000000_sdk22() {
        val amount = -11695
        val converted = viewModel.convertAmount(amount)
        val result = """<b><font color="#000000">${amount.numberFormat()}$SZ_WON</font></b>""".html()

        assertEquals(converted?.toHtml(), result?.toHtml())
    }

    @Test
    fun testConvertBalance() {
        val amount = 1000000
        val converted = viewModel.convertBalance(amount)
        val result = "${amount.numberFormat()}$SZ_WON"

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