package com.example.imagebank.model.remote.entity

import brigitte.IRecyclerDiff
import brigitte.IRecyclerItem
import com.example.imagebank.model.IKakaoSearchData

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 *
 * https://developers.kakao.com/docs/restapi/search#%EC%9D%B4%EB%AF%B8%EC%A7%80-%EA%B2%80%EC%83%89
 *
 *
 */

// 문서에 오류 일때 정보가 없음 ... 일해라.. 카카오..
data class KakaoMetaResult (
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)

// 문서에 Mandatory 인지 Optional 인지 없다 ... 일해라.. 카카오..
data class KakaoImageResult(
    val collection: String?,
    val thumbnail_url: String?,
    val image_url: String?,
    val width: Int,
    val height: Int,
    val display_sitename: String?,
    val doc_url: String?,
    val datetime: String
)

data class KakaoVClipResult (
    val title: String,
    val url: String,
    val datetime: String,
    val play_time: Int,
    val thumbnail: String,
    val author: String
)

data class KakaoSearch<T>(
    val documents: List<T>,
    val meta: KakaoMetaResult
)

typealias KakaoImageSearch = KakaoSearch<KakaoImageResult>
typealias KakaoVClipSearch = KakaoSearch<KakaoVClipResult>

data class KakaoMergeResult(
    val thumbnail: String,
    val datetime: String,
    val unixtime: Long,
    var dibs: Boolean = false
): IRecyclerDiff {
    override fun compare(item: IRecyclerDiff): Boolean {
        val newItem = item as KakaoMergeResult

        return thumbnail == newItem.thumbnail &&
                unixtime == newItem.unixtime
    }
}



//data class KakaoImageSearch(
//    val documents: List<KakaoImageResult>,
//    val meta: KakaoMetaResult
//)
//
//data class KakaoVClipSearch(
//    val documents: List<KakaoImageResult>,
//    val meta: KakaoMetaResult
//)