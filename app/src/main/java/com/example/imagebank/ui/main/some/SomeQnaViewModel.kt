package com.example.imagebank.ui.main.some

import android.app.Application
import androidx.databinding.ObservableInt
import brigitte.RecyclerViewModel
import com.example.imagebank.R
import com.example.imagebank.model.local.QnaItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-17 <p/>
 */

class SomeQnaViewModel @Inject constructor(application: Application
) : RecyclerViewModel<QnaItem>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeQnaViewModel::class.java)
    }

    init {
        initAdapter(R.layout.some_qna_parent_item, R.layout.some_qna_child_item)

        // IRecyclerExpandable 이 일반화 되었는지를 증명하기 위한 view model
        // 모양이 이쁘진 않네 리펙한번 해야 할 듯
        items.set(arrayListOf(
            QnaItem("1", "qna 1", childList = arrayListOf(QnaItem("1-1","child item 1", QnaItem.T_CHILD))),
            QnaItem("2", "qna 2", childList = arrayListOf(QnaItem("2-1","child item 2", QnaItem.T_CHILD))),
            QnaItem("3", "qna 3", childList = arrayListOf(QnaItem("3-1","child item 3", QnaItem.T_CHILD))),
            QnaItem("4", "qna 4", childList = arrayListOf(QnaItem("4-1","child item 4", QnaItem.T_CHILD))),
            QnaItem("5", "qna 5", childList = arrayListOf(QnaItem("5-1","child item 5", QnaItem.T_CHILD))),
            QnaItem("6", "qna 6", childList = arrayListOf(QnaItem("6-1","child item 6", QnaItem.T_CHILD))),
            QnaItem("7", "qna 7", childList = arrayListOf(QnaItem("7-1","child item 7", QnaItem.T_CHILD))),
            QnaItem("8", "qna 8", childList = arrayListOf(QnaItem("8-1","child item 8", QnaItem.T_CHILD)))
        ))
    }

    fun toggle(item: QnaItem) {
        item.toggle(items.get()!!, adapter.get()!!)
    }
}