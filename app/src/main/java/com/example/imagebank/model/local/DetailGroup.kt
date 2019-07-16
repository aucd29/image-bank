package com.example.imagebank.model.local

import brigitte.IRecyclerDiff

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-01 <p/>
 */

data class AccountData(
    val id: Int,
    val name: String,
    val date: String,
    val amount: Int,
    val balance: Int,
    val tag: String? = null
) : IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff): Boolean {
        val newItem = item as AccountData
        return id == newItem.id
    }

    override fun contentsSame(item: IRecyclerDiff): Boolean {
        val newItem = item as AccountData
        return name == newItem.name && date == newItem.date && balance == newItem.balance
    }
}