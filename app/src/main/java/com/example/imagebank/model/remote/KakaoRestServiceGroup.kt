package com.example.imagebank.model.remote

import com.example.imagebank.model.remote.entity.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 */

interface KakaoRestSearchService {
    @GET("v2/search/image")
    fun image(@Query("query") query: String,
              @Query("page") page: String = "1",
              @Query("sort") sort: String = "accuracy",
              @Query("size") size: String = "80"): Observable<KakaoImageSearch>

    @GET("v2/search/vclip")
    fun vclip(@Query("query") query: String,
              @Query("page") page: String = "1",
              @Query("sort") sort: String = "accuracy",
              @Query("size") size: String = "15"): Observable<KakaoVClipSearch>
}