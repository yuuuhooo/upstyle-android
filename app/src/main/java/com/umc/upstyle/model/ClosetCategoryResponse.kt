package com.umc.upstyle.model

data class ClosetCategoryResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ClosetCategoryResult
)

data class ClosetCategoryResult(
    val clothPreviewList: List<ClothPreview>
)

data class ClothPreview(
    val id: Long,
    val kindId: Long,
    val kindName: String,
    val categoryId: Long,
    val categoryName: String,
    val fitId: Long,
    val fitName: String,
    val colorId: Long,
    val colorName: String,
    val additionalInfo: String?,
    val ootd: Ootd?
)

data class Ootd(
    val id: Long,
    val user: User,
    val date: String,
    val imageUrl: String,
    val clothList: List<ClothPreview>
)

data class User(
    val id: Long,
    val nickname: String
)
