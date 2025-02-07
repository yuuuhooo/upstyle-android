package com.umc.upstyle.data.model

data class ClosetResult(
    val userId: Long,
    val userName: String,
    val clothKindList: List<ClothKind>
)