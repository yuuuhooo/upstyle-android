package com.umc.upstyle.model

data class ClosetResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ClosetResult
)

data class ClosetResult(
    val userId: Long,
    val userName: String,
    val clothKindList: List<ClothKind>
)

data class ClothKind(
    val kindId: Long,
    val kindName: String,
    val thumbnailUrl: String
)
