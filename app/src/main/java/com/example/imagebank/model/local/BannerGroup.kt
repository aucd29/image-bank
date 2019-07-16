package com.example.imagebank.model.local

import brigitte.widget.IBannerItem

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

data class Banner(
    val title: String,
    val description: String,
    val img: String,
    val bgcolor: String,
    val statusColor: String
): IBannerItem


