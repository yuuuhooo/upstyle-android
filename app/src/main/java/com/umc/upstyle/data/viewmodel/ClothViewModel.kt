package com.umc.upstyle.data.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.upstyle.data.model.ClothRequestDTO

class ClothViewModel : ViewModel() {

    private val _imageUris = MutableLiveData<List<String>>(emptyList())
    val imageUris: LiveData<List<String>> = _imageUris

    private val _clothList = MutableLiveData<MutableList<ClothRequestDTO>>()
    val clothList: LiveData<MutableList<ClothRequestDTO>> = _clothList

    private val _categoryData = MutableLiveData<Map<String, String>>()
    val categoryData: LiveData<Map<String, String>> = _categoryData

    init {
        resetData()
    }

    fun addImage(uri: String) {
        _imageUris.value = _imageUris.value?.plus(uri)
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

    // ✅ ViewModel의 데이터를 초기화하는 함수 추가
    fun clearData() {
        resetData()
    }

    private fun resetData() {
        _imageUris.value = emptyList()
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
}
