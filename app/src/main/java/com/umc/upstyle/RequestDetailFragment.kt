package com.umc.upstyle

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.upstyle.data.model.CodiResPreview
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var responseAdapter: ResponseAdapter
    private var codiResPreviewList = mutableListOf<CodiResPreview>()


    private var commentCount: Int = 0
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
        commentCount = args.commentCount

        fetchRequestDetails(requestId)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        responseAdapter = ResponseAdapter(codiResPreviewList) { commentId ->
            showBottomSheet(commentId) // 아이템 클릭 시 BottomSheet 호출
        }

        recyclerView.adapter = responseAdapter

//        binding.testButton.setOnClickListener {
//            modalBottomSheet()
//        }

        binding.backButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun fetchRequestDetails(requestId: Int) {
        lifecycleScope.launch {
            try {
                Log.d("RequestDetail", "API 호출 시작: requestId = $requestId")

                val response = withContext(Dispatchers.IO) { requestService.getRequestDetail(requestId) }

                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        Log.d("RequestDetail", "API 응답 성공: ${apiResponse.result}")

                        if (apiResponse.isSuccess) {
                            updateUI(apiResponse.result)

                            // 🚨 Null 체크 추가
                            val responseList = apiResponse.result.codiResPreviewList ?: emptyList()

                            codiResPreviewList.clear()
                            codiResPreviewList.addAll(responseList)

                            Log.d("RequestDetail", "업데이트된 리스트 크기: ${codiResPreviewList.size}")

                            responseAdapter.notifyDataSetChanged()
                        }
                    } ?: Log.e("RequestDetail", "응답 본문이 null")
                } else {
                    Log.e("RequestDetail", "API 응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("RequestDetail", "API 요청 실패: ${e.message}")
            }
        }
    }




    private fun updateUI(requestDetail: RequestDetailResponse) {
        binding.tvUsername.text = requestDetail.user.nickname
        binding.tvTitle.text = requestDetail.title
        binding.tvText.text = requestDetail.body
        binding.tvCommentCount.text = commentCount.toString()
        Glide.with(this)
            .load(requestDetail.imageUrl)
            .into(binding.ivImage)

    }

    private fun showBottomSheet(commentId: Int) {
        // 클릭된 댓글 id를 Bottom Sheet에 넘겨서 API 호출
        val bottomSheetViewFragment = BottomSheetViewFragment.newInstance(commentId)
        bottomSheetViewFragment.show(parentFragmentManager, bottomSheetViewFragment.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun modalBottomSheet() {
        val modal = CodiBottomSheetFragment()
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(parentFragmentManager, CodiBottomSheetFragment.TAG)
    }
}

