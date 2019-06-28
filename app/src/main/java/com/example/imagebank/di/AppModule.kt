package com.example.imagebank.di

import brigitte.di.dagger.module.RxModule
import brigitte.di.dagger.module.ViewModelFactoryModule
import com.example.imagebank.model.remote.KakaoRestSearchService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 */

@Module(includes = [ViewModelFactoryModule::class
    , ViewModelModule::class
    , NetworkModule::class
    , RxModule::class
    , CalligraphyModule::class
])
class AppModule {
    companion object {
        const val KAKAO_REST_URL  = "https://dapi.kakao.com/"
    }

    // 다수개의 retrofit 을 이용해야 하므로 Retrofit.Builder 를 전달 받은 후
    // 이곳에서 baseurl 을 설정하는 방식을 이용한다.

    @Singleton
    @Provides
    fun privodeKakaoRestSearchService(retrofitBuilder: Retrofit.Builder): KakaoRestSearchService =
        retrofitBuilder
            .baseUrl(KAKAO_REST_URL)
            .build()
            .create(KakaoRestSearchService::class.java)
}
