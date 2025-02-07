package com.umc.upstyle

import PostAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.upstyle.databinding.FragmentVoteBinding


// 투표 목록 보여주는 프래그먼트 !!!!
// PostRepositoryd에서 데이터 로드함
class VoteFragment : Fragment() {
    private var _binding: FragmentVoteBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentVoteBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val postList = PostRepository.fetchPosts()
            val adapter = PostAdapter(postList) { post ->
                val action = VoteFragmentDirections
                    .actionVoteFragmentToPostDetailFragment(post.id, post.title, post.voteCount)
                findNavController().navigate(action)
            }

            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        // ViewPager2 길이 동적으로 변하게 하는 부분
        override fun onResume() {
            super.onResume()
            binding.recyclerView.requestLayout()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
