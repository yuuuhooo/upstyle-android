package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import androidx.navigation.fragment.findNavController
import com.umc.upstyle.databinding.ActivityMyhomeBinding
import java.util.Calendar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper



class MyHomeFragment : Fragment() {
    private var _binding: ActivityMyhomeBinding? = null
    private val binding get() = _binding!! // Non-nullable binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityMyhomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val monthListManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth()

        binding.calendarCustom.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.calendarCustom)

        // 현재 연도와 월 가져오기
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // MONTH는 0부터 시작하므로 +1 필요

        // TextView에 연도와 월 설정
        binding.calendarMonth.text = "${year}년 ${month}월"

        // ~님의 옷장으로 이동
        binding.closetSection.setOnClickListener {
            findNavController().navigate(R.id.closetFragment)
        }

        // 오늘의 OOTD로 이동
        binding.todayOOTDSection.setOnClickListener {
            findNavController().navigate(R.id.todayOotdFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Binding 객체 해제
    }
}
