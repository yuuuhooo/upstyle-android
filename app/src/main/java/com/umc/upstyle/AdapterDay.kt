package com.umc.upstyle


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.util.Calendar
import androidx.recyclerview.widget.RecyclerView
import com.umc.upstyle.databinding.ListItemDayBinding
import java.util.*

class AdapterDay(private val tempMonth: Int, private val dayList: MutableList<Date>) :
    RecyclerView.Adapter<AdapterDay.DayView>() {

    private val ROW = 6

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

        // 클릭 리스너 설정
        holder.binding.itemDayLayout.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "${dayList[position]}", Toast.LENGTH_SHORT)
                .show()
        }

        val currentDate = dayList[position]
        val currentDay = currentDate.date

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
}
