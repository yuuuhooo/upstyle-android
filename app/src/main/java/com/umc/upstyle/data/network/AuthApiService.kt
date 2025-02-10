package com.umc.upstyle.data.network

import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.model.KakaoJwtDTO
import com.umc.upstyle.data.model.OOTDCalendar
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AuthApiService {
    @GET("/auth/login/kakao")
    fun loginWithKakao(
        @Header("Authorization") accessToken: String // 카카오 액세스 토큰을 헤더에 포함
    ): Call<ApiResponse<KakaoJwtDTO>>
}

