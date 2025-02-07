package com.umc.upstyle.data.model

data class ClosetCategoryResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ClosetCategoryResult
)
