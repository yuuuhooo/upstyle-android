package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.upstyle.databinding.FragmentChatBinding

class ChatFragment : Fragment(R.layout.fragment_chat), VoteFragmentListener {

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
        val voteFragment = VoteFragment()

        // VoteFragment에 리스너 설정
        voteFragment.setVoteFragmentListener(this)

        adapter.addFragment(voteFragment)   // 첫 번째 탭: 투표
        adapter.addFragment(RequestFragment()) // 두 번째 탭: 코디 요청
        binding.viewPager.adapter = adapter

        // TabLayout과 ViewPager2를 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "투표"     // 첫 번째 탭
                1 -> tab.text = "코디 요청" // 두 번째 탭
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.btnWritePost.setOnClickListener {
                            findNavController().navigate(R.id.createVoteFragment)
                        }
                    }

                    1 -> {
                        binding.btnWritePost.setOnClickListener {
                            findNavController().navigate(R.id.createRequestFragment)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.btnWritePost.setOnClickListener {
            findNavController().navigate(R.id.createVoteFragment)
        }
    }

    // VoteFragment에서 클릭된 데이터 받아서 PostDetailFragment로 이동
    override fun onVoteSelected(postId: Int, postTitle: String, voteCount: Int) {
        val action = ChatFragmentDirections
            .actionChatFragmentToPostDetailFragment(postId, postTitle, voteCount)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
