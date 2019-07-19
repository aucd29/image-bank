package brigitte.widget.viewpager

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import brigitte.widget.InfinitePagerAdapter

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-19 <p/>
 *
 * https://github.com/antonyt/InfiniteViewPager/blob/master/library/src/main/java/com/antonyt/infiniteviewpager/InfinitePagerAdapter.java
 * https://github.com/antonyt/InfiniteViewPager/blob/master/library/src/main/java/com/antonyt/infiniteviewpager/InfiniteViewPager.java
 */

class InfiniteViewPager : WrapContentViewPager {
    constructor(context: Context): super(context)
    constructor(context: Context, attr: AttributeSet): super(context, attr)

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)

        currentItem = 0
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        adapter?.let {
            if (it.count == 0) {
                super.setCurrentItem(item, smoothScroll)
                return
            }

            val newItem = offsetAmount() + (item % it.count)

            super.setCurrentItem(newItem, smoothScroll)
        } ?: super.setCurrentItem(item, smoothScroll)
    }

    private fun offsetAmount(): Int {
        return adapter?.let {
            if (it.count == 0) {
                0
            }

            if (it is InfinitePagerAdapter) {
                it.realCount * 100
            } else {
                0
            }
        }  ?: 0
    }
}
