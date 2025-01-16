package com.umc.upstyle


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onBindViewHolder(holder: DayView, position: Int) {
        // 클릭 리스너 설정
        holder.binding.itemDayLayout.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "${dayList[position]}", Toast.LENGTH_SHORT).show()
        }

        // 날짜 설정
        holder.binding.itemDayText.text = dayList[position].date.toString()

        // 텍스트 색상 설정
        holder.binding.itemDayText.setTextColor(
            when (position % 7) {
                0 -> Color.BLACK
                6 -> Color.BLACK
                else -> Color.BLACK
            }
        )

        // 다른 달의 날짜는 투명도 설정
        if (tempMonth != dayList[position].month) {
            holder.binding.itemDayText.alpha = 0.4f
            holder.binding.itemDayLayout.alpha = 0.4f
        }
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}
