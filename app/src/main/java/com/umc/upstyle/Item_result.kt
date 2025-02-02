import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item_result(
    val description: String,
    val imageUrl: String
) : Parcelable
