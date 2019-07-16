package com.example.imagebank.ui.main.some

import android.app.Application
import androidx.databinding.ObservableField
import brigitte.RecyclerViewModel
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

    init {
        initAdapter(R.layout.some_chip_item)
        items.set(arrayListOf(
            ChipItem(1, "test1", "content://"),
            ChipItem(2, "test2", "content://"),
            ChipItem(3, "test3", "content://"),
            ChipItem(4, "test4", "content://")
        ))
    }
}