package com.umc.upstyle.data.model

// Swagger UI Schema 함수명의 DTO 제외한 이름이 데이터 클래스 이름

// OOTD 상세 데이터 클래스
data class OOTD(
    val id: Int,
    val user: User,
    val date: String,
    val imageUrl: String,
    val clothList: List<Cloth>
)













