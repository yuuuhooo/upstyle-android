package com.umc.upstyle.data.network

import com.umc.upstyle.data.model.AddCodiReqDTO
import com.umc.upstyle.data.model.AddCodiRes
import com.umc.upstyle.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestService {
    @POST("codi-requests/")
    suspend fun createCodiReq(@Body request: AddCodiReqDTO): Response<ApiResponse<AddCodiRes>>
}