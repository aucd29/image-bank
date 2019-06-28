package com.example.imagebank.model.remote.entity

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 */


data class KakaoSearchResult(
    val collection: String,
    val datetime: String,
    val display_sitename: String,
    val doc_url: String,
    val height: String,
    val image_url: String,
    val thumbnail_url: String,
    val width: Int
)

data class KakaoMetaResult (
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)

data class KakaoSearch(
    val documents: List<KakaoSearchResult>,
    val meta: KakaoMetaResult
)