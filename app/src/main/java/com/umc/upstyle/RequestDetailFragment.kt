package com.umc.upstyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.umc.upstyle.data.model.RequestDetailResponse
import com.umc.upstyle.data.network.RequestService
import com.umc.upstyle.data.network.RetrofitClient
import com.umc.upstyle.databinding.FragmentRequestDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestDetailFragment : Fragment() {
    private var _binding: FragmentRequestDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestService: RequestService
    private val args: RequestDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestDetailBinding.inflate(inflater, container, false)
        requestService = RetrofitClient.createService(RequestService::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestId = args.id // 이전 프래그먼트에서 전달된 voteId

        fetchRequestDetails(requestId)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun fetchRequestDetails(requestId: Int) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { requestService.getRequestDetail(requestId) }
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

    private fun updateUI(requestDetail: RequestDetailResponse) {
        binding.tvUsername.text = requestDetail.user.nickname
        binding.tvTitle.text = requestDetail.title
        binding.tvText.text = requestDetail.body
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

