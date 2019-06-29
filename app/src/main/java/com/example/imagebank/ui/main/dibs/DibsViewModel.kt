package com.example.imagebank.ui.main.dibs

import android.app.Application
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import brigitte.RecyclerViewModel
import brigitte.toast
import com.example.imagebank.R
import com.example.imagebank.model.remote.entity.KakaoMergeResult
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

class DibsViewModel @Inject constructor(application: Application
) : RecyclerViewModel<KakaoMergeResult>(application) {
    companion object {
        private val mLog = LoggerFactory.getLogger(DibsViewModel::class.java)
    }

    val gridCount = ObservableInt(2)

    init {
        initAdapter("dibs_item")
        items.set(arrayListOf())

        adapter.get()?.isNotifySetChanged = true
    }

    fun toggleDibs(item: KakaoMergeResult) {
        items.get()?.let {
            if (it is ArrayList) {
                if (item.dibs) {
                    if (mLog.isDebugEnabled) {
                        mLog.debug("REMOVE DIBS")
                    }
                    it.remove(item)
                } else {
                    if (mLog.isDebugEnabled) {
                        mLog.debug("ADD DIBS")
                    }
                    it.add(item)
                }
            }
        }

        item.dibs = !item.dibs
        items.notifyChange()

        toast(if (item.dibs) {
            R.string.search_add_dibs
        } else {
            R.string.search_remove_dibs
        })
    }
}