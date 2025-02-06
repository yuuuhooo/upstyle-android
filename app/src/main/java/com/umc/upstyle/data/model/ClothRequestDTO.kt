package com.umc.upstyle.data.model

data class ClothRequestDTO(
    val clothId: Int,
    val clothKindId: Int,
    val clothCategoryId: Int,
    val fitCategoryId: Int,
    val colorCategoryId: Int,
    val additionalInfo: String
)