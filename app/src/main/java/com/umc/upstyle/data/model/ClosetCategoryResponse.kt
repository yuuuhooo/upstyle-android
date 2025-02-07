package com.umc.upstyle.data.model

data class ClosetCategoryResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ClosetCategoryResult
)





data class Ootd(
    val id: Long,
    val user: User,
    val date: String,
    val imageUrl: String,
    val clothList: List<ClothPreview>
)

