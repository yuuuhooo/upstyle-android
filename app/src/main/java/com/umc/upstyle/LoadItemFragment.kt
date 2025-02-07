package com.umc.upstyle

import com.umc.upstyle.utils.Item_load
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.upstyle.databinding.FragmentLoadItemBinding
import java.io.File

class LoadItemFragment : Fragment() {


    private var _binding: FragmentLoadItemBinding? = null
    private val binding get() = _binding!!

    private var category: String? = null
    private var selectedItem: Item_load? = null // 선택된 아이템 저장



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = LoadItemFragmentArgs.fromBundle(requireArguments())
        category = args.category // Safe Args로 전달된 CATEGORY 값
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentLoadItemBinding.inflate(inflater, container, false)
        return binding.root
    }
    private lateinit var adapter: RecyclerAdapter_Load

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // 이전 Fragment로 이동
        }


        // 상단 텍스트 표시
        binding.itemText.text = when (category) {
            "OUTER" -> "OUTER"
            "TOP" -> "TOP"
            "SHOES" -> "SHOES"
            "BOTTOM" -> "BOTTOM"
            "BAG" -> "BAG"
            else -> "OTHER"
        }

        // RecyclerView 설정
        val items = loadItemsFromPreferences()
        adapter = RecyclerAdapter_Load(items) { item, position ->
            selectedItem = item
            adapter.setSelectedPosition(position)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        binding.btSelect.setOnClickListener {
            if (selectedItem != null) {
                sendSelectedItemToPreviousFragment(selectedItem!!)
            } else {
                Toast.makeText(requireContext(), "아이템을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sendSelectedItemToPreviousFragment(selectedItem: Item_load) {
        val category = this.category ?: return
        findNavController().previousBackStackEntry?.savedStateHandle?.set("SELECTED_ITEM", selectedItem.description)
        findNavController().previousBackStackEntry?.savedStateHandle?.set("SELECTED_ITEM_IMAGE_URL", selectedItem.imageUrl)
        findNavController().previousBackStackEntry?.savedStateHandle?.set("CATEGORY", category) // ✅ CATEGORY 추가
        findNavController().popBackStack()
    }




    private fun loadItemsFromPreferences(): List<Item_load> {
        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        // 해당 카테고리에 맞는 텍스트와 이미지 경로 가져오기
        val description = preferences.getString(category, "$category 정보 없음")
        val savedImagePath = preferences.getString("SAVED_IMAGE_PATH", null)

        // 기본 샘플 데이터
        val itemLoads = mutableListOf(
            Item_load("케로로", "https://image.cine21.com/resize/cine21/still/2005/1121/M0020066_focus52804[W578-].jpg"),
            Item_load("샘플 2", "https://example.com/image2.jpg"),
            Item_load("샘플 1", "https://example.com/image1.jpg"),
            Item_load("샘플 2", "https://example.com/image2.jpg"),
            Item_load("샘플 1", "https://example.com/image1.jpg"),
            Item_load("샘플 2", "https://example.com/image2.jpg"),
            Item_load("샘플 1", "https://example.com/image1.jpg"),
            Item_load("샘플 2", "https://example.com/image2.jpg"),
            Item_load("샘플 1", "https://example.com/image1.jpg"),
            Item_load("샘플 2", "https://example.com/image2.jpg")
        )

        // 저장된 데이터 추가 + 없으면 걍 안뜨게
        if (!savedImagePath.isNullOrEmpty() && !description.isNullOrEmpty() && description != "없음") {
            val file = File(savedImagePath)
            if (file.exists()) {
                val fileUri = Uri.fromFile(file)
                itemLoads.add(0, Item_load(description, fileUri.toString()))
            }
        }

        return itemLoads
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 뷰 바인딩 해제
    }

    /* companion object {
         fun newInstance(category: String): LoadItemFragment {
             val fragment = LoadItemFragment()
             val args = Bundle()
             args.putString("CATEGORY", category) // 카테고리 전달
             fragment.arguments = args
             return fragment
         }
     }*/
}