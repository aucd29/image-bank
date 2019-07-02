package com.example.imagebank.ui.main.dibs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.example.imagebank.R
import com.example.imagebank.databinding.BannerLayoutBinding
import com.example.imagebank.model.local.Banner

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */
// https://medium.com/@cdmunoz/the-easiest-way-to-work-with-viewpager-and-without-fragments-c62ec3e8b9f3
class DibsPagerAdapter(
    private val mContext: Context,
    private val mItems: List<Banner>,
    private var mBannerViewModel: BannerViewModel
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflator = LayoutInflater.from(mContext)
        val binding = DataBindingUtil.inflate<BannerLayoutBinding>(inflator,
            R.layout.banner_layout, container, true)

        binding.model = mBannerViewModel
        binding.item  = mItems[position]

        return binding.root
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        if (obj is View) {
            container.removeView(obj)
        }
    }

    override fun getCount(): Int {
        return mItems.size
    }
}

