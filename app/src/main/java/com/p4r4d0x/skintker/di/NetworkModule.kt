package com.p4r4d0x.skintker.di

import com.p4r4d0x.data.SkintkvaultApi
import com.p4r4d0x.skintker.BuildConfig
import okhttp3.OkHttpClient
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


fun provideOkHttpClient(/*interceptor*/): OkHttpClient =
    OkHttpClient()
        .newBuilder()
//        .addInterceptor(interceptor)
        .build()


fun provideSkintkvaultApi(retrofit: Retrofit): SkintkvaultApi =
    retrofit.create(SkintkvaultApi::class.java)
