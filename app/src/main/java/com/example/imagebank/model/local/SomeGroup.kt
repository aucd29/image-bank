package com.example.imagebank.model.local

import brigitte.IRecyclerDiff

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-16 <p/>
 */

data class IconLinkItem(val id: Int, val title: String, val link: String) : IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff): Boolean {
        val newItem = item as IconLinkItem
        return id == newItem.id
    }

    override fun contentsSame(item: IRecyclerDiff): Boolean {
        val newItem = item as IconLinkItem
        return title == newItem.title && link == newItem.link
    }
}

data class ChipItem(
    val id: Int,
    val title: String,
    val link: String
) : IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff): Boolean {
        val newItem = item as ChipItem
        return id == newItem.id
    }

    override fun contentsSame(item: IRecyclerDiff): Boolean {
        val newItem = item as ChipItem
        return item.title == newItem.title && link == newItem.link
    }
}
