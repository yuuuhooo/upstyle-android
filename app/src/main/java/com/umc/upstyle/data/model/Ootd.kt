package com.umc.upstyle.data.model

data class Ootd(
    val id: Long,
    val user: User,
    val date: String,
    val imageUrl: String,
    val clothList: List<ClothPreview>
)
