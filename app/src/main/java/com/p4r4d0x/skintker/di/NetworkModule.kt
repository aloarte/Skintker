package com.p4r4d0x.skintker.di

import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.skintker.BuildConfig
import com.p4r4d0x.skintker.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single { provideRetrofit(get()) }
    factory { provideOkHttpClient() }
    factory { provideSkintkvaultApi(get()) }
}


fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


fun provideOkHttpClient(): OkHttpClient =
    OkHttpClient()
        .newBuilder()
        .addInterceptor(TokenInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()


fun provideSkintkvaultApi(retrofit: Retrofit): SkintkvaultApi =
    retrofit.create(SkintkvaultApi::class.java)
