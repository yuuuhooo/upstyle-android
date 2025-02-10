package com.umc.upstyle.data.network

import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.model.VoteDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VoteService {
    @GET("votes/{voteId}")
    suspend fun getVoteDetail(@Path("voteId") voteId: Int): Response<ApiResponse<VoteDetailResponse>>
}
