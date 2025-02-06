package com.umc.upstyle.data.model

data class OOTDRequest(
    val userId: Int,
    val date: String,
    val ootdImages: String,
    val clothRequestDTOList: List<ClothRequestDTO>
)