package com.umc.upstyle.data.model

// 옷 데이터 클래스
data class Cloth(
    val id: Int,
    val kindId: Int,
    val kindName: String,
    val categoryId: Int,
    val categoryName: String,
    val fitId: Int,
    val fitName: String,
    val colorId: Int,
    val colorName: String
)