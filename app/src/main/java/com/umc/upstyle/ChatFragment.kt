package com.umc.upstyle

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.upstyle.databinding.FragmentChatBinding

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TabPagerAdapter(this)
        adapter.addFragment(VoteFragment())   // 첫 번째 탭: 투표
        adapter.addFragment(RequestFragment()) // 두 번째 탭: 코디 요청
        binding.viewPager.adapter = adapter

        // TabLayout과 ViewPager2를 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "투표"     // 첫 번째 탭
                1 -> tab.text = "코디 요청" // 두 번째 탭
            }
        }.attach()

//        binding.btnWritePost.setOnClickListener { findNavController().navigate(R.id.createVoteFragment) }



        // TabLayout 선택 상태에 따른 버튼 동작 처리
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // 첫 번째 탭(투표) 선택 시
                        binding.btnWritePost.setOnClickListener {
                            // 투표 작성 화면으로 이동
                            findNavController().navigate(R.id.createVoteFragment)
                        }
                    }

                    1 -> {
                        // 두 번째 탭(코디 요청) 선택 시
                        binding.btnWritePost.setOnClickListener {
                            // 코디 요청 화면으로 이동 (예시로 다른 프래그먼트로 이동)
                            findNavController().navigate(R.id.createRequestFragment)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })



        binding.btnWritePost.setOnClickListener {
            // 투표 작성 화면으로 이동
            findNavController().navigate(R.id.createVoteFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}