package com.umc.upstyle

// Swagger UI Schema 함수명의 DTO 제외한 이름이 데이터 클래스 이름

// OOTD 상세 데이터 클래스
data class OOTD(
    val id: Int,
    val user: User,
    val date: String,
    val imageUrl: String,
    val clothList: List<Cloth>
)

// 공통 API 응답 데이터 클래스 (ApiResponseOotd, ApiResponseCalendar 에서 사용)
data class ApiResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T
)

// 사용자 데이터 클래스
data class User(
    val id: Int,
    val nickname: String
)

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


// OOTD 캘린더 데이터 클래스
data class OOTDCalendar(
    val userId: Int,
    val ootdPreviewList: List<OOTDPreview>
)

// OOTD 캘린더 미리보기 데이터 클래스
data class OOTDPreview(
    val id: Int,
    val date: String,
    val imageUrl: String
)


