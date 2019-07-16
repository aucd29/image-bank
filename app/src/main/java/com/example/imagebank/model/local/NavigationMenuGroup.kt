package com.example.imagebank.model.local

import brigitte.IRecyclerDiff
import brigitte.IRecyclerItem

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

data class NavigationGridItem(
    val id: Int,
    val name: String,
    val resid: Int
) : IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff): Boolean {
        val newItem = item as NavigationGridItem
        return id == newItem.id
    }

    override fun contentsSame(item: IRecyclerDiff): Boolean {
        val newItem = item as NavigationGridItem
        return name == newItem.name && resid == newItem.resid
    }
}

data class NavigationItem(
    val id: Int,
    val name: String,
    val isNew: Boolean = false
): IRecyclerDiff, IRecyclerItem {
    override var type = TYPE_DEFAULT

    override fun itemSame(item: IRecyclerDiff): Boolean {
        val newItem = item as NavigationItem
        return id == newItem.id
    }

    override fun contentsSame(item: IRecyclerDiff): Boolean {
        val newItem = item as NavigationItem
        return name == newItem.name
    }

    companion object {
        const val TYPE_DEFAULT   = 0
        const val TYPE_SEPARATOR = 1
    }
}