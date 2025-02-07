package com.umc.upstyle.data.network

import com.umc.upstyle.data.model.ClosetCategoryResponse
import com.umc.upstyle.data.model.ClosetResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // 특정 사용자 옷장 조회 API
    @GET("closets/")
    fun getUserCloset(
        @Query("userId") userId: Long
    ): Call<ClosetResponse>

    // 옷장 카테고리별 조회 API
    @GET("closets/categories")
    fun getClosetByCategory(
        @Query("userId") userId: Long,
        @Query("categoryId") categoryId: Long? = null
    ): Call<ClosetCategoryResponse>
}
