package com.umc.upstyle.data.network

import com.umc.upstyle.data.model.BookmarkResponse
import com.umc.upstyle.data.model.ClosetCategoryResponse
import com.umc.upstyle.data.model.ClosetResponse
import com.umc.upstyle.data.model.RequestResponse
import com.umc.upstyle.data.model.VotePreviewResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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


    @GET("closet/{userId}/{categoryId}")
    fun getClosetByCategory(
        @Path("userId") userId: Long,
        @Path("categoryId") categoryId: Long
    ): Call<ClosetCategoryResponse>

    @GET("votes/")
    fun getVotePreviews(): Call<VotePreviewResponse>


    @GET("bookmarks/")  // 서버의 API 엔드포인트
    fun getBookmarks(
        @Query("userId") userId: Long): Call<BookmarkResponse>

    @GET("codi-requests/")  // 실제 서버의 API 엔드포인트로 교체
    fun getCodiRequests(): Call<RequestResponse>


}
