import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item_load(
    val categoryName: String, // ✅ 카테고리명
    val fitName: String,      // ✅ 핏 정보
    val colorName: String,    // ✅ 색상 정보
    val imageUrl: String,     // ✅ 이미지 URL
    var isSelected: Boolean = false
) : Parcelable
