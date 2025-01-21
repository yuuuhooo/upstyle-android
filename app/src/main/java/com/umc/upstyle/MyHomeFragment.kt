package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.umc.upstyle.databinding.ActivityMyhomeBinding
import java.util.Calendar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import java.util.Date


class MyHomeFragment : Fragment() {
    private var _binding: ActivityMyhomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var monthListManager: LinearLayoutManager
    private val center = Int.MAX_VALUE / 2 // 중앙 위치 값 (Adapter에서 사용한 값)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityMyhomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰 설정
        monthListManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth()

        binding.calendarCustom.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(center) // 중앙으로 스크롤 초기화
        }

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.calendarCustom)

        // 초기 년/월 설정
        updateYearMonth()

        // 스크롤 이벤트 감지
        binding.calendarCustom.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 스크롤이 멈췄을 때
                    updateYearMonth()
                }
            }
        })

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // MONTH는 0부터 시작하므로 1을 더함

        binding.calendarMonth.text = "${year}년 ${month}월"

        // 섹션 이동 버튼 리스너
        binding.closetSection.setOnClickListener {
            findNavController().navigate(R.id.closetFragment)
        }
        binding.todayOOTDSection.setOnClickListener {
            findNavController().navigate(R.id.todayOotdFragment)
        }
    }

    private fun updateYearMonth() {
        // RecyclerView에서 현재 중앙에 위치한 아이템의 위치를 가져옴
        val currentPosition = monthListManager.findFirstVisibleItemPosition()

        // 위치가 유효하지 않거나 첫 번째 위치일 경우 예외 처리
        if (currentPosition == RecyclerView.NO_POSITION || currentPosition == 0) return

        // 중앙 위치를 기준으로 날짜를 계산
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1) // 매달의 첫 번째 날로 설정
            add(Calendar.MONTH, currentPosition - center) // 중앙 위치로부터 몇 번째 월인지 계산
        }

        // 계산된 년/월을 UI에 반영
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // MONTH는 0부터 시작하므로 1을 더함
        binding.calendarMonth.text = "${year}년 ${month}월"

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
