package com.umc.upstyle.data.network

import com.umc.upstyle.data.model.AdditionalInfoRequestDTO
import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.model.UserInfoDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {
    // 사용자 추가 정보 입력 API
    @POST("/users/additional-info")
    fun addAdditionalInfo(
        @Header("Authorization") token: String,  // JWT 인증 토큰
        @Body request: AdditionalInfoRequestDTO
    ): Call<ApiResponse<UserInfoDTO>>
}