package com.example.imagebank.ui.main.some

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import brigitte.RecyclerExpandableViewModel
import brigitte.RecyclerViewModel
import com.example.imagebank.R
import com.example.imagebank.model.local.QnaItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-17 <p/>
 */

class SomeQnaViewModel @Inject constructor(
    application: Application
) : RecyclerExpandableViewModel<QnaItem>(application) {
    companion object {
        private val mLog = LoggerFactory.getLogger(SomeQnaViewModel::class.java)
    }

    val horDecoration = ObservableField(R.drawable.shape_divider_gray)
    val verDecoration = ObservableField(R.drawable.shape_divider_gray)

    init {
        initAdapter(R.layout.some_qna_parent_item, R.layout.some_qna_child_item)

        // IRecyclerExpandable 이 일반화 되었는지를 증명하기 위한 view model
        // 모양이 이쁘진 않네 리펙한번 해야 할 듯
        val list = arrayListOf<QnaItem>()
        (0..8).forEach {
            list.add(QnaItem(it.toString(), "qna $it", childList = arrayListOf(QnaItem("${it}-1","child item $it", QnaItem.T_CHILD))))
        }

        items.set(list)
    }
}