package com.example.imagebank.ui.main.some

import android.app.Application
import android.graphics.Rect
import androidx.databinding.ObservableField
import brigitte.RecyclerViewModel
import brigitte.app
import brigitte.dpToPx
import brigitte.widget.viewpager.SpaceItemDecoration
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.example.imagebank.R
import com.example.imagebank.model.local.ChipItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-16 <p/>
 */

class SomeChipViewModel @Inject constructor(application: Application,
    val chipLayoutManager: ObservableField<ChipsLayoutManager>
) : RecyclerViewModel<ChipItem>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeChipViewModel::class.java)

        const val CMD_CHIP_EVENT = "cmd-chip-event"
    }

    val itemDecoration = ObservableField(SpaceItemDecoration(Rect().apply {
        right  = 10.dpToPx(app)
        bottom = right
    }))

    init {
        var i = 0
        initAdapter(R.layout.some_chip_item)
        items.set(arrayListOf(
            ChipItem(++i, "내 신용정보", "content://"),
            ChipItem(++i, "한도계좌", "content://"),
            ChipItem(++i, "카톡이체", "content://"),
            ChipItem(++i, "모임통장", "content://"),
            ChipItem(++i, "WU빠른해외송금", "content://")
        ))
    }
}