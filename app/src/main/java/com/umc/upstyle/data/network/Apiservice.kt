package com.umc.upstyle.data.network

import com.umc.upstyle.data.model.BookmarkRequest
import com.umc.upstyle.data.model.BookmarkResponse
import com.umc.upstyle.data.model.ClosetCategoryResponse
import com.umc.upstyle.data.model.ClosetResponse
import com.umc.upstyle.data.model.ClothesCategoryResponse
import com.umc.upstyle.data.model.RequestResponse
import com.umc.upstyle.data.model.VotePreviewResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
        @Query("kindId") categoryId: Long? = null
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
        @Query("userId") userId: Long
    ): Call<BookmarkResponse>

    @GET("codi-requests/")  // 실제 서버의 API 엔드포인트로 교체
    fun getCodiRequests(): Call<RequestResponse>

    @POST("/bookmarks/")
    fun toggleBookmark(
        @Body request: BookmarkRequest
    ): Call<BookmarkResponse>

    @GET("/clothes/categories")
    fun getClothesByCategory(
        @Query("kindId") kindId: Long?,
        @Query("categoryId") categoryId: Long?,
        @Query("colorIds") colorIds: List<Long>?,
        @Query("fitId") fitId: Long?,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Call<ClothesCategoryResponse>



}
