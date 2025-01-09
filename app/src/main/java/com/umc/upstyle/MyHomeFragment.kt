package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.util.Calendar

class MyHomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_myhome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 연도와 월 가져오기
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // MONTH는 0부터 시작하므로 +1 필요

        // TextView에 연도와 월 설정
        val calendarMonthTextView = view.findViewById<TextView>(R.id.calendar_month)
        calendarMonthTextView.text = "${year}년 ${month}월"

        // 버튼 클릭 이벤트 설정
        val todayOOTDSection = view.findViewById<LinearLayout>(R.id.todayOOTDSection)
        todayOOTDSection.setOnClickListener {
            // TodayOotdFragment로 이동
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, TodayOotdFragment())
            transaction.addToBackStack(null) // 뒤로 가기 버튼으로 돌아가기 가능
            transaction.commit()
        }
    }
}
