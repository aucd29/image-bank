package com.example.imagebank.ui.viewmodel

import androidx.core.text.toHtml
import brigitte.html
import brigitte.numberFormat
import com.example.imagebank.R
import com.example.imagebank.ui.detail.DetailViewModel
import com.example.imagebank.util.BaseRoboViewModelTest
import com.example.imagebank.util.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-29 <p/>
 */

@RunWith(RobolectricTestRunner::class)
class DetailViewModelTest: BaseRoboViewModelTest<DetailViewModel>() {
    val MONEY_UNIT = string(R.string.detail_money_unit)

    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = DetailViewModel(app)
    }

    @Test
    @Config(sdk=[24])
    fun testConvertAmount_color087BCD_sdk24() {
        val amount = 1000000
        val converted = viewmodel.convertAmount(amount)
        val result = """<b><font color="#087BCD">${amount.numberFormat()}$MONEY_UNIT</font></b>""".html()

        converted?.toHtml().assertEquals(result?.toHtml())
    }

    @Test
    @Config(sdk=[22])
    fun testConvertAmount_color087BCD_sdk22() {
        val amount = 1000000
        val converted = viewmodel.convertAmount(amount)
        val result = """<b><font color="#087BCD">${amount.numberFormat()}$MONEY_UNIT</font></b>""".html()

        converted?.toHtml().assertEquals(result?.toHtml())
    }

    @Test
    @Config(sdk=[24])
    fun testConvertAmount_color000000_sdk24() {
        val amount = -11695
        val converted = viewmodel.convertAmount(amount)
        val result = """<b><font color="#000000">${amount.numberFormat()}$MONEY_UNIT</font></b>""".html()

        converted?.toHtml().assertEquals(result?.toHtml())
    }

    @Test
    @Config(sdk=[22])
    fun testConvertAmount_color000000_sdk22() {
        val amount = -11695
        val converted = viewmodel.convertAmount(amount)
        val result = """<b><font color="#000000">${amount.numberFormat()}$MONEY_UNIT</font></b>""".html()

        converted?.toHtml().assertEquals(result?.toHtml())
    }

    @Test
    fun testConvertBalance() {
        val amount = 1000000
        val converted = viewmodel.convertBalance(amount)
        val result = "${amount.numberFormat()}$MONEY_UNIT"

        converted.assertEquals(result)
    }
}