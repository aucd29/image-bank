package com.example.imagebank.ui

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import brigitte.FragmentAnim
import brigitte.FragmentParams
import brigitte.bindingadapter.AnimParams
import brigitte.show
import com.example.imagebank.R
import com.example.imagebank.model.remote.entity.KakaoSearchResult
import com.example.imagebank.ui.detail.DetailFragment
import kotlinx.android.synthetic.main.main_activity.view.*
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */
class ViewController @Inject constructor(private val manager: FragmentManager) {
    // 나만의 룰을 만들었더니만 navigation editor 나와버림 =_ = ㅋ

    companion object {
        private val mLog = LoggerFactory.getLogger(ViewController::class.java)

        const val CONTAINER = R.id.main_container
    }

    fun detailFragment(item: KakaoSearchResult) {
        manager.show<DetailFragment>(FragmentParams(CONTAINER,
            anim = FragmentAnim.UP, bundle = Bundle().apply {
                putSerializable(DetailFragment.K_ITEM, item)
            }
        ))
    }
}