package com.umc.upstyle.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClothesCategoryResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ResultData
) : Parcelable {
    @Parcelize
    data class ResultData(
        val clothPreviewList: List<ClothPreview>,
        val listSize: Int,
        val totalPage: Int,
        val totalElements: Int,
        val isFirst: Boolean,
        val isLast: Boolean
    ) : Parcelable

    @Parcelize
    data class ClothPreview(
        val id: Long,
        val kindId: Long,
        val kindName: String,
        val categoryId: Long,
        val categoryName: String,
        val fitId: Long,
        val fitName: String,
        val colorId: Long,
        val colorName: String,
        val additionalInfo: String?,
        val ootd: Ootd?
    ) : Parcelable {
        @Parcelize
        data class Ootd(
            val id: Long,
            val date: String,
            val imageUrls: List<String>
        ) : Parcelable
    }
}
