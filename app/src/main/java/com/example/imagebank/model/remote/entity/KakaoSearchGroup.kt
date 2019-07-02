package com.example.imagebank.model.remote.entity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import brigitte.IRecyclerDiff
import brigitte.bindingadapter.ToLargeAlphaAnimParams
import com.example.imagebank.R
import java.io.Serializable

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 *
 * https://developers.kakao.com/docs/restapi/search#%EC%9D%B4%EB%AF%B8%EC%A7%80-%EA%B2%80%EC%83%89
 */

// 문서에 오류 일때 정보가 없음 ... 일해라.. 카카오..
data class KakaoMetaResult (
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
) : Serializable

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
) : Serializable

data class KakaoVClipResult (
    val title: String,
    val url: String,
    val datetime: String,
    val play_time: Int,
    val thumbnail: String,
    val author: String
) : Serializable

data class KakaoSearch<T>(
    val documents: List<T>?,
    val meta: KakaoMetaResult?,

    // {"errorType":"InvalidArgument","message":"page is more than max"}
    val errorType: String?,
    val message: String?
) : Serializable

typealias KakaoImageSearch = KakaoSearch<KakaoImageResult>
typealias KakaoVClipSearch = KakaoSearch<KakaoVClipResult>





data class KakaoSearchResult(
    val thumbnail: String,
    val datetime: String,
    val unixtime: Long,
    val url: String?,
    val title: String?,
    var type: Int = T_IMAGE
): IRecyclerDiff, Serializable {
    companion object {
        val T_IMAGE = 0
        val T_VCLIP = 1
    }

    var dibs = ObservableInt(R.drawable.ic_star_border_yellow_24dp)
    var anim = ObservableField<ToLargeAlphaAnimParams>()

    fun toggleDibs() {
        dibs.set(if (dibs.get() == R.drawable.ic_star_border_yellow_24dp) {
            R.drawable.ic_star_yellow_24dp
        } else {
            R.drawable.ic_star_border_yellow_24dp
        })
    }

    fun isFilledStar() = dibs.get() == R.drawable.ic_star_yellow_24dp
    fun fillStar() {
        dibs.set(R.drawable.ic_star_yellow_24dp)
    }

    override fun compare(item: IRecyclerDiff): Boolean {
        val newItem = item as KakaoSearchResult

        return thumbnail == newItem.thumbnail &&
                unixtime == newItem.unixtime && dibs.get() == newItem.dibs.get()
    }

    fun trace() = "TITLE: $title\n TYPE: $type\n URL: $url"
}
