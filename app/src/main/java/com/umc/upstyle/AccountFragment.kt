package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.upstyle.databinding.FragmentAccountBinding
import com.umc.upstyle.databinding.FragmentSearchBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myProfileBtn.setOnClickListener { findNavController().navigate(R.id.myProfileFragment) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지: 뷰가 파괴되면 바인딩 해제
        _binding = null
    }

}


