package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class BookmarkFragment : Fragment() {

    private lateinit var selectedButton: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmark, container, false)

        // 버튼 ID 리스트
        val buttonIds = listOf(
            R.id.btn_all, R.id.btn_outer, R.id.btn_top, R.id.btn_bottom,
            R.id.btn_shoes, R.id.btn_bag, R.id.btn_other
        )

        // 기본 선택 버튼 (ALL)
        selectedButton = view.findViewById(R.id.btn_all)
        selectedButton.setBackgroundResource(R.drawable.bg_circle_yellow) // 기본 선택 배경 적용

        // 각 버튼 클릭 이벤트 설정
        buttonIds.forEach { id ->
            val button = view.findViewById<ConstraintLayout>(id)
            button.setOnClickListener { selectButton(button) }
        }

        return view
    }

    private fun selectButton(button: ConstraintLayout) {
        if (::selectedButton.isInitialized && selectedButton != button) {
            selectedButton.setBackgroundResource(R.drawable.bg_circle_gray) // 이전 버튼 원래 색으로
        }

        // 현재 선택된 버튼을 노란색으로 변경
        button.setBackgroundResource(R.drawable.bg_circle_yellow)
        selectedButton = button
    }
}
