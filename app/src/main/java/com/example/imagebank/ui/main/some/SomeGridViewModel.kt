package com.example.imagebank.ui.main.some

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import brigitte.RecyclerViewModel
import com.example.imagebank.R
import com.example.imagebank.model.local.LinkItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-17 <p/>
 */

class SomeGridViewModel @Inject constructor(application: Application
) : RecyclerViewModel<LinkItem>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeGridViewModel::class.java)

        const val CMD_CLICK = "link-click"
    }

    val gridCount = ObservableInt(4)
    val horDecoration = ObservableField(R.drawable.shape_divider_gray)
    val verDecoration = ObservableField(R.drawable.shape_divider_gray)

    init {
        var i = 0
//        ++i 이 없었는데? 생긴건가???
        initAdapter(R.layout.some_grid_item)
        items.set(arrayListOf(
            LinkItem(++i, "예/적금", ""),
            LinkItem(++i, "대출", ""),
            LinkItem(++i, "외환", ""),
            LinkItem(++i, "카드", ""),
            LinkItem(++i, "ATM/CD", ""),
            LinkItem(++i, "앱서비스", ""),
            LinkItem(++i, "보안설정", ""),
            LinkItem(++i, "기타", "")
        ))
    }
}