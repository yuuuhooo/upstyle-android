package com.umc.upstyle

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.upstyle.databinding.ListItemDayBinding
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterDay(private val tempMonth: Int, private val dayList: MutableList<Date>) :
    RecyclerView.Adapter<AdapterDay.DayView>() {

    private val ROW = 6
    private val ootdMap = mutableMapOf<String, Pair<Int, String>>() // 날짜별 (ootdId, imageUrl) 저장

    init {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        fetchOOTDDataFromAPI(1, year, month) // ✅ Adapter가 생성될 때 한 번만 API 호출
    }

    // ViewHolder에 뷰 바인딩 추가
    inner class DayView(val binding: ListItemDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        // 뷰 바인딩을 사용하여 뷰를 인플레이트
        val binding = ListItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayView(binding)
    }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // MONTH는 0부터 시작하므로 1을 더함
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    override fun onBindViewHolder(holder: DayView, position: Int) {

        val currentDate = dayList[position]
        val currentDay = currentDate.date

        // dateKey를 통해 해당 날짜에 이미지 로드
        val formattedMonth = String.format("%02d", currentDate.month + 1)
        val formattedDay = String.format("%02d", currentDay)
        val dateKey = "${currentDate.year + 1900}-$formattedMonth-$formattedDay"

        // 클릭 리스너 설정
        holder.binding.itemDayLayout.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "${dayList[position]}", Toast.LENGTH_SHORT)
                .show()
            val context = holder.binding.root.context
            val ootdId = getOOTDIdByDate(dateKey)

            Log.d("IMAGE_DEBUG", "Clicked Date: $dateKey, OOTD ID: $ootdId")

            if (ootdId != null) {
                val bundle = Bundle().apply { putInt("SELECTED_OOTD_ID", ootdId) }
                val navController = Navigation.findNavController(holder.binding.root)
                navController.navigate(R.id.ootdDetailFragment, bundle)
            } else {
                Toast.makeText(context, "해당 날짜에 대한 OOTD가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // OOTD 이미지 적용
        val imageUrl = ootdMap[dateKey]?.second
        if (imageUrl != null) {
            holder.binding.itemDayImage.visibility = View.VISIBLE
            loadImage(holder.binding.itemDayImage, imageUrl.toString())
            Log.d("IMAGE_DEBUG", "Image Loaded for Date: $dateKey, URL: $imageUrl")
        } else {
            holder.binding.itemDayImage.visibility = View.GONE
            Log.d("IMAGE_DEBUG", "No Image for Date: $dateKey")
        }

        // 현재 아이템의 달과 비교
        val isOtherMonth = tempMonth != currentDate.month

        // 일주일이 모두 다음 달인지 확인
        val isNextMonthWeek = isNextMonthWeek(position)

        if (isNextMonthWeek) {
            // 일주일이 전부 다음 달일 경우, layout과 text를 숨김
            holder.binding.itemDayLayout.visibility = View.GONE
        } else {
            // 기본 설정
            holder.binding.itemDayLayout.visibility = View.VISIBLE

            // 날짜 설정
            holder.binding.itemDayText.text = currentDay.toString()

            // 오늘 날짜 색상 처리
            if (currentDay == day && currentDate.month == calendar.get(Calendar.MONTH)) {
                holder.binding.itemDayLayout.setBackgroundResource(R.drawable.bg_sub_color_1)
            } else {
                holder.binding.itemDayLayout.setBackgroundResource(R.drawable.bg_other_day)
            }

            // 텍스트 색상 설정
            holder.binding.itemDayText.setTextColor(
                when (position % 7) {
                    0 -> Color.BLACK
                    6 -> Color.BLACK
                    else -> Color.BLACK
                }
            )

            // 다른 달의 날짜는 투명도 설정
            if (isOtherMonth) {
                holder.binding.itemDayText.alpha = 0.4f
            } else {
                holder.binding.itemDayText.alpha = 1f
            }

            Log.d("IMAGE_DEBUG", "Date: $dateKey, ImageView Visibility: ${holder.binding.itemDayImage.visibility}")
        }
    }

    /**
     * 특정 위치의 일주일이 모두 다음 달인지 확인하는 함수
     */
    private fun isNextMonthWeek(position: Int): Boolean {
        val start = (position / 7) * 7 // 주의 시작 인덱스
        val end = start + 6            // 주의 끝 인덱스

        // 주에 포함된 모든 날짜가 다음 달인지 확인
        for (i in start..end) {
            if (i < dayList.size && dayList[i].month == tempMonth) {
                return false // 현재 달에 속한 날짜가 있으면 false
            }
        }
        return true // 모두 다음 달에 속하면 true
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }

    fun fetchOOTDDataFromAPI(userId: Int, year: Int, month: Int) {
        val apiService = RetrofitClient.createService(OotdApiService::class.java)
        apiService.getOOTDCalendar(userId, year, month).enqueue(object : Callback<ApiResponse<OOTDCalendar>> {
            override fun onResponse(call: Call<ApiResponse<OOTDCalendar>>, response: Response<ApiResponse<OOTDCalendar>>) {
                if (response.isSuccessful) {
                    val ootdCalendar = response.body()?.result
                    if (ootdCalendar != null) {
                        updateOOTDData(ootdCalendar.ootdPreviewList) // API 데이터를 `ootdMap`에 저장
                        Log.d("API_SUCCESS", "OOTD Data Loaded from API")
                    } else {
                        Log.e("API_ERROR", "Response body is null")
                    }
                } else {
                    Log.e("API_ERROR", "Failed to fetch OOTD Data: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<OOTDCalendar>>, t: Throwable) {
                Log.e("API_ERROR", "API Request failed: ${t.message}")
            }
        })
    }


    // OOTD 데이터를 저장하는 함수
    fun updateOOTDData(ootdList: List<OOTDPreview>) {
        ootdMap.clear()
        for (ootd in ootdList) {
            Log.d("API_SUCCESS", "Adding Data - Date: ${ootd.date}, ID: ${ootd.id}, ImageURL: ${ootd.imageUrl}")
            ootdMap[ootd.date] = Pair(ootd.id, ootd.imageUrl)
        }

        Log.d("API_SUCCESS", "Final OOTD Map: $ootdMap")
        notifyDataSetChanged()
    }

    // 날짜를 기반으로 OOTD ID 가져오는 함수
    private fun getOOTDIdByDate(dateKey: String): Int? {
        return ootdMap[dateKey]?.first // 날짜를 기반으로 OOTD ID 반환
    }

    // Glide를 이용한 이미지 로드
    private fun loadImage(imageView: ImageView, url: String) {
        Log.d("GLIDE_DEBUG", "Loading Image from URL: $url")
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }
}