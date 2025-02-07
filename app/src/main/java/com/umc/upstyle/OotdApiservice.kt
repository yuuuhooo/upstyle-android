package com.umc.upstyle

// OotdApiService.kt

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OotdApiService {

    @GET("/ootds/{ootdId}")
    fun getOOTDById(@Path("ootdId") ootdId: Int): Call<ApiResponse<OOTD>>

    @GET("/ootds/calendar")
    fun getOOTDCalendar(
        @Query("userId") userId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<ApiResponse<OOTDCalendar>>

}

