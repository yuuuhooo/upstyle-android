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

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
