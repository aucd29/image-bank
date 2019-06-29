package com.example.imagebank.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import brigitte.string
import com.example.imagebank.R
import com.example.imagebank.ui.main.dibs.DibsFragment
import com.example.imagebank.ui.main.seach.SearchFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_search,
    R.string.tab_dibs
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0    -> SearchFragment()
            else -> DibsFragment()
        }
    }

    override fun getPageTitle(position: Int) = context.string(TAB_TITLES[position])
    override fun getCount() = TAB_TITLES.size
}