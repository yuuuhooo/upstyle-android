package com.umc.upstyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.umc.upstyle.databinding.FragmentPostDetailBinding

class PostDetailFragment : Fragment() {
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe Args로 전달된 값 받기
        val args = PostDetailFragmentArgs.fromBundle(requireArguments())

        binding.tvPostTitle.text = args.title
        binding.tvVoteCount.text = "${args.voteCount}명 투표"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

