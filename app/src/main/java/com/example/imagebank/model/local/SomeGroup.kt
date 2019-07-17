package com.example.imagebank.model.local

import androidx.databinding.ObservableBoolean
import brigitte.IRecyclerDiff
import brigitte.IRecyclerExpandable
import brigitte.IRecyclerItem

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

open class QnaItem(val id: String, val title: String, override var type: Int = T_PARENT,
                   override var childList: List<QnaItem> = emptyList())
    : IRecyclerDiff, IRecyclerItem, IRecyclerExpandable<QnaItem> {

    companion object {
        const val T_PARENT = 0
        const val T_CHILD  = 1
    }

    override var toggle = ObservableBoolean(false)

    override fun itemSame(item: IRecyclerDiff): Boolean {
        val newItem = item as QnaItem
        return id == newItem.id
    }

    override fun contentsSame(item: IRecyclerDiff): Boolean {
        val newItem = item as QnaItem
        return title == newItem.title
    }
}
