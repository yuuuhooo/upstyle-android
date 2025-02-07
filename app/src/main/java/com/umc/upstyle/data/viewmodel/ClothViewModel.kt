package com.umc.upstyle.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.upstyle.data.model.ClothRequestDTO

class ClothViewModel : ViewModel() {

    private val _clothList = MutableLiveData<MutableList<ClothRequestDTO>>()
    val clothList: LiveData<MutableList<ClothRequestDTO>> = _clothList

    private val _categoryData = MutableLiveData<Map<String, String>>()
    val categoryData: LiveData<Map<String, String>> = _categoryData

    init {
        // 초기값 설정
        _clothList.value = mutableListOf()
        _categoryData.value = mapOf(
            "OUTER" to "없음",
            "TOP" to "없음",
            "BOTTOM" to "없음",
            "SHOES" to "없음",
            "BAG" to "없음",
            "OTHER" to "없음"
        )
    }

    fun addClothRequest(cloth: ClothRequestDTO) {
        val currentList = _clothList.value ?: mutableListOf()
        currentList.add(cloth)
        _clothList.value = currentList
    }

    fun updateCategory(category: String, data: String) {
        val currentData = _categoryData.value?.toMutableMap() ?: mutableMapOf()
        currentData[category] = data
        _categoryData.value = currentData
    }


}

