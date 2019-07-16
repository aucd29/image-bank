package com.example.imagebank.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import brigitte.string
import com.example.imagebank.R
import com.example.imagebank.ui.main.dibs.DibsFragment
import com.example.imagebank.ui.main.search.SearchFragment
import com.example.imagebank.ui.main.some.SomeFragment
import javax.inject.Inject

// string array 가 더 나을 듯 ?
private val TAB_TITLES = arrayOf(
    R.string.tab_search,
    R.string.tab_dibs,
    R.string.tab_some
)

class SectionsPagerAdapter @Inject constructor(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    @Inject lateinit var mSearchFragment: SearchFragment
    @Inject lateinit var mDibsFragment: DibsFragment
    @Inject lateinit var mSomeFragment: SomeFragment

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0    -> mSearchFragment
            1    -> mDibsFragment
            else -> mSomeFragment
        }
    }

    override fun getPageTitle(position: Int) = context.string(TAB_TITLES[position])
    override fun getCount() = TAB_TITLES.size
}