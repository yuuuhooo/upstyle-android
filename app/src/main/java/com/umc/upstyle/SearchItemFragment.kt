package com.umc.upstyle

import Item_search
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.upstyle.databinding.FragmentSearchItemBinding
import java.io.File

class SearchItemFragment : Fragment() {

    private var _binding: FragmentSearchItemBinding? = null
    private val binding get() = _binding!!

    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Safe Args로 전달된 데이터 수신
        val args = SearchItemFragmentArgs.fromBundle(requireArguments())
        category = args.category // Safe Args로 데이터 수신
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼 클릭 이벤트
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val category = arguments?.getString("category") // 전달된 데이터 수신


        val bundle = Bundle().apply {
            putString("category", category)
        }

        // 컬러 필터링 filterButton
        binding.filterButton.setOnClickListener {
            val action = SearchItemFragmentDirections
                .actionSearchItemFragmentToClosetItemFilterFragment(category ?: "DEFAULT")
            findNavController().navigate(action)
        }



        // 상단 제목 설정
        binding.titleText.text = "$category"


        // RecyclerView 설정
        val items = loadItemsFromPreferences()
        setupRecyclerView(items)
    }

    private fun setupRecyclerView(items: List<Item_search>) {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = RecyclerAdapter_Search(items) { selectedItem ->
            navigateToClosetResultFragment(selectedItem)
        }

    }


    private fun loadItemsFromPreferences(): List<Item_search> {
        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        // 해당 카테고리에 맞는 텍스트와 이미지 경로 가져오기
        val description = preferences.getString(category, "$category 정보 없음")
        val savedImagePath = preferences.getString("SAVED_IMAGE_PATH", null)

        // 기본 샘플 데이터
        val itemSearchs = mutableListOf(
            Item_search("샘플 1", "https://example.com/image1.jpg"),
            Item_search("샘플 2", "https://example.com/image2.jpg")
        )

        // 저장된 데이터 추가
        if (!savedImagePath.isNullOrEmpty() && !description.isNullOrEmpty() && description != "없음") {
            val file = File(savedImagePath)
            if (file.exists()) {
                val fileUri = Uri.fromFile(file)
                itemSearchs.add(0, Item_search(description, fileUri.toString()))
            }
        }

        return itemSearchs
    }

    private fun navigateToClosetResultFragment(item: Item_search) {
        val action = SearchItemFragmentDirections
            .actionSearchItemFragmentToSearchResultFragment(
                imageUrl = item.imageUrl,
                description = item.description
            )
        findNavController().navigate(action)
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 뷰 바인딩 해제
    }

    companion object {
        fun newInstance(category: String): SearchItemFragment {
            val fragment = SearchItemFragment()
            val args = Bundle()
            args.putString("CATEGORY", category)
            fragment.arguments = args
            return fragment
        }
    }
}