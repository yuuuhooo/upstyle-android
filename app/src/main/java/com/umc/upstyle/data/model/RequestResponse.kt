package com.umc.upstyle.data.model

data class RequestResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: CodiReqResult
)

data class CodiReqResult(
    val codiReqPreviewList: List<RequestItem>,
    val listSize: Int,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)


// 앱에서 사용할 데이터 모델
data class Request(
    val id: Int,
    val title: String,
    val commentCount: Int  // 서버 responseCount를 commentCount로 변환
)


data class RequestDetailResponse(
    val id: Int,
    val user: User,
    val title: String,
    val body: String,
    val imageUrl: String,
    val codiResPreviewList: List<CodiResPreview>
)

data class CodiResPreview (
    val id: Int,
    val user: User
)

