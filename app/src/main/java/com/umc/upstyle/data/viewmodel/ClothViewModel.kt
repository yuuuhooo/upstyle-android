package com.umc.upstyle.data.viewmodel

import androidx.lifecycle.ViewModel
import com.umc.upstyle.data.model.ClothRequestDTO

class ClothViewModel : ViewModel() {
    // 리스트를 ViewModel에서 관리
    val clothList = mutableListOf<ClothRequestDTO>()

    // 리스트에 원소 추가하는 함수
    fun addCloth(cloth: ClothRequestDTO) {
        clothList.add(cloth)
    }

}
