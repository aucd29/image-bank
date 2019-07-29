package com.example.imagebank.ui.main.some

import android.app.Application
import androidx.databinding.ObservableInt
import brigitte.RecyclerViewModel
import com.example.imagebank.R
import com.example.imagebank.model.local.LinkItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-17 <p/>
 */

class SomeLinkViewModel @Inject constructor(application: Application
) : RecyclerViewModel<LinkItem>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeLinkViewModel::class.java)

        const val CMD_CLICK = "link-click"
    }

    val gridCount = ObservableInt(3)

    init {
        var i = 0
        initAdapter(R.layout.some_link_item)
        items.set(arrayListOf(
            LinkItem(++i, "전화", ""),
            LinkItem(++i, "카카", ""),
            LinkItem(++i, "문의", "")
        ))
    }
}