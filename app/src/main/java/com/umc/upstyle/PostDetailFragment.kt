package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.umc.upstyle.data.model.ApiResponse
import com.umc.upstyle.data.model.VoteDetailResponse
import com.umc.upstyle.data.network.RetrofitClient
import com.umc.upstyle.data.network.VoteService
import com.umc.upstyle.databinding.FragmentViewVoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostDetailFragment : Fragment() {
    private var _binding: FragmentViewVoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var voteService: VoteService
    private val args: PostDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewVoteBinding.inflate(inflater, container, false)
        voteService = RetrofitClient.createService(VoteService::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val voteId = args.id // 이전 프래그먼트에서 전달된 voteId

        fetchVoteDetails(voteId)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }

    }

    private fun fetchVoteDetails(voteId: Int) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { voteService.getVoteDetail(voteId) }
                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        if (apiResponse.isSuccess) {
                            updateUI(apiResponse.result)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateUI(voteDetail: VoteDetailResponse) {
        binding.tvUsername.text = voteDetail.user.nickname
        binding.tvTitle.text = voteDetail.title
        binding.tvText.text = voteDetail.body
        Glide.with(this)
            .load(voteDetail.imageUrl)
            .into(binding.ivImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
