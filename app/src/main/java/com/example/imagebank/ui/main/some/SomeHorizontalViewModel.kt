package com.example.imagebank.ui.main.some

import android.app.Application
import android.graphics.Rect
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import brigitte.RecyclerViewModel
import brigitte.app
import brigitte.dpToPx
import brigitte.widget.viewpager.SpaceItemDecoration
import com.example.imagebank.R
import com.example.imagebank.model.local.HorizontalItem
import com.example.imagebank.model.local.LinkItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-26 <p/>
 */

class SomeHorizontalViewModel @Inject constructor(
    application: Application
) : RecyclerViewModel<HorizontalItem>(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(SomeHorizontalViewModel::class.java)
    }

    lateinit var layoutManager: LinearLayoutManager
    val itemDecoration = ObservableField(SpaceItemDecoration(Rect().apply {
        left   = 10.dpToPx(app)
        bottom = left
    }))

    init {
        var i = 0
        initAdapter(R.layout.some_horizontal_item)
        items.set(arrayListOf(
            HorizontalItem(++i, "입출금 통장", 0, "link"),
            HorizontalItem(++i, "정기예금", 0, "link"),
            HorizontalItem(++i, "비상금대출", 0, "link"),
            HorizontalItem(++i, "마이너스 통장대출", 0, "link"),
            HorizontalItem(++i, "신용대출", 0, "link")
        ))
    }

    fun initLayoutManager() {
        layoutManager = LinearLayoutManager(app, LinearLayoutManager.HORIZONTAL, false)
    }
}