package com.umc.upstyle

import Item_result
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore.Video.VideoColumns.CATEGORY
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.upstyle.databinding.FragmentLoadItemBinding

class LoadItemFragment : Fragment() {

    private var _binding: FragmentLoadItemBinding? = null
    private val binding get() = _binding!!

    private val args: LoadItemFragmentArgs by navArgs() // Safe Args로 전달된 데이터 받기

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoadItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 카테고리
        val category = args.category

        // 더미 데이터 로드
        val savedItems: List<Item_result> = loadPreviousItems(CATEGORY)



        // 리사이클러뷰 초기화
        val adapter = RecyclerAdapter_Result(savedItems) { clickedItem ->
            Toast.makeText(requireContext(), "클릭된 아이템: $clickedItem", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 돌아가기 버튼 클릭 이벤트
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadPreviousItems(category: String): List<Item_result> {
        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val savedItem = preferences.getString(category, null)

        return if (!savedItem.isNullOrEmpty()) {
            savedItem.split(";").map { item ->
                val parts = item.split(":")
                Item_result(parts[0], parts.getOrElse(1) { "설명 없음" }) // 제목과 이미지 URL
            }
        } else {
            emptyList()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
