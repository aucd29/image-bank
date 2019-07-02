package com.example.imagebank.ui.detail

import android.app.Application
import brigitte.RecyclerViewModel
import brigitte.html
import brigitte.numberFormat
import brigitte.string
import com.example.imagebank.R
import com.example.imagebank.model.local.AccountData
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

class DetailViewModel @Inject constructor(application: Application

) : RecyclerViewModel<AccountData>(application) {
    private val mMoneyUnit = string(R.string.detail_money_unit)

    init {
        initAdapter("detail_item")

        val list = arrayListOf(
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.30", 1000000, 2197),
            AccountData("대출이자", "06.24", -11695, -997803, "#대출원리금"),
            AccountData("최철동", "06.21", 2000000, -986108)
        )

        items.set(list)
    }

    fun convertAmount(amount: Int) =
        if (amount > 0) {
            """<b><font color="#087BCD">${amount.numberFormat()}$mMoneyUnit</font></b>"""
        } else {
            """<b><font color"#000000">${amount.numberFormat()}$mMoneyUnit</font></b>"""
        }.html()

    fun convertBalance(balance: Int) =
        "${balance.numberFormat()}$mMoneyUnit"
}