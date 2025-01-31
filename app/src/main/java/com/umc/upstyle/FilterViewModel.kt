package com.umc.upstyle

import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    var selectedCategory: String? = null
    var selectedSubCategory: String? = null
    var selectedFitSize: String? = null
    var selectedColor: String? = null

    // 필터를 초기화하는 메서드
    fun resetFilters() {
        selectedCategory = null
        selectedSubCategory = null
        selectedFitSize = null
        selectedColor = null
    }
}
