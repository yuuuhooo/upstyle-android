package com.umc.upstyle.data.model

data class ClothRequestDTO(
    val clothId: Int = 0,
    val clothKindId: Int = 0,
    val clothCategoryId: Int = 0,
    val fitCategoryId: Int = 0,
    val colorCategoryId: Int = 0,
    val additionalInfo: String = ""
)