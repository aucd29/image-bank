package com.example.imagebank.model

import brigitte.IRecyclerDiff
import brigitte.IRecyclerItem

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-29 <p/>
 */

interface IKakaoSearchData : IRecyclerDiff, IRecyclerItem {
    companion object {
        val T_IMAGE = 0
        val T_VCLIP = 1
    }
}