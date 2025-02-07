package com.umc.upstyle.data.model

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