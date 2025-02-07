package com.umc.upstyle

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.upstyle.data.network.ApiService
import com.umc.upstyle.databinding.FragmentClosetItemBinding
import com.umc.upstyle.data.model.ClosetCategoryResponse
import java.io.File
import com.umc.upstyle.data.model.ClothPreview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClosetItemFragment : Fragment() {


    private var _binding: FragmentClosetItemBinding? = null
    private val binding get() = _binding!!

    private var categoryId: Long? = null  // API에서 사용할 categoryId
    private var category: String? = null
    private var userId: Long = 1L // 기본 userId 값 (필요 시 수정)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = ClosetItemFragmentArgs.fromBundle(requireArguments())
        category = args.category // Safe Args로 전달된 CATEGORY 값
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentClosetItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // 이전 Fragment로 이동
        }


        // 상단 텍스트 표시
        binding.titleText.text = when (categoryId) {
            1L -> "OUTER"
            2L -> "TOP"
            3L -> "BOTTOM"
            4L -> "SHOES"
            else -> "OTHER"
        }

        // ✅ API 호출로 데이터 불러오기
        fetchClosetItems()
    }

    private fun fetchClosetItems() {
        val apiService = RetrofitClient.createService(ApiService::class.java)

        apiService.getClosetByCategory(userId = 1L, categoryId = categoryId)
            .enqueue(object : Callback<ClosetCategoryResponse> {
                override fun onResponse(
                    call: Call<ClosetCategoryResponse>,
                    response: Response<ClosetCategoryResponse>
                ) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val items = response.body()?.result?.clothPreviewList ?: emptyList()
                        setupRecyclerView(items)
                    } else {
                        binding.titleText.text = "데이터 불러오기 실패"


                    }
                }

                override fun onFailure(call: Call<ClosetCategoryResponse>, t: Throwable) {
                }
            })
    }
        // RecyclerView 설정
        private fun setupRecyclerView(items: List<ClothPreview>) {
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.recyclerView.adapter = RecyclerAdapter_Closet(items)
        }



    private fun loadItemsFromPreferences(): List<Item_closet> {
        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        // 해당 카테고리에 맞는 텍스트와 이미지 경로 가져오기
        val description = preferences.getString(category, "$category 정보 없음")
        val savedImagePath = preferences.getString("SAVED_IMAGE_PATH", null)

        // 기본 샘플 데이터
        val itemClosets = mutableListOf(
            Item_closet("샘플 1", "https://example.com/image1.jpg"),
            Item_closet("샘플 2", "https://example.com/image2.jpg")
        )

        // 저장된 데이터 추가 + 없으면 걍 안뜨게
        if (!savedImagePath.isNullOrEmpty() && !description.isNullOrEmpty() && description != "없음") {
            val file = File(savedImagePath)
            if (file.exists()) {
                val fileUri = Uri.fromFile(file)
                itemClosets.add(0, Item_closet(description, fileUri.toString()))
            }
        }

        return itemClosets
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 뷰 바인딩 해제
    }

    companion object {
        fun newInstance(category: String): ClosetItemFragment {
            val fragment = ClosetItemFragment()
            val args = Bundle()
            args.putString("CATEGORY", category) // 카테고리 전달
            fragment.arguments = args
            return fragment
        }
    }
}