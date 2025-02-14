package com.umc.upstyle.data.network

import com.umc.upstyle.data.model.AddCodiReqDTO
import com.umc.upstyle.data.model.AddCodiRes
import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.model.RequestDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RequestService {
    @POST("codi-requests/")
    suspend fun createCodiReq(@Body request: AddCodiReqDTO): Response<ApiResponse<AddCodiRes>>

    @GET("codi-requests/{requestId}")
    suspend fun getRequestDetail(@Path("requestId") requestId: Int): Response<ApiResponse<RequestDetailResponse>>
}