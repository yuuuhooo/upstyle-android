package com.umc.upstyle

object RequestRepository {  // 'object' 키워드를 사용하여 싱글턴 객체로 만듦
    fun fetchPosts(): List<Request> {
        return listOf(
            Request(1, "내일 갑자기 강릉으로 여행가게 되었는데 뭘 입어야 할까요? 그리고 제목이 이렇게 길면 어떻게 될까요?", 1),
            Request(2, "제 옷장에 있는 옷으로 코디 좀 해주세요", 2),
            Request(3, "소개팅룩 추천 부탁", 2),
            Request(4, "내일 썸남이랑 데이트 있는데 짧치 입을까요?", 3),
            Request(5, "면접 가는데 그렇게 빡센데는 아니거든요 뭐 입고 가야 할까요", 3),
        )
    }
}
