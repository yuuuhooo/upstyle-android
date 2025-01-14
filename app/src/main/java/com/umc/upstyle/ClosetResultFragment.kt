package com.umc.upstyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.upstyle.databinding.FragmentClosetResultBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ClosetResultFragment : Fragment() {

    private var _binding: FragmentClosetResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClosetResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // arguments에서 전달된 데이터 받기
        val where = arguments?.getString("where") ?: "기본값1"
        val param2 = arguments?.getString("param2") ?: "기본값2"
        val param3 = arguments?.getString("param3") ?: "기본값3"

        // 어디서 온 건지 TextView에 데이터 설정 ex. OUTER, TOP
        binding.mainTitleTextView.text = where

        // arguments에서 전달된 리스트 받기
        val colorList = arguments?.getStringArrayList("colorList") ?: arrayListOf()

        // 리스트를 ", "로 구분하여 하나의 문자열로 결합
        val resultText = colorList.joinToString(", ")

        // TextView에 선택된 컬러들 보여주기
        binding.tvSelectedOptions.text = resultText

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}