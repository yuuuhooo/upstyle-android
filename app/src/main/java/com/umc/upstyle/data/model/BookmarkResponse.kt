package com.umc.upstyle.data.model

data class BookmarkResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: BookmarkResult
)

data class BookmarkResult(
    val bookmarkList: List<BookmarkItem>,
    val listSize: Int,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

data class BookmarkItem(
    val clothId: Int,
    val kind: String,
    val category: String,
    val fit: String,
    val color: String,
    val ootd: OotdItem?
)

data class OotdItem(
    val id: Int,
    val imageUrl: String
)
