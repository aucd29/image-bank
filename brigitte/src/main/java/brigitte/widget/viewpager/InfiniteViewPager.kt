package brigitte.widget.viewpager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.PagerAdapter
import brigitte.widget.InfinitePagerAdapter
import org.slf4j.LoggerFactory

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-19 <p/>
 *
 * https://github.com/antonyt/InfiniteViewPager/blob/master/library/src/main/java/com/antonyt/infiniteviewpager/InfinitePagerAdapter.java
 * https://github.com/antonyt/InfiniteViewPager/blob/master/library/src/main/java/com/antonyt/infiniteviewpager/InfiniteViewPager.java
 */

class InfiniteViewPager : WrapContentViewPager {
    companion object {
        private val mLog = LoggerFactory.getLogger(InfiniteViewPager::class.java)
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attr: AttributeSet): super(context, attr)

    private var mActionDown = false
    private var mAutoScrollDelay: Long = 0L
    private var mHandler = Handler(Looper.getMainLooper())
    private var mScrollRunnable: Runnable? = null
    private var mPageChangeListener: SimpleOnPageChangeListener? = null

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)

        setCurrentItem(0)
    }

    override fun setCurrentItem(item: Int) {
        setCurrentItem(0, false)
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
            if (it is InfinitePagerAdapter) {
                it.realCount * 100
            } else {
                0
            }
        } ?: 0
    }

    fun runAutoScroll(delay: Long) {
        mAutoScrollDelay = delay

        if (mAutoScrollDelay == 0L) {
            mPageChangeListener?.let { removeOnPageChangeListener(it) }
            mPageChangeListener = null

            freeRunnable()
        } else {
            mPageChangeListener = object: SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    if (mActionDown) {
                        mActionDown = false
                        return
                    }

                    doAutoScroll()
                }
            }

            mPageChangeListener?.let { addOnPageChangeListener(it) }

            doAutoScroll()
        }
    }

    @Synchronized
    private fun doAutoScroll() {
        freeRunnable()

        if (mAutoScrollDelay == 0L) {
            return
        }

        mScrollRunnable = Runnable {
            val next = currentItem + 1
            if (mLog.isDebugEnabled) {
                mLog.debug("SCROLL NEXT $next")
            }

            setCurrentItem(next, true)
        }
        mHandler.postDelayed(mScrollRunnable, mAutoScrollDelay)
    }

    @Synchronized
    fun freeRunnable() {
        mScrollRunnable?.let {
            mHandler.removeCallbacks(it)
            mScrollRunnable = null
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mActionDown = true
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_UP -> {
                if (mScrollRunnable == null) {
                    doAutoScroll()
                }
            }
        }

        return super.onTouchEvent(ev)
    }
}
