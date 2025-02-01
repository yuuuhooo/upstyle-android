package com.umc.upstyle

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.upstyle.databinding.FragmentChatBinding


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

        val tabLayout: TabLayout = binding.tabLayout
        val viewPager: ViewPager2 = binding.viewPager
//
        val adapter = TabPagerAdapter(this)
//        viewPager.adapter = adapter

//        // 탭 이름 설정
//        val tabTitles = arrayOf("투표", "코디 요청")
//
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = tabTitles[position]
//        }.attach()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.tabLayout
//
        // '코디 요청' 탭 가져오기 (Index 확인 필요)
        val tabRequest = tabLayout.getTabAt(1)  // 'tab_request'는 두 번째 탭이므로 index 1

//        // 탭 클릭 이벤트 설정 (예제)
//        tabRequest?.let {
//            it.select()  // 기본적으로 선택되도록 설정 가능
//            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    if (tab == tabRequest) {
//                        // '코디 요청' 탭이 선택되었을 때 실행할 코드
//                    }
//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab?) {}
//                override fun onTabReselected(tab: TabLayout.Tab?) {}
//            })
//        }



        binding.btnWritePost.setOnClickListener {
            findNavController().navigate(R.id.createVoteFragment)
        }
    }

    // Navigation Component를 통한 전환 함수
    private fun navigateToCreateVoteFragment() {
        val action = ChatFragmentDirections.actionChatFragmentToCreateVoteFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}

