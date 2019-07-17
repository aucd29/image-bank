package com.example.imagebank.ui.main.some

import android.app.Application
import androidx.databinding.ObservableInt
import brigitte.RecyclerViewModel
import com.example.imagebank.R
import com.example.imagebank.model.local.IconLinkItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-17 <p/>
 */

class SomeLinkViewModel @Inject constructor(application: Application
) : RecyclerViewModel<IconLinkItem>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeLinkViewModel::class.java)

        const val CMD_CLICK = "link-click"
    }

    val gridCount = ObservableInt(3)

    init {
        initAdapter(R.layout.some_link_item)
        items.set(arrayListOf(
            IconLinkItem(1, "전화", ""),
            IconLinkItem(2, "카카", ""),
            IconLinkItem(3, "문의", "")
        ))
    }
}