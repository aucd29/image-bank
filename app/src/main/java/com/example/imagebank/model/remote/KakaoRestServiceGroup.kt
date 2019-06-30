package com.example.imagebank.model.remote

import com.example.imagebank.model.remote.entity.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 */

interface KakaoRestSearchService {
    // size 기본 값은 80
    // size 기본 값을 15로 변경하고 mPage 값을 조정하면 안먹음 =_ =
    // https://developer.kakao.com/docs/restapi/search#이미지-검색
    @GET("v2/search/image")
    fun image(@Query("query") query: String,
              @Query("page") page: String = "1",
              @Query("sort") sort: String = "accuracy",
              @Query("size") size: String = "80"): Observable<KakaoImageSearch>

    //https://developer.kakao.com/docs/restapi/search#동영상-검색
    @GET("v2/search/vclip")
    fun vclip(@Query("query") query: String,
              @Query("page") page: String = "1",
              @Query("sort") sort: String = "accuracy",
              @Query("size") size: String = "15"): Observable<KakaoVClipSearch>
}