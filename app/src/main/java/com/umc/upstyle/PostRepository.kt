package com.umc.upstyle

object PostRepository {  // 'object' 키워드를 사용하여 싱글턴 객체로 만듦
    fun fetchPosts(): List<Post> {
        return listOf(
            Post(1, "이 신발엔 무엇이 더 어울릴까요?", 124),
            Post(2, "빨간구두 롱 코트 VS 삭스부츠 롱 코트", 12),
            Post(3, "둘 중에 무슨 옷이 더 실용적일까요?", 84),
            Post(4, "캐주얼에 흰 시계? 은 시계?", 71),
            Post(5, "아니 이거 설마 투표 길이 맞춰서 변하나?", 32),
            Post(6, "오마이갓진짜네", 72),
        )
    }
}
