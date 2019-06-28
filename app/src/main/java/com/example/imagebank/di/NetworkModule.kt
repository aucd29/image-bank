package com.example.imagebank.di

import brigitte.AuthorizationInterceptor
import brigitte.di.dagger.module.OkhttpModule
import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 5. <p/>
 */

@Module(includes = [OkhttpModule::class])
class NetworkModule {
    companion object {
        val LOG_CLASS = NetworkModule::class.java

        const val KAKAO_AK        = "KakaoAK"
        const val AUTHORIZATION   = "Authorization"
        const val KAKAO_REST_AUTH = "e302331ef568c1a4af2053c77eef1b89"
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger =
        LoggerFactory.getLogger(LOG_CLASS)

    @Provides
    @Singleton
    fun provideLogLevel() =
        HttpLoggingInterceptor.Level.BODY


    @Provides
    @Singleton
    fun provideAuthorizationInterceptor() =
        object : AuthorizationInterceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                return chain.proceed(chain.request().newBuilder()
                        .addHeader(AUTHORIZATION, "$KAKAO_AK $KAKAO_REST_AUTH")
                        .build()
                )
            }
        }
}
