package com.umc.upstyle.utils

object CategoryUtil {
    private val categoryMap = mapOf(
        "레더" to Pair(1, 1),
        "무스탕" to Pair(2, 1),
        "가디건" to Pair(3, 1),
        "코트" to Pair(4, 1),
        "숏패딩" to Pair(5, 1),
        "롱패딩" to Pair(6, 1),
        "블레이저" to Pair(7, 1),
        "트러커 재킷" to Pair(8, 1),
        "재킷" to Pair(9, 1),
        "후드집업" to Pair(10, 1),
        "트레이닝 재킷" to Pair(11, 1),
        "셔츠" to Pair(12, 2),
        "맨투맨" to Pair(13, 2),
        "블라우스" to Pair(14, 2),
        "니트" to Pair(15, 2),
        "후드티" to Pair(16, 2),
        "카라 티셔츠" to Pair(17, 2),
        "긴소매 티셔츠" to Pair(18, 2),
        "반소매 티셔츠" to Pair(19, 2),
        "민소매 티셔츠" to Pair(20, 2),
        "기타" to Pair(21, 2),
        "숏팬츠" to Pair(22, 3),
        "데님팬츠" to Pair(23, 3),
        "코튼팬츠" to Pair(24, 3),
        "레깅스" to Pair(25, 3),
        "트레이닝" to Pair(26, 3),
        "미니스커트" to Pair(27, 3),
        "미디스커트" to Pair(28, 3),
        "롱스커트" to Pair(29, 3),
        "롱원피스" to Pair(30, 3),
        "미니원피스" to Pair(31, 3),
        "미디원피스" to Pair(32, 3),
        "기타" to Pair(33, 3),
        "구두" to Pair(34, 4),
        "스니커즈" to Pair(35, 4),
        "스포츠화" to Pair(36, 4),
        "샌들/슬리퍼" to Pair(37, 4),
        "패딩/퍼 신발" to Pair(38, 4),
        "부츠/워커" to Pair(39, 4),
    )

    fun getCategoryIds(subCategory: String): Pair<Int, Int>? {
        return categoryMap[subCategory]
    }
}
