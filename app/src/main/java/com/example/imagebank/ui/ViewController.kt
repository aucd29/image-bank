package com.example.imagebank.ui

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import brigitte.FragmentAnim
import brigitte.FragmentParams
import brigitte.show
import brigitte.showBy
import com.example.imagebank.R
import com.example.imagebank.model.remote.entity.KakaoSearchResult
import com.example.imagebank.ui.detail.DetailFragment
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */
class ViewController @Inject constructor(private val manager: FragmentManager) {
    companion object {
        private val mLog = LoggerFactory.getLogger(ViewController::class.java)

        const val CONTAINER = R.id.main_container
    }

    @Inject lateinit var mDetailFragment: DetailFragment

    fun detailFragment(item: KakaoSearchResult) {
        if (mLog.isInfoEnabled) {
            mLog.info("DETAIL FRAGMENT")
        }

        manager.showBy(FragmentParams(CONTAINER,
            fragment = mDetailFragment,
            anim     = FragmentAnim.UP,
            bundle   = Bundle().apply { putSerializable(DetailFragment.K_ITEM, item) }
        ))
    }
}