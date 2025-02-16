package com.umc.upstyle.data.model

data class ClothPreview(
    val id: Int,
    val kindId: Int,
    val kindName: String,
    val categoryId: Int,
    val categoryName: String,
    val fitId: Int,
    val fitName: String,
    val colorId: Int,
    val colorName: String,
    val additionalInfo: String?,
    val ootd: Ootd? = null  // ✅ Ootd를 nullable로 변경
) {
    data class Ootd(
        val imageUrl: String
    )
}
