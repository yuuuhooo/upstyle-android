package com.umc.upstyle

import PostAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.upstyle.data.repository.PostRepository
import com.umc.upstyle.databinding.FragmentVoteBinding
import com.umc.upstyle.data.model.Post

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

        // 서버에서 데이터 가져오기
        PostRepository.fetchPosts { postList ->
            val adapter = PostAdapter(postList) { post ->
                val action = VoteFragmentDirections
                    .actionVoteFragmentToPostDetailFragment(post.id, post.title, post.totalResponseCount)
                findNavController().navigate(action)
            }

            binding.voteRecyclerView.adapter = adapter
            binding.voteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }


    }

    // ViewPager2 길이 동적으로 변하게 하는 부분
    override fun onResume() {
        super.onResume()
        binding.voteRecyclerView.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
