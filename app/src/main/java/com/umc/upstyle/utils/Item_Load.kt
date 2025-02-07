package com.umc.upstyle.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item_load(
    val description: String,
    val imageUrl: String,
    var isSelected: Boolean = false
) : Parcelable