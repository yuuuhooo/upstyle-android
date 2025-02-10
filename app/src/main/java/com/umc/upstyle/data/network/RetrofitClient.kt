package com.umc.upstyle.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://ec2-3-37-185-8.ap-northeast-2.compute.amazonaws.com:8080/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 자동 변환
            .build()
    }

    // ✅ 원하는 API 인터페이스를 가져오는 제네릭 함수
    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
