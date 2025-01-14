package com.umc.upstyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.upstyle.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {


    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!! // Non-nullable로 사용하기 위한 접근자

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root


    }



    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지: 뷰가 파괴되면 바인딩 해제
        _binding = null
    }

}